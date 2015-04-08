package ui.directconnect;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.ResourceBundle;

import player.IPlayer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lobby.RemoteGameLobby;
import lobby.Validator;
import lobby.handler.JoinLobbyEventHandler;
import settings.Settings;
import ui.*;

public class DirectConnectController extends AnchorPane implements
		Initializable {

	private Main application;

	public void setApp(Main application) {
		this.application = application;
		this.ip.setText("localhost");
		this.port.setText(Settings.port + "");
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
	private ProgressIndicator progressRing;

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
		boolean valid = true;

		if (!Validator.isValidPort(port.getText())) {
			status("Error: Invalid port");
			valid = false;
		}

		return valid;
	}

	@FXML
	protected void joinButtonAction(ActionEvent event) {
		if (!isFormValid())
			return;
		progressRing.setVisible(true);
		connectionStatus.setText("connecting...");

		try {
			RemoteGameLobby lobby = new RemoteGameLobby(
					InetAddress.getByName(ip.getText()), Settings.port,
					joinHandler);
			lobby.start();
		} catch (UnknownHostException e) {
			status("Unknown host: " + e.getMessage());
		}
	}

	private void status(String message) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				connectionStatus.setText(message);
				progressRing.setVisible(false);
			}
		});
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}

	public JoinLobbyEventHandler joinHandler = new JoinLobbyEventHandler() {

		@Override
		public void onTCPConnect() {
			status("onTCPConnect ");
		}

		@Override
		public void onJoinAccepted(int playerid) {
			status("onJoinAccepted " + playerid);
		}

		@Override
		public void onJoinRejected(String message) {
			status("Join rejected: " + message);
		}

		@Override
		public void onPlayerJoin(int playerid) {
			status("onPlayerJoin " + playerid);
		}

		@Override
		public void onPlayerLeave(int playerid) {
			status("onPlayerLeave " + playerid);
		}

		@Override
		public void onPingStart() {

			status("onPingStart ");
		}

		@Override
		public void onPingReceive(int playerid) {
			status("onPingReceive " + playerid);

		}

		@Override
		public void onReady() {
			status("onReady ");

		}

		@Override
		public void onReadyAcknowledge(int playerid) {
			status("onReadyAcknowledge " + playerid);

		}

		@Override
		public void onInitialiseGame(double protocolVersion, String[] extendedFeatures) {
			status("onInitialiseGame " + protocolVersion + ". " + extendedFeatures.toString());
		}

		@Override
		public void onDicePlayerOrder() {
			status("onDicePlayerOrder ");

		}

		@Override
		public void onDiceHash(int playerid) {
			status("onDiceHash " + playerid);

		}

		@Override
		public void onDiceNumber(int playerid) {
			status("onDiceNumber " + playerid);

		}

		@Override
		public void onDiceCardShuffle() {
			status("onDiceCardShuffle ");

		}

		@Override
		public void onLobbyComplete(List<IPlayer> playersBefore, List<IPlayer> playersAfter, List<Object> cards) {
			status("onLobbyComplete: ");
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					application.goToGame(playersBefore, playersAfter, cards);
				}
			});
		}

		@Override
		public void onFailure(Throwable e) {
			status("onFailure: " + e.getMessage());

			e.printStackTrace();
		}
	};
}
