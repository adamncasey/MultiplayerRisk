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

    private boolean printBoard = false;
    private boolean slowDown = false;

    public CommandLinePlayer(PlayerController controller, Scanner reader, PrintWriter writer, boolean printBoard, boolean slowDown){
        this.controller = controller;
        this.reader = reader;
        this.writer = writer;
        this.printBoard = printBoard;
        this.slowDown = slowDown;
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
        writer.format("Player %d is %s\n", currentPlayer, currentMove);
        writer.flush();
    }

    public void updatePlayer(Board board, ArrayList<Card> hand, int currentPlayer, Move previousMove){
        this.controller.updateAI(hand, board, currentPlayer, previousMove);
        if(printBoard){
            board.printBoard(writer);
        }
        if(slowDown){
            try{
                Thread.sleep(100);
            }catch(Exception e){
            }
        }
    }

    public void endGame(int winner){
        writer.format("Player %d is the winner!\n", winner);
    }

    public Move getMove(Move move){
        return controller.getMove(move); 
    }
}


