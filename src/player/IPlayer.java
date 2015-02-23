package player;

import logic.*;
import java.util.ArrayList;

/**
 * IPlayer --- The common interface between players and game logic.
 */
public interface IPlayer {
    public int getUID();
    public void setUID(int uid);

    public boolean isEliminated();
    public void eliminate();

    // Lets the player know when ever the gamestate changes, and describes how it changed
    public void updatePlayer(Board board, ArrayList<Card> hand, int currentPlayer, Move previousMove);

    // Lets the player know who is currently acting, and what they are doing
    public void nextMove(int currentPlayer, String currentMove);

    public Move getMove(Move move);
}
