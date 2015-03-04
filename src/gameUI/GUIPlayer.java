package gameUI;

import java.io.*;
import java.util.*;

import player.IPlayer;
import player.PlayerController;
import logic.*;

/**
 * GUIPlayer
 */
public class GUIPlayer implements IPlayer {
    private PlayerController controller;
    private Scanner reader;
    private PrintWriter writer;

    public Main gui = new Main();
    
    private boolean slowDown = false;

    public GUIPlayer(PlayerController controller, Scanner reader, PrintWriter writer, boolean slowDown){
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

        String message = MoveProcessor.processMove(currentPlayer, previousMove, board);
        
        Controller controller = (Controller) gui.getLoader().getController();
        
        controller.printToConsole(message);

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


