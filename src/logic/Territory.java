package logic;

import java.util.List;
import java.util.ArrayList;

/**
 * Territory --- Stores information about a territory.
 */
public class Territory {

    private Integer ID;

    private String name;
    private int owner;
    private int armies;

    // Stores the IDs of the territories linked to this territory
    private List<Integer> links;

    public Territory(Integer id){
        this.ID = id;
        this.name = "";
        this.owner = -1;
        this.armies = 0;
        this.links = new ArrayList<Integer>();
    }

    public int getID(){
        return this.ID;
    }

    protected void setOwner(int uid){
        this.owner = uid;
    }

    protected int getOwner(){
        return this.owner;
    }

    protected void addArmies(int armies){
        this.armies += armies;
    }

    protected int getArmies(){
        return this.armies;
    }

    protected void setName(String name){
        this.name = name;
    }

    protected String getName(){
        return this.name;
    }

    protected void addLink(Integer id){
        links.add(new Integer(id));
    }

    protected List<Integer> getLinks(){
        return links;
    }
}
