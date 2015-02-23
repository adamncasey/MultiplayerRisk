package logic;

import java.io.*;
import java.util.*;

/**
 * Board --- Stores information about the game board.
 */
public class Board {

    private Map<Integer, Territory> territories = new HashMap<Integer, Territory>();
    private Map<Integer, Continent> continents = new HashMap<Integer, Continent>();
    private Integer wildcards = 0;

    // Don't change territories using this
    public Map<Integer, Territory> getTerritories(){
        return territories;
    }

    // Don't change continents using this
    public Map<Integer, Continent> getContinents(){
        return continents;
    }

    protected Deck getDeck(){
        Deck deck = new Deck();
   
        int values[] = {1, 5, 10};

        Iterator it = territories.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pairs = (Map.Entry)it.next();
            Integer TID = (Integer)pairs.getKey();
            Territory T = (Territory)pairs.getValue();

            int type = values[T.getCard()];

            Card card = new Card(TID, type, T.getName());
            deck.addCard(card);

        }
        for(int i = 0; i != wildcards; ++i){
            Card card = new Card(0, 0, "Wildcard");
            deck.addCard(card);
        }
        return deck;
    }

   /**
    * Take a board file and initiate this object.
    */
    private void loadBoard(String filename){
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
        String name = parts[1].replace(",", "").replace("\"", "");
        T.setName(name);
    }

    private void processCountryCard(String[] parts){
        Integer TID = Integer.valueOf(parts[0].replace("\"", ""));
        Territory T = territories.get(TID);
        Integer card = Integer.valueOf(parts[1].replace(",", ""));
        T.setCard(card);
    }

    private void processWildcards(String[] parts){
        this.wildcards =  Integer.valueOf(parts[1].trim());
    }

    public Board(String filename){
        loadBoard(filename);
    }

    protected int calculatePlayerTerritoryArmies(int uid){
        int territoryCounter = 0;
        for(Territory t : territories.values()){
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
        for(Continent c : continents.values()){
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

    public boolean checkTerritoryOwner(int uid, int TID){
        Territory t = territories.get(TID);
        if(t.getOwner() == uid){
            return true;
        }
        return false;
    }

    public void printBoard(PrintWriter writer){
        for(Territory t : territories.values()){
            writer.format("[%d-%s-%d-%d]", t.getID(), t.getName(), t.getOwner(), t.getArmies());
        }
        writer.format("\n");
        writer.flush();
    }
}
