package logic.state;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import logic.Card;

/**
 * Board --- Stores information about the game board.
 */
public class Board {
    // Default Board
    private static int[] CONTINENT_SIZES = {9, 4, 7, 6, 12, 4};
    private static int[] CONTINENT_VALUES = {5, 2, 5, 3, 7, 2};
    private static String[] CONTINENT_NAMES = {"North America", "South America", "Europe", "Africa", "Asia", "Australia"};
    private static int[][] TERRITORY_LINKS = {{ 1,  3, 29, -1, -1}, { 2,  3,  4, -1, -1}, { 4,  5, 13, -1, -1}, { 4,  6, -1, -1, -1}, { 5,  6,  7, -1, -1}, { 7, -1, -1, -1, -1}, { 7,  8, -1, -1, -1},
                                              { 8, -1, -1, -1, -1}, { 9, -1, -1, -1, -1}, {10, 11, -1, -1, -1}, {11, 12, -1, -1, -1}, {12, 20, -1, -1, -1}, {-1, -1, -1, -1, -1}, {14, 16, -1, -1, -1},
                                              {15, 16, 17, -1, -1}, {17, 19, 26, 33, 35}, {14, 17, 18, -1, -1}, {18, 19, -1, -1, -1}, {19, 20, -1, -1, -1}, {20, 21, 35, -1, -1}, {21, 22, 23, -1, -1},
                                              {23, 35, -1, -1, -1}, {23, 24, -1, -1, -1}, {24, 25, 35, -1, -1}, {25, -1, -1, -1, -1}, {-1, -1, -1, -1, -1}, {27, 33, 34, -1, -1}, {28, 30, 34, -1, -1},
                                              {29, 30, -1, -1, -1}, {30, 31, 32, -1, -1}, {31, -1, -1, -1, -1}, {32, 34, -1, -1, -1}, {-1, -1, -1, -1, -1}, {34, 35, 36, -1, -1}, {36, 37, -1, -1, -1},
                                              {36, -1, -1, -1, -1}, {37, -1, -1, -1, -1}, {38, -1, -1, -1, -1}, {39, 40, -1, -1, -1}, {40, 41, -1, -1, -1}, {41, -1, -1, -1, -1}, {-1, -1, -1, -1, -1}};
    private static String[] TERRITORY_NAMES = {"Alaska", "Northwest Territory", "Greenland", "Alberta", "Ontario", "Quebec",
                                               "Western United States", "Eastern United States", "Central America", "Venezuela",
                                               "Peru", "Brazil", "Argentina", "Iceland", "Scandinavia", "Ukraine", "Great Britain",
                                               "Northern Europe", "Western Europe", "Southern Europe", "North Africa", "Egypt",
                                               "Congo", "East Africa", "South Africa", "Madagascar", "Ural", "Siberia", "Yakutsk",
                                               "Kamchatka", "Irkutsk", "Mongolia", "Japan", "Afghanistan", "China", "Middle East",
                                               "India", "Siam", "Indonesia", "New Guinea", "Western Australia", "Eastern Australia"};
    private static int[] TERRITORY_CARDS = {10, 5, 5, 10, 10, 10, 10, 10, 1, 5, 5, 1, 1, 5, 1, 1, 1, 5, 1, 1, 1, 5, 10, 1, 10, 5, 1,
                                            1, 10, 10, 10, 5, 5, 1, 10, 10, 5, 5, 1, 5, 10, 5};
    private static int NUM_WILDCARDS = 2;

    private List<Territory> territories;
    private List<Continent> continents;

    public Board(){
        loadDefaultBoard();
    }

    protected Board(boolean testing, int[] owners, int[] armies){
        if(testing){
            loadTestBoard();
        }else{
            loadDefaultBoard();
        }
        fillTestBoard(owners, armies);
    }

//// Use these methods

    public int getNumTerritories(){
        return territories.size();
    }

    public int getOwner(int territory){
        return territories.get(territory).getOwner();
    }

    public int getArmies(int territory){
        return territories.get(territory).getArmies();
    }

    public String getName(int territory){
        return territories.get(territory).getName();
    }

    public List<Integer> getLinks(int territory){
        return territories.get(territory).getLinks();
    }

