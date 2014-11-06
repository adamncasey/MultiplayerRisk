package player;

import logic.GameState;
import networking.Message;

/**
 * SimplePlayer --- Implements the basic features of IPlayer for testing purposes.
 * @author Nathan Blades
 */
public class SimplePlayer implements IPlayer {

    private String id;
    private GameState state; // SimplePlayer keeps a reference to the game state so that it can confirm moves.

    public SimplePlayer(String id, GameState state){
        this.id = id;
        this.state = state;
    }
 
    public String getId(){
        return id;
    }

    public Message getMove(int stage){
        Message m = new Message();
        return m;
    }

    public boolean confirmMove(int stage, Message move){
        Message m = new Message();
        // SimplePlayer uses the game state to confirm moves,
        // other players would confirm moves in other ways (i.e. across the network)
        return state.isMoveValid(m);
    }
}
