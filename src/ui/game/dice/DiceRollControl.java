package ui.game.dice;

import java.io.IOException;
import java.io.InputStream;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class DiceRollControl extends BorderPane {

	@FXML
	Label title;
	@FXML
	ChoiceBox<Integer> userDiceChoiceBox;

	// Results Properties
	@FXML
	HBox userDiceHBox;
	@FXML
	HBox enemyDiceHBox;

	@FXML
	Label winnerName;

	private BooleanProperty isResultsVisible = new SimpleBooleanProperty(false);

	public boolean getIsResultsVisible() {
		return isResultsVisible.get();
	}

	public void setIsResultsVisible(boolean value) {
		isResultsVisible.set(value);
	}

	public final BooleanProperty isResultsVisibleProperty() {
		return isResultsVisible;
	}

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

	@FXML
	protected void submit() {
		int selectedNumberOfDice = (int) userDiceChoiceBox.getSelectionModel()
				.getSelectedItem();

		if (mode == Mode.ATTACKING) {
			attackHandler.onReadyToRoll(selectedNumberOfDice);
		} else {
			defendHandler.onReadyToRoll(selectedNumberOfDice);
		}

		setIsResultsVisible(true);
	}

	// ================================================================================
	// Attack Mode
	// ================================================================================

	private AttackingDiceRollControlEventHandler attackHandler;

	public void initialiseAttack(String defendingName,
			AttackingDiceRollControlEventHandler attackHandler) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				title.setText(String.format("Attacking %s!", defendingName));
			}
		});

		this.attackHandler = attackHandler;
		this.mode = Mode.ATTACKING;
	}

	// ================================================================================
	// Defend Mode
	// ================================================================================

	private DefendingDiceRollControlEventHandler defendHandler;

	public void initialiseDefend(String attackingName,
			int numberOfAttackingDice,
			DefendingDiceRollControlEventHandler defendHandler) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				title.setText(String.format("Attacked from %s!", attackingName));
			}
		});
		this.defendHandler = defendHandler;
		this.mode = Mode.DEFENDING;
	}

	// ================================================================================
	// Results
	// ================================================================================

	public void visualiseResults(DiceRollResult results, int attackerLosses,
			int defenderLosses) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				HBox attackerHBox = mode.equals(Mode.ATTACKING) ? userDiceHBox
						: enemyDiceHBox;
				HBox defenderHBox = mode.equals(Mode.DEFENDING) ? userDiceHBox
						: enemyDiceHBox;

				for (int attackDie : results.attackingDice) {
					attackerHBox.getChildren().add(getDie(attackDie));
				}
				for (int defendDie : results.defendingDice) {
					defenderHBox.getChildren().add(getDie(defendDie));
				}
			}
		});

		// winnerName.setText("");
	}

	public ImageView getDie(int number) {
		ImageView result = new ImageView();
		result.setFitHeight(50.0);
		result.setPreserveRatio(true);
		InputStream in = DiceRollControl.class
				.getResourceAsStream("resources/die_" + number + ".png");
		result.setImage(new Image(in));
		return result;
	}

	// ================================================================================
	// Utils
	// ================================================================================

	private enum Mode {
		ATTACKING, DEFENDING
	}

	private Mode mode;

	public void reset() {
		setIsResultsVisible(false);
		title.setText("");
		mode = null;
		userDiceChoiceBox.getSelectionModel().selectFirst();
		userDiceHBox.getChildren().clear();
		enemyDiceHBox.getChildren().clear();

		winnerName.setText("");
	}
}
