package player;

import logic.Player;
import logic.Board;
import logic.Move;

/**
 * IPlayer --- The common interface between players and game logic.
 */
public interface IPlayer {
    // Provides references to the board / player objects
    public void setup(Player player, Board board);

    // Lets the player know who is currently acting, and what they are doing
    public void nextMove(String currentMove);

    // Lets the player know when ever the gamestate changes, and describes how it changed
    public void updatePlayer(Move move);

    // Asks the player to make their move
    public Move getMove(Move move);
}
