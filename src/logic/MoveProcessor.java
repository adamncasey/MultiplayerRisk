package logic;

import logic.Move.Stage;

import java.util.*;


/**
 * MoveProcessor --- Takes a move and returns a message describing what just happened.
 */
public class MoveProcessor {

    public MoveProcessor(){
    }

    public static String processMove(int uid, Move move, Board board){
        try{
            String message;
            switch(move.getStage()){
                case CLAIM_TERRITORY:
                    int claimedTerritory = move.getTerritory();
                    String claimedTerritoryName = board.getName(claimedTerritory);
                    message = String.format("Player %d has claimed territory [%d-%s].\n", uid, claimedTerritory, claimedTerritoryName);
                    return message;
                case REINFORCE_TERRITORY:
                    int reinforcedTerritory = move.getTerritory();
                    String reinforcedTerritoryName = board.getName(reinforcedTerritory);
                    message = String.format("Player %d has reinforced territory [%d-%s].\n", uid, reinforcedTerritory, reinforcedTerritoryName);
                    return message;
                case TRADE_IN_CARDS:
                    List<Card> toTradeIn = move.getToTradeIn();
                    String handMessage = Card.printHand(null, toTradeIn);
                    message = String.format("Player %d has traded in %s", uid, handMessage);
                    return message;
                case PLACE_ARMIES:
                    int placeArmiesTerritory = move.getTerritory();
                    int placeArmiesNum = move.getArmies();
                    String placeArmiesName = board.getName(placeArmiesTerritory);
                    message = String.format("Player %d has placed %d armies at [%d-%s].\n", uid, placeArmiesNum, placeArmiesTerritory, placeArmiesName); 
                    return message;
                case DECIDE_ATTACK:
                    boolean decideAttack = move.getDecision();
                    if(decideAttack){
                        message = String.format("Player %d has chosen to attack.\n", uid);
                    }else{
                        message = String.format("Player %d has chosen not to attack.\n", uid);
                    }
                    return message;
                case START_ATTACK:
                    int attackFrom = move.getFrom();
                    int attackTo = move.getTo();
                    String attackFromName = board.getName(attackFrom);
                    String attackToName = board.getName(attackTo);
                    message = String.format("Player %d is attacking [%d-%s] from [%d-%s].\n", uid, attackTo, attackToName, attackFrom, attackFromName);
                    return message;
                case CHOOSE_ATTACK_DICE:
                    int numAttackingDice = move.getAttackDice();
                    message = String.format("Player %d has chosen to attack with %d dice.\n", uid, numAttackingDice);
                    return message;
                case CHOOSE_DEFEND_DICE:
                    int numDefendingDice = move.getDefendDice();
                    message = String.format("Player %d has chosen to defend with %d dice.\n", uid, numDefendingDice);
                    return message;
                case OCCUPY_TERRITORY:
                    int numOccupyArmies = move.getArmies();
                    message = String.format("Player %d was successful in their attack and has moved %d armies forward.\n", uid, numOccupyArmies);
                    return message;
                case DECIDE_FORTIFY:
                    boolean decideFortify = move.getDecision();
                    if(decideFortify){
                        message = String.format("Player %d has chosen to fortify.\n", uid);
                    }else{
                        message = String.format("Player %d has chosen not to fortify.\n", uid);
                    }
                    return message;
                case START_FORTIFY:
                    int fortifyFrom = move.getFrom();
                    int fortifyTo = move.getTo();
                    String fortifyFromName = board.getName(fortifyFrom);
                    String fortifyToName = board.getName(fortifyTo);
                    message = String.format("Player %d is fortifying [%d-%s] from [%d-%s].\n", uid, fortifyTo, fortifyToName, fortifyFrom, fortifyFromName);
                    return message;
                case FORTIFY_TERRITORY:
                    int numFortifyArmies = move.getArmies();
                    message = String.format("Player %d has fortified with %d armies.\n", uid, numFortifyArmies);
                    return message;
                case END_ATTACK:
                    int attackerLosses = move.getAttackerLosses();
                    int defenderLosses = move.getDefenderLosses();
                    message = String.format("The attacker lost %d armies, the defender lost %d armies.\n", attackerLosses, defenderLosses);
                    return message;
                case PLAYER_ELIMINATED:
                    int eliminatedPlayer = move.getPlayer();
                    message = String.format("Player %d has just been eliminated by player %d.\n", eliminatedPlayer, uid);
                    return message;
                case CARD_DRAWN:
                    message = String.format("Player %d has drawn a card.\n", uid);
                    return message;
                case SETUP_BEGIN:
                    message = String.format("Game setup is beginning with %d players.\n", uid);
                    return message;
                case SETUP_END:
                    return "Game setup has ended.\n";
                case GAME_BEGIN:
                    message = String.format("Game is beginning, player %d is first to go.\n", uid);
                    return message;
                case GAME_END:
                    int turns = move.getTurns();
                    int winner = move.getPlayer();
                    message = String.format("Game has ended after %d turns, player %d is the winner!\n", turns, winner);
                    return message;
                default:
                     return "";
            }
        }catch(WrongMoveException e){
            System.out.println("CommandLinePlayer is processing a move update incorrectly.");
            return "";
        }
    }
}
