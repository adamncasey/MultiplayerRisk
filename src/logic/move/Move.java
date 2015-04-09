package logic.move;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import logic.Card;
import logic.rng.RNG;
import logic.state.Board;

public class Move {
    private static List<String> names = null;

    public enum Stage {
        // Moves - Stages that IPlayers have to react to
        CLAIM_TERRITORY, REINFORCE_TERRITORY, TRADE_IN_CARDS, PLACE_ARMIES,
        DECIDE_ATTACK, START_ATTACK, CHOOSE_ATTACK_DICE, CHOOSE_DEFEND_DICE, ROLL_HASH, ROLL_NUMBER,
        OCCUPY_TERRITORY, DECIDE_FORTIFY, START_FORTIFY, FORTIFY_TERRITORY,
        // Events - Stages used by Game (IPlayers are only updated with these)
        END_ATTACK, PLAYER_ELIMINATED, CARD_DRAWN,
        SETUP_BEGIN, SETUP_END, GAME_BEGIN, GAME_END
    }

    private final int uid;
    private final Stage stage;
    private boolean readOnly;
    private boolean readOnlyInputs;

    public Move(int uid, Stage stage){
        this.uid = uid;
        this.stage = stage;
        this.readOnly = false;
        this.readOnlyInputs = false;
    }

    public int getUID(){
        return this.uid;
    }

    public Stage getStage(){
        return this.stage;
    }

    // CLAIM_TERRITORY, REINFORCE_TERRITORY, PLACE_ARMIES
    private int territory = -1;
    public void setTerritory(int territory){
        checkStage(Stage.CLAIM_TERRITORY, Stage.REINFORCE_TERRITORY, Stage.PLACE_ARMIES);
        checkPermissions();
        this.territory = territory;
    }
    public int getTerritory(){
        checkStage(Stage.CLAIM_TERRITORY, Stage.REINFORCE_TERRITORY, Stage.PLACE_ARMIES);
        return this.territory;

    }

    // PLACE_ARMIES, OCCUPY_TERRITORY, FORTIFY_TERRITORY
    private int armies = -1;
    public void setArmies(int numArmies){
        checkStage(Stage.PLACE_ARMIES, Stage.OCCUPY_TERRITORY, Stage.FORTIFY_TERRITORY);
        checkPermissions();
        this.armies = numArmies;
    }
    public int getArmies(){
        checkStage(Stage.PLACE_ARMIES, Stage.OCCUPY_TERRITORY, Stage.FORTIFY_TERRITORY);
        return this.armies;
    }

    // PLACE_ARMIES, OCCUPY_TERRITORY, FORTIFY_TERRITORY
    private int currentArmies = 0;
    public void setCurrentArmies(int numArmies){
        checkStage(Stage.PLACE_ARMIES, Stage.OCCUPY_TERRITORY, Stage.FORTIFY_TERRITORY);
        checkPermissions(Stage.PLACE_ARMIES, Stage.OCCUPY_TERRITORY, Stage.FORTIFY_TERRITORY);
        this.currentArmies = numArmies;
    }
    public int getCurrentArmies(){
        checkStage(Stage.PLACE_ARMIES, Stage.OCCUPY_TERRITORY, Stage.FORTIFY_TERRITORY);
        return this.currentArmies;
    }

    // PLACE_ARMIES
    private int extraArmies = 0;
    public void setExtraArmies(int extraArmies){
        checkStage(Stage.PLACE_ARMIES);
        checkPermissions(Stage.PLACE_ARMIES);
        this.extraArmies = extraArmies;
    }
    public int getExtraArmies(){
        checkStage(Stage.PLACE_ARMIES);
        return this.extraArmies;
    }

    // PLACE_ARMIES
    private List<Integer> matches = null;
    public void setMatches(List<Integer> matches){
        checkStage(Stage.PLACE_ARMIES);
        checkPermissions(Stage.PLACE_ARMIES);
        this.matches = new ArrayList<Integer>(matches);
    }
    public List<Integer> getMatches(){
        checkStage(Stage.PLACE_ARMIES);
        return Collections.unmodifiableList(this.matches);
    }
    
