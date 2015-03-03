package logic;

import logic.Move.Stage;
import static logic.Move.Stage.*;

import java.util.*;

import player.*;

/**
 * Game --- The main game loop that lets each player take their turn, updating every player whenever anything happens.
 */
public class Game {
    private static Random random;

    private List<IPlayer> players;
    private int firstPlayer;

    private Board board;
    private Deck deck;
    private List<List<Card>> playerHands;
    private MoveChecker checker;

    private int setupValues[] = {35, 30, 25, 20};
    private int setCounter = 0;
    private int armyReward = 4;
    private int setValues[] = {4, 6, 8, 10, 12, 15};

    private int totalPlayerCount = 0;
    private int activePlayerCount = 0;

    public Game(List<IPlayer> players, int firstPlayer, int seed, String boardFilename){
        this.random = new Random(seed);
        this.players = new ArrayList<IPlayer>();
        this.firstPlayer = firstPlayer;
        this.playerHands = new ArrayList<List<Card>>();
        for(int i = 0; i != players.size(); ++i){
            IPlayer pi = players.get(i);
            this.players.add(pi);
            this.playerHands.add(new ArrayList<Card>());
            totalPlayerCount++;
        }
        activePlayerCount = totalPlayerCount;
        this.board = new Board(boardFilename);
        this.deck = board.getDeck();
        this.deck.shuffle(seed);
        this.checker = new MoveChecker(board);
    }

    private void updatePlayers(int currentPlayer, Move previousMove){
        for(int i = 0; i != players.size(); ++i){
            IPlayer p = players.get(i);
            p.updatePlayer(board, playerHands.get(i), currentPlayer, previousMove);
        }
        checker.update(board);
        String message = MoveProcessor.processMove(currentPlayer, previousMove, board);
    }

    public void setupGame() throws WrongMoveException{
        if(activePlayerCount < 3 || activePlayerCount > 6){
            return;
        }
        updatePlayers(activePlayerCount, new Move(-1, SETUP_BEGIN));
        int initialArmyValue = setupValues[activePlayerCount-3];
        int totalArmies = activePlayerCount * initialArmyValue;
        int territoriesToClaim = board.getNumTerritories();

        int currentPlayer = firstPlayer;
        while(totalArmies != 0){
            IPlayer player = players.get(currentPlayer);
            if(territoriesToClaim != 0){
                Move move = new Move(currentPlayer, CLAIM_TERRITORY);
                move = getMove(currentPlayer, move);
                int territoryToClaim = move.getTerritory();
                board.claimTerritory(territoryToClaim, currentPlayer);
                board.placeArmies(territoryToClaim, 1);
                territoriesToClaim--;
                updatePlayers(currentPlayer, move);
            } else {
                Move move = new Move(currentPlayer, REINFORCE_TERRITORY);
                move = getMove(currentPlayer, move);
                int territoryToReinforce = move.getTerritory();
                board.placeArmies(territoryToReinforce, 1);
                updatePlayers(currentPlayer, move);
            }
            totalArmies--;
            currentPlayer++;
            if(currentPlayer == totalPlayerCount){
                currentPlayer = 0;
            }
        }
        updatePlayers(activePlayerCount, new Move(-1, SETUP_END));
    }

    public void playGame() throws WrongMoveException{
        if(totalPlayerCount < 3 || totalPlayerCount > 6){
            return;
        }
        int turnCounter = 0;
        int currentPlayer = firstPlayer;
        updatePlayers(currentPlayer, new Move(-1, GAME_BEGIN));
        while(activePlayerCount != 1){
            IPlayer player = players.get(currentPlayer);
            if(!player.isEliminated()){
                playerTurn(currentPlayer);
                turnCounter++;
            }
            currentPlayer++;
            if(currentPlayer == totalPlayerCount){
                currentPlayer = 0;
            }
        }
        Move gameEnded = new Move(-1, GAME_END);
        gameEnded.setTurns(turnCounter);
        gameEnded.setPlayer(currentPlayer);
        updatePlayers(currentPlayer, gameEnded);
        return;
    }

