package networking.message;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Created by Adam on 31/01/2015.
 */
public class AcceptJoinGamePayload extends Payload {
    public final int playerid;
    public final int acknowledgement_timeout;
    public final int move_timeout;

    public AcceptJoinGamePayload(int playerid, int ack_timeout, int move_timeout) {
        this.playerid = playerid;
        this.acknowledgement_timeout = ack_timeout;
        this.move_timeout = move_timeout;
    }

    public AcceptJoinGamePayload(JSONObject object) {

        throw new UnsupportedOperationException ("Not implemented yet"); // TODO
    }

    @Override
    public JSONValue getJSONValue() {
        return null;
    }
}
