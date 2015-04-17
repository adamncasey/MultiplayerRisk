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
        	
            List<IPlayer> players = new ArrayList<IPlayer>();

            int nextPlayerID = 1;

            players.add(new AgentPlayer(AgentFactory.buildAgent(AgentTypes.Type.ANGRY), nextPlayerID++));
            players.add(new AgentPlayer(AgentFactory.buildAgent(AgentTypes.Type.ANGRY), nextPlayerID++));

            Board board = new Board();
            Deck deck = board.getDeck();
            
        	application.goToGameTest(new ArrayList<IPlayer>(), players, deck);
        }
    }
}
