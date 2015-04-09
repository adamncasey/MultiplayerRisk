package ui.game.map;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;
import ui.game.GameConsole;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

public class MapControl extends Pane{

	GameConsole console;

	HashMap<String, GUITerritory> nameIndex = new HashMap<>();
	HashMap<GUITerritory, Node> armyMapping = new HashMap<>();
	HashMap<ImageView, GUITerritory> imageMapping = new HashMap<>();
	HashMap<GUITerritory, Integer> ownershipMapping = new HashMap<>();

	@FXML
	public ImageView worldmap;
	@FXML
	public ImageView AU0, AU1, AU2, AU3, AF0, AF1, AF2, AF3, AF4, AF5, SA0,
			SA1, SA2, SA3, EU0, EU1, EU2, EU3, EU4, EU5, EU6, NA0, NA1, NA2,
			NA3, NA4, NA5, NA6, NA7, NA8, AS0, AS1, AS2, AS3, AS4, AS5, AS6,
			AS7, AS8, AS9, AS10, AS11;

	private float armyScalingFactor = (float) 0.25;
	private ArrayList<GUITerritory> highlighted_all;

	private EventHandler<MouseEvent> mouseOverFocus;
	{
		mouseOverFocus = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				try {

					ImageView source = (ImageView) mouseEvent.getSource();
					GUITerritory territory = getTerritoryByImageView(source);

					for (Node child : getChildren()) {
							if (territory.getArmyID() != null
									&& child.getId() != null
									&& child.getId().equals(territory.getArmyID())) {
								child.toFront();
							}
					}
				} catch (Exception e) {
					// Do nothing, because it means nothing in this context.
				}
			}
		};
	}

	public boolean selectTerritory(GUITerritory t){

		if (t.isSelected()){
			unselectTerritory(t);
			return false;
		}
		else{
			t.getImage().setOpacity(100);
			t.setSelected(true);
		}
		return true;
	}

	public boolean unselectTerritory(GUITerritory t){
		t.getImage().setOpacity(0);
		t.setSelected(false);
		return true;
	}

	private GUITerritory getTerritoryByImageView(ImageView img) {
		return imageMapping.get(img);
	}

	public GUITerritory getTerritoryByName(String name) {
		return nameIndex.get(name);
	}

	private String generateRandomID() {
		SecureRandom random = new SecureRandom();
		return (new BigInteger(130, random).toString(32));
	}

	DefaultMap territories;

	public enum ArmyMode {
		SET, ADD, REMOVE
	}

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

	public void initialise(GameConsole console) {
		this.console = console;

		territories = new DefaultMap(AU0, AU1, AU2, AU3, AF0, AF1, AF2, AF3,
				AF4, AF5, SA0, SA1, SA2, SA3, EU0, EU1, EU2, EU3, EU4, EU5,
				EU6, NA0, NA1, NA2, NA3, NA4, NA5, NA6, NA7, NA8, AS0, AS1,
				AS2, AS3, AS4, AS5, AS6, AS7, AS8, AS9, AS10, AS11);

		highlighted_all = territories.getTerritoryList();

		for (final GUITerritory territory : highlighted_all) {
			nameIndex.put(territory.getName(), territory);
			imageMapping.put(territory.getImage(), territory);

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
							Random generator = new Random();
							int x = generator.nextInt(6) + 1;
							int y = generator.nextInt(25) + 1;
							setArmies(x, y, territory);
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

	public void setArmies(int playerID, int number, GUITerritory territory) {

		if(territory == null)
			return;

		if(number == 0)
			return;

		if(ownershipMapping!=null) {
			if (ownershipMapping.get(territory)!=null && ownershipMapping.get(territory) == playerID) {
				if (territory.getArmyQuantity() == number) {
					return;
				}
			}
		}

		removeArmy(territory);
		ownershipMapping.put(territory, playerID);

		Image infantryImage = new Image(getClass().getResourceAsStream(
				"army/infantry_player" + playerID + ".png"));
		Image cavalryImage = new Image(getClass().getResourceAsStream(
				"army/cavalry_player" + playerID + ".png"));
		Image artilleryImage = new Image(getClass().getResourceAsStream(
				"army/artillery_player" + playerID + ".png"));
		
		Label army = new Label(Integer.toString(number));
		territory.setArmyQuantity(number);
		ImageView currentImage = null;
		
		double sizex = territory.getImage().getImage().getWidth();
		double sizey = territory.getImage().getImage().getHeight();

		double x = territory.getImage().getLayoutX() + sizex / 2;
		double y = territory.getImage().getLayoutY() + sizey / 2;

		
			int labelOffset = 0;
			if (number < 5) {			
				currentImage = new ImageView(infantryImage);
				labelOffset = -94; //ignore_this
			}
			if (number >= 5 && number < 20) {				
				currentImage = new ImageView(cavalryImage);
				labelOffset = -110;
			}
			if (number >= 20) {
				currentImage = new ImageView(artilleryImage);
				labelOffset = -162;
			}

			currentImage.setScaleX(armyScalingFactor);
			currentImage.setScaleY(armyScalingFactor);
			
			x -= currentImage.getImage().getWidth() / 2;
			y -= currentImage.getImage().getHeight() / 2;
			
			army.setGraphic(currentImage);
			army.setGraphicTextGap(labelOffset);
			
			army.setContentDisplay(ContentDisplay.TOP);
			army.setMouseTransparent(true);

			army.relocate(x, y);

			army.setId(generateRandomID());
			territory.setArmyID(army.getId());

			armyMapping.put(territory, army);

			territory.getImage().addEventFilter(MouseEvent.MOUSE_ENTERED,
					mouseOverFocus);

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					getChildren().add(army);
				}
			});
			

	}

	public void removeArmy(GUITerritory territory) {

		if (territory == null) {
			return;
		}

		if (territory.getArmyID() == null) {
			return;
		}

		ownershipMapping.remove(territory);

		Node toRemove = armyMapping.get(territory);

		ObservableList<Node> children = getChildren();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				children.remove(toRemove);
			}
		});

	}

	public ArmyMode getArmyMode() {
		return armyMode;
	}

	public void setArmyMode(ArmyMode armyMode) {
		this.armyMode = armyMode;
	}

}