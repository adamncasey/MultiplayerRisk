package networking.parser;


import org.json.simple.JSONArray;

public class PayloadParser {
    public static int[] parseIntArray(JSONArray array, int requiredLength) throws ParserException {
        if(array.size() != requiredLength) {
            throw new ParserException("Army Movement Payload must be an array of length " +
                    requiredLength + ". Actual Length: " + array.size());
        }

        int[] values = new int[requiredLength];

        for(int i=0; i<requiredLength; i++) {
            Object value = array.get(i);

            Parser.validateType(value, Long.class);
            values[i] = ((Long)value).intValue();
        }

        return values;
    }
}
