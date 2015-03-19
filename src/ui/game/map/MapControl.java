package ui.game.map;

import java.io.IOException;
import java.util.ArrayList;

import ui.game.GameConsole;
import ui.game.GameController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

public class MapControl extends Pane {

	@FXML
	public ImageView worldmap;
	@FXML
	public ImageView AU0, AU1, AU2, AU3, AF0, AF1, AF2, AF3, AF4, AF5, SA0,
			SA1, SA2, SA3, EU0, EU1, EU2, EU3, EU4, EU5, EU6, NA0, NA1, NA2,
			NA3, NA4, NA5, NA6, NA7, NA8, AS0, AS1, AS2, AS3, AS4, AS5, AS6,
			AS7, AS8, AS9, AS10, AS11;
	
	Image infantryImage = new Image(getClass().getResourceAsStream(
			"army/infantry.png"));
	Image cavalryImage = new Image(getClass().getResourceAsStream(
			"army/cavalry.png"));
	Image artilleryImage = new Image(getClass().getResourceAsStream(
			"army/artillery.png"));

	DefaultMap territories;
	public enum ArmyMode { SET, ADD, REMOVE }
	private ArmyMode armyMode;

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

		territories = new DefaultMap(AU0, AU1, AU2, AU3, AF0, AF1, AF2, AF3,
				AF4, AF5, SA0, SA1, SA2, SA3, EU0, EU1, EU2, EU3, EU4, EU5,
				EU6, NA0, NA1, NA2, NA3, NA4, NA5, NA6, NA7, NA8, AS0, AS1,
				AS2, AS3, AS4, AS5, AS6, AS7, AS8, AS9, AS10, AS11);
		
		ArrayList<GUITerritory> highlighted_all = territories
				.getTerritoryList();

		System.out.println(highlighted_all.get(0).getImage().getLayoutX());

		for (final GUITerritory territory : highlighted_all) {

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

			territory.getImage().addEventFilter(MouseEvent.MOUSE_CLICKED,
					new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent mouseEvent) {

							set_armies(1, 20, territory);
						}
					});
		}

		this.addEventFilter(MouseEvent.MOUSE_CLICKED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent mouseEvent) {
						System.out.print("Mouse click at: "
								+ mouseEvent.getSceneX() + ", "
								+ mouseEvent.getSceneY() + "\n");
					}
				});

	}
	
	public void set_armies(int playerID, int number, GUITerritory territory) {
		double sizex = territory.getImage().getImage().getWidth();
		double sizey = territory.getImage().getImage().getHeight();

		double x = territory.getImage().getLayoutX() + sizex / 2;
		double y = territory.getImage().getLayoutY() + sizey / 2;

		GameController.console.write(territory.getImage().getId() + " " + x + " " + y + " "
				+ sizex + " " + sizey);

		if (armyMode == ArmyMode.ADD) {
			ImageView army = null;
			if (number < 5) {
				army = new ImageView(infantryImage);
			}
			if (number >= 5 && number < 20) {
				army = new ImageView(cavalryImage);
			}
			if (number >= 20) {
				army = new ImageView(artilleryImage);
			}
			army.setScaleX(0.5);
			army.setScaleY(0.5);
			x -= army.getImage().getWidth() / 2;
			y -= army.getImage().getHeight() / 2;
			army.relocate(x, y);
			getChildren().add(army);
		}
	}
	
	public ArmyMode getArmyMode() {
		return armyMode;
	}

	public void setArmyMode(ArmyMode armyMode) {
		this.armyMode = armyMode;
	}

}