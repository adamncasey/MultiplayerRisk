package networking.message;

import networking.Command;
import networking.parser.Parser;
import networking.parser.ParserException;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adam on 08/02/2015.
 */
public class AcknowledgementPayload extends Payload {

    public final long ack_id;
    public final int response_code;
    public final Payload data;

    public AcknowledgementPayload(JSONObject object) throws ParserException {
        Parser.validateType(object, "ack_id", Number.class);
        Parser.validateType(object, "response", Number.class);

        this.ack_id = ((Number)object.get("ack_id")).longValue();
        this.response_code = ((Number)object.get("response")).intValue();

        // TODO Handle acknowledgement data when we need to (During attack turn?)
        this.data = null;
    }

    public AcknowledgementPayload(long ack_id, int response_code, Payload data) {
        this.ack_id = ack_id;
        this.response_code = response_code;
        this.data = data;
    }

    @Override
    public Object getJSONValue() {
        Map<String, Object> map = new HashMap<>();

        map.put("ack_id", ack_id);
        map.put("response", response_code);
        assert data == null; // TODO if data isn't null
        map.put("data", data);

        return map;
    }
}
