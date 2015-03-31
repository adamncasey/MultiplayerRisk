package networking.message.payload;

import networking.parser.Parser;
import networking.parser.ParserException;
import networking.parser.PayloadParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.HashMap;


public class PlayCardsPayload extends Payload {
    public final int armiesExpected;
    public final int[][] cardSetsPlayed;

    public PlayCardsPayload(int[][] cardSetsPlayed) {
        this.cardSetsPlayed = cardSetsPlayed;
        // TODO Get this in NetworkPlayer
        this.armiesExpected = -1;
    }

    public PlayCardsPayload(JSONObject payloadObj) throws ParserException {
        // armies: integer
        Parser.validateType(payloadObj, "armies", Long.class);
        armiesExpected = ((Long)payloadObj.get("armies")).intValue();

        // cards: array of triples
        Parser.validateType(payloadObj, "cards", JSONArray.class);
        JSONArray cardSets = (JSONArray)payloadObj.get("cards");
        cardSetsPlayed = new int[cardSets.size()][3];

        for(int i=0; i<cardSets.size(); i++) {
            Object obj = cardSets.get(i);
            Parser.validateType(obj, JSONArray.class);

            JSONArray cardSet = (JSONArray)obj;
            int[] cards = PayloadParser.parseIntArray(cardSet, 3);

            cardSetsPlayed[i] = cards;
        }
    }

    @Override
    public Object getJSONValue() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("armies", armiesExpected);
        map.put("cards", PayloadParser.convert2DArrayTo2DList(cardSetsPlayed));

        return map;
    }
}
