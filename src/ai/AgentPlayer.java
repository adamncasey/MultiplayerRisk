package ai;

import java.util.List;

import ai.agents.Agent;
import logic.move.Move;
import logic.move.MoveChecker;
import logic.state.Board;
import logic.state.Player;
import player.IPlayer;
import networking.LocalPlayerHandler;

public class AgentPlayer implements IPlayer {

    private Agent controller;

    private LocalPlayerHandler handler;
    private Player player;

    public AgentPlayer(Agent controller){
        this.controller = controller;
    }

    public void setup(Player player, List<String> names, Board board, MoveChecker checker, LocalPlayerHandler handler){
        this.controller.setup(player, board);
        this.handler = handler;
        this.player = player;
    }

    public void nextMove(String move){
    }

    public void updatePlayer(Move move){
        if(move.getUID() == player.getUID()){
            handler.sendMove(move);
        }
    }

    public void getMove(Move move){
        if(move.getStage() == Move.Stage.ROLL_HASH || move.getStage() == Move.Stage.ROLL_NUMBER){
            handler.handleRoll(move);
        }else{
            controller.getMove(move);
        }
    }
}
