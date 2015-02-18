package player;

import logic.*;
import java.util.ArrayList;

/**
 * IPlayer --- The common interface between players and game logic.
 */
public interface IPlayer {
    // ---
    // if you are implementing this, and would like more information to be passed to IPlayer for one of the methods, or in updatePlayers, it should be easy to add
    // ---

    // For every move function here, i.e. everything from claimTerritory to chooseFortifyArmies. Game will check if a move is valid and not allow it otherwise.
    // the requestMessage on the subsequent calls will let you know that an invalid move was made

    public int getUID();
    public void setUID(int uid);

    public boolean isEliminated();
    public void eliminate();

    // updatePlayer -- need a method to give player info about their hand and the board
    // currentStage lets the IPlayer know what the current player is about to do
    public void updatePlayer(Board board, ArrayList<Card> hand, int currentPlayer, String currentStage);

    // Pick an empty territory to add 1 army to
    public int claimTerritory(String requestMessage);

    // Pick one of your territories to add 1 army to
    public int reinforceTerritory(String requestMessage, int uid);

    // return value is a list of the cards to be traded in, Game will check that these cards make a valid set, return an empty list to trade nothing
    public ArrayList<Card> tradeInCards(String requestMessage);

    // return value should be a list of 2 ints, the first int should be the ID of the territory, the second should be the number of armies to be placed
    // this will be requested until all armies have been placed
    public ArrayList<Integer> placeArmies(String requestMessage, int armiesToPlace);

    // true = yes this player wants to attack, false = do not attack
    // after this request either an attack will commence, or the game will move on
    // after the attack this will be requested again until the player no longer wants to attack
    // players will not be asked if they want to attack, if it is not possible for them to attack on the current board
    public boolean decideAttack(String requestMessage);

    // return valie should be a list of 2 ints, the first int should be the ID of the territory you are attacking from, the second should be the id of the territory being attacked
    // the territories must be adjacent (Game will enforce this)
    public ArrayList<Integer> startAttack(String requestMessage);

    // 1, 2, or 3 dice
    public int chooseAttackingDice(String requestMessage);

    // 1 or 2 dice
    public int chooseDefendingDice(String requestMessage);

    // return value should be an array list of ints, one int for each dice roll, each int should be between 1 and 6
    public ArrayList<Integer> rollDice(String requestMessage, int numDice);

    // return value should be the number of armies to move from attacking territory to defending territory.
    // It has to be at least numDice, and you must leave 1 army at the attacking territory (Game will check and enforce this)
    public int occupyTerritory(String requestMessage, int currentArmies, int numDice);

    // true = yes this player wants to fortify, false = do not fortify
    // players only get one opportunity to fortify per turn
    // players will not be asked if they want to fortify, if it is not possible for them to fortify on the current board
    public boolean decideFortify(String requestMessage);

    // Same format as startAttack, but both territories should be owned by the player (and adjacent)
    public ArrayList<Integer> startFortify(String requestMessage);

    // return value should be the number of armies to fortify with, leaving at least 1 behind
    public int chooseFortifyArmies(String requestMessage, int currentArmies);
}
