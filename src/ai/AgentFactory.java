package ai;

import ai.Agents.Type;
import player.PlayerController;

public class AgentFactory {

    public static IAgent buildAgent(Type type){
        switch(type){
            case RANDOM:
                return new RandomAgent();
            case SIMPLE:
                return new SimpleAgent();
            default:
                assert false : type;
        }
        return null;
    }
}
