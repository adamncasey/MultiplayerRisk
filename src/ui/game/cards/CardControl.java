package ui.game.cards;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class CardControl extends GridPane {

	@FXML
	Label title;

	public CardControl() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
				"CardControl.fxml"));
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
	}
}
