package ai.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import logic.move.Move;
import logic.state.Board;
import logic.state.Player;

/**
 * AggressiveStrategy --- Always attack, fortify to the front lines.
 */
public class AggressiveStrategy extends Strategy {

    public AggressiveStrategy(Player player, Board board, Random random){
        super(player, board, random);
    }

    public void getMove(Move move){
        switch(move.getStage()){
            case DECIDE_ATTACK:
                decideAttack(move);
                return;
            case CHOOSE_ATTACK_DICE:
                chooseAttackingDice(move);
                return;
            case CHOOSE_DEFEND_DICE:
                chooseDefendingDice(move);
                return;
            case OCCUPY_TERRITORY:
                pickArmies(move);
                return;
            case DECIDE_FORTIFY:
                decideFortify(move);
                return;
            case START_FORTIFY:
                startFortify(move);
                return;
            case FORTIFY_TERRITORY:
                pickArmies(move);
                return;
            default:
                assert false : move.getStage();
        }
    }

    // Always attack
    private void decideAttack(Move move){
        move.setDecision(true);
    }

    // Choose the maximum number of armies
    private void chooseAttackingDice(Move move){
        int numArmies = board.getArmies(move.getFrom());
        int decision = (numArmies > 4) ? 3 : ((numArmies > 3) ? 2 : 1);
        move.setAttackDice(decision);
    }

    // Defend with the maximum number of armies
    private void chooseDefendingDice(Move move){
        int numArmies = board.getArmies(move.getTo());
        int decision = (numArmies > 1) ? 2 : 1;
        move.setDefendDice(decision);
    }

    // Move the maximum number of armies forward
    private void pickArmies(Move move){
        move.setArmies(move.getCurrentArmies()-1);
    }

    // Fortifies when one of it's territories has no adjacent enemies
    private void decideFortify(Move move){
        int uid = move.getUID();
        for(int i = 0; i != board.getNumTerritories(); ++i){
            if(board.getOwner(i) != uid || board.getArmies(uid) < 2){
                continue;
            }
            List<Integer> adjacents = board.getLinks(i);
            int enemyCounter = 0;
            for(int j : adjacents){
                if(board.getOwner(j) != uid){
                    enemyCounter++;
                }
            }
            if(enemyCounter == 0){
                move.setDecision(true);
                return;
            }
        }
        move.setDecision(false);
    }

    // Fortify to the front lines
    private void startFortify(Move move){
        int uid = move.getUID();
        int randomAlly = 0;;
        int enemyCounter = -1;
        List<Integer> adjacents = new ArrayList<Integer>();
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
    }
}