    private void playerTurn(int uid) throws WrongMoveException{
        IPlayer player = players.get(uid);

        Move move = new Move(uid, TRADE_IN_CARDS);
        move = getMove(uid, move);
        List<Card> toTradeIn = move.getToTradeIn();
        boolean traded = tradeInCards(uid, toTradeIn); 
        updatePlayers(uid, move);

        int armies = calculatePlayerArmies(uid, traded, toTradeIn);
        while(armies != 0){
            move = new Move(uid, PLACE_ARMIES);
            move.setCurrentArmies(armies);
            move = getMove(uid, move);
            board.placeArmies(move.getTerritory(), move.getArmies());
            armies -= move.getArmies();
            updatePlayers(uid, move);
        }

        boolean territoryCaptured = false;
        while(checkAttackPossible(uid)){
            move = new Move(uid, DECIDE_ATTACK);
            move = getMove(uid, move);
            updatePlayers(uid, move);
            boolean attacking = move.getDecision();
            if(!attacking){
                break;
            }

            move = new Move(uid, START_ATTACK);
            move = getMove(uid, move);
            int attackFrom = move.getFrom();
            int attackTo = move.getTo();
            updatePlayers(uid, move);

            move = new Move(uid, CHOOSE_ATTACK_DICE);
            move.setFrom(attackFrom);
            move.setTo(attackTo);
            move = getMove(uid, move);
            int attackingDice = move.getAttackDice();
            updatePlayers(uid, move);

            int enemyUID = board.getOwner(attackTo);
            move = new Move(enemyUID, CHOOSE_DEFEND_DICE);
            move.setFrom(attackFrom);
            move.setTo(attackTo);
            move = getMove(enemyUID, move);
            int defendingDice = move.getDefendDice();
            updatePlayers(enemyUID, move);
 
            List<Integer> attackDiceRolls = rollDice(attackingDice);
            List<Integer> defendDiceRolls = rollDice(defendingDice);
            List<Integer> attackResult = decideAttackResult(attackDiceRolls, defendDiceRolls);
            board.placeArmies(attackFrom, -attackResult.get(0));
            board.placeArmies(attackTo, -attackResult.get(1));
            boolean willCaptureTerritory = board.getArmies(attackTo) == 0;

            move = new Move(uid, END_ATTACK);
            move.setAttackerLosses(attackResult.get(0));
            move.setDefenderLosses(attackResult.get(1));
            updatePlayers(uid, move);

            if(willCaptureTerritory){ 
                territoryCaptured = true;
                move = new Move(uid, OCCUPY_TERRITORY);
                move.setCurrentArmies(board.getArmies(attackFrom));
                move.setAttackDice(attackingDice);
                move = getMove(uid, move);
                int occupyArmies = move.getArmies();
                board.placeArmies(attackFrom, -occupyArmies);
                board.placeArmies(attackTo, occupyArmies);
                board.claimTerritory(attackTo, uid);
                updatePlayers(uid, move);
            }

            if(isEliminated(enemyUID)){
                if(eliminatePlayer(uid, enemyUID)){
                    return;
                }
                move = new Move(uid, PLAYER_ELIMINATED);
                move.setPlayer(enemyUID);
                updatePlayers(uid, move);
                List<Card> hand = playerHands.get(uid);
                if(hand.size() > 5){ // immediately trade in cards when at 6 or more
                    while(hand.size() >= 5){ // trade in cards and place armies until 4 or fewer cards
                        move = new Move(uid, TRADE_IN_CARDS);
                        move = getMove(uid, move);
                        toTradeIn = move.getToTradeIn();
                        tradeInCards(uid, toTradeIn); 
                        updatePlayers(uid, move);
                    
                        armies = incrementSetCounter();
                        while(armies != 0){
                            move = new Move(uid, PLACE_ARMIES);
                            move.setCurrentArmies(armies);
                            move = getMove(uid, move);
                            board.placeArmies(move.getTerritory(), move.getArmies());
                            armies -= move.getArmies();
                            updatePlayers(uid, move);
                        }
                    }
                }
            }
        }

        if(territoryCaptured){
            Card newCard = deck.drawCard();
            if(newCard != null){
                playerHands.get(uid).add(newCard);
                updatePlayers(uid, new Move(uid, CARD_DRAWN));
            }
        }

        if(checkFortifyPossible(uid)){
            move = new Move(uid, DECIDE_FORTIFY);
            move = getMove(uid, move);
            updatePlayers(uid, move);

            boolean decideFortify = move.getDecision();
            if(decideFortify){
                move = new Move(uid, START_FORTIFY);
                move = getMove(uid, move);
                int fortifyFrom = move.getFrom();
                int fortifyTo = move.getTo();
                updatePlayers(uid, move);

                move = new Move(uid, FORTIFY_TERRITORY);
                move.setCurrentArmies(board.getArmies(fortifyFrom));
                move = getMove(uid, move);
                int numFortifyArmies = move.getArmies();
                board.placeArmies(fortifyFrom, -numFortifyArmies);
                board.placeArmies(fortifyTo, numFortifyArmies);
                updatePlayers(uid, move);
            }
        }
    }

