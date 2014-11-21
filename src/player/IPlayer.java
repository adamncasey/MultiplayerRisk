package player;

import java.util.ArrayList;
import networking.Message;

/**
 * IPlayer --- The common interface between players and game logic.
 */
public interface IPlayer {
    // This interface will change with the protocol nothing here is final

    private ArrayList<Card> hand;

   /**
    * Get the player's unique identifier.
    * @return The players identifier
    */
    public String getId(); 

   /**
    * The Game / Central server will provide a card, the player should keep track of it.
    */
    public void addCard(Card card);

   /**
    * Get a move from the player.
    * @param stage The tye of move (current stage of the turn)
    * @return The move in GameMove format
    */
    public GameMove getMove(int stage);

   /**
    * Confirm that a move is legal.
    * @param stage The type of move (current stage of the turn)
    * @param move The move to be confirmed
    * @return True when the move is legal, False otherwise
    */
    public boolean confirmMove(int stage, GameMove move);
}
