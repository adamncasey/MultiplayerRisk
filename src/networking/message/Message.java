package networking.message;

import networking.Command;
import org.json.simple.JSONValue;

import java.util.HashMap;
import java.util.Map;

/**
 * The format network messages are parsed into/serialised from.
 */
public class Message {

    public final Command command;
    public final Payload payload;
    public final boolean signed; // Not filled in at the moment.
    public final int playerid;
    public final Long ackId;

	public Message(Command command, boolean signed, int playerid, Payload payload, Long ackId) {
		this.command = command;
		this.payload = payload;
		this.signed = signed;
		this.playerid = playerid;

		this.ackId = ackId;
	}
	public Message(Command command, int playerid, Payload payload) {
		this(command, true, playerid, payload, null);
	}

    /**
     * Constructor used for Join Game message
     * Due to no knowledge of playerid
     */
    public Message(Command command, Payload payload) {
        this(command, true, -1 /*unknown playerid*/, payload, null);
    }

	public Message(Command command, int playerid, Payload payload, long ackId) {
		this(command, true, playerid, payload, ackId);
	}

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
