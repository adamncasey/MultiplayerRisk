package ui.game.dice;

import ui.game.GameConsole;

public class DiceRollController {
	
	GameConsole console;
	DiceRollControl control;
	
	public DiceRollController(DiceRollControl control, GameConsole console) {
		this.control = control;
		this.console = console;
	}
}
