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
		return isValidPort() && isValidNumberOfPlayers();
	}

	private boolean isValidPort() {
		boolean valid = true;

		if (!Validator.isValidPort(port.getText())) {
			status("Error: Invalid port");
			valid = false;
		}

		return valid;
	}

	private boolean isValidNumberOfPlayers() {
		boolean valid = false;

		try {
			int noOfPlayers = Integer.parseInt(players.getText());

			if (noOfPlayers < Settings.MinNumberOfPlayers)
				status("Error: Minimum " + Settings.MinNumberOfPlayers + " players");
			else if (noOfPlayers > Settings.MaxNumberOfPlayers)
				status("Error: Maximum " + Settings.MaxNumberOfPlayers + " players");
			else {
				valid = true;
			}
		} catch (NumberFormatException e) {
			status("Error: Invalid value for max number of players.");
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
		if (isFormValid()) {
			application.gotoLobbyAsHost(Integer.parseInt(port.getText()), Integer.parseInt(players.getText()));
		}
	}
}
