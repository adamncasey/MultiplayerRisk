package logic;


/**
 * GameState --- Stores and updates the game board, can be queried to check if a move is valid.
 */
public class GameState {

    private Board board;
    

    public GameState(){
    } 


    public boolean isMoveValid(GameMove move){
        return true;
    }


    // GameMove should have some information about the type of move allowing GameState to update the board appropriately
    public void makeMove(GameMove move){

    } 
}
