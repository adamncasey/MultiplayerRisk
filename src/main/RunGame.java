package main;

import java.util.*;
import java.io.*;

import ai.*;
import logic.*;
import player.*;

/**
 * RunGame --- Sets up and runs the game with 3 RandomPlayers.
 */
public class RunGame {

    public static void main(String[] args){ 
        Scanner reader = new Scanner(System.console().reader());
        PrintWriter writer = System.console().writer();

        String boardFilename = "resources/risk_map.json";
        System.out.format("Loading Game Using board : %s\n", boardFilename);

        Random random = new Random();
        int seed = random.nextInt(); // Should be chosen by dice roll and agreed upon by all players 

        ArrayList<IPlayer> players = new ArrayList<IPlayer>();

        System.out.println("Adding player 1 - CommandLinePlayer with SimpleAI");
        PlayerController p1Controller = new SimpleAI();
        CommandLinePlayer p1 = new CommandLinePlayer(p1Controller, reader, writer, false, false);
        players.add(p1);

        System.out.println("Adding player 2 - ComputerPlayer with SimpleAI");
        PlayerController p2Controller = new SimpleAI();
        ComputerPlayer p2 = new ComputerPlayer(p2Controller);
        players.add(p2);

        System.out.println("Adding AIPlayer 3 with SimpleAI");
        PlayerController p3Controller = new SimpleAI();
        ComputerPlayer p3 = new ComputerPlayer(p3Controller);
        players.add(p3);
        
        System.out.println("Creating Game");
        Game game = new Game(players, 0, seed, boardFilename);

        try{
            System.out.println("Setting Up Game");
            game.setupGame();
        }catch(WrongMoveException e){
            System.out.println("Game crashed during setup");
            System.out.println(e.getMessage());
        }

        try{
            System.out.println("Playing Game");
            int turns = game.playGame();
            System.out.format("Game ended in %d turns\n", turns);
        }catch(WrongMoveException e){
            System.out.println("Game crashed during game");
            System.out.println(e.getMessage());
        }
    }
}
