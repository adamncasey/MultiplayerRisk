package main;

import java.util.ArrayList;
import java.util.Scanner;

import logic.*;
import player.*;

/**
 * RunGame --- A simple main that tests Game / GameState / IPlayer 
 */
public class RunGame {

    public static void main(String[] args){ 
        System.out.println("Begin");

//        String boardFilename = "resources/testBoard.json";
        String boardFilename = "resources/risk_map.json";

        System.out.format("Using board : %s\n", boardFilename);

        System.out.println("Creating the GameState");
        GameState state = new GameState(boardFilename);

        System.out.println("Creating player list");
        ArrayList<IPlayer> players = new ArrayList<IPlayer>(); 
        System.out.println("Adding player 1");
        SimplePlayer player1 = new SimplePlayer("Player1", state);
        players.add(player1);
        System.out.println("Adding player 2");
        SimplePlayer player2 = new SimplePlayer("Player2", state);
        players.add(player2);
        System.out.println("Adding player 3");
        SimplePlayer player3 = new SimplePlayer("Player3", state);
        players.add(player3);


        System.out.println("End");
//        System.out.println("Setting up the game");
//        Game game = new Game(players, state);
//        game.setupGame();

//        System.out.println("Running Game - Control transferred to Game");
//        game.playGame();
    
//        System.out.println("Game ended");
    }
}
