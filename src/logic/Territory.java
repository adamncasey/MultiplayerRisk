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

    public Territory(Integer id){
        ID = id;
    }

    public void addLink(Integer id){
        links.add(new Integer(id));
    }

    public void setName(String name){
        this.name = name;
    }

    public void setCard(Integer card){
        this.card = card;
    }
}



