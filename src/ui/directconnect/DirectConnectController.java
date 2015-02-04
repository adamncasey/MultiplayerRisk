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

public class DirectConnectController extends AnchorPane implements
		Initializable {

	private Main application;

	public void setApp(Main application) {
		this.application = application;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
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
		return isValidName()
				&& isValidIP()
				&& isValidPort();
	}
	
	private boolean isValidName() {
		boolean valid = name.getText() != null
				&& name.getText().length() > 0;
		
		if(!valid)
			connectionStatus.setText("Error: Invalid Nickname");
		
		return valid;
	}
	
	private boolean isValidIP(){
		boolean valid = ip.getText() != null
				&& ip.getText().length() > 0;
		
		if(!valid)
			connectionStatus.setText("Error: Invalid IP address");
		
		return valid;
	}
	
	private boolean isValidPort() {
		boolean valid = port.getText() != null
				&& port.getText().length() > 0;
		
		if(!valid)
			connectionStatus.setText("Error: Invalid port");
		
		return valid;
	}

	@FXML
	protected void joinButtonAction(ActionEvent event) {
		if(!isFormValid()) return;
		
		progressRing.setVisible(true);
		connectionStatus.setText("connecting...");
		
		try {
            RemoteGameLobby lobby = new RemoteGameLobby(InetAddress.getByName("127.0.0.1"), Settings.port, joinHandler);

            lobby.start();

            synchronized (lobby) {
                lobby.wait();
            }
        } catch(UnknownHostException e) {
            System.out.println("Unknown host: " + e.getMessage());
        } catch(InterruptedException e) {
            System.out.printf("Interrupted Exception: " + e.getMessage());
        }
	}
	
    public static JoinLobbyEventHandler joinHandler = new JoinLobbyEventHandler() {

        @Override
        public void onTCPConnect() {
            System.out.println("onTCPConnect ");
        }

        @Override
        public void onJoinAccepted(int playerid) {
            System.out.println("onJoinAccepted " + playerid);
        }

        @Override
        public void onJoinRejected(String message) {
            System.out.println("onJoinRejected " + message);
        }

        @Override
        public void onPlayerJoin(int playerid) {
            System.out.println("onPlayerJoin " + playerid);
        }

        @Override
        public void onPlayerLeave(int playerid) {
            System.out.println("onPlayerLeave " + playerid);
        }

        @Override
        public void onPingStart() {

            System.out.println("onPingStart ");
        }

        @Override
        public void onPingReceive(int playerid) {
            System.out.println("onPingReceive " + playerid);

        }

        @Override
        public void onReady() {
            System.out.println("onReady ");

        }

        @Override
        public void onReadyAcknowledge(int playerid) {
            System.out.println("onReadyAcknowledge " + playerid);

        }

        @Override
        public void onDicePlayerOrder() {
            System.out.println("onDicePlayerOrder ");

        }

        @Override
        public void onDiceHash(int playerid) {
            System.out.println("onDiceHash " + playerid);

        }

        @Override
        public void onDiceNumber(int playerid) {
            System.out.println("onDiceNumber " + playerid);

        }

        @Override
        public void onDiceCardShuffle() {
            System.out.println("onDiceCardShuffle ");

        }

        @Override
        public void onLobbyComplete(List<IPlayer> players, List<Object> cards, Object board) {
            System.out.println("onLobbyComplete: ");
            System.out.println("\tplayers: " + players.toString());
            System.out.println("\tcards: " + cards.toString());
            System.out.println("\tboard: " + board.toString());
        }

        @Override
        public void onFailure(Throwable e) {
            System.out.println("onFailure: " + e.getMessage());

            e.printStackTrace();
        }
    };
}
