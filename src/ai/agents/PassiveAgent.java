package ai.agents;

import ai.strategy.PassiveStrategy;
import ai.strategy.RandomStrategy;
import logic.move.Move;
import logic.state.Board;
import logic.state.Player;

public class PassiveAgent extends Agent {

    private PassiveStrategy ps;
    private RandomStrategy rs;

    public PassiveAgent(){
        super("Passive");
    }

    public void setup(Player player, Board board){
        super.setup(player, board);
        ps = new PassiveStrategy(player, board, random);
        rs = new RandomStrategy(player, board, random);
    }

    public String getDescription(){
        return "Does as little as possible.";
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
                ps.getMove(move);
                return;
            case START_ATTACK:
                return;
            case CHOOSE_ATTACK_DICE:
                return;
            case CHOOSE_DEFEND_DICE:
                ps.getMove(move);
                return;
            case OCCUPY_TERRITORY:
                return;
            case DECIDE_FORTIFY:
                ps.getMove(move);
                return;
            case START_FORTIFY:
                return;
            case FORTIFY_TERRITORY:
                return;
            default:
                assert false : move.getStage();
        }
    }
}

