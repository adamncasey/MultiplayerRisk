package player;

import java.io.*;
import java.util.*;
import logic.*;

/**
 * CommandLinePlayer --- A player that outputs everything that happens to the console (So we can spectate AI vs AI games / play on the command line)
 */
public class CommandLinePlayer implements IPlayer {
    private PlayerController controller;
    private Scanner reader;
    private PrintWriter writer;

    private boolean slowDown = false;

    public CommandLinePlayer(PlayerController controller, Scanner reader, PrintWriter writer, boolean slowDown){
        this.controller = controller;
        this.reader = reader;
        this.writer = writer;
        this.slowDown = slowDown;
    }

    private boolean eliminated = false; // Set and used by Game
    public boolean isEliminated(){
        return eliminated;
    }
    public void eliminate(){
        eliminated = true;
    }

    public void nextMove(int currentPlayer, String currentMove){
        writer.println(currentMove); 
    }

    public void updatePlayer(Board board, List<Card> hand, int currentPlayer, Move previousMove){
        this.controller.updateAI(hand, board, currentPlayer, previousMove);

        String message = MoveProcessor.processMove(currentPlayer, previousMove, board);
        writer.print(message);
        writer.flush();

        if(slowDown){
            try{
                Thread.sleep(100);
            }catch(Exception e){
            }
        }
    }

    public Move getMove(Move move){
        return controller.getMove(move); 
    }
}


