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
import settings.Settings;
import ui.*;

public class LobbyHostController extends AnchorPane implements Initializable {

	private Main application;

	@FXML
	private ListView<String> players;
	@FXML
	private TextArea consoleWindow;
	
	private ObservableList<String> playersList;
	
	LocalGameLobby lobby;


	public void setApp(Main application) {
		this.application = application;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ObservableList<String> items = FXCollections.observableArrayList("You (Host)");
		playersList = items;
		players.setItems(playersList);
		
        lobby = new LocalGameLobby(handler, Settings.port);
        lobby.start();
	}

	@FXML
	protected void backButtonAction(ActionEvent event) {
		lobby.closeLobby();
		application.gotoMenu();
	}

	@FXML
	protected void startButtonAction(ActionEvent event) {
		lobby.startGame();
	}
	
	@FXML
	protected void kickPlayerButtonAction(ActionEvent event) {
		int selectedIndex = players.getSelectionModel().getSelectedIndex();
		if(selectedIndex > 0) {
			playersList.remove(selectedIndex);
			String selected = playersList.get(selectedIndex);
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
            writeToConsole("onPLayerJoinRequest " + client.supportedVersions[0]);
            return null; // Accept the player (rejecting requires a string for reject message)
        }

        @Override
        public void onPlayerJoin(int playerid) {
            writeToConsole("Player " + playerid + " joined the lobby");
            playersList.add("Player " + playerid);
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
        public void onLobbyComplete(List<IPlayer> players, List<Object> cards, Object board) {
            writeToConsole("onLobbyComplete: ");
            writeToConsole("\tplayers: " + players.toString());
            writeToConsole("\tcards: " + cards.toString());
            writeToConsole("\tboard: " + board.toString());

            writeToConsole("At this point, we should pass this data off to the Game Loop");
        }

        @Override
        public void onFailure(Throwable e) {
            writeToConsole("onFailure: " + e.getMessage());

            e.printStackTrace();
        }
    };
}
