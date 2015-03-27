package networking.parser;


import org.apache.commons.lang3.ArrayUtils;
import org.json.simple.JSONArray;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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

    public static List<List<Integer>> convert2DArrayTo2DList(int[][] array) {
        // convert to List<List<Integer>>
        List<List<Integer>> values = new LinkedList<>();
        for(int[] deployment : array) {
            values.add(Arrays.asList(ArrayUtils.toObject(deployment)));
        }

        return values;
    }
}