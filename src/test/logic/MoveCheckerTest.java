package test.logic;

import static org.junit.Assert.*;
import org.junit.*;

import java.util.*;

import logic.*;
import static logic.Move.Stage.*;

public class MoveCheckerTest {

    @Test
    public void checkClaimTerritoryGood() throws WrongMoveException {
        int[] owners = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CLAIM_TERRITORY);
        move.setTerritory(0); 
        assertTrue(checker.checkMove(move));
    }

    @Test
    public void checkClaimTerritoryBad1() throws WrongMoveException {
        int[] owners = {-1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CLAIM_TERRITORY);
        move.setTerritory(1); 
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkClaimTerritoryBad2() throws WrongMoveException {
        int[] owners = {-1, -1,  1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CLAIM_TERRITORY);
        move.setTerritory(2); 
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkClaimTerritoryInvalid1() throws WrongMoveException {
        int[] owners = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CLAIM_TERRITORY);
        move.setTerritory(-1); 
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkClaimTerritoryInvalid2() throws WrongMoveException {
        int[] owners = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CLAIM_TERRITORY);
        move.setTerritory(12); 
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkReinforceTerritoryGood() throws WrongMoveException {
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, REINFORCE_TERRITORY);
        move.setTerritory(0); 
        assertTrue(checker.checkMove(move));
    }

    @Test
    public void checkReinforceTerritoryBad() throws WrongMoveException {
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, REINFORCE_TERRITORY);
        move.setTerritory(1); 
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkReinforceTerritoryInvalid1() throws WrongMoveException {
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, REINFORCE_TERRITORY);
        move.setTerritory(-1); 
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkReinforceTerritoryInvalid2() throws WrongMoveException {
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, REINFORCE_TERRITORY);
        move.setTerritory(12); 
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkPlaceArmiesGood1() throws WrongMoveException {
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, PLACE_ARMIES);
        move.setCurrentArmies(3);
        move.setTerritory(0); 
        move.setArmies(2);
        assertTrue(checker.checkMove(move));
    }

    @Test
    public void checkPlaceArmiesGood2() throws WrongMoveException {
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, PLACE_ARMIES);
        move.setCurrentArmies(3);
        move.setTerritory(0); 
        move.setArmies(3);
        assertTrue(checker.checkMove(move));
    }

    @Test
    public void checkPlaceArmiesBad1() throws WrongMoveException {
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, PLACE_ARMIES);
        move.setCurrentArmies(3);
        move.setTerritory(0); 
        move.setArmies(4);
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkPlaceArmiesBad2() throws WrongMoveException {
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, PLACE_ARMIES);
        move.setCurrentArmies(3);
        move.setTerritory(1); 
        move.setArmies(2);
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkPlaceArmiesInvalid1() throws WrongMoveException {
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, PLACE_ARMIES);
        move.setCurrentArmies(3);
        move.setTerritory(-1); 
        move.setArmies(2);
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkPlaceArmiesInvalid2() throws WrongMoveException {
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, PLACE_ARMIES);
        move.setCurrentArmies(3);
        move.setTerritory(12); 
        move.setArmies(2);
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkPlaceArmiesInvalid3() throws WrongMoveException {
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, PLACE_ARMIES);
        move.setCurrentArmies(1);
        move.setTerritory(0); 
        move.setArmies(0);
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkStartAttackGood() throws WrongMoveException {
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, START_ATTACK);
        move.setFrom(0);
        move.setTo(1);
        assertTrue(checker.checkMove(move)); 
    }

    @Test
    public void checkStartAttackBad1() throws WrongMoveException {
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, START_ATTACK);
        move.setFrom(2);
        move.setTo(1);
        assertFalse(checker.checkMove(move)); 
    }

    @Test
    public void checkStartAttackBad2() throws WrongMoveException {
        int[] owners = {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, START_ATTACK);
        move.setFrom(0);
        move.setTo(1);
        assertFalse(checker.checkMove(move)); 
    }

    @Test
    public void checkStartAttackBad3() throws WrongMoveException {
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, START_ATTACK);
        move.setFrom(0);
        move.setTo(1);
        assertFalse(checker.checkMove(move)); 
    }

    @Test
    public void checkStartAttackBad4() throws WrongMoveException {
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, START_ATTACK);
        move.setFrom(0);
        move.setTo(4);
        assertFalse(checker.checkMove(move)); 
    }

    @Test
    public void checkStartAttackInvalid1() throws WrongMoveException {
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, START_ATTACK);
        move.setFrom(-1);
        move.setTo(1);
        assertFalse(checker.checkMove(move)); 
    }

    @Test
    public void checkStartAttackInvalid2() throws WrongMoveException {
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, START_ATTACK);
        move.setFrom(0);
        move.setTo(-1);
        assertFalse(checker.checkMove(move)); 
    }

    @Test
    public void checkStartAttackInvalid3() throws WrongMoveException {
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, START_ATTACK);
        move.setFrom(12);
        move.setTo(0);
        assertFalse(checker.checkMove(move)); 
    }

    @Test
    public void checkStartAttackInvalid4() throws WrongMoveException {
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, START_ATTACK);
        move.setFrom(0);
        move.setTo(12);
        assertFalse(checker.checkMove(move)); 
    }

    @Test
    public void checkChooseAttackDiceGood1() throws WrongMoveException {
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CHOOSE_ATTACK_DICE);
        move.setFrom(0);
        move.setAttackDice(1);
        assertTrue(checker.checkMove(move));
    }

    @Test
    public void checkChooseAttackDiceGood2() throws WrongMoveException {
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CHOOSE_ATTACK_DICE);
        move.setFrom(0);
        move.setAttackDice(3);
        assertTrue(checker.checkMove(move));
    }

    @Test
    public void checkChooseAttackDiceBad() throws WrongMoveException {
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CHOOSE_ATTACK_DICE);
        move.setFrom(0);
        move.setAttackDice(2);
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkChooseAttackDiceInvalid1() throws WrongMoveException {
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {5, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CHOOSE_ATTACK_DICE);
        move.setFrom(0);
        move.setAttackDice(4);
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkChooseAttackDiceInvalid2() throws WrongMoveException {
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CHOOSE_ATTACK_DICE);
        move.setFrom(0);
        move.setAttackDice(0);
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkChooseAttackDiceInvalid3() throws WrongMoveException {
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CHOOSE_ATTACK_DICE);
        move.setFrom(-1);
        move.setAttackDice(1);
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkChooseAttackDiceInvalid4() throws WrongMoveException {
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CHOOSE_ATTACK_DICE);
        move.setFrom(12);
        move.setAttackDice(1);
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkChooseDefendDiceGood1() throws WrongMoveException {
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CHOOSE_DEFEND_DICE);
        move.setTo(0);
        move.setDefendDice(1);
        assertTrue(checker.checkMove(move));
    }

    @Test
    public void checkChooseDefendDiceGood2() throws WrongMoveException {
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CHOOSE_DEFEND_DICE);
        move.setTo(0);
        move.setDefendDice(2);
        assertTrue(checker.checkMove(move));
    }

    @Test
    public void checkChooseDefendDiceBad1() throws WrongMoveException {
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CHOOSE_DEFEND_DICE);
        move.setTo(0);
        move.setDefendDice(2);
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkChooseDefendDiceInvalid1() throws WrongMoveException {
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CHOOSE_DEFEND_DICE);
        move.setTo(-1);
        move.setDefendDice(1);
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkChooseDefendDiceInvalid2() throws WrongMoveException {
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CHOOSE_DEFEND_DICE);
        move.setTo(12);
        move.setDefendDice(1);
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkChooseDefendDiceInvalid3() throws WrongMoveException {
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CHOOSE_DEFEND_DICE);
        move.setTo(0);
        move.setDefendDice(0);
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkChooseDefendDiceInvalid4() throws WrongMoveException {
        int[] owners = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, CHOOSE_DEFEND_DICE);
        move.setTo(0);
        move.setDefendDice(3);
        assertFalse(checker.checkMove(move));
    }

    @Test
    public void checkOccupyTerritoryGood1() throws WrongMoveException {
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
    public void checkOccupyTerritoryGood2() throws WrongMoveException {
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
    public void checkOccupyTerritoryGood3() throws WrongMoveException {
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
    public void checkOccupyTerritoryBad1() throws WrongMoveException {
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
    public void checkOccupyTerritoryBad2() throws WrongMoveException {
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
    public void checkOccupyTerritoryInvalid() throws WrongMoveException {
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
    public void checkStartFortifyGood() throws WrongMoveException {
        int[] owners = {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, START_FORTIFY);
        move.setFrom(0);
        move.setTo(1);
        assertTrue(checker.checkMove(move)); 
    }

    @Test
    public void checkStartFortifyBad1() throws WrongMoveException {
        int[] owners = {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, START_FORTIFY);
        move.setFrom(2);
        move.setTo(1);
        assertFalse(checker.checkMove(move)); 
    }

    @Test
    public void checkStartFortifyBad2() throws WrongMoveException {
        int[] owners = {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, START_FORTIFY);
        move.setFrom(0);
        move.setTo(2);
        assertFalse(checker.checkMove(move)); 
    }

    @Test
    public void checkStartFortifyBad3() throws WrongMoveException {
        int[] owners = {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, START_FORTIFY);
        move.setFrom(0);
        move.setTo(1);
        assertFalse(checker.checkMove(move)); 
    }

    @Test
    public void checkStartFortifyBad4() throws WrongMoveException {
        int[] owners = {0, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, START_FORTIFY);
        move.setFrom(0);
        move.setTo(4);
        assertFalse(checker.checkMove(move)); 
    }

    @Test
    public void checkStartFortifyInvalid1() throws WrongMoveException {
        int[] owners = {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, START_FORTIFY);
        move.setFrom(-1);
        move.setTo(1);
        assertFalse(checker.checkMove(move)); 
    }

    @Test
    public void checkStartFortifyInvalid2() throws WrongMoveException {
        int[] owners = {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, START_FORTIFY);
        move.setFrom(12);
        move.setTo(1);
        assertFalse(checker.checkMove(move)); 
    }

    @Test
    public void checkStartFortifyInvalid3() throws WrongMoveException {
        int[] owners = {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, START_FORTIFY);
        move.setFrom(0);
        move.setTo(-1);
        assertFalse(checker.checkMove(move)); 
    }

    @Test
    public void checkStartFortifyInvalid4() throws WrongMoveException {
        int[] owners = {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, START_FORTIFY);
        move.setFrom(0);
        move.setTo(12);
        assertFalse(checker.checkMove(move)); 
    }

    @Test
    public void checkFortifyTerritoryGood1() throws WrongMoveException {
        int[] owners = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, FORTIFY_TERRITORY);
        move.setCurrentArmies(3);
        move.setArmies(2);
        assertTrue(checker.checkMove(move)); 
    }

    @Test
    public void checkFortifyTerritoryGood2() throws WrongMoveException {
        int[] owners = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, FORTIFY_TERRITORY);
        move.setCurrentArmies(3);
        move.setArmies(1);
        assertTrue(checker.checkMove(move)); 
    }

    @Test
    public void checkFortifyTerritoryBad() throws WrongMoveException {
        int[] owners = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, FORTIFY_TERRITORY);
        move.setCurrentArmies(2);
        move.setArmies(2);
        assertFalse(checker.checkMove(move)); 
    }

    @Test
    public void checkFortifyTerritoryInvalid() throws WrongMoveException {
        int[] owners = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] armies = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        MoveChecker checker = new MoveChecker(new GameState(true, 3, owners, armies));
        Move move = new Move(0, FORTIFY_TERRITORY);
        move.setCurrentArmies(2);
        move.setArmies(0);
        assertFalse(checker.checkMove(move)); 
    }
}
