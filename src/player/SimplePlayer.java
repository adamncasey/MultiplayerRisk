package player;

import networking.Message;

/**
 * SimplePlayer --- Implements the basic features of IPlayer for testing purposes.
 * @author Nathan Blades
 */

public class SimplePlayer implements IPlayer {

    private String id;

    SimplePlayer(String id){
        this.id = id;
    }
 
    public String getId(){
        return id;
    }

    public Message getMove(int stage){
        Message m = new Message();
        return m;
    }

    public boolean confirmMove(int stage, Message move){
        return true;
    }
}
