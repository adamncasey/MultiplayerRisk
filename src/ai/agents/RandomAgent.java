package ai.agents;

import ai.strategy.RandomStrategy;
import ai.strategy.PassiveStrategy;
import logic.move.Move;
import logic.state.Board;
import logic.state.Player;

public class RandomAgent extends Agent {

    private RandomStrategy rs;
    private PassiveStrategy ps;

    public RandomAgent(){
    }

    public void setup(Player player, Board board){
        super.setup(player, board);
        rs = new RandomStrategy(player, board, random);
        ps = new PassiveStrategy(player, board, random);
    }

    public String getName(){
        return "Random";
    }

    public String getDescription(){
        return "Randomly decides what to do.";
    }

    public void getMove(Move move){
        switch(move.getStage()){
            case CLAIM_TERRITORY:
                rs.getMove(move);
                return;
            case REINFORCE_TERRITORY:
                rs.getMove(move);
                return;
            case TRADE_IN_CARDS:
                ps.getMove(move);
                return;
            case PLACE_ARMIES:
                rs.getMove(move);
                return;
            case DECIDE_ATTACK:
                rs.getMove(move);
                return;
            case START_ATTACK:
                rs.getMove(move);
                return;
            case CHOOSE_ATTACK_DICE:
                rs.getMove(move);
                return;
            case CHOOSE_DEFEND_DICE:
                rs.getMove(move);
                return;
            case OCCUPY_TERRITORY:
                rs.getMove(move);
                return;
            case DECIDE_FORTIFY:
                rs.getMove(move);
                return;
            case START_FORTIFY:
                rs.getMove(move);
                return;
            case FORTIFY_TERRITORY:
                rs.getMove(move);
                return;
            default:
                assert false : move.getStage();
        }
    }
}
