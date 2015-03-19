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
	@FXML
	public BorderPane popup;

	
	public enum CenterPaneMode {DICE, GAME}
	private CenterPaneMode centerPaneMode = CenterPaneMode.GAME;
	public static GameConsole console;
	
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		GameController.console = new GameConsole(consoleTextArea);
		this.mapControl.initialise();
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

	public void rollDice(ActionEvent event) {
		diceRollControl.setVisible(true);
		popup.setVisible(true);

		
		//diceRollControl.rollDice(2, 3);
		
		//setMode(CenterPaneMode.GAME);
	}
}

