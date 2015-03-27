package ui.game.map;

import javafx.scene.image.ImageView;
import logic.state.Territory;

public class GUITerritory {
	String name;
	int id;
	int continent_id;

	public Territory getTerritory() {
		return territory;
	}

	public void setTerritory(Territory territory) {
		this.territory = territory;
	}

	Territory territory;

	public int getContinent_id() {
		return continent_id;
	}

	public void setContinent_id(int continent_id) {
		this.continent_id = continent_id;
	}

	boolean selected;
	ImageView image;
	String imgPath;
	String armyID; //id of the army node associated with the territory
	
	public String getArmyID() {
		return armyID;
	}

	public void setArmyID(String string) {
		this.armyID = string;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public ImageView getImage() {
		return image;
	}

	public void setImage(ImageView image) {
		this.image = image;
	}

	public GUITerritory(String name){
		this.name = name;
		selected = false;
	}
	
	public GUITerritory(int id, int continent_id){
		this("", id, continent_id);
	}
	
	public GUITerritory(String name, int id, int continent_id) {
		this.name = name;
		this.id = id;
		this.continent_id = id;
	}
	
	public GUITerritory(String name, int id, int continent_id, ImageView img) {
		this.name = name;
		this.id = id;
		this.continent_id = id;
		this.image = img;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
