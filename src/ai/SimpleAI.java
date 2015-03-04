package ai;

import logic.Player;
import logic.Board;
import logic.Card;
import logic.Move;
import logic.Move.Stage;
import logic.WrongMoveException;
import player.PlayerController;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * SimpleAI --- Takes moves with the aim of ending the game quickly.
 */
public class SimpleAI implements PlayerController {
    private static Random random = new Random();

    private Player player;
    private Board board;

    public SimpleAI(){
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
            System.out.println("SimpleAI is not choosing a move correctly");
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
        move.setDecision(true);
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
        int numArmies = board.getArmies(move.getFrom());
        int decision = 1;
        if(numArmies > 4){
            decision = 3;
        }else if(numArmies > 3){
            decision = 2;
        }
        move.setAttackDice(decision);
    }

    private void chooseDefendingDice(Move move) throws WrongMoveException{
        int numArmies = board.getArmies(move.getTo());
        int decision = 1;
        if(numArmies > 1){
            decision = 2;
        }
        move.setDefendDice(decision);
    }

    private void occupyTerritory(Move move) throws WrongMoveException{
        int currentArmies = move.getCurrentArmies();
        int numDice = move.getAttackDice();
        int decision = currentArmies-1;
        move.setArmies(decision);
    }

    // This AI only fortifies when one of it's territories has no adjacent enemies
    private void decideFortify(Move move) throws WrongMoveException{
        int uid = move.getUID();
        for(int i = 0; i != board.getNumTerritories(); ++i){
            if(board.getOwner(i) != uid || board.getArmies(uid) < 2){
                continue;
            }
            List<Integer> adjacents = board.getLinks(i);
            int enemyCounter = 0;
            for(int j : adjacents){
                if(board.getOwner(j) != uid){
                    enemyCounter++;
                }
            }
            if(enemyCounter == 0){
                move.setDecision(true);
                return;
            }
        }
        move.setDecision(false);
    }

    private void startFortify(Move move) throws WrongMoveException{
        int uid = move.getUID();
        int randomAlly = 0;;
        int enemyCounter = -1;
        List<Integer> adjacents = new ArrayList<Integer>();
        while(enemyCounter != 0){
            randomAlly = random.nextInt(board.getNumTerritories());
            while(board.getOwner(randomAlly) != uid){
                randomAlly = random.nextInt(board.getNumTerritories());
            }
            adjacents = board.getLinks(randomAlly);
            enemyCounter = 0;
            for(int i : adjacents){
                if(board.getOwner(i) != uid){
                    enemyCounter++;
                }
            }
        }
        int randomFortify = adjacents.get(random.nextInt(adjacents.size()));
        while(board.getOwner(randomFortify) != uid){
            randomFortify = adjacents.get(random.nextInt(adjacents.size()));
        }
        move.setFrom(randomAlly);
        move.setTo(randomFortify);
    }

    private void chooseFortifyArmies(Move move) throws WrongMoveException{
        move.setArmies(move.getCurrentArmies()-1);
    }
}
