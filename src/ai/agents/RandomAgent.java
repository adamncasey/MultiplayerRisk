package ai.agents;

import ai.strategy.RandomStrategy;
import logic.move.Move;
import logic.state.Board;
import logic.state.Player;

public class RandomAgent extends Agent {

    private RandomStrategy rs;

    public RandomAgent(){
    }

    public void setup(Player player, Board board){
        super.setup(player, board);
        rs = new RandomStrategy(player, board, random);
    }

    public String getName(){
        return "Random";
    }

    public String getDescription(){
        return "Randomly decides what to do.";
    }

    public void getMove(Move move){
        rs.getMove(move);
    }
}
