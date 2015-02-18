package logic;

import java.util.*;

import player.*;

/**
 * game --- the main game loop that lets each player take their turn, it links player moves to the gamestate.
 */
public class Game {

    private ArrayList<IPlayer> playerInterfaces;
    private int firstPlayer;

    private Board board;
    private Deck deck;
    private ArrayList<ArrayList<Card>> playerHands;

    private int setupValues[] = {35, 30, 25, 20};
    private int setCounter = 0;
    private int armyReward = 4;
    private int setValues[] = {4, 6, 8, 10, 12, 15};

    private int totalPlayerCount = 0;
    private int activePlayerCount = 0;

    public Game(ArrayList<IPlayer> playerInterfaces, int firstPlayer, int seed, String boardFilename){
        this.playerInterfaces = new ArrayList<IPlayer>();
        this.firstPlayer = firstPlayer;
        this.playerHands = new ArrayList<ArrayList<Card>>();
        for(int i = 0; i != playerInterfaces.size(); ++i){
            IPlayer pi = playerInterfaces.get(i);
            pi.setUID(i);
            this.playerInterfaces.add(pi);
            this.playerHands.add(new ArrayList<Card>());
            totalPlayerCount++;
            activePlayerCount++;
        }
        this.board = new Board(boardFilename);
        this.deck = board.getDeck();
        this.deck.shuffle(seed);
    }

    // Whenever the game state changes, update all players
    // IPlayer implementations can decide whether or not they care about this information
    private void updatePlayers(){
        for(IPlayer p : playerInterfaces){
            p.updatePlayer(board, playerHands.get(p.getUID()));
        }
    }

    public void setupGame(){
        if(activePlayerCount < 3 || activePlayerCount > 6){
            return;
        }
        int initialArmyValue = setupValues[activePlayerCount-3];
        int totalArmies = activePlayerCount * initialArmyValue;
        int territoriesToClaim = board.getTerritories().size();

        int currentPlayer = firstPlayer;
        while(totalArmies != 0){
            updatePlayers();
            IPlayer playerInterface = playerInterfaces.get(currentPlayer);
             if(territoriesToClaim != 0){
                 int territory = playerInterface.claimTerritory("Claim a territory");
                 while(!checkClaimTerritory(territory)){
                     territory = playerInterface.claimTerritory("Invalid selection");
                 }
                 claimTerritory(currentPlayer, territory);
                 territoriesToClaim--;
             } else {
                 int territory = playerInterface.reinforceTerritory("Reinforce a territory", currentPlayer);
                 while(!checkReinforceTerritory(currentPlayer, territory)){
                     territory = playerInterface.reinforceTerritory("Invalid selection", currentPlayer);
                 }
                 reinforceTerritory(territory);
             }
             totalArmies--;
             currentPlayer++;
             if(currentPlayer == totalPlayerCount){
                 currentPlayer = 0;
             }
        }
    }

    public boolean checkClaimTerritory(int tid){
        Territory territory = board.getTerritories().get(tid);
        if(territory.getOwner() != -1){
            return false;
        }
        return true;
    }

    public void claimTerritory(int uid, int tid){
        Territory territory = board.getTerritories().get(tid);
        territory.setOwner(uid);
        territory.addArmies(1);
    }

    public boolean checkReinforceTerritory(int uid, int tid){
        Territory territory = board.getTerritories().get(tid);
        if(territory.getOwner() != uid){
            return false;
        }
        return true;
    }

    public void reinforceTerritory(int tid){
        Territory territory = board.getTerritories().get(tid);
        territory.addArmies(1);
    }

    public void playGame(){
        if(totalPlayerCount < 3 || totalPlayerCount > 6){
            return;
        }

        int currentPlayer = firstPlayer;
        while(activePlayerCount != 1){
            IPlayer playerInterface = playerInterfaces.get(currentPlayer);

            if(!playerInterface.isEliminated()){

                playerTurn(currentPlayer);
            }
            if(currentPlayer == totalPlayerCount){
                currentPlayer = 0;
            }
        }
        return;
    }

