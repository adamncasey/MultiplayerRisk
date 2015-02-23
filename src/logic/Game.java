package logic;

import java.util.*;

import player.*;

/**
 * game --- the main game loop that lets each player take their turn, it links player moves to the gamestate.
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
        updatePlayers(0, null);
    }

    private void informPlayers(int currentPlayer, String currentMove){
        for(IPlayer p : players){
            p.nextMove(currentPlayer, currentMove);
        }
    }

    private void updatePlayers(int currentPlayer, Move previousMove){
        for(IPlayer p : players){
            p.updatePlayer(board, playerHands.get(p.getUID()), currentPlayer, previousMove);
        }
        checker.update(board);
    }

    private void endGame(int winner){
        for(IPlayer p : players){
            p.endGame(winner);
        }
    }

    public void setupGame() throws WrongMoveException{
        if(activePlayerCount < 3 || activePlayerCount > 6){
            return;
        }
        int initialArmyValue = setupValues[activePlayerCount-3];
        int totalArmies = activePlayerCount * initialArmyValue;
        int territoriesToClaim = board.getTerritories().size();

        int currentPlayer = firstPlayer;
        while(totalArmies != 0){
            IPlayer player = players.get(currentPlayer);
            if(territoriesToClaim != 0){
                informPlayers(currentPlayer, "claming a territory");
                Move move = new Move(0);
                move = getMove(currentPlayer, 0, move);
                int territoryToClaim = move.getTerritoryToClaim();
                claimTerritory(currentPlayer, territoryToClaim);
                territoriesToClaim--;
                updatePlayers(currentPlayer, move);
            } else {
                informPlayers(currentPlayer, "reinforcing a territory");
                Move move = new Move(1);
                move = getMove(currentPlayer, 1, move);
                int territoryToReinforce = move.getTerritoryToReinforce();
                reinforceTerritory(territoryToReinforce);
                updatePlayers(currentPlayer, move);
            }
            totalArmies--;
            currentPlayer++;
            if(currentPlayer == totalPlayerCount){
                currentPlayer = 0;
            }
        }
    }

    public int playGame() throws WrongMoveException{
        if(totalPlayerCount < 3 || totalPlayerCount > 6){
            return 0;
        }
        int turnCounter = 0;
        int currentPlayer = firstPlayer;
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
        return turnCounter;
    }

    private void playerTurn(int uid) throws WrongMoveException{
        IPlayer player = players.get(uid);

        informPlayers(uid, "trading in cards");
        Move move = new Move(2);
        move = getMove(uid, 2, move);
        ArrayList<Card> toTradeIn = move.getToTradeIn();
        boolean traded = tradeInCards(uid, toTradeIn); 
        updatePlayers(uid, move);

        int armies = calculatePlayerArmies(uid, traded, toTradeIn);
        while(armies != 0){
            informPlayers(uid, "placing armies");
            move = new Move(3);
            move.setArmiesToPlace(armies);
            move = getMove(uid, 3, move);
            armies = placeArmies(uid, move.getPlaceArmiesTerritory(), move.getPlaceArmiesNum(), armies);
            updatePlayers(uid, move);
        }

        boolean territoryCaptured = false;
        while(checkAttackPossible(uid)){
            informPlayers(uid, "deciding whether or not to attack");
            move = new Move(4);
            move = getMove(uid, 4, move);
            updatePlayers(uid, move);

            informPlayers(uid, "choosing where to attack");
            move = new Move(5);
            move = getMove(uid, 5, move);
            int attackFrom = move.getAttackFrom();
            int attackTo = move.getAttackTo();
            updatePlayers(uid, move);

            informPlayers(uid, "deciding how many dice to attack with");
            move = new Move(6);
            move.setAttackingNumArmies(board.getTerritories().get(attackFrom).getArmies());
            move = getMove(uid, 6, move);
            int attackingDice = move.getAttackingDice();
            updatePlayers(uid, move);

            int enemyUID = board.getTerritories().get(attackTo).getOwner();
            informPlayers(enemyUID, "deciding how many dice to defend with");
            move = new Move(7);
            move.setDefendingNumArmies(board.getTerritories().get(attackTo).getArmies());
            move = getMove(uid, 7, move);
            int defendingDice = move.getDefendingDice();
            updatePlayers(uid, move);
 
            ArrayList<Integer> attackDiceRolls = rollDice(attackingDice);
            ArrayList<Integer> defendDiceRolls = rollDice(defendingDice);
            ArrayList<Integer> attackResult = decideAttackResult(attackDiceRolls, defendDiceRolls);
            if(loseArmies(attackResult, attackFrom, attackTo)){ // loseArmies returns true when the territory should be captured
                territoryCaptured = true;
                updatePlayers(uid, null);
                informPlayers(uid, "capturing the territory");
                move = new Move(8);
                move.setOccupyCurrentArmies(board.getTerritories().get(attackFrom).getArmies());
                move.setOccupyDice(attackingDice);
                move = getMove(uid, 8, move);
                int occupyArmies = move.getOccupyArmies();
                occupyTerritory(uid, occupyArmies, attackFrom, attackTo);
                updatePlayers(uid, move);
            }

            if(isEliminated(enemyUID)){
                informPlayers(enemyUID, "eliminated");
                if(eliminatePlayer(uid, enemyUID)){
                    return;
                }
                updatePlayers(uid, null);
                ArrayList<Card> hand = playerHands.get(uid);
                if(hand.size() > 5){ // immediately trade in cards when at 6 or more
                    while(hand.size() >= 5){ // trade in cards and place armies until 4 or fewer cards
                        informPlayers(uid, "trading in cards");
                        move = new Move(2);
                        move = getMove(uid, 2, move);
                        toTradeIn = move.getToTradeIn();
                        tradeInCards(uid, toTradeIn); 
                        updatePlayers(uid, move);
                    
                        armies = incrementSetCounter();
                        while(armies != 0){
                            informPlayers(uid, "placing armies");
                            move = new Move(3);
                            move.setArmiesToPlace(armies);
                            move = getMove(uid, 3, move);
                            armies = placeArmies(uid, move.getPlaceArmiesTerritory(), move.getPlaceArmiesNum(), armies);
                            updatePlayers(uid, move);
                        }
                    }
                }
            }
        }

        if(territoryCaptured){
            informPlayers(uid, "drawing a card");
            Card newCard = deck.drawCard();
            if(newCard != null){
                playerHands.get(uid).add(newCard);
            }
            updatePlayers(uid, null);
        }

        if(checkFortifyPossible(uid)){
            informPlayers(uid, "deciding whether or not to fortify");
            move = new Move(9);
            move = getMove(uid, 9, move);
            updatePlayers(uid, move);

            boolean decideFortify = move.getDecideFortify();
            if(decideFortify){
                informPlayers(uid, "choosing where to fortify");
                move = new Move(10);
                move = getMove(uid, 10, move);
                int fortifyFrom = move.getFortifyFrom();
                int fortifyTo = move.getFortifyTo();
                updatePlayers(uid, move);

                informPlayers(uid, "deciding how many armies to fortify with");
                move = new Move(11);
                move.setFortifyCurrentArmies(board.getTerritories().get(fortifyFrom).getArmies());
                move = getMove(uid, 11, move);
                int numFortifyArmies = move.getFortifyArmies();
                fortifyArmies(fortifyFrom, fortifyTo, numFortifyArmies);
                updatePlayers(uid, move);
            }
        }
    }

    public Move getMove(int currentPlayer, int stage, Move move) throws WrongMoveException{
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
                int attackingNumArmies = move.getAttackingNumArmies();
                return checker.checkAttackingDice(attackingDice, attackingNumArmies);
            case 7:
                int defendingDice = move.getDefendingDice();
                int defendingNumArmies = move.getDefendingNumArmies();
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

    public void claimTerritory(int uid, int tid){
        Territory territory = board.getTerritories().get(tid);
        territory.setOwner(uid);
        territory.addArmies(1);
    }

    public void reinforceTerritory(int tid){
        Territory territory = board.getTerritories().get(tid);
        territory.addArmies(1);
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
            if(board.checkTerritoryOwner(uid, card.getID())){
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

    public int placeArmies(int uid, int territory, int armiesBeingPlaced, int armiesToPlace){
        Territory t = board.getTerritories().get(territory);
        t.addArmies(armiesBeingPlaced);
        return armiesToPlace - armiesBeingPlaced;
    }

    public boolean checkAttackPossible(int uid){
        for(Territory t : board.getTerritories().values()){
            if(t.getOwner() == uid && t.getArmies() >= 2){
                for(int i : t.getLinks()){
                    if(!board.checkTerritoryOwner(uid, i)){
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

    // Return value decides whether or not the territory should be captured
    public boolean loseArmies(ArrayList<Integer> attackResult, int attackFrom, int attackTo){
        Territory t1 = board.getTerritories().get(attackFrom);
        t1.loseArmies(attackResult.get(0));
        Territory t2 = board.getTerritories().get(attackTo);
        t2.loseArmies(attackResult.get(1));
        if(t2.getArmies() == 0){
            return true;
        }
        return false;
    }

    public void occupyTerritory(int uid, int armies, int attackFrom, int attackTo){
        Territory attacker = board.getTerritories().get(attackFrom);
        attacker.loseArmies(armies);
        Territory defender = board.getTerritories().get(attackTo);
        defender.addArmies(armies);
        defender.setOwner(uid);
    }

    public boolean isEliminated(int uid){
        for(Territory t : board.getTerritories().values()){
            if(t.getOwner() == uid){
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
            endGame(currentUID);
            return true;
        }
        return false;
    }

    public boolean checkFortifyPossible(int uid){
        for(Territory t : board.getTerritories().values()){
            if(t.getOwner() == uid && t.getArmies() >= 2){
                for(int i : t.getLinks()){
                    if(board.checkTerritoryOwner(uid, i)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void fortifyArmies(int fortifyFrom, int fortifyTo, int numFortifyArmies){
        Territory ally = board.getTerritories().get(fortifyFrom);
        Territory fortify = board.getTerritories().get(fortifyTo);
        ally.loseArmies(numFortifyArmies);
        fortify.addArmies(numFortifyArmies);
    }
}
