package main;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

import logic.*;
import player.*;

/**
 * RunGame --- A simple main that tests Game / GameState / IPlayer 
 */
public class RunGame {

    public static void main(String[] args){ 
        System.out.println("Begin");

        String boardFilename = "resources/risk_map.json"; // Should be passed in from lobby

        System.out.format("Using board : %s\n", boardFilename);

        Random random = new Random();
        int seed = random.nextInt(); // Should be chosen by dice roll and agreed upon by all players (seed is a 32 bit int for the mersenne twister)


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


        System.out.println("Creating Game");
        Game game = new Game(players, 0, state);

        System.out.println("Setting up game - Control transferred to Game");
        game.setupGame();
        System.out.println("Setup finished");

        System.out.println("Playing game - Control transferred to Game");
        game.playGame();
        System.out.println("Game finished");
    
        System.out.println("End");
    }
}
