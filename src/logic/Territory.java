package logic;

import java.util.*;

/**
 * Territory --- Stores information about a territory.
 */
public class Territory {

    private Integer ID;

    // Stores the IDs of the territories linked to this territory
    private ArrayList<Integer> links = new ArrayList<Integer>();

    public Territory(Integer id) {
        ID = id;
    }

    public void addLink(Integer id) {
        links.add(new Integer(id));
    }
}



