package ai.agents;

import ai.strategy.PassiveStrategy;
import logic.move.Move;
import logic.state.Board;
import logic.state.Player;

public class PassiveAgent extends Agent {

    private PassiveStrategy ps;

    public PassiveAgent(){
    }

    public void setup(Player player, Board board){
        super.setup(player, board);
        ps = new PassiveStrategy(player, board, random);
    }

    public String getName(){
        return "Passive";
    }

    public String getDescription(){
        return "Does as little as possible.";
    }

    public void getMove(Move move){
        ps.getMove(move);
    }
}

