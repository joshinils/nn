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
			
			int[] h={ 1 };
			NN netz =new NN(2, h, 3);
			
			double[] train_input= {1,1};
			double[] train_output= {1,1,1};
			
			netz.registerTrainingData(Matrix.makeVec(train_input), Matrix.makeVec(train_output));
			
			netz.trainEpoch(10);
			
			int resolution=10;
			double scale=20;// distance in coords from -x to x
			
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
				}
				System.out.println(i/(double)pixel.length*100+"%");
			}
			WritableImage img=root.snapshot(null, null);
			File f=new File(netz.parameters()+" "+System.currentTimeMillis()+" "+Integer.toString(img.hashCode())+".png");
			ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", f );
			System.out.println("done drawing!");
			System.exit(0); // leave and dont gunk up ram

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














