package ui.game;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class GameConsole {
	TextArea textArea;
	
	public GameConsole(TextArea textArea) {
		this.textArea = textArea;
	}
	
	public void write(String s) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				textArea.appendText(s + "\n");
			}
		});
	}
	
	public void clear() {
		textArea.clear();
	}
}
