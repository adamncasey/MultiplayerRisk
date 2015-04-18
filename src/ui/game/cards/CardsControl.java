package ui.game.cards;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import logic.Card;
import logic.state.Board;
import logic.state.GameState;
import player.IPlayer;

import javax.xml.stream.EventFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by 120014299 on 16/04/15.
 */
public class CardsControl extends BorderPane{

    @FXML
    ImageView cell0, cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8, cell9, cell10, cell11, cell12, cell13, cell14, cell15, cell16, cell17, cell18, cell19, cell20, cell21, cell22, cell23, cell24, cell25, cell26, cell27, cell28, cell29, cell30, cell31, cell32, cell33, cell34, cell35, cell36, cell37, cell38, cell39, cell40, cell41;
    ImageView[] cells = {cell0, cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8, cell9, cell10, cell11, cell12, cell13, cell14, cell15, cell16, cell17, cell18, cell19, cell20, cell21, cell22, cell23, cell24, cell25, cell26, cell27, cell28, cell29, cell30, cell31, cell32, cell33, cell34, cell35, cell36, cell37, cell38, cell39, cell40, cell41};

    ImageView noCard;

    boolean[] cellIsFree = new boolean[42];

    private boolean isDiceMoveCompleted = false;

    private BooleanProperty isResultsVisible = new SimpleBooleanProperty(false);

    public boolean getIsResultsVisible() {
        return isResultsVisible.get();
    }

    public void setIsResultsVisible(boolean value) {
        isResultsVisible.set(value);
    }


    private int selectionCount = 0;
    private final int MAX_SELECT = 3;
    ArrayList<String> selected;

    HashMap<String, String> cardNameMapping = new HashMap<>();
    HashMap<String, ImageView> cardImageMapping = new HashMap<>();
    HashMap<ImageView, String> imageCardMapping = new HashMap<>();
    HashMap<String, Boolean> selection = new HashMap<>();

    private static String[] NAMES = {"Alaska", "Northwest Territory", "Greenland", "Alberta", "Ontario", "Quebec",
            "Western United States", "Eastern United States", "Central America", "Venezuela",
            "Peru", "Brazil", "Argentina", "Iceland", "Scandinavia", "Ukraine", "Great Britain",
            "Northern Europe", "Western Europe", "Southern Europe", "North Africa", "Egypt",
            "Congo", "East Africa", "South Africa", "Madagascar", "Ural", "Siberia", "Yakutsk",
            "Kamchatka", "Irkutsk", "Mongolia", "Japan", "Afghanistan", "China", "Middle East",
            "India", "Siam", "Indonesia", "New Guinea", "Western Australia", "Eastern Australia"};

    public CardsControl() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "Cards.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        noCard = getCard("nocard");

        populateCardMap();

        cells[0] = cell0;
        cells[1] = cell1;
        cells[2] = cell2;
        cells[3] = cell3;
        cells[4] = cell4;
        cells[5] = cell5;
        cells[6] = cell6;
        cells[7] = cell7;
        cells[8] = cell8;
        cells[9] = cell9;
        cells[10] = cell10;
        cells[11] = cell11;
        cells[12] = cell12;
        cells[13] = cell13;
        cells[14] = cell14;
        cells[15] = cell15;
        cells[16] = cell16;
        cells[17] = cell17;
        cells[18] = cell18;
        cells[19] = cell19;
        cells[20] = cell20;
        cells[21] = cell21;
        cells[22] = cell22;
        cells[23] = cell23;
        cells[24] = cell24;
        cells[25] = cell25;
        cells[26] = cell26;
        cells[27] = cell27;
        cells[28] = cell28;
        cells[29] = cell29;
        cells[30] = cell30;
        cells[31] = cell31;
        cells[32] = cell32;
        cells[33] = cell33;
        cells[34] = cell34;
        cells[35] = cell35;
        cells[36] = cell36;
        cells[37] = cell37;
        cells[38] = cell38;
        cells[39] = cell39;
        cells[40] = cell40;
        cells[41] = cell41;


