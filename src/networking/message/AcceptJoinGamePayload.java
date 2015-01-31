package networking.message;

import networking.parser.Parser;
import networking.parser.ParserException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.HashMap;
import java.util.Map;

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

    public AcceptJoinGamePayload(JSONObject payload) throws ParserException {
        Parser.validateType(payload, "player_id", Long.class);
        Parser.validateType(payload, "acknowledgement_timeout", Long.class);
        Parser.validateType(payload, "move_timeout", Long.class);

        this.playerid = ((Long)payload.get("player_id")).intValue();
        this.acknowledgement_timeout = ((Long)payload.get("acknowledgement_timeout")).intValue();
        this.move_timeout = ((Long)payload.get("move_timeout")).intValue();
    }

    @Override
    public Map getJSONValue() {
        Map<String, Object> map = new HashMap<>();

        map.put("player_id", playerid);
        map.put("acknowledgement_timeout", acknowledgement_timeout);
        map.put("move_timeout", move_timeout);

        return map;
    }
}
