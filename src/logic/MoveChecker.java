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
        Territory territory = board.getTerritories().get(tid);
        if(territory.getOwner() != -1){
            return false;
        }
        return true;
    }

    public boolean checkReinforceTerritory(int uid, int tid){
        Territory territory = board.getTerritories().get(tid);
        if(territory.getOwner() != uid){
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
        if(!board.checkTerritoryOwner(uid, territory)){
            return false;
        }
        return true;
    }

    public boolean checkStartAttack(int uid, int attackFrom, int attackTo){
        Territory ally = board.getTerritories().get(attackFrom);
        Territory enemy = board.getTerritories().get(attackTo);

        // Does this player own the territory to be attacked from?
        if(ally.getOwner() != uid){
            return false;
        }

        // Does ally have at least 2 armies?
        if(ally.getArmies() < 2){
            return false;
        }

        // Does this player not own the territory to be attacked?
        if(enemy.getOwner() == uid){
            return false;
        }
        
        // Are the two territories adjacent?
        boolean found = false;
        for(Integer i : ally.getLinks()){
            if(i == enemy.getID()){
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
        Territory ally = board.getTerritories().get(fortifyFrom);
        Territory fortify = board.getTerritories().get(fortifyTo);

        // Does this player own the territory to be attacked from?
        if(ally.getOwner() != uid){
            return false;
        }

        // Does this territory have at least 2 armies?
        if(ally.getArmies() < 2){
            return false;
        }

        // Does this player own the territory to be fortified?
        if(fortify.getOwner() != uid){
            return false;
        }
        
        // Are the two territories adjacent?
        boolean found = false;
        for(Integer i : ally.getLinks()){
            if(i == fortify.getID()){
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
