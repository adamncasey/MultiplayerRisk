package player;

import logic.GameState;
import networking.Message;

/**
 * SimplePlayer --- Implements the basic features of IPlayer for testing purposes.
 */
public class SimplePlayer implements IPlayer {

    private String id;
    private GameState state; // SimplePlayer keeps a reference to the game state so that it can confirm moves.
    private ArrayList<Card> hand;

    public SimplePlayer(String id, GameState state){
        this.id = id;
        this.state = state;
    }
 
    public String getId(){
        return id;
    }

    public Card addCard(Card card){
        hand.add(card);
    }

    public GameMove getMove(int stage){
        GameMove move = null;
        return move;
    }

    public boolean confirmMove(int stage, GameMove move){
        GameMove move = null;
        // SimplePlayer uses the game state to confirm moves,
        // other players would confirm moves in other ways (i.e. across the network)
        return state.isMoveValid(move);
    }
}
