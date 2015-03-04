package ui;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ui.creategame.CreateGameController;
import ui.directconnect.DirectConnectController;
import ui.lobby.host.LobbyHostController;
import ui.menu.*;

public class Main extends Application {

	private Stage stage;
    private final double MINIMUM_WINDOW_WIDTH = 500;
    private final double MINIMUM_WINDOW_HEIGHT = 600;
    
    private final double HEIGHT = 600;
    private final double WIDTH = 1000;


    public static void main(String[] args) {
        Application.launch(Main.class, (java.lang.String[])null);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            stage = primaryStage;
            stage.setTitle("Risk");
            stage.setMinWidth(MINIMUM_WINDOW_WIDTH);
            stage.setMinHeight(MINIMUM_WINDOW_HEIGHT);
            gotoMenu();
            primaryStage.show();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void gotoMenu() {
        try {
            MenuController menu = (MenuController) replaceSceneContent("menu/menu.fxml", WIDTH, HEIGHT);
            stage.setResizable(false);
            menu.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    
    public void gotoCreateGame() {
        try {
        	CreateGameController lobby = (CreateGameController) replaceSceneContent("creategame/creategame.fxml", WIDTH, HEIGHT);
        	stage.setResizable(true);
            lobby.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void gotoLobbyHost() {
        try {
        	LobbyHostController lobby = (LobbyHostController) replaceSceneContent("lobby/lobby.fxml", WIDTH, HEIGHT);
        	stage.setResizable(true);
            lobby.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void gotoLobbyFinder() {
        try {
//            LobbyFinderController lobby = (LobbyFinderController) replaceSceneContent("lobbyfinder.fxml");
//            lobby.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void gotoDirectConnect() {
        try {
        	DirectConnectController lobby = (DirectConnectController) replaceSceneContent("directconnect/directconnect.fxml", WIDTH, HEIGHT);
        	stage.setResizable(true);
            lobby.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Initializable replaceSceneContent(String fxml, double width, double height) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = Main.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(fxml));
        AnchorPane page;
        try {
            page = (AnchorPane) loader.load(in);
        } finally {
            in.close();
        } 
        Scene scene = new Scene(page, width, height);
        stage.setScene(scene);
        //stage.sizeToScene();
        //stage.centerOnScreen();
        return (Initializable) loader.getController();
    }
}
