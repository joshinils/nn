package application;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class NN implements Serializable
{
	private static final long serialVersionUID = -9043404844076109142L;
	private Matrix[] weight;
	private Matrix[] bias;
	
	private ArrayList<Matrix[]> training_data;
	
	public double w_rand_min;
	public double w_rand_max;
	public double b_rand_min;
	public double b_rand_max;
	
	private double learning_rate;
	
	private static int[] thisRestrictionIsStupid(int[] a, int b)
	{
		int[] foo=new int[a.length+1];
		for (int i = 0; i < a.length; i++)
			foo[i]=a[i];
		foo[foo.length-1]=b;
		return foo;
	}
	
	public NN(int n_input, int[] n_layer, int n_output)
	{
		this(n_input, thisRestrictionIsStupid(n_layer, n_output));
	}

	public NN(int n_input, int[] n_layer)
	{
		weight=new Matrix[n_layer.length];
		bias=new Matrix[n_layer.length];
		w_rand_min=-3;
		w_rand_max=3;
		b_rand_min=-3;
		b_rand_max=3;
		
		learning_rate=.9;
		
		weight[0]=Matrix.random(n_layer[0], n_input, w_rand_min, w_rand_max);
		bias[0]= Matrix.random(n_layer[0], 1, b_rand_min, b_rand_max);
		for (int i = 1; i < n_layer.length; i++)
		{
			w_rand_min*=1.1;
			w_rand_max*=1.1;
			b_rand_min/=1.1;
			b_rand_max/=1.1;
			
			weight[i]=Matrix.random(n_layer[i], n_layer[i-1], w_rand_min, w_rand_max);
			bias[i]=Matrix.random(n_layer[i], 1, b_rand_min, b_rand_max);
		}
	}

	private static Matrix sigmoid(Matrix m)
	{
		Matrix erg=new Matrix(m.rows(), m.collumns());
		for (int i = 0; i < m.rows(); i++)
			for (int j = 0; j < m.collumns(); j++)
				erg.set(i, j, sigmoid(m.get(i, j)) );
		return erg;
	}
	
	private static double sigmoid(double d)
	{
		return 1. / ( 1. + Math.exp(-d) );
	}
	
	public void registerTrainingData(Matrix input, Matrix output) throws Exception
	{
		if(training_data==null)
			training_data=new ArrayList<Matrix[]>();
		if(input.collumns()!=1)
			throw(new Exception("NN::registerTrainingData input muss ein vektor sein!"));
		if(output.collumns()!=1)
			throw(new Exception("NN::registerTrainingData output muss ein vektor sein!"));
		Matrix[] n = {input, output};
		training_data.add(n);
	}
	
	public void trainEpoch(int batchSize) throws Exception
	{
		if(training_data==null)
			throw(new Exception("NN::trainEpoch no training data!"));
		if(batchSize<=0)
			batchSize=1;
		if(batchSize>training_data.size())
			batchSize=training_data.size();
		
		@SuppressWarnings("unchecked")
		ArrayList<Matrix[]> leftToTrain = (ArrayList<Matrix[]>) training_data.clone();
		// shuffle randomly
		Collections.shuffle(leftToTrain);
//		leftToTrain.sort(new Comparator<Matrix[]>()
//		{	@Override public int compare(Matrix[] o1, Matrix[] o2) { if(Math.random()>.5) return 1; return -1;}		});
		
		while (!leftToTrain.isEmpty())
		{			
			// do one batch
			for (int i = 0; i < batchSize && !leftToTrain.isEmpty(); i++)
			{
				Matrix[] delta_weight=new Matrix[weight.length];
				Matrix[] delta_bias=new Matrix[bias.length];
				for (int j = 0; j < delta_weight.length; j++)
				{
					delta_weight[j]=new Matrix(weight[j].rows(), weight[j].collumns());
					delta_bias[j]=new Matrix(bias[j].rows(), 1);
				}

//				System.out.println(this);
//				System.out.println();

				Matrix activation[]=new Matrix[weight.length];//Layers + output
				activation[0]=sigmoid(Matrix.add( Matrix.mult(weight[0], leftToTrain.get(0)[0]), bias[0]) );
//				System.out.println("activation[0]\n"+activation[0]);
				for (int j = 1; j < weight.length; j++)
				{
					activation[j] = sigmoid(Matrix.add( Matrix.mult(weight[j], activation[j-1]), bias[j]) );
//					System.out.println("activation["+(j)+"]\n"+activation[j]);
				}
				
				Matrix error[]=new Matrix[activation.length];
				error[error.length-1]=Matrix.scale(Matrix.subtract(leftToTrain.get(0)[1], activation[activation.length -1]), learning_rate);//prediction - computed
				
//				System.out.println("Leror["+(error.length-1)+"]\n"+error[error.length-1]);
				
				// calculate error backwards
				for (int e = error.length-2; e >=0 ; e--)
				{
//					System.out.println("computing error nr:"+e);
					error[e]=Matrix.scale(Matrix.mult( Matrix.transpose(weight[e+1]), error[e+1]), learning_rate);
//					System.out.println("error["+e+"]\n"+error[e]);
				}
//				System.out.println("\ncomputed errors:");
//for (int j = 0; j < error.length; j++)
//{	System.out.println("error["+j+"]\n"+error[j]);	}

//System.out.println();

				for (int j = 0; j < delta_weight.length; j++)
				{
					Matrix new_delta_bias=
						Matrix.hadamard(
							error[j], Matrix.hadamard(
								activation[j], Matrix.subtract(
									new Matrix(activation[j].rows(),1,1), activation[j]
								)
							)
						);
					Matrix new_delta_weight;
					if(j>0)
						new_delta_weight=Matrix.mult(new_delta_bias, Matrix.transpose(activation[j-1]));
					else
						new_delta_weight=Matrix.mult(new_delta_bias, Matrix.transpose(leftToTrain.get(0)[0]));
					
//					System.out.println("new_delta_bias "+j+" \n"+new_delta_bias);
//					System.out.println("new_delta_weight "+j+" \n"+new_delta_weight);
					
					delta_bias[j]=Matrix.add(delta_bias[j], new_delta_bias);
					delta_weight[j]=Matrix.add(delta_weight[j], new_delta_weight);
					
//					System.out.println("delta_bias["+j+"]\n"+delta_bias[j]);
//					System.out.println("delta_weight["+j+"]\n"+delta_weight[j]);
//					System.out.println();
				}
				
				for (int j = 0; j < weight.length; j++)
				{
					weight[j]=Matrix.add(weight[j], delta_weight[j]);
					bias[j]=Matrix.add(bias[j], delta_bias[j]);
				}

				leftToTrain.remove(0);
			}
		}
//		System.out.println(this);
	}

	public Matrix think(Matrix input) throws Exception
	{
//		System.out.println();
		Matrix erg=Matrix.clone(input);
		for (int i = 0; i < weight.length; i++)
		{
/*/		System.out.println("erg:\n"+erg);
			System.out.println("weight["+i+"]:\n"+weight[i]);
			System.out.println("bias["+i+"]:\n"+bias[i]);
			System.out.println("mult:\n"+Matrix.mult(weight[i], erg));
			System.out.println("add:\n"+Matrix.add( Matrix.mult(weight[i], erg), bias[i])); //*/
			erg = sigmoid(Matrix.add( Matrix.mult(weight[i], erg), bias[i]) ); // h <- s( w_i * h + b_i )
//			System.out.println();
		}
//		System.out.println("final erg:"+erg);
		return erg;
	}
	
	public Matrix trainingError() throws Exception
	{
		Matrix max_err=new Matrix(training_data.get(0)[1].rows(), 1);
		Matrix two_err=new Matrix(training_data.get(0)[1].rows(), 1);
		Matrix avg_err=new Matrix(training_data.get(0)[1].rows(), 1);
		for (Matrix[] data : training_data)
		{
			Matrix erg=think(data[0]);
			Matrix out_error=Matrix.subtract(erg, data[1]);
			for (int i = 0; i < out_error.rows(); i++)
			{
				if(out_error.get(i, 0)<0)
					out_error.set(i, 0, -out_error.get(i, 0));
				if(out_error.get(i, 0)>max_err.get(i, 0))
					max_err.set(i, 0, out_error.get(i, 0));
			}
			avg_err=Matrix.add(avg_err, out_error);
			two_err=Matrix.add((out_error).apply(x->x*x), two_err);
		}
		two_err=(two_err).apply(x->Math.sqrt(x));
		avg_err=(avg_err).apply(x->x/training_data.size());
		Matrix s=new Matrix(1, 3);
		s.set(0, 0, 1);
		Matrix erg=Matrix.mult(max_err, s);

		s.set(0, 0, 0);
		s.set(0, 1, 1);
		erg=Matrix.add(erg, Matrix.mult(two_err, s));
		
		s.set(0, 1, 0);
		s.set(0, 2, 1);
		erg=Matrix.add(erg, Matrix.mult(avg_err, s));
		return erg;
	}

	public double getLearning_rate()
	{
		return learning_rate;
	}

	public void setLearning_rate(double learning_rate)
	{
		this.learning_rate = learning_rate;
	}

	@Override
	public String toString()
	{
		String w="\n";
		for (int i = 0; i < weight.length; i++)
			w+=i+":\n"+weight[i]+"\n";
		String b="\n";
		for (int i = 0; i < bias.length; i++)
			b+=i+":\n"+bias[i]+"\n";
		
		return "NN [weights=" + w + ",\nbias=" + b + "]";
	}

	public String parameters()
	{
		String erg="{"+weight[0].collumns()+",";
		for (int i = 0; i < weight.length-1; i++)
			erg+=" "+weight[i].rows();
		return erg+", "+weight[weight.length -1].rows()+"}";
	}

	public int trainingDataAmount()
	{
		return training_data.size();
	}
}
























