package ui.game;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import logic.Game;
import logic.move.Move;
import logic.move.Move.Stage;
import logic.state.Board;
import logic.state.Deck;
import logic.state.Player;
import player.IPlayer;
import player.PlayerController;
import networking.LocalPlayerHandler;
import ui.game.cards.CardsControl;
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
	CardsControl cardsControl;
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
	@FXML
	Button actionButton;
	@FXML
	Button cardsButton;

	public static GameConsole console;
	public GUIPlayer player;
	List<IPlayer> players;

	Deck deck;

	Move currentMove;

	Map<Integer, BorderPane> playerShields = new HashMap<Integer, BorderPane>();

	// ================================================================================
	// Startup
	// ================================================================================

	public void setApp(List<IPlayer> playersBefore, List<IPlayer> playersAfter,
			Deck deck, GUIPlayer player) {

		this.players = combinePlayers(playersBefore, player, playersAfter);
		this.deck = deck;

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
			playerShields.put(players.get(i).getPlayerid(), pane);
		}
	}

	List<IPlayer> combinePlayers(List<IPlayer> playersBefore, GUIPlayer player,
			List<IPlayer> playersAfter) {
		List<IPlayer> players = new LinkedList<>();

		players.addAll(playersBefore);
		players.add(player);
		players.addAll(playersAfter);

		return players;
	}

	void startGame(List<IPlayer> players) {
		Game game = new Game(players, new LocalPlayerHandler(), deck);

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
		case DECIDE_ATTACK:
			showActionButton("Done");
			break;
		case DECIDE_FORTIFY:
			showActionButton("Done");
			break;

		case TRADE_IN_CARDS:
			ps.getMove(move);
			moveCompleted = true;
			break;
		case CHOOSE_ATTACK_DICE:
			showActionButton("Continue");
			
			int maxArmies = player.getBoard().getArmies(move.getFrom()) - 1;
			if(maxArmies > 3)
				maxArmies = 3;
			
			diceRollControl.initialiseAttack(
					player.getBoard().getName(move.getTo()),
					new AttackingDiceRollControlEventHandler() {
						@Override
						public void onReadyToRoll(int numberOfAttackingDice) {
							currentMove.setAttackDice(numberOfAttackingDice);
							notifyMoveCompleted();
						}
					}, 1, maxArmies);
			//openPopup(diceRollControl);

			break;

		case CHOOSE_DEFEND_DICE:
			showActionButton("Continue");
			
			maxArmies = player.getBoard().getArmies(move.getTo());
			if(maxArmies > 2)
				maxArmies = 2;
			
			diceRollControl.initialiseDefend(
					player.getBoard().getName(move.getTo()),
					move.getDefendDice(),
					new DefendingDiceRollControlEventHandler() {
						@Override
						public void onReadyToRoll(int numberOfDefendingDice) {
							currentMove.setDefendDice(numberOfDefendingDice);
							notifyMoveCompleted();
						}
					}, 1, maxArmies);
			//openPopup(diceRollControl);
			break;
		case OCCUPY_TERRITORY:
			showActionButton("Occupy "
					+ player.getBoard().getName(move.getTo()));
			occupyControl.initialise(new OccupyNumberOfArmiesEventHandler() {
				@Override
				public void onNumberOfArmiesSelected(int numberOfArmies) {
					currentMove.setArmies(numberOfArmies);
					closePopup(null);
					notifyMoveCompleted();
				}
			}, mapControl.getTerritoryByID(move.getTo()).getName(),
					lastAttackerNumberOfArmiesSurvived,
					move.getCurrentArmies() - 1);
			//openPopup(occupyControl);
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

		currentMove = null;
		hideActionButton();
	}

	public synchronized void notifyMoveCompleted() {
		moveCompleted = true;
		notifyAll();
	}

	void territoryClicked(GUITerritory territory) {
		if (currentMove == null)
			return;

		if (currentMove == null) {
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
			if (isAttackFromValid(territory, currentMove)) {
				currentMove.setDecision(true);
				this.attackFrom = territory;
				notifyMoveCompleted();
			}

			break;
		case START_ATTACK:
			if (attackFrom == null) {
				if (isAttackFromValid(territory, currentMove)) {
					this.attackFrom = territory;
					console.write("Choose a territory to attack");
				} else {
					console.write("Choose a territory to attack from");
				}

				break;
			}

			// Selecting attack to
			if (isAttackToValid(attackFrom, territory, currentMove)) {
				currentMove.setFrom(attackFrom.getId());
				currentMove.setTo(territory.getId());
				notifyMoveCompleted();
			} else {
				attackFrom = null;
				console.write("Inavalid attack move");
				console.write("Choose a territory to attack from");
			}

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

	boolean isAttackFromValid(GUITerritory attackFrom, Move move) {
		if (attackFrom == null) {
			return false;
		}

		if (player.getBoard().getOwner(attackFrom.getId()) != move.getUID()) {
			console.write(String.format("%s is not your territory!",
					attackFrom.getName()));
			attackFrom = null;
			return false;
		}

		if (attackFrom.getNumberOfArmies() < 2) {
			console.write(String.format(
					"%s has too few armies to make an attack",
					attackFrom.getName()));
			attackFrom = null;
			return false;
		}

		return true;
	}

	boolean isAttackToValid(GUITerritory attackFrom, GUITerritory attackTo,
			Move move) {
		boolean valid = player.getMoveChecker().checkStartAttack(
				currentMove.getUID(), attackFrom.getId(), attackTo.getId());

		return valid;
	}

	boolean diceMoveDismissed = false;

	public synchronized void diceRollEnded(Move move) {
		diceMoveDismissed = false;

		diceRollControl.visualiseResults(
				new DiceRollResult(move.getAttackDiceRolls(), move
						.getDefendDiceRolls()), move.getAttackerLosses(), move
						.getDefenderLosses());

		lastAttackerNumberOfArmiesSurvived = move.getAttackDiceRolls().size()
				- move.getAttackerLosses();

		while (!diceMoveDismissed) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		diceRollControl.reset();
	}

	// ================================================================================
	// Buttons
	// ================================================================================
	public void onActionButtonClick(ActionEvent event) {
		if (currentMove == null)
			return;

		switch (currentMove.getStage()) {
		case DECIDE_ATTACK:
			currentMove.setDecision(false);
			notifyMoveCompleted();
			break;
		case DECIDE_FORTIFY:
			currentMove.setDecision(false);
			notifyMoveCompleted();
			break;
		case CHOOSE_ATTACK_DICE:
			openPopup(diceRollControl);
			break;
		case CHOOSE_DEFEND_DICE:
			openPopup(diceRollControl);
			break;
		case OCCUPY_TERRITORY:
			openPopup(occupyControl);
			break;

		default:
			break;
		}
	}

	public void onCardsButtonClick(ActionEvent event){
		openPopup(cardsControl);
	}

	void showActionButton(String text) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				actionButton.setText(text);
				actionButton.setVisible(true);
			}
		});
	}

	void hideActionButton() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				actionButton.setVisible(false);
			}
		});
	}

	// ================================================================================
	// Popup
	// ================================================================================

	public synchronized void notifyDiceMoveDismissed() {
		diceMoveDismissed = true;
		notifyAll();
	}

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

		if (diceRollControl.isDiceMoveCompleted()) {
			notifyDiceMoveDismissed();
		}
	}
}