    private void playerTurn(int uid){
        updatePlayers();

        IPlayer playerInterface = playerInterfaces.get(uid);
        ArrayList<Card> hand = new ArrayList<Card>(playerHands.get(uid)); // create a copy because hand may be edited by the check function
        ArrayList<Card> toTradeIn = playerInterface.tradeInCards("Trade in cards");

        while(!checkTradeInCards(hand, toTradeIn)){
            toTradeIn = playerInterface.tradeInCards("Invalid selection");
        }
        boolean traded = tradeInCards(uid, toTradeIn); 
        updatePlayers();

        int armies = calculatePlayerArmies(uid, traded, toTradeIn);

        while(armies != 0){
            ArrayList<Integer> placeMove = playerInterface.placeArmies("Place your armies", armies);
            while(!checkPlaceArmies(uid, placeMove, armies)){
                placeMove = playerInterface.placeArmies("Invalid selection", armies);
            }
            armies = placeArmies(uid, placeMove, armies);
            updatePlayers();
        }

        boolean territoryCaptured = false;
        while(checkAttackPossible(uid) && playerInterface.decideAttack("Do you want to attack?")){

            ArrayList<Integer> attackMove = playerInterface.startAttack("Which territory do you want to attack?");
            while(!checkStartAttack(uid, attackMove)){
                attackMove = playerInterface.startAttack("Invalid selection");
            }

            int attackingDice = playerInterface.chooseAttackingDice("Attack with 1, 2, or 3 dice?");
            while(!checkAttackingDice(uid, attackingDice, attackMove.get(0))){
                attackingDice = playerInterface.chooseAttackingDice("Invalid selection");
            }

            int enemyUID = board.getTerritories().get(attackMove.get(1)).getOwner();
            int defendingDice = playerInterfaces.get(enemyUID).chooseDefendingDice("Defend with 1 or 2 dice?");
            while(!checkDefendingDice(uid, defendingDice, attackMove.get(1))){
                defendingDice = playerInterfaces.get(enemyUID).chooseDefendingDice("Invalid selection");
            }

            ArrayList<Integer> attackRoll = playerInterface.rollDice("Roll dice", attackingDice);
            while(!checkDiceRoll(attackRoll, attackingDice)){
                attackRoll = playerInterface.rollDice("Invalid roll", attackingDice);
            }

            ArrayList<Integer> defendRoll = playerInterfaces.get(enemyUID).rollDice("Roll dice", defendingDice);
            while(!checkDiceRoll(defendRoll, defendingDice)){
                defendRoll = playerInterfaces.get(enemyUID).rollDice("Invalid roll", defendingDice);
            }

            ArrayList<Integer> attackResult = decideAttackResult(attackRoll, defendRoll);

            if(loseArmies(attackResult, attackMove)){ // loseArmies returns true when the territory should be captured
                territoryCaptured = true;
                updatePlayers();
                int currentArmies = board.getTerritories().get(attackMove.get(0)).getArmies();
                int occupyArmies = playerInterface.occupyTerritory("You captured the territory, how many armies would you lose to move in?", currentArmies, attackingDice);
                while(!checkOccupyArmies(occupyArmies, attackingDice, attackMove.get(0))){
                    occupyArmies = playerInterface.occupyTerritory("Invalid number of armies", currentArmies, attackingDice);
                }
                occupyTerritory(uid, occupyArmies, attackMove);
            }

            if(isEliminated(enemyUID)){
                eliminatePlayer(uid, enemyUID);
                hand = new ArrayList<Card>(playerHands.get(uid));
                updatePlayers();
                if(hand.size() > 5){ // immediately trade in cards when at 6 or more
                    while(hand.size() >= 5){ // trade in cards and place armies until 4 or fewer cards
                        hand = new ArrayList<Card>(playerHands.get(uid));
                        toTradeIn = playerInterface.tradeInCards("Trade in cards");
                        while(!checkTradeInCards(hand, toTradeIn)){
                            toTradeIn = playerInterface.tradeInCards("Invalid selection");
                        }
                        tradeInCards(uid, toTradeIn); 
                        updatePlayers();
                    
                        armies = incrementSetCounter();
                        while(armies != 0){
                            ArrayList<Integer> placeMove = playerInterface.placeArmies("Place your armies", armies);
                            while(!checkPlaceArmies(uid, placeMove, armies)){
                                placeMove = playerInterface.placeArmies("Invalid selection", armies);
                            }
                            armies = placeArmies(uid, placeMove, armies);
                            updatePlayers();
                        }
                    }
                }
            }
        }

        if(territoryCaptured){
            Card newCard = deck.drawCard();
            playerHands.get(uid).add(newCard);
        }
        updatePlayers();

        if(checkFortifyPossible(uid)){

        if(playerInterface.decideFortify("Do you want to fortify?")){
            ArrayList<Integer> fortifyMove = playerInterface.startFortify("Which territory do you want to fortify?");
            while(!checkStartFortify(uid, fortifyMove)){
                fortifyMove = playerInterface.startFortify("Invalid selection");
            }
            int currentFortifyArmies = board.getTerritories().get(fortifyMove.get(0)).getArmies();
            int numFortifyArmies = playerInterface.chooseFortifyArmies("How many armies do you want to fortify with?", currentFortifyArmies);        
            while(!checkFortifyArmies(currentFortifyArmies, numFortifyArmies)){
                numFortifyArmies = playerInterface.chooseFortifyArmies("Invalid selection", currentFortifyArmies);
            }
            fortifyArmies(fortifyMove, numFortifyArmies);
        }
        }
    }

