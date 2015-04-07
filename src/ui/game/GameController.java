package ui.game;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import logic.Game;
import player.IPlayer;
import networking.LocalPlayerHandler;
import ui.Main;
import ui.commandline.CommandLinePlayer;
import ui.game.dice.AttackingDiceRollControlEventHandler;
import ui.game.dice.DefendingDiceRollControlEventHandler;
import ui.game.dice.DiceRollControl;
import ui.game.dice.DiceRollResult;
import ui.game.map.GUIPlayer;
import ui.game.map.MapControl;
import ui.game.map.MapControl.ArmyMode;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

public class GameController implements Initializable {

	@FXML
	public Pane centerPane;

	public void setMapControl(MapControl mapControl) {
		this.mapControl = mapControl;
	}

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

	public static GameConsole console;

	List<IPlayer> playersBefore;
	List<IPlayer> playersAfter;
	List<Object> cards;
	
	public GUIPlayer player;

	public void setApp(List<IPlayer> playersBefore, List<IPlayer> playersAfter, List<Object> cards, GUIPlayer player) {

		if(playersBefore == null && playersAfter == null) {
			System.out.println("Error setting up game.");
			return;
		}
		System.out.println("Starting game");

		List<IPlayer> players = new LinkedList<>();
		players.addAll(playersBefore);
		
        this.player = player;
        
		players.add(player);

		if(playersAfter != null)
			players.addAll(playersAfter);

		List<String> names = namePlayers(playersBefore, playersAfter);


		Game game = new Game(players, names, new LocalPlayerHandler());
		
		new Thread()
		{
		    public void run() {
				game.run();
		    }
		}.start();


		System.out.println("Players: ");
		for(String name : names) {
			System.out.println(name);
		}
	}

	/**
	 * Temporary method. To be replaced.
	 */
	private static List<String> namePlayers(List<IPlayer> playersBefore, List<IPlayer> playersAfter) {
		int i=0;
		List<String> names = new LinkedList<>();

		if(playersBefore != null)
			for(;i<playersBefore.size(); i++) {
				names.add("Foreign Player " + i);
			}

		names.add("Local Player");

		if(playersAfter != null)
			for(int j=0;j<playersAfter.size(); j++, i++) {
				names.add("Foreign Player " + i);
			}

		return names;
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		System.out.println("Initializing...");
		GameController.console = new GameConsole(consoleTextArea);
		this.mapControl.initialise(console, player);
	}

	public void setGUIPlayer(GUIPlayer player){
		this.player = player;
	}

	// ================================================================================
	// Button Actions
	// ================================================================================

	public void addArmies(ActionEvent event) {
		mapControl.setArmyMode(ArmyMode.ADD);
		console.write("In army adding mode.");
	}

	public void removeArmies(ActionEvent event) {
		mapControl.setArmyMode(ArmyMode.REMOVE);
		console.write("In army removing mode mode.");
	}

	public void setArmies(ActionEvent event) {
		mapControl.setArmyMode(ArmyMode.SET);
		console.write("In army setting mode.");
	}


	// ================================================================================
	// Popup
	// ================================================================================

	public void openPopup(Node child) {
		child.setVisible(true);
		popup.setVisible(true);
	}

	public void closePopup(MouseEvent event) {
		popup.setVisible(false);
		for(Node n : popupContent.getChildren()) {
			n.setVisible(false);
		}
		
		diceRollControl.reset();
	}
	
	
	// ================================================================================
	// Dice
	// ================================================================================
	public void rollDiceAttack(ActionEvent event) {
		diceRollControl.initialiseAttack("Nathan the defender", new AttackingDiceRollControlEventHandler() {
			@Override
			public void onReadyToRoll(int numberOfAttackingDice) {
				console.write(String.format("Attacking with %d dice!", numberOfAttackingDice));

				// Get number of defending dice from the defending player.
				int numberOfDefendingDie = 3;

				// Get result of dice rolls.
				DiceRollResult result = DiceRollResult.generateDummyResults(numberOfAttackingDice, numberOfDefendingDie);
				diceRollControl.visualiseResults(result);
			}
		});
		openPopup(diceRollControl);
	}

	public void rollDiceDefend(ActionEvent event) {
		int numberOfAttackingDice = 2;
		
		diceRollControl.initialiseDefend("Victor the brave", 3, new DefendingDiceRollControlEventHandler() {
			@Override
			public void onReadyToRoll(int numberOfDefendingDice) {
				console.write(String.format("Defending %d dice with %d dice!", numberOfAttackingDice, numberOfDefendingDice));
				
				// Get result of dice rolls.
				DiceRollResult result = DiceRollResult.generateDummyResults(numberOfAttackingDice, numberOfDefendingDice);
				diceRollControl.visualiseResults(result);
			}
		});
		openPopup(diceRollControl);
	}
}
