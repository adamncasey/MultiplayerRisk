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
import ui.game.numericControl.NumberSelectedEventHandler;
import ui.game.numericControl.NumericControl;
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
	NumericControl numericControl;
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
					.format("player/shield_player_%d.png", players.get(i)
							.getPlayerid() + 1));
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
	boolean testing = true;
	Stage testingStage = Stage.DECIDE_ATTACK;

	// TODO: Replace this with Cards control.
	private PassiveStrategy ps;

	boolean moveCompleted = false;
	Player userDetails;

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

		case DECIDE_FORTIFY:
			showActionButton("Done");
			break;
		case START_FORTIFY:
			currentMove.setFrom(mapControl.getSelectedFrom().getId());
			currentMove.setTo(mapControl.getSelectedTo().getId());
			notifyMoveCompleted();
			break;

		 case FORTIFY_TERRITORY:
				numericControl.initialise(new NumberSelectedEventHandler() {
					@Override
					public void onSelected(int number) {
						hideNumericControl();
						currentMove.setArmies(number);
						notifyMoveCompleted();
					}
				}, String.format(
						"How many armies would move from %s to %s", player
								.getBoard().getName(move.getFrom()), player
								.getBoard().getName(move.getTo())), 1, move.getCurrentArmies()-1);
				showNumericControl();
		 break;

		case DECIDE_ATTACK:
			showActionButton("Done");
			break;
		case START_ATTACK:
			currentMove.setFrom(mapControl.getSelectedFrom().getId());
			currentMove.setTo(mapControl.getSelectedTo().getId());
			notifyMoveCompleted();
			break;

		case CHOOSE_ATTACK_DICE:
			int maxArmies = player.getBoard().getArmies(move.getFrom()) - 1;
			if (maxArmies > 3)
				maxArmies = 3;

			numericControl.initialise(new NumberSelectedEventHandler() {
				@Override
				public void onSelected(int number) {
					hideNumericControl();
					currentMove.setAttackDice(number);
					notifyMoveCompleted();
				}
			}, String.format(
					"How many armies would you like to attack %s with", player
							.getBoard().getName(move.getTo())), 1, maxArmies);
			showNumericControl();
			break;

		case CHOOSE_DEFEND_DICE:
			maxArmies = player.getBoard().getArmies(move.getTo());
			if (maxArmies > 2)
				maxArmies = 2;

			numericControl.initialise(new NumberSelectedEventHandler() {
				@Override
				public void onSelected(int number) {
					hideNumericControl();
					currentMove.setDefendDice(number);
					notifyMoveCompleted();
				}
			}, String.format(
					"How many armies would you like to defend %s with", player
							.getBoard().getName(move.getTo())), 1, maxArmies);
			showNumericControl();
			break;

		case OCCUPY_TERRITORY:
			numericControl.initialise(new NumberSelectedEventHandler() {
				@Override
				public void onSelected(int number) {
					hideNumericControl();
					currentMove.setArmies(number);
					closePopup(null);
					notifyMoveCompleted();
				}
			}, String.format(
					"How many armies would you like to occupy %s with", player
							.getBoard().getName(move.getTo())),
					lastAttackerNumberOfArmiesSurvived,
					move.getCurrentArmies() - 1);
			showNumericControl();
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
			if (mapControl.getSelectedFrom() == null) {
				if (isAttackFromValid(territory, currentMove)) {
					mapControl.setSelectedFrom(territory);
					console.write("Choose a territory to attack");
				} else {
					console.write("Choose a territory to attack from");
				}

				break;
			}

			if (isAttackToValid(mapControl.getSelectedFrom(), territory,
					currentMove)) {
				mapControl.setSelectedTo(territory);
				currentMove.setDecision(true);
				notifyMoveCompleted();
			} else {
				mapControl.setSelectedFrom(null);
				console.write("Invalid attack move");
				console.write("Choose a territory to attack from");
			}

			break;

		case DECIDE_FORTIFY:
			if (mapControl.getSelectedFrom() == null) {
				if (isFortifyFromValid(territory, currentMove)) {
					mapControl.setSelectedFrom(territory);
					console.write("Choose a territory to move armies to");
				} else {
					console.write("Choose a territory to move armies from");
				}

				break;
			}

			if (isFortifyToValid(mapControl.getSelectedFrom(), territory,
					currentMove)) {
				mapControl.setSelectedTo(territory);
				currentMove.setDecision(true);
				notifyMoveCompleted();
			} else {
				mapControl.setSelectedFrom(null);
				console.write("Invalid move");
				console.write("Choose a territory to move armies from");
			}
			break;

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
						.getDefenderLosses(), player.getBoard().getOwner(move.getFrom()) == player.getPlayerid());

		lastAttackerNumberOfArmiesSurvived = move.getAttackDiceRolls().size()
				- move.getAttackerLosses();
		
		openPopup(diceRollControl);

		while (!diceMoveDismissed) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		diceRollControl.reset();
	}

	// ================================================================================
	// User move validation
	// ================================================================================

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
				player.getPlayerid(), attackFrom.getId(), attackTo.getId());

		return valid;
	}

	boolean isFortifyFromValid(GUITerritory from, Move move) {
		if (from == null) {
			return false;
		}

		if (player.getBoard().getOwner(from.getId()) != player.getPlayerid()) {
			console.write(String.format("%s is not your territory!",
					from.getName()));
			from = null;
			return false;
		}

		if (from.getNumberOfArmies() < 2) {
			console.write(String.format("%s has too few armies", from.getName()));
			from = null;
			return false;
		}

		return true;
	}

	boolean isFortifyToValid(GUITerritory from, GUITerritory to, Move move) {

		if (player.getBoard().getOwner(to.getId()) != player.getPlayerid()) {
			console.write(String.format("%s is not your territory!",
					from.getName()));
			return false;
		}

		return player.getMoveChecker().checkStartFortify(player.getPlayerid(),
				from.getId(), to.getId());
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
			mapControl.setSelectedFrom(null);
			mapControl.setSelectedTo(null);
			notifyMoveCompleted();
			break;
		case DECIDE_FORTIFY:
			currentMove.setDecision(false);
			notifyMoveCompleted();
			break;

		default:
			break;
		}
	}


	public void onCardsButtonClick(ActionEvent event) {
		openPopup(cardsControl);
		cardsControl.showHand(player.getLogicPlayer().getHand());
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
	// Numeric input control
	// ================================================================================
	public void showNumericControl() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				numericControl.setVisible(true);
			}
		});
	}

	public void hideNumericControl() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				numericControl.setVisible(false);
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
