package ui.game;

import java.util.*;

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

/**
 * GUIPlayer
 */
public class GUIPlayer implements IPlayer {
	private PlayerController playerController;
	private GameController gameController;
	private Board board;
	private Player player;
	int lastPlayerToMove = -1;
	private boolean isRealUserPlaying;
	private String playerName;
	private MoveChecker moveChecker;

	private LocalPlayerHandler handler;
	
	private final int playerid;

	public GUIPlayer(GameController gameController, String playerName, int playerid) {
		this.gameController = gameController;
		this.playerName = playerName;
		
		this.playerid = playerid;
	}

	public void setup(Player player, Map<Integer, String> names, Board board,
			MoveChecker checker, LocalPlayerHandler handler) {
		this.board = board;
		this.player = player;
		playerController.setup(player, board);
		this.handler = handler;
		this.moveChecker = checker;
	}

	public Player getLogicPlayer(){
		return this.player;
	}

	public void nextMove(String move, int playerID) {
		GameController.console.write(move);

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				gameController.moveDescription.setText(move);
			}
		});

		final BorderPane lastShield;
		if (lastPlayerToMove != -1) {
			lastShield = gameController.playerShields.get(lastPlayerToMove);

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lastShield.getStyleClass().remove("selectedPlayer");
				}
			});
		}

		final BorderPane shield = gameController.playerShields.get(playerID);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				shield.getStyleClass().add("selectedPlayer");
			}
		});

		lastPlayerToMove = playerID;
	}

	public void updatePlayer(Move move) {
		// Print update to console.
		String desc = Move.describeMove(move, board);
		GameController.console.write(desc);
		System.out.print(desc);

		switch (move.getStage()) {
//		case CARD_DRAWN:
//			break;
//		case CHOOSE_ATTACK_DICE:
//			break;
//		case CHOOSE_DEFEND_DICE:
//			break;
		case CLAIM_TERRITORY:
			updateMapSingleTerritory(move);
			break;
		case CARD_DRAWN:
			System.out.print("Card drawn!");
			break;
//		case DECIDE_ATTACK:
//			break;
//		case DECIDE_FORTIFY:
//			break;
		case END_ATTACK:
			if (isRealUserPlaying && (board.getOwner(move.getFrom()) == player.getUID() || board.getOwner(move.getTo()) == player.getUID())) {
				gameController.diceRollEnded(move);
			}
			postAttackMapUpdate(move);
			// Update
			break;
		case FORTIFY_TERRITORY:
			updateMapMoveBetweenTerritories(move);
			break;
//		case GAME_BEGIN:
//			break;
//		case GAME_END:
//			break;
		case OCCUPY_TERRITORY:
			updateMapMoveBetweenTerritories(move);
			break;
		case PLACE_ARMIES:
			updateMapSingleTerritory(move);
			break;
		case PLAYER_ELIMINATED:
			break;
		case REINFORCE_TERRITORY:
			updateMapSingleTerritory(move);
			break;
//		case ROLL_HASH:
//			break;
//		case ROLL_NUMBER:
//			break;
//		case SETUP_BEGIN:
//			break;
//		case SETUP_END:
//			break;
//		case START_ATTACK:
//			break;
//		case START_FORTIFY:
//			break;
		case TRADE_IN_CARDS:
			break;
		default:
			break;
		}

		// Networking
		if (move.getUID() == player.getUID()) {
			handler.sendMove(move);
		}
		
		try {
			Thread.sleep(15);
		} catch (InterruptedException e) {}
	}
	
	void updateMapSingleTerritory(Move move) {
		gameController.mapControl.updateTerritory(move.getUID(),
				board.getArmies(move.getTerritory()),
				getTerritory(move.getTerritory()));
	}

	void updateMapMoveBetweenTerritories(Move move) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				gameController.mapControl.updateTerritory(move.getUID(),
						board.getArmies(move.getFrom()),
						getTerritory(move.getFrom()));
				gameController.mapControl.updateTerritory(move.getUID(),
						board.getArmies(move.getTo()),
						getTerritory(move.getTo()));
			}
		});
	}

	GUITerritory getTerritory(int id) {
		return gameController.mapControl.getTerritoryByID(id);
	}

	void postAttackMapUpdate(Move move) {
		GUITerritory attackerTerritory = gameController.mapControl
				.getTerritoryByID(move.getFrom());

		GUITerritory defenderTerritory = gameController.mapControl
				.getTerritoryByID(move.getTo());

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				gameController.mapControl.updateTerritory(
						board.getOwner(attackerTerritory.getId()),
						board.getArmies(move.getFrom()), attackerTerritory);
				gameController.mapControl.updateTerritory(
						board.getOwner(defenderTerritory.getId()),
						board.getArmies(move.getTo()), defenderTerritory);
			}
		});
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


	@Override
	public String getPlayerName() {
		return playerName;
	}

	public MoveChecker getMoveChecker() {
		return moveChecker;

	}

	@Override
	public int getPlayerid() {
		return playerid;
	}
}
