package networking.message;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class RejectJoinGamePayload extends Payload {
    public final String message;

    public RejectJoinGamePayload(String message) {
        this.message = message;
    }

    public RejectJoinGamePayload(JSONObject payload) {

        throw new UnsupportedOperationException ("Not implemented yet"); // TODO
    }

    @Override
    public JSONValue getJSONValue() {
        return null;
    }
}
