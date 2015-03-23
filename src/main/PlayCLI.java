package main;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import ai.AgentPlayer;
import ai.AgentFactory;
import ai.Agents.Type;
import logic.Game;
import logic.move.WrongMoveException;
import player.IPlayer;
import ui.commandline.CommandLineController;
import ui.commandline.CommandLinePlayer;

/**
 * PlayCLI --- Sets up a game for 1 human to play on the command line vs a variable number of RandomPlayers.
 */
public class PlayCLI {
    private static Random random = new Random();

    public static void main(String[] args){
        int seed = random.nextInt();
        Scanner reader = new Scanner(System.in);
        PrintWriter writer = new PrintWriter(System.out);

        writer.println("Hello! Welcome to Risk");
        writer.print("How many AIs would you like to play against? (2-5)\n> ");
        writer.flush();
        int numAI = 0; boolean correct = false;
        while(!correct){
            writer.flush();
            while(!reader.hasNextInt()){
                writer.print("Invalid input\n> ");
                writer.flush();
                reader.next();
            }
            numAI = reader.nextInt();
            correct = numAI >= 2 && numAI <= 5;
            if(!correct){
                writer.print("Invalid input\n> ");
                writer.flush();
            }
        }

        writer.format("Loading game with %d AIs\n", numAI);
        List<IPlayer> players = new ArrayList<IPlayer>();
        CommandLinePlayer user = new CommandLinePlayer(new CommandLineController(reader, writer), reader, writer);
        players.add(user);
        for(int i = 0; i != numAI; ++i){
            AgentPlayer ai = new AgentPlayer(AgentFactory.buildAgent(Type.SIMPLE));
            players.add(ai);
        }
        Game game = new Game(players, seed);

        try{
            game.setupGame();
            game.playGame();
        } catch(WrongMoveException e){
            System.out.println("Game has crashed.");
            System.out.println(e.getMessage());
        }
    }
}

