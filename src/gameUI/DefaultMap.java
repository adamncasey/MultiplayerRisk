package gameUI;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class DefaultMap {
	
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
    
    public DefaultMap(ImageView AU0, ImageView AU1, ImageView AU2, ImageView AU3, ImageView AF0, ImageView AF1, ImageView AF2, ImageView AF3, ImageView AF4, ImageView AF5, ImageView SA0, ImageView SA1, ImageView SA2, ImageView SA3, ImageView EU0, ImageView EU1, ImageView EU2, ImageView EU3, ImageView EU4, ImageView EU5, ImageView EU6, ImageView NA0, ImageView NA1, ImageView NA2, ImageView NA3, ImageView NA4, ImageView NA5, ImageView NA6, ImageView NA7, ImageView NA8, ImageView AS0, ImageView AS1, ImageView AS2, ImageView AS3, ImageView AS4, ImageView AS5, ImageView AS6, ImageView AS7, ImageView AS8, ImageView AS9, ImageView AS10, ImageView AS11){
    	this.AF0 = AF0;
    	this.AF1 = AF1;
    	this.AF2 = AF2;
    	this.AF3 = AF3;
    	this.AF4 = AF4;
    	this.AF5 = AF5;
    	this.AS0 = AS0;
    	this.AS1 = AS1;
    	this.AS2 = AS2;
    	this.AS3 = AS3;
    	this.AS4 = AS4;
    	this.AS5 = AS5;
    	this.AS6 = AS6;
    	this.AS7 = AS7;
    	this.AS8 = AS8;
    	this.AS9 = AS9;
    	this.AS10 = AS10;
    	this.AS11 = AS11;
    	this.AU0 = AU0;
    	this.AU1 = AU1;
    	this.AU2 = AU2;
    	this.AU3 = AU3;
    	this.NA0 = NA0;
    	this.NA1 = NA1;
    	this.NA2 = NA2;
    	this.NA3 = NA3;
    	this.NA4 = NA4;
    	this.NA5 = NA5;
    	this.NA6 = NA6;
    	this.NA7 = NA7;
    	this.NA8 = NA8;
    	this.SA0 = SA0;
    	this.SA1 = SA1;
    	this.SA2 = SA2;
    	this.SA3 = SA3;
    	this.EU0 = EU0;
    	this.EU1 = EU1;
    	this.EU2 = EU2;
    	this.EU3 = EU3;
    	this.EU4 = EU4;
    	this.EU5 = EU5;
    	this.EU6 = EU6;
    }
    
	public ArrayList<GUITerritory> getTerritoryList(){
		ArrayList<GUITerritory> list = new ArrayList<GUITerritory>();
		list.add(new GUITerritory("AF0",0,3,AF0));
		list.add(new GUITerritory("AF1",1,3,AF1));
		list.add(new GUITerritory("AF2",2,3,AF2));
		list.add(new GUITerritory("AF3",3,3,AF3));
		list.add(new GUITerritory("AF4",4,3,AF4));
		list.add(new GUITerritory("AF5",5,3,AF5));
		list.add(new GUITerritory("AU0",0,5,AU0));
		list.add(new GUITerritory("AU1",1,5,AU1));
		list.add(new GUITerritory("AU2",2,5,AU2));
		list.add(new GUITerritory("AU3",3,5,AU3));
		list.add(new GUITerritory("SA0",0,1,SA0));
		list.add(new GUITerritory("SA1",1,1,SA1));
		list.add(new GUITerritory("SA2",2,1,SA2));
		list.add(new GUITerritory("SA3",3,1,SA3));
		list.add(new GUITerritory("EU0",0,2,EU0));
		list.add(new GUITerritory("EU1",1,2,EU1));
		list.add(new GUITerritory("EU2",2,2,EU2));
		list.add(new GUITerritory("EU4",3,2,EU3));
		list.add(new GUITerritory("EU5",4,2,EU4));
		list.add(new GUITerritory("EU6",5,2,EU5));
		list.add(new GUITerritory("NA0",0,0,NA0));
		list.add(new GUITerritory("NA1",1,0,NA1));
		list.add(new GUITerritory("NA2",2,0,NA2));
		list.add(new GUITerritory("NA3",3,0,NA3));
		list.add(new GUITerritory("NA4",4,0,NA4));
		list.add(new GUITerritory("NA5",5,0,NA5));
		list.add(new GUITerritory("NA6",6,0,NA6));
		list.add(new GUITerritory("NA7",7,0,NA7));
		list.add(new GUITerritory("NA8",8,0,NA8));
		list.add(new GUITerritory("AS0",0,4,AS0));
		list.add(new GUITerritory("AS1",1,4,AS1));
		list.add(new GUITerritory("AS2",2,4,AS2));
		list.add(new GUITerritory("AS3",3,4,AS3));
		list.add(new GUITerritory("AS4",4,4,AS4));
		list.add(new GUITerritory("AS5",5,4,AS5));
		list.add(new GUITerritory("AS6",6,4,AS6));
		list.add(new GUITerritory("AS7",7,4,AS7));
		list.add(new GUITerritory("AS8",8,4,AS8));
		list.add(new GUITerritory("AS9",8,4,AS9));
		list.add(new GUITerritory("AS10",10,4,AS10));
		list.add(new GUITerritory("AS11",11,4,AS11));

		return list;
	}
}
