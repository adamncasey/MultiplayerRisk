package ui.game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import java.net.URL;
import java.util.ResourceBundle;
import ui.game.dice.DiceRollControl;
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

	
	public GameConsole console;

	public enum CenterPaneMode {
		DICE, GAME
	}
	private CenterPaneMode centerPaneMode = CenterPaneMode.GAME;

	
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		this.console = new GameConsole(consoleTextArea);
		this.mapControl.initialise(console);
	}

	void setMode(CenterPaneMode newMode) {
		if (centerPaneMode != newMode) {
			centerPane.getChildren().clear();

			switch (newMode) {
			case DICE: {
				centerPane.getChildren().setAll(diceRollControl);
				diceRollControl.setVisible(true);
				centerPaneMode = newMode;
				break;
			}
			case GAME: {
				centerPane.getChildren().setAll(mapControl);
				centerPaneMode = newMode;
				break;
			}
			}
		}
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

	public void rollDie(ActionEvent event) {
		setMode(CenterPaneMode.DICE);
		
		//diceRollController.rollDice(2, 3);
		
		//setMode(CenterPaneMode.GAME);
	}
}









