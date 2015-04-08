package networking.message.payload;

import networking.parser.Parser;
import networking.parser.ParserException;
import networking.parser.PayloadParser;
import org.apache.commons.lang3.ArrayUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Adam on 08/04/2015.
 */
public class InitialiseGamePayload extends Payload {
    public final double version;
    public final String[] features;

    public InitialiseGamePayload(double version, String[] features) {
        this.version = version;
        this.features = features;
    }

    public InitialiseGamePayload(JSONObject payload) throws ParserException {
        this.version = validateVersionType(payload);
        Parser.validateType(payload, "supported_features", JSONArray.class);

        this.features = PayloadParser.parseStringArray((JSONArray)payload.get("supported_features"));
    }

    private double validateVersionType(JSONObject payload) throws ParserException {
        Object version = payload.get("version");

        if(version == null) {
            throw new ParserException("version field required in Initialise Game Payload.");
        }

        if(Parser.validateType(version, Long.class)) {
            return ((Long)version).doubleValue();
        }


        if(Parser.validateType(version, Double.class)) {

            return ((Double)version);
        }


        throw new ParserException("version field must be numerical. Actual type: " + version.getClass());
    }

    @Override
    public Object getJSONValue() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("version", version);
        map.put("supported_features", Arrays.asList(features));

        return map;
    }
}
