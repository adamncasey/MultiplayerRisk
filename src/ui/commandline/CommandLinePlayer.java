package ui.commandline;

import java.io.PrintWriter;
import java.util.Scanner;

import logic.move.Move;
import logic.move.MoveChecker;
import logic.move.WrongMoveException;
import logic.state.Board;
import logic.state.Player;
import player.IPlayer;
import player.PlayerController;

/**
 * CommandLinePlayer --- A player that outputs everything that happens to the console (So we can spectate AI vs AI games / play on the command line)
 */
public class CommandLinePlayer implements IPlayer {
    private PlayerController controller;
    private Scanner reader;
    private PrintWriter writer;

    private Player player;
    private Board board;

    public CommandLinePlayer(PlayerController controller, Scanner reader, PrintWriter writer){
        this.controller = controller;
        this.reader = reader;
        this.writer = writer;
    }

    public void setup(Player player, Board board, MoveChecker checker){
       this.player = player;
       this.board = board;
       this.controller.setup(player, board);
    }

    public void nextMove(String move){
        writer.println(move); 
    }

    public void updatePlayer(Move move) throws WrongMoveException {
        String message = Move.describeMove(move, board);
        writer.print(message);
        writer.flush();
    }

    public void getMove(Move move) throws WrongMoveException {
        controller.getMove(move); 
    }
}


