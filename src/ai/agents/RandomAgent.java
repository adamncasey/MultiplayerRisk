package ai.agents;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ai.IAgent;
import logic.Card;
import logic.move.Move;
import logic.move.Move.Stage;
import logic.state.Board;
import logic.state.Player;

/**
 * RandomAgent --- Randomly decides what to do, will drag out games forever if there are no smarter players in the game.
 */
public class RandomAgent implements IAgent {
    private static Random random = new Random();

    private Player player;
    private Board board;

    public RandomAgent(){
    }

    public String getName(){
        return "Random";
    }

    public String getDescription(){
        return "Randomly decides what to do, games with only random players can take a long time.";
    }

    public void setup(Player player, Board board){
        this.player = player;
        this.board = board;
    }

    public void getMove(Move move){
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
    }

    private void claimTerritory(Move move){
        int tid = random.nextInt(board.getNumTerritories());
        while(board.getOwner(tid) != -1){
            tid = random.nextInt(board.getNumTerritories());
        }

        move.setTerritory(tid);
    }

    private void reinforceTerritory(Move move){
        int uid = move.getUID();
        int tid = random.nextInt(board.getNumTerritories());
        while(board.getOwner(tid) != uid){
            tid = random.nextInt(board.getNumTerritories());
        }

        move.setTerritory(tid);
    }

    private void tradeInCards(Move move){
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

    private void placeArmies(Move move){
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

    private void decideAttack(Move move){
        move.setDecision(random.nextBoolean());
    }

    private void startAttack(Move move){
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

    private void chooseAttackingDice(Move move){
        move.setAttackDice(random.nextInt(3)+1);
    }

    private void chooseDefendingDice(Move move){
        move.setDefendDice(random.nextInt(2)+1);
    }

    private void occupyTerritory(Move move){
        int currentArmies = move.getCurrentArmies();
        move.setArmies(random.nextInt(currentArmies));
    }

    private void decideFortify(Move move){
        move.setDecision(random.nextBoolean());
    }

    private void startFortify(Move move){
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

    private void chooseFortifyArmies(Move move){
        move.setArmies(random.nextInt(move.getCurrentArmies()));
    }
}

