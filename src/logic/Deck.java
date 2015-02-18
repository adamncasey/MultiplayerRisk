package logic;

import java.util.Collections;
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

    public Card drawCard(){
        Card c = cards.get(cards.size()-1);
        cards.remove(cards.size()-1);
        return c;
    }

    public void shuffle(int seed){
        MersenneTwister twister = new MersenneTwister();
        int size = cards.size();
        for(int i = 0; i != size; ++i){
            int j = twister.nextInt(size);
            Collections.swap(cards, i, j);
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

