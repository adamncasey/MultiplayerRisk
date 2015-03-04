package logic;

import java.util.List;
import logic.Move.Stage;

public class MoveChecker {
    private GameState state;
    private Board board;

    public MoveChecker(GameState state){
        this.state = state;
        this.board = state.getBoard();
    }

    public boolean checkMove(Move move) throws WrongMoveException{
        if(move == null){
            return false;
        }
        int currentPlayer = move.getUID();
        switch(move.getStage()){
            case CLAIM_TERRITORY:
                int territoryToClaim = move.getTerritory();
                return checkClaimTerritory(territoryToClaim);
            case REINFORCE_TERRITORY:
                int territoryToReinforce = move.getTerritory();
                return checkReinforceTerritory(currentPlayer, territoryToReinforce);
            case TRADE_IN_CARDS:
                List<Card> hand = state.getPlayer(currentPlayer).getHand(); 
                List<Card> toTradeIn = move.getToTradeIn();
                return checkTradeInCards(hand, toTradeIn);
            case PLACE_ARMIES:
                int placeArmiesTerritory = move.getTerritory();
                int placeArmiesNum = move.getArmies();
                int armiesToPlace = move.getCurrentArmies();
                return checkPlaceArmies(currentPlayer, placeArmiesTerritory, placeArmiesNum, armiesToPlace);
            case DECIDE_ATTACK:
                return true;
            case START_ATTACK:
                int attackFrom = move.getFrom();
                int attackTo = move.getTo();
                return checkStartAttack(currentPlayer, attackFrom, attackTo);
            case CHOOSE_ATTACK_DICE:
                int attackingDice = move.getAttackDice();
                int attackingNumArmies = board.getArmies(move.getFrom());
                return checkAttackingDice(attackingDice, attackingNumArmies);
            case CHOOSE_DEFEND_DICE:
                int defendingDice = move.getDefendDice();
                int defendingNumArmies = board.getArmies(move.getTo());
                return checkDefendingDice(defendingDice, defendingNumArmies);
            case OCCUPY_TERRITORY:
                int occupyArmies = move.getArmies();
                int occupyDice = move.getAttackDice();
                int occupyCurrentArmies = move.getCurrentArmies();
                return checkOccupyArmies(occupyArmies, occupyDice, occupyCurrentArmies);
            case DECIDE_FORTIFY:
                return true;
            case START_FORTIFY:
                int fortifyFrom = move.getFrom();
                int fortifyTo = move.getTo();
                return checkStartFortify(currentPlayer, fortifyFrom, fortifyTo);
            case FORTIFY_TERRITORY:
                int fortifyArmies = move.getArmies();
                int fortifyCurrentArmies = move.getCurrentArmies();
                return checkFortifyArmies(fortifyArmies, fortifyCurrentArmies);
            default:
                return false;
        }
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

    public static boolean checkTradeInCards(List<Card> hand, List<Card> toTradeIn){
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
