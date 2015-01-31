package networking.message;

import networking.parser.Parser;
import networking.parser.ParserException;
import org.apache.commons.lang3.ArrayUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JoinGamePayload extends Payload {
    public final double[] supported_versions;
    public final String[] supported_features;

    public JoinGamePayload(double[] versions, String[] features) {
        this.supported_features = features;
        this.supported_versions = versions;
    }

    public JoinGamePayload(JSONObject payload) throws ParserException {

        Parser.validateType(payload, "supported_versions", JSONArray.class);

        List<Double> versions_list = (List)payload.get("supported_versions");
        Double[] middle = versions_list.toArray(new Double[versions_list.size()]);
        supported_versions = ArrayUtils.toPrimitive(middle);


        Parser.validateType(payload, "supported_features", JSONArray.class);

        List<String> features_list = (List)payload.get("supported_features");
        supported_features = features_list.toArray(new String[features_list.size()]);
    }

    @Override
    public Map getJSONValue() {
        // JSONObject: "supported_versions": [float, float]
        //             "supported_features": [string, string]

        Map<String, Object> map = new HashMap<>();

        // Needs to be reformatted for JSON simple library
        List<Double> versions = Arrays.asList(ArrayUtils.toObject(supported_versions));
        map.put("supported_versions", versions);
        map.put("supported_features", Arrays.asList(supported_features));

        return map;
    }
}
