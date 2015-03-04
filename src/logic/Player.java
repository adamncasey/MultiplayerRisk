package logic;

import java.io.*;
import java.util.*;

/**
 * Player --- Stores information about a player.
 */
public class Player {
    private boolean eliminated;
    private List<Card> hand;

    public Player(){
        this.eliminated = false;
        this.hand = new ArrayList<Card>();
    }

    public boolean isEliminated(){
        return this.eliminated;
    }

    public void eliminate(){
        this.eliminated = true;
    }

    public List<Card> getHand(){
        return this.hand;
    }
}



