package test.logic.state.card;

import static org.junit.Assert.*;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import logic.Card;

public class ContainsSetTest{

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
        infantry2 = new Card(2, 1, "country2");
        infantry3 = new Card(3, 1, "country3");
        cavalry = new Card(1, 5, "country");
        cavalry2 = new Card(2, 5, "country");
        cavalry3 = new Card(3, 5, "country");
        artillery = new Card(1, 10, "country");
        artillery2 = new Card(2, 10, "country");
        artillery3 = new Card(3, 10, "country");
        wildcard = new Card(0, 0, "country");
    }

    // hand is empty
    @Test
    public void empty(){
        List<Card> hand = new ArrayList<Card>();
        assertEquals(false, Card.containsSet(hand));
    }

    // hand has one card
    @Test
    public void oneCard(){
        List<Card> hand = new ArrayList<Card>();
        assertEquals(false, Card.containsSet(hand));
    }

    // hand has a set of 3 different cards
    @Test
    public void different(){
        List<Card> hand = new ArrayList<Card>();
        hand.add(infantry); hand.add(cavalry); hand.add(artillery);
        assertEquals(true, Card.containsSet(hand));
    }

    // hand has a set of 3 infantry
    @Test
    public void same1(){
        List<Card> hand = new ArrayList<Card>();
        hand.add(infantry); hand.add(infantry2); hand.add(infantry3);
        assertEquals(true, Card.containsSet(hand));
    }

    // hand has a set of 3 cavalry
    @Test
    public void same2(){
        List<Card> hand = new ArrayList<Card>();
        hand.add(cavalry); hand.add(cavalry2); hand.add(cavalry3);
        assertEquals(true, Card.containsSet(hand));
    }

    // hand has a set of 3 artillery
    @Test
    public void same3(){
        List<Card> hand = new ArrayList<Card>();
        hand.add(artillery); hand.add(artillery2); hand.add(artillery3);
        assertEquals(true, Card.containsSet(hand));
    }

    // hand has 3 cards, but they do not make a set
    @Test
    public void no(){
        List<Card> hand = new ArrayList<Card>();
        hand.add(infantry); hand.add(infantry2); hand.add(cavalry);
        assertEquals(false, Card.containsSet(hand));
    }

    // hand has 4 cards, including a set of 3 different cards
    @Test
    public void extraDifferent(){
        List<Card> hand = new ArrayList<Card>();
        hand.add(infantry); hand.add(cavalry); hand.add(artillery);
        hand.add(infantry2);
        assertEquals(true, Card.containsSet(hand));
    }

    // hand has 4 cards, including a set of 3 of the same card
    @Test
    public void extraSame(){
        List<Card> hand = new ArrayList<Card>();
        hand.add(infantry); hand.add(infantry2); hand.add(infantry3);
        hand.add(cavalry);
        assertEquals(true, Card.containsSet(hand));
    }

    // hand has 4 cards, but doesn't contain a set
    @Test
    public void extraNo(){
        List<Card> hand = new ArrayList<Card>();
        hand.add(infantry); hand.add(infantry2); hand.add(cavalry);
        hand.add(cavalry2);
        assertEquals(false, Card.containsSet(hand));
    }

    // hand has a set containing a wildcard and 2 different cards
    @Test
    public void wildcardDifferent(){
        List<Card> hand = new ArrayList<Card>();
        hand.add(infantry); hand.add(cavalry); hand.add(wildcard);
        assertEquals(true, Card.containsSet(hand));
    }

    // hand has a set containing a wildcard and 2 infantry
    @Test
    public void wildcardSame(){
        List<Card> hand = new ArrayList<Card>();
        hand.add(infantry); hand.add(infantry2); hand.add(wildcard);
        assertEquals(true, Card.containsSet(hand));
    }

    // hand has a wildcard but still doesn't make a set
    @Test
    public void wildcardNo(){
        List<Card> hand = new ArrayList<Card>();
        hand.add(infantry); hand.add(wildcard);
        assertEquals(false, Card.containsSet(hand)); 
    }

    // hand has a wildcard that could be a part of 2 different sets
    @Test
    public void wildcardExtra(){
        List<Card> hand = new ArrayList<Card>();
        hand.add(infantry); hand.add(cavalry); hand.add(wildcard);
        hand.add(infantry2);
        assertEquals(true, Card.containsSet(hand));
    }

    // hand has two wildcards but nothing else
    @Test
    public void wildcardTwoNo(){
        List<Card> hand = new ArrayList<Card>();
        hand.add(wildcard); hand.add(wildcard);
        assertEquals(false, Card.containsSet(hand));
    }

    // hand makes a set containing 2 wildcards
    @Test
    public void wildcardTwo(){
        List<Card> hand = new ArrayList<Card>();
        hand.add(infantry); hand.add(wildcard); hand.add(wildcard);
        assertEquals(true, Card.containsSet(hand));
    }

}
