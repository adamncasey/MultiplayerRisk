package player;

import java.io.PrintWriter;
import java.util.Scanner;
import logic.Player;
import logic.Board;
import logic.Move;
import logic.MoveChecker;

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

    public void updatePlayer(Move move){
        String message = Move.describeMove(move, board);
        writer.print(message);
        writer.flush();
    }

    public void getMove(Move move){
        controller.getMove(move); 
    }
}


