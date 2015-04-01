package networking;

import logic.move.Move;
import logic.rng.Int256;

public class LocalPlayerHandler {

    // Instead of Main methods instantiating this, it should come from networking?
    public LocalPlayerHandler(){
    }

    public void sendMove(Move move){

    }

    public void getRollHash(Move move){
        move.setRollHash(new Int256());
    }
}
