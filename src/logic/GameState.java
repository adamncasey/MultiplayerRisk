package logic;


import networking.Message;


/**
 * GameState --- Stores and updates the game board, can be queried to check if a move is valid.
 * @author Nathan Blades
 */
public class GameState {


    public GameState(){
    } 


    public boolean isMoveValid(Message m){
        return true;
    }

    public void update(Message m){

    }
   
}
