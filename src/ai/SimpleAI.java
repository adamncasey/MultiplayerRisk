package ai;

import logic.*;
import logic.Move.Stage;
import player.*;

import java.util.*;
import java.io.*;

/**
 * SimpleAI --- Takes moves with the aim of ending the game quickly.
 */
public class SimpleAI implements PlayerController {
    private static Random random = new Random();

    private int uid;
    private ArrayList<Card> hand;
    private Board board;

    public SimpleAI(){
    }

    public void setUID(int uid){
        this.uid = uid;
    }

    public void updateAI(ArrayList<Card> hand, Board board, int currentPlayer, Move previousMove){
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
            System.out.println("SimpleAI is not choosing a move correctly");
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
        int tid = random.nextInt(board.getNumTerritories());
        while(board.getOwner(tid) != uid){
            tid = random.nextInt(board.getNumTerritories());
        }

        move.setTerritory(tid);
        return move;
    }

    private Move tradeInCards(Move move) throws WrongMoveException{ 
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
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
        move.setDecision(true);
        return move;
    }

    private Move startAttack(Move move) throws WrongMoveException{
        int randomAlly = random.nextInt(board.getNumTerritories());
        while((board.getOwner(randomAlly) != uid) || board.getArmies(randomAlly) < 2){
            randomAlly = random.nextInt(board.getNumTerritories());
        }
        ArrayList<Integer> adjacents = board.getLinks(randomAlly);
        int randomEnemy = adjacents.get(random.nextInt(adjacents.size()));

        move.setFrom(randomAlly);
        move.setTo(randomEnemy);
        return move;
    }

    private Move chooseAttackingDice(Move move) throws WrongMoveException{
        int numArmies = board.getArmies(move.getFrom());
        int decision = 1;
        if(numArmies > 4){
            decision = 3;
        }else if(numArmies > 3){
            decision = 2;
        }
        move.setAttackDice(decision);
        return move;
    }

    private Move chooseDefendingDice(Move move) throws WrongMoveException{
        int numArmies = board.getArmies(move.getTo());
        int decision = 1;
        if(numArmies > 1){
            decision = 2;
        }
        move.setDefendDice(decision);
        return move;
    }

    private Move occupyTerritory(Move move) throws WrongMoveException{
        int currentArmies = move.getCurrentArmies();
        int numDice = move.getAttackDice();
        int decision = currentArmies-1;
        move.setArmies(decision);
        return move;
    }

    // This AI only fortifies when one of it's territories has no adjacent enemies
    private Move decideFortify(Move move) throws WrongMoveException{
        for(int i = 0; i != board.getNumTerritories(); ++i){
            if(board.getOwner(i) != uid || board.getArmies(uid) < 2){
                continue;
            }
            ArrayList<Integer> adjacents = board.getLinks(i);
            int enemyCounter = 0;
            for(int j : adjacents){
                if(board.getOwner(j) != uid){
                    enemyCounter++;
                }
            }
            if(enemyCounter == 0){
                move.setDecision(true);
                return move;
            }
        }
        move.setDecision(false);
        return move;
    }

    private Move startFortify(Move move) throws WrongMoveException{
        int randomAlly = 0;;
        int enemyCounter = -1;
        ArrayList<Integer> adjacents = new ArrayList<Integer>();
        while(enemyCounter != 0){
            randomAlly = random.nextInt(board.getNumTerritories());
            while(board.getOwner(randomAlly) != uid){
                randomAlly = random.nextInt(board.getNumTerritories());
            }
            adjacents = board.getLinks(randomAlly);
            enemyCounter = 0;
            for(int i : adjacents){
                if(board.getOwner(i) != uid){
                    enemyCounter++;
                }
            }
        }
        int randomFortify = adjacents.get(random.nextInt(adjacents.size()));
        while(board.getOwner(randomFortify) != uid){
            randomFortify = adjacents.get(random.nextInt(adjacents.size()));
        }

        move.setFrom(randomAlly);
        move.setTo(randomFortify);
        return move;
    }

    private Move chooseFortifyArmies(Move move) throws WrongMoveException{
        move.setArmies(move.getCurrentArmies()-1);
        return move;
    }
}
