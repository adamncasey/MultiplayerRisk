package logic;

import java.util.ArrayList;
import java.util.Random;

import player.*;

/**
 * game --- the main game loop that lets each player take their turn, it links player moves to the gamestate.
 */
public class Game {

    private ArrayList<IPlayer> playerInterfaces;

    private Board board;
    private Deck deck;
    private ArrayList<ArrayList<Card>> playerHands;

    int setCounter = 0;
    int armyReward = 4;
    int setValues[] = {4, 6, 8, 10, 12, 15};

    public Game(ArrayList<IPlayer> playerInterfaces, int seed, String boardFilename){
        this.playerInterfaces = new ArrayList<IPlayer>();
        this.playerHands = new ArrayList<ArrayList<Card>>();
        for(int i = 0; i != playerInterfaces.size(); ++i){
            IPlayer pi = playerInterfaces.get(i);
            pi.setUID(i);
            this.playerInterfaces.add(pi);
            this.playerHands.add(new ArrayList<Card>());
        }
        this.board = new Board(boardFilename);
        this.deck = board.getDeck();
        this.deck.shuffle(seed);
    }

    public void setupGame(){
        // Setup here
    }

   /**
    * Play the game, players take turns until there is a winner.
    * @return No return value
    */
    public void playGame(){

        playerTurn(0);
    }

    private void updatePlayers(){
        for(IPlayer p : playerInterfaces){
            p.updatePlayer(board, playerHands.get(p.getUID()));
        }
    }

    private void playerTurn(int uid){
        // Whenever the game state changes, update all players.
        updatePlayers();

        IPlayer playerInterface = playerInterfaces.get(uid);
        ArrayList<Card> hand = new ArrayList<Card>(playerHands.get(uid)); // create a copy because hand may be edited by the check function
        ArrayList<Card> toTradeIn = playerInterface.tradeInCards("Trade in cards");
        while(!checkTradeInCards(hand, toTradeIn)){
            toTradeIn = playerInterface.tradeInCards("Invalid selection");
        }
        boolean traded = tradeInCards(uid, toTradeIn); 

        updatePlayers();

        int armies = calculatePlayerArmies(uid, traded, toTradeIn);

        while(armies != 0){
            ArrayList<Integer> move = playerInterface.placeArmies("Place your armies", armies);
            while(!checkPlaceArmies(uid, move, armies)){
                move = playerInterface.placeArmies("Invalid selection", armies);
            }
            armies = placeArmies(uid, move, armies);
            updatePlayers();
        }
    }

    public static boolean checkTradeInCards(ArrayList<Card> hand, ArrayList<Card> toTradeIn){
        if(toTradeIn.size() == 0 && hand.size() < 5){
            return true;
        }
        if(Card.containsSet(hand)){
            if(toTradeIn.size() == 3 && Card.isSubset(toTradeIn, hand)){
                return true;
            }
        }
        return false;
    }

    public boolean tradeInCards(int uid, ArrayList<Card> toTradeIn){
        ArrayList<Card> hand = playerHands.get(uid);
        for(Card c: toTradeIn){
            hand.remove(c);
        }
        if(toTradeIn.size() == 3){
            return true;
        }
        return false;
    }

    public int calculatePlayerArmies(int uid, boolean traded, ArrayList<Card> toTradeIn){
        int armies = 0;

        armies += board.calculatePlayerTerritoryArmies(uid);
        armies += board.calculatePlayerContinentArmies(uid);

        if(traded){
            armies += incrementSetCounter();
        }

        int extraArmies = 0;
        for(Card card : toTradeIn){
            if(board.checkTerritoryOwner(uid, card.getID())){
                extraArmies = 2;
            }
        }
        armies += extraArmies;

        return armies;
    }

    // returns the number of armies rewarded for trading in the set
    public int incrementSetCounter(){
        int reward = armyReward;
        setCounter++;
        if(setCounter > setValues.length-1){
            armyReward = setValues[setValues.length-1] + (5 * (setCounter - (setValues.length-1)));
        } else {
            armyReward = setValues[setCounter];
        }
        return reward; 
    }

    public boolean checkPlaceArmies(int uid, ArrayList<Integer> move, int armies){
        if(move.get(1) > armies){
            return false;
        }
        if(!board.checkTerritoryOwner(uid, move.get(0))){
            return false;
        }
        return true;
    }

    public int placeArmies(int uid, ArrayList<Integer> move, int armies){
        Territory t = board.getTerritories().get(move.get(0));
        t.addArmies(move.get(1));
        return armies - move.get(1);
    }
}
