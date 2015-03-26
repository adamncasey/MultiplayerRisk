package ai;

import java.util.List;

import ai.agents.Agent;
import logic.move.Move;
import logic.move.MoveChecker;
import logic.state.Board;
import logic.state.Player;
import player.IPlayer;

public class AgentPlayer implements IPlayer {

    private Agent controller;

    public AgentPlayer(Agent controller){
        this.controller = controller;
    }

    public void setup(Player player, List<String> names, Board board, MoveChecker checker){
        this.controller.setup(player, board);
    }

    public void nextMove(String move){
    }

    public void updatePlayer(Move move){
    }

    public void getMove(Move move){
        controller.getMove(move); 
    }
}
