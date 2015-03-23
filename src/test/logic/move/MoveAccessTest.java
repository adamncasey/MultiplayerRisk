package test.logic.move;

import static org.junit.Assert.*;
import org.junit.rules.*;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import logic.Card;
import logic.move.Move;
import logic.move.Move.Stage;
import logic.move.WrongMoveException;

import static logic.move.Move.Stage.*;

public class MoveAccessTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void territoryWriteAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setTerritory(1);
                assertTrue(s == CLAIM_TERRITORY || s == REINFORCE_TERRITORY || s == PLACE_ARMIES);
            }catch(WrongMoveException e){
                assertFalse(s == CLAIM_TERRITORY || s == REINFORCE_TERRITORY || s == PLACE_ARMIES);
            }
        }
    }

    @Test
    public void territoryReadAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                int territory = move.getTerritory();
                assertTrue(s == CLAIM_TERRITORY || s == REINFORCE_TERRITORY || s == PLACE_ARMIES);
            }catch(WrongMoveException e){
                assertFalse(s == CLAIM_TERRITORY || s == REINFORCE_TERRITORY || s == PLACE_ARMIES);
            }
        }
    }


    @Test
    public void territoryInputsAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setReadOnlyInputs();
                move.setTerritory(1);
                assertTrue(s == CLAIM_TERRITORY || s == REINFORCE_TERRITORY || s == PLACE_ARMIES);
            }catch(WrongMoveException e){
                assertFalse(s == CLAIM_TERRITORY || s == REINFORCE_TERRITORY || s == PLACE_ARMIES);
            }
        }
    }

    @Test
    public void armiesWriteAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setArmies(1);
                assertTrue(s == PLACE_ARMIES || s == OCCUPY_TERRITORY || s == FORTIFY_TERRITORY);
            }catch(WrongMoveException e){
                assertFalse(s == PLACE_ARMIES || s == OCCUPY_TERRITORY || s == FORTIFY_TERRITORY);
            }
        }
    }

    @Test
    public void armiesReadAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                int armies = move.getArmies();
                assertTrue(s == PLACE_ARMIES || s == OCCUPY_TERRITORY || s == FORTIFY_TERRITORY);
            }catch(WrongMoveException e){
                assertFalse(s == PLACE_ARMIES || s == OCCUPY_TERRITORY || s == FORTIFY_TERRITORY);
            }
        }
    }

    @Test
    public void armiesInputsAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setReadOnlyInputs();
                move.setArmies(1);
                assertTrue(s == PLACE_ARMIES || s == OCCUPY_TERRITORY || s == FORTIFY_TERRITORY);
            }catch(WrongMoveException e){
                assertFalse(s == PLACE_ARMIES || s == OCCUPY_TERRITORY || s == FORTIFY_TERRITORY);
            }
        }
    }

    @Test
    public void currentArmiesWriteAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setCurrentArmies(1);
                assertTrue(s == PLACE_ARMIES || s == OCCUPY_TERRITORY || s == FORTIFY_TERRITORY);
            }catch(WrongMoveException e){
                assertFalse(s == PLACE_ARMIES || s == OCCUPY_TERRITORY || s == FORTIFY_TERRITORY);
            }
        }
    }

    @Test
    public void currentArmiesReadAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                int currentArmies = move.getCurrentArmies();
                assertTrue(s == PLACE_ARMIES || s == OCCUPY_TERRITORY || s == FORTIFY_TERRITORY);
            }catch(WrongMoveException e){
                assertFalse(s == PLACE_ARMIES || s == OCCUPY_TERRITORY || s == FORTIFY_TERRITORY);
            }
        }
    }

    @Test
    public void currentArmiesInputsAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setReadOnlyInputs();
                move.setCurrentArmies(1);
                fail();
            }catch(WrongMoveException e){
            }
        }
    }

    @Test
    public void extraArmiesWriteAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setExtraArmies(1);
                assertTrue(s == PLACE_ARMIES);
            }catch(WrongMoveException e){
                assertFalse(s == PLACE_ARMIES);
            }
        }
    }

    @Test
    public void extraArmiesReadAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                int extraArmies = move.getExtraArmies();
                assertTrue(s == PLACE_ARMIES);
            }catch(WrongMoveException e){
                assertFalse(s == PLACE_ARMIES);
            }
        }
    }

    @Test
    public void extraArmiesInputsAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setReadOnlyInputs();
                move.setExtraArmies(1);
                fail();
            }catch(WrongMoveException e){
            }
        }
    }

    @Test
    public void matchesWriteAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setMatches(new ArrayList<Integer>());
                assertTrue(s == PLACE_ARMIES);
            }catch(WrongMoveException e){
                assertFalse(s == PLACE_ARMIES);
            }
        }
    }

    @Test
    public void matchesReadAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setMatches(new ArrayList<Integer>());
                List<Integer> matches = move.getMatches();
                assertTrue(s == PLACE_ARMIES);
            }catch(WrongMoveException e){
                assertFalse(s == PLACE_ARMIES);
            }
        }
    }

    @Test
    public void matchesInputsAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setReadOnlyInputs();
                move.setMatches(new ArrayList<Integer>());
                fail();
            }catch(WrongMoveException e){
            }
        }
    }

    @Test
    public void toTradeInWriteAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setToTradeIn(new ArrayList<Card>());
                assertTrue(s == TRADE_IN_CARDS);
            }catch(WrongMoveException e){
                assertFalse(s == TRADE_IN_CARDS);
            }
        }
    }

    @Test
    public void toTradeInReadAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setToTradeIn(new ArrayList<Card>());
                List<Card> toTradeIn = move.getToTradeIn();
                assertTrue(s == TRADE_IN_CARDS);
            }catch(WrongMoveException e){
                assertFalse(s == TRADE_IN_CARDS);
            }
        }
    }

    @Test
    public void toTradeInInputsAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setReadOnlyInputs();
                move.setToTradeIn(new ArrayList<Card>());
                assertTrue(s == TRADE_IN_CARDS);
            }catch(WrongMoveException e){
                assertFalse(s == TRADE_IN_CARDS);
            }
        }
    }

    @Test
    public void decisionWriteAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setDecision(true);
                assertTrue(s == DECIDE_ATTACK || s == DECIDE_FORTIFY);
            }catch(WrongMoveException e){
                assertFalse(s == DECIDE_ATTACK || s == DECIDE_FORTIFY);
            }
        }
    }

    @Test
    public void decisionReadAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                boolean decision = move.getDecision();
                assertTrue(s == DECIDE_ATTACK || s == DECIDE_FORTIFY);
            }catch(WrongMoveException e){
                assertFalse(s == DECIDE_ATTACK || s == DECIDE_FORTIFY);
            }
        }
    }

    @Test
    public void decisionInputsAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setReadOnlyInputs();
                move.setDecision(true);
                assertTrue(s == DECIDE_ATTACK || s == DECIDE_FORTIFY);
            }catch(WrongMoveException e){
                assertFalse(s == DECIDE_ATTACK || s == DECIDE_FORTIFY);
            }
        }
    }

    @Test
    public void fromWriteAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setFrom(1);
                assertTrue(s == START_ATTACK || s == START_FORTIFY || s == CHOOSE_ATTACK_DICE || s == CHOOSE_DEFEND_DICE);
            }catch(WrongMoveException e){
                assertFalse(s == START_ATTACK || s == START_FORTIFY || s == CHOOSE_ATTACK_DICE || s == CHOOSE_DEFEND_DICE);
            }
        }
    }

    @Test
    public void fromReadAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                int from = move.getFrom();
                assertTrue(s == START_ATTACK || s == START_FORTIFY || s == CHOOSE_ATTACK_DICE || s == CHOOSE_DEFEND_DICE);
            }catch(WrongMoveException e){
                assertFalse(s == START_ATTACK || s == START_FORTIFY || s == CHOOSE_ATTACK_DICE || s == CHOOSE_DEFEND_DICE);
            }
        }
    }

    @Test
    public void fromInputsAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setReadOnlyInputs();
                move.setFrom(1);
                assertTrue(s == START_ATTACK || s == START_FORTIFY);
            }catch(WrongMoveException e){
                assertFalse(s == START_ATTACK || s == START_FORTIFY);
            }
        }
    }

    @Test
    public void toWriteAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setTo(1);
                assertTrue(s == START_ATTACK || s == START_FORTIFY || s == CHOOSE_ATTACK_DICE || s == CHOOSE_DEFEND_DICE);
            }catch(WrongMoveException e){
                assertFalse(s == START_ATTACK || s == START_FORTIFY || s == CHOOSE_ATTACK_DICE || s == CHOOSE_DEFEND_DICE);
            }
        }
    }

    @Test
    public void toReadAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setTo(1);
                assertTrue(s == START_ATTACK || s == START_FORTIFY || s == CHOOSE_ATTACK_DICE || s == CHOOSE_DEFEND_DICE);
            }catch(WrongMoveException e){
                assertFalse(s == START_ATTACK || s == START_FORTIFY || s == CHOOSE_ATTACK_DICE || s == CHOOSE_DEFEND_DICE);
            }
        }
    }

    @Test
    public void toInputsAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setReadOnlyInputs();
                move.setTo(1);
                assertTrue(s == START_ATTACK || s == START_FORTIFY);
            }catch(WrongMoveException e){
                assertFalse(s == START_ATTACK || s == START_FORTIFY);
            }
        }
    }

    @Test
    public void attackDiceWriteAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setAttackDice(1);
                assertTrue(s == CHOOSE_ATTACK_DICE || s == OCCUPY_TERRITORY);
            }catch(WrongMoveException e){
                assertFalse(s == CHOOSE_ATTACK_DICE || s == OCCUPY_TERRITORY);
            }
        }
    }

    @Test
    public void attackDiceReadAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                int attackDice = move.getAttackDice();
                assertTrue(s == CHOOSE_ATTACK_DICE || s == OCCUPY_TERRITORY);
            }catch(WrongMoveException e){
                assertFalse(s == CHOOSE_ATTACK_DICE || s == OCCUPY_TERRITORY);
            }
        }
    }

    @Test
    public void attackDiceInputsAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setReadOnlyInputs();
                move.setAttackDice(1);
                assertTrue(s == CHOOSE_ATTACK_DICE);
            }catch(WrongMoveException e){
                assertFalse(s == CHOOSE_ATTACK_DICE);
            }
        }
    }

    @Test
    public void defendDiceWriteAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setDefendDice(1);
                assertTrue(s == CHOOSE_DEFEND_DICE);
            }catch(WrongMoveException e){
                assertFalse(s == CHOOSE_DEFEND_DICE);
            }
        }
    }

    @Test
    public void defendDiceReadAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                int defendDice = move.getDefendDice();
                assertTrue(s == CHOOSE_DEFEND_DICE);
            }catch(WrongMoveException e){
                assertFalse(s == CHOOSE_DEFEND_DICE);
            }
        }
    }

    @Test
    public void defendDiceInputsAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setReadOnlyInputs();
                move.setDefendDice(1);
                assertTrue(s == CHOOSE_DEFEND_DICE);
            }catch(WrongMoveException e){
                assertFalse(s == CHOOSE_DEFEND_DICE);
            }
        }
    }

    @Test
    public void attackerLossesWriteAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setAttackerLosses(1);
                assertTrue(s == END_ATTACK);
            }catch(WrongMoveException e){
                assertFalse(s == END_ATTACK);
            }
        }
    }

    @Test
    public void attackerLossesReadAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                int attackerLosses = move.getAttackerLosses();
                assertTrue(s == END_ATTACK);
            }catch(WrongMoveException e){
                assertFalse(s == END_ATTACK);
            }
        }
    }

    @Test
    public void attackerLossesInputsAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setReadOnlyInputs();
                move.setAttackerLosses(1);
                fail();
            }catch(WrongMoveException e){
            }
        }
    }

    @Test
    public void defenderLossesWriteAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setDefenderLosses(1);
                assertTrue(s == END_ATTACK);
            }catch(WrongMoveException e){
                assertFalse(s == END_ATTACK);
            }
        }
    }

    @Test
    public void defenderLossesReadAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                int defenderLosses = move.getDefenderLosses();
                assertTrue(s == END_ATTACK);
            }catch(WrongMoveException e){
                assertFalse(s == END_ATTACK);
            }
        }
    }

    @Test
    public void defenderLossesInputsAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setReadOnlyInputs();
                move.setDefenderLosses(1);
                fail();
            }catch(WrongMoveException e){
            }
        }
    }

    @Test
    public void attackDiceRollsWriteAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setAttackDiceRolls(new ArrayList<Integer>());
                assertTrue(s == END_ATTACK);
            }catch(WrongMoveException e){
                assertFalse(s == END_ATTACK);
            }
        }
    }

    @Test
    public void attackDiceRollsReadAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                List<Integer> attackDiceRolls = move.getAttackDiceRolls();
                assertTrue(s == END_ATTACK);
            }catch(WrongMoveException e){
                assertFalse(s == END_ATTACK);
            }
        }
    }

    @Test
    public void attackDiceRollsInputsAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setReadOnlyInputs();
                move.setAttackDiceRolls(new ArrayList<Integer>());
                fail();
            }catch(WrongMoveException e){
            }
        }
    }

    @Test
    public void defendDiceRollsWriteAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setDefendDiceRolls(new ArrayList<Integer>());
                assertTrue(s == END_ATTACK);
            }catch(WrongMoveException e){
                assertFalse(s == END_ATTACK);
            }
        }
    }

    @Test
    public void defendDiceRollsReadAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                List<Integer> defendDiceRolls = move.getDefendDiceRolls();
                assertTrue(s == END_ATTACK);
            }catch(WrongMoveException e){
                assertFalse(s == END_ATTACK);
            }
        }
    }

    @Test
    public void defendDiceRollsInputsAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setReadOnlyInputs();
                move.setDefendDiceRolls(new ArrayList<Integer>());
                fail();
            }catch(WrongMoveException e){
            }
        }
    }

    @Test
    public void playerAccessWriteTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setPlayer(1);
                assertTrue(s == SETUP_BEGIN || s == PLAYER_ELIMINATED || s == GAME_END);
            }catch(WrongMoveException e){
                assertFalse(s == SETUP_BEGIN ||s == PLAYER_ELIMINATED || s == GAME_END);
            }
        }
    }

    @Test
    public void playerAccessReadTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                int player = move.getPlayer();
                assertTrue(s == SETUP_BEGIN || s == PLAYER_ELIMINATED || s == GAME_END);
            }catch(WrongMoveException e){
                assertFalse(s == SETUP_BEGIN || s == PLAYER_ELIMINATED || s == GAME_END);
            }
        }
    }

    @Test
    public void playerInputsAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setReadOnlyInputs();
                move.setPlayer(1);
                fail();
            }catch(WrongMoveException e){
            }
        }
    }

    @Test
    public void turnsAccessWriteTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setTurns(1);
                assertTrue(s == GAME_END);
            }catch(WrongMoveException e){
                assertFalse(s == GAME_END);
            }
        }
    }

    @Test
    public void turnsAccessReadTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                int turns = move.getTurns();
                assertTrue(s == GAME_END);
            }catch(WrongMoveException e){
                assertFalse(s == GAME_END);
            }
        }
    }

    @Test
    public void turnsInputsAccessTest(){
        for(Stage s : Stage.values()){
            Move move = new Move(0, s);
            try{
                move.setReadOnlyInputs();
                move.setTurns(1);
                fail();
            }catch(WrongMoveException e){
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
    public void extraArmiesWriteTest() throws WrongMoveException{
        Move move = new Move(0, PLACE_ARMIES);
        move.setReadOnly();
        exception.expect(WrongMoveException.class);
        move.setExtraArmies(1);
    }

    @Test
    public void matchesWriteTest() throws WrongMoveException{
        Move move = new Move(0, PLACE_ARMIES);
        move.setReadOnly();
        exception.expect(WrongMoveException.class);
        move.setMatches(new ArrayList<Integer>());
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
