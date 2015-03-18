package ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import logic.Card;
import logic.move.Move;
import logic.move.Move.Stage;
import logic.move.WrongMoveException;
import logic.state.Board;
import logic.state.Player;
import player.PlayerController;

/**
 * DumbAI --- Randomly decides what to do, will drag out games forever if there are no smarter players in the game.
 */
public class DumbAI implements PlayerController {
    private static Random random = new Random();

    private Player player;
    private Board board;

    public DumbAI(){
    }

    public void setup(Player player, Board board){
        this.player = player;
        this.board = board;
    }

    public void getMove(Move move){
        try{
            switch(move.getStage()){
                case CLAIM_TERRITORY:
                    claimTerritory(move);
                    return;
                case REINFORCE_TERRITORY:
                    reinforceTerritory(move);
                    return;
                case TRADE_IN_CARDS:
                    tradeInCards(move);
                    return;
                case PLACE_ARMIES:
                    placeArmies(move);
                    return;
                case DECIDE_ATTACK:
                    decideAttack(move);
                    return;
                case START_ATTACK:
                    startAttack(move);
                    return;
                case CHOOSE_ATTACK_DICE:
                    chooseAttackingDice(move);
                    return;
                case CHOOSE_DEFEND_DICE:
                    chooseDefendingDice(move);
                    return;
                case OCCUPY_TERRITORY:
                    occupyTerritory(move);
                    return;
                case DECIDE_FORTIFY:
                    decideFortify(move);
                    return;
                case START_FORTIFY:
                    startFortify(move);
                    return;
                case FORTIFY_TERRITORY:
                    chooseFortifyArmies(move);
                    return;
                default:
                    return;
            }
        }catch(WrongMoveException e){
            System.out.println("DumbAI is not choosing a move correctly");
            System.out.println(e.getMessage());
            return;
        }
    }

    private void claimTerritory(Move move) throws WrongMoveException{
        int tid = random.nextInt(board.getNumTerritories());
        while(board.getOwner(tid) != -1){
            tid = random.nextInt(board.getNumTerritories());
        }

        move.setTerritory(tid);
    }

    private void reinforceTerritory(Move move) throws WrongMoveException{
        int uid = move.getUID();
        int tid = random.nextInt(board.getNumTerritories());
        while(board.getOwner(tid) != uid){
            tid = random.nextInt(board.getNumTerritories());
        }

        move.setTerritory(tid);
    }

    private void tradeInCards(Move move) throws WrongMoveException{ 
        List<Card> toTradeIn = new ArrayList<Card>();
        List<Card> hand = player.getHand();
        if(hand.size() >= 5){
            for(int i = 0; i != 3; ++i){
                int randomCard = random.nextInt(hand.size());
                Card c = hand.get(randomCard);
                toTradeIn.add(c);
            } 
        }

        move.setToTradeIn(toTradeIn);
    }

    private void placeArmies(Move move) throws WrongMoveException{
        int uid = move.getUID();
        int armiesToPlace = move.getCurrentArmies();

        int randomTerritory = random.nextInt(board.getNumTerritories());
        while(board.getOwner(randomTerritory) != uid){
            randomTerritory = random.nextInt(board.getNumTerritories());
        }
        int randomArmies = random.nextInt(armiesToPlace+1); // Can't place 0 armies

        move.setTerritory(randomTerritory);
        move.setArmies(randomArmies);
    }

    private void decideAttack(Move move) throws WrongMoveException{
        move.setDecision(random.nextBoolean());
    }

    private void startAttack(Move move) throws WrongMoveException{
        int uid = move.getUID();
        int randomAlly = random.nextInt(board.getNumTerritories());
        while((board.getOwner(randomAlly) != uid) || board.getArmies(randomAlly) < 2){
            randomAlly = random.nextInt(board.getNumTerritories());
        }
        List<Integer> adjacents = board.getLinks(randomAlly);
        int randomEnemy = adjacents.get(random.nextInt(adjacents.size()));

        move.setFrom(randomAlly);
        move.setTo(randomEnemy);
    }

    private void chooseAttackingDice(Move move) throws WrongMoveException{
        move.setAttackDice(random.nextInt(3)+1);
    }

    private void chooseDefendingDice(Move move) throws WrongMoveException{
        move.setDefendDice(random.nextInt(2)+1);
    }

    private void occupyTerritory(Move move) throws WrongMoveException{
        int currentArmies = move.getCurrentArmies();
        move.setArmies(random.nextInt(currentArmies));
    }

    private void decideFortify(Move move) throws WrongMoveException{
        move.setDecision(random.nextBoolean());
    }

    private void startFortify(Move move) throws WrongMoveException{
        int uid = move.getUID();
        int randomAlly = 0;
        randomAlly = random.nextInt(board.getNumTerritories());
        while(board.getOwner(randomAlly) != uid){
            randomAlly = random.nextInt(board.getNumTerritories());
        }
        List<Integer> adjacents = board.getLinks(randomAlly);
        int randomFortify = adjacents.get(random.nextInt(adjacents.size()));

        move.setFrom(randomAlly);
        move.setTo(randomFortify);
    }

    private void chooseFortifyArmies(Move move) throws WrongMoveException{
        move.setArmies(random.nextInt(move.getCurrentArmies()));
    }
}

