package logic.state;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import logic.Card;

/**
 * Player --- Stores information about a player.
 */
public class Player {
    private int uid;
    private List<Card> hand;
    private boolean eliminated;
    private boolean disconnected;

    public Player(int uid){
        this.uid = uid;
        this.hand = new ArrayList<Card>();
        this.eliminated = false;
        this.disconnected = false;
    }

    public int getUID(){
        return this.uid;
    }

    public List<Card> getHand(){
        return Collections.unmodifiableList(this.hand);
    }

    protected List<Card> modifyHand(){
        return this.hand;
    }

    protected void addCard(Card card){
        this.hand.add(card);
    }

    public boolean isEliminated(){
        return this.eliminated;
    }

    protected void eliminate(){
        this.hand.clear();
        this.eliminated = true;
    }

    public boolean isDisconnected(){
        return this.disconnected;
    }

    public void disconnect(){
        this.disconnected = true;
    }
}
