package ui.menu;

import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import player.IPlayer;
import ai.AgentFactory;
import ai.AgentPlayer;
import ai.AgentTypes;
import ai.agents.Agent;
import logic.state.Deck;
import logic.state.Board;
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
    
    public void gametest(ActionEvent event) {
        if (application != null) {
        	
            PrintWriter writer = new PrintWriter(System.out);

            int numAI = 2;
            
            writer.format("Loading game with %d AIs\n", numAI);
            List<IPlayer> players = new ArrayList<IPlayer>();
            List<String> names = new ArrayList<String>();
            
            int nextPlayerID = 0;
            
            Agent userAgent = AgentFactory.buildAgent(AgentTypes.Type.FURIOUS);
            
            names.add(String.format("%s 1", userAgent.getName()));
            players.add(new AgentPlayer(userAgent, nextPlayerID++));
            
            for(int i = 0; i != numAI-1; ++i){
                Agent agent = AgentFactory.buildAgent(AgentTypes.Type.GREEDY);
                AgentPlayer ai = new AgentPlayer(agent, nextPlayerID++);
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

            Board board = new Board();
            Deck deck = board.getDeck();        	
        	application.goToGameTest(players, null, deck);
        }
    }
}
