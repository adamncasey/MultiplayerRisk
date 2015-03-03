package gameUI;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import java.util.Timer;
import java.util.TimerTask;

public class Controller implements Initializable{

    @FXML //  fx:id="gamePane"
    public Pane gamePane;

    @FXML //  fx:id="console"
    public TextArea console;

    @FXML
    public ImageView worldmap;

    @FXML //
    public Button addArmies;
    
    @FXML //
    public Button removeArmies;
    
    @FXML //
    public Button setArmies;
    
	@FXML //territories
    public ImageView AU0, AU1, AU2, AU3, AF0, AF1, AF2, AF3, AF4, AF5, SA0, SA1, SA2, SA3, EU0, EU1, EU2, EU3, EU4, EU5, EU6, NA0, NA1, NA2, NA3, NA4, NA5, NA6, NA7, NA8, AS0, AS1, AS2, AS3, AS4, AS5, AS6, AS7, AS8, AS9, AS10, AS11;
    
    Image infantryImage = new Image(getClass().getResourceAsStream("armies/infantry.png"));
    Image cavalryImage = new Image(getClass().getResourceAsStream("armies/cavalry.png"));
    Image artilleryImage = new Image(getClass().getResourceAsStream("armies/artillery.png"));
    
    private double x_over_y;
    private double global_X, global_Y;
    
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

    public void set_armies(int playerID, int number, GUITerritory territory){
    	double sizex = territory.getImage().getImage().getWidth();
        double sizey = territory.getImage().getImage().getHeight();
    	
    	double x = territory.getImage().getLayoutX() + sizex/2;
        double y = territory.getImage().getLayoutY() + sizey/2;
        
        printToConsole(territory.getImage().getId()+ " " + x + " " + y + " " + sizex + " " + sizey);
        
        if(addArmyMode) {
        	ImageView army = null;
        	if(number < 5){
        		army = new ImageView(infantryImage);
        	}
        	if(number >=5 && number <20){
        		army = new ImageView(cavalryImage);
        	}
        	if(number >=20){
        		army = new ImageView(artilleryImage);
        	}
        	army.setScaleX(0.5);
        	army.setScaleY(0.5);
        	x-=army.getImage().getWidth()/2;
        	y-=army.getImage().getHeight()/2;
        	army.relocate(x, y);
        	gamePane.getChildren().add(army);
        	
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    	x_over_y = gamePane.getPrefWidth() / gamePane.getPrefHeight();
    	
    	DefaultMap default_map = new DefaultMap(AU0, AU1, AU2, AU3, AF0, AF1, AF2, AF3, AF4, AF5, SA0, SA1, SA2, SA3, EU0, EU1, EU2, EU3, EU4, EU5, EU6, NA0, NA1, NA2, NA3, NA4, NA5, NA6, NA7, NA8, AS0, AS1, AS2, AS3, AS4, AS5, AS6, AS7, AS8, AS9, AS10, AS11);
    	default_map.getTerritoryList();
    	
        ArrayList<GUITerritory> highlighted_all = default_map.getTerritoryList();
        
        System.out.println(highlighted_all.get(0).getImage().getLayoutX());

        for(final GUITerritory territory : highlighted_all){

            territory.getImage().addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(territory.getImage().getOpacity() < 100)
                        territory.getImage().setOpacity(100);
                }
            });

            territory.getImage().addEventFilter(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    territory.getImage().setOpacity(0);
                }
            });
            
            territory.getImage().addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    
                	set_armies(1, 20, territory);
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
        
        //gamePane.widthProperty().addListener(listener);
        //gamePane.heightProperty().addListener(listener);

        gamePane.addEventFilter(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //
            }
        });

    }
  
    
 // create a listener
    final ChangeListener<Number> listener = new ChangeListener<Number>(){
    	

    	
      final Timer timer = new Timer(); // uses a timer to call your resize method
      TimerTask task = null; // task to execute after defined delay
      final long delayTime = 200; // delay that has to pass in order to consider an operation done

      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, final Number newValue)
      {
    	  
        if (task != null)
        { // there was already a task scheduled from the previous operation ...
          task.cancel(); // cancel it, we have a new size to consider
        }

        task = new TimerTask() // create new task that calls your resize operation
        {
          @Override
          public void run()
          { 

            // resize code here
          }

        };
        // schedule new task
        timer.schedule(task, delayTime);
      }
    };
}
