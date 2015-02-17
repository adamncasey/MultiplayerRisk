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

        ArrayList<IPlayer> players = new ArrayList<IPlayer>();

        System.out.println("Adding 1 RandomPlayer");
        RandomPlayer p1 = new RandomPlayer();
        players.add(p1);
        
        System.out.println("Creating Game");
        Game game = new Game(players, seed, boardFilename);
    }
}
