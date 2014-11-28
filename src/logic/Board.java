package logic;

import java.io.*;
import java.util.*;

/**
 * Board --- Stores information about the game board.
 */
public class Board {

    private Map<Integer, Territory> territories = new HashMap<Integer, Territory>();
    private Map<Integer, Continent> continents = new HashMap<Integer, Continent>();

    // continents 1 - 123456
    // connections territory - territory
    // continent_values - values

   /**
    * Take a board file and initiate this object.
    */
    public void loadBoard(String filename) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line;
            int control = 0;
            while((line = br.readLine()) != null) {
                line = line.trim();
                String[] parts = line.split(":");
                if(parts[0].equals("\"continents\"")) {
                    control = 1;
                    continue;
                } else if(parts[0].equals("\"connections\"")) {
                    control = 2;
                    continue;
                } else if(parts[0].equals("\"continent_values\"")) {
                    control = 3;
                    continue;
                } else if((parts.length == 1) && !(parts[0].charAt(0) == '[')) {
                    continue;
                }

                if(control == 1) {
                    Integer newCID = Integer.valueOf(parts[0].replace("\"", ""));
                    Continent newContinent = new Continent(newCID);
                    String[] newTerritories = parts[1].split(",");
                    for(int i = 0; i != newTerritories.length; ++i){
                        Integer newTID = Integer.valueOf(newTerritories[i].replace("[", "").replace("]", ""));
                        Territory newTerritory = new Territory(newTID);
                        territories.put(newTID, newTerritory);
                        newContinent.addTerritory(newTID);
                    }
                    continents.put(newCID, newContinent);
                } else if(control == 2) {
                    line = line.replace("[","").replace("]","");
                    String[] link = line.split(",");
                    Integer TID1 = Integer.valueOf(link[0]);
                    Integer TID2 = Integer.valueOf(link[1]);
                    Territory T1 = territories.get(TID1);
                    T1.addLink(TID2);
                    Territory T2 = territories.get(TID2);
                    T2.addLink(TID1);
                } else if(control == 3) {
                    Integer CID = Integer.valueOf(parts[0].replace("\"", ""));
                    Continent C = continents.get(CID);
                    Integer value = Integer.valueOf(parts[1].replace(",", ""));
                    C.setValue(value);
                } 
            }
            br.close();
        } catch (Exception e) {
             // oh well
        }
    }

    public void printBoard() {

    }

    // add one Infantry to any unclaimed territory on the board
    public void setup2ClaimTerritory(GameMove move) throws InvalidTerritoryException {
    }

    // add one Army to any unoccupied territory
    public void setup3OccupyTerritory(GameMove move) throws InvalidTerritoryException {
    }

    // place one additional army onto any territory claimed by that player)
    public void setup4AddOneArmy(GameMove move) throws InvalidTerritoryException {
    }

    // add armies to the board as per rules
    public void game1AddArmies(GameMove move) throws InvalidTerritoryException {
    }

    // attack
    public void game2Attack(GameMove move) throws InvalidTerritoryException {
    }

    // fortify
    public void game3Fortify(GameMove move) throws InvalidTerritoryException {
    }

    public Board(String filename){
        loadBoard(filename);
    } 
}


