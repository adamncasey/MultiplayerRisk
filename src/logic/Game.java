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
    private ArrayList<PlayerState> playerStates;


    public Game(ArrayList<IPlayer> playerInterfaces, int seed, String boardFilename){
        this.playerInterfaces = new ArrayList<IPlayer>();
        this.playerStates = new ArrayList<PlayerState>();
        for(int i = 0; i != playerInterfaces.size(); ++i){
            IPlayer pi = playerInterfaces.get(i);
            pi.setUID(i);
            this.playerInterfaces.add(pi);
            this.playerStates.add(new PlayerState(i));
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

    private void playerTurn(int uid){
        IPlayer playerInterface = playerInterfaces.get(uid);
        PlayerState playerState = playerStates.get(uid);
        ArrayList<Card> hand = playerState.getHand();
        ArrayList<Card> toTradeIn = playerInterface.tradeInCards(hand, "Trade in cards");
        while(!checkTradeInCards(hand, toTradeIn)){
            toTradeIn = playerInterface.tradeInCards(hand, "Invalid selection");
        }
        makeTradeInCards(uid, toTradeIn); 
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

    public static void makeTradeInCards(int uid, ArrayList<Card> toTradeIn){
     
    }
}
