package ui.lobby.host;

import java.net.InetAddress;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import networking.LobbyClient;
import networking.LocalPlayerHandler;
import player.IPlayer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import lobby.LocalGameLobby;
import lobby.handler.HostLobbyEventHandler;
import logic.state.Deck;
import settings.Settings;
import ui.*;

public class LobbyHostController extends AnchorPane implements Initializable {

	private Main application;

	@FXML
	ListView<String> players;
	@FXML
	TextArea consoleWindow;
	@FXML
	Button startButton;
	
	ObservableList<String> playersList;
	int maxPlayers;
	
	String hostPlayerType;
	
	LocalGameLobby lobby;
	
	private String hostNickname;

	public void setApp(Main application) {
		this.application = application;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		playersList = FXCollections.observableArrayList();
		players.setItems(playersList);
	}
	
	public void startLobby(int port, int maxPlayers, String hostPlayerType, InetAddress addr, String hostNickname) {
		this.maxPlayers = maxPlayers;
		this.hostPlayerType = hostPlayerType;
		this.hostNickname = hostNickname;
		
		playersList.add(hostNickname);
		
        lobby = new LocalGameLobby(handler, port, addr, hostNickname);

        lobby.start();
	}

	@FXML
	protected void backButtonAction(ActionEvent event) {
		lobby.cancelLobby();
		lobby.interrupt();
		application.gotoMenu();
	}

	@FXML
	protected void startButtonAction(ActionEvent event) {
		if(playersList.size() >= Settings.MinNumberOfPlayers) {
			startButton.setDisable(true);
			lobby.startGame();
		}
		else {
			writeToConsole("You need at least " + Settings.MinNumberOfPlayers + " players");
		}
	}
	
	void writeToConsole(String message) {
		System.out.println(message);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				consoleWindow.appendText(message + "\n");
			}
		});

	}

    public HostLobbyEventHandler handler = new HostLobbyEventHandler() {
        @Override
        public String onPlayerJoinRequest(LobbyClient client) {
        	if(playersList.size() == maxPlayers) {
        		writeToConsole("New join request rejected because lobby is full");
        		return "Lobby is full";
        	}
        	else
        	{
                writeToConsole("New join request. Supported version: " + client.supportedVersions[0]);
                return null;
        	}
        }

        @Override
        public void onPlayerJoin(int playerid, String name) {
    		Platform.runLater(new Runnable() {
    			@Override
    			public void run() {
    				playersList.add(name);
    			}
    		});
    		
            writeToConsole("Player " + playerid + " joined the lobby");
        }

        @Override
        public void onPlayerLeave(int playerid) {
        	writeToConsole("Player " + playerid + " left the lobby");
        }

        @Override
        public void onPingStart() {

            writeToConsole("onPingStart ");
        }

        @Override
        public void onPingReceive(int playerid) {
            writeToConsole("onPingReceive " + playerid);

        }

        @Override
        public void onReady() {
            writeToConsole("onReady ");

        }

        @Override
        public void onReadyAcknowledge(int playerid) {
            writeToConsole("onReadyAcknowledge " + playerid);

        }

		@Override
		public void onInitialiseGame(double protocolVersion, String[] extendedFeatures) {

			writeToConsole("onInitialiseGame " + protocolVersion + ". " + extendedFeatures.toString());
		}

		@Override
        public void onDicePlayerOrder() {
            writeToConsole("onDicePlayerOrder ");

        }

        @Override
        public void onDiceHash(int playerid) {
            writeToConsole("onDiceHash " + playerid);
        }

        @Override
        public void onDiceNumber(int playerid) {
            writeToConsole("onDiceNumber " + playerid);

        }

        @Override
        public void onDiceCardShuffle() {
            writeToConsole("onDiceCardShuffle ");

        }

        @Override
        public void onLobbyComplete(List<IPlayer> playersBefore, List<IPlayer> playersAfter, Deck deck, LocalPlayerHandler localPlayerHandler) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					application.goToGame(playersBefore, playersAfter, deck, hostPlayerType, hostNickname, LocalGameLobby.HOST_PLAYERID, localPlayerHandler);
				}
			});
        }

        @Override
        public void onFailure(Throwable e) {
            writeToConsole("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    };
}