    public Move getMove(int currentPlayer, Move move) throws WrongMoveException{
        Stage stage = move.getStage();
        for(IPlayer p : players){
            p.nextMove(currentPlayer, Move.getDescription(currentPlayer, stage));
        }

        IPlayer player = players.get(currentPlayer);
        move = player.getMove(move);
        while(!checkMove(currentPlayer, stage, move)){
            move = player.getMove(move);
        }
        move.setReadOnly();
        return move;
    }

    public boolean checkMove(int currentPlayer, Stage stage, Move move) throws WrongMoveException{
        if(move == null){
            return false;
        }
        if(stage != move.getStage()){
            return false;
        }
        switch(stage){
            case CLAIM_TERRITORY:
                int territoryToClaim = move.getTerritory();
                return checker.checkClaimTerritory(territoryToClaim);
            case REINFORCE_TERRITORY:
                int territoryToReinforce = move.getTerritory();
                return checker.checkReinforceTerritory(currentPlayer, territoryToReinforce);
            case TRADE_IN_CARDS:
                List<Card> hand = playerHands.get(currentPlayer); 
                List<Card> toTradeIn = move.getToTradeIn();
                return checker.checkTradeInCards(hand, toTradeIn);
            case PLACE_ARMIES:
                int placeArmiesTerritory = move.getTerritory();
                int placeArmiesNum = move.getArmies();
                int armiesToPlace = move.getCurrentArmies();
                return checker.checkPlaceArmies(currentPlayer, placeArmiesTerritory, placeArmiesNum, armiesToPlace);
            case DECIDE_ATTACK:
                return true;
            case START_ATTACK:
                int attackFrom = move.getFrom();
                int attackTo = move.getTo();
                return checker.checkStartAttack(currentPlayer, attackFrom, attackTo);
            case CHOOSE_ATTACK_DICE:
                int attackingDice = move.getAttackDice();
                int attackingNumArmies = board.getArmies(move.getFrom());
                return checker.checkAttackingDice(attackingDice, attackingNumArmies);
            case CHOOSE_DEFEND_DICE:
                int defendingDice = move.getDefendDice();
                int defendingNumArmies = board.getArmies(move.getTo());
                return checker.checkDefendingDice(defendingDice, defendingNumArmies);
            case OCCUPY_TERRITORY:
                int occupyArmies = move.getArmies();
                int occupyDice = move.getAttackDice();
                int occupyCurrentArmies = move.getCurrentArmies();
                return checker.checkOccupyArmies(occupyArmies, occupyDice, occupyCurrentArmies);
            case DECIDE_FORTIFY:
                return true;
            case START_FORTIFY:
                int fortifyFrom = move.getFrom();
                int fortifyTo = move.getTo();
                return checker.checkStartFortify(currentPlayer, fortifyFrom, fortifyTo);
            case FORTIFY_TERRITORY:
                int fortifyArmies = move.getArmies();
                int fortifyCurrentArmies = move.getCurrentArmies();
                return checker.checkFortifyArmies(fortifyArmies, fortifyCurrentArmies);
            default:
                return false;
        }
    }