    // TRADE_IN_CARDS
    private List<Card> toTradeIn = null;
    public void setToTradeIn(List<Card> cards){
        checkStage(Stage.TRADE_IN_CARDS);
        checkPermissions();
        this.toTradeIn = new ArrayList<Card>(cards);
    }
    public List<Card> getToTradeIn(){
        checkStage(Stage.TRADE_IN_CARDS);
        return Collections.unmodifiableList(this.toTradeIn);
    }

    // DECIDE_ATTACK, DECIDE_FORTIFY
    private boolean decision = false;
    public void setDecision(boolean decision){
        checkStage(Stage.DECIDE_ATTACK, Stage.DECIDE_FORTIFY);
        checkPermissions();
        this.decision = decision;
    }
    public boolean getDecision(){
        checkStage(Stage.DECIDE_ATTACK, Stage.DECIDE_FORTIFY);
        return this.decision;
    }

    // START_ATTACK, START_FORTIFY, CHOOSE_ATTACK_DICE, CHOOSE_DEFEND_DICE
    private int from = -1;
    public void setFrom(int territory){
        checkStage(Stage.START_ATTACK, Stage.START_FORTIFY, Stage.CHOOSE_ATTACK_DICE, Stage.CHOOSE_DEFEND_DICE, Stage.OCCUPY_TERRITORY);
        checkPermissions(Stage.CHOOSE_ATTACK_DICE, Stage.CHOOSE_DEFEND_DICE);
        this.from = territory;
    }
    public int getFrom(){
        checkStage(Stage.START_ATTACK, Stage.START_FORTIFY, Stage.CHOOSE_ATTACK_DICE, Stage.CHOOSE_DEFEND_DICE, Stage.OCCUPY_TERRITORY);
        return this.from;
    }

    // START_ATTACK, START_FORTIFY, CHOOSE_ATTACK_DICE, CHOOSE_DEFEND_DICE
    private int to = -1;
    public void setTo(int territory){
        checkStage(Stage.START_ATTACK, Stage.START_FORTIFY, Stage.CHOOSE_ATTACK_DICE, Stage.CHOOSE_DEFEND_DICE, Stage.OCCUPY_TERRITORY);
        checkPermissions(Stage.CHOOSE_ATTACK_DICE, Stage.CHOOSE_DEFEND_DICE);
        this.to = territory;
    }
    public int getTo(){
        checkStage(Stage.START_ATTACK, Stage.START_FORTIFY, Stage.CHOOSE_ATTACK_DICE, Stage.CHOOSE_DEFEND_DICE, Stage.OCCUPY_TERRITORY);
        return this.to;
    }

    // CHOOSE_ATTACK_DICE, OCCUPY_TERRITORY
    private int attackDice = 0;
    public void setAttackDice(int numDice){
        checkStage(Stage.CHOOSE_ATTACK_DICE, Stage.OCCUPY_TERRITORY);
        checkPermissions(Stage.OCCUPY_TERRITORY);
        this.attackDice = numDice;
    }
    public int getAttackDice(){
        checkStage(Stage.CHOOSE_ATTACK_DICE, Stage.OCCUPY_TERRITORY);
        return this.attackDice;
    }
 
    // CHOOSE_DEFEND_DICE
    private int defendDice = 0;
    public void setDefendDice(int numDice){
        checkStage(Stage.CHOOSE_DEFEND_DICE);
        checkPermissions();
        this.defendDice = numDice;
    }
    public int getDefendDice(){
        checkStage(Stage.CHOOSE_DEFEND_DICE);
        return this.defendDice;
    }

    // ROLL_HASH, ROLL_NUMBER
    private RNG rng = null;
    public void setRNG(RNG rng){
        checkStage(Stage.ROLL_HASH, Stage.ROLL_NUMBER);
        checkPermissions(Stage.ROLL_HASH, Stage.ROLL_NUMBER);
        this.rng = rng;
    }
    public RNG getRNG(){
        checkStage(Stage.ROLL_HASH, Stage.ROLL_NUMBER);
        return this.rng;
    }

    // ROLL_HASH, ROLL_NUMBER
    private String rollHash = null;
    public void setRollHash(String rollHash){
        checkStage(Stage.ROLL_HASH, Stage.ROLL_NUMBER);
        checkPermissions(Stage.ROLL_NUMBER);
        this.rollHash = rollHash;
    }
    public String getRollHash(){
        checkStage(Stage.ROLL_HASH, Stage.ROLL_NUMBER);
        return this.rollHash;
    }

