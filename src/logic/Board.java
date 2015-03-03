package logic;

import java.io.*;
import java.util.*;

/**
 * Board --- Stores information about the game board.
 */
public class Board {
    private ArrayList<Territory> territories;
    private ArrayList<Continent> continents;
    private Integer wildcards = 0;

    public Board(String filename){
        this.territories = new ArrayList<Territory>();
        this.continents = new ArrayList<Continent>();
        loadBoard(filename);
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

////////
// For Game use
    protected void claimTerritory(int tid, int uid){
        Territory t = territories.get(tid);
        t.setOwner(uid);
    }

    protected void placeArmies(int tid, int numArmies){
        Territory t = territories.get(tid);
        t.addArmies(numArmies);
    }
////////

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
        }
    }

    // This won't work if a new map file doesn't list territories in order.
    private void processContinents(String[] parts){
        Integer newCID = Integer.valueOf(parts[0].replace("\"", ""));
        Continent newContinent = new Continent(newCID);
        String[] newTerritories = parts[1].split(",");
        for(int i = 0; i != newTerritories.length; ++i){
            Integer newTID = Integer.valueOf(newTerritories[i].replace("[", "").replace("]", "").trim());
            Territory newTerritory = new Territory(newTID);
            territories.add(newTerritory);
            newContinent.addTerritory(newTID);
        }
        continents.add(newContinent);
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

    // Extracts the initial deck from the board
    protected Deck getDeck(){
        Deck deck = new Deck();
        int values[] = {1, 5, 10};
        for(int i = 0; i != territories.size(); ++i){
            Territory t = territories.get(i);
            int type = values[t.getCard()];
            Card card = new Card(i, type, t.getName());
            deck.addCard(card);
        }
        for(int i = 0; i != wildcards; ++i){
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
}
