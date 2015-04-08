package ai.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import logic.Card;
import logic.move.Move;
import logic.state.Board;
import logic.state.Player;

/**
 * PassiveStrategy
 */
public class PassiveStrategy extends Strategy {

    public PassiveStrategy(Player player, Board board, Random random){
        super(player, board, random);
    }

    public void getMove(Move move){
        switch(move.getStage()){
            case TRADE_IN_CARDS:
                tradeInCards(move);
                return;
            case DECIDE_ATTACK:
                decide(move);
                return;
            case CHOOSE_DEFEND_DICE:
                chooseDefendingDice(move);
                return;
            case DECIDE_FORTIFY:
                decide(move);
                return;
            default:
                assert false : move.getStage();
        }
    }

    private void decide(Move move){
        move.setDecision(false);
    }

    private void tradeInCards(Move move){
        List<Card> hand = player.getHand();
        List<Card> toTradeIn = new ArrayList<Card>();
        if(hand.size() >= 5){
            for(int i = 0; i != 3; ++i){
                Card c = hand.get(random.nextInt(hand.size()));
                toTradeIn.add(c);
            } 
        }
        move.setToTradeIn(toTradeIn);
    }

    private void chooseDefendingDice(Move move){
        move.setDefendDice(1);
    }
}

