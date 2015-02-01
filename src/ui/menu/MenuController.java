package ui.menu;

import java.awt.Color;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

public class MenuController extends AnchorPane implements Initializable {
	
    private Main application;

    public void setApp(Main application) {
        this.application = application;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void createGame(ActionEvent event) {
        if (application != null) {
        }
    }

    public void findGame(ActionEvent event) {
        if (application != null) {
            application.gotoLobbyFinder();
        }
    }
    
    public void directConnect(ActionEvent event) {
        if (application != null) {
        }
    }

    public void quit(ActionEvent event) throws Exception {
        if (application != null) {
            System.exit(0);
        }
    }
}
