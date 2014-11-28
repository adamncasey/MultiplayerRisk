package logic;

import java.util.*;

/**
 * Continent --- Stores information about a continent.
 */
public class Continent {

    private Integer ID;

    // Stores the IDs of the territories in the continent
    private ArrayList<Integer> territories = new ArrayList<Integer>();

    private int continentValue = 0;

    public Continent(Integer id){
        ID = id;
    } 

    public void addTerritory(Integer id) {
        territories.add(id);
    }

    public void setValue(int value) {
        continentValue = value;
    }
}




