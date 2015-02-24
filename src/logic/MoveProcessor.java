package logic;

import java.util.ArrayList;

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
                case 0:
                    int claimedTerritory = move.getTerritoryToClaim();
                    String claimedTerritoryName = board.getName(claimedTerritory);
                    message = String.format("Player %d has claimed territory [%d-%s].\n", uid, claimedTerritory, claimedTerritoryName);
                    return message;
                case 1:
                    int reinforcedTerritory = move.getTerritoryToReinforce();
                    String reinforcedTerritoryName = board.getName(reinforcedTerritory);
                    message = String.format("Player %d has reinforced territory [%d-%s].\n", uid, reinforcedTerritory, reinforcedTerritoryName);
                    return message;
                case 2:
                    ArrayList<Card> toTradeIn = move.getToTradeIn();
                    String handMessage = Card.printHand(null, toTradeIn);
                    message = String.format("Player %d has traded in %s", uid, handMessage);
                    return message;
                case 3:
                    int placeArmiesTerritory = move.getPlaceArmiesTerritory();
                    int placeArmiesNum = move.getPlaceArmiesNum();
                    String placeArmiesName = board.getName(placeArmiesTerritory);
                    message = String.format("Player %d has placed %d armies at [%d-%s].\n", uid, placeArmiesNum, placeArmiesTerritory, placeArmiesName); 
                    return message;
                case 4:
                    boolean decideAttack = move.getDecideAttack();
                    if(decideAttack){
                        message = String.format("Player %d has chosen to attack.\n", uid);
                    }else{
                        message = String.format("Player %d has chosen not to attack.\n", uid);
                    }
                    return message;
                case 5:
                    int attackFrom = move.getAttackFrom();
                    int attackTo = move.getAttackTo();
                    String attackFromName = board.getName(attackFrom);
                    String attackToName = board.getName(attackTo);
                    message = String.format("Player %d is attacking [%d-%s] from [%d-%s].\n", uid, attackTo, attackToName, attackFrom, attackFromName);
                    return message;
                case 6:
                    int numAttackingDice = move.getAttackingDice();
                    message = String.format("Player %d has chosen to attack with %d dice.\n", uid, numAttackingDice);
                    return message;
                case 7:
                    int numDefendingDice = move.getDefendingDice();
                    message = String.format("Player %d has chosen to defend with %d dice.\n", uid, numDefendingDice);
                    return message;
                case 8:
                    int numOccupyArmies = move.getOccupyArmies();
                    message = String.format("Player %d was successful in their attack and has moved %d armies forward.\n", uid, numOccupyArmies);
                    return message;
                case 9:
                    boolean decideFortify = move.getDecideFortify();
                    if(decideFortify){
                        message = String.format("Player %d has chosen to fortify.\n", uid);
                    }else{
                        message = String.format("Player %d has chosen not to fortify.\n", uid);
                    }
                    return message;
                case 10:
                    int fortifyFrom = move.getFortifyFrom();
                    int fortifyTo = move.getFortifyTo();
                    String fortifyFromName = board.getName(fortifyFrom);
                    String fortifyToName = board.getName(fortifyTo);
                    message = String.format("Player %d is fortifying [%d-%s] from [%d-%s].\n", uid, fortifyTo, fortifyToName, fortifyFrom, fortifyFromName);
                    return message;
                case 11:
                    int numFortifyArmies = move.getFortifyArmies();
                    message = String.format("Player %d has fortified with %d armies.\n", uid, numFortifyArmies);
                    return message;
                case 101:
                    int attackerLosses = move.getAttackerLosses();
                    int defenderLosses = move.getDefenderLosses();
                    message = String.format("The attacker lost %d armies, the defender lost %d armies.\n", attackerLosses, defenderLosses);
                    return message;
                case 102:
                    message = String.format("Player %d has just been eliminated.\n", uid);
                    return message;
                case 103:
                    message = String.format("Player %d has drawn a card.\n", uid);
                    return message;
                case 104:
                    message = String.format("Game setup is beginning with %d players.\n", uid);
                    return message;
                case 105:
                    return "Game setup has ended.\n";
                case 106:
                    message = String.format("Game is beginning, player %d is first to go.\n", uid);
                    return message;
                case 107:
                    message = String.format("Game has ended, player %d is the winner!\n", uid);
                    return message;
                case 108:
                    message = String.format("Game has completed in %d turns.\n", uid);
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
