package ui.game.cards;

import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

public class CardControl extends GridPane {

	@FXML
	Label title;
	@FXML
	ScrollPane root;
	@FXML
	FlowPane flowPane;

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
		
		root.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
            	flowPane.setPrefWidth(bounds.getWidth());
            	flowPane.setPrefHeight(bounds.getHeight());
            }
        });
	}

	@FXML
	protected void submit() {
	}
}
