package ui.game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import java.net.URL;
import java.util.ResourceBundle;
import ui.game.dice.AttackingDiceRollControlEventHandler;
import ui.game.dice.DefendingDiceRollControlEventHandler;
import ui.game.dice.DiceRollControl;
import ui.game.dice.DiceRollResult;
import ui.game.map.MapControl;
import ui.game.map.MapControl.ArmyMode;

public class GameController implements Initializable {

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

	public static GameConsole console;


	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		GameController.console = new GameConsole(consoleTextArea);
		this.mapControl.initialise(console);
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
		console.write("Closing popup");
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
