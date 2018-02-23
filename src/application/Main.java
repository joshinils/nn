package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,800,800);
//			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
			int[] h={ 200 };
			
			
			int out_amt=1;
			NN netz =new NN(2, h, out_amt);
			
			double[] train_input= new double[2];
			double[] train_output= new double[out_amt];
/*			train_input[0]= 1;
			train_input[1]= 1;
			train_output[0]=0;
			train_output[1]=0;
			train_output[2]=0;
			netz.registerTrainingData(Matrix.makeVec(train_input), Matrix.makeVec(train_output));
			train_input[0]= 1;
			train_input[1]=-1;
			train_output[0]=1;
			train_output[1]=1;
			train_output[2]=1;
			netz.registerTrainingData(Matrix.makeVec(train_input), Matrix.makeVec(train_output));
			train_input[0]=-1;
			train_input[1]= 1;
			train_output[0]=1;
			train_output[1]=1;
			train_output[2]=1;
			netz.registerTrainingData(Matrix.makeVec(train_input), Matrix.makeVec(train_output));
			train_input[0]=-1;
			train_input[1]=-1;
			train_output[0]=0;
			train_output[1]=0;
			train_output[2]=0;
			netz.registerTrainingData(Matrix.makeVec(train_input), Matrix.makeVec(train_output));
*/	
/*	
			train_input[0]= 10;
			train_input[1]= 10;
			train_output[0]=0;
			train_output[1]=0;
			train_output[2]=0;
			netz.registerTrainingData(Matrix.makeVec(train_input), Matrix.makeVec(train_output));
			
			train_input[0]= 10;
			train_input[1]= 0;
			train_output[0]=1;
			train_output[1]=0;
			train_output[2]=0;
			netz.registerTrainingData(Matrix.makeVec(train_input), Matrix.makeVec(train_output));

			train_input[0]= 10;
			train_input[1]=-10;
			train_output[0]=0;
			train_output[1]=1;
			train_output[2]=0;
			netz.registerTrainingData(Matrix.makeVec(train_input), Matrix.makeVec(train_output));

			train_input[0]=0;
			train_input[1]=10;
			train_output[0]=1;
			train_output[1]=1;
			train_output[2]=0;
			netz.registerTrainingData(Matrix.makeVec(train_input), Matrix.makeVec(train_output));

			train_input[0]=0;
			train_input[1]=-10;
			train_output[0]=0;
			train_output[1]=0;
			train_output[2]=1;
			netz.registerTrainingData(Matrix.makeVec(train_input), Matrix.makeVec(train_output));

			train_input[0]=-10;
			train_input[1]=10;
			train_output[0]=1;
			train_output[1]=0;
			train_output[2]=1;
			netz.registerTrainingData(Matrix.makeVec(train_input), Matrix.makeVec(train_output));

			train_input[0]=-10;
			train_input[1]= 0;
			train_output[0]=0;
			train_output[1]=1;
			train_output[2]=1;
			netz.registerTrainingData(Matrix.makeVec(train_input), Matrix.makeVec(train_output));

			train_input[0]=-10;
			train_input[1]= -10;
			train_output[0]=1;
			train_output[1]=1;
			train_output[2]=1;
			netz.registerTrainingData(Matrix.makeVec(train_input), Matrix.makeVec(train_output));
			
/*			
			for (Double i = 0.; i < 2*Math.PI; i+=Math.PI/100)
			{
				train_input[0]=15*Math.cos(i);
				train_input[1]=15*Math.sin(i);
				Color hsb=Color.hsb(i/(Math.PI)*180, 1, 1);
				train_output[0]=hsb.getRed();
				train_output[1]=hsb.getGreen();
				train_output[2]=hsb.getBlue();
				System.out.println(hsb.getRed()+" "+hsb.getGreen()+" "+hsb.getBlue());
				netz.registerTrainingData(Matrix.makeVec(train_input), Matrix.makeVec(train_output));
			}
			System.out.println(netz.trainingDataAmount());
//*/

			// generate chessboard
			int chessSize=5;
			for (int i = 0; i < chessSize; i++)
			{
				for (int j = 0; j < chessSize; j++)
				{
					train_input[0]=i+.5;
					train_input[1]=j+.5;
					double c=0;
					
					if(i%2==0)
						c++;
					if(j%2==0)
						c++;
					c%=2;
					
					train_output[0]=c;
//					train_output[1]=c;
//					train_output[2]=c;
					netz.registerTrainingData(Matrix.makeVec(train_input), Matrix.makeVec(train_output));
				}
			}
			
