package test.logic;

import static org.junit.Assert.*;
import org.junit.*;

import java.util.ArrayList;

import logic.*;
import player.*;

public class TradeInCardsTest{

    private static Game game;
    private Card infantry; // an infantry card
    private Card infantry2; // an infantry card with a different id
    private Card infantry3; // an infantry card with another different id
    private Card cavalry; // a cavalry card
    private Card cavalry2;
    private Card artillery; // an artillery card
    private Card wildcard; // a wildcard

    @BeforeClass
    public static void setupGame(){
        ArrayList<IPlayer> players = new ArrayList<IPlayer>();
        game = new Game(players, 0, 0, "resources/risk_map.json");
    }

    @Before
    public void setupCards(){
        infantry = new Card(1, 1, "country");
        infantry2 = new Card(2, 1, "country2");
        infantry3 = new Card(3, 1, "country3");
        cavalry = new Card(1, 5, "country");
        cavalry2 = new Card(2, 5, "country");
        artillery = new Card(1, 10, "country");
        wildcard = new Card(0, 0, "country");
    }

    // hand is empty, toTradeIn is empty
    @Test
    public void emptyHandGood(){
        ArrayList<Card> hand = new ArrayList<Card>();
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        assertEquals(true, game.checkTradeInCards(hand, toTradeIn));
    }

    // hand is empty, toTradeIn isn't empty
    @Test
    public void emptyHandBad(){
        ArrayList<Card> hand = new ArrayList<Card>();
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        toTradeIn.add(infantry);
        assertEquals(false, game.checkTradeInCards(hand, toTradeIn));
    }

    // hand has one card, toTradeIn is empty
    @Test
    public void oneCardGood(){
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.add(infantry);
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        assertEquals(true, game.checkTradeInCards(hand, toTradeIn));
    }

    // hand has one card, toTradeIn isn't empty
    @Test
    public void oneCardBad(){
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.add(infantry);
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        toTradeIn.add(infantry);
        assertEquals(false, game.checkTradeInCards(hand, toTradeIn));
    }

    // hand has 3 different cards making a set, toTradeIn is empty
    @Test
    public void differentSetGood1(){
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.add(infantry); hand.add(cavalry); hand.add(artillery);
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        assertEquals(true, game.checkTradeInCards(hand, toTradeIn));
    }
    
    // hand has 3 different cards making a set, toTradeIn contains that set
    @Test
    public void differentSetGood2(){
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.add(infantry); hand.add(cavalry); hand.add(artillery);
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        toTradeIn.add(infantry); toTradeIn.add(cavalry); toTradeIn.add(artillery);
        assertEquals(true, game.checkTradeInCards(hand, toTradeIn));
    }

    // hand has 3 different cards making a set, toTradeIn contains that set in a different order
    @Test
    public void differentSetGood3(){
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.add(infantry); hand.add(cavalry); hand.add(artillery);
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        toTradeIn.add(artillery); toTradeIn.add(cavalry); toTradeIn.add(infantry);
        assertEquals(true, game.checkTradeInCards(hand, toTradeIn));
    }

    // hand has 3 different cards making a set, toTradeIn contains a different 3 cards
    @Test
    public void differentSetBad1(){
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.add(infantry); hand.add(cavalry); hand.add(artillery);
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        toTradeIn.add(infantry); toTradeIn.add(infantry); toTradeIn.add(cavalry);
        assertEquals(false, game.checkTradeInCards(hand, toTradeIn));
    }

    // hand has 3 different cards making a set, toTradeIn contains a set with a card not in hand
    @Test
    public void differentSetBad2(){
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.add(infantry); hand.add(cavalry); hand.add(artillery);
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        toTradeIn.add(infantry2); toTradeIn.add(cavalry); toTradeIn.add(artillery);
        assertEquals(false, game.checkTradeInCards(hand, toTradeIn));
    }

    // hand has 3 same cards making a set, toTradeIn is empty
    @Test
    public void sameSetGood1(){
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.add(infantry); hand.add(infantry2); hand.add(infantry3);
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        assertEquals(true, game.checkTradeInCards(hand, toTradeIn));
    }

    // hand has 3 same cards making a set, toTradeIn contains that set
    @Test
    public void sameSetGood2(){
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.add(infantry); hand.add(infantry2); hand.add(infantry3);
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        toTradeIn.add(infantry); toTradeIn.add(infantry2); toTradeIn.add(infantry3);
        assertEquals(true, game.checkTradeInCards(hand, toTradeIn));
    }

    // hand has 3 same cards making a set, toTradeIn contains that set in a different order
    @Test
    public void sameSetGood3(){
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.add(infantry); hand.add(infantry2); hand.add(infantry3);
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        toTradeIn.add(infantry2); toTradeIn.add(infantry); toTradeIn.add(infantry3);
        assertEquals(true, game.checkTradeInCards(hand, toTradeIn));
    }

