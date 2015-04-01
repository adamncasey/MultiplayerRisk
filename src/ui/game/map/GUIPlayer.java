package ui.game.map;

import java.io.*;
import java.util.*;

import javafx.application.Application;
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
    private PlayerController controller;

    private Main gui;
    GameController guiController;

    private Board board;
    private Player player;

    public GUIPlayer(PlayerController controller){
        this.controller = controller;
    }

    public void setup(Player player, List<String> names, Board board, MoveChecker checker){
        this.board = board;
        this.player = player;
        gui = new Main().getSelf();

        gui.launch(gui.getClass());

        FXMLLoader loader = gui.getLoader();
        guiController = (GameController) loader.getController();

        guiController.setGUIPlayer(this);
    }

    public void nextMove(String move){
        guiController.console.write(move);
    }

    public void updatePlayer(Move move){
        String message = Move.describeMove(move, board);
        guiController.console.write(message);
        MapControl mapController = guiController.mapControl;

        int number = board.getNumTerritories();
        for(int i = 0 ; i < number ; i++){
            int armies = board.getArmies(i);
            int owner = board.getOwner(i);
            String name = board.getName(i);
            if (owner >= 0)
                mapController.setArmies(owner+1,armies,mapController.getTerritoryByName(name));
        }

        try {
            sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void getMove(Move move){
        controller.getMove(move); 
    }
}


