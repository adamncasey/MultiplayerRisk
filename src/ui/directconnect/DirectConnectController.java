package ui.directconnect;

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

public class DirectConnectController extends AnchorPane implements Initializable {

	private Main application;

	public void setApp(Main application) {
		this.application = application;
	}

	@FXML
	private Label connectionStatus;

	@FXML
	private TextField name;
	@FXML
	private TextField ip;
	@FXML
	private TextField port;

	@FXML
	protected void backButtonAction(ActionEvent event) {
		application.gotoMenu();
	}

	private boolean isFormValid() {
		return isValidName() && isValidIP() && isValidPort();
	}

	private boolean isValidName() {
		boolean valid = name.getText() != null && name.getText().length() > 0;

		if (!valid)
			status("Error: Invalid Nickname");

		return valid;
	}

	private boolean isValidIP() {
		boolean valid = ip.getText() != null && ip.getText().length() > 0;

		if (!valid)
			status("Error: Invalid IP address");

		return valid;
	}

	private boolean isValidPort() {
		boolean valid = port.getText() != null && port.getText().length() > 0;

		if (!valid)
			status("Error: Invalid port");

		return valid;
	}

	@FXML
	protected void joinButtonAction(ActionEvent event) {
		if (!isFormValid())
			return;

		connectionStatus.setText("connecting...");

		try {
			RemoteGameLobby lobby = new RemoteGameLobby(
					InetAddress.getByName(ip.getText()), Integer.parseInt(port
							.getText()));
			if(lobby.joinLobby()) {
				status("Connected successfully!");
			}
			else
			{
				status("Error: " + lobby.getError());
			}
			
		} catch (NumberFormatException | UnknownHostException e) {
			status("Error: Invalid connection settings");
		}
	}

	private void status(String message) {
		connectionStatus.setText(message);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	}
}
