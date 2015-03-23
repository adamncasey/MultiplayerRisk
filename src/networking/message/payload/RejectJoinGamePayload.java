package networking.message.payload;

import org.json.simple.JSONValue;

public class RejectJoinGamePayload extends Payload {
    public final String message;

    public RejectJoinGamePayload(String message) {
        this.message = message;
    }

    @Override
    public JSONValue getJSONValue() {
        return null;
    }
}
