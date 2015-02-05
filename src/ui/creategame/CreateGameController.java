package ui.creategame;

import java.awt.Color;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import player.IPlayer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import lobby.RemoteGameLobby;
import lobby.handler.JoinLobbyEventHandler;
import settings.Settings;
import ui.*;

public class CreateGameController extends AnchorPane implements Initializable {

	private Main application;
	
	@FXML
	private Label connectionStatus;

	public void setApp(Main application) {
		this.application = application;
	}

	@FXML
	protected void backButtonAction(ActionEvent event) {
		application.gotoMenu();
	}

	@FXML
	protected void startButtonAction(ActionEvent event) {
		final ScheduledExecutorService scheduler 
        = Executors.newScheduledThreadPool(1);

    scheduler.scheduleAtFixedRate(
            new Runnable(){
                  
                int counter = 0;
                  
                @Override
                public void run() {
                    counter++;
                    if(counter<=10){
                         
                        Platform.runLater(new Runnable(){
                            @Override
                            public void run() {
                            	connectionStatus.setText(
                                "isFxApplicationThread: "
                                + Platform.isFxApplicationThread() + "\n"
                                + "Counting: "
                                + String.valueOf(counter));
                            }
                        });
                         
                         
                    }else{
                        scheduler.shutdown();
                        Platform.runLater(new Runnable(){
                            @Override
                            public void run() {
                            	connectionStatus.setText(
                                "isFxApplicationThread: "
                                + Platform.isFxApplicationThread() + "\n"
                                + "-Finished-");
                            }
                        });
                    }
                      
                }
            }, 
            1, 
            1, 
            TimeUnit.SECONDS);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	}
}
