package main;

import java.util.*;
import java.io.*;

import ai.*;
import logic.*;
import player.*;

/**
 * WatchCLI - Watch 2-6 AIs play out the game on the command line.
 */
public class WatchCLI {
    private static String boardFilename = "resources/risk_map.json";
    private static Random random = new Random();

    public static void main(String[] args){ 
        int seed = random.nextInt();
        Scanner reader = new Scanner(System.console().reader());
        PrintWriter writer = System.console().writer();

        writer.println("Hello! Welcome to Risk");
        writer.print("How many AIs would you like to watch? (3-6)\n> ");
        writer.flush();
        int numAI = 0; boolean correct = false;
        while(!correct){
            writer.flush();
            while(!reader.hasNextInt()){
                writer.print("Invalid Input\n> ");
                writer.flush();
                reader.next();
            }
            numAI = reader.nextInt();
            correct = numAI >= 3 && numAI <= 6;
            if(!correct){
                writer.print("Invalid Input\n> ");
                writer.flush();
            }
        }

        writer.format("Loading game with %d AIs\n", numAI);
        ArrayList<IPlayer> players = new ArrayList<IPlayer>();
        CommandLinePlayer user = new CommandLinePlayer(new SimpleAI(), reader, writer, false, false);
        players.add(user);
        for(int i = 0; i != numAI-1; ++i){
            ComputerPlayer ai = new ComputerPlayer(new SimpleAI());
            players.add(ai);
        }
        Game game = new Game(players, 0, seed, boardFilename);

        try{
            writer.println("The setup will now begin"); 
            game.setupGame();
            writer.println("The setup has completed");

            writer.println("The game will now start");
            int turns = game.playGame();
            writer.format("The game has completed in %d turns\n", turns);
        } catch(WrongMoveException e){
            System.out.println("Game has crashed.");
            System.out.println(e.getMessage());
        }
    }
}
