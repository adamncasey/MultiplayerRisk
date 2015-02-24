package logic;

import java.util.*;

/**
 * Territory --- Stores information about a territory.
 */
public class Territory {

    private Integer ID;

    private String name = "";
    private int owner = -1;
    private int armies = 0;

    // Stores the IDs of the territories linked to this territory
    private ArrayList<Integer> links = new ArrayList<Integer>();

    private Integer card;

    public Territory(Integer id){
        ID = id;
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

    protected ArrayList<Integer> getLinks(){
        return links;
    }

    protected void setCard(Integer card){
        this.card = card;
    }

    protected Integer getCard(){
        return this.card;
    }
}



