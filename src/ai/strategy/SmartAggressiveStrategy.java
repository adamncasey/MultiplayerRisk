package ai.strategy;

import java.util.List;
import java.util.Random;
import logic.move.Move;
import logic.state.Board;
import logic.state.Player;

/**
 * SmartAggressiveStrategy --- Always attack if you have an advantage, place armies and fortify to the front lines.
 */
public class SmartAggressiveStrategy extends Strategy {

    public SmartAggressiveStrategy(Player player, Board board, Random random){
        super(player, board, random);
    }

    public void getMove(Move move){
        switch(move.getStage()){
            case PLACE_ARMIES:
                placeArmies(move);
                return;
            case DECIDE_ATTACK:
                pickBestAttack(move, true);
                return;
            case START_ATTACK:
                pickBestAttack(move, false);
                return;
            default:
                assert false : move.getStage();
        }
    }

    // Places armies on the front lines
    private void placeArmies(Move move){
        int territory = -1;
        boolean ok = false;
        if(move.getExtraArmies() > 0){
            List<Integer> matches = move.getMatches();
            territory = matches.get(random.nextInt(matches.size()));
            move.setTerritory(territory);
            move.setArmies(2);
            return;
        }

        while(!ok){
            territory = random.nextInt(board.getNumTerritories());
            if(board.getOwner(territory) != move.getUID()){
                continue;
            }
            for(int link : board.getLinks(territory)){
                if(board.getOwner(link) != move.getUID()){
                    ok = true;
                }
            }
        }

        move.setTerritory(territory);
        move.setArmies(1);
    }

    // Always attack if you have an advantage
    private void pickBestAttack(Move move, boolean deciding){
        int bestFrom = -1; int bestTo = -1;
        int bestScore = -Integer.MAX_VALUE;
        int uid = player.getUID();
        for(int i = 0; i != board.getNumTerritories(); ++i){
            if(board.getOwner(i) != uid){
                continue;
            }
            for(int j : board.getLinks(i)){
                if(board.getOwner(j) == uid){
                    continue;
                }
                int score = board.getArmies(i) - board.getArmies(j);
                if(score > bestScore){
                    bestScore = score;
                    bestFrom = i;
                    bestTo = j;
                }            
            }
        }
        if(deciding){
            if(bestScore > 0){
                move.setDecision(true);
            }else{
                move.setDecision(false);
            }
        }else{
            move.setFrom(bestFrom);
            move.setTo(bestTo);
        }
    }
}


