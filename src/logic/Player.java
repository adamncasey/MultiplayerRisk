package logic;

import java.util.List;
import java.util.ArrayList;

/**
 * Player --- Stores information about a player.
 */
public class Player {
    private int uid;
    private List<Card> hand;
    private boolean eliminated;

    public Player(int uid){
        this.uid = uid;
        this.hand = new ArrayList<Card>();
        this.eliminated = false;
    }

    public int getUID(){
        return this.uid;
    }

    public List<Card> getHand(){
        return this.hand;
    }
    public boolean isEliminated(){
        return this.eliminated;
    }

    public void eliminate(){
        this.eliminated = true;
    }
}



