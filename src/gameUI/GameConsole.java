package gameUI;

import javafx.scene.control.TextArea;

public class GameConsole {
	TextArea textArea;
	
	public GameConsole(TextArea textArea) {
		this.textArea = textArea;
	}
	
	public void write(String s) {
		textArea.appendText(s + "\n");
	}
	
	public void clear() {
		textArea.clear();
	}
}
