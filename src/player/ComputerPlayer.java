package player;

import logic.Player;
import logic.Board;
import logic.Move;

public class ComputerPlayer implements IPlayer {

    private PlayerController controller;

    public ComputerPlayer(PlayerController controller){
        this.controller = controller;
    }

    public void setup(Player player, Board board){
        this.controller.setup(player, board);
    }

    public void nextMove(String move){
    }

    public void updatePlayer(Move move){
    }

    public Move getMove(Move move){
        return controller.getMove(move); 
    }
}
