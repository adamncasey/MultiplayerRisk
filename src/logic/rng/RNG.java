package logic.rng;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RNG {
    private static Random random = new Random();

    public final Int256 hash;
    public final Int256 number;

    public RNG() {
        this.number = Int256.fromRandom();
        this.hash = Int256.fromHash(number);
    };

    public static List<Integer> getDiceRolls(List<Int256> seedValues, int numRolls){
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
