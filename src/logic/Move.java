package logic;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Move {
    public final int uid;

    public enum Stage {
        // Moves - Stages that IPlayers have to react to
        CLAIM_TERRITORY, REINFORCE_TERRITORY, TRADE_IN_CARDS, PLACE_ARMIES,
        DECIDE_ATTACK, START_ATTACK, CHOOSE_ATTACK_DICE, CHOOSE_DEFEND_DICE, OCCUPY_TERRITORY,
        DECIDE_FORTIFY, START_FORTIFY, FORTIFY_TERRITORY,
        // Events - Stages used by Game (IPlayers are only updated with these)
        END_ATTACK, PLAYER_ELIMINATED, CARD_DRAWN,
        SETUP_BEGIN, SETUP_END, GAME_BEGIN, GAME_END
    }

    private final Stage stage;
    private boolean readOnly;

    public Move(int uid, Stage stage){
        this.uid = uid;
        this.stage = stage;
        this.readOnly = false;
    }

    public int getUID(){
        return this.uid;
    }

    public Stage getStage(){
        return this.stage;
    }

    // CLAIM_TERRITORY, REINFORCE_TERRITORY, PLACE_ARMIES
    private int territory = -1;
    public void setTerritory(int territory) throws WrongMoveException{
        checkStage(Stage.CLAIM_TERRITORY, Stage.REINFORCE_TERRITORY, Stage.PLACE_ARMIES);
        checkPermissions();
        this.territory = territory;
    }
    public int getTerritory() throws WrongMoveException{
        checkStage(Stage.CLAIM_TERRITORY, Stage.REINFORCE_TERRITORY, Stage.PLACE_ARMIES);
        return this.territory;

    }

    // PLACE_ARMIES, OCCUPY_TERRITORY, FORTIFY_TERRITORY
    private int armies = -1;
    public void setArmies(int numArmies) throws WrongMoveException{
        checkStage(Stage.PLACE_ARMIES, Stage.OCCUPY_TERRITORY, Stage.FORTIFY_TERRITORY);
        checkPermissions();
        this.armies = numArmies;
    }
    public int getArmies() throws WrongMoveException{
        checkStage(Stage.PLACE_ARMIES, Stage.OCCUPY_TERRITORY, Stage.FORTIFY_TERRITORY);
        return this.armies;
    }

    // PLACE_ARMIES, OCCUPY_TERRITORY, FORTIFY_TERRITORY
    private int currentArmies = 0;
    protected void setCurrentArmies(int numArmies) throws WrongMoveException{
        checkStage(Stage.PLACE_ARMIES, Stage.OCCUPY_TERRITORY, Stage.FORTIFY_TERRITORY);
        checkPermissions();
        this.currentArmies = numArmies;
    }
    public int getCurrentArmies() throws WrongMoveException{
        checkStage(Stage.PLACE_ARMIES, Stage.OCCUPY_TERRITORY, Stage.FORTIFY_TERRITORY);
        return this.currentArmies;
    }
    
    // TRADE_IN_CARDS
    private List<Card> toTradeIn = null;
    public void setToTradeIn(List<Card> cards) throws WrongMoveException{
        checkStage(Stage.TRADE_IN_CARDS);
        checkPermissions();
        this.toTradeIn = new ArrayList<Card>(cards);
    }
    public List<Card> getToTradeIn() throws WrongMoveException{
        checkStage(Stage.TRADE_IN_CARDS);
        return Collections.unmodifiableList(this.toTradeIn);
    }

    // DECIDE_ATTACK, DECIDE_FORTIFY
    private boolean decision = false;
    public void setDecision(boolean decision) throws WrongMoveException{
        checkStage(Stage.DECIDE_ATTACK, Stage.DECIDE_FORTIFY);
        checkPermissions();
        this.decision = decision;
    }
    public boolean getDecision() throws WrongMoveException{
        checkStage(Stage.DECIDE_ATTACK, Stage.DECIDE_FORTIFY);
        return this.decision;
    }

    // START_ATTACK, START_FORTIFY, CHOOSE_ATTACK_DICE, CHOOSE_DEFEND_DICE
    private int from = -1;
    public void setFrom(int territory) throws WrongMoveException{
        checkStage(Stage.START_ATTACK, Stage.START_FORTIFY, Stage.CHOOSE_ATTACK_DICE, Stage.CHOOSE_DEFEND_DICE);
        checkPermissions();
        this.from = territory;
    }
    public int getFrom() throws WrongMoveException{
        checkStage(Stage.START_ATTACK, Stage.START_FORTIFY, Stage.CHOOSE_ATTACK_DICE, Stage.CHOOSE_DEFEND_DICE);
        return this.from;
    }

    // START_ATTACK, START_FORTIFY, CHOOSE_ATTACK_DICE, CHOOSE_DEFEND_DICE
    private int to = -1;
    public void setTo(int territory) throws WrongMoveException{
        checkStage(Stage.START_ATTACK, Stage.START_FORTIFY, Stage.CHOOSE_ATTACK_DICE, Stage.CHOOSE_DEFEND_DICE);
        checkPermissions();
        this.to = territory;
    }
    public int getTo() throws WrongMoveException{
        checkStage(Stage.START_ATTACK, Stage.START_FORTIFY, Stage.CHOOSE_ATTACK_DICE, Stage.CHOOSE_DEFEND_DICE);
        return this.to;
    }

    // CHOOSE_ATTACK_DICE, OCCUPY_TERRITORY
    private int attackDice = 0;
    public void setAttackDice(int numDice) throws WrongMoveException{
        checkStage(Stage.CHOOSE_ATTACK_DICE, Stage.OCCUPY_TERRITORY);
        checkPermissions();
        this.attackDice = numDice;
    }
    public int getAttackDice() throws WrongMoveException{
        checkStage(Stage.CHOOSE_ATTACK_DICE, Stage.OCCUPY_TERRITORY);
        return this.attackDice;
    }
 
    // CHOOSE_DEFEND_DICE
    private int defendDice = 0;
    public void setDefendDice(int numDice) throws WrongMoveException{
        checkStage(Stage.CHOOSE_DEFEND_DICE);
        checkPermissions();
        this.defendDice = numDice;
    }
    public int getDefendDice() throws WrongMoveException{
        checkStage(Stage.CHOOSE_DEFEND_DICE);
        return this.defendDice;
    }

    // END_ATTACK
    private int attackerLosses = 0;
    protected void setAttackerLosses(int numLosses) throws WrongMoveException{
        checkStage(Stage.END_ATTACK);
        checkPermissions();
        this.attackerLosses = numLosses;
    }
    public int getAttackerLosses() throws WrongMoveException{
        checkStage(Stage.END_ATTACK);
        return this.attackerLosses;
    }

    // END_ATTACK
    private int defenderLosses = 0;
    protected void setDefenderLosses(int numLosses) throws WrongMoveException{
        checkStage(Stage.END_ATTACK);
        checkPermissions();
        this.defenderLosses = numLosses;
    }
    public int getDefenderLosses() throws WrongMoveException{
        checkStage(Stage.END_ATTACK);
        return this.defenderLosses;
    }

    // END_ATTACK
    private List<Integer> attackDiceRolls = null;
    protected void setAttackDiceRolls(List<Integer> results) throws WrongMoveException{
        checkStage(Stage.END_ATTACK);
        checkPermissions();
        this.attackDiceRolls = Collections.unmodifiableList(new ArrayList<Integer>(results));
    }
    public List<Integer> getAttackDiceRolls() throws WrongMoveException{
        checkStage(Stage.END_ATTACK);
        return this.attackDiceRolls;
    }

    // END_ATTACK
    private List<Integer> defendDiceRolls = null;
    protected void setDefendDiceRolls(List<Integer> results) throws WrongMoveException{
        checkStage(Stage.END_ATTACK);
        checkPermissions();
        this.defendDiceRolls = Collections.unmodifiableList(new ArrayList<Integer>(results));
    }
    public List<Integer> getDefendDiceRolls() throws WrongMoveException{
        checkStage(Stage.END_ATTACK);
        return this.defendDiceRolls;
    }

    // PLAYER_ELIMINATED, GAME_END
    private int player = -1;
    protected void setPlayer(int player) throws WrongMoveException{
        checkStage(Stage.PLAYER_ELIMINATED, Stage.GAME_END);
        checkPermissions();
        this.player = player;
    }
    public int getPlayer() throws WrongMoveException{
        checkStage(Stage.PLAYER_ELIMINATED, Stage.GAME_END);
        return this.player;
    }

    // GAME_END
    private int turns = -1;
    protected void setTurns(int turns) throws WrongMoveException{
        checkStage(Stage.GAME_END);
        checkPermissions();
        this.turns = turns;
    }
    public int getTurns() throws WrongMoveException{
        checkStage(Stage.GAME_END);
        return this.turns;
    }

    private void checkStage(Stage... stages) throws WrongMoveException{
        boolean ok = false;
        for(Stage s : stages){
            if(this.stage == s){
                ok = true;
            }

        }
        if(!ok){
            StackTraceElement[] ste = Thread.currentThread().getStackTrace();
            String callingMethod = ste[ste.length - 8].getMethodName();
            String message = String.format("%s cannot be accessed from stage %d.", callingMethod, stage);
            throw new WrongMoveException(message);
        }
    }

    protected void setReadOnly(){
        this.readOnly = true;
    }

    private void checkPermissions() throws WrongMoveException{
        if(readOnly){
            throw new WrongMoveException("Attempted to write to move while it is in read only mode.");
        }
    }

    // Returns a string describing what has just happened.
    public static String describeMove(Move move, Board board){
        int uid = move.getUID();
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
                    int enemyUID = board.getOwner(attackTo);
                    message = String.format("Player %d is attacking Player %d owned territory [%d-%s] from [%d-%s].\n", uid, enemyUID, attackTo, attackToName, attackFrom, attackFromName);
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
                    List<Integer> attackDiceRolls = move.getAttackDiceRolls();
                    message = "Dice Rolls : Attacker - ";
                    for(int r : attackDiceRolls){
                        message += String.format("(%d) ", r);
                    }
                    message += "- Defender - ";
                    List<Integer> defendDiceRolls = move.getDefendDiceRolls();
                    for(int r : defendDiceRolls){
                        message += String.format("(%d) ", r);
                    }
                    message += "\n";
                    message += String.format("The attacker lost %d armies, the defender lost %d armies.\n", attackerLosses, defenderLosses);
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

    // Returns a string describe what the player is about to do. (To be displayed while waiting)
    public static String describeStatus(Move move){
        int uid = move.getUID();
        Stage stage = move.getStage();
        String message = "";
        switch(stage){
            case CLAIM_TERRITORY:
                message = String.format("Player %d is claiming a territory.", uid);
                break;
            case REINFORCE_TERRITORY:
                message = String.format("Player %d is reinforcing a territory.", uid);
                break;
            case TRADE_IN_CARDS:
                message = String.format("Player %d is trading in cards.", uid);
                break;
            case PLACE_ARMIES:
                message = String.format("Player %d is placing armies.", uid);
                break;
            case DECIDE_ATTACK:
                message = String.format("Player %d is deciding whether or not to attack.", uid);
                break;
            case START_ATTACK:
                message = String.format("Player %d is choosing where to attack.", uid);
                break;
            case CHOOSE_ATTACK_DICE:
                message = String.format("Player %d is deciding how many dice to attack with.", uid);
                break;
            case CHOOSE_DEFEND_DICE:
                message = String.format("Player %d is deciding how many dice to defend with.", uid);
                break;
            case OCCUPY_TERRITORY:
                message = String.format("Player %d is deciding how many armies to move into the captured territory.", uid);
                break;
            case DECIDE_FORTIFY:
                message = String.format("Player %d is deciding whether or not to fortify.", uid);
                break;
            case START_FORTIFY:
                message = String.format("Player %d is choosing where to fortify.", uid);
                break;
            case FORTIFY_TERRITORY:
                message = String.format("Player %d is deciding how many armies to fortify with.", uid);
                break;
            default:
                break;
        }
        return message;
    }
}
