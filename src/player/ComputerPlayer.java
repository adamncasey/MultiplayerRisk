package player;

import logic.Player;
import logic.Board;
import logic.Move;
import logic.MoveChecker;

public class ComputerPlayer implements IPlayer {

    private PlayerController controller;

    public ComputerPlayer(PlayerController controller){
        this.controller = controller;
    }

    public void setup(Player player, Board board, MoveChecker checker){
        this.controller.setup(player, board);
    }

    public void nextMove(String move){
    }

    public void updatePlayer(Move move){
    }

    public void getMove(Move move){
        controller.getMove(move); 
    }
}
