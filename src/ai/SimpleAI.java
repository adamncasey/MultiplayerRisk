package ai;

import logic.*;
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
                case 0:
                    return claimTerritory(move);
                case 1:
                    return reinforceTerritory(move);
                case 2:
                    return tradeInCards(move);
                case 3:
                    return placeArmies(move);
                case 4:
                    return decideAttack(move);
                case 5:
                    return startAttack(move);
                case 6:
                    return chooseAttackingDice(move);
                case 7:
                    return chooseDefendingDice(move);
                case 8:
                    return occupyTerritory(move);
                case 9:
                    return decideFortify(move);
                case 10:
                    return startFortify(move);
                case 11:
                    return chooseFortifyArmies(move);
                default:
                     return move;
            }
        }catch(WrongMoveException e){
            System.out.println("SimpleAI is not choosing a move correctly");
            return new Move(-1);
        }
    }

    private Move claimTerritory(Move move) throws WrongMoveException{
        int tid = random.nextInt(board.getNumTerritories());
        while(board.getOwner(tid) != -1){
            tid = random.nextInt(board.getNumTerritories());
        }

        move.setTerritoryToClaim(tid);
        return move;
    }

    private Move reinforceTerritory(Move move) throws WrongMoveException{
        int tid = random.nextInt(board.getNumTerritories());
        while(board.getOwner(tid) != uid){
            tid = random.nextInt(board.getNumTerritories());
        }

        move.setTerritoryToReinforce(tid);
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
        int armiesToPlace = move.getArmiesToPlace();

        int randomTerritory = random.nextInt(board.getNumTerritories());
        while(board.getOwner(randomTerritory) != uid){
            randomTerritory = random.nextInt(board.getNumTerritories());
        }
        int randomArmies = random.nextInt(armiesToPlace+1); // Can't place 0 armies

        move.setPlaceArmiesTerritory(randomTerritory);
        move.setPlaceArmiesNum(randomArmies);
        return move;
    }

    private Move decideAttack(Move move) throws WrongMoveException{
        move.setDecideAttack(true);
        return move;
    }

    private Move startAttack(Move move) throws WrongMoveException{
        int randomAlly = random.nextInt(board.getNumTerritories());
        while((board.getOwner(randomAlly) != uid) || board.getArmies(randomAlly) < 2){
            randomAlly = random.nextInt(board.getNumTerritories());
        }
        ArrayList<Integer> adjacents = board.getLinks(randomAlly);
        int randomEnemy = adjacents.get(random.nextInt(adjacents.size()));

        move.setAttackFrom(randomAlly);
        move.setAttackTo(randomEnemy);
        return move;
    }

    private Move chooseAttackingDice(Move move) throws WrongMoveException{
        int numArmies = board.getArmies(move.getAttackingFrom());
        if(numArmies > 4){
            move.setAttackingDice(3);
        }else if(numArmies > 3){
            move.setAttackingDice(2);
        }else{
            move.setAttackingDice(1);
        }
        return move;
    }

    private Move chooseDefendingDice(Move move) throws WrongMoveException{
        int numArmies = board.getArmies(move.getDefendingFrom());
        if(numArmies > 1){
            move.setDefendingDice(2);
        }else{
            move.setDefendingDice(1);
        }
        return move;
    }

    private Move occupyTerritory(Move move) throws WrongMoveException{
        int currentArmies = move.getOccupyCurrentArmies();
        int numDice = move.getOccupyDice();
        int decision = currentArmies-1;
        move.setOccupyArmies(decision);
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
                move.setDecideFortify(true);
                return move;
            }
        }
        move.setDecideFortify(false);
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

        move.setFortifyFrom(randomAlly);
        move.setFortifyTo(randomFortify);
        return move;
    }

    private Move chooseFortifyArmies(Move move) throws WrongMoveException{
        move.setFortifyArmies(move.getFortifyCurrentArmies()-1);
        return move;
    }
}
