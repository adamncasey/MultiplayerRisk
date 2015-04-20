package ai.agents;

import ai.strategy.AggressiveStrategy;
import ai.strategy.RandomStrategy;
import ai.strategy.CardsStrategy;
import ai.strategy.ContinentsStrategy;
import ai.strategy.SmartAggressiveStrategy;
import ai.strategy.ControlledStrategy;
import logic.move.Move;
import logic.state.Board;
import logic.state.Player;

public class SteadyAgent extends Agent {

    private AggressiveStrategy as;
    private RandomStrategy rs;
    private CardsStrategy cs;
    private ContinentsStrategy cos;
    private SmartAggressiveStrategy sas;
    private ControlledStrategy crt;

    public SteadyAgent(){
        super("Steady");
    }

    public void setup(Player player, Board board){
        super.setup(player, board);
        as = new AggressiveStrategy(player, board, random);
        rs = new RandomStrategy(player, board, random);
        cs = new CardsStrategy(player, board, random);
        cos = new ContinentsStrategy(player, board, random);
        sas = new SmartAggressiveStrategy(player, board, random);
        crt = new ControlledStrategy(player, board, random);
    }

    public String getDescription(){
        return "Reinforces its borders.";
    }

    public void getMove(Move move){
        switch(move.getStage()){
            case CLAIM_TERRITORY:
                cos.getMove(move);
                return;
            case REINFORCE_TERRITORY:
                rs.getMove(move);
                return;
            case TRADE_IN_CARDS:
                cs.getMove(move);
                return;
            case PLACE_ARMIES:
                crt.getMove(move);
                return;
            case DECIDE_ATTACK:
                sas.getMove(move);
                return;
            case START_ATTACK:
                sas.getMove(move);
                return;
            case CHOOSE_ATTACK_DICE:
                as.getMove(move);
                return;
            case CHOOSE_DEFEND_DICE:
                as.getMove(move);
                return;
            case OCCUPY_TERRITORY:
                as.getMove(move);
                return;
            case DECIDE_FORTIFY:
                as.getMove(move);
                return;
            case START_FORTIFY:
                as.getMove(move);
                return;
            case FORTIFY_TERRITORY:
                as.getMove(move);
                return;
            default:
                assert false : move.getStage();
        }
    }
}