    public boolean tradeInCards(int uid, List<Card> toTradeIn){
        List<Card> hand = playerHands.get(uid);
        for(Card c: toTradeIn){
            hand.remove(c);
        }
        if(toTradeIn.size() == 3){
            return true;
        }
        return false;
    }

    public int calculatePlayerArmies(int uid, boolean traded, List<Card> toTradeIn){
        int armies = 0;

        armies += board.calculatePlayerTerritoryArmies(uid);
        armies += board.calculatePlayerContinentArmies(uid);

        if(traded){
            armies += incrementSetCounter();
        }

        int extraArmies = 0;
        for(Card card : toTradeIn){
            if(board.getOwner(card.getID()) == uid){
                extraArmies = 2;
            }
        }
        armies += extraArmies;

        return armies;
    }

    // returns the number of armies rewarded for trading in the set
    public int incrementSetCounter(){
        int reward = armyReward;
        setCounter++;
        if(setCounter > setValues.length-1){
            armyReward = setValues[setValues.length-1] + (5 * (setCounter - (setValues.length-1)));
        } else {
            armyReward = setValues[setCounter];
        }
        return reward; 
    }

    public boolean checkAttackPossible(int uid){
        for(int i = 0; i != board.getNumTerritories(); ++i){
            if(board.getOwner(i) == uid && board.getArmies(i) >= 2){
                for(int j : board.getLinks(i)){
                    if(board.getOwner(j) != uid){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static List<Integer> rollDice(int numDice){
        List<Integer> diceRolls = new ArrayList<Integer>();
        for(int i = 0; i != numDice; ++i){
            diceRolls.add(random.nextInt(6)+1);
        }
        return diceRolls;
    }

    public static List<Integer> decideAttackResult(List<Integer> attack, List<Integer> defend){
        int attackerLosses = 0; int defenderLosses = 0;

        while(attack.size() != 0 && defend.size() != 0){
            int attackScore = 0; int defendScore = 0;
            int attackIndex = -1; int defendIndex = -1;
            for(int i = 0; i != attack.size(); ++i){
                if(attack.get(i) > attackScore){
                    attackScore = attack.get(i);
                    attackIndex = i;
                }
            }
            for(int i = 0; i != defend.size(); ++i){
                if(defend.get(i) > defendScore){
                    defendScore = defend.get(i);
                    defendIndex = i;
                }
            }
            if(attackScore > defendScore){
                defenderLosses++;
            } else {
                attackerLosses++;
            }
            attack.remove(attackIndex);
            defend.remove(defendIndex);
        }

        List<Integer> result = new ArrayList<Integer>();
        result.add(attackerLosses);
        result.add(defenderLosses);
        return result;
    }

    public boolean isEliminated(int uid){
        for(int i = 0; i != board.getNumTerritories(); ++i){
            if(board.getOwner(i) == uid){
                return false;
            }
        }
        return true;
    }

    public boolean eliminatePlayer(int currentUID, int eliminatedUID){
        List<Card> hand = playerHands.get(currentUID);
        for(Card c : playerHands.get(eliminatedUID)){
            hand.add(c);
        }
        playerHands.get(eliminatedUID).clear();
        players.get(eliminatedUID).eliminate();
        activePlayerCount--;
        if(activePlayerCount == 1){
            return true;
        }
        return false;
    }

    public boolean checkFortifyPossible(int uid){
        for(int i = 0; i != board.getNumTerritories(); ++i){
            if(board.getOwner(i) == uid && board.getArmies(i) >= 2){
                for(int j : board.getLinks(i)){
                    if(board.getOwner(j) == uid){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
