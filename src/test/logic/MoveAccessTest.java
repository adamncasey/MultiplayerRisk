package test.logic;

import static org.junit.Assert.*;
import org.junit.rules.*;
import org.junit.*;

import java.util.*;

import logic.*;
import logic.Move.Stage;
import static logic.Move.Stage.*;

public class MoveAccessTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void territoryAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setTerritory(1);
                assertTrue(s == CLAIM_TERRITORY || s == REINFORCE_TERRITORY || s == PLACE_ARMIES);
            }catch(WrongMoveException e){
                assertFalse(s == CLAIM_TERRITORY || s == REINFORCE_TERRITORY || s == PLACE_ARMIES);
            }
            try{
                int territory = move.getTerritory();
                assertTrue(s == CLAIM_TERRITORY || s == REINFORCE_TERRITORY || s == PLACE_ARMIES);
            }catch(WrongMoveException e){
                assertFalse(s == CLAIM_TERRITORY || s == REINFORCE_TERRITORY || s == PLACE_ARMIES);
            }
        }
    }

    @Test
    public void armiesAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setArmies(1);
                assertTrue(s == PLACE_ARMIES || s == OCCUPY_TERRITORY || s == FORTIFY_TERRITORY);
            }catch(WrongMoveException e){
                assertFalse(s == PLACE_ARMIES || s == OCCUPY_TERRITORY || s == FORTIFY_TERRITORY);
            }
            try{
                int armies = move.getArmies();
                assertTrue(s == PLACE_ARMIES || s == OCCUPY_TERRITORY || s == FORTIFY_TERRITORY);
            }catch(WrongMoveException e){
                assertFalse(s == PLACE_ARMIES || s == OCCUPY_TERRITORY || s == FORTIFY_TERRITORY);
            }
        }
    }

    @Test
    public void currentArmiesAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setCurrentArmies(1);
                assertTrue(s == PLACE_ARMIES || s == OCCUPY_TERRITORY || s == FORTIFY_TERRITORY);
            }catch(WrongMoveException e){
                assertFalse(s == PLACE_ARMIES || s == OCCUPY_TERRITORY || s == FORTIFY_TERRITORY);
            }
            try{
                int currentArmies = move.getCurrentArmies();
                assertTrue(s == PLACE_ARMIES || s == OCCUPY_TERRITORY || s == FORTIFY_TERRITORY);
            }catch(WrongMoveException e){
                assertFalse(s == PLACE_ARMIES || s == OCCUPY_TERRITORY || s == FORTIFY_TERRITORY);
            }
        }
    }

    @Test
    public void toTradeInAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setToTradeIn(new ArrayList<Card>());
                assertTrue(s == TRADE_IN_CARDS);
            }catch(WrongMoveException e){
                assertFalse(s == TRADE_IN_CARDS);
            }
            try{
                List<Card> toTradeIn = move.getToTradeIn();
                assertTrue(s == TRADE_IN_CARDS);
            }catch(WrongMoveException e){
                assertFalse(s == TRADE_IN_CARDS);
            }
        }
    }

    @Test
    public void decisionAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setDecision(true);
                assertTrue(s == DECIDE_ATTACK || s == DECIDE_FORTIFY);
            }catch(WrongMoveException e){
                assertFalse(s == DECIDE_ATTACK || s == DECIDE_FORTIFY);
            }
            try{
                boolean decision = move.getDecision();
                assertTrue(s == DECIDE_ATTACK || s == DECIDE_FORTIFY);
            }catch(WrongMoveException e){
                assertFalse(s == DECIDE_ATTACK || s == DECIDE_FORTIFY);
            }
        }
    }

    @Test
    public void fromAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setFrom(1);
                assertTrue(s == START_ATTACK || s == START_FORTIFY || s == CHOOSE_ATTACK_DICE || s == CHOOSE_DEFEND_DICE);
            }catch(WrongMoveException e){
                assertFalse(s == START_ATTACK || s == START_FORTIFY || s == CHOOSE_ATTACK_DICE || s == CHOOSE_DEFEND_DICE);
            }
            try{
                int from = move.getFrom();
                assertTrue(s == START_ATTACK || s == START_FORTIFY || s == CHOOSE_ATTACK_DICE || s == CHOOSE_DEFEND_DICE);
            }catch(WrongMoveException e){
                assertFalse(s == START_ATTACK || s == START_FORTIFY || s == CHOOSE_ATTACK_DICE || s == CHOOSE_DEFEND_DICE);
            }
        }
    }

    @Test
    public void toAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setTo(1);
                assertTrue(s == START_ATTACK || s == START_FORTIFY || s == CHOOSE_ATTACK_DICE || s == CHOOSE_DEFEND_DICE);
            }catch(WrongMoveException e){
                assertFalse(s == START_ATTACK || s == START_FORTIFY || s == CHOOSE_ATTACK_DICE || s == CHOOSE_DEFEND_DICE);
            }
            try{
                int from = move.getTo();
                assertTrue(s == START_ATTACK || s == START_FORTIFY || s == CHOOSE_ATTACK_DICE || s == CHOOSE_DEFEND_DICE);
            }catch(WrongMoveException e){
                assertFalse(s == START_ATTACK || s == START_FORTIFY || s == CHOOSE_ATTACK_DICE || s == CHOOSE_DEFEND_DICE);
            }
        }
    }

    @Test
    public void attackDiceAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setAttackDice(1);
                assertTrue(s == CHOOSE_ATTACK_DICE || s == OCCUPY_TERRITORY);
            }catch(WrongMoveException e){
                assertFalse(s == CHOOSE_ATTACK_DICE || s == OCCUPY_TERRITORY);
            }
            try{
                int attackDice = move.getAttackDice();
                assertTrue(s == CHOOSE_ATTACK_DICE || s == OCCUPY_TERRITORY);
            }catch(WrongMoveException e){
                assertFalse(s == CHOOSE_ATTACK_DICE || s == OCCUPY_TERRITORY);
            }
        }
    }

    @Test
    public void defendDiceAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setDefendDice(1);
                assertTrue(s == CHOOSE_DEFEND_DICE);
            }catch(WrongMoveException e){
                assertFalse(s == CHOOSE_DEFEND_DICE);
            }
            try{
                int defendDice = move.getDefendDice();
                assertTrue(s == CHOOSE_DEFEND_DICE);
            }catch(WrongMoveException e){
                assertFalse(s == CHOOSE_DEFEND_DICE);
            }
        }
    }

    @Test
    public void attackerLossesAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setAttackerLosses(1);
                assertTrue(s == END_ATTACK);
            }catch(WrongMoveException e){
                assertFalse(s == END_ATTACK);
            }
            try{
                int attackerLosses = move.getAttackerLosses();
                assertTrue(s == END_ATTACK);
            }catch(WrongMoveException e){
                assertFalse(s == END_ATTACK);
            }
        }
    }

    @Test
    public void defenderLossesAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setDefenderLosses(1);
                assertTrue(s == END_ATTACK);
            }catch(WrongMoveException e){
                assertFalse(s == END_ATTACK);
            }
            try{
                int defenderLosses = move.getDefenderLosses();
                assertTrue(s == END_ATTACK);
            }catch(WrongMoveException e){
                assertFalse(s == END_ATTACK);
            }
        }
    }

    @Test
    public void attackDiceRollsAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setAttackDiceRolls(new ArrayList<Integer>());
                assertTrue(s == END_ATTACK);
            }catch(WrongMoveException e){
                assertFalse(s == END_ATTACK);
            }
            try{
                List<Integer> attackDiceRolls = move.getAttackDiceRolls();
                assertTrue(s == END_ATTACK);
            }catch(WrongMoveException e){
                assertFalse(s == END_ATTACK);
            }
        }
    }

    @Test
    public void defendDiceRollsAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setDefendDiceRolls(new ArrayList<Integer>());
                assertTrue(s == END_ATTACK);
            }catch(WrongMoveException e){
                assertFalse(s == END_ATTACK);
            }
            try{
                List<Integer> defendDiceRolls = move.getDefendDiceRolls();
                assertTrue(s == END_ATTACK);
            }catch(WrongMoveException e){
                assertFalse(s == END_ATTACK);
            }
        }
    }

    @Test
    public void playerAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setPlayer(1);
                assertTrue(s == SETUP_BEGIN || s == PLAYER_ELIMINATED || s == GAME_END);
            }catch(WrongMoveException e){
                assertFalse(s == SETUP_BEGIN ||s == PLAYER_ELIMINATED || s == GAME_END);
            }
            try{
                int player = move.getPlayer();
                assertTrue(s == SETUP_BEGIN || s == PLAYER_ELIMINATED || s == GAME_END);
            }catch(WrongMoveException e){
                assertFalse(s == SETUP_BEGIN || s == PLAYER_ELIMINATED || s == GAME_END);
            }
        }
    }

    @Test
    public void turnsAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setTurns(1);
                assertTrue(s == GAME_END);
            }catch(WrongMoveException e){
                assertFalse(s == GAME_END);
            }
            try{
                int turns = move.getTurns();
                assertTrue(s == GAME_END);
            }catch(WrongMoveException e){
                assertFalse(s == GAME_END);
            }
        }
    }

    @Test
    public void territoryWriteTest() throws WrongMoveException{
        Move move = new Move(0, CLAIM_TERRITORY);
        move.setReadOnly();
        exception.expect(WrongMoveException.class);
        move.setTerritory(1);
    }

    @Test
    public void armiesWriteTest() throws WrongMoveException{
        Move move = new Move(0, PLACE_ARMIES);
        move.setReadOnly();
        exception.expect(WrongMoveException.class);
        move.setArmies(1);
    }

    @Test
    public void currentArmiesWriteTest() throws WrongMoveException{
        Move move = new Move(0, PLACE_ARMIES);
        move.setReadOnly();
        exception.expect(WrongMoveException.class);
        move.setCurrentArmies(1);
    }

    @Test
    public void tradeInCardsWriteTest() throws WrongMoveException{
        Move move = new Move(0, TRADE_IN_CARDS);
        move.setReadOnly();
        exception.expect(WrongMoveException.class);
        move.setToTradeIn(new ArrayList<Card>());
    }

    @Test
    public void decisionWriteTest() throws WrongMoveException{
        Move move = new Move(0, DECIDE_ATTACK);
        move.setReadOnly();
        exception.expect(WrongMoveException.class);
        move.setDecision(true);
    }

    @Test
    public void fromWriteTest() throws WrongMoveException{
        Move move = new Move(0, START_ATTACK);
        move.setReadOnly();
        exception.expect(WrongMoveException.class);
        move.setFrom(1);
    }

    @Test
    public void toWriteTest() throws WrongMoveException{
        Move move = new Move(0, START_ATTACK);
        move.setReadOnly();
        exception.expect(WrongMoveException.class);
        move.setTo(1);
    }

    @Test
    public void attackDiceWriteTest() throws WrongMoveException{
        Move move = new Move(0, CHOOSE_ATTACK_DICE);;
        move.setReadOnly();
        exception.expect(WrongMoveException.class);
        move.setAttackDice(1);
    }

    @Test
    public void defendDiceWriteTest() throws WrongMoveException{
        Move move = new Move(0, CHOOSE_DEFEND_DICE);
        move.setReadOnly();
        exception.expect(WrongMoveException.class);
        move.setDefendDice(1);
    }

    @Test
    public void attackerLossesWriteTest() throws WrongMoveException{
        Move move = new Move(0, END_ATTACK);
        move.setReadOnly();
        exception.expect(WrongMoveException.class);
        move.setAttackerLosses(1);
    }

    @Test
    public void defenderLossesWriteTest() throws WrongMoveException{
        Move move = new Move(0, END_ATTACK);
        move.setReadOnly();
        exception.expect(WrongMoveException.class);
        move.setDefenderLosses(1);
    }

    @Test
    public void attackDiceRollsWriteTest() throws WrongMoveException{
        Move move = new Move(0, END_ATTACK);
        move.setReadOnly();
        exception.expect(WrongMoveException.class);
        move.setAttackDiceRolls(new ArrayList<Integer>());
    }

    @Test
    public void defendDiceRollsWriteTest() throws WrongMoveException{
        Move move = new Move(0, END_ATTACK);
        move.setReadOnly();
        exception.expect(WrongMoveException.class);
        move.setDefendDiceRolls(new ArrayList<Integer>());
    }

    @Test
    public void playerWriteTest() throws WrongMoveException{
        Move move = new Move(0, PLAYER_ELIMINATED);
        move.setReadOnly();
        exception.expect(WrongMoveException.class);
        move.setPlayer(1);
    }

    @Test
    public void turnsWriteTest() throws WrongMoveException{
        Move move = new Move(0, GAME_END);
        move.setReadOnly();
        exception.expect(WrongMoveException.class);
        move.setTurns(1);
    }
}
