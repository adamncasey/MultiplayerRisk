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

    public int getUID();
    public void setUID(int uid);

    public ArrayList<Card> tradeInCards(ArrayList<Card> hand, String requestMessage);

//    public GameMove get2DeployNewArmies(){

//    public GameMove get3Attack(){

//    public GameMove get3Fortify(){

//    public GameMove get4DrawCard(){

//    public GameMove respond3Defend(){

}
