package logic.rng;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RNG {
    public final Int256 hash;
    public final Int256 number;

    public RNG() {
        this.number = Int256.fromRandom();
        this.hash = Int256.fromHash(number);
    }

    public static List<Integer> getDiceRolls(Collection<Int256> seedValues, int numRolls, int numFaces){

        // Feed into our RNG
        SeededGenerator generator = new SeededGenerator();

        for(Int256 bigInt : seedValues) {
            generator.addNumber(bigInt.toBytes());
        }
        generator.finalise();

        List<Integer> rolls = new ArrayList<>();
        for(int i = 0; i != numRolls; ++i){
            int roll = (int) ((generator.nextInt() % numFaces) + numFaces) % numFaces + 1;
            rolls.add(roll);
        }
        return rolls;
    }

    public static List<Integer> getDiceRolls(List<Int256> seedValues, int numRolls) {
        return getDiceRolls(seedValues, numRolls, 6);
    }
}
