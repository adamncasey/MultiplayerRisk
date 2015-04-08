package networking.message.payload;

import networking.parser.Parser;
import networking.parser.ParserException;
import org.json.simple.JSONArray;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by Adam on 08/04/2015.
 */
public class PlayersJoinedPayload extends Payload {
    public final PlayerInfo[] info;

    public PlayersJoinedPayload(PlayerInfo[] info) {
        this.info = info;
    }

    public PlayersJoinedPayload(JSONArray array) throws ParserException {
        PlayerInfo[] list = new PlayerInfo[array.size()];
        int index = 0;

        for(Object obj : array) {
            if(!Parser.validateType(obj, JSONArray.class)) {
                throw new ParserException("Invalid message format. Players Joined array must contain a list of arrays");
            }
            JSONArray inner = (JSONArray)obj;

            if(inner.size() < 2) {
                throw new ParserException("Each entry in players_joined array must have at least 2 elements.");
            }

            Object id = inner.get(0);
            Object name = inner.get(1);

            if(!Parser.validateType(id, Long.class)) {
                throw new ParserException("First element of inner players joined array must be playerid integer");
            }
            if(!Parser.validateType(name, String.class)) {
                throw new ParserException("First element of inner players joined array must be name string");
            }

            list[index] = new PlayerInfo(((Long)id).intValue(), (String)name);

            index++;
        }

        info = list;
    }

    @Override
    public Object getJSONValue() {
        LinkedList<Object> list = new LinkedList<>();

        for(PlayerInfo player : info) {
            list.add(player.getJSONValue());
        }

        return list;
    }

    public class PlayerInfo {
        public final int playerid;
        public final String name;

        public PlayerInfo(int playerid, String name) {
            this.playerid = playerid;
            this.name = name;
        }

        public Object getJSONValue() {
            return Arrays.asList(playerid, name);
        }
    }
}
