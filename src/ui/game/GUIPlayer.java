package ui.game;

import java.util.*;

import ai.agents.Agent;
import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import logic.move.Move;
import logic.move.MoveChecker;
import logic.state.Board;
import logic.state.Player;
import player.IPlayer;
import player.PlayerController;
import networking.LocalPlayerHandler;
import ui.game.map.GUITerritory;
import ui.game.map.MapControl;

/**
 * GUIPlayer
 */
public class GUIPlayer implements IPlayer {
	private PlayerController playerController;
	private GameController gameController;
	private Board board;
	private Player player;
	String lastPlayerToMove;
	private boolean isRealUserPlaying;

	private LocalPlayerHandler handler;

	public GUIPlayer(GameController gameController) {
		this.gameController = gameController;
	}

	public void setup(Player player, List<String> names, Board board,
			MoveChecker checker, LocalPlayerHandler handler) {
		this.board = board;
		this.player = player;
		playerController.setup(player, board);
		this.handler = handler;
	}

	public void nextMove(String move, String playerName) {
		GameController.console.write(move);

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				gameController.moveDescription.setText(move);
			}
		});

		final BorderPane lastShield;
		if (lastPlayerToMove != null) {
			lastShield = gameController.playerShields.get(lastPlayerToMove);

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lastShield.getStyleClass().remove("selectedPlayer");
				}
			});
		}

		final BorderPane shield = gameController.playerShields.get(playerName);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				shield.getStyleClass().add("selectedPlayer");
			}
		});

		lastPlayerToMove = playerName;
	}

	public void updatePlayer(Move move) {
		// Print update to console.
		String desc = Move.describeMove(move, board);
		GameController.console.write(desc);
		System.out.print(desc);

		// Update map.
		GUITerritory territory;
		switch (move.getStage()) {
		case CARD_DRAWN:
			break;
		case CHOOSE_ATTACK_DICE:
			break;
		case CHOOSE_DEFEND_DICE:
			break;
		case CLAIM_TERRITORY:
			gameController.mapControl.updateTerritory(move.getUID() + 1, 1,
					gameController.mapControl.getTerritoryByID(move
							.getTerritory()));
			break;
		case DECIDE_ATTACK:
			break;
		case DECIDE_FORTIFY:
			break;
		case END_ATTACK:
			if (isRealUserPlaying) {
				gameController.diceRollEnded(move);
			}
			break;
		case FORTIFY_TERRITORY:
			break;
		case GAME_BEGIN:
			break;
		case GAME_END:
			break;
		case OCCUPY_TERRITORY:
			break;
		case PLACE_ARMIES:
			territory = gameController.mapControl.getTerritoryByID(move
					.getTerritory());
			gameController.mapControl.updateTerritory(move.getUID() + 1,
					territory.getNumberOfArmies() + 1, territory);
			break;
		case PLAYER_ELIMINATED:
			break;
		case REINFORCE_TERRITORY:
			territory = gameController.mapControl.getTerritoryByID(move
					.getTerritory());
			gameController.mapControl.updateTerritory(move.getUID() + 1,
					territory.getNumberOfArmies() + 1, territory);
			break;
		case ROLL_HASH:
			break;
		case ROLL_NUMBER:
			break;
		case SETUP_BEGIN:
			break;
		case SETUP_END:
			break;
		case START_ATTACK:
			break;
		case START_FORTIFY:
			break;
		case TRADE_IN_CARDS:
			break;
		default:
			break;
		}

		// int number = board.getNumTerritories();
		// for(int i = 0 ; i < number ; i++) {
		// int owner = board.getOwner(i);
		// if (owner >= 0) {
		// int armies = board.getArmies(i);
		// String name = board.getName(i);
		// gameController.mapControl.setArmies(owner + 1, armies,
		// gameController.mapControl.getTerritoryByName(name));
		// }
		// }

		// Networking
		if (move.getUID() == player.getUID()) {
			handler.sendMove(move);
		}
	}

	public void getMove(Move move) {
		if (move.getStage() == Move.Stage.ROLL_HASH
				|| move.getStage() == Move.Stage.ROLL_NUMBER) {
			handler.handleRoll(move);
		} else {
			playerController.getMove(move);
		}
	}

	public PlayerController getPlayerController() {
		return this.playerController;
	}

	public void setPlayerController(PlayerController playerController) {
		this.playerController = playerController;
	}

	public Board getBoard() {
		return board;
	}

	public boolean isRealUserPlaying() {
		return isRealUserPlaying;
	}

	public void setRealUserPlaying(boolean isRealUserPlaying) {
		this.isRealUserPlaying = isRealUserPlaying;
	}
}