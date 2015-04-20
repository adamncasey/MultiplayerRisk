package ai.strategy;

import java.util.List;
import java.util.Random;
import logic.move.Move;
import logic.state.Board;
import logic.state.Player;

/**
 * ControlledStrategy
 */
public class ControlledStrategy extends Strategy {

    public ControlledStrategy(Player player, Board board, Random random){
        super(player, board, random);
    }

    public void getMove(Move move){
        switch(move.getStage()){
            case PLACE_ARMIES:
                placeArmies(move);
                return;
            default:
                assert false : move.getStage();
        }
    }

    private void placeArmies(Move move){
        if(move.getExtraArmies() > 0){
            List<Integer> matches = move.getMatches();
            move.setTerritory(matches.get(random.nextInt(matches.size())));
            move.setArmies(1);
            return;
        }
        int bestToReinforce= -1;
        int lowestArmies = Integer.MAX_VALUE;
        for(int i = 0; i != board.getNumTerritories(); ++i){
            if(board.getOwner(i) != move.getUID()){
                 continue;
            }
            int enemyCounter = 0;
            for(int j : board.getLinks(i)){
                if(board.getOwner(j) != move.getUID()){
                    enemyCounter++;
                }
            }
            if(enemyCounter > 0){
                if(board.getArmies(i) < lowestArmies){
                    bestToReinforce = i;
                    lowestArmies = board.getArmies(i);
                }
            }
        }
        move.setTerritory(bestToReinforce);
        move.setArmies(1);
    }
}
