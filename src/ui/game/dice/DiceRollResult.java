package ui.game.dice;

import java.util.Random;

public class DiceRollResult {
	public int[] attackingDice;
	public int[] defendingDice;
	
	public DiceRollResult(int[] attackingDice, int[] defendingDice) {
		this.attackingDice = attackingDice;
		this.defendingDice = defendingDice;
	}
	
	public static DiceRollResult generateDummyResults(int numberOfAttackerDice,
			int numberOfDefenderDice) {

		int[] defenderResults = new int[numberOfDefenderDice];
		int[] attackerResults = new int[numberOfAttackerDice];

		for (int i = 0; i < attackerResults.length; i++) {
			attackerResults[i] = rollDie();
		}
		for (int i = 0; i < defenderResults.length; i++) {
			defenderResults[i] = rollDie();
		}

		return new DiceRollResult(attackerResults, defenderResults);
	}

	private static int rollDie() {
		return new Random().nextInt(5) + 1;
	}
}
