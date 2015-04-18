package ui.game.numericControl;


import java.io.IOException;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class NumericControl extends BorderPane {

	@FXML
	Label title;
	@FXML
	ChoiceBox<Integer> choiceBox;

	ObservableList<Integer> choices;

	NumberSelectedEventHandler onSubmit;

	public NumericControl() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
				"NumericControl.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	public void initialise(NumberSelectedEventHandler e, String message, int min, int max) {
		onSubmit = e;

		choices = FXCollections.observableArrayList();

		for (int i = min; i <= max; i++) {
			choices.add(i);
		}

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				title.setText(message);
				choiceBox.setItems(choices);
			}
		});
	}

	@FXML
	protected void submit() {
		int selectedNumber = (int) choiceBox.getSelectionModel()
				.getSelectedItem();
		onSubmit.onSelected(selectedNumber);
	}
}
