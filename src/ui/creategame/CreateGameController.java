package ui.creategame;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
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
	private TextField ipaddress;
	@FXML
	private TextField players;
	@FXML
	private TextField name;
	@FXML
	private Label status;
	@FXML
	private ChoiceBox<String> playAsChoiceBox;

	public void setApp(Main application) {
		this.application = application;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.players.setText("4");
		this.port.setText(Settings.DEFAULT_PORT + "");
		this.ipaddress.setText(Settings.DEFAULT_LISTEN_IP);
	}

	private boolean isFormValid() {
		return isValidPort() && isValidNumberOfPlayers() && isValidNickname() && isValidIPAddress();
	}

	private boolean isValidPort() {
		boolean valid = true;

		if (!Validator.isValidPort(port.getText())) {
			status("Error: Invalid port");
			valid = false;
		}

		return valid;
	}

	private boolean isValidIPAddress() {
		try {
			InetAddress.getByName(ipaddress.getText());
		} catch (UnknownHostException e) {
			// TODO If the user enters a valid URL which doesn't resolve through DNS, this is not a useful error message.
			return false;
		}

		return true;
	}
	
	private boolean isValidNickname() {
		boolean valid = true;

		if (name.getText() == null || name.getText().isEmpty()) {
			status("Error: Invalid nickname");
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
			String selected = playAsChoiceBox.getSelectionModel().getSelectedItem();
			InetAddress addr;
			try {
				addr = InetAddress.getByName(ipaddress.getText());
			} catch (UnknownHostException e) {
				status("Error: Invalid Binding IP Address");
				return;
			}

			application.gotoLobbyAsHost(Integer.parseInt(port.getText()), Integer.parseInt(players.getText()), selected, addr, name.getText());
		}
	}
}
