package logic;

import java.util.ArrayList;

/**
 * PlayerState --- Stores information about one of the players.
 * This class should be used by the Game to check that players are making valid moves. (this class should not interact with the UI/Network)
 */
public class PlayerState {
    private ArrayList<Card> hand; // The players current hand
    private int uid;

    public PlayerState(int uid){
        this.hand = new ArrayList<Card>();
        this.uid = uid;
    }

    public Integer getUID(){
        return this.uid;
    }

    public ArrayList<Card> getHand(){
        return hand;
    }
}



