package networking.parser;

import networking.message.payload.Payload;
import org.apache.commons.lang3.ArrayUtils;
import org.json.simple.JSONArray;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class DeployPayload extends Payload {
    public final int[][] deployments;

    public DeployPayload(JSONArray array) throws ParserException {
        deployments = new int[array.size()][2];

        for(int i=0; i<array.size(); i++) {
            Object obj = array.get(i);
            Parser.validateType(obj, JSONArray.class);

            JSONArray deployment = (JSONArray)obj;
            deployments[i] = PayloadParser.parseIntArray(deployment, 2);
        }
    }

    @Override
    public Object getJSONValue() {

        return PayloadParser.convert2DArrayTo2DList(deployments);
    }
}
