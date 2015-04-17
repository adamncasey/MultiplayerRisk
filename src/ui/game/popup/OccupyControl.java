package ui.game.popup;

import java.io.IOException;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class OccupyControl extends BorderPane {

	@FXML
	Label title;
	@FXML
	ChoiceBox<Integer> choiceBox;

	ObservableList<Integer> choices;
	
	OccupyNumberOfArmiesEventHandler onSubmit;

	public OccupyControl() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
				"Occupy.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}
	
	public void initialise(OccupyNumberOfArmiesEventHandler e, String territory, int minArmies, int maxArmies) {
		onSubmit = e;
		
		choices = FXCollections.observableArrayList();
		
		for (int i = minArmies; i <= maxArmies; i++) {
			choices.add(i);
		}
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				title.setText(String.format("Occupy %s", territory));
				choiceBox.setItems(choices);
			}
		});
	}

	@FXML
	protected void submit() {
		int selectedNumber = (int) choiceBox.getSelectionModel()
				.getSelectedItem();
		onSubmit.onNumberOfArmiesSelected(selectedNumber);
	}
}