        for(int i = 0 ; i < 42 ; i++) {
            cellIsFree[i] = true;
        }
    }

    public ImageView getCard(String name) {
        ImageView result = new ImageView();
        result.setPreserveRatio(true);
        InputStream in = CardsControl.class
                .getResourceAsStream("resources/" + name + ".png");
        result.setImage(new Image(in));
        return result;
    }


    public void populateCardMap(){
        cardNameMapping.put("North Africa","AF0");
        cardNameMapping.put("Egypt","AF1");
        cardNameMapping.put("Congo","AF2");
        cardNameMapping.put("East Africa","AF3");
        cardNameMapping.put("South Africa","AF4");
        cardNameMapping.put("Madagascar","AF5");
        cardNameMapping.put("Indonesia","AU0");
        cardNameMapping.put("New Guinea","AU1");
        cardNameMapping.put("Western Australia","AU2");
        cardNameMapping.put("Eastern Australia","AU3");
        cardNameMapping.put("Venezuela","SA0");
        cardNameMapping.put("Peru","SA1");
        cardNameMapping.put("Brazil","SA2");
        cardNameMapping.put("Argentina","SA3");
        cardNameMapping.put("Iceland","EU0");
        cardNameMapping.put("Scandinavia","EU1");
        cardNameMapping.put("Ukraine","EU2");
        cardNameMapping.put("Great Britain","EU3");
        cardNameMapping.put("Northern Europe","EU4");
        cardNameMapping.put("Western Europe","EU5");
        cardNameMapping.put("Southern Europe","EU6");
        cardNameMapping.put("Alaska","NA0");
        cardNameMapping.put("Northwest Territory","NA1");
        cardNameMapping.put("Greenland","NA2");
        cardNameMapping.put("Alberta","NA3");
        cardNameMapping.put("Ontario","NA4");
        cardNameMapping.put("Quebec","NA5");
        cardNameMapping.put("Western United States","NA6");
        cardNameMapping.put("Eastern United States","NA7");
        cardNameMapping.put("Central America","NA8");
        cardNameMapping.put("Ural","AS0");
        cardNameMapping.put("Siberia","AS1");
        cardNameMapping.put("Yakutsk","AS2");
        cardNameMapping.put("Kamchatka","AS3");
        cardNameMapping.put("Irkutsk","AS4");
        cardNameMapping.put("Mongolia","AS5");
        cardNameMapping.put("Japan","AS6");
        cardNameMapping.put("Afghanistan","AS7");
        cardNameMapping.put("China","AS8");
        cardNameMapping.put("Middle East","AS9");
        cardNameMapping.put("India","AS10");
        cardNameMapping.put("Siam","AS11");

        for(String name : NAMES){
            cardImageMapping.put(name, getCard(cardNameMapping.get(name)));
            imageCardMapping.put(cardImageMapping.get(name), name);
            selection.put(name, false);
        }
    }

    EventHandler click = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            ImageView source = (ImageView) mouseEvent.getSource();
            String name = imageCardMapping.get(source);
            if(selection.get(name)){
                selection.remove(name);
                selection.put(name, false);
                source.setOpacity(1);
                selectionCount --;
                selected.remove(name);
            } else{
                if(selectionCount < MAX_SELECT) {
                    selection.remove(name);
                    selection.put(name, true);
                    source.setOpacity(0.5);
                    selectionCount ++;
                    selected.add(name);
                }
            }
        }
    };

    public void showHand(List<Card> hand){
        ListIterator<Card> cardIter = hand.listIterator();

        while(cardIter.hasNext()){
            Card currentCard = cardIter.next();
            addCard(currentCard);
        }
    }

    private int getFirstFreeCell(){
        for(int i=0;i<42;i++){
            if(cellIsFree[i])
                return i;
        }
        return -1;
    }

    private ImageView getCellWithCard(String name){
        for(ImageView cell : cells){
            if(cell.getImage().equals(getCard(cardNameMapping.get(name)).getImage()))
                return cell;
        }
        return null;
    }

    public void addCard(String name){
        int index = getFirstFreeCell();
        ImageView currentCell = cells[index];
        currentCell.setImage(cardImageMapping.get(name).getImage());
        currentCell.addEventFilter(MouseEvent.MOUSE_CLICKED, click);
        cellIsFree[index] = false;
    }

    public void addCard(Card card){
        addCard(card.getName());
    }

    public void removeCard(Card card){
        removeCard(card.getName());
    }

    public void removeCard(String name){
        ImageView currentCell = getCellWithCard(name);
        currentCell.setImage(noCard.getImage());
        currentCell.removeEventFilter(MouseEvent.MOUSE_CLICKED, click);
    }

    public void clearCrads(){
        
    }
}