    // ROLL_NUMBER
    private String rollNumber = null;
    public void setRollNumber(String rollNumber){
        checkStage(Stage.ROLL_NUMBER);
        checkPermissions();
        this.rollNumber = rollNumber;
    }
    public String getRollNumber(){
        checkStage(Stage.ROLL_NUMBER);
        return this.rollNumber;
    }

    // END_ATTACK
    private int attackerLosses = 0;
    public void setAttackerLosses(int numLosses){
        checkStage(Stage.END_ATTACK);
        checkPermissions(Stage.END_ATTACK);
        this.attackerLosses = numLosses;
    }
    public int getAttackerLosses(){
        checkStage(Stage.END_ATTACK);
        return this.attackerLosses;
    }

    // END_ATTACK
    private int defenderLosses = 0;
    public void setDefenderLosses(int numLosses){
        checkStage(Stage.END_ATTACK);
        checkPermissions(Stage.END_ATTACK);
        this.defenderLosses = numLosses;
    }
    public int getDefenderLosses(){
        checkStage(Stage.END_ATTACK);
        return this.defenderLosses;
    }

    // END_ATTACK
    private List<Integer> attackDiceRolls = null;
    public void setAttackDiceRolls(List<Integer> results){
        checkStage(Stage.END_ATTACK);
        checkPermissions(Stage.END_ATTACK);
        this.attackDiceRolls = Collections.unmodifiableList(new ArrayList<Integer>(results));
    }
    public List<Integer> getAttackDiceRolls(){
        checkStage(Stage.END_ATTACK);
        return this.attackDiceRolls;
    }

    // END_ATTACK
    private List<Integer> defendDiceRolls = null;
    public void setDefendDiceRolls(List<Integer> results){
        checkStage(Stage.END_ATTACK);
        checkPermissions(Stage.END_ATTACK);
        this.defendDiceRolls = Collections.unmodifiableList(new ArrayList<Integer>(results));
    }
    public List<Integer> getDefendDiceRolls(){
        checkStage(Stage.END_ATTACK);
        return this.defendDiceRolls;
    }

    // CARD_DRAWN
    private Card card = null;
    public void setCard(Card card){
        checkStage(Stage.CARD_DRAWN);
        checkPermissions(Stage.CARD_DRAWN);
        this.card = card;
    }
    public Card getCard(){
        checkStage(Stage.CARD_DRAWN);
        return this.card;
    }

    // SETUP_BEGIN, PLAYER_ELIMINATED, GAME_END
    private int player = -1;
    public void setPlayer(int player){
        checkStage(Stage.SETUP_BEGIN, Stage.PLAYER_ELIMINATED, Stage.GAME_END);
        checkPermissions(Stage.SETUP_BEGIN, Stage.PLAYER_ELIMINATED, Stage.GAME_END);
        this.player = player;
    }
    public int getPlayer(){
        checkStage(Stage.SETUP_BEGIN, Stage.PLAYER_ELIMINATED, Stage.GAME_END);
        return this.player;
    }

    // GAME_END
    private int turns = -1;
    public void setTurns(int turns){
        checkStage(Stage.GAME_END);
        checkPermissions(Stage.GAME_END);
        this.turns = turns;
    }
    public int getTurns(){
        checkStage(Stage.GAME_END);
        return this.turns;
    }

    private void checkStage(Stage... stages){
        boolean ok = false;
        for(Stage s : stages){
            if(this.stage == s){
                ok = true;
            }

        }
        if(!ok){
            StackTraceElement[] ste = Thread.currentThread().getStackTrace();
            String callingMethod = ste[2].getMethodName();
            String message = String.format("%s cannot be accessed from stage %s.", callingMethod, Move.stageName(stage));
            System.out.println(message);
            System.exit(-1);
        }
    }

    public void setReadOnly(){
        this.readOnly = true;
    }

    public void setReadOnlyInputs(){
        this.readOnlyInputs = true;
    }

    /**
     * Stops players from writing to read only moves, and from editing the inputs received from Game.
     */
    private void checkPermissions(Stage... stages){
        if(readOnly){
            System.out.println("Attempted to write to a read only move.");
            System.exit(-1);
        }
        if(readOnlyInputs){
            for(Stage s : stages){
                if(s == this.stage){
                    System.out.println("Attempted to write to an input only field.");
                    System.exit(-1);
                }
            }
        }
    }