    // hand has 3 same cards making a set, toTradeIn contains a different 3 cards
    @Test
    public void sameSetBad1(){
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.add(infantry); hand.add(infantry2); hand.add(infantry3);
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        toTradeIn.add(infantry); toTradeIn.add(infantry); toTradeIn.add(infantry2);
        assertEquals(false, game.checkTradeInCards(hand, toTradeIn));
    }

    // hand has 3 same cards making a set, toTradeIn contains a set with a card not in hand
    @Test
    public void sameSetBad2(){
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.add(infantry); hand.add(infantry2); hand.add(infantry3);
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        toTradeIn.add(infantry); toTradeIn.add(infantry2); toTradeIn.add(cavalry);
        assertEquals(false, game.checkTradeInCards(hand, toTradeIn));
    }

    // hand has 3 cards not making a set, toTradeIn is empty
    @Test
    public void notSetGood(){
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.add(infantry); hand.add(infantry2); hand.add(cavalry);
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        assertEquals(true, game.checkTradeInCards(hand, toTradeIn));
    }

    // hand has 3 cards not making a set, toTradeIn contains those cards
    @Test
    public void notSetBad1(){
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.add(infantry); hand.add(infantry2); hand.add(cavalry);
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        toTradeIn.add(infantry); hand.add(infantry2); hand.add(cavalry);
        assertEquals(false, game.checkTradeInCards(hand, toTradeIn));
    }

    // hand has 3 cards not making a set, toTradeIn contains a set (with cards not in hand)
    @Test
    public void notSetBad2(){
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.add(infantry); hand.add(infantry2); hand.add(cavalry);
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        toTradeIn.add(infantry); hand.add(cavalry); hand.add(artillery);
        assertEquals(false, game.checkTradeInCards(hand, toTradeIn));
    }

    // hand has 5 cards, including a set of 3 different cards, toTradeIn is empty
    @Test
    public void fiveWithDifferentBad(){
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.add(infantry); hand.add(infantry2); hand.add(cavalry); hand.add(cavalry2); hand.add(artillery);
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        assertEquals(false, game.checkTradeInCards(hand, toTradeIn));
    }

    // hand has 5 cards, including a set of 3 different cards, toTradeIn contains the set of 3 different cards
    @Test
    public void fiveWithDifferentGood(){
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.add(infantry); hand.add(infantry2); hand.add(cavalry); hand.add(cavalry2); hand.add(artillery);
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        toTradeIn.add(infantry); toTradeIn.add(cavalry); toTradeIn.add(artillery);
        assertEquals(true, game.checkTradeInCards(hand, toTradeIn));
    }

    // hand has 5 cards, including a set of 3 same cards, toTradeIn is empty
    @Test
    public void fiveWithSameBad(){
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.add(infantry); hand.add(infantry2); hand.add(infantry3); hand.add(cavalry); hand.add(cavalry2);
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        assertEquals(false, game.checkTradeInCards(hand, toTradeIn));
    }

    // hand has 5 cards, including a set of 3 same cards, toTradeIn contains the set of 3 same cards
    @Test
    public void fiveWithSameGood(){
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.add(infantry); hand.add(infantry2); hand.add(infantry3); hand.add(cavalry); hand.add(cavalry2);
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        toTradeIn.add(infantry); toTradeIn.add(infantry2); toTradeIn.add(infantry3);
        assertEquals(true, game.checkTradeInCards(hand, toTradeIn));
    }


    // hand has 5 cards, including potentially 2 different sets, toTradeIn is empty
    @Test
    public void fiveWithMultipleBad(){
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.add(infantry); hand.add(infantry2); hand.add(infantry3); hand.add(cavalry); hand.add(artillery);
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        assertEquals(false, game.checkTradeInCards(hand, toTradeIn));
    }

    // hand has 5 cards, including potentially 2 different sets, toTradeIn contains the set of 3 different cards
    @Test
    public void fiveWithMultipleGood1(){
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.add(infantry); hand.add(infantry2); hand.add(infantry3); hand.add(cavalry); hand.add(artillery);
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        toTradeIn.add(infantry); toTradeIn.add(cavalry); toTradeIn.add(artillery);
        assertEquals(true, game.checkTradeInCards(hand, toTradeIn));
    }

    // hand has 5 cards, including potentially 2 different sets, toTradeIn contains the set of 3 same cards
    @Test
    public void fiveWithMultipleGood2(){
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.add(infantry); hand.add(infantry2); hand.add(infantry3); hand.add(cavalry); hand.add(artillery);
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        toTradeIn.add(infantry); toTradeIn.add(infantry2); toTradeIn.add(infantry3);
        assertEquals(true, game.checkTradeInCards(hand, toTradeIn));
    }

    // hand has 5 cards, including a set of 3 different cards, toTradeIn has 4 cards, including that set
    @Test
    public void toTradeInTooBig(){
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.add(infantry); hand.add(infantry2); hand.add(infantry3); hand.add(cavalry); hand.add(artillery);
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        toTradeIn.add(infantry); toTradeIn.add(cavalry); toTradeIn.add(artillery);
        toTradeIn.add(infantry2);
        assertEquals(false, game.checkTradeInCards(hand, toTradeIn));
    }
}


