package ai.strategy;

import java.util.Random;

import logic.move.Move;
import logic.state.Board;
import logic.state.Player;

/**
 * Strategy
 */
public abstract class Strategy {
    protected Random random;

    protected Player player;
    protected Board board;

    public Strategy(Player player, Board board, Random random){
        this.player = player;
        this.board = board;
        this.random = random;
    }

    public abstract void getMove(Move move);
}
