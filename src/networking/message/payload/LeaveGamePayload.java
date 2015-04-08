package networking.message.payload;

import networking.parser.Parser;
import networking.parser.ParserException;
import org.json.simple.JSONObject;

import java.util.HashMap;

/**
 * Created by Adam on 08/04/2015.
 */
public class LeaveGamePayload extends Payload {
    public final int responseCode;
    public final String message;
    public final boolean receiveUpdates;

    public LeaveGamePayload(int code, String msg, boolean continueUpdates) {
        responseCode = code;
        message = msg;
        receiveUpdates = continueUpdates;
    }

    public LeaveGamePayload(JSONObject obj) throws ParserException {
        Parser.validateType(obj, "response", Long.class);
        Parser.validateType(obj, "message", String.class);
        Parser.validateType(obj, "receive_updates", Boolean.class);

        responseCode = ((Long)obj.get("response")).intValue();
        message = (String)obj.get("message");
        receiveUpdates = (Boolean)obj.get("receive_updates");
    }

    @Override
    public Object getJSONValue() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("response", responseCode);
        map.put("message", message);
        map.put("receive_updates", receiveUpdates);

        return map;
    }
}
