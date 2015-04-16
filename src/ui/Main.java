package ui;

import java.io.InputStream;
import java.net.InetAddress;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ai.AgentFactory;
import ai.AgentTypes;
import ai.agents.Agent;
import player.IPlayer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ui.creategame.CreateGameController;
import ui.directconnect.DirectConnectController;
import ui.game.GUIPlayer;
import ui.game.GameController;
import ui.lobby.host.LobbyHostController;
import ui.menu.*;

/**
 * Start point for the Application.
 * Handles the main window and navigation to views/controllers
 * @author James
 */
public class Main extends Application {

	private Stage stage;
	private final double MINIMUM_WINDOW_WIDTH = 1000;
	private final double MINIMUM_WINDOW_HEIGHT = 600;

	private final double MENU_HEIGHT = 600;
	private final double IN_GAME_HEIGHT = 800;
	
	private final double MENU_WIDTH = 1000;
	private final double IN_GAME_WIDTH = 1000;

	public static void main(String[] args) {
		Application.launch(Main.class, (java.lang.String[]) null);
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			stage = primaryStage;
			stage.setTitle("Risk");
			stage.setMinWidth(MINIMUM_WINDOW_WIDTH);
			stage.setMinHeight(MINIMUM_WINDOW_HEIGHT);
			gotoMenu();
			primaryStage.show();
		} catch (Exception ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void gotoMenu() {
		try {
			MenuController menu = (MenuController) replaceSceneContent(
					"menu/menu.fxml", MINIMUM_WINDOW_WIDTH, MINIMUM_WINDOW_HEIGHT);
			stage.setResizable(false);
			menu.setApp(this);
		} catch (Exception ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void gotoCreateGame() {
		try {
			CreateGameController lobby = (CreateGameController) replaceSceneContent(
					"creategame/creategame.fxml", MENU_WIDTH, MENU_HEIGHT);
			stage.setResizable(true);
			lobby.setApp(this);
		} catch (Exception ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void gotoLobbyAsClient() {
		try {
			LobbyHostController lobby = (LobbyHostController) replaceSceneContent(
					"lobby/lobby.fxml", MENU_WIDTH, MENU_HEIGHT);
			stage.setResizable(true);
			lobby.setApp(this);
		} catch (Exception ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void gotoLobbyAsHost(int port, int maxPlayers, String playerType,
			InetAddress addr, String hostNickname) {
		try {
			LobbyHostController lobby = (LobbyHostController) replaceSceneContent(
					"lobby/lobby.fxml", MENU_WIDTH, MENU_HEIGHT);
			lobby.startLobby(port, maxPlayers, playerType, addr, hostNickname);

			stage.setResizable(true);
			lobby.setApp(this);
		} catch (Exception ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void gotoDirectConnect() {
		try {
			DirectConnectController lobby = (DirectConnectController) replaceSceneContent(
					"directconnect/directconnect.fxml", MENU_WIDTH, MENU_HEIGHT);
			stage.setResizable(true);
			lobby.setApp(this);
		} catch (Exception ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void goToGame(List<IPlayer> playersBefore,
			List<IPlayer> playersAfter, List<Integer> cards, String playerType, String playerName) {
		try {
			GameController game = (GameController) replaceSceneContent(
					"game/Game.fxml", IN_GAME_WIDTH, IN_GAME_HEIGHT);
			stage.setResizable(true);

			GUIPlayer player = new GUIPlayer(game, playerName);
			switch (playerType) {
				case "Self": {
					break;
				}
				case "ANGRY AI": {
					player.setPlayerController(AgentFactory.buildAgent(AgentTypes.Type.ANGRY));
					break;
				}
				case "GREEDY AI": {
					player.setPlayerController(AgentFactory.buildAgent(AgentTypes.Type.GREEDY));
					break;
				}
				case "CONTINENTAL AI": {
					player.setPlayerController(AgentFactory.buildAgent(AgentTypes.Type.CONTINENTAL));
					break;
				}
				case "FURIOUS AI": {
					player.setPlayerController(AgentFactory.buildAgent(AgentTypes.Type.FURIOUS));
					break;
				}
			}

			game.setApp(playersBefore, playersAfter, cards, player);
		} catch (Exception ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void goToGameTest(List<IPlayer> playersBefore,
			List<IPlayer> playersAfter, List<Integer> cards) {
		try {
			GameController game = (GameController) replaceSceneContent(
					"game/Game.fxml", IN_GAME_WIDTH, IN_GAME_HEIGHT);
			stage.setResizable(true);

			Agent userAgent = AgentFactory.buildAgent(AgentTypes.Type.FURIOUS);
			GUIPlayer player = new GUIPlayer(game, "Host");
			
			player.setPlayerController(userAgent);

			game.setApp(playersBefore, playersAfter, cards, player);

		} catch (Exception ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private Initializable replaceSceneContent(String fxml, double width,
			double height) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		InputStream in = Main.class.getResourceAsStream(fxml);
		loader.setBuilderFactory(new JavaFXBuilderFactory());
		loader.setLocation(Main.class.getResource(fxml));
		AnchorPane page;
		try {
			page = (AnchorPane) loader.load(in);
		} finally {
			in.close();
		}
		Scene scene = new Scene(page, width, height);
		stage.setScene(scene);
		// stage.sizeToScene();
		stage.show();
		return (Initializable) loader.getController();
	}
}
