package ai;

import java.util.*;
import logic.*;
import player.*;

/**
 * RandomPlayer --- Randomly makes moves until one is valid.
 */
public class RandomPlayer implements IPlayer {
    private static Random random = new Random();

    public RandomPlayer(){
    }

    private int uid = 0; // Set and used by Game
    public int getUID(){
        return this.uid;
    }
    public void setUID(int uid){
        this.uid = uid;

    }
    private boolean eliminated = false; // Set and used by Game
    public boolean isEliminated(){
        return eliminated;
    }
    public void eliminate(){
        eliminated = true;
    }

    private Board board;
    private ArrayList<Card> hand;
    public void updatePlayer(Board board, ArrayList<Card> hand, int currentPlayer, String currentStage){
        this.board = board;
        this.hand = hand;
    }

    public int claimTerritory(String requestMessage){
        int tid = random.nextInt(board.getTerritories().size());
        while(!(board.checkTerritoryOwner(-1, tid))){
            tid = random.nextInt(board.getTerritories().size());
        }
        return tid;
    }

    public int reinforceTerritory(String requestMessage, int uid){
        int tid = random.nextInt(board.getTerritories().size());
        while(!(board.checkTerritoryOwner(uid, tid))){
            tid = random.nextInt(board.getTerritories().size());
        }
        return tid;
    }


    public ArrayList<Card> tradeInCards(String requestMessage){ 
        ArrayList<Card> toTradeIn = new ArrayList<Card>();
        int handSize = hand.size();
        if(handSize >= 5){ // Doesn't guarantee that a set will be chosen, Game can do that.
            for(int i = 0; i != 3; ++i){
                int randomCard = random.nextInt(hand.size());
                Card c = hand.get(randomCard);
                hand.remove(randomCard);
                toTradeIn.add(c);
            } 
        }
        return toTradeIn;
    }

    public ArrayList<Integer> placeArmies(String requestMessage, int armiesToPlace){
        ArrayList<Integer> move = new ArrayList<Integer>();
        int randomTerritory = random.nextInt(board.getTerritories().size());
        while(!board.checkTerritoryOwner(uid, randomTerritory)){
            randomTerritory = random.nextInt(board.getTerritories().size());
        }
        move.add(randomTerritory);
        int randomArmies = random.nextInt(armiesToPlace+1); // Can't place 0 armies
        move.add(randomArmies);
        return move;
    }

    public boolean decideAttack(String requestMessage){
        return true;
    }

    public ArrayList<Integer> startAttack(String requestMessage){
        ArrayList<Integer> move = new ArrayList<Integer>();
        int randomAlly = random.nextInt(board.getTerritories().size());
        while(!board.checkTerritoryOwner(uid, randomAlly) || board.getTerritories().get(randomAlly).getArmies() < 2){
            randomAlly = random.nextInt(board.getTerritories().size());
        }
        ArrayList<Integer> adjacents = board.getTerritories().get(randomAlly).getLinks();
        int randomEnemy = adjacents.get(random.nextInt(adjacents.size())); // Doesn't guarantee that an enemy is chosen, Game can do that // Adjacents might not even have an enemy in it
        move.add(randomAlly);
        move.add(randomEnemy);
        return move;
    }

    public int chooseAttackingDice(String requestMessage, int numArmies){
        if(numArmies > 4){
            return 3;
        }else if(numArmies > 3){
            return 2;
        }
        return 1;
    }

    public int chooseDefendingDice(String requestMessage, int numArmies){
        if(numArmies > 1){
            return 2;
        }
        return 1;
    }

    public ArrayList<Integer> rollDice(String requestMessage, int numDice){
        ArrayList<Integer> roll = new ArrayList<Integer>();
        for(int i = 0; i != numDice; ++i){
            roll.add(random.nextInt(6)+1);
        }
        return roll;
    }

    public int occupyTerritory(String requestMessage, int currentArmies, int numDice){
        int decision = -1;
        while(decision < numDice){
            decision = currentArmies-1;
        }
        return decision;
    }

    // This AI only fortifies when one of it's territories has no adjacent enemies
    public boolean decideFortify(String requestMessage){
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
                return true;
            }
        }
        return false;
    }

    public ArrayList<Integer> startFortify(String requestMessage){
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

        ArrayList<Integer> move = new ArrayList<Integer>();
        move.add(randomAlly);
        move.add(randomFortify);
        return move;
    }

    public int chooseFortifyArmies(String requestMessage, int currentArmies){
        return currentArmies-1;
    }
}

