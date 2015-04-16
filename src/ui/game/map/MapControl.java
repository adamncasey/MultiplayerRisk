package ui.game.map;

import java.io.IOException;
import java.util.*;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

public class MapControl extends Pane {

	public enum ArmyClass {
		None, Infantry, Cavalry, Artillery
	}

	Map<Integer, GUITerritory> territoryByID = new HashMap<>();
	Map<ImageView, GUITerritory> territoryByImageView = new HashMap<>();

	Map<Integer, Image> artilleryImages = new HashMap<>();
	Map<Integer, Image> infantryImages = new HashMap<>();
	Map<Integer, Image> cavalryImages = new HashMap<>();

	@FXML
	ImageView worldmap;
	@FXML
	ImageView AU0, AU1, AU2, AU3, AF0, AF1, AF2, AF3, AF4, AF5, SA0, SA1, SA2,
			SA3, EU0, EU1, EU2, EU3, EU4, EU5, EU6, NA0, NA1, NA2, NA3, NA4,
			NA5, NA6, NA7, NA8, AS0, AS1, AS2, AS3, AS4, AS5, AS6, AS7, AS8,
			AS9, AS10, AS11;

	ArrayList<GUITerritory> clickableTerritories;

	public MapControl() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
				"MapControl.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	public void initialise() {
		loadArmyIcons();

		DefaultMap territories = new DefaultMap(AU0, AU1, AU2, AU3, AF0, AF1,
				AF2, AF3, AF4, AF5, SA0, SA1, SA2, SA3, EU0, EU1, EU2, EU3,
				EU4, EU5, EU6, NA0, NA1, NA2, NA3, NA4, NA5, NA6, NA7, NA8,
				AS0, AS1, AS2, AS3, AS4, AS5, AS6, AS7, AS8, AS9, AS10, AS11);

		clickableTerritories = territories.getTerritoryList();

		Label label;
		for (final GUITerritory territory : clickableTerritories) {
			// Add territory
			territoryByID.put(territory.getId(), territory);
			territoryByImageView.put(territory.getImage(), territory);

			// Mouse events
			addMouseHoverEvents(territory);

			// Army
			label = new Label();
			territory.setArmyLabel(label);
			getChildren().add(label);
		}
	}

	void loadArmyIcons() {
		for (int i = 1; i < 7; i++) {
			artilleryImages.put(
					i,
					new Image(getClass().getResourceAsStream(
							"army/artillery_player" + i + ".png")));
			infantryImages.put(
					i,
					new Image(getClass().getResourceAsStream(
							"army/infantry_player" + i + ".png")));
			cavalryImages.put(
					i,
					new Image(getClass().getResourceAsStream(
							"army/cavalry_player" + i + ".png")));
		}
	}

	void addMouseHoverEvents(GUITerritory territory) {
		territory.getImage().addEventFilter(MouseEvent.MOUSE_ENTERED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent mouseEvent) {
						if (territory.getImage().getOpacity() < 100)
							territory.getImage().setOpacity(100);
					}
				});

		territory.getImage().addEventFilter(MouseEvent.MOUSE_EXITED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent mouseEvent) {
						territory.getImage().setOpacity(0);
					}
				});
	}

	public void updateTerritory(int playerID, int numberOfArmies,
			GUITerritory territory) {

		ArmyClass oldClass = territory.getArmyClass();
		int oldOwnerID = territory.getOwnerID();
		int oldNumberOfArmies = territory.getNumberOfArmies();

		territory.setOwnerID(playerID);
		territory.setNumberOfArmies(numberOfArmies);

		Label label = territory.getArmyLabel();

		// Update image (if required)
		updateTerritoryImage(territory, label, oldOwnerID, oldClass);

		// Update number
		if (territory.getNumberOfArmies() != oldNumberOfArmies) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					label.setText(Integer.toString(numberOfArmies));
				}
			});
		}
	}

	void updateTerritoryImage(GUITerritory territory, Label label,
			int oldOwnerID, ArmyClass oldClass) {
		if (territory.getOwnerID() != oldOwnerID
				|| territory.getArmyClass() != oldClass) {

			ImageView image = null;
			int labelOffset = 0;

			switch (territory.getArmyClass()) {
			 case Artillery:
			 image = new ImageView(artilleryImages.get(territory
			 .getOwnerID()));
			 labelOffset = -162;
			 break;
			 case Cavalry:
			 image = new ImageView(cavalryImages.get(territory.getOwnerID()));
			 labelOffset = -110;
			 break;
			 case Infantry:
			 image = new ImageView(
			 infantryImages.get(territory.getOwnerID()));
			 labelOffset = -94;
			 break;
			default:
				image = null;
				break;
			}

			double x = territory.getImage().getLayoutX()
					+ territory.getImage().getImage().getWidth() / 2;
			double y = territory.getImage().getLayoutY()
					+ territory.getImage().getImage().getHeight() / 2;

			if (image != null) {
				image.setScaleX(0.25f);
				image.setScaleY(0.25f);

				if (image.getImage() != null) {
					x -= image.getImage().getWidth() / 2;
					y -= image.getImage().getHeight() / 2;
				}
			}

			final ImageView finalImage = image;
			final int finalLabelOffset = labelOffset;
			final double xF = x;
			final double yF = y;

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					if (finalImage != null)
						label.setGraphic(finalImage);
					label.setGraphicTextGap(finalLabelOffset);

					label.setContentDisplay(ContentDisplay.TOP);
					label.setMouseTransparent(true);

					label.relocate(xF, yF);
				}
			});
		}
	}

	// ================================================================================
	// Accessors
	// ================================================================================

	public ArrayList<GUITerritory> getClickableTerritories() {
		return clickableTerritories;
	}

	public GUITerritory getTerritoryByID(int id) {
		return territoryByID.get(id);
	}
}