package logic;

import java.util.ArrayList;
import java.util.Random;

import player.IPlayer;
import networking.Message;

/**
 * game --- the main game loop that lets each player take their turn, it links player moves to the gamestate.
 * @author Nathan Blades
 */
public class Game {

   /**
    * The list of IPlayers currently playing the game.
    */
    private ArrayList<IPlayer> players;

   /**
    * The current game state, it is updated by Game after each player confirms the move.
    */
    private GameState state;

   /**
    * Configure the game for a specific set of players and settings.
    * @param players The list of IPlayers that will play the game
    */
    public Game(ArrayList<IPlayer> players, GameState state){
        this.players = new ArrayList<IPlayer>(players);
        this.state = state;
    }

    /**
     * Setup the game, performing initial army placements (among other things)
     * @return No return value
     */
    public void setupGame(){
        // Setup here
    }

   /**
    * Play the game, players take turns until there is a winner.
    * @return No return value
    */
    public void playGame(){
        Random random = new Random();

        int activePlayer = 0;
        int numPlayers = players.size();
        while(true){
            // The activePlayer takes their turn
       	    turn(players.get(activePlayer));

            // Code to remove eliminated players goes here
            // Here is an example, this game randomly removes the active player after their turn/
            int randInt = random.nextInt(10);
            if(randInt == 0){
                players.remove(activePlayer);
                numPlayers = players.size();
            } 

            // End the game when there is 1 player left
            if(numPlayers == 1){
                break;
            }

            // Increment activePlayer
            activePlayer = (activePlayer + 1) % numPlayers;   
        }
    }

    private void turn(IPlayer activePlayer){
        // Current protocol has 4 stages per turn
        for(int i = 0; i != 4; ++i){
            Message move = activePlayer.getMove(i);
            boolean legal = true; // Is the move legal?
            for(IPlayer p : players){
                // Don't confirm with the player who made the move
                if(p == activePlayer){ // Reference equality should work
                    continue;
                }
                boolean confirmation = p.confirmMove(i, move);
                legal = legal && confirmation;
            }
            if(legal){
                state.update(move);
            } else {
                // What do we do when a move isn't legal?
            }
        }
    }
}