    // Returns a string describing what has just happened.
    public static String describeMove(Move move, Board board){
        String name = "Computer";
        if(move.getUID() != -1){
            name = names.get(move.getUID());
        }
        String message;
        switch(move.getStage()){
            case CLAIM_TERRITORY:
                int claimedTerritory = move.getTerritory();
                String claimedTerritoryName = board.getName(claimedTerritory);
                message = String.format("%s has claimed territory [%d-%s].\n", name, claimedTerritory, claimedTerritoryName);
                return message;
            case REINFORCE_TERRITORY:
                int reinforcedTerritory = move.getTerritory();
                String reinforcedTerritoryName = board.getName(reinforcedTerritory);
                message = String.format("%s has reinforced territory [%d-%s].\n", name, reinforcedTerritory, reinforcedTerritoryName);
                return message;
            case TRADE_IN_CARDS:
                List<Card> toTradeIn = move.getToTradeIn();
                if(toTradeIn.size() > 0){
                    String handMessage = Card.printHand(null, toTradeIn);
                    message = String.format("%s has traded in %s", name, handMessage);
                }else{
                    message = String.format("%s has not traded in any cards.\n", name);
                }
                return message;
            case PLACE_ARMIES:
                int placeArmiesTerritory = move.getTerritory();
                int placeArmiesNum = move.getArmies();
                String placeArmiesName = board.getName(placeArmiesTerritory);
                message = String.format("%s has placed %d armies at [%d-%s].\n", name, placeArmiesNum, placeArmiesTerritory, placeArmiesName); 
                return message;
            case DECIDE_ATTACK:
                boolean decideAttack = move.getDecision();
                if(decideAttack){
                    message = String.format("%s has chosen to attack.\n", name);
                }else{
                    message = String.format("%s has chosen not to attack.\n", name);
                }
                return message;
            case START_ATTACK:
                int attackFrom = move.getFrom();
                int attackTo = move.getTo();
                String attackFromName = board.getName(attackFrom);
                String attackToName = board.getName(attackTo);
                String enemyName = names.get(board.getOwner(attackTo));
                message = String.format("%s is attacking %s owned territory [%d-%s] from [%d-%s].\n", name, enemyName, attackTo, attackToName, attackFrom, attackFromName);
                return message;
            case CHOOSE_ATTACK_DICE:
                int numAttackingDice = move.getAttackDice();
                message = String.format("%s has chosen to attack with %d dice.\n", name, numAttackingDice);
                return message;
            case CHOOSE_DEFEND_DICE:
                int numDefendingDice = move.getDefendDice();
                message = String.format("%s has chosen to defend with %d dice.\n", name, numDefendingDice);
                return message;
            case ROLL_HASH:
                message = String.format("%s has sent their roll hash.\n", name);
                return message;
            case ROLL_NUMBER:
                message = String.format("%s has sent their roll number.\n", name);
                return message;
            case OCCUPY_TERRITORY:
                int numOccupyArmies = move.getArmies();
                message = String.format("%s was successful in their attack and has moved %d armies forward.\n", name, numOccupyArmies);
                return message;
            case DECIDE_FORTIFY:
                boolean decideFortify = move.getDecision();
                if(decideFortify){
                    message = String.format("%s has chosen to fortify.\n", name);
                }else{
                    message = String.format("%s has chosen not to fortify.\n", name);
                }
                return message;
            case START_FORTIFY:
                int fortifyFrom = move.getFrom();
                int fortifyTo = move.getTo();
                String fortifyFromName = board.getName(fortifyFrom);
                String fortifyToName = board.getName(fortifyTo);
                message = String.format("%s is fortifying [%d-%s] from [%d-%s].\n", name, fortifyTo, fortifyToName, fortifyFrom, fortifyFromName);
                return message;
            case FORTIFY_TERRITORY:
                int numFortifyArmies = move.getArmies();
                message = String.format("%s has fortified with %d armies.\n", name, numFortifyArmies);
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
                String eliminatedPlayer = names.get(move.getPlayer());
                message = String.format("%s has just been eliminated by %s.\n", eliminatedPlayer, name);
                return message;
            case CARD_DRAWN:
                message = String.format("%s has drawn a card (%s).\n", name, move.getCard().getID());
                return message;
            case SETUP_BEGIN:
                int numPlayers = move.getPlayer();
                message = String.format("Game setup is beginning with %d players, %s is first to go.\n", numPlayers, name);
                return message;
            case SETUP_END:
                return "Game setup has ended.\n";
            case GAME_BEGIN:
                message = String.format("Game is beginning, %s is first to go.\n", name);
                return message;
            case GAME_END:
                int turns = move.getTurns();
                String winner = names.get(move.getPlayer());
                message = String.format("Game has ended after %d turns, %s is the winner!\n", turns, winner);
                return message;
            default:
                 return "";
        }
    }

