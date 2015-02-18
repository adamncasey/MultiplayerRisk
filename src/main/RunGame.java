package main;

import java.util.*;

import ai.*;
import logic.*;
import player.*;

/**
 * RunGame --- Sets up and runs the game with 3 RandomPlayers.
 */
public class RunGame {

    public static void main(String[] args){ 
        String boardFilename = "resources/risk_map.json";
        System.out.format("Loading Game Using board : %s\n", boardFilename);

        Random random = new Random();
        int seed = random.nextInt(); // Should be chosen by dice roll and agreed upon by all players 

        ArrayList<IPlayer> players = new ArrayList<IPlayer>();

        System.out.println("Adding RandomPlayer 1");
        RandomPlayer p1 = new RandomPlayer();
        players.add(p1);
        System.out.println("Adding RandomPlayer 2");
        RandomPlayer p2 = new RandomPlayer();
        players.add(p2);
        System.out.println("Adding RandomPlayer 3");
        RandomPlayer p3 = new RandomPlayer();
        players.add(p3);
        
        System.out.println("Creating Game");
        Game game = new Game(players, 0, seed, boardFilename);

        System.out.println("Setting Up Game");
        game.setupGame();

        System.out.println("Playing Game");
        int turns = game.playGame();
        System.out.format("Game ended in %d turns\n", turns);
    }
}
