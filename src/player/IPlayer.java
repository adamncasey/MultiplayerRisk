package player;

import networking.Message;

/**
 * IPlayer --- The common interface between players and game logic.
 * @author Nathan Blades
 */
public interface IPlayer {
    // This interface will change with the protocol nothing here is final
    // Not sure if using Message is best
    // The current idea is that GameState gets a move from the active player then confirms that the move is ok with all of the other players

   /**
    * Get the player's unique identifier.
    * @return The players identifier
    */
    public String getId(); 

   /**
    * Get a move from the player.
    * @param stage The tye of move (current stage of the turn)
    * @return The move in Message format
    */
    public Message getMove(int stage);

   /**
    * Confirm that a move is legal.
    * @param stage The type of move (current stage of the turn)
    * @param move The move to be confirmed
    * @return True when the move is legal, False otherwise
    */
    public boolean confirmMove(int stage, Message move);
}
