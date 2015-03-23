package ui.game.dice;

public class DiceRollResult {
	public int[] attackingDice;
	public int[] defendingDice;
	
	public DiceRollResult(int[] attackingDice, int[] defendingDice) {
		this.attackingDice = attackingDice;
		this.defendingDice = defendingDice;
	}
}
