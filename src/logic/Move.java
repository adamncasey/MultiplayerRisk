package logic;

import java.util.ArrayList;


public class Move {
    // Stage 0 = claimTerritory
    // returns int - id of territory to claim
    // Stage 1 = reinforceTerritory
    // returns int - id of territory to claim
    // Stage 2 = tradeInCards
    // returns ArrayList<Card> cards to be traded in
    // Stage 3 = placeArmies
    // returns int - id of territory to reinforce
    // returns int - number of armies to reinforce with
    // Stage 4 = decideAttack
    // returns boolean - whether or not the player wants to attack
    // Stage 5 = startAttack
    // returns int - id of territory to attack from
    // returns int - id of territory to attack
    // Stage 6 = chooseAttackDice
    // returns int - 1, 2, or 3 dice
    // Stage 7 = chooseDefendDice
    // returns int - 1, or 2 dice
    // Stage 8 = occupyTerritory
    // returns int - the number of armies to move from attacking territory to attacked territory
    // Stage 9 = decideFortify
    // returns boolean - whether or not to fortify
    // Stage 10 = startFortify
    // returns int - id of territory to fortify from
    // returns int - id of territory to fortify
    // Stage 11 = chooseFortifyArmies
    // returns int - number of armies to fortify with

    // These stages are used to let IPlayer know what has just changed (in situations where the game updates but a player hasn't had to input anything)
    // Stage 101 - An attack has just been carried out (After dice rolls the board is updated)
    // Stage 102 - The player has just been eliminated
    // Stage 103 - The player has just drawn a card
    // Stage 104 - Game setup is beginning
    // Stage 105 - Game setup has ended
    // Stage 106 - Game is beginning
    // Stage 107 - Game had ended (winner)
    // Stage 108 - Game has ended (numTurns)

    private int stage;

    public Move(int stage){
        this.stage = stage;
    }

    public int getStage(){
        return this.stage;
    }

    //stage 0
    private int territoryToClaim = -1;
    public void setTerritoryToClaim(int territory) throws WrongMoveException{
        checkStage(0);
        this.territoryToClaim = territory;
    }
    public int getTerritoryToClaim() throws WrongMoveException{
        checkStage(0);
        return this.territoryToClaim;

    }

    //stage 1
    private int territoryToReinforce = -1;
    public void setTerritoryToReinforce(int territory) throws WrongMoveException{
        checkStage(1);
        this.territoryToReinforce = territory;
    }
    public int getTerritoryToReinforce() throws WrongMoveException{
        checkStage(1);
        return this.territoryToReinforce;
    }

    //stage 2
    private ArrayList<Card> toTradeIn = null;
    public void setToTradeIn(ArrayList<Card> cards) throws WrongMoveException{
        checkStage(2);
        this.toTradeIn = new ArrayList<Card>(cards);
    }
    public ArrayList<Card> getToTradeIn() throws WrongMoveException{
        checkStage(2);
        return this.toTradeIn;
    }

    //stage 3
    private int placeArmiesTerritory = -1;
    private int placeArmiesNum = -1;
    public void setPlaceArmiesTerritory(int territory) throws WrongMoveException{
        checkStage(3);
        this.placeArmiesTerritory = territory;
    }
    public void setPlaceArmiesNum(int numArmies) throws WrongMoveException{
        checkStage(3);
        this.placeArmiesNum = numArmies;
    }
    public int getPlaceArmiesTerritory() throws WrongMoveException{
        checkStage(3);
        return this.placeArmiesTerritory;
    }
    public int getPlaceArmiesNum() throws WrongMoveException{
        checkStage(3);
        return this.placeArmiesNum;
    }
    //stage 3 inputs (Game will provide these for IPlayer to use)
    private int armiesToPlace = 0;
    public void setArmiesToPlace(int armiesToPlace) throws WrongMoveException{
        checkStage(3);
        this.armiesToPlace = armiesToPlace;
    }
    public int getArmiesToPlace() throws WrongMoveException{
        checkStage(3);
        return this.armiesToPlace;
    }


    //stage 4
    private boolean decideAttack = false;
    public void setDecideAttack(boolean decision) throws WrongMoveException{
        checkStage(4);
        this.decideAttack = decision;
    }
    public boolean getDecideAttack() throws WrongMoveException{
        checkStage(4);
        return this.decideAttack;
    }

    //stage 5
    private int attackFrom = -1;
    private int attackTo = -1;
    public void setAttackFrom(int territory) throws WrongMoveException{
        checkStage(5);
        this.attackFrom = territory;
    }
    public void setAttackTo(int territory) throws WrongMoveException{
        checkStage(5);
        this.attackTo = territory;
    }
    public int getAttackFrom() throws WrongMoveException{
        checkStage(5);
        return this.attackFrom;
    }
    public int getAttackTo() throws WrongMoveException{
        checkStage(5);
        return this.attackTo;
    }

