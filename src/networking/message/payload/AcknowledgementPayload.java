package networking.message.payload;

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

    public AcknowledgementPayload(JSONObject object) throws ParserException {
        Parser.validateType(object, "ack_id", Number.class);

        this.ack_id = ((Number)object.get("ack_id")).longValue();
    }

    public AcknowledgementPayload(long ack_id) {
        this.ack_id = ack_id;
    }

    @Override
    public Object getJSONValue() {
        Map<String, Object> map = new HashMap<>();

        map.put("ack_id", ack_id);

        return map;
    }
}
