package ai.agents;

import java.util.Random;

import logic.move.Move;
import logic.state.Board;
import logic.state.Player;
import player.PlayerController;

public abstract class Agent implements PlayerController {
    protected Random random;
    protected Board board;
    protected Player player;

    public Agent(){
    }

    public void setup(Player player, Board board){
        this.random = new Random();
        this.board = board;
        this.player = player;
    }

    public abstract String getName();

    public abstract String getDescription();

    public abstract void getMove(Move move);
}