    //stage 6
    private int attackingDice = 0;
    public void setAttackingDice(int numDice) throws WrongMoveException{
        checkStage(6);
        this.attackingDice = numDice;
    }
    public int getAttackingDice() throws WrongMoveException{
        checkStage(6);
        return this.attackingDice;
    }
    //stage 6 inputs
    private int attackingFrom = -1;
    public void setAttackingFrom(int territory) throws WrongMoveException{
        checkStage(6);
        this.attackingFrom = territory;
    }
    public int getAttackingFrom() throws WrongMoveException{
        checkStage(6);
        return this.attackingFrom;
    }
    private int attackingTo = -1;
    public void setAttackingTo(int territory) throws WrongMoveException{
        checkStage(6);
        this.attackingTo = territory;
    }
    public int getAttackingTo() throws WrongMoveException{
        checkStage(6);
        return this.attackingTo;
    }
    
    //stage 7
    private int defendingDice = 0;
    public void setDefendingDice(int numDice) throws WrongMoveException{
        checkStage(7);
        this.defendingDice = numDice;
    }
    public int getDefendingDice() throws WrongMoveException{
        checkStage(7);
        return this.defendingDice;
    }
    //stage 7 inputs
    private int defendingFrom = -1;
    public void setDefendingFrom(int territory) throws WrongMoveException{
        checkStage(7);
        this.defendingFrom = territory;
    }
    public int getDefendingFrom() throws WrongMoveException{
        checkStage(7);
        return this.defendingFrom;
    }
    private int defendingTo = -1;
    public void setDefendingTo(int territory) throws WrongMoveException{
        checkStage(7);
        this.defendingTo = territory;
    }
    public int getDefendingTo() throws WrongMoveException{
        checkStage(7);
        return this.defendingTo;
    }

    //stage 8
    private int occupyArmies = 0;
    public void setOccupyArmies(int numArmies) throws WrongMoveException{
        checkStage(8);
        this.occupyArmies = numArmies;
    }
    public int getOccupyArmies() throws WrongMoveException{
        checkStage(8);
        return this.occupyArmies;
    }
    //stage 8 inputs
    private int occupyCurrentArmies = 0;
    public void setOccupyCurrentArmies(int numArmies) throws WrongMoveException{
        checkStage(8);
        this.occupyCurrentArmies = numArmies;
    }
    public int getOccupyCurrentArmies() throws WrongMoveException{
        checkStage(8);
        return this.occupyCurrentArmies;
    }
    private int occupyDice = 0;
    public void setOccupyDice(int numDice) throws WrongMoveException{
        checkStage(8);
        this.occupyDice = numDice;
    }
    public int getOccupyDice() throws WrongMoveException{
        checkStage(8);
        return this.occupyDice;
    }

    //stage 9
    private boolean decideFortify = false;
    public void setDecideFortify(boolean decision) throws WrongMoveException{
        checkStage(9);
        this.decideFortify = decision;
    }
    public boolean getDecideFortify() throws WrongMoveException{
        checkStage(9);
        return this.decideFortify;
    }

    //stage 10
    private int fortifyFrom = -1;
    private int fortifyTo = -1;
    public void setFortifyFrom(int territory) throws WrongMoveException{
        checkStage(10);
        this.fortifyFrom = territory;
    }
    public void setFortifyTo(int territory) throws WrongMoveException{
        checkStage(10);
        this.fortifyTo = territory;
    }
    public int getFortifyFrom() throws WrongMoveException{
        checkStage(10);
        return this.fortifyFrom;
    }
    public int getFortifyTo() throws WrongMoveException{
        checkStage(10);
        return this.fortifyTo;
    }

    //stage 11
    private int fortifyArmies = 0;
    public void setFortifyArmies(int numArmies) throws WrongMoveException{
        checkStage(11);
        this.fortifyArmies = numArmies;
    }
    public int getFortifyArmies() throws WrongMoveException{
        checkStage(11);
        return this.fortifyArmies;
    }
    //stage 11 inputs
    private int fortifyCurrentArmies = 0;
    public void setFortifyCurrentArmies(int numArmies) throws WrongMoveException{
        checkStage(11);
        this.fortifyCurrentArmies = numArmies;
    }
    public int getFortifyCurrentArmies() throws WrongMoveException{
        checkStage(11);
        return this.fortifyCurrentArmies;
    }

    //stage 101
    private int attackerLosses = 0;
    public void setAttackerLosses(int numLosses) throws WrongMoveException{
        checkStage(101);
        this.attackerLosses = numLosses;
    }
    public int getAttackerLosses() throws WrongMoveException{
        checkStage(101);
        return this.attackerLosses;
    }
    private int defenderLosses = 0;
    public void setDefenderLosses(int numLosses) throws WrongMoveException{
        checkStage(101);
        this.defenderLosses = numLosses;
    }
    public int getDefenderLosses() throws WrongMoveException{
        checkStage(101);
        return this.defenderLosses;
    }

    private void checkStage(int stage) throws WrongMoveException{
        if(this.stage != stage){
            StackTraceElement[] ste = Thread.currentThread().getStackTrace();
            String callingMethod = ste[ste.length - 8].getMethodName();
            String message = String.format("%s cannot be accessed from stage %d.", callingMethod, stage);
            throw new WrongMoveException(message);
        }
    }
}
