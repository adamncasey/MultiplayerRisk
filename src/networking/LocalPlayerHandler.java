package networking;

import logic.move.Move;
import logic.rng.Int256;
import logic.rng.RNG;

public class LocalPlayerHandler {

    // Instead of Main methods instantiating this, it should come from networking?
    public LocalPlayerHandler(){
    }

    public void sendMove(Move move) {
        // CARD_DRAWN goes here
    }

    public void handleRoll(Move move) {
        if(move.getStage() == Move.Stage.ROLL_HASH){
             move.setRollHash(move.getRNG().hash.string);
        }
        if(move.getStage() == Move.Stage.ROLL_NUMBER){
             move.setRollNumber(move.getRNG().number.string);
        }
    }
}
