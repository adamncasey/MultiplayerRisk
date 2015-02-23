package player;

import java.util.*;
import logic.*;
import ai.*;

public class ComputerPlayer implements IPlayer {

    private PlayerController controller;

    public ComputerPlayer(PlayerController controller){
        this.controller = controller;
    }

    private int uid = 0; // Set and used by Game
    public int getUID(){
        return this.uid;
    }
    public void setUID(int uid){
        this.uid = uid;
        this.controller.setUID(uid);
    }

    private boolean eliminated = false; // Set and used by Game
    public boolean isEliminated(){
        return eliminated;
    }
    public void eliminate(){
        eliminated = true;
    }

    public void nextMove(int currentPlayer, String currentMove){
    }

    public void updatePlayer(Board board, ArrayList<Card> hand, int currentPlayer, Move previousMove){
        this.controller.updateAI(hand, board, currentPlayer, previousMove);
    }

    public Move getMove(Move move){
        return controller.getMove(move); 
    }
}