    // Returns a string describe what the player is about to do. (To be displayed while waiting)
    public static String describeStatus(Move move){
        String name = names.get(move.getUID());
        Stage stage = move.getStage();
        String message = "";
        switch(stage){
            case CLAIM_TERRITORY:
                message = String.format("%s is claiming a territory.", name);
                break;
            case REINFORCE_TERRITORY:
                message = String.format("%s is reinforcing a territory.", name);
                break;
            case TRADE_IN_CARDS:
                message = String.format("%s is trading in cards.", name);
                break;
            case PLACE_ARMIES:
                message = String.format("%s is placing armies.", name);
                break;
            case DECIDE_ATTACK:
                message = String.format("%s is deciding whether or not to attack.", name);
                break;
            case START_ATTACK:
                message = String.format("%s is choosing where to attack.", name);
                break;
            case CHOOSE_ATTACK_DICE:
                message = String.format("%s is deciding how many dice to attack with.", name);
                break;
            case CHOOSE_DEFEND_DICE:
                message = String.format("%s is deciding how many dice to defend with.", name);
                break;
            case ROLL_HASH:
                message = String.format("%s is sending their roll hash.", name);
                break;
            case ROLL_NUMBER:
                message = String.format("%s is sending their roll number.", name);
                break;
            case OCCUPY_TERRITORY:
                message = String.format("%s is deciding how many armies to move into the captured territory.", name);
                break;
            case DECIDE_FORTIFY:
                message = String.format("%s is deciding whether or not to fortify.", name);
                break;
            case START_FORTIFY:
                message = String.format("%s is choosing where to fortify.", name);
                break;
            case FORTIFY_TERRITORY:
                message = String.format("%s is deciding how many armies to fortify with.", name);
                break;
            default:
                break;
        }
        return message;
    }

    // Returns a string describing what has just happened.
    public static String stageName(Stage s){
        switch(s){
            case CLAIM_TERRITORY:
                return "CLAIM_TERRITORY";
            case REINFORCE_TERRITORY:
                return "REINFORCE_TERRITORY";
            case TRADE_IN_CARDS:
                return "TRADE_IN_CARDS";
            case PLACE_ARMIES:
                return "PLACE_ARMIES";
            case DECIDE_ATTACK:
                return "DECIDE_ATTACK";
            case START_ATTACK:
                return "START_ATTACK";
            case CHOOSE_ATTACK_DICE:
                return "CHOOSE_ATTACK_DICE";
            case CHOOSE_DEFEND_DICE:
                return "CHOOSE_DEFEND_DICE";
            case ROLL_HASH:
                return "ROLL_HASH";
            case ROLL_NUMBER:
                return "ROLL_NUMBER";
            case OCCUPY_TERRITORY:
                return "OCCUPY_TERRITORY";
            case DECIDE_FORTIFY:
                return "DECIDE_FORTIFY";
            case START_FORTIFY:
                return "START_FORTIFY";
            case FORTIFY_TERRITORY:
                return "FORTIFY_TERRITORY";
            case END_ATTACK:
                return "END_ATTACK";
            case PLAYER_ELIMINATED:
                return "PLAYER_ELIMINATED";
            case CARD_DRAWN:
                return "CARD_DRAWN";
            case SETUP_BEGIN:
                return "SETUP_BEGIN";
            case SETUP_END:
                return "SETUP_END";
            case GAME_BEGIN:
                return "GAME_BEGIN";
            case GAME_END:
                return "GAME_END";
        }
        return null;
    }

    public static void setNames(List<String> playerNames){
        if(names == null){
            names = new ArrayList<String>(playerNames);
        }
    }
}
