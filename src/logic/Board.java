package logic;

import java.io.*;
import java.util.*;

/**
 * Board --- Stores information about the game board.
 */
public class Board {

    private ArrayList<Integer> territories;


    // TODO - way to store links between territories.

   /**
    * Take a board file and initiate this object.
    */
    public void loadBoard(String filename){
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line;
            int control = 0;
            while((line = br.readLine()) != null){
                line = line.trim();
                String[] parts = line.split(":");
                if(parts[0].equals("\"continents\"")){
                    control = 1;
                    continue;
                } else if(parts[0].equals("\"connections\"")){
                    control = 2;
                    continue;
                } else if(parts[0].equals("\"continent_values\"")){
                    control = 3;
                    continue;
                } else if((parts.length == 1) && !(parts[0].charAt(0) == '[')){
                    continue;
                }

                if(control == 1){
                    System.out.println(line);
                } else if(control == 2){
                    System.out.println(line);
                } else if(control == 3){
                    System.out.println(line);
                } 
            }
            br.close();
        } catch (Exception e) {
             // oh well
        }
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


