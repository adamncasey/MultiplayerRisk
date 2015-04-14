package ui.game;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import logic.Game;
import logic.move.Move;
import logic.state.Board;
import logic.state.Player;
import player.IPlayer;
import player.PlayerController;
import networking.LocalPlayerHandler;
import ui.game.dice.AttackingDiceRollControlEventHandler;
import ui.game.dice.DefendingDiceRollControlEventHandler;
import ui.game.dice.DiceRollControl;
import ui.game.dice.DiceRollResult;
import ui.game.map.GUITerritory;
import ui.game.map.MapControl;

import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import ai.strategy.PassiveStrategy;

public class GameController implements Initializable, PlayerController {

	@FXML
	public Pane centerPane;
	@FXML
	public MapControl mapControl;
	@FXML
	public DiceRollControl diceRollControl;
	@FXML
	public TextArea consoleTextArea;
	@FXML
	public GridPane popup;
	@FXML
	public Pane popupContent;
	@FXML
	HBox playerShields;

	private Move currentMove;

	public static GameConsole console;

	List<Object> cards;
	public GUIPlayer player;
	
	List<String> players;
	
	
	// ================================================================================
	// Startup
	// ================================================================================

	public void setApp(List<IPlayer> playersBefore, List<IPlayer> playersAfter,
			List<Integer> cards, GUIPlayer player, List<String> playerNames) {

		// If playing as self, use this as the PlayerController.
		if (player.getPlayerController() == null) {
			player.setPlayerController(this);
		}
		this.player = player;

		setPlayers(playerNames);
		startGame(combinePlayers(playersBefore, player, playersAfter), playerNames);
	}
	
	void setPlayers(List<String> players) {
		this.players = players;
		
		// Add player shields for each player.
		for(int i=0; i<players.size(); i++) {
			BorderPane pane = new BorderPane();
			pane.setPrefSize(70, 60);
			pane.getStyleClass().add("playerBorder");
		
			ImageView image = new ImageView();
			image.setFitWidth(45);
			image.setPreserveRatio(true);
			
			InputStream in = GameController.class.getResourceAsStream(String.format("player/shield_player_%d.png", i+1));
			image.setImage(new Image(in));
			pane.setCenter(image);
			BorderPane.setAlignment(image, Pos.TOP_CENTER);
			
			Label label = new Label();
			label.setText(players.get(i));
			label.getStyleClass().add("playerName");
			pane.setBottom(label);
			BorderPane.setAlignment(label, Pos.CENTER);
			BorderPane.setMargin(label, new Insets(0,0,7,0));
			
			playerShields.getChildren().add(pane);
		}
	}
	
	List<IPlayer> combinePlayers(List<IPlayer> playersBefore, GUIPlayer player, List<IPlayer> playersAfter) {
		List<IPlayer> players = new LinkedList<>();
		players.addAll(playersBefore);
		players.add(player);

		if (playersAfter != null)
			players.addAll(playersAfter);
		
		return players;
	}
	
	void startGame(List<IPlayer> players, List<String> playerNames) {
		Game game = new Game(players, playerNames, new LocalPlayerHandler());

		new Thread() {
			public void run() {
				game.run();
			}
		}.start();
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		GameController.console = new GameConsole(consoleTextArea);
		this.mapControl.initialise(console);
	}

	
	// ================================================================================
	// PlayerController Functions
	// ================================================================================
	
	// TODO: Replace this with Cards control.
	private PassiveStrategy ps;
	
	boolean moveCompleted = false;
	Player userDetails;

