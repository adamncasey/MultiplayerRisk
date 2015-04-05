package ai.agents;

import ai.strategy.AggressiveStrategy;
import ai.strategy.RandomStrategy;
import ai.strategy.CardsStrategy;
import logic.move.Move;
import logic.state.Board;
import logic.state.Player;

public class GreedyAgent extends Agent {

    private AggressiveStrategy as;
    private RandomStrategy rs;
    private CardsStrategy cs;

    public GreedyAgent(){
    }

    public void setup(Player player, Board board){
        super.setup(player, board);
        as = new AggressiveStrategy(player, board, random);
        rs = new RandomStrategy(player, board, random);
        cs = new CardsStrategy(player, board, random);
    }

    public String getName(){
        return "Greedy";
    }

    public String getDescription(){
        return "Trades in cards ASAP. Tries to draw a card every turn.";
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
                cs.getMove(move);
                return;
            case PLACE_ARMIES:
                rs.getMove(move);
                return;
            case DECIDE_ATTACK:
                cs.getMove(move);
                return;
            case START_ATTACK:
                rs.getMove(move);
                return;
            case CHOOSE_ATTACK_DICE:
                as.getMove(move);
                return;
            case CHOOSE_DEFEND_DICE:
                as.getMove(move);
                return;
            case OCCUPY_TERRITORY:
                as.getMove(move);
                return;
            case DECIDE_FORTIFY:
                as.getMove(move);
                return;
            case START_FORTIFY:
                as.getMove(move);
                return;
            case FORTIFY_TERRITORY:
                as.getMove(move);
                return;
            default:
                assert false : move.getStage();
        }
    }
}


