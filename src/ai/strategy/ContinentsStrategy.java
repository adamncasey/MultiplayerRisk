package ai.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import logic.Card;
import logic.move.Move;
import logic.state.Board;
import logic.state.Player;

/**
 * ContinentsStrategy
 */
public class ContinentsStrategy extends Strategy {

    public ContinentsStrategy(Player player, Board board, Random random){
        super(player, board, random);
    }

    public void getMove(Move move){
        switch(move.getStage()){
            case CLAIM_TERRITORY:
                claimTerritory(move);
                return;
            default:
                assert false : move.getStage();
        }
    }

    private void claimTerritory(Move move){
        List<Integer> territories = board.getContinent(pickSmallestContinent());
        move.setTerritory(territories.get(random.nextInt(territories.size())));
    }

    private int pickSmallestContinent(){
        int smallestContinent = -1;
        int bestSize = Integer.MAX_VALUE;
        for(int i = 0; i != board.getNumContinents(); ++i){
            List<Integer> continent = board.getContinent(i);
            int freeTerritories = continent.size();
            for(int j = 0; j != continent.size(); ++j){
                 if(board.getOwner(continent.get(j)) != -1){
                     freeTerritories--;
                 }
            }
            if(freeTerritories == 0){
                continue;
            }
            int continentSize = continent.size();
            if(continentSize < bestSize){
                smallestContinent = i;
                bestSize = continentSize;
            }
        }
        return smallestContinent;
    }
}