    // Prints a representation of the board to writer, or returns the representation as a string (if writer is null)
    public String printBoard(PrintWriter writer){
        String message = "";
        for(Territory t : territories){
            if(t.getOwner() == -1){
                message += String.format("[%d-%s-Free-%d]", t.getID(), t.getName(), t.getArmies());
            }else{
                message += String.format("[%d-%s-%d-%d]", t.getID(), t.getName(), t.getOwner(), t.getArmies());
            }
        }
        message += "\n";
        if(writer != null){
            writer.print(message);
            writer.flush();
            return "";
        }else{
            return message;
        }
    }

////

    protected void claimTerritory(int tid, int uid){
        Territory t = territories.get(tid);
        t.setOwner(uid);
    }

    protected void placeArmies(int tid, int numArmies){
        Territory t = territories.get(tid);
        t.addArmies(numArmies);
    }

    protected Deck getDeck(){
        Deck deck = new Deck();
        for(int i = 0; i != territories.size(); ++i){
            Card card = new Card(i, TERRITORY_CARDS[i], getName(i));
            deck.addCard(card);
        }
        for(int i = 0; i != NUM_WILDCARDS; ++i){
            Card card = new Card(0, 0, "Wildcard");
            deck.addCard(card);
        }
        return deck;
    }

    protected int calculatePlayerTerritoryArmies(int uid){
        int territoryCounter = 0;
        for(Territory t : territories){
            if(t.getOwner() == uid){
                territoryCounter++;
            }
        }
        int armies = territoryCounter/3;
        if(armies < 3){
            return 3;
        }
        return armies;
    }

    protected int calculatePlayerContinentArmies(int uid){
        int armies = 0;
        for(Continent c : continents){
            boolean owned = true;
            for(Integer TID : c.getTerritories()){
                Territory t = territories.get(TID);
                if(t.getOwner() != uid){
                    owned = false;
                }
            }
            if(owned){
                armies += c.getValue();
            }
        }
        return armies;
    }

    private void loadDefaultBoard(){
        loadBoard(CONTINENT_SIZES, CONTINENT_VALUES, CONTINENT_NAMES, TERRITORY_LINKS, 5, TERRITORY_NAMES, TERRITORY_CARDS, NUM_WILDCARDS);
    }

    private void loadTestBoard(){
        int[] continentSizes = {4, 4, 4};
        int[] continentValues = {3, 4, 5};
        String[] continentNames = {"CA", "CB", "CC"};
        int[][] territoryLinks = {{ 1,  2, -1}, { 2,  3,  5}, { 3,  9, -1}, { 7, 11, -1},
                                  { 5,  6, -1}, { 6,  7, -1}, { 7, 10, -1}, {11, -1, -1},
                                  { 9, 10, -1}, {10, 11, -1}, {11, -1, -1}, {-1, -1, -1}};
        String[] territoryNames = {"T0", "T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11"};
        int[] territoryCards = {10, 5, 5, 1, 10, 5, 5, 1, 10, 5, 5, 1};
        int numWildcards = 1;
        loadBoard(continentSizes, continentValues, continentNames, territoryLinks, 3, territoryNames, territoryCards, numWildcards);
    }

    private void loadBoard(int[] continentSizes, int[] continentValues, String[] continentNames, int[][] territoryLinks, int maxLinks, String[] territoryNames, int[] territoryCards, int numWildcards){
        this.territories = new ArrayList<Territory>();
        this.continents = new ArrayList<Continent>();
        int territoryCounter = 0;
        for(int i = 0; i != continentSizes.length; ++i){
            Continent newContinent = new Continent(i);
            newContinent.setValue(continentValues[i]);
            newContinent.setName(continentNames[i]);
            for(int j = 0; j != continentSizes[i]; ++j){
                int newTID = territoryCounter++;
                newContinent.addTerritory(newTID);
                Territory newTerritory = new Territory(newTID);
                newTerritory.setName(territoryNames[newTID]);
                territories.add(newTerritory);
            }
            continents.add(newContinent);
        }
        for(int i = 0; i != territoryLinks.length; ++i){
            Territory t1 = territories.get(i);
            for(int k = 0; k != maxLinks; ++k){
                int link = territoryLinks[i][k];
                if(link != -1){
                    t1.addLink(link);
                    Territory t2 = territories.get(link);
                    t2.addLink(i);
                }
            }
        }
    }

    private void fillTestBoard(int[] owners, int[] armies){
        for(int i = 0; i != owners.length; ++i){
            claimTerritory(i, owners[i]);
            placeArmies(i, armies[i]);
        } 
    }
}
