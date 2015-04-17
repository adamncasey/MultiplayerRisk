package ai.agents;

import java.util.Random;

import logic.move.Move;
import logic.state.Board;
import logic.state.Player;
import player.PlayerController;

public abstract class Agent implements PlayerController {
    private static int ID_COUNTER = 1;

    protected Random random;
    protected Board board;
    protected Player player;

    private String name;

    public Agent(String name){
        this.name = String.format("%s %d", name, ID_COUNTER++);
    }

    public void setup(Player player, Board board){
        this.random = new Random();
        this.board = board;
        this.player = player;
    }

    public String getName(){
        return this.name;
    }

    public abstract String getDescription();

    public abstract void getMove(Move move);
}
