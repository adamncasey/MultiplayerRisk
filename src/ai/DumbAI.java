package ai;

import logic.*;
import logic.Move.Stage;
import player.*;

import java.util.*;
import java.io.*;

/**
 * DumbAI --- Randomly decides what to do, will drag out games forever if there are no smarter players in the game.
 */
public class DumbAI implements PlayerController {
    private static Random random = new Random();

    private List<Card> hand;
    private Board board;

    public DumbAI(){
    }

    public void updateAI(List<Card> hand, Board board, int currentPlayer, Move previousMove){
        this.hand = new ArrayList<Card>(hand);
        this.board = board;
    }

    public Move getMove(Move move){
        try{
            switch(move.getStage()){
                case CLAIM_TERRITORY:
                    return claimTerritory(move);
                case REINFORCE_TERRITORY:
                    return reinforceTerritory(move);
                case TRADE_IN_CARDS:
                    return tradeInCards(move);
                case PLACE_ARMIES:
                    return placeArmies(move);
                case DECIDE_ATTACK:
                    return decideAttack(move);
                case START_ATTACK:
                    return startAttack(move);
                case CHOOSE_ATTACK_DICE:
                    return chooseAttackingDice(move);
                case CHOOSE_DEFEND_DICE:
                    return chooseDefendingDice(move);
                case OCCUPY_TERRITORY:
                    return occupyTerritory(move);
                case DECIDE_FORTIFY:
                    return decideFortify(move);
                case START_FORTIFY:
                    return startFortify(move);
                case FORTIFY_TERRITORY:
                    return chooseFortifyArmies(move);
                default:
                     return move;
            }
        }catch(WrongMoveException e){
            System.out.println("DumbAI is not choosing a move correctly");
            System.out.println(e.getMessage());
            return null;
        }
    }

    private Move claimTerritory(Move move) throws WrongMoveException{
        int tid = random.nextInt(board.getNumTerritories());
        while(board.getOwner(tid) != -1){
            tid = random.nextInt(board.getNumTerritories());
        }

        move.setTerritory(tid);
        return move;
    }

    private Move reinforceTerritory(Move move) throws WrongMoveException{
        int uid = move.getUID();
        int tid = random.nextInt(board.getNumTerritories());
        while(board.getOwner(tid) != uid){
            tid = random.nextInt(board.getNumTerritories());
        }

        move.setTerritory(tid);
        return move;
    }

    private Move tradeInCards(Move move) throws WrongMoveException{ 
        List<Card> toTradeIn = new ArrayList<Card>();
        if(hand.size() >= 5){
            for(int i = 0; i != 3; ++i){
                int randomCard = random.nextInt(hand.size());
                Card c = hand.get(randomCard);
                toTradeIn.add(c);
            } 
        }

        move.setToTradeIn(toTradeIn);
        return move;
    }

    private Move placeArmies(Move move) throws WrongMoveException{
        int uid = move.getUID();
        int armiesToPlace = move.getCurrentArmies();

        int randomTerritory = random.nextInt(board.getNumTerritories());
        while(board.getOwner(randomTerritory) != uid){
            randomTerritory = random.nextInt(board.getNumTerritories());
        }
        int randomArmies = random.nextInt(armiesToPlace+1); // Can't place 0 armies

        move.setTerritory(randomTerritory);
        move.setArmies(randomArmies);
        return move;
    }

    private Move decideAttack(Move move) throws WrongMoveException{
        move.setDecision(random.nextBoolean());
        return move;
    }

    private Move startAttack(Move move) throws WrongMoveException{
        int uid = move.getUID();
        int randomAlly = random.nextInt(board.getNumTerritories());
        while((board.getOwner(randomAlly) != uid) || board.getArmies(randomAlly) < 2){
            randomAlly = random.nextInt(board.getNumTerritories());
        }
        List<Integer> adjacents = board.getLinks(randomAlly);
        int randomEnemy = adjacents.get(random.nextInt(adjacents.size()));

        move.setFrom(randomAlly);
        move.setTo(randomEnemy);
        return move;
    }

    private Move chooseAttackingDice(Move move) throws WrongMoveException{
        move.setAttackDice(random.nextInt(3)+1);
        return move;
    }

    private Move chooseDefendingDice(Move move) throws WrongMoveException{
        move.setDefendDice(random.nextInt(2)+1);
        return move;
    }

    private Move occupyTerritory(Move move) throws WrongMoveException{
        int currentArmies = move.getCurrentArmies();
        move.setArmies(random.nextInt(currentArmies));
        return move;
    }

    private Move decideFortify(Move move) throws WrongMoveException{
        move.setDecision(random.nextBoolean());
        return move;
    }

    private Move startFortify(Move move) throws WrongMoveException{
        int uid = move.getUID();
        int randomAlly = 0;
        randomAlly = random.nextInt(board.getNumTerritories());
        while(board.getOwner(randomAlly) != uid){
            randomAlly = random.nextInt(board.getNumTerritories());
        }
        List<Integer> adjacents = board.getLinks(randomAlly);
        int randomFortify = adjacents.get(random.nextInt(adjacents.size()));

        move.setFrom(randomAlly);
        move.setTo(randomFortify);
        return move;
    }

    private Move chooseFortifyArmies(Move move) throws WrongMoveException{
        move.setArmies(random.nextInt(move.getCurrentArmies()));
        return move;
    }
}

