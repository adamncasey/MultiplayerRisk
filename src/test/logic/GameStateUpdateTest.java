package test.logic;

import static org.junit.Assert.*;
import org.junit.*;

import java.util.*;

import logic.*;
import player.*;

public class GameStateUpdateTest{

    @Test
    public void numArmies0Owned(){
        int[] owners = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        GameState game = new GameState(true, 3, owners, armies);
        int result = game.calculatePlayerArmies(0, false, new ArrayList<Card>());
        assertEquals(3, result);
    }

    @Test
    public void numArmies11Owned(){
        int[] owners = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        GameState game = new GameState(true, 3, owners, armies);
        int result = game.calculatePlayerArmies(0, false, new ArrayList<Card>());
        assertEquals(10, result);
    }

    @Test
    public void numArmies12Owned(){
        int[] owners = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        GameState game = new GameState(true, 3, owners, armies);
        int result = game.calculatePlayerArmies(0, false, new ArrayList<Card>());
        assertEquals(16, result);
    }

    @Test
    public void numArmiesSet(){
        int[] owners = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        GameState game = new GameState(true, 3, owners, armies);
        int result = game.calculatePlayerArmies(0, true, new ArrayList<Card>());
        assertEquals(7, result);
    }

    @Test
    public void numArmiesNotMatching(){
        int[] owners = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        GameState game = new GameState(true, 3, owners, armies);
        List<Card> toTradeIn = new ArrayList<Card>();
        toTradeIn.add(new Card(0, 1, "T0"));
        int result = game.calculatePlayerArmies(0, true, toTradeIn);
        assertEquals(7, result);
    }

    @Test
    public void numArmiesMatching(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        GameState game = new GameState(true, 3, owners, armies);
        List<Card> toTradeIn = new ArrayList<Card>();
        toTradeIn.add(new Card(0, 1, "T0"));
        int result = game.calculatePlayerArmies(0, true, toTradeIn);
        assertEquals(9, result);
    }

    @Test
    public void numArmies2Matching(){
        int[] owners = {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        GameState game = new GameState(true, 3, owners, armies);
        List<Card> toTradeIn = new ArrayList<Card>();
        toTradeIn.add(new Card(0, 1, "T0"));
        toTradeIn.add(new Card(1, 1, "T1"));
        int result = game.calculatePlayerArmies(0, true, toTradeIn);
        assertEquals(9, result);
    }

    @Test
    public void setCounterTest(){
        GameState game = new GameState(0, 0); 
        int expectedValues[] = {4, 6, 8, 10, 12, 15, 20, 25};
        for(int i : expectedValues){
            assertEquals(i, game.incrementSetCounter());
        }
    }

    @Test
    public void attackIsPossible(){
        int[] owners = {0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2};
        int[] armies = {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2};
        GameState game = new GameState(true, 3, owners, armies);
        boolean result = game.checkAttackPossible(0);
        assertTrue(result);
    }

    @Test
    public void attackIsntPossible1(){
        int[] owners = {0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2};
        int[] armies = {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2};
        GameState game = new GameState(true, 3, owners, armies);
        boolean result = game.checkAttackPossible(3);
        assertFalse(result);
    }

    @Test
    public void attackIsntPossible2(){
        int[] owners = {0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2};
        int[] armies = {1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2};
        GameState game = new GameState(true, 3, owners, armies);
        boolean result = game.checkAttackPossible(0);
        assertFalse(result);
    }

    @Test
    public void attackIsntPossible3(){
        int[] owners = {0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2};
        int[] armies = {2, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2};
        GameState game = new GameState(true, 3, owners, armies);
        boolean result = game.checkAttackPossible(0);
        assertFalse(result);
    }

    @Test
    public void fortifyIsPossible1(){
        int[] owners = {0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2};
        int[] armies = {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2};
        GameState game = new GameState(true, 3, owners, armies);
        boolean result = game.checkFortifyPossible(0);
        assertTrue(result);
    }

    @Test
    public void fortifyIsPossible2(){
        int[] owners = {0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2};
        int[] armies = {1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2};
        GameState game = new GameState(true, 3, owners, armies);
        boolean result = game.checkFortifyPossible(0);
        assertTrue(result);
    }

    @Test
    public void fortifyIsntPossible1(){
        int[] owners = {0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2};
        int[] armies = {1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2};
        GameState game = new GameState(true, 3, owners, armies);
        boolean result = game.checkFortifyPossible(0);
        assertFalse(result);
    }

    @Test
    public void fortifyIsntPossible2(){
        int[] owners = {0, 0, 0, 0, 1, 1, 1, 1, 0, 2, 2, 2};
        int[] armies = {1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2};
        GameState game = new GameState(true, 3, owners, armies);
        boolean result = game.checkFortifyPossible(0);
        assertFalse(result);
    }

    @Test
    public void fortifyIsntPossible3(){
        int[] owners = {0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2};
        int[] armies = {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2};
        GameState game = new GameState(true, 3, owners, armies);
        boolean result = game.checkFortifyPossible(3);
        assertFalse(result);
    }

    @Test
    public void isntEliminated(){
        int[] owners = {0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2};
        int[] armies = {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2};
        GameState game = new GameState(true, 3, owners, armies);
        boolean result = game.isEliminated(0);
        assertFalse(result);
    }

    @Test
    public void isEliminated(){
        int[] owners = {1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2};
        int[] armies = {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2};
        GameState game = new GameState(true, 3, owners, armies);
        boolean result = game.isEliminated(0);
        assertTrue(result);
    }
}

