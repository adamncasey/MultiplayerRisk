package ui.game;

import javafx.application.Platform;
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
import logic.move.Move.Stage;
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
import ui.game.popup.OccupyControl;
import ui.game.popup.OccupyNumberOfArmiesEventHandler;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import ai.agents.Agent;
import ai.agents.RandomAgent;
import ai.strategy.PassiveStrategy;

public class GameController implements Initializable, PlayerController {

	@FXML
	Pane centerPane;
	@FXML
	MapControl mapControl;
	@FXML
	DiceRollControl diceRollControl;
	@FXML
	OccupyControl occupyControl;
	@FXML
	TextArea consoleTextArea;
	@FXML
	GridPane popup;
	@FXML
	Pane popupContent;
	@FXML
	HBox playerShieldContainer;
	@FXML
	Label moveDescription;

	public static GameConsole console;
	public GUIPlayer player;
	List<IPlayer> players;
	List<Object> cards;

	Move currentMove;

	Map<String, BorderPane> playerShields = new HashMap<String, BorderPane>();

	// ================================================================================
	// Startup
	// ================================================================================

	public void setApp(List<IPlayer> playersBefore, List<IPlayer> playersAfter,
			List<Integer> cards, GUIPlayer player) {
		
		this.players = combinePlayers(playersBefore, player, playersAfter);
		

		// If playing as self, use this as the PlayerController.
		if (player.getPlayerController() == null) {
			player.setPlayerController(this);
			player.setRealUserPlaying(true);
		}
		this.player = player;

		setPlayers();
		startGame(combinePlayers(playersBefore, player, playersAfter));
	}
	
	void setPlayers() {	

		// Add player shields for each player.
		for (int i = 0; i < players.size(); i++) {
			BorderPane pane = new BorderPane();
			pane.setPrefSize(70, 60);
			pane.getStyleClass().add("playerBorder");

			ImageView image = new ImageView();
			image.setFitWidth(45);
			image.setPreserveRatio(true);

			InputStream in = GameController.class.getResourceAsStream(String
					.format("player/shield_player_%d.png", i + 1));
			image.setImage(new Image(in));
			pane.setCenter(image);
			BorderPane.setAlignment(image, Pos.TOP_CENTER);

			Label label = new Label();
			label.setText(players.get(i).getPlayerName());
			label.getStyleClass().add("playerName");
			pane.setBottom(label);
			BorderPane.setAlignment(label, Pos.CENTER);
			BorderPane.setMargin(label, new Insets(0, 0, 7, 0));

			playerShieldContainer.getChildren().add(pane);
			playerShields.put(players.get(i).getPlayerName(), pane);
		}
	}

	List<IPlayer> combinePlayers(List<IPlayer> playersBefore, GUIPlayer player,
			List<IPlayer> playersAfter) {
		List<IPlayer> players = new LinkedList<>();
		players.addAll(playersBefore);
		players.add(player);

		if (playersAfter != null)
			players.addAll(playersAfter);

		return players;
	}

	void startGame(List<IPlayer> players) {
		Game game = new Game(players, new LocalPlayerHandler());

		new Thread() {
			public void run() {
				game.run();
			}
		}.start();
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		GameController.console = new GameConsole(consoleTextArea);
		this.mapControl.initialise();
	}

	// ================================================================================
	// PlayerController Functions
	// ================================================================================

    private Agent testingAI;
    boolean testing = false;
    Stage testingStage = Stage.DECIDE_ATTACK;
	
	// TODO: Replace this with Cards control.
	private PassiveStrategy ps;

	boolean moveCompleted = false;
	Player userDetails;
	
	GUITerritory attackFrom;
	int lastAttackerNumberOfArmiesSurvived;

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

