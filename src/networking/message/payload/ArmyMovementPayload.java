package networking.message.payload;

import networking.parser.ParserException;
import networking.parser.PayloadParser;
import org.json.simple.JSONArray;

import java.util.Arrays;


public class ArmyMovementPayload extends Payload {

    public final int sourceTerritory;
    public final int destinationTerritory;
    public final int numArmies;

    public ArmyMovementPayload(int sourceTerritory, int destinationTerritory, int numArmies) {
        this.sourceTerritory = sourceTerritory;
        this.destinationTerritory = destinationTerritory;
        this.numArmies = numArmies;
    }

    public ArmyMovementPayload(JSONArray array) throws ParserException {

        int[] values = PayloadParser.parseIntArray(array, 3);

        sourceTerritory = values[0];
        destinationTerritory = values[1];
        numArmies = values[2];
    }

    @Override
    public Object getJSONValue() {
        return Arrays.asList(sourceTerritory, destinationTerritory, numArmies);
    }
}
