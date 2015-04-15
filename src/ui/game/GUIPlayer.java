package ui.game;

import java.util.*;

import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import logic.move.Move;
import logic.move.MoveChecker;
import logic.state.Board;
import logic.state.Player;
import player.IPlayer;
import player.PlayerController;
import networking.LocalPlayerHandler;
import ui.game.map.MapControl;

/**
 * GUIPlayer
 */
public class GUIPlayer implements IPlayer {
    private PlayerController playerController;
	private GameController gameController;
    private Board board;
    private Player player;
    String lastPlayerToMove;

    private LocalPlayerHandler handler;

    public GUIPlayer(GameController gameController){
        this.gameController = gameController;
    }

    public void setup(Player player, List<String> names, Board board, MoveChecker checker, LocalPlayerHandler handler){
        this.board = board;
        this.player = player;
        playerController.setup(player, board);
        this.handler = handler;
    }

    public void nextMove(String move, String playerName){
        GameController.console.write(move);
        
        Platform.runLater(new Runnable() {
			@Override
			public void run() {
				gameController.moveDescription.setText(move);
			}
		});
        
        final BorderPane lastShield;
        if(lastPlayerToMove != null) {
        	lastShield = gameController.playerShields.get(lastPlayerToMove);
        	
        	Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lastShield.getStyleClass().remove("selectedPlayer");
				}
			});
        }
        
        final BorderPane shield = gameController.playerShields.get(playerName);
    	Platform.runLater(new Runnable() {
			@Override
			public void run() {
		        shield.getStyleClass().add("selectedPlayer");
			}
		});

        lastPlayerToMove = playerName;
    }

    public void updatePlayer(Move move){
        String message = Move.describeMove(move, board);
        System.out.print(message);

        GameController.console.write(message);
        MapControl mapController = gameController.mapControl;

        int number = board.getNumTerritories();
        for(int i = 0 ; i < number ; i++) {
            int owner = board.getOwner(i);
            if (owner >= 0) {
                int armies = board.getArmies(i);
                String name = board.getName(i);
                mapController.setArmies(owner + 1, armies, mapController.getTerritoryByName(name));
            }
        }

        if(move.getUID() == player.getUID()){
            handler.sendMove(move);
        }

        try{
            Thread.sleep(20);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void getMove(Move move){
        if(move.getStage() == Move.Stage.ROLL_HASH || move.getStage() == Move.Stage.ROLL_NUMBER){
            handler.handleRoll(move);
        }else{
            playerController.getMove(move);
        }
    }
    
    public PlayerController getPlayerController() {
		return this.playerController;
	}
    public void setPlayerController(PlayerController playerController) {
		this.playerController = playerController;
	}
}


