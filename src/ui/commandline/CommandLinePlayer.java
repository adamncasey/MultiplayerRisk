package ui.commandline;

import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import logic.move.Move;
import logic.move.MoveChecker;
import logic.state.Board;
import logic.state.Player;
import player.IPlayer;
import player.PlayerController;
import networking.LocalPlayerHandler;

/**
 * CommandLinePlayer --- A player that outputs everything that happens to the console (So we can spectate AI vs AI games / play on the command line)
 */
public class CommandLinePlayer implements IPlayer {
    private PlayerController controller;
    private Scanner reader;
    private PrintWriter writer;
	private String playerName;

    private Player player;
    private Board board;

    private LocalPlayerHandler handler;
    
    private final int playerid;

    public CommandLinePlayer(PlayerController controller, Scanner reader, PrintWriter writer, String playerName, int playerid){
        this.controller = controller;
        this.reader = reader;
        this.writer = writer;
        this.playerName = playerName;
        this.playerid = playerid;
    }

    public void setup(Player player, List<String> names, Board board, MoveChecker checker, LocalPlayerHandler handler){
       this.player = player;
       this.board = board;
       this.controller.setup(player, board);
       this.handler = handler;
    }

    public void nextMove(String move, String playerName){
        if(move.getStage() != Move.Stage.ROLL_HASH && move.getStage() != Move.Stage.ROLL_NUMBER){
            writer.println(move); 
            writer.flush();
        }
    }

    public void updatePlayer(Move move){
        if(move.getStage() != Move.Stage.ROLL_HASH && move.getStage() != Move.Stage.ROLL_NUMBER){
            String message = Move.describeMove(move, board);
            writer.print(message);
            writer.flush();
        }
        if(move.getUID() == player.getUID()){
            handler.sendMove(move);
        }
    }

    public void getMove(Move move){
        if(move.getStage() == Move.Stage.ROLL_HASH || move.getStage() == Move.Stage.ROLL_NUMBER){
            handler.handleRoll(move);
        }else{
            controller.getMove(move);
        }
    }

	@Override
	public String getPlayerName() {
		return playerName;
	}

	@Override
	public int getPlayerid() {
		return playerid;
	}
}