		if (testing) {
			this.testingAI = new RandomAgent();
			this.testingAI.setup(player, board);
		}
	}

	@Override
	public synchronized void getMove(Move move) {
		moveCompleted = false;
		currentMove = move;

		if (testing && move.getStage() != testingStage) {
			testingAI.getMove(move);
			return;
		} else if (testingStage == move.getStage()) {
			testing = false;
		}

		switch (currentMove.getStage()) {
		case TRADE_IN_CARDS:
			ps.getMove(move);
			moveCompleted = true;
			break;
		case CHOOSE_ATTACK_DICE:
			diceRollControl.initialiseAttack(
					player.getBoard().getName(move.getTo()),
					new AttackingDiceRollControlEventHandler() {
						@Override
						public void onReadyToRoll(int numberOfAttackingDice) {
							currentMove.setAttackDice(numberOfAttackingDice);
							notifyMoveCompleted();
						}
					});
			openPopup(diceRollControl);
			break;
		case CHOOSE_DEFEND_DICE:
			diceRollControl.initialiseDefend(
					player.getBoard().getName(move.getTo()),
					move.getDefendDice(),
					new DefendingDiceRollControlEventHandler() {
						@Override
						public void onReadyToRoll(int numberOfDefendingDice) {
							currentMove.setDefendDice(numberOfDefendingDice);
							notifyMoveCompleted();
						}
					});
			openPopup(diceRollControl);
			break;
		case OCCUPY_TERRITORY:
			occupyControl.initialise(new OccupyNumberOfArmiesEventHandler() {
				@Override
				public void onNumberOfArmiesSelected(int numberOfArmies) {
					currentMove.setArmies(numberOfArmies);
					closePopup(null);
					notifyMoveCompleted();
				}
			}, mapControl.getTerritoryByID(move.getTo()).getName(), lastAttackerNumberOfArmiesSurvived-1, move.getCurrentArmies()-1);
			openPopup(occupyControl);
			break;
		default:
			break;
		}

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

		if(currentMove == null) {
			return;
		}
		
		switch (currentMove.getStage()) {
		case CLAIM_TERRITORY:
			currentMove.setTerritory(territory.getId());
			notifyMoveCompleted();
			break;
		case REINFORCE_TERRITORY:
			currentMove.setTerritory(territory.getId());
			notifyMoveCompleted();
			break;
		case PLACE_ARMIES:
			currentMove.setTerritory(territory.getId());
			currentMove.setArmies(1);
			notifyMoveCompleted();
			break;
		case DECIDE_ATTACK:
			currentMove.setDecision(true);
			attackFrom = territory;
			notifyMoveCompleted();
			break;
		case START_ATTACK:
			if (attackFrom == null) {
				attackFrom = territory;
			} else {
				currentMove.setFrom(attackFrom.getId());
				currentMove.setTo(territory.getId());
				attackFrom = null;
			}

			notifyMoveCompleted();
			break;
		// case DECIDE_FORTIFY:
		// break;
		// case START_FORTIFY:
		// break;
		// case FORTIFY_TERRITORY:
		// break;
		default:
			console.write("Stage " + currentMove.getStage().toString()
					+ " not implemented");
		}
	}

	boolean diceMoveDismissed = false;

	public synchronized void diceRollEnded(Move move) {
		diceMoveDismissed = false;

		diceRollControl.visualiseResults(
				new DiceRollResult(move.getAttackDiceRolls(), move
						.getDefendDiceRolls()), move.getAttackerLosses(), move
						.getDefenderLosses());
		
		lastAttackerNumberOfArmiesSurvived = this.player.getBoard().getArmies(move.getFrom()) - move.getAttackerLosses();

		while (!diceMoveDismissed) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
	}

	public synchronized void notifyDiceMoveDismissed() {
		diceMoveDismissed = true;
		notifyAll();
	}

	// ================================================================================
	// Popup
	// ================================================================================

	public void openPopup(Node child) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				child.setVisible(true);
				popup.setVisible(true);
			}
		});
	}

	public void closePopup(MouseEvent event) {
		popup.setVisible(false);
		for (Node n : popupContent.getChildren()) {
			n.setVisible(false);
		}

		diceRollControl.reset();

		notifyDiceMoveDismissed();
	}
}
