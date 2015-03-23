package networking.message;

import networking.Command;
import org.json.simple.JSONValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * The format network messages are parsed into/serialised from.
 */
public class Message {

    public final Command command;
    public final Payload payload;
    public final int playerid;
    public final Long ackId;

	public Message(Command command, int playerid, Payload payload, Long ackId) {
		this.command = command;
		this.payload = payload;
		this.playerid = playerid;

		this.ackId = ackId;
	}

    public Message(Command command, int playerid, Payload payload, boolean ack) {
        this(command, playerid, payload, ack ? generateAcknowledgementID() : null);
    }

	public Message(Command command, int playerid, Payload payload) {
		this(command, playerid, payload, false);
	}

    /**
     * Constructor used for Join Game message
     * Due to no knowledge of playerid
     */
    public Message(Command command, Payload payload) {
        this(command, -1 /*unknown playerid*/, payload, null);
    }

    private static long generateAcknowledgementID() {
        return new Random().nextLong();
    }

	public String toString() {
		Map<String, Object> jsonObject = new HashMap<>();

		jsonObject.put("command", command.name);

        if(payload != null) {
            jsonObject.put("payload", payload.getJSONValue());
        }
        else {
            jsonObject.put("payload", null);
        }

        if(playerid != -1) {
            jsonObject.put("player_id", playerid);
        }

        if(ackId != null) {
            jsonObject.put("ack_id", ackId);
        }

		return JSONValue.toJSONString(jsonObject) + "\n";
	}
}
