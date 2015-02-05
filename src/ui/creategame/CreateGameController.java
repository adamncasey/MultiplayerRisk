package ui.creategame;

import java.awt.Color;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.ResourceBundle;

import player.IPlayer;
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

	public void setApp(Main application) {
		this.application = application;
	}

	@FXML
	protected void backButtonAction(ActionEvent event) {
		application.gotoMenu();
	}

	@FXML
	protected void startButtonAction(ActionEvent event) {
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	}
}
