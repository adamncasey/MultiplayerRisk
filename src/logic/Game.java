package logic;

import java.util.*;

import player.*;

/**
 * Game --- The main game loop that lets each player take their turn, updating every player whenever anything happens.
 */
public class Game {
    private static Random random;

    private ArrayList<IPlayer> players;
    private int firstPlayer;

    private Board board;
    private Deck deck;
    private ArrayList<ArrayList<Card>> playerHands;
    private MoveChecker checker;

    private int setupValues[] = {35, 30, 25, 20};
    private int setCounter = 0;
    private int armyReward = 4;
    private int setValues[] = {4, 6, 8, 10, 12, 15};

    private static String[] moveDescriptions = {"claiming a territory",
                                                "reinforcing a territory",
                                                "trading in cards",
                                                "placing armies",
                                                "deciding whether or not to attack",
                                                "choosing where to attack",
                                                "deciding how many dice to attack with",
                                                "deciding how many dice to defend with",
                                                "capturing the territory",
                                                "deciding whether or not to fortify",
                                                "choosing where to fortify",
                                                "deciding how many armies to fortify with"};

    private int totalPlayerCount = 0;
    private int activePlayerCount = 0;

    public Game(ArrayList<IPlayer> players, int firstPlayer, int seed, String boardFilename){
        this.random = new Random(seed);
        this.players = new ArrayList<IPlayer>();
        this.firstPlayer = firstPlayer;
        this.playerHands = new ArrayList<ArrayList<Card>>();
        for(int i = 0; i != players.size(); ++i){
            IPlayer pi = players.get(i);
            pi.setUID(i);
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
        for(IPlayer p : players){
            p.updatePlayer(board, playerHands.get(p.getUID()), currentPlayer, previousMove);
        }
        checker.update(board);
        String message = MoveProcessor.processMove(currentPlayer, previousMove, board);
    }

    public void setupGame() throws WrongMoveException{
        if(activePlayerCount < 3 || activePlayerCount > 6){
            return;
        }
        updatePlayers(activePlayerCount, new Move(104));
        int initialArmyValue = setupValues[activePlayerCount-3];
        int totalArmies = activePlayerCount * initialArmyValue;
        int territoriesToClaim = board.getNumTerritories();

        int currentPlayer = firstPlayer;
        while(totalArmies != 0){
            IPlayer player = players.get(currentPlayer);
            if(territoriesToClaim != 0){
                Move move = new Move(0);
                move = getMove(currentPlayer, 0, move);
                int territoryToClaim = move.getTerritoryToClaim();
                board.claimTerritory(territoryToClaim, currentPlayer);
                board.placeArmies(territoryToClaim, 1);
                territoriesToClaim--;
                updatePlayers(currentPlayer, move);
            } else {
                Move move = new Move(1);
                move = getMove(currentPlayer, 1, move);
                int territoryToReinforce = move.getTerritoryToReinforce();
                board.placeArmies(territoryToReinforce, 1);
                updatePlayers(currentPlayer, move);
            }
            totalArmies--;
            currentPlayer++;
            if(currentPlayer == totalPlayerCount){
                currentPlayer = 0;
            }
        }
        updatePlayers(activePlayerCount, new Move(105));
    }

    public void playGame() throws WrongMoveException{
        if(totalPlayerCount < 3 || totalPlayerCount > 6){
            return;
        }
        int turnCounter = 0;
        int currentPlayer = firstPlayer;
        updatePlayers(currentPlayer, new Move(106));
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
        updatePlayers(currentPlayer, new Move(107));
        updatePlayers(turnCounter, new Move(108));
        return;
    }

    private void playerTurn(int uid) throws WrongMoveException{
        IPlayer player = players.get(uid);

        Move move = new Move(2);
        move = getMove(uid, 2, move);
        ArrayList<Card> toTradeIn = move.getToTradeIn();
        boolean traded = tradeInCards(uid, toTradeIn); 
        updatePlayers(uid, move);

        int armies = calculatePlayerArmies(uid, traded, toTradeIn);
        while(armies != 0){
            move = new Move(3);
            move.setArmiesToPlace(armies);
            move = getMove(uid, 3, move);
            board.placeArmies(move.getPlaceArmiesTerritory(), move.getPlaceArmiesNum());
            armies -= move.getPlaceArmiesNum();
            updatePlayers(uid, move);
        }

        boolean territoryCaptured = false;
        while(checkAttackPossible(uid)){
            move = new Move(4);
            move = getMove(uid, 4, move);
            updatePlayers(uid, move);
            boolean attacking = move.getDecideAttack();
            if(!attacking){
                break;
            }

            move = new Move(5);
            move = getMove(uid, 5, move);
            int attackFrom = move.getAttackFrom();
            int attackTo = move.getAttackTo();
            updatePlayers(uid, move);

            move = new Move(6);
            move.setAttackingFrom(attackFrom);
            move.setAttackingTo(attackTo);
            move = getMove(uid, 6, move);
            int attackingDice = move.getAttackingDice();
            updatePlayers(uid, move);

            int enemyUID = board.getOwner(attackTo);
            move = new Move(7);
            move.setDefendingFrom(attackTo);
            move.setDefendingTo(attackFrom);
            move = getMove(enemyUID, 7, move);
            int defendingDice = move.getDefendingDice();
            updatePlayers(enemyUID, move);
 
            ArrayList<Integer> attackDiceRolls = rollDice(attackingDice);
            ArrayList<Integer> defendDiceRolls = rollDice(defendingDice);
            ArrayList<Integer> attackResult = decideAttackResult(attackDiceRolls, defendDiceRolls);
            board.placeArmies(attackFrom, -attackResult.get(0));
            board.placeArmies(attackTo, -attackResult.get(1));
            boolean willCaptureTerritory = board.getArmies(attackTo) == 0;

            move = new Move(101);
            move.setAttackerLosses(attackResult.get(0));
            move.setDefenderLosses(attackResult.get(1));
            updatePlayers(uid, move);

            if(willCaptureTerritory){ 
                territoryCaptured = true;
                move = new Move(8);
                move.setOccupyCurrentArmies(board.getArmies(attackFrom));
                move.setOccupyDice(attackingDice);
                move = getMove(uid, 8, move);
                int occupyArmies = move.getOccupyArmies();
                board.placeArmies(attackFrom, -occupyArmies);
                board.placeArmies(attackTo, occupyArmies);
                board.claimTerritory(attackTo, uid);
                updatePlayers(uid, move);
            }

            if(isEliminated(enemyUID)){
                if(eliminatePlayer(uid, enemyUID)){
                    return;
                }
                updatePlayers(enemyUID, new Move(102));
                ArrayList<Card> hand = playerHands.get(uid);
                if(hand.size() > 5){ // immediately trade in cards when at 6 or more
                    while(hand.size() >= 5){ // trade in cards and place armies until 4 or fewer cards
                        move = new Move(2);
                        move = getMove(uid, 2, move);
                        toTradeIn = move.getToTradeIn();
                        tradeInCards(uid, toTradeIn); 
                        updatePlayers(uid, move);
                    
                        armies = incrementSetCounter();
                        while(armies != 0){
                            move = new Move(3);
                            move.setArmiesToPlace(armies);
                            move = getMove(uid, 3, move);
                            board.placeArmies(move.getPlaceArmiesTerritory(), move.getPlaceArmiesNum());
                            armies -= move.getPlaceArmiesNum();
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
                updatePlayers(uid, new Move(103));
            }
        }

        if(checkFortifyPossible(uid)){
            move = new Move(9);
            move = getMove(uid, 9, move);
            updatePlayers(uid, move);

            boolean decideFortify = move.getDecideFortify();
            if(decideFortify){
                move = new Move(10);
                move = getMove(uid, 10, move);
                int fortifyFrom = move.getFortifyFrom();
                int fortifyTo = move.getFortifyTo();
                updatePlayers(uid, move);

                move = new Move(11);
                move.setFortifyCurrentArmies(board.getArmies(fortifyFrom));
                move = getMove(uid, 11, move);
                int numFortifyArmies = move.getFortifyArmies();
                board.placeArmies(fortifyFrom, -numFortifyArmies);
                board.placeArmies(fortifyTo, numFortifyArmies);
                updatePlayers(uid, move);
            }
        }
    }

    public Move getMove(int currentPlayer, int stage, Move move) throws WrongMoveException{
        for(IPlayer p : players){
            p.nextMove(currentPlayer, moveDescriptions[stage]);
        }

        IPlayer player = players.get(currentPlayer);
        move = player.getMove(move);
        while(!checkMove(currentPlayer, stage, move)){
            move = player.getMove(move);
        }
        return move;
    }

    public boolean checkMove(int currentPlayer, int stage, Move move) throws WrongMoveException{
        if(move == null){
            return false;
        }
        if(stage != move.getStage()){
            return false;
        }
        switch(stage){
            case 0:
                int territoryToClaim = move.getTerritoryToClaim();
                return checker.checkClaimTerritory(territoryToClaim);
            case 1:
                int territoryToReinforce = move.getTerritoryToReinforce();
                return checker.checkReinforceTerritory(currentPlayer, territoryToReinforce);
            case 2:
                ArrayList<Card> hand = playerHands.get(currentPlayer); 
                ArrayList<Card> toTradeIn = move.getToTradeIn();
                return checker.checkTradeInCards(hand, toTradeIn);
            case 3:
                int placeArmiesTerritory = move.getPlaceArmiesTerritory();
                int placeArmiesNum = move.getPlaceArmiesNum();
                int armiesToPlace = move.getArmiesToPlace();
                return checker.checkPlaceArmies(currentPlayer, placeArmiesTerritory, placeArmiesNum, armiesToPlace);
            case 4:
                return true;
            case 5:
                int attackFrom = move.getAttackFrom();
                int attackTo = move.getAttackTo();
                return checker.checkStartAttack(currentPlayer, attackFrom, attackTo);
            case 6:
                int attackingDice = move.getAttackingDice();
                int attackingNumArmies = board.getArmies(move.getAttackingFrom());
                return checker.checkAttackingDice(attackingDice, attackingNumArmies);
            case 7:
                int defendingDice = move.getDefendingDice();
                int defendingNumArmies = board.getArmies(move.getDefendingFrom());
                return checker.checkDefendingDice(defendingDice, defendingNumArmies);
            case 8:
                int occupyArmies = move.getOccupyArmies();
                int occupyDice = move.getOccupyDice();
                int occupyCurrentArmies = move.getOccupyCurrentArmies();
                return checker.checkOccupyArmies(occupyArmies, occupyDice, occupyCurrentArmies);
            case 9:
                return true;
            case 10:
                int fortifyFrom = move.getFortifyFrom();
                int fortifyTo = move.getFortifyTo();
                return checker.checkStartFortify(currentPlayer, fortifyFrom, fortifyTo);
            case 11:
                int fortifyArmies = move.getFortifyArmies();
                int fortifyCurrentArmies = move.getFortifyCurrentArmies();
                return checker.checkFortifyArmies(fortifyArmies, fortifyCurrentArmies);
            default:
                return false;
        }
    }

    public boolean tradeInCards(int uid, ArrayList<Card> toTradeIn){
        ArrayList<Card> hand = playerHands.get(uid);
        for(Card c: toTradeIn){
            hand.remove(c);
        }
        if(toTradeIn.size() == 3){
            return true;
        }
        return false;
    }

    public int calculatePlayerArmies(int uid, boolean traded, ArrayList<Card> toTradeIn){
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

    public static ArrayList<Integer> rollDice(int numDice){
        ArrayList<Integer> diceRolls = new ArrayList<Integer>();
        for(int i = 0; i != numDice; ++i){
            diceRolls.add(random.nextInt(6)+1);
        }
        return diceRolls;
    }

    public static ArrayList<Integer> decideAttackResult(ArrayList<Integer> attack, ArrayList<Integer> defend){
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

        ArrayList<Integer> result = new ArrayList<Integer>();
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
        ArrayList<Card> hand = playerHands.get(currentUID);
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
