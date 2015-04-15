package ui.directconnect;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.ResourceBundle;

import player.IPlayer;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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
	private ChoiceBox<String> playAsChoiceBox;
	@FXML
	private Button connectButton;
	
	private BooleanProperty isFormEditable = new SimpleBooleanProperty(true);
	public boolean getIsFormEditable() {
		return isFormEditable.get();
	}
	public void setIsFormEditable(boolean value) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				isFormEditable.set(value);
			}
		});
	}
	public final BooleanProperty isFormEditableProperty() {
		return isFormEditable;
	}

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
			connectButton.setDisable(true);
			RemoteGameLobby lobby = new RemoteGameLobby(
					InetAddress.getByName(ip.getText()), Settings.port,
					joinHandler,
					name.getText());
			lobby.start();
		} catch (UnknownHostException e) {
			status("Unknown host: " + e.getMessage());
			connectButton.setDisable(false);
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
			setIsFormEditable(false);
			status("Join accepted!\nYou are player number " + playerid + "\n\nWaiting for host to start the game...");
		}

		@Override
		public void onJoinRejected(String message) {
			status("Join rejected: " + message);
		}

		@Override
		public void onPlayerJoin(int playerid, String name) {
			status("onPlayerJoin (" + playerid + "): " + name);
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
		public void onLobbyComplete(List<IPlayer> playersBefore, List<IPlayer> playersAfter, List<Integer> cardIDs) {
			status("onLobbyComplete: ");
			String selectedPlayerType = (String)playAsChoiceBox.getSelectionModel().getSelectedItem();
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					application.goToGame(playersBefore, playersAfter, cardIDs, selectedPlayerType, ui.Main.namePlayers(playersBefore, name.getText(), playersAfter));
				}
			});
		}

		@Override
		public void onFailure(Throwable e) {
			status("onFailure: " + e.getMessage());
			
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					connectButton.setDisable(false);
				}
			});
			
		}
	};
}
