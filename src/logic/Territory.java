package logic;

import java.util.*;

/**
 * Territory --- Stores information about a territory.
 */
public class Territory {

    private Integer ID;

    // Stores the IDs of the territories linked to this territory
    private ArrayList<Integer> links = new ArrayList<Integer>();

    private String name = "";
    private Integer card;

    private int owner = 0;
    private int armies = 0;

    public Territory(Integer id){
        ID = id;
    }

    public int getID(){
        return this.ID;
    }

    public void addLink(Integer id){
        links.add(new Integer(id));
    }

    public ArrayList<Integer> getLinks(){
        return links;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setCard(Integer card){
        this.card = card;
    }

    public Integer getCard(){
        return this.card;
    }

    public void setOwner(int uid){
        this.owner = uid;
    }

    public int getOwner(){
        return this.owner;
    }

    public void addArmies(int armies){
        this.armies += armies;
    }

    public int getArmies(){
        return this.armies;
    }
}



