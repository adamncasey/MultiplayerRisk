package ui.game.cards;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ui.game.GameController;
import ui.game.map.MapControl;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import logic.Card;

public class CardControl extends GridPane {

	@FXML
	Label title;
	@FXML
	ScrollPane root;
	@FXML
	FlowPane flowPane;
	@FXML
	Button submit;

	Image artilleryImage, cavalryImage, infantryImage, wildcardImage;

	MapControl map;

	private boolean isInTradeStage;

	Map<GridPane, Card> hand = new HashMap<GridPane, Card>();
	Map<Card, GridPane> cardToGridPane = new HashMap<Card, GridPane>();
	Map<Card, Region> selectRegions = new HashMap<Card, Region>();

	Set<GridPane> selected = new HashSet<GridPane>();
	CardTradeEventHandler tradeHandler;

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
			public void changed(ObservableValue<? extends Bounds> ov,
					Bounds oldBounds, Bounds bounds) {
				flowPane.setPrefWidth(bounds.getWidth());
				flowPane.setPrefHeight(bounds.getHeight());
			}
		});
	}

	// ================================================================================
	// Create / Add cards
	// ================================================================================

	public void initialise(MapControl map, CardTradeEventHandler tradeHandler) {
		this.map = map;
		this.tradeHandler = tradeHandler;
		artilleryImage = new Image(getClass().getResourceAsStream(
				"resources/artillery.jpg"));
		cavalryImage = new Image(getClass().getResourceAsStream(
				"resources/cavalry.jpg"));
		infantryImage = new Image(getClass().getResourceAsStream(
				"resources/infantry.jpg"));
		wildcardImage = new Image(getClass().getResourceAsStream(
				"resources/wildcard.jpg"));
	}

	public void add(Card card) {
		GridPane pane = getCard(card);
		hand.put(pane, card);
		cardToGridPane.put(card, pane);

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				flowPane.getChildren().add(pane);
			}
		});

		pane.addEventFilter(MouseEvent.MOUSE_CLICKED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent mouseEvent) {
						cardClicked((GridPane) mouseEvent.getSource(), card);
					}
				});
	}
	
	private GridPane getWildcard(Card card) {
		GridPane grid = new GridPane();
		grid.setPrefSize(99, 150);

		ImageView background = new ImageView(wildcardImage);
		background.setFitHeight(150);
		background.setPickOnBounds(true);
		background.setPreserveRatio(true);
		
		Region blackout = new Region();
		blackout.getStyleClass().add("blackout");
		selectRegions.put(card, blackout);

		grid.add(blackout, 0, 0);
		grid.getStyleClass().add("card");

		return grid;
	}

	private GridPane getCard(Card card) {
		if(card.getID() > 41) {
			return getWildcard(card);
		}
		
		GridPane grid = new GridPane();
		grid.setPrefSize(99, 150);

		ImageView background = null;

		if (card.getType() == 1) {
			background = new ImageView(infantryImage);
		} else if (card.getType() == 5) {
			background = new ImageView(cavalryImage);
		} else if (card.getType() == 10) {
			background = new ImageView(artilleryImage);
		}

		background.setFitHeight(150);
		background.setPickOnBounds(true);
		background.setPreserveRatio(true);

		Label name = new Label(card.getName());
		name.getStyleClass().add("cardName");
		GridPane.setHalignment(name, HPos.CENTER);
		GridPane.setValignment(name, VPos.CENTER);
		GridPane.setMargin(name, new Insets(10, 0, 0, 0));

		ImageView territory = new ImageView(map.getTerritoryByID(card.getID())
				.getImage().getImage());
		territory.setFitHeight(50);
		territory.setFitWidth(80);
		territory.setPickOnBounds(true);
		territory.setPreserveRatio(true);

		GridPane.setHalignment(territory, HPos.CENTER);
		GridPane.setValignment(territory, VPos.BOTTOM);
		GridPane.setMargin(territory, new Insets(0, 0, 8, 0));

		Region blackout = new Region();
		blackout.getStyleClass().add("blackout");
		selectRegions.put(card, blackout);

		grid.add(background, 0, 0);
		grid.add(name, 0, 0);
		grid.add(territory, 0, 0);
		grid.add(blackout, 0, 0);
		grid.getStyleClass().add("card");

		return grid;
	}

	private void cardClicked(GridPane pane, Card card) {
		if (selected.contains(pane)) {
			unselectCard(pane);
			if (selected.size() == 2) {
				updateTradeButton();
			}
		} else if (selected.size() < 3) {
			selectCard(pane, card);
			updateTradeButton();
		}
	}

	private void selectCard(GridPane pane, Card card) {
		pane.getStyleClass().add("cardSelected");
		Region r = selectRegions.get(card);
		r.getStyleClass().clear();
		r.getStyleClass().add("selected");
		selected.add(pane);
	}

	private void unselectCard(GridPane pane) {
		pane.getStyleClass().add("cardSelected");
		Region r = selectRegions.get(hand.get(pane));
		r.getStyleClass().clear();
		r.getStyleClass().add("blackout");
		selected.remove(pane);
	}

	private void updateTradeButton() {
		boolean newValue = false;
		if (selected.size() == 3 && isInTradeStage) {
			newValue = true;
		}
		final boolean val = newValue;
		if (newValue != submit.isVisible()) {
			Platform.runLater(() -> submit.setVisible(val));
		}
	}
	
	public void removeCards(List<Card> cards) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				for(Card card : cards) {
					flowPane.getChildren().remove(cardToGridPane.get(card));
				}
			}
		});
	}

	@FXML
	protected void submit(ActionEvent event) {
		List<Card> cards = new ArrayList<Card>();
		for (GridPane pane : selected) {
			cards.add(hand.get(pane));
		}

		if (Card.containsSet(cards)) {
			setInTradeStage(false);
			tradeHandler.onCardsPicked(cards);
		} else {
			GameController.console
					.write("Invalid - selected cards do not make a set");
			unselectAll();
		}
	}

	private void unselectAll() {
		for (GridPane pane : selected) {
			unselectCard(pane);
		}
	}

	public boolean isInTradeStage() {
		return isInTradeStage;
	}

	public void setInTradeStage(boolean isInTradeStage) {
		if (this.isInTradeStage != isInTradeStage) {
			this.isInTradeStage = isInTradeStage;
			updateTradeButton();
		}
	}
}
