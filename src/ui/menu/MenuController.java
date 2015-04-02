package ui.menu;

import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;

import player.IPlayer;
import ai.AgentFactory;
import ai.AgentPlayer;
import ai.AgentTypes;
import ai.agents.Agent;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import ui.*;

public class MenuController extends AnchorPane implements Initializable {
	
    private Main application;

    public void setApp(Main application) {
        this.application = application;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void createGame(ActionEvent event) {
        if (application != null) {
        	application.gotoCreateGame();
        }
    }
    
    public void directConnect(ActionEvent event) {
        if (application != null) {
        	application.gotoDirectConnect();
        }
    }

    public void quit(ActionEvent event) throws Exception {
        if (application != null) {
            System.exit(0);
        }
    }
    
    
	// ================================================================================
	// Test map
	// ================================================================================
    
    private static Random random = new Random();
    public void gametest(ActionEvent event) {
        if (application != null) {
        	
        	int seed = random.nextInt();
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
            List<String> names = new ArrayList<String>();
            
            Agent userAgent = AgentFactory.buildAgent(AgentTypes.randomType());
            
            names.add(String.format("%s 1", userAgent.getName()));
            for(int i = 0; i != numAI-1; ++i){
                Agent agent = AgentFactory.buildAgent(AgentTypes.randomType());
                AgentPlayer ai = new AgentPlayer(agent);
                players.add(ai);
                names.add(String.format("%s %d", agent.getName(), i + 2));
            }
            String nameSummary = "Players were ";
            for(String s : names){
                nameSummary += String.format("%s, ", s);
            }
            nameSummary = nameSummary.substring(0, nameSummary.length() - 2);
            writer.println(nameSummary); 
            writer.flush();
        	
        	
        	application.goToGameTest(players, null, null);
        }
    }
}
