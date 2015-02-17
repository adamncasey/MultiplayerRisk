package player;

import java.util.*;
import logic.*;
import java.util.Random;

/**
 * RandomPlayer --- Randomly makes moves until one is valid.
 */
public class RandomPlayer implements IPlayer {
    private static Random random = new Random();
    private int uid = 0; // Set and used by Game

    public RandomPlayer(){
    }

    public int getUID(){
        return this.uid;
    }

    public void setUID(int uid){
        this.uid = uid;
    }

    public ArrayList<Card> tradeInCards(ArrayList<Card> hand, String requestMessage){ 
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        int handSize = hand.size();
        if(handSize >= 5){
            for(int i = 0; i != 3; ++i){
                int randomCard = random.nextInt(hand.size());
                Card c = hand.get(randomCard);
                hand.remove(randomCard);
                toTradeIn.add(c);
            } 
        }
        return toTradeIn;
    }

}

