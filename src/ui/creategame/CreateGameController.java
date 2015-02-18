package ui.creategame;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lobby.Validator;
import settings.Settings;
import ui.*;

public class CreateGameController extends AnchorPane implements Initializable {

	private Main application;
	
	@FXML
	private TextField port;
	@FXML
	private TextField players;
	@FXML
	private Label status;
	
	public void setApp(Main application) {
		this.application = application;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.players.setText("4");
		this.port.setText(Settings.port + "");
	}

	private boolean isFormValid() {
		return isValidPort();
	}
	
	private boolean isValidPort() {
		boolean valid = true;

		if (!Validator.isValidPort(port.getText())) {
			status("Error: Invalid port");
			valid = false;
		}

		return valid;
	}
	
	private void status(String message) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				status.setText(message);
			}
		});
	}

	
	@FXML
	protected void backButtonAction(ActionEvent event) {
		application.gotoMenu();
	}

	@FXML
	protected void startButtonAction(ActionEvent event) {
		if(isFormValid()) {
			application.gotoLobbyHost();
		}
	}
}
