package logic;

import java.util.ArrayList;

/**
 * GameMove --- Stores information about a game move.
 */
public class GameMove {

    private int stage; // 1, 2, 3, or 4
    // 1 = Trade In Cards
    // 2 = Deploy New Armies
    // 3 = Attack OR Fortify
    // 4 = Draw Card

    private ArrayList<Integer> Stage1TradeInCards;

    public GameMove(int stage){
        this.stage = stage;
    }

//    public FillStage1(ArrayList<Integer> cards){
//       
//    }

}


