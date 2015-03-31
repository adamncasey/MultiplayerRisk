package ui.game.dice;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class DiceRollControl extends BorderPane {

	@FXML
	public Label title;
	@FXML
	ChoiceBox<Integer> userDiceChoiceBox;

	public DiceRollControl() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
				"DiceRollControl.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	private enum Mode {
		ATTACKING, DEFENDING
	}

	private Mode mode;

	@FXML
	protected void submit() {
		int selectedNumberOfDice = (int) userDiceChoiceBox.getSelectionModel()
				.getSelectedItem();

		if (mode == Mode.ATTACKING) {
			attackHandler.onReadyToRoll(selectedNumberOfDice);
		} else {
			defendHandler.onReadyToRoll(selectedNumberOfDice);
		}
	}

	// ================================================================================
	// Attack Mode
	// ================================================================================

	private AttackingDiceRollControlEventHandler attackHandler;

	public void initialiseAttack(String defendingPlayerName,
			AttackingDiceRollControlEventHandler attackHandler) {
		title.setText(String.format("Attacking %s!",
				defendingPlayerName));
		this.attackHandler = attackHandler;
		this.mode = Mode.ATTACKING;
	}

	// ================================================================================
	// Defend Mode
	// ================================================================================

	private DefendingDiceRollControlEventHandler defendHandler;

	public void initialiseDefend(String attackingPlayerName,
			int numberOfAttackingDice,
			DefendingDiceRollControlEventHandler defendHandler) {
		title.setText(String.format("Attacked by %s!",
				attackingPlayerName));
		this.defendHandler = defendHandler;
		this.mode = Mode.DEFENDING;
	}


	// ================================================================================
	// Utils
	// ================================================================================

	public void visualiseResults(DiceRollResult results) {
		
	}

	public void reset() {
		title.setText("");
		this.attackHandler = null;
		this.defendHandler = null;
		this.mode = null;
		this.userDiceChoiceBox.getSelectionModel().selectFirst();
	}
}