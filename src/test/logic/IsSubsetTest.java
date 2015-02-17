package test.logic;

import static org.junit.Assert.*;
import org.junit.*;

import java.util.ArrayList;

import logic.*;

public class IsSubsetTest{

    private Card infantry; // an infantry card
    private Card infantry2; // an infantry card with a different id
    private Card infantry3; // an infantry card with another different id
    private Card cavalry; // a cavalry card
    private Card cavalry2;
    private Card cavalry3;
    private Card artillery; // an artillery card
    private Card artillery2;
    private Card artillery3;
    private Card wildcard; // a wildcard

    @Before
    public void setupCards(){
        infantry = new Card(1, 1, "country");
        cavalry = new Card(1, 5, "country");
        artillery = new Card(1, 10, "country");
    }

    // both lists are empty
    @Test
    public void emptyEmpty(){
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        ArrayList<Card> hand = new ArrayList<Card>();
        assertEquals(true, Card.isSubset(toTradeIn, hand));
    }

    // toTradeIn is empty
    @Test
    public void emptyTradeIn(){
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.add(infantry);
        assertEquals(true, Card.isSubset(toTradeIn, hand));
    }

    // hand is empty
    @Test
    public void emptyHand(){
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        toTradeIn.add(infantry);
        ArrayList<Card> hand = new ArrayList<Card>();
        assertEquals(false, Card.isSubset(toTradeIn, hand));
    }

    // both lists contain the same card
    @Test
    public void same(){
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        toTradeIn.add(infantry);
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.add(infantry);
        assertEquals(true, Card.isSubset(toTradeIn, hand));
    }

    // each list has a different card
    @Test
    public void different(){
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        toTradeIn.add(infantry);
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.add(cavalry);
        assertEquals(false, Card.isSubset(toTradeIn, hand));
    }

    // the lists have one same card and one different card
    @Test
    public void almostSame(){
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        toTradeIn.add(infantry);
        toTradeIn.add(cavalry);
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.add(infantry);
        hand.add(artillery);
        assertEquals(false, Card.isSubset(toTradeIn, hand));
    }

    // toTradeIn is bigger than hand
    @Test
    public void biggerToTradeIn(){
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        toTradeIn.add(infantry);
        toTradeIn.add(infantry);
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.add(infantry);
        assertEquals(false, Card.isSubset(toTradeIn, hand));
    }

    // toTradeIn has multiple copies of a card, but hand only has one
    @Test
    public void duplicatesInToTradeIn(){
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        toTradeIn.add(infantry);
        toTradeIn.add(infantry);
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.add(infantry);
        hand.add(cavalry);
        hand.add(artillery);
        assertEquals(false, Card.isSubset(toTradeIn, hand));
    }

    // both lists have duplicates of a card
    @Test
    public void bothDuplicates(){
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        toTradeIn.add(infantry);
        toTradeIn.add(infantry);
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.add(infantry);
        hand.add(infantry);
        hand.add(cavalry);
        assertEquals(true, Card.isSubset(toTradeIn, hand));
    }
}
