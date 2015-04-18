package networking.message.payload;

import networking.parser.Parser;
import networking.parser.ParserException;

import org.apache.commons.lang3.ArrayUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JoinGamePayload extends Payload {
    public final double[] supportedVersions;
    public final String[] supportedFeatures;
    public final String playerName;

    public JoinGamePayload(double[] versions, String[] features, String name) {
        this.supportedFeatures = features;
        this.supportedVersions = versions;
        this.playerName = name;
    }

    public JoinGamePayload(JSONObject payload) throws ParserException {

        Parser.validateType(payload, "supported_versions", JSONArray.class);
        JSONArray versionsArray = (JSONArray)payload.get("supported_versions");
        supportedVersions = convertJSONArrayToDoubleArray(versionsArray);

        Parser.validateType(payload, "supported_features", JSONArray.class);
        JSONArray featuresArray = (JSONArray)payload.get("supported_features");

        supportedFeatures = convertJSONArrayToStringArray(featuresArray);

        Parser.validateType(payload, "name", String.class);
        playerName = (String)payload.get("name");
    }

    @SuppressWarnings("rawtypes")
	@Override
    public Map getJSONValue() {
        // JSONObject: "supported_versions": [float, float]
        //             "supported_features": [string, string]
        //             "name": string

        Map<String, Object> map = new HashMap<>();

        // Needs to be reformatted for JSON simple library
        List<Double> versions = Arrays.asList(ArrayUtils.toObject(supportedVersions));
        map.put("supported_versions", versions);
        map.put("supported_features", Arrays.asList(supportedFeatures));
        map.put("name", playerName);

        return map;
    }
    
    private double[] convertJSONArrayToDoubleArray(JSONArray array) {
    	double[] result = new double[array.size()];
    	
    	int i=0;
    	for(Object obj : array) {
    		if(obj != null) {
    			Parser.validateType(obj, Number.class);
    			
    			Number value = (Number)obj;
    			
    			result[i++] = value.doubleValue();
    		}
    	}
    	
    	return result;
    }
    
    private String[] convertJSONArrayToStringArray(JSONArray array) {
    	String[] result = new String[array.size()];
    	
    	int i=0;
    	for(Object obj : array) {
    		if(obj != null) {
    			Parser.validateType(obj, String.class);
    			
    			result[i++] = (String)obj;
    		}
    	}
    	
    	return result;
    }
}
