package player;

import logic.*;
import java.util.ArrayList;

/**
 * IPlayer --- The common interface between players and game logic.
 */
public interface IPlayer {
    // IPlayer implementations must be able to respond to each of the following requests
    // Trade In Cards
    // Deploy New Armies
    // Attack OR Fortify
    // Draw Card
    // IPlayers must also be able to respond appropriately if an opposing player attacks them (A Defend request will be called).


    // For move functions, i.e. tradeInCards, placeArmies etc. Game will check if a move is valid and not allow it otherwise.

    public int getUID();
    public void setUID(int uid);

    // updatePlayer -- need a method to give player info about their hand and the board

    public void updatePlayer(Board board, ArrayList<Card> hand);

    public ArrayList<Card> tradeInCards(String requestMessage);

    // return value should be a list of 2 ints, the first int should be the ID of the territory, the second should be the number of armies to be placed
    // this will be requested until all armies have been placed
    public ArrayList<Integer> placeArmies(String requestMessage, int armiesToPlace);
//    public GameMove get2DeployNewArmies(){

//    public GameMove get3Attack(){

//    public GameMove get3Fortify(){

//    public GameMove get4DrawCard(){

//    public GameMove respond3Defend(){

}
