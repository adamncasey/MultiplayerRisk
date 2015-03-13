package ui.game;

import javafx.scene.layout.Pane;

public class DiceRollController {
	GameConsole console;
	Pane pane;
	
	public DiceRollController(Pane pane, GameConsole console) {
		this.pane = pane;
		this.console = console;
	}
}
