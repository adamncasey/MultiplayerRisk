package ai.strategy;

import java.util.List;
import java.util.Random;
import logic.move.Move;
import logic.state.Board;
import logic.state.Player;

/**
 * RandomStrategy
 */
public class RandomStrategy extends Strategy {

    public RandomStrategy(Player player, Board board, Random random){
        super(player, board, random);
    }

    public void getMove(Move move){
        switch(move.getStage()){
            case CLAIM_TERRITORY:
                pickTerritory(move);
                return;
            case REINFORCE_TERRITORY:
                pickTerritory(move);
                return;
            case PLACE_ARMIES:
                placeArmies(move);
                return;
            case DECIDE_ATTACK:
                decide(move);
                return;
            case START_ATTACK:
                pickPair(move);
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
                decide(move);
                return;
            case START_FORTIFY:
                pickPair(move);
                return;
            case FORTIFY_TERRITORY:
                pickArmies(move);
                return;
            default:
                assert false : move.getStage();
        }
    }

    private void pickTerritory(Move move){
        move.setTerritory(random.nextInt(board.getNumTerritories()));
    }

    private void placeArmies(Move move){
        move.setTerritory(random.nextInt(board.getNumTerritories()));
        move.setArmies(1);
    }

    private void decide(Move move){
        move.setDecision(random.nextBoolean());
    }

    private void pickPair(Move move){
        int from = random.nextInt(board.getNumTerritories());
        List<Integer> adjacents = board.getLinks(from);
        int to = adjacents.get(random.nextInt(adjacents.size()));
        move.setFrom(from);
        move.setTo(to);
    }

    private void pickArmies(Move move){
        move.setArmies(random.nextInt(move.getCurrentArmies()));
    }

    private void chooseAttackingDice(Move move){
        move.setAttackDice(random.nextInt(3)+1);
    }

    private void chooseDefendingDice(Move move){
        move.setDefendDice(random.nextInt(2)+1);
    }
}
