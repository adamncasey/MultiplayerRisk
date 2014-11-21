package logic;

/**
 * Board --- Stores information about the game board.
 */
public class Board {

    // t[n][0] = territory number
    // t[n][1] = player id
    // t[n][2] = number of armies // not sure if its important to remember what cards make up the army (or if it was an initial army)
    public int[42][3] territories;

    // TODO - way to store links between territories.

   /**
    * Take a board file and initiate this object.
    */
    public void loadBoard(String filename){ 
    }

    // add one Infantry to any unclaimed territory on the board
    public void setup2ClaimTerritory(GameMove move) throws InvalidTerritoryException {
    }

    // add one Army to any unoccupied territory
    public void setup3OccupyTerritory(GameMove move) throws InvalidTerritoryException {
    }

    // place one additional army onto any territory claimed by that player)
    public void setup4AddOneArmy(GameMove move) throws InvalidTerritoryException {
    }

    // add armies to the board as per rules
    public void game1AddArmies(GameMove move) throws InvalidTerritoryException {
    }

    // attack
    public void game2Attack(GameMove move) throws InvalidTerritoryException {
    }

    // fortify
    public void game3Fortify(GameMove move) throws InvalidTerritoryException {
    }

    public Board(String filename){
        loadBoard(filename);
    } 
}


