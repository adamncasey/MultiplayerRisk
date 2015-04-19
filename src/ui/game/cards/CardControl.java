package ui.game.cards;

import java.io.IOException;

import ui.game.map.MapControl;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import logic.Card;

public class CardControl extends GridPane {

	@FXML
	Label title;
	@FXML
	ScrollPane root;
	@FXML
	FlowPane flowPane;
	
	Image artilleryImage, cavalryImage, infantryImage;
	
	MapControl map;

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
	
	public void initialise(MapControl map) {
		this.map = map;
		artilleryImage = new Image(getClass().getResourceAsStream("resources/artillery.jpg"));
		cavalryImage = new Image(getClass().getResourceAsStream("resources/cavalry.jpg"));
		infantryImage = new Image(getClass().getResourceAsStream("resources/infantry.jpg"));
	}
	
	
	public void add(Card card) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				flowPane.getChildren().add(getCard(card));
			}
		});
	}

	private GridPane getCard(Card card) {
		GridPane grid = new GridPane();
		grid.setPrefSize(99, 150);
		
		ImageView background = new ImageView(artilleryImage);
		background.setFitHeight(150);
		background.setPickOnBounds(true);
		background.setPreserveRatio(true);
		
		Label name = new Label(card.getName());
		name.getStyleClass().add("cardName");
		GridPane.setHalignment(name, HPos.CENTER);
		GridPane.setValignment(name, VPos.CENTER);
		GridPane.setMargin(name,new Insets(10,0,0,0));
		
		ImageView territory = new ImageView(map.getTerritoryByID(card.getID()).getImage().getImage());
		territory.setFitHeight(50);
		territory.setFitWidth(80);
		territory.setPickOnBounds(true);
		territory.setPreserveRatio(true);
		
		GridPane.setHalignment(territory, HPos.CENTER);
		GridPane.setValignment(territory, VPos.BOTTOM);
		GridPane.setMargin(territory, new Insets(0,0,8,0));
		
		grid.add(background, 0, 0);
		grid.add(name, 0, 0);
		grid.add(territory, 0, 0);
		
		return grid;
	}

	@FXML
	protected void submit() {
		
	}
}
