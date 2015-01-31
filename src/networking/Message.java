package networking;

import org.json.simple.JSONValue;

import java.util.HashMap;
import java.util.Map;

/**
 * The format network messages are parsed into/serialised from.
 */
public class Message {
	public Message(Command command, boolean signed, long playerId, Object payload, Long ackId) {
		this.command = command;
		this.payload = payload;
		this.signed = signed;
		this.playerid = playerId;

		this.ackId = ackId;
	}
	public Message(Command command, long playerId, Object payload) {
		this(command, true, playerId, payload, null);
	}

    /**
     * Join game message, before we know our playerid
     */
    public Message(Command command, Object payload) {
        this(command, true, -1, payload, null);
    }

	public Message(Command command, long playerId, Object payload, long ackId) {
		this(command, true, playerId, payload, ackId);
	}

	public final Command command;
	
	public final Object payload;
	
	public final boolean signed; // Not filled in at the moment.
	
	public final long playerid;

	public final Long ackId;

	public String toString() {
		Map<String, Object> jsonObject = new HashMap<>();

		jsonObject.put("command", command.name);
		jsonObject.put("payload", payload);
		if(signed) {
			//TODO Signing
			jsonObject.put("signature", "TBA");
		}
        if(playerid == -1) {
            jsonObject.put("player_id", playerid);
        }

		return JSONValue.toJSONString(jsonObject);
	}
}
