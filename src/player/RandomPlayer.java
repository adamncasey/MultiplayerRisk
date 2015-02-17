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

    // Don't change board or hand
    private Board board;
    private ArrayList<Card> hand;

    public RandomPlayer(){
    }

    public int getUID(){
        return this.uid;
    }

    public void setUID(int uid){
        this.uid = uid;
    }

    public void updatePlayer(Board board, ArrayList<Card> hand){
        this.board = board;
        this.hand = hand;
    }

    public ArrayList<Card> tradeInCards(String requestMessage){ 
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

    public ArrayList<Integer> placeArmies(String requestMessage, int armiesToPlace){
        ArrayList<Integer> move = new ArrayList<Integer>();
        return move;
    }

}

