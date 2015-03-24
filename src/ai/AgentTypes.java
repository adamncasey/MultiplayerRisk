package ai;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AgentTypes {
    public enum Type {
        RANDOM, SIMPLE
    }

    private static final List<Type> VALUES = Collections.unmodifiableList(Arrays.asList(Type.values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static Type randomType(){
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
