package ui.directconnect;

import java.awt.Color;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import ui.*;

public class DirectConnectController extends AnchorPane implements
		Initializable {

	private Main application;

	public void setApp(Main application) {
		this.application = application;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	@FXML
	private Label connectionStatus;
	
	@FXML
	private ProgressIndicator progressRing;

	@FXML
	protected void handleSubmitButtonAction(ActionEvent event) {
		progressRing.setVisible(true);
		connectionStatus.setText("connecting...");
	}
	
	@FXML
	protected void backButtonAction(ActionEvent event) {
		application.gotoMenu();
	}
}
