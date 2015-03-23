package ui.game.dice;

import java.io.IOException;
import java.util.Random;

import ui.game.GameConsole;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.*;

public class DiceRollControl extends BorderPane {
	
	@FXML
	ChoiceBox<Integer> userDiceChoiceBox;
	GameConsole console;

	public DiceRollControl() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DiceRollControl.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    public void initialise(GameConsole console) {
    	this.console = console;
    }
    
    @FXML
    protected void submit() {
    	// User clicked Roll Dice - maybe stick this in a handler or something?
    }
    
    
    // ================================================================================
 	// Mutating state variables
 	// ================================================================================
	private int numberOfEnemyDice;
	private DiceRollResult lastRollResult;
	private boolean isAttacking;
	
    
	// ================================================================================
	// These methods should be called in chronological order
	// ================================================================================
    public void startNewDiceRoll(boolean isAttacking) {
    	numberOfEnemyDice = 0;
    	setLastRollResult(null);
    	this.setAttacking(isAttacking);
    }
    
	public DiceRollResult rollDice() {
		int[] attackerResults;
		int[] defenderResults;
		
		int userNumberOfDice = (int)userDiceChoiceBox.getSelectionModel().getSelectedItem();
		
		if(isAttacking) {
			defenderResults = new int[numberOfEnemyDice];
			attackerResults = new int[userNumberOfDice];
		}
		else
		{
			attackerResults = new int[numberOfEnemyDice];
			defenderResults = new int[userNumberOfDice];
		}
		
		for(int i = 0; i<attackerResults.length; i++) {
			attackerResults[i] = rollDie();
		}
		for(int i = 0; i<defenderResults.length; i++) {
			defenderResults[i] = rollDie();
		}
		
		return new DiceRollResult(defenderResults, attackerResults);
	}
	
    public void visualiseResults(DiceRollResult results) {
    }
	
    
	// ================================================================================
	// Utils
	// ================================================================================
	private int rollDie() {
		return new Random().nextInt(5) + 1;
	}
	
	
	// ================================================================================
	// Accessors
	// ================================================================================
    public int getNumberOfEnemyDice() {
		return numberOfEnemyDice;
	}

	public void setNumberOfEnemyDice(int numberOfEnemyDice) {
		this.numberOfEnemyDice = numberOfEnemyDice;
	}

	public DiceRollResult getLastRollResult() {
		return lastRollResult;
	}

	public void setLastRollResult(DiceRollResult lastRollResult) {
		this.lastRollResult = lastRollResult;
	}

	public boolean isAttacking() {
		return isAttacking;
	}

	public void setAttacking(boolean isAttacking) {
		this.isAttacking = isAttacking;
	}
}