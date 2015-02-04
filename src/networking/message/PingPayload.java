package networking.message;

import org.json.simple.JSONValue;

/**
 * Created by Adam on 04/02/2015.
 */
public class PingPayload extends Payload {
    private int numPlayers;

    public PingPayload(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public PingPayload(JSONValue value) {
        throw new UnsupportedOperationException("Not implemented");
    }


    @Override
    public Object getJSONValue() {
        return numPlayers;
    }
}
