package test.logic;

import static org.junit.Assert.*;
import org.junit.*;

import java.util.ArrayList;

import logic.*;

public class CardTest{

    private Card card;
    private Card cardSame;
    private Card cardID;
    private Card cardType;
    private Card cardName;

    @Before
    public void setupCards(){
        card = new Card(1, 1, "country");
        cardSame = new Card(1, 1, "country");
        cardID = new Card(2, 1, "country");
        cardType = new Card(1, 5, "country");
        cardName = new Card(1, 1, "different");
    }

    // c1 is equal to c2 
    @Test
    public void equalCards(){
        assertEquals(true, card.equals(cardSame));
        assertEquals(true, cardSame.equals(card));
    }

    // c2 has a different ID than c1
    @Test
    public void differentID(){
        assertEquals(false, card.equals(cardID));
        assertEquals(false, cardID.equals(card));
    }

    // c2 has a different type than c1
    @Test
    public void differentType(){
        assertEquals(false, card.equals(cardType));
        assertEquals(false, cardType.equals(card));
    }

    // c2 has a different name than c1
    @Test
    public void differentName(){
        assertEquals(false, card.equals(cardName));
        assertEquals(false, cardName.equals(card));
    }

    // check that getters return correct values
    @Test
    public void getterTest(){
        assertEquals(Integer.valueOf(1), card.getID());
        assertEquals(Integer.valueOf(1), card.getType());
        assertEquals("country", card.getName());
    }
}
