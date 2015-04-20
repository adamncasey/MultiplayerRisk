package test.logic.move;

import static org.junit.Assert.*;
import org.junit.*;
import java.util.ArrayList;
import java.util.List;
import logic.move.Move;
import logic.move.MoveChecker;
import logic.state.GameState;
import settings.Settings;
import static logic.move.Move.Stage.*;

public class MoveCheckerTest {

    @Test
    public void checkClaimTerritoryGood(){
        int[] owners = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CLAIM_TERRITORY);
        move.setTerritory(0); 
        assertTrue(checker.checkMove(move));
    }

    @Test
    public void checkClaimTerritoryBad1(){
        int[] owners = {-1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CLAIM_TERRITORY);
        move.setTerritory(1); 
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkClaimTerritoryBad2(){
        int[] owners = {-1, -1,  1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CLAIM_TERRITORY);
        move.setTerritory(2); 
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkClaimTerritoryInvalid1(){
        int[] owners = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CLAIM_TERRITORY);
        move.setTerritory(-1); 
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkClaimTerritoryInvalid2(){
        int[] owners = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CLAIM_TERRITORY);
        move.setTerritory(12); 
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkReinforceTerritoryGood(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, REINFORCE_TERRITORY);
        move.setTerritory(0); 
        assertTrue(checker.checkMove(move));
    }

    @Test
    public void checkReinforceTerritoryBad(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, REINFORCE_TERRITORY);
        move.setTerritory(1); 
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkReinforceTerritoryInvalid1(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, REINFORCE_TERRITORY);
        move.setTerritory(-1); 
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkReinforceTerritoryInvalid2(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, REINFORCE_TERRITORY);
        move.setTerritory(12); 
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkPlaceArmiesGood1(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, PLACE_ARMIES);
        move.setCurrentArmies(3);
        move.setExtraArmies(0);
        move.setMatches(new ArrayList<Integer>());
        move.setTerritory(0); 
        move.setArmies(2);
        assertTrue(checker.checkMove(move));
    }

    @Test
    public void checkPlaceArmiesGood2(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, PLACE_ARMIES);
        move.setCurrentArmies(3);
        move.setExtraArmies(0);
        move.setMatches(new ArrayList<Integer>());
        move.setTerritory(0); 
        move.setArmies(3);
        assertTrue(checker.checkMove(move));
    }

    @Test
    public void checkPlaceArmiesGood3(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, PLACE_ARMIES);
        move.setCurrentArmies(0);
        move.setExtraArmies(2);
        List<Integer> matches = new ArrayList<Integer>();
        matches.add(0);
        move.setMatches(matches);
        move.setTerritory(0); 
        move.setArmies(2);
        assertTrue(checker.checkMove(move));
    }

    @Test
    public void checkPlaceArmiesGood4(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, PLACE_ARMIES);
        move.setCurrentArmies(1);
        move.setExtraArmies(2);
        List<Integer> matches = new ArrayList<Integer>();
        matches.add(0);
        move.setMatches(matches);
        move.setTerritory(0); 
        move.setArmies(1);
        assertTrue(checker.checkMove(move));
    }

    @Test
    public void checkPlaceArmiesSpecial(){
        int[] owners = {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, PLACE_ARMIES);
        move.setCurrentArmies(0);
        move.setExtraArmies(2);
        List<Integer> matches = new ArrayList<Integer>();
        matches.add(0);
        matches.add(1);
        move.setMatches(matches);
        move.setTerritory(1); 
        move.setArmies(1);
        if(Settings.ExtraArmiesTogether){
            assertFalse(checker.checkMove(move));
        }else{
            assertTrue(checker.checkMove(move));
        }
    }

    @Test
    public void checkPlaceArmiesBad1(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, PLACE_ARMIES);
        move.setCurrentArmies(3);
        move.setExtraArmies(0);
        move.setMatches(new ArrayList<Integer>());
        move.setTerritory(0); 
        move.setArmies(4);
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkPlaceArmiesBad2(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, PLACE_ARMIES);
        move.setCurrentArmies(3);
        move.setExtraArmies(0);
        move.setMatches(new ArrayList<Integer>());
        move.setTerritory(1); 
        move.setArmies(2);
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkPlaceArmiesBad3(){
        int[] owners = {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, PLACE_ARMIES);
        move.setCurrentArmies(1);
        move.setExtraArmies(2);
        List<Integer> matches = new ArrayList<Integer>();
        matches.add(1);
        move.setMatches(matches);
        move.setTerritory(0); 
        move.setArmies(2);
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkPlaceArmiesInvalid1(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, PLACE_ARMIES);
        move.setCurrentArmies(3);
        move.setExtraArmies(0);
        move.setMatches(new ArrayList<Integer>());
        move.setTerritory(-1); 
        move.setArmies(2);
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkPlaceArmiesInvalid2(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, PLACE_ARMIES);
        move.setCurrentArmies(3);
        move.setExtraArmies(0);
        move.setMatches(new ArrayList<Integer>());
        move.setTerritory(12); 
        move.setArmies(2);
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkPlaceArmiesInvalid3(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, PLACE_ARMIES);
        move.setCurrentArmies(1);
        move.setExtraArmies(0);
        move.setMatches(new ArrayList<Integer>());
        move.setTerritory(0); 
        move.setArmies(0);
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkStartAttackGood(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, START_ATTACK);
        move.setFrom(0);
        move.setTo(1);
        assertTrue(checker.checkMove(move)); 
    }

    @Test
    public void checkStartAttackBad1(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, START_ATTACK);
        move.setFrom(2);
        move.setTo(1);
        assertFalse(checker.checkMove(move)); 
    }

    @Test
    public void checkStartAttackBad2(){
        int[] owners = {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, START_ATTACK);
        move.setFrom(0);
        move.setTo(1);
        assertFalse(checker.checkMove(move)); 
    }

    @Test
    public void checkStartAttackBad3(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, START_ATTACK);
        move.setFrom(0);
        move.setTo(1);
        assertFalse(checker.checkMove(move)); 
    }

    @Test
    public void checkStartAttackBad4(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, START_ATTACK);
        move.setFrom(0);
        move.setTo(4);
        assertFalse(checker.checkMove(move)); 
    }

    @Test
    public void checkStartAttackInvalid1(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, START_ATTACK);
        move.setFrom(-1);
        move.setTo(1);
        assertFalse(checker.checkMove(move)); 
    }

    @Test
    public void checkStartAttackInvalid2(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, START_ATTACK);
        move.setFrom(0);
        move.setTo(-1);
        assertFalse(checker.checkMove(move)); 
    }

    @Test
    public void checkStartAttackInvalid3(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, START_ATTACK);
        move.setFrom(12);
        move.setTo(0);
        assertFalse(checker.checkMove(move)); 
    }

    @Test
    public void checkStartAttackInvalid4(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, START_ATTACK);
        move.setFrom(0);
        move.setTo(12);
        assertFalse(checker.checkMove(move)); 
    }

    @Test
    public void checkChooseAttackDiceGood1(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CHOOSE_ATTACK_DICE);
        move.setFrom(0);
        move.setAttackDice(1);
        assertTrue(checker.checkMove(move));
    }

    @Test
    public void checkChooseAttackDiceGood2(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CHOOSE_ATTACK_DICE);
        move.setFrom(0);
        move.setAttackDice(3);
        assertTrue(checker.checkMove(move));
    }

    @Test
    public void checkChooseAttackDiceBad(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CHOOSE_ATTACK_DICE);
        move.setFrom(0);
        move.setAttackDice(2);
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkChooseAttackDiceInvalid1(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {5, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CHOOSE_ATTACK_DICE);
        move.setFrom(0);
        move.setAttackDice(4);
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkChooseAttackDiceInvalid2(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CHOOSE_ATTACK_DICE);
        move.setFrom(0);
        move.setAttackDice(0);
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkChooseAttackDiceInvalid3(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CHOOSE_ATTACK_DICE);
        move.setFrom(-1);
        move.setAttackDice(1);
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkChooseAttackDiceInvalid4(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CHOOSE_ATTACK_DICE);
        move.setFrom(12);
        move.setAttackDice(1);
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkChooseDefendDiceGood1(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CHOOSE_DEFEND_DICE);
        move.setTo(0);
        move.setDefendDice(1);
        assertTrue(checker.checkMove(move));
    }

    @Test
    public void checkChooseDefendDiceGood2(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CHOOSE_DEFEND_DICE);
        move.setTo(0);
        move.setDefendDice(2);
        assertTrue(checker.checkMove(move));
    }

    @Test
    public void checkChooseDefendDiceBad1(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CHOOSE_DEFEND_DICE);
        move.setTo(0);
        move.setDefendDice(2);
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkChooseDefendDiceInvalid1(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CHOOSE_DEFEND_DICE);
        move.setTo(-1);
        move.setDefendDice(1);
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkChooseDefendDiceInvalid2(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CHOOSE_DEFEND_DICE);
        move.setTo(12);
        move.setDefendDice(1);
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkChooseDefendDiceInvalid3(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CHOOSE_DEFEND_DICE);
        move.setTo(0);
        move.setDefendDice(0);
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkChooseDefendDiceInvalid4(){
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CHOOSE_DEFEND_DICE);
        move.setTo(0);
        move.setDefendDice(3);
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkOccupyTerritoryGood1(){
        int[] owners = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, OCCUPY_TERRITORY);
        move.setAttackDice(1);
        move.setCurrentArmies(2);
        move.setArmies(1);
        assertTrue(checker.checkMove(move));
    }

    @Test
    public void checkOccupyTerritoryGood2(){
        int[] owners = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, OCCUPY_TERRITORY);
        move.setAttackDice(2);
        move.setCurrentArmies(3);
        move.setArmies(2);
        assertTrue(checker.checkMove(move));
    }

    @Test
    public void checkOccupyTerritoryGood3(){
        int[] owners = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, OCCUPY_TERRITORY);
        move.setAttackDice(2);
        move.setCurrentArmies(4);
        move.setArmies(3);
        assertTrue(checker.checkMove(move));
    }

    @Test
    public void checkOccupyTerritoryBad1(){
        int[] owners = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, OCCUPY_TERRITORY);
        move.setAttackDice(2);
        move.setCurrentArmies(3);
        move.setArmies(1);
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkOccupyTerritoryBad2(){
        int[] owners = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, OCCUPY_TERRITORY);
        move.setAttackDice(2);
        move.setCurrentArmies(2);
        move.setArmies(2);
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkOccupyTerritoryInvalid(){
        int[] owners = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, OCCUPY_TERRITORY);
        move.setAttackDice(1);
        move.setCurrentArmies(2);
        move.setArmies(0);
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkStartFortifyGood(){
        int[] owners = {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, START_FORTIFY);
        move.setFrom(0);
        move.setTo(1);
        assertTrue(checker.checkMove(move)); 
    }

    @Test
    public void checkStartFortifyBad1(){
        int[] owners = {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, START_FORTIFY);
        move.setFrom(2);
        move.setTo(1);
        assertFalse(checker.checkMove(move)); 
    }

    @Test
    public void checkStartFortifyBad2(){
        int[] owners = {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, START_FORTIFY);
        move.setFrom(0);
        move.setTo(2);
        assertFalse(checker.checkMove(move)); 
    }

    @Test
    public void checkStartFortifyBad3(){
        int[] owners = {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, START_FORTIFY);
        move.setFrom(0);
        move.setTo(1);
        assertFalse(checker.checkMove(move)); 
    }

    @Test
    public void checkStartFortifyBad4(){
        int[] owners = {0, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, START_FORTIFY);
        move.setFrom(0);
        move.setTo(4);
        assertFalse(checker.checkMove(move)); 
    }

    @Test
    public void checkStartFortifyInvalid1(){
        int[] owners = {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, START_FORTIFY);
        move.setFrom(-1);
        move.setTo(1);
        assertFalse(checker.checkMove(move)); 
    }

    @Test
    public void checkStartFortifyInvalid2(){
        int[] owners = {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, START_FORTIFY);
        move.setFrom(12);
        move.setTo(1);
        assertFalse(checker.checkMove(move)); 
    }

    @Test
    public void checkStartFortifyInvalid3(){
        int[] owners = {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, START_FORTIFY);
        move.setFrom(0);
        move.setTo(-1);
        assertFalse(checker.checkMove(move)); 
    }

    @Test
    public void checkStartFortifyInvalid4(){
        int[] owners = {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, START_FORTIFY);
        move.setFrom(0);
        move.setTo(12);
        assertFalse(checker.checkMove(move)); 
    }

    @Test
    public void checkFortifyTerritoryGood1(){
        int[] owners = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, FORTIFY_TERRITORY);
        move.setCurrentArmies(3);
        move.setArmies(2);
        assertTrue(checker.checkMove(move)); 
    }

    @Test
    public void checkFortifyTerritoryGood2(){
        int[] owners = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, FORTIFY_TERRITORY);
        move.setCurrentArmies(3);
        move.setArmies(1);
        assertTrue(checker.checkMove(move)); 
    }

    @Test
    public void checkFortifyTerritoryBad(){
        int[] owners = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, FORTIFY_TERRITORY);
        move.setCurrentArmies(2);
        move.setArmies(2);
        assertFalse(checker.checkMove(move)); 
    }

    @Test
    public void checkFortifyTerritoryInvalid(){
        int[] owners = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, FORTIFY_TERRITORY);
        move.setCurrentArmies(2);
        move.setArmies(0);
        assertFalse(checker.checkMove(move)); 
    }

    @Test
    public void checkRollHashGood1(){
        int[] owners = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, ROLL_HASH);
        move.setRollHash("1234123412341234123412341234123412341234123412341234123412341234");
        assertTrue(checker.checkMove(move));
    }

    @Test
    public void checkRollHashInvalid1(){
        int[] owners = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, ROLL_HASH);
        move.setRollHash("123412341234123412341234123412341234123412341234123412341234123");
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkRollHashInvalid2(){
        int[] owners = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, ROLL_HASH);
        move.setRollHash("12341234123412341234123412341234123412341234123412341234123412341");
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkRollHashInvalid3(){
        int[] owners = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, ROLL_HASH);
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkRollNumberGood1(){
        int[] owners = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, ROLL_NUMBER);
        move.setRollHash("DEA972021186F84A751A1AC1F981F850A4C5A0B7C1A2669949F7120F2AC3D073");
        move.setRollNumber("459D935F60606172953493ABDDF59DEA1B3402A7FC33781F74FDB19356231C12");
        assertTrue(checker.checkMove(move));
    }

    @Test
    public void checkRollNumberBad1(){
        int[] owners = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, ROLL_NUMBER);
        move.setRollHash("1234123412341234123412341234123412341234123412341234123412341234");
        String hashNumber = "1234123412341234123412341234123412341234123412341234123412341234";
        move.setRollNumber(hashNumber);
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkRollNumberInvalid1(){
        int[] owners = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, ROLL_NUMBER);
        move.setRollHash("1234123412341234123412341234123412341234123412341234123412341234");
        assertFalse(checker.checkMove(move));
    }
}
