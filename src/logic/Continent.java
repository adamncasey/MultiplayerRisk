package logic;

import java.util.*;

/**
 * Continent --- Stores information about a continent.
 */
public class Continent {

    private Integer ID;

    // Stores the IDs of the territories in the continent
    private List<Integer> territories = new ArrayList<Integer>();

    private int value = 0;
    private String name = "";

    public Continent(Integer id){
        ID = id;
    } 

    protected void addTerritory(Integer id){
        territories.add(id);
    }

    protected List<Integer> getTerritories(){
        return territories;
    }

    protected void setValue(int value){
        this.value = value;
    }

    protected int getValue(){
        return this.value;
    }

    protected void setName(String name){
        this.name = name;
    }

    protected String getName(){
        return this.name;
    }
}
