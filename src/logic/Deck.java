package logic;

import java.util.ArrayList;
import org.apache.commons.math3.random.MersenneTwister;

/**
 * Deck --- Stores information about the deck of RISK cards.
 */
public class Deck {

    ArrayList<Card> cards = new ArrayList<Card>();

    public Deck(){
    }

    public void addCard(Card card){
        cards.add(card);
    }

    public void shuffle(int seed){
        MersenneTwister twister = new MersenneTwister();
        for(int i = 0; i != 10; ++i){
            System.out.println(twister.nextDouble());
        }
    }

    public void printDeck(){
        System.out.println("Printing current deck:");
        for(int i = 0; i != cards.size(); ++i){
            Card C = cards.get(i);
            System.out.format("    %d - %d - %s\n", C.getID(), C.getType(), C.getName());
        }
    }
}

