package ui.game.dice;

import java.io.IOException;
import java.io.InputStream;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class DiceRollControl extends BorderPane {

	@FXML
	HBox userDiceHBox;
	@FXML
	HBox enemyDiceHBox;

	@FXML
	Label results;

	private boolean isDiceMoveCompleted = false;

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
	
	// ================================================================================
	// Results
	// ================================================================================

	public void visualiseResults(DiceRollResult results, int attackerLosses,
			int defenderLosses, boolean isAttacking) {
		isDiceMoveCompleted = true;
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				HBox attackerHBox = isAttacking ? userDiceHBox
						: enemyDiceHBox;
				HBox defenderHBox = !isAttacking ? userDiceHBox
						: enemyDiceHBox;

				for (int attackDie : results.attackingDice) {
					attackerHBox.getChildren().add(getDie(attackDie));
				}
				for (int defendDie : results.defendingDice) {
					defenderHBox.getChildren().add(getDie(defendDie));
				}
				
				DiceRollControl.this.results.setText(String.format("The attacker lost %d armies, the defender lost %d armies.\n", attackerLosses, defenderLosses));
			}
		});
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
	public void reset() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				userDiceHBox.getChildren().clear();
				enemyDiceHBox.getChildren().clear();
				DiceRollControl.this.results.setText("");
			}
		});
	}

	public boolean isDiceMoveCompleted() {
		return isDiceMoveCompleted;
	}
}