	@Override
	public void setup(Player player, Board board) {
		this.userDetails = player;
		ps = new PassiveStrategy(player, board, new Random());

		for (final GUITerritory territory : mapControl
				.getClickableTerritories()) {

			// Handle territory clicks.
			territory.getImage().addEventFilter(MouseEvent.MOUSE_CLICKED,
					new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent mouseEvent) {
							territoryClicked(territory);
						}
					});
		}
	}

	@Override
	public synchronized void getMove(Move move) {
		moveCompleted = false;
		currentMove = move;

		String prompt = "Not set";

		switch (move.getStage()) {
		case CLAIM_TERRITORY:
			prompt = "Claim a territory!";
			break;
		case REINFORCE_TERRITORY:
			prompt = "Reinforce a territory!";
			break;
		case TRADE_IN_CARDS:
			ps.getMove(move);
			moveCompleted = true;
			break;
		case PLACE_ARMIES:
			break;
		case DECIDE_ATTACK:
			break;
		case START_ATTACK:
			break;
		case CHOOSE_ATTACK_DICE:
			break;
		case CHOOSE_DEFEND_DICE:
			break;
		case OCCUPY_TERRITORY:
			break;
		case DECIDE_FORTIFY:
			break;
		case START_FORTIFY:
			break;
		case FORTIFY_TERRITORY:
			break;
		default:
			assert false : move.getStage();
		}

		console.write(prompt);

		while (!moveCompleted) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
	}

	public synchronized void notifyMoveCompleted() {
		moveCompleted = true;
		notifyAll();
	}

	void territoryClicked(GUITerritory territory) {

		switch (currentMove.getStage()) {
		case CLAIM_TERRITORY:
			currentMove.setTerritory(territory.getId());
			notifyMoveCompleted();
			break;
		case REINFORCE_TERRITORY:
			currentMove.setTerritory(territory.getId());
			notifyMoveCompleted();
			break;
		case TRADE_IN_CARDS:
			break;
		case PLACE_ARMIES:
			currentMove.setTerritory(territory.getId());
			currentMove.setArmies(1);
			notifyMoveCompleted();
			break;
//		case DECIDE_ATTACK:
//			break;
//		case START_ATTACK:
//			break;
//		case CHOOSE_ATTACK_DICE:
//			break;
//		case CHOOSE_DEFEND_DICE:
//			break;
//		case OCCUPY_TERRITORY:
//			break;
//		case DECIDE_FORTIFY:
//			break;
//		case START_FORTIFY:
//			break;
//		case FORTIFY_TERRITORY:
//			break;
		default:
			console.write("Stage " + currentMove.getStage().toString()
					+ " not implemented");
		}
	}

	public Move getCurrentMove() {
		return currentMove;
	}

	
	// ================================================================================
	// Button Actions
	// ================================================================================

	
	// ================================================================================
	// Popup
	// ================================================================================

	public void openPopup(Node child) {
		child.setVisible(true);
		popup.setVisible(true);
	}

	public void closePopup(MouseEvent event) {
		popup.setVisible(false);
		for (Node n : popupContent.getChildren()) {
			n.setVisible(false);
		}

		diceRollControl.reset();
	}

	
	// ================================================================================
	// Dice
	// ================================================================================
	public void rollDiceAttack(ActionEvent event) {
		diceRollControl.initialiseAttack("Nathan the defender",
				new AttackingDiceRollControlEventHandler() {
					@Override
					public void onReadyToRoll(int numberOfAttackingDice) {
						console.write(String.format("Attacking with %d dice!",
								numberOfAttackingDice));

						// Get number of defending dice from the defending
						// player.
						int numberOfDefendingDie = 3;

						// Get result of dice rolls.
						DiceRollResult result = DiceRollResult
								.generateDummyResults(numberOfAttackingDice,
										numberOfDefendingDie);
						diceRollControl.visualiseResults(result);
					}
				});
		openPopup(diceRollControl);
	}

	public void rollDiceDefend(ActionEvent event) {
		int numberOfAttackingDice = 2;

		diceRollControl.initialiseDefend("Victor the brave", 3,
				new DefendingDiceRollControlEventHandler() {
					@Override
					public void onReadyToRoll(int numberOfDefendingDice) {
						console.write(String.format(
								"Defending %d dice with %d dice!",
								numberOfAttackingDice, numberOfDefendingDice));

						// Get result of dice rolls.
						DiceRollResult result = DiceRollResult
								.generateDummyResults(numberOfAttackingDice,
										numberOfDefendingDice);
						diceRollControl.visualiseResults(result);
					}
				});
		openPopup(diceRollControl);
	}
}
