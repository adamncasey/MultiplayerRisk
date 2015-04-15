package ui.game.dice;

import java.util.List;

public class DiceRollResult {
	public List<Integer> attackingDice;
	public List<Integer> defendingDice;

	public DiceRollResult(List<Integer> attackingDice, List<Integer> defendingDice) {
		this.attackingDice = bubbleSort(attackingDice);
		this.defendingDice = bubbleSort(defendingDice);
	}

	private static List<Integer> bubbleSort(List<Integer> attackingDice2) {
//		for (int i = 0; i < attackingDice2.size(); i++) {
//			for (int x = 1; x < attackingDice2.size() - i; x++) {
//				if (attackingDice2.get(x - 1) < attackingDice2.get(x)) {
//					int temp = attackingDice2.get(x - 1);
//					attackingDice2.set(x - 1, attackingDice2.get(x));
//					attackingDice2.set(x, temp);
//				}
//			}
//		}
		return attackingDice2;
	}
}
