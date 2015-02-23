package main;

import java.io.*;
import java.util.*;

import ai.*;
import logic.*;
import player.*;

/**
 * PlayCLI --- Sets up a game for 1 human to play on the command line vs a variable number of RandomPlayers.
 */
public class PlayCLI {
   // private static String boardFilename = "resources/risk_map.json";
   // private static Random random = new Random();

    public static void main(String[] args){
   //     int seed = random.nextInt();
   //     Scanner reader = new Scanner(System.console().reader());
   //     PrintWriter writer = System.console().writer();

   //     writer.println("Hello! Welcome to Risk");
   //     writer.print("How many AIs would you like to play against? (2-5)\n> ");
   //     writer.flush();
   //     int numAI = 0; boolean correct = false;
   //     while(!correct){
   //         writer.flush();
   //         while(!reader.hasNextInt()){
   //             writer.print("Invalid Input\n> ");
   //             writer.flush();
   //             reader.next();
   //         }
   //         numAI = reader.nextInt();
   //         correct = numAI >= 2 && numAI <= 5;
   //         if(!correct){
   //             writer.print("Invalid Input\n> ");
   //             writer.flush();
   //         }
   //     }

   //     writer.format("Loading game with %d AIs\n", numAI);
   //     ArrayList<IPlayer> players = new ArrayList<IPlayer>();
   //     CommandLinePlayer user = new CommandLinePlayer(reader, writer);
   //     players.add(user);
   //     for(int i = 0; i != numAI; ++i){
   //         RandomPlayer ai = new RandomPlayer();
   //         players.add(ai);
   //     }
   //     Game game = new Game(players, 0, seed, boardFilename);

   //     writer.println("The setup will now begin"); 
   //     game.setupGame();
   //     writer.println("The setup has completed");

   //     writer.println("The game will now start");
   //     int turns = game.playGame();
   //     writer.format("The game has completed in %d turns\n", turns);
    }
}

