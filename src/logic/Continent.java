package logic;

import java.util.*;

/**
 * Continent --- Stores information about a continent.
 */
public class Continent {

    private Integer ID;

    // Stores the IDs of the territories in the continent
    private ArrayList<Integer> territories = new ArrayList<Integer>();

    private int value = 0;
    private String name = "";

    public Continent(Integer id){
        ID = id;
    } 

    public void addTerritory(Integer id){
        territories.add(id);
    }

    public ArrayList<Integer> getTerritories(){
        return territories;
    }

    public void setValue(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }

    public void setName(String name){
        this.name = name;
    }
}
