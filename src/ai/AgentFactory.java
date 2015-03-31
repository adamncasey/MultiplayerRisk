package ai;

import ai.agents.Agent;
import ai.agents.PassiveAgent;
import ai.agents.RandomAgent;
import ai.agents.AngryAgent;
import ai.agents.GreedyAgent;
import ai.agents.ContinentalAgent;
import ai.agents.FuriousAgent;
import ai.AgentTypes.Type;
import player.PlayerController;

public class AgentFactory {

    public static Agent buildAgent(Type type){
        switch(type){
//            case PASSIVE:
//                return new PassiveAgent();
//            case RANDOM:
//                return new RandomAgent();
            case ANGRY:
                return new AngryAgent();
            case GREEDY:
                return new GreedyAgent();
            case CONTINENTAL:
                return new ContinentalAgent();
            case FURIOUS:
                return new FuriousAgent();
            default:
                assert false : type;
        }
        return null;
    }
}
