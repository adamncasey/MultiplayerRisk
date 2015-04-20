package ai.strategy;

import java.util.List;
import java.util.Random;
import logic.move.Move;
import logic.state.Board;
import logic.state.Player;

/**
 * CaptureContinentsStrategy
 */
public class CaptureContinentsStrategy extends Strategy {

    public CaptureContinentsStrategy(Player player, Board board, Random random){
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
            move.setArmies(2);
            return;
        }
        int bestContinent = -1;
        double bestScore = -1;
        for(int i = 0; i != board.getNumContinents(); ++i){
            int myForces = 0;
            List<Integer> continent = board.getContinent(i);
            for(int j = 0; j != continent.size(); ++j){
                if(board.getOwner(continent.get(j)) == move.getUID()){
                    myForces++;
                }
            }
            double score = (double)myForces / (double)continent.size();
            if(score > bestScore && (score < 1 || bestScore == -1) && score > 0){
                bestContinent = i;
                bestScore = score;
            }
        }
        List<Integer> continent = board.getContinent(bestContinent);
        move.setTerritory(continent.get(random.nextInt(continent.size())));
        move.setArmies(1);
    }
}



