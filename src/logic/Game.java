package logic;

import java.util.ArrayList;

import player.IPlayer;
import networking.Message;

/**
 * Game --- The main game loop that lets each player take their turn, it links player moves to the GameState.
 * @author Nathan Blades
 */
public class Game {

   /**
    * The list of IPlayers currently playing the game.
    */
    private ArrayList<IPlayer> players;


   /**
    * Configure the game for a specific set of players and settings.
    * @param players The list of IPlayers that will play the game
    * @return No return value
    */
    public void initialize(ArrayList<IPlayer> players){
        this.players = new ArrayList<IPlayer>(players);
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
        boolean play = true;
        int activePlayer = 0;
        int numPlayers = players.size();
        while(play){
       	    turn(players.get(activePlayer));
            activePlayer = (activePlayer + 1) % numPlayers;
            // Code to remove eliminated players goes here
            // Check that there is more than 1 player left (or end game)
        }
    }

    private void turn(IPlayer activePlayer){
        // Current protocol has 4 stages per turn
        for(int i = 0; i != 4; ++i){
            Message move = activePlayer.getMove(i);
            for(IPlayer p : players){
                // Don't confirm with the player who made the move
                if(p.getId().equals(activePlayer.getId())){
                    continue;
                }
                boolean legal = p.confirmMove(i, move);
                if(!legal){
                    // ??????????
                }
            }
        }
    }
}
