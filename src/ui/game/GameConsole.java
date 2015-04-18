package ui.game;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class GameConsole {
	TextArea textArea;

	final int MAX_CONSOLE_CHARACTERS = 50000;
	final int CLAMPED_CONSOLE_CHARACTERS = 10000;
	
	public GameConsole(TextArea textArea) {
		this.textArea = textArea;
	}
	
	public void write(String s) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if(textArea.getLength() > MAX_CONSOLE_CHARACTERS) {
					String text = textArea.getText();

					text = text.substring(text.length() - CLAMPED_CONSOLE_CHARACTERS, text.length());

					textArea.setText(text + (s + "\n"));
					return;
				}
				textArea.appendText(s + "\n");
				return;
			}
		});
	}
	
	public void clear() {
		textArea.clear();
	}
}
