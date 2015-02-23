package ai;

import logic.*;
import player.*;

import java.util.*;
import java.io.*;

/**
 * DumbAI --- Randomly decides what to do, will drag out games forever if there are no smarter players in the game.
 */
public class DumbAI implements PlayerController {
    private static Random random = new Random();

    private int uid;
    private ArrayList<Card> hand;
    private Board board;

    public DumbAI(){
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
            System.out.println("DumbAI is not choosing a move correctly");
            System.out.println(e.getMessage());
            return new Move(-1);
        }
    }

    private Move claimTerritory(Move move) throws WrongMoveException{
        int tid = random.nextInt(board.getTerritories().size());
        while(!(board.checkTerritoryOwner(-1, tid))){
            tid = random.nextInt(board.getTerritories().size());
        }

        move.setTerritoryToClaim(tid);
        return move;
    }

    private Move reinforceTerritory(Move move) throws WrongMoveException{
        int tid = random.nextInt(board.getTerritories().size());
        while(!(board.checkTerritoryOwner(uid, tid))){
            tid = random.nextInt(board.getTerritories().size());
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

        int randomTerritory = random.nextInt(board.getTerritories().size());
        while(!board.checkTerritoryOwner(uid, randomTerritory)){
            randomTerritory = random.nextInt(board.getTerritories().size());
        }
        int randomArmies = random.nextInt(armiesToPlace+1); // Can't place 0 armies

        move.setPlaceArmiesTerritory(randomTerritory);
        move.setPlaceArmiesNum(randomArmies);
        return move;
    }

    private Move decideAttack(Move move) throws WrongMoveException{
        move.setDecideAttack(random.nextBoolean());
        return move;
    }

    private Move startAttack(Move move) throws WrongMoveException{
        int randomAlly = random.nextInt(board.getTerritories().size());
        while(!board.checkTerritoryOwner(uid, randomAlly) || board.getTerritories().get(randomAlly).getArmies() < 2){
            randomAlly = random.nextInt(board.getTerritories().size());
        }
        ArrayList<Integer> adjacents = board.getTerritories().get(randomAlly).getLinks();
        int randomEnemy = adjacents.get(random.nextInt(adjacents.size()));

        move.setAttackFrom(randomAlly);
        move.setAttackTo(randomEnemy);
        return move;
    }

    private Move chooseAttackingDice(Move move) throws WrongMoveException{
        move.setAttackingDice(random.nextInt(3)+1);
        return move;
    }

    private Move chooseDefendingDice(Move move) throws WrongMoveException{
        move.setDefendingDice(random.nextInt(2)+1);
        return move;
    }

    private Move occupyTerritory(Move move) throws WrongMoveException{
        int currentArmies = move.getOccupyCurrentArmies();
        move.setOccupyArmies(random.nextInt(currentArmies));
        return move;
    }

    private Move decideFortify(Move move) throws WrongMoveException{
        move.setDecideFortify(random.nextBoolean());
        return move;
    }

    private Move startFortify(Move move) throws WrongMoveException{
        int randomAlly = 0;
        ArrayList<Integer> adjacents = new ArrayList<Integer>();
        randomAlly = random.nextInt(board.getTerritories().size());
        while(!board.checkTerritoryOwner(uid, randomAlly)){
            randomAlly = random.nextInt(board.getTerritories().size());
        }
        adjacents = board.getTerritories().get(randomAlly).getLinks();
        int randomFortify = adjacents.get(random.nextInt(adjacents.size()));

        move.setFortifyFrom(randomAlly);
        move.setFortifyTo(randomFortify);
        return move;
    }

    private Move chooseFortifyArmies(Move move) throws WrongMoveException{
        move.setFortifyArmies(random.nextInt(move.getFortifyCurrentArmies()));
        return move;
    }
}

