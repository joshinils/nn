package application;

import java.io.File;
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
			
			int[] h={ 100, 100, 10 };
			NN netz =new NN(2, h, 3);
			
			double[] train_input= new double[2];
			double[] train_output= new double[3];
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
			
/*			for (Double i = 0.; i < 2*Math.PI; i+=Math.PI/2)
			{
				train_input[0]=15*Math.cos(i);
				train_input[1]=15*Math.sin(i);
				train_output[0]=0;
				train_output[1]=0;
				train_output[2]=0;
				netz.registerTrainingData(Matrix.makeVec(train_input), Matrix.makeVec(train_output));
			}
*/

//			System.out.println("vor training \n"+netz);
			int training_epochs=100000;
			double min_error=0.01;
			for (int r = 0; r < training_epochs; r++)
			{
				netz.trainEpoch(6);
				if(r%1000==0)
				{
					System.out.println("training: "+r/(double)training_epochs*100+"%");
					Matrix training_error=netz.trainingError();					
					System.out.println("error: \n"+Matrix.transpose(training_error)+"\n");
					if(training_error.max() < min_error)
					{
						System.out.println("stopping training early after "+r+" epochs");
						break; // stop training early
					}
				}
			}
			System.out.println("completed training of "+ training_epochs+" epochs");
			
//			System.out.println("\nnach training \n"+netz);

			
			int resolution=1;
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
					Color c= new Color(erg.get(0, 0), erg.get(1, 0), erg.get(2, 0), 1);// erg.get(3, 0));
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
}