//			System.out.println("vor training \n"+netz);
			int training_epochs=20000;
			long trained_epochs=0;
			double min_error=0.01;
			Matrix first=new Matrix(3, 1, 1);
			first.set(0, 0, 1);
			netz.setLearning_rate(2);
			double lernRate=netz.getLearning_rate();
			double einsDurchlr=1./lernRate;
			int batchSize=(int)(netz.trainingDataAmount()/4.0);
			if (batchSize<=0)
				batchSize=1;
			
			for (int r = 0; r < training_epochs; r++)
			{
				int epoch_train_cluster=(int)Math.ceil(einsDurchlr)*3+10;
				System.out.println("epochs: "+epoch_train_cluster+" ; trained epochs: "+trained_epochs);
				for (int it = 0; it < epoch_train_cluster; it++)
				{
					netz.trainEpoch(batchSize);
				}
				trained_epochs+=epoch_train_cluster;
				
				System.out.println("training: "+r/(double)training_epochs*100+"%");
				Matrix training_error=netz.trainingError();					
				System.out.println("error: \n"+Matrix.transpose(training_error));
				double m=training_error.max();
				if( m < min_error)
				{
					System.out.println("stopping training early after "+r+" epoch rounds");
					break; // stop training early
				}
				else
				{
					lernRate=m *.2;
					einsDurchlr=1./lernRate;
					netz.setLearning_rate(lernRate);
					System.out.println("Max E: "+m+" Lernrate: "+lernRate);
				}
				System.out.println();
			}
			System.out.println("completed training of "+ training_epochs+" epochs");
			
			
			writeToFile(new File("latestNet "+System.currentTimeMillis()+".nn"), netz);
			
//			System.out.println("\nnach training \n"+netz);
			
			int resolution=3;
			double scale=15;// distance in coords from -x to x
			
			int pixel_amount=(int) (root.getHeight()/resolution);
			
			Rectangle[][] pixel=new Rectangle[pixel_amount][pixel_amount];
			
			System.out.println("\nDrawing");
			for (int i = 0; i < pixel.length; i++)
			{
				for (int j = 0; j < pixel[0].length; j++)
				{
					pixel[i][j]=new Rectangle(i*resolution, j*resolution, resolution, resolution);
					root.getChildren().add(pixel[i][j]);
					double[] v= {(i-pixel_amount/2)/(double)pixel.length, (j-pixel_amount/2)/(double)pixel[0].length};
//					System.out.println(scale*v[0]+" "+scale*v[1]);
//					System.out.println("v:"+Matrix.scale(Matrix.makeVec(v),scale));
					Matrix erg=netz.think(Matrix.scale(Matrix.makeVec(v),scale));
					Color c= new Color(erg.get(0, 0), erg.get(0, 0), erg.get(0, 0), 1);// erg.get(3, 0));
					pixel[i][j].setFill(c);
					
					if((i*pixel.length+j)%10000==0)
						System.out.println("drawing: "+(i*pixel.length+j)/(double)(pixel.length*pixel.length)*100+"%");
				}
			}
			
			WritableImage img=root.snapshot(null, null);
			File f=new File(netz.parameters()+" "+System.currentTimeMillis()+" "+Integer.toString(img.hashCode())+".png");
			ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", f );
			System.out.println("done drawing!");
//			System.exit(0); // leave and dont gunk up ram

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
/*
		Matrix m=new Matrix(3, 2, 0.1);
		Matrix n=new Matrix(2, 3, -0.1);
		m.set(0, 0, 1);
		m.set(0, 1, 2);
		m.set(1, 0, 3);
		m.set(1, 1, 4);
		n.set(0, 0, 5);
		n.set(0, 1, 6);
		n.set(1, 0, 7);
		n.set(1, 1, 8);
		System.out.println("m :\n"+m);
		System.out.println("n :\n"+n);
		System.out.println("m*n :\n"+Matrix.mult(m, n));
		System.out.println("m^T :\n"+Matrix.transpose(m));
		System.out.println("n^T :\n"+Matrix.transpose(n));
		System.out.println("m^T*n^T :\n"+Matrix.mult(Matrix.transpose(m), Matrix.transpose(n)));
		
		System.out.println(Matrix.scale(Matrix.eye(3, 4), 2));
		
		System.out.println("clone test");
		
		Matrix c=Matrix.clone(m);
		c=Matrix.scale(c, 5);
		System.out.println("m:\n"+m);
		System.out.println("c:\n"+c);
*/
				
		launch(args);
		System.exit(0); // leave and dont gunk up ram
	}
	
	public static void writeToFile(File path, Object data)
	{
	    try(ObjectOutputStream write= new ObjectOutputStream (new FileOutputStream(path)))
	    {
	        write.writeObject(data);
	    }
	    catch(NotSerializableException nse)
	    {
	        //do something
	    	System.out.println("NotSerializableException");
	    }
	    catch(IOException eio)
	    {
	    	System.out.println("IOException");
	        //do something
	    }
	}
	
	public static Object readFromFile(File path)
	{
	    Object data = null;

	    try(ObjectInputStream inFile = new ObjectInputStream(new FileInputStream(path)))
	    {
	        data = inFile.readObject();
	        return data;
	    }
	    catch(ClassNotFoundException cnfe)
	    {
	    	System.out.println("ClassNotFoundException");
	        //do something
	    }
	    catch(FileNotFoundException fnfe)
	    {
	    	System.out.println("FileNotFoundException");
	        //do something
	    }
	    catch(IOException e)
	    {
	    	System.out.println("IOException");
	        //do something
	    }
	    return data;
	}   
}














