package ai;

import logic.move.Move;
import logic.move.MoveChecker;
import logic.move.WrongMoveException;
import logic.state.Board;
import logic.state.Player;
import player.IPlayer;
import player.PlayerController;

public class AIPlayer implements IPlayer {

    private PlayerController controller;

    public AIPlayer(PlayerController controller){
        this.controller = controller;
    }

    public void setup(Player player, Board board, MoveChecker checker){
        this.controller.setup(player, board);
    }

    public void nextMove(String move){
    }

    public void updatePlayer(Move move) throws WrongMoveException {
    }

    public void getMove(Move move) throws WrongMoveException {
        controller.getMove(move); 
    }
}
