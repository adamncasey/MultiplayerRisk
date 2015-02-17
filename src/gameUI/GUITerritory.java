package gameUI;

import javafx.scene.image.ImageView;

public class GUITerritory {
	String name;
	boolean selected;
	ImageView image;
	String imgPath;
	
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
		this("");
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
