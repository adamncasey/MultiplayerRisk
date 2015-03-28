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
    public final Payload data;

    public AcknowledgementPayload(JSONObject object) throws ParserException {
        Parser.validateType(object, "ack_id", Number.class);

        this.ack_id = ((Number)object.get("ack_id")).longValue();

        this.data = null;
    }

    public AcknowledgementPayload(long ack_id, Payload data) {
        this.ack_id = ack_id;
        this.data = data;
    }

    @Override
    public Object getJSONValue() {
        Map<String, Object> map = new HashMap<>();

        map.put("ack_id", ack_id);
        assert data == null; // TODO if data isn't null
        map.put("data", data);

        return map;
    }
}
