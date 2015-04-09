package ai.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import logic.Card;
import logic.move.Move;
import logic.state.Board;
import logic.state.Player;

/**
 * CardsStrategy
 */
public class CardsStrategy extends Strategy {

    private int bestFrom;
    private int bestTo;

    public CardsStrategy(Player player, Board board, Random random){
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
            case START_ATTACK:
                attack(move);
                return;
            default:
                assert false : move.getStage();
        }
    }

    // Trade in cards immediately
    private void tradeInCards(Move move){
        List<Card> hand = player.getHand();
        List<Card> toTradeIn = new ArrayList<Card>();
        if(Card.containsSet(hand)){
            for(int i = 0; i != 3; ++i){
                Card c = hand.get(random.nextInt(hand.size()));
                toTradeIn.add(c);
            } 
        }
        move.setToTradeIn(toTradeIn);
        bestFrom = -1; bestTo = -1; // Must happen for decide to work
    }

    private void decide(Move move){
        // If an attack hasn't been chosen this turn, try to find one
        if(bestFrom == -1 || bestTo == -1){
            decideAttack(move);
            return;
        // If the current attack has failed or looks like it is going to faill, try to find another
        }else if(board.getArmies(bestFrom) == 1 || board.getArmies(bestFrom) < board.getArmies(bestTo)){
            decideAttack(move);
            return;
        // If the current attack was successful, do not attack as a card will be drawn
        }else if(board.getOwner(bestTo) == player.getUID()){
            move.setDecision(false);
        // Continue attacking otherwise
        }else{
            move.setDecision(true);
        }
    }

    private void decideAttack(Move move){
        int result = pickBestAttack();
        // If there are no promising attacks, do not attack
        if(result < 0){
            move.setDecision(false);
        }else{
            move.setDecision(true);
        }
    }

    /**
     * pickBestAttack --- Calculate a score for each possible attack, and choose the best one.
     * The best attack will the biggest positive difference between attacking armies and defending armies.
     */
    private int pickBestAttack(){
        int bestScore = -Integer.MAX_VALUE;
        int uid = player.getUID();
        for(int i = 0; i != board.getNumTerritories(); ++i){
            if(board.getOwner(i) != uid || board.getArmies(i) == 1){
                continue;
            }
            for(int j : board.getLinks(i)){
                if(board.getOwner(j) == uid){
                    continue;
                }
                int score = board.getArmies(i) - board.getArmies(j);
                if(score > bestScore){
                    bestScore = score;
                    bestFrom = i;
                    bestTo = j;
                }            
            }
        }
        return bestScore;
    }

    private void attack(Move move){
        move.setFrom(bestFrom);
        move.setTo(bestTo);
// System.out.format("%d %d %d %d %d %d\n", bestFrom, board.getArmies(bestFrom), board.getOwner(bestFrom), bestTo, board.getArmies(bestTo), board.getOwner(bestTo));
    }
}

