package player;

import logic.*;
import java.util.*;

/**
 * IPlayer --- The common interface between players and game logic.
 */
public interface IPlayer {
    public boolean isEliminated();
    public void eliminate();

    // Lets the player know who is currently acting, and what they are doing
    public void nextMove(int currentPlayer, String currentMove);

    // Lets the player know when ever the gamestate changes, and describes how it changed
    public void updatePlayer(Board board, List<Card> hand, int currentPlayer, Move previousMove);

    public Move getMove(Move move);
}
