package player;

import java.util.*;
import logic.*;
import ai.*;

public class ComputerPlayer implements IPlayer {

    private PlayerController controller;

    public ComputerPlayer(PlayerController controller){
        this.controller = controller;
    }

    public void nextMove(int currentPlayer, String currentMove){
    }

    public void updatePlayer(Board board, List<Card> hand, int currentPlayer, Move previousMove){
        this.controller.updateAI(hand, board, currentPlayer, previousMove);
    }

    public Move getMove(Move move){
        return controller.getMove(move); 
    }
}
