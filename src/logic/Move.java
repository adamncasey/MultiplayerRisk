package logic;

import java.util.*;

public class Move {
    public final int uid;

    public enum Stage {
        // Moves
        CLAIM_TERRITORY, REINFORCE_TERRITORY, TRADE_IN_CARDS, PLACE_ARMIES,
        DECIDE_ATTACK, START_ATTACK, CHOOSE_ATTACK_DICE, CHOOSE_DEFEND_DICE, OCCUPY_TERRITORY,
        DECIDE_FORTIFY, START_FORTIFY, FORTIFY_TERRITORY,
        // Events
        END_ATTACK, PLAYER_ELIMINATED, CARD_DRAWN,
        SETUP_BEGIN, SETUP_END, GAME_BEGIN, GAME_END
    }

    private final Stage stage;

    public Move(int uid, Stage stage){
        this.uid = uid;
        this.stage = stage;
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
        this.toTradeIn = new ArrayList<Card>(cards);
    }
    public List<Card> getToTradeIn() throws WrongMoveException{
        checkStage(Stage.TRADE_IN_CARDS);
        return this.toTradeIn;
    }

    // DECIDE_ATTACK, DECIDE_FORTIFY
    private boolean decision = false;
    public void setDecision(boolean decision) throws WrongMoveException{
        checkStage(Stage.DECIDE_ATTACK, Stage.DECIDE_FORTIFY);
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
        this.defenderLosses = numLosses;
    }
    public int getDefenderLosses() throws WrongMoveException{
        checkStage(Stage.END_ATTACK);
        return this.defenderLosses;
    }

    // PLAYER_ELIMINATED, GAME_END
    private int player = -1;
    protected void setPlayer(int player) throws WrongMoveException{
        checkStage(Stage.PLAYER_ELIMINATED, Stage.GAME_END);
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

    public static String getDescription(int uid, Stage stage){
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
        }
        return message;
    }
}
