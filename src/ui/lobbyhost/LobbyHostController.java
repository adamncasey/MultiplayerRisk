package ui.lobbyhost;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import ui.*;

public class LobbyHostController extends AnchorPane implements Initializable {

	private Main application;

	@FXML
	private ListView<String> players;
	@FXML
	private TextArea consoleWindow;
	
	private ObservableList<String> playersList;

	public void setApp(Main application) {
		this.application = application;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ObservableList<String> items = FXCollections.observableArrayList("You (Hosting)" , "Player2", "Player3", "Player4");
		playersList = items;
		players.setItems(playersList);
	}

	@FXML
	protected void backButtonAction(ActionEvent event) {
		application.gotoMenu();
	}

	@FXML
	protected void startButtonAction(ActionEvent event) {
		playersList.add("Player #");
		consoleWindow.appendText("-> You added player #\n");
	}
	
	@FXML
	protected void kickPlayerButtonAction(ActionEvent event) {
		int selectedIndex = players.getSelectionModel().getSelectedIndex();
		String selected = playersList.get(selectedIndex);
		if(selectedIndex >= 0) {
			playersList.remove(selectedIndex);
			consoleWindow.appendText(String.format("-> You kicked %s\n", selected));
		}
	}
}
