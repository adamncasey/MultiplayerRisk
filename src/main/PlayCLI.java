package main;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import ai.AgentFactory;
import ai.AgentPlayer;
import ai.AgentTypes;
import ai.agents.Agent;
import logic.Game;
import player.IPlayer;
import networking.LocalPlayerHandler;
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
        List<String> names = new ArrayList<String>();
        CommandLinePlayer user = new CommandLinePlayer(new CommandLineController(reader, writer), reader, writer);
        players.add(user);
        names.add("User 1");
        for(int i = 0; i != numAI; ++i){
            Agent agent = AgentFactory.buildAgent(AgentTypes.randomType());
            AgentPlayer ai = new AgentPlayer(agent);
            players.add(ai);
            names.add(String.format("%s %d", agent.getName(), i + 2));
        }
        Game game = new Game(players, names, new LocalPlayerHandler());

        game.setupGame();
        game.playGame();
    }
}

