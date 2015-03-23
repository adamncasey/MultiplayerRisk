package ui.game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	FXMLLoader loader;

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("Game.fxml"));
		primaryStage.setTitle("Risk");

		Scene newScene = new Scene(root, 1000, 800);
		newScene.getStylesheets().add("http://fonts.googleapis.com/css?family=Open+Sans:400,600");

		primaryStage.setScene(newScene);
		primaryStage.show();

	}

	public FXMLLoader getLoader() {
		return loader;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
