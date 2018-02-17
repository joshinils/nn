package application;

public class NN
{
	Matrix[] weight;
	Matrix[] bias;
	
	public double w_rand_min;
	public double w_rand_max;
	public double b_rand_min;
	public double b_rand_max;
	
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
		w_rand_min=-2;
		w_rand_max=2;
		b_rand_min=-1;
		b_rand_max=1;
		
		weight[0]=Matrix.random(n_layer[0], n_input, w_rand_min, w_rand_max);
		bias[0]= Matrix.random(n_layer[0], 1, b_rand_min, b_rand_max);
		for (int i = 1; i < n_layer.length; i++)
		{
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
}
























