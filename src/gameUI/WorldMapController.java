package gameUI;

import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class WorldMapController {
	
	public enum ArmyMode { SET, ADD, REMOVE }
	private ArmyMode armyMode;

	GameConsole console;
	
	Pane mapPane;

	Image infantryImage = new Image(getClass().getResourceAsStream(
			"armies/infantry.png"));
	Image cavalryImage = new Image(getClass().getResourceAsStream(
			"armies/cavalry.png"));
	Image artilleryImage = new Image(getClass().getResourceAsStream(
			"armies/artillery.png"));

	public WorldMapController(Pane pane, GameConsole console, DefaultMap territories) {
		this.mapPane = pane;
		this.console = console;
		
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

		mapPane.addEventFilter(MouseEvent.MOUSE_CLICKED,
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

		console.write(territory.getImage().getId() + " " + x + " " + y + " "
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
			mapPane.getChildren().add(army);
		}
	}
	
	public ArmyMode getArmyMode() {
		return armyMode;
	}

	public void setArmyMode(ArmyMode armyMode) {
		this.armyMode = armyMode;
	}
}
