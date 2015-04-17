package main;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import ai.AgentFactory;
import ai.AgentPlayer;
import ai.AgentTypes;
import ai.agents.Agent;
import logic.Game;
import player.IPlayer;
import networking.LocalPlayerHandler;
import ui.commandline.CommandLinePlayer;

/**
 * WatchCLI - Watch 3-6 AIs play out the game on the command line.
 */
public class WatchCLI {

    public static void main(String[] args){ 
        Scanner reader = new Scanner(System.in);
        PrintWriter writer = new PrintWriter(System.out);

        writer.println("Hello! Welcome to Risk");
        writer.print("How many AIs would you like to watch? (3-6)\n> ");
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
            correct = numAI >= 3 && numAI <= 6;
            if(!correct){
                writer.print("Invalid input\n> ");
                writer.flush();
            }
        }

        writer.format("Loading game with %d AIs\n", numAI);
        List<IPlayer> players = new ArrayList<IPlayer>();
        Agent userAgent = AgentFactory.buildAgent(AgentTypes.randomType());
        CommandLinePlayer user = new CommandLinePlayer(userAgent, reader, writer, userAgent.getName());
        players.add(user);
        for(int i = 0; i != numAI-1; ++i){
            Agent agent = AgentFactory.buildAgent(AgentTypes.randomType());
            AgentPlayer ai = new AgentPlayer(agent);
            players.add(ai);
        }
        Game game = new Game(players, new LocalPlayerHandler(), null);

        game.run();

        String nameSummary = "Players were ";
        for(IPlayer player : players){
            nameSummary += String.format("%s, ", player.getPlayerName());
        }
        nameSummary = nameSummary.substring(0, nameSummary.length() - 2);
        writer.println(nameSummary); 
        writer.flush();
    }
}
