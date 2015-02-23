package player;

import logic.*;
import player.*;

import java.util.*;
import java.io.*;

/**
 * CommandLineController --- Allows the user to control a player from the command line.
 */
public class CommandLineController implements PlayerController {
    private static Random random = new Random();

    private int uid;
    private ArrayList<Card> hand;
    private Board board;

    private Scanner reader;
    private PrintWriter writer;

    public CommandLineController(Scanner reader, PrintWriter writer){
        this.reader = reader;
        this.writer = writer;
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
            System.out.println("CommandLineController is not choosing a move correctly");
            return new Move(-1);
        }
    }

    private Move claimTerritory(Move move) throws WrongMoveException{
        writer.print("Which territory do you want to claim?\n> ");
        int territory = -1; boolean correct = false;
        while(!correct){
            writer.flush();
            while(!reader.hasNextInt()){
                writer.print("Invalid Input\n> ");
                writer.flush();
                reader.next();
            }
            territory = reader.nextInt();
            if(territory >= 0 && territory < board.getTerritories().size()){
                if(board.getTerritories().get(territory).getOwner() == -1){
                    correct = true;
                }else{
                    writer.print("That territory has already been claimed.\n> ");
                }
            }else{
                writer.print("That territory doesn't exist.\n> ");
            }
        }

        move.setTerritoryToClaim(territory);
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
        move.setDecideAttack(true);
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
        int numArmies = move.getAttackingNumArmies();
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
        int numArmies = move.getDefendingNumArmies();
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
        for(Territory t : board.getTerritories().values()){
            if(t.getOwner() != uid || t.getArmies() < 2){
                continue;
            }
            ArrayList<Integer> adjacents = t.getLinks();
            int enemyCounter = 0;
            for(int i : adjacents){
                if(!board.checkTerritoryOwner(uid, i)){
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
        int randomAlly = 0;
        ArrayList<Integer> adjacents = new ArrayList<Integer>();
        int enemyCounter = -1;
        while(enemyCounter != 0){
            randomAlly = random.nextInt(board.getTerritories().size());
            while(!board.checkTerritoryOwner(uid, randomAlly)){
                randomAlly = random.nextInt(board.getTerritories().size());
            }
            adjacents = board.getTerritories().get(randomAlly).getLinks();
            enemyCounter = 0;
            for(int i : adjacents){
                if(!board.checkTerritoryOwner(uid, i)){
                    enemyCounter++;
                }
            }
        }
        int randomFortify = adjacents.get(random.nextInt(adjacents.size()));
        while(!board.checkTerritoryOwner(uid, randomFortify)){
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

