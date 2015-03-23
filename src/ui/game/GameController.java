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
	
	// Popup
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
	
	public void rollDice(ActionEvent event) {
		openPopup(diceRollControl);
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
	}
}

