package logic;

import java.util.*;

public class MoveChecker {

    private Board board;

    public MoveChecker(Board board){
        this.board = board;
    }

    public void update(Board board){
        this.board = board;
    }

    public boolean checkClaimTerritory(int tid){
        if(board.getOwner(tid) != -1){
            return false;
        }
        return true;
    }

    public boolean checkReinforceTerritory(int uid, int tid){
        if(board.getOwner(tid) != uid){
            return false;
        }
        return true;
    }

    public static boolean checkTradeInCards(ArrayList<Card> hand, ArrayList<Card> toTradeIn){
        if(toTradeIn.size() == 0 && hand.size() < 5){
            return true;
        }
        if(Card.containsSet(hand) && Card.containsSet(toTradeIn)){
            if(toTradeIn.size() == 3 && Card.isSubset(toTradeIn, hand)){
                return true;
            }
        }
        return false;
    }

    public boolean checkPlaceArmies(int uid, int territory, int armies, int armiesToPlace){
        if(armies < 1){
            return false;
        }
        if(armies > armiesToPlace){
            return false;
        }
        if(board.getOwner(territory) != uid){
            return false;
        }
        return true;
    }

    public boolean checkStartAttack(int uid, int attackFrom, int attackTo){
        // Does this player own the territory to be attacked from?
        if(board.getOwner(attackFrom) != uid){
            return false;
        }

        // Does ally have at least 2 armies?
        if(board.getArmies(attackFrom) < 2){
            return false;
        }

        // Does this player not own the territory to be attacked?
        if(board.getOwner(attackTo) == uid){
            return false;
        }
        
        // Are the two territories adjacent?
        boolean found = false;
        for(Integer i : board.getLinks(attackFrom)){
            if(i == attackTo){
                found = true;
            }
        }
        if(!found){
            return false;
        }

        return true;
    }

    public boolean checkAttackingDice(int numDice, int numArmies){
        if(numArmies <= numDice){ // You must have one more armies than the number of dice you wish to roll.
            return false;
        }
        return true;
    }

    public boolean checkDefendingDice(int numDice, int numArmies){ 
        if(numArmies < numDice){ // You must have 2 armies to roll 2 dice.
            return false;
        }
        return true;
    }

    public boolean checkOccupyArmies(int armies, int numDice, int currentArmies){
        if(armies < numDice){
            return false;
        }
        if((currentArmies - armies) < 1){
            return false;
        }
        return true;
    }

    public boolean checkStartFortify(int uid, int fortifyFrom, int fortifyTo){
        // Does this player own the territory to be attacked from?
        if(board.getOwner(fortifyFrom) != uid){
            return false;
        }

        // Does this territory have at least 2 armies?
        if(board.getArmies(fortifyFrom) < 2){
            return false;
        }

        // Does this player own the territory to be fortified?
        if(board.getOwner(fortifyTo) != uid){
            return false;
        }
        
        // Are the two territories adjacent?
        boolean found = false;
        for(Integer i : board.getLinks(fortifyFrom)){
            if(i == fortifyTo){
                found = true;
            }
        }
        if(!found){
            return false;
        }
        return true;        
    }

    public boolean checkFortifyArmies(int fortifyArmies, int fortifyCurrentArmies){
        if((fortifyCurrentArmies - fortifyArmies) < 1){
            return false;
        }
        if(fortifyArmies < 1){
            return false;
        }
        return true;
    }
}
