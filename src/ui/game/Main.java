package ui.game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	FXMLLoader loader;
	
    @Override
    public void start(Stage primaryStage) throws Exception{
    	Parent root = FXMLLoader.load(getClass().getResource("risk.fxml"));
        primaryStage.setTitle("Risk");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }
    
    public FXMLLoader getLoader(){
    	return loader;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
