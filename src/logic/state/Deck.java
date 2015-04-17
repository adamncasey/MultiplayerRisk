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

    List<Card> cards = new ArrayList<>();

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

    public int getNumCards() {

        return cards.size();
    }

    public void shuffleSwap(int index1, int index2) {
        Collections.swap(cards, index1, index2);
    }
}