    public static boolean checkTradeInCards(ArrayList<Card> hand, ArrayList<Card> toTradeIn){
        if(toTradeIn.size() == 0 && hand.size() < 5){
            return true;
        }
        if(Card.containsSet(hand)){
            if(toTradeIn.size() == 3 && Card.isSubset(toTradeIn, hand)){
                return true;
            }
        }
        return false;
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

    public boolean checkPlaceArmies(int uid, ArrayList<Integer> move, int armies){
        if(move.get(1) > armies){
            return false;
        }
        if(!board.checkTerritoryOwner(uid, move.get(0))){
            return false;
        }
        return true;
    }

    public int placeArmies(int uid, ArrayList<Integer> move, int armies){
        Territory t = board.getTerritories().get(move.get(0));
        t.addArmies(move.get(1));
        return armies - move.get(1);
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

    public boolean checkStartAttack(int uid, ArrayList<Integer> move){
        Territory ally = board.getTerritories().get(move.get(0));
        Territory enemy = board.getTerritories().get(move.get(1));

        // Does this player own the territory to be attacked from?
        if(ally.getOwner() != uid){
            return false;
        }

        // Does ally have at least 2 armies?
        if(ally.getArmies() < 2){
            return false;
        }

        // Does this player not own the territory to be attacked?
        if(enemy.getOwner() == uid){
            return false;
        }
        
        // Are the two territories adjacent?
        boolean found = false;
        for(Integer i : ally.getLinks()){
            if(i == enemy.getID()){
                found = true;
            }
        }
        if(!found){
            return false;
        }

        return true;
    }

    public boolean checkAttackingDice(int uid, int numDice, int TID){
        int numArmies = board.getTerritories().get(TID).getArmies();
        if(numArmies <= numDice){ // You must have one more armies than the number of dice you wish to roll.
            return false;
        }
        return true;
    }

    public boolean checkDefendingDice(int uid, int numDice, int TID){
        int numArmies = board.getTerritories().get(TID).getArmies();
        if(numArmies < numDice){ // You must have 2 armies to roll 2 dice.
            return false;
        }
        return true;
    }

    public boolean checkDiceRoll(ArrayList<Integer> roll, int numDice){
        if(roll.size() != numDice){
            return false;
        }
        for(int i : roll){
            if(i < 1 || i > 6){
                return false;
            }
        }
        return true;
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
    public boolean loseArmies(ArrayList<Integer> attackResult, ArrayList<Integer> attackMove){
        for(int i = 0; i != 2; ++i){
            Territory t = board.getTerritories().get(attackMove.get(i));
            t.loseArmies(attackResult.get(i));
            if(i == 1 && t.getArmies() == 0){
                return true;
            }
        }
        return false;
    }

    public boolean checkOccupyArmies(int armies, int numDice, int TID){
        if(armies < numDice){
            return false;
        }
        int currentArmies = board.getTerritories().get(TID).getArmies();
        if((currentArmies - armies) < 1){
            return false;
        }
        return true;
    }

    public void occupyTerritory(int uid, int armies, ArrayList<Integer> attackMove){
        Territory attacker = board.getTerritories().get(attackMove.get(0));
        attacker.loseArmies(armies);
        Territory defender = board.getTerritories().get(attackMove.get(1));
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

    public void eliminatePlayer(int currentUID, int eliminatedUID){
        ArrayList<Card> hand = playerHands.get(currentUID);
        for(Card c : playerHands.get(eliminatedUID)){
            hand.add(c);
        }
        playerHands.get(eliminatedUID).clear();
        playerInterfaces.get(eliminatedUID).eliminate();
        activePlayerCount--;
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

    public boolean checkStartFortify(int uid, ArrayList<Integer> move){
        Territory ally = board.getTerritories().get(move.get(0));
        Territory fortify = board.getTerritories().get(move.get(1));

        // Does this player own the territory to be attacked from?
        if(ally.getOwner() != uid){
            return false;
        }

        // Does this territory have at least 2 armies?
        if(ally.getArmies() < 2){
            return false;
        }

        // Does this player own the territory to be fortified?
        if(fortify.getOwner() != uid){
            return false;
        }
        
        // Are the two territories adjacent?
        boolean found = false;
        for(Integer i : ally.getLinks()){
            if(i == fortify.getID()){
                found = true;
            }
        }
        if(!found){
            return false;
        }
        return true;
        
    }

    public boolean checkFortifyArmies(int currentFortifyArmies, int numFortifyArmies){
        return ((currentFortifyArmies - numFortifyArmies) >= 1);
    }

    public void fortifyArmies(ArrayList<Integer> move, int numFortifyArmies){
        Territory ally = board.getTerritories().get(move.get(0));
        Territory fortify = board.getTerritories().get(move.get(1));
        ally.loseArmies(numFortifyArmies);
        fortify.addArmies(numFortifyArmies);
    }
}
