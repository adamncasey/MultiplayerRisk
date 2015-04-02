package ui.game.dice;

import java.util.Random;

public class DiceRollResult {
	public int[] attackingDice;
	public int[] defendingDice;

	public DiceRollResult(int[] attackingDice, int[] defendingDice) {
		this.attackingDice = bubbleSort(attackingDice);
		this.defendingDice = bubbleSort(defendingDice);
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

	private static int[] bubbleSort(int[] num) {
		for (int i = 0; i < num.length; i++) {
			for (int x = 1; x < num.length - i; x++) {
				if (num[x - 1] < num[x]) {
					int temp = num[x - 1];
					num[x - 1] = num[x];
					num[x] = temp;
				}
			}
		}
		return num;
	}

	public boolean isAttackerWinner() {
		boolean isWinner = false;
		
		for(int i=0; i<attackingDice.length && i<defendingDice.length; i++) {
			
		}
		
		return isWinner;
	}
}
