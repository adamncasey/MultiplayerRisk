package ui.game.map;

import ui.game.map.MapControl.ArmyClass;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class GUITerritory {

	private String name;
	private int id;
	private int continent_id;
	private int numberOfArmies;
	private ImageView image;
	private Label armyLabel;
	private int ownerID;
	private boolean isSelected;

	public GUITerritory(String name, int id, int continent_id, ImageView img) {
		this.name = name;
		this.id = id;
		this.continent_id = id;
		this.image = img;
	}

	public ArmyClass getArmyClass() {
		if (numberOfArmies == 0) {
			return ArmyClass.None;
		}
		if (numberOfArmies < 5) {
			return ArmyClass.Infantry;
		} else if (numberOfArmies < 10) {
			return ArmyClass.Cavalry;
		} else {
			return ArmyClass.Artillery;
		}
	}

	// ================================================================================
	// Accessors
	// ================================================================================

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getContinent_id() {
		return continent_id;
	}

	public void setContinent_id(int continent_id) {
		this.continent_id = continent_id;
	}

	public int getNumberOfArmies() {
		return numberOfArmies;
	}

	public void setNumberOfArmies(int armyQuantity) {
		this.numberOfArmies = armyQuantity;
	}

	public ImageView getImage() {
		return image;
	}

	public Label getArmyLabel() {
		return armyLabel;
	}

	public void setArmyLabel(Label armyLabel) {
		this.armyLabel = armyLabel;
	}

	public int getOwnerID() {
		return ownerID - 1;
	}

	public void setOwnerID(int ownerID) {
		this.ownerID = ownerID + 1;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		if(this.isSelected != isSelected) {
			this.isSelected = isSelected;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					if (isSelected)
						GUITerritory.this.getImage().setOpacity(100);
					else
						GUITerritory.this.getImage().setOpacity(0);
				}
			});
		}
	}
}
