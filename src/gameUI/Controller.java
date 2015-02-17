package gameUI;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.event.Event.*;
import javafx.event.EventHandler;

import java.lang.management.MonitorInfo;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    @FXML //  fx:id="gamePane"
    public Pane gamePane;

    @FXML //  fx:id="console"
    public TextArea console;

    @FXML //Australia
    public ImageView AU0, AU1, AU2, AU3;

    @FXML //Africa
    public ImageView AF0, AF1, AF2, AF3, AF4, AF5;

    @FXML //South America
    public ImageView SA0, SA1, SA2, SA3;

    @FXML //Europe
    public ImageView EU0, EU1, EU2, EU3, EU4, EU5, EU6;

    @FXML //North America
    public ImageView NA0, NA1, NA2, NA3, NA4, NA5, NA6, NA7, NA8;

    @FXML //Asia
    public ImageView AS0, AS1, AS2, AS3, AS4, AS5, AS6, AS7, AS8, AS9, AS10, AS11;

    @FXML //
    public Button addArmies;
    
    @FXML //
    public Button removeArmies;
    
    @FXML //
    public Button setArmies;
    
    Image infantryImage = new Image(getClass().getResourceAsStream("armies/infantry.gif"));
    
    // modes
    public boolean setArmyMode, addArmyMode, removeArmyMode;
    
    public void setArmyMode(String s){
    	if(s.equals("add")){
    		setArmyMode=false;
    		addArmyMode=true;
    		removeArmyMode=false;
    		printToConsole("In army adding mode.");
    		return;
    	}
    	if(s.equals("set")){
    		setArmyMode=true;
    		addArmyMode=false;
    		removeArmyMode=false;
    		printToConsole("In army setting mode.");
    		return;
    	}
    	if(s.equals("remove")){
    		setArmyMode=false;
    		addArmyMode=false;
    		removeArmyMode=true;
    		printToConsole("In army removing mode mode.");
    		return;
    	}
    	setArmyMode=false;
		addArmyMode=false;
		removeArmyMode=false;
		printToConsole("All army modes off.");
    }
    
    public void printToConsole(String message){
        console.appendText(message + "\n");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ArrayList<ImageView> highlighted_all = new ArrayList<ImageView>();
        highlighted_all.add(AF0);
        highlighted_all.add(AF1);
        highlighted_all.add(AF2);
        highlighted_all.add(AF3);
        highlighted_all.add(AF4);
        highlighted_all.add(AF5);
        highlighted_all.add(AS0);
        highlighted_all.add(AS1);
        highlighted_all.add(AS2);
        highlighted_all.add(AS3);
        highlighted_all.add(AS4);
        highlighted_all.add(AS5);
        highlighted_all.add(AS6);
        highlighted_all.add(AS7);
        highlighted_all.add(AS8);
        highlighted_all.add(AS9);
        highlighted_all.add(AS10);
        highlighted_all.add(AS11);
        highlighted_all.add(NA0);
        highlighted_all.add(NA1);
        highlighted_all.add(NA2);
        highlighted_all.add(NA3);
        highlighted_all.add(NA4);
        highlighted_all.add(NA5);
        highlighted_all.add(NA6);
        highlighted_all.add(NA7);
        highlighted_all.add(NA8);
        highlighted_all.add(AU0);
        highlighted_all.add(AU1);
        highlighted_all.add(AU2);
        highlighted_all.add(AU3);
        highlighted_all.add(SA0);
        highlighted_all.add(SA1);
        highlighted_all.add(SA2);
        highlighted_all.add(SA3);
        highlighted_all.add(EU0);
        highlighted_all.add(EU1);
        highlighted_all.add(EU2);
        highlighted_all.add(EU3);
        highlighted_all.add(EU4);
        highlighted_all.add(EU5);
        highlighted_all.add(EU6);

        for(final ImageView territory : highlighted_all){

            territory.addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(territory.getOpacity() < 100)
                        territory.setOpacity(100);
                }
            });

            territory.addEventFilter(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    territory.setOpacity(0);
                }
            });
            
            territory.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    
                	double sizex = territory.getImage().getWidth();
                    double sizey = territory.getImage().getHeight();
                	
                	double x = territory.getLayoutX() + sizex/8;
                    double y = territory.getLayoutY() + sizey/8;
                    
                    printToConsole(territory.getId()+ " " + x + " " + y + " " + sizex + " " + sizey);
                    
                    if(addArmyMode) {
                    	Label infantry = new Label();
                    	infantry.setGraphic(new ImageView(infantryImage));
                    	infantry.relocate(x, y);
                    	gamePane.getChildren().add(infantry);
                    }
                }
            });
        }
        
        addArmies.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				setArmyMode("add");
			}
        	
        });
        
        removeArmies.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				setArmyMode("remove");
			}
        	
        });
        
        setArmies.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				setArmyMode("set");
			}
        	
        });

        gamePane.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.print("Mouse click at: " + mouseEvent.getSceneX() + ", " + mouseEvent.getSceneY() + "\n" );
            }
        });

        gamePane.addEventFilter(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //
            }
        });

    }
}
