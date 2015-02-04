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
                } else if(parts[0].equals("\"continent_names\"")) {
                    control = 4;
                    continue;
                } else if(parts[0].equals("\"country_names\"")) {
                    control = 5;
                    continue;
                } else if(parts[0].equals("\"country_card\"")) {
                    control = 6;
                    continue;
                } else if(parts[0].equals("\"wildcards\"")) {
                    processWildcards(parts);
                    continue;
                } else if(parts.length == 1){
                    control = 0; // end current block 
                    continue;
                }

                if(control == 1) {
                    processContinents(parts); 
                } else if(control == 2){
                    processConnections(parts); 
                } else if(control == 3){
                    processContinentValues(parts); 
                } else if(control == 4){
                    processContinentNames(parts); 
                } else if(control == 5){
                    processCountryNames(parts); 
                } else if(control == 6){
                    processCountryCard(parts); 
                }
            }
            br.close();
        } catch (Exception e) {
             // oh well
        }
    }

    private void processContinents(String[] parts){
        Integer newCID = Integer.valueOf(parts[0].replace("\"", ""));
        Continent newContinent = new Continent(newCID);
        String[] newTerritories = parts[1].split(",");
        for(int i = 0; i != newTerritories.length; ++i){
            Integer newTID = Integer.valueOf(newTerritories[i].replace("[", "").replace("]", "").trim());
            Territory newTerritory = new Territory(newTID);
            territories.put(newTID, newTerritory);
            newContinent.addTerritory(newTID);
        }
        continents.put(newCID, newContinent);
    }

    private void processConnections(String[] parts){
        Integer TID1 = Integer.valueOf(parts[0].replace("\"", ""));
        Territory T1 = territories.get(TID1);
        String[] newLinks = parts[1].split(",");
        for(int i = 0; i != newLinks.length; ++i){
            if(newLinks[i].equals("[]")){
                continue;
            }
            Integer TID2 = Integer.valueOf(newLinks[i].replace("[", "").replace("]", "").trim());
            Territory T2 = territories.get(TID2);
            T1.addLink(TID2);
            T2.addLink(TID1);
        }
    }

    private void processContinentValues(String[] parts){
        Integer CID = Integer.valueOf(parts[0].replace("\"", ""));
        Continent C = continents.get(CID);
        Integer value = Integer.valueOf(parts[1].replace(",", ""));
        C.setValue(value);
    }

    private void processContinentNames(String[] parts){
        Integer CID = Integer.valueOf(parts[0].replace("\"", ""));
        Continent C = continents.get(CID);
        String name = parts[1].replace(",", "");
        C.setName(name);
    }

    private void processCountryNames(String[] parts){
        Integer TID = Integer.valueOf(parts[0].replace("\"", ""));
        Territory T = territories.get(TID);
        String name = parts[1].replace(",", "");
        T.setName(name);
    }

    private void processCountryCard(String[] parts){
        Integer TID = Integer.valueOf(parts[0].replace("\"", ""));
        Territory T = territories.get(TID);
        Integer card = Integer.valueOf(parts[1].replace(",", ""));
        T.setCard(card);
    }

    private void processWildcards(String[] parts){
        Integer wildcards = Integer.valueOf(parts[1].trim());
        // Do something with wildcards
    }

    public void printBoard() {
    }

    public Board(String filename){
        loadBoard(filename);
    } 
}


