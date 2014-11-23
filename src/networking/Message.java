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
		this.playerId = playerId;

		this.ackId = ackId;
	}
	public Message(Command command, long playerId, Object payload) {
		this(command, true, playerId, payload, null);
	}
	public Message(Command command, long playerId, Object payload, long ackId) {
		this(command, true, playerId, payload, ackId);
	}

	public final Command command;
	
	public final Object payload;
	
	public final boolean signed; // Not filled in at the moment.
	
	public final long playerId;

	public final Long ackId;

	public String toString() {
		Map<String, Object> jsonObject = new HashMap<>();

		jsonObject.put("command", command.name);
		jsonObject.put("payload", payload);
		if(signed) {
			//TODO Signing
			jsonObject.put("signature", "TBA");
		}
		jsonObject.put("player_id", playerId);

		return JSONValue.toJSONString(jsonObject);
	}
}
