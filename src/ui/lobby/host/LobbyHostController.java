package ui.lobby.host;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import networking.LobbyClient;
import player.IPlayer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import lobby.LocalGameLobby;
import lobby.handler.HostLobbyEventHandler;
import ui.*;

public class LobbyHostController extends AnchorPane implements Initializable {

	private Main application;

	@FXML
	ListView<String> players;
	@FXML
	TextArea consoleWindow;
	
	ObservableList<String> playersList;
	int maxPlayers;
	
	String hostPlayerType;
	
	LocalGameLobby lobby;


	public void setApp(Main application) {
		this.application = application;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		playersList = FXCollections.observableArrayList();
		players.setItems(playersList);
	}
	
	public void startLobby(int port, int maxPlayers, String hostPlayerType, String hostNickname) {
		this.maxPlayers = maxPlayers;
		this.hostPlayerType = hostPlayerType;
		
		playersList.add(String.format("%s (%s) \t[Host]", hostNickname, hostPlayerType));
		
        lobby = new LocalGameLobby(handler, port, hostNickname);

        lobby.start();
	}

	@FXML
	protected void backButtonAction(ActionEvent event) {
		lobby.closeLobby();
		application.gotoMenu();
	}

	@FXML
	protected void startButtonAction(ActionEvent event) {
		if(playersList.size()>1) {
			lobby.startGame();
		}
	}
	
	@FXML
	protected void kickPlayerButtonAction(ActionEvent event) {
		int selectedIndex = players.getSelectionModel().getSelectedIndex();
		if(selectedIndex > 0) {
			String selected = playersList.get(selectedIndex);
			
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					playersList.remove(selectedIndex);
				}
			});
			
			writeToConsole(String.format("-> You kicked %s\n", selected));
		}
		else
		{
			writeToConsole("You cannot kick the host!");
		}
	}
	
	void writeToConsole(String message) {
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
    				playersList.add("Player (" + playerid + "): " + name);
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
        public void onLobbyComplete(List<IPlayer> playersBefore, List<IPlayer> playersAfter, List<Integer> cardIDs) {
            writeToConsole("onLobbyComplete: ");
			writeToConsole("\tplayers before: " + playersBefore.toString());
			writeToConsole("\tplayers after: " + playersAfter.toString());
			
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					application.goToGame(playersBefore, playersAfter, cardIDs);
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
