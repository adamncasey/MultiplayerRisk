package test.logic.state;

import static org.junit.Assert.*;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import logic.Card;
import logic.move.Move;
import logic.state.GameState;
import player.IPlayer;

public class GameStateUpdateTest{

    @Test
    public void territoryArmies0Owned(){
        int[] owners = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        GameState game = new GameState(true, 3, owners, armies);
        int result = game.calculateTerritoryArmies(0);
        assertEquals(3, result);
    }

    @Test
    public void territoryArmies11Owned(){
        int[] owners = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        GameState game = new GameState(true, 3, owners, armies);
        int result = game.calculateTerritoryArmies(0);
        assertEquals(3, result);
    }

    @Test
    public void territoryArmies12Owned(){
        int[] owners = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        GameState game = new GameState(true, 3, owners, armies);
        int result = game.calculateTerritoryArmies(0);
        assertEquals(4, result);
    }

    @Test
    public void continentArmies0Owned(){
        int[] owners = {0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        GameState game = new GameState(true, 3, owners, armies);
        int result = game.calculateContinentArmies(0);
        assertEquals(0, result);
    }

    @Test
    public void continentArmiesAOwned(){
        int[] owners = {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        GameState game = new GameState(true, 3, owners, armies);
        int result = game.calculateContinentArmies(0);
        assertEquals(3, result);
    }

    @Test
    public void continentArmiesBOwned(){
        int[] owners = {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        GameState game = new GameState(true, 3, owners, armies);
        int result = game.calculateContinentArmies(0);
        assertEquals(4, result);
    }

    @Test
    public void continentArmies2Owned(){
        int[] owners = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        GameState game = new GameState(true, 3, owners, armies);
        int result = game.calculateContinentArmies(0);
        assertEquals(7, result);
    }

    @Test
    public void setArmiesTest1(){
        GameState game = new GameState(new ArrayList<IPlayer>(), new ArrayList<String>(), null); 
        int expectedValues[] = {4, 6, 8, 10, 12, 15, 20, 25};
        for(int i : expectedValues){
            assertEquals(i, game.calculateSetArmies(1));
        }
    }

    @Test
    public void setArmiesTest2(){
        GameState game = new GameState(new ArrayList<IPlayer>(), new ArrayList<String>(), null); 
        int expectedValues[] = {10, 18, 27, 45};
        for(int i : expectedValues){
            assertEquals(i, game.calculateSetArmies(2));
        }
    }

    @Test
    public void matchingArmiesBad(){
        int[] owners = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        GameState game = new GameState(true, 3, owners, armies);
        List<Card> toTradeIn = new ArrayList<Card>();
        toTradeIn.add(new Card(0, 1, "T0"));
        List<Integer> matches = game.calculateMatchingCards(0, toTradeIn);
        int result = game.calculateMatchingArmies(matches);
        assertEquals(result, 0);
    }

    @Test
    public void matchingArmiesGood1(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        GameState game = new GameState(true, 3, owners, armies);
        List<Card> toTradeIn = new ArrayList<Card>();
        toTradeIn.add(new Card(0, 1, "T0"));
        List<Integer> matches = game.calculateMatchingCards(0, toTradeIn);
        int result = game.calculateMatchingArmies(matches);
        assertEquals(result, 2);
    }

    @Test
    public void matchingArmiesGood2(){
        int[] owners = {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        GameState game = new GameState(true, 3, owners, armies);
        List<Card> toTradeIn = new ArrayList<Card>();
        toTradeIn.add(new Card(0, 1, "T0"));
        toTradeIn.add(new Card(1, 1, "T1"));
        List<Integer> matches = game.calculateMatchingCards(0, toTradeIn);
        int result = game.calculateMatchingArmies(matches);
        assertEquals(result, 2);
    }

    @Test
    public void updateExtraArmies1(){
        int[] owners = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        GameState game = new GameState(true, 3, owners, armies);
        List<Integer> matches = new ArrayList<Integer>();
        int result = game.updateExtraArmies(0, 1, 2, matches);
        assertEquals(2, result);
    }

    @Test
    public void updateExtraArmies2(){
        int[] owners = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        GameState game = new GameState(true, 3, owners, armies);
        List<Integer> matches = new ArrayList<Integer>();
        matches.add(0);
        matches.add(1);
        int result = game.updateExtraArmies(1, 1, 2, matches);
        assertEquals(1, result);
    }

    @Test
    public void updateExtraArmies3(){
        int[] owners = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        GameState game = new GameState(true, 3, owners, armies);
        List<Integer> matches = new ArrayList<Integer>();
        matches.add(0);
        int result = game.updateExtraArmies(0, 3, 2, matches);
        assertEquals(0, result);
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

