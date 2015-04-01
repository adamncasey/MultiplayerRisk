package logic.rng;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RNG {

    private static Random random = new Random(0);

    public static List<Integer> getDiceRolls(List<Int256> seedValues, int numRolls, int numDiceFaces){
//TEMP
        List<Integer> rolls = new ArrayList<Integer>();
        for(int i = 0; i != numRolls; ++i){
            int roll = random.nextInt(6)+1;
            rolls.add(roll);
        }
        return rolls; 
//

        // XOR seedValues together

        // Feed into RC4 somehow

        // Read numRolls values from RC4 somehow
    }
}
