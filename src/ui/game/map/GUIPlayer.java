package ui.game.map;

import java.io.*;
import java.util.*;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import logic.move.Move;
import logic.move.MoveChecker;
import logic.state.Board;
import logic.state.Player;
import player.IPlayer;
import player.PlayerController;
import ui.game.GameController;
import ui.game.Main;
import javafx.stage.Stage;

import static java.lang.Thread.sleep;

/**
 * GUIPlayer
 */
public class GUIPlayer implements IPlayer {
    private PlayerController playerController;
    private GameController gameController;
    private Board board;
    private Player player;

    public GUIPlayer(GameController gameController, PlayerController playerController){
        this.playerController = playerController;
        this.gameController = gameController;
    }

    public void setup(Player player, List<String> names, Board board, MoveChecker checker){
        this.board = board;
        this.player = player;
        playerController.setup(player, board);
    }

    public void nextMove(String move){
        GameController.console.write(move);
    }

    public void updatePlayer(Move move){
        String message = Move.describeMove(move, board);
        System.out.print(message);

        GameController.console.write(message);
        MapControl mapController = gameController.mapControl;

        int number = board.getNumTerritories();
        for(int i = 0 ; i < number ; i++){
            int owner = board.getOwner(i);
            if (owner >= 0) {
                int armies = board.getArmies(i);
                String name = board.getName(i);
                mapController.setArmies(owner + 1, armies, mapController.getTerritoryByName(name));
            }
        }

    }

    public void getMove(Move move){
    	playerController.getMove(move); 
    }
}


