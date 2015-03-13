package ui.game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ResourceBundle;

import ui.game.WorldMapController.ArmyMode;

public class GameController implements Initializable {
	
	@FXML
	public Pane centerPane;
	@FXML
	public Pane mapPane;
	@FXML
	public Pane dicePane;
	@FXML
	public TextArea consoleTextArea;
	@FXML
	public ImageView worldmap;
	@FXML
	public ImageView AU0, AU1, AU2, AU3, AF0, AF1, AF2, AF3, AF4, AF5, SA0,
			SA1, SA2, SA3, EU0, EU1, EU2, EU3, EU4, EU5, EU6, NA0, NA1, NA2,
			NA3, NA4, NA5, NA6, NA7, NA8, AS0, AS1, AS2, AS3, AS4, AS5, AS6,
			AS7, AS8, AS9, AS10, AS11;
	
	
	
	GameConsole console;
	
	private WorldMapController mapController;
	private DiceRollController diceRollController;
	
	
	public enum CenterPaneMode { DICE, GAME }
	private CenterPaneMode centerPaneMode = CenterPaneMode.GAME;

	
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		DefaultMap default_map = new DefaultMap(AU0, AU1, AU2, AU3, AF0, AF1,
				AF2, AF3, AF4, AF5, SA0, SA1, SA2, SA3, EU0, EU1, EU2, EU3,
				EU4, EU5, EU6, NA0, NA1, NA2, NA3, NA4, NA5, NA6, NA7, NA8,
				AS0, AS1, AS2, AS3, AS4, AS5, AS6, AS7, AS8, AS9, AS10, AS11);
		
		
		this.console = new GameConsole(consoleTextArea);
		this.diceRollController = new DiceRollController(dicePane, console);
		this.mapController = new WorldMapController(mapPane, console, default_map);
	}
	
	

	void setMode(CenterPaneMode newMode) {
		if (centerPaneMode != newMode) {
			centerPane.getChildren().clear();

			switch (newMode) {
			case DICE: {
				centerPane.getChildren().setAll(dicePane);
				dicePane.setVisible(true);
				centerPaneMode = newMode;
				break;
			}
			case GAME: {
				centerPane.getChildren().setAll(mapPane);
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
		mapController.setArmyMode(ArmyMode.ADD);
		console.write("In army adding mode.");
	}

	public void removeArmies(ActionEvent event) {
		mapController.setArmyMode(ArmyMode.REMOVE);
		console.write("In army removing mode mode.");
	}

	public void setArmies(ActionEvent event) {
		mapController.setArmyMode(ArmyMode.SET);
		console.write("In army setting mode.");
	}

	public void rollDie(ActionEvent event) {
		if(centerPaneMode == CenterPaneMode.DICE) {
			setMode(CenterPaneMode.GAME);
		}
		else
		{
			setMode(CenterPaneMode.DICE);
		}
		console.write("Changing center pane mode");
	}
}
