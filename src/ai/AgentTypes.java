package ai;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AgentTypes {
    public enum Type {
        ANGRY, GREEDY, CONTINENTAL, FURIOUS, FOCUSED
    }
// RANDOM, PASSIVE

    private static final List<Type> values = Collections.unmodifiableList(Arrays.asList(Type.values()));
    private static final int size = values.size();
    private static final Random random = new Random();

    public static Type randomType(){
        return values.get(random.nextInt(size));
    }
}
