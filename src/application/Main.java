package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
//			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
			
			
			
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
