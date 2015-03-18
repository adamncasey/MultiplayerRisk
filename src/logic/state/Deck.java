package logic.state;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.math3.random.MersenneTwister;

import logic.Card;

/**
 * Deck --- Stores information about the deck of RISK cards.
 */
public class Deck {

    List<Card> cards = new ArrayList<Card>();

    public Deck(){
    }

    public void addCard(Card card){
        cards.add(card);
    }

    public Card drawCard(){
        if(cards.size() == 0){
            return null;
        }
        Card c = cards.get(0);
        cards.remove(0);
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
}

