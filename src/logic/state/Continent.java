package logic.state;

import java.util.ArrayList;
import java.util.List;

/**
 * Continent --- Stores information about a continent.
 */
public class Continent {

    // Stores the IDs of the territories in the continent
    private List<Integer> territories;

    private int value;
    private String name;

    public Continent(Integer id){
        this.territories = new ArrayList<Integer>();
        this.value = 0;
        this.name = "";
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
