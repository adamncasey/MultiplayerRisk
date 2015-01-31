package networking.parser;

import networking.message.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import networking.Command;

public class Parser {
	/**
	 * Parses an entire JSON object in the format described by Json communication structure
	 * @param jsonMessage
	 * @return Message - The data in jsonMessage parsed into our data structure.
	 * @throws ParserException - Thrown if there is an error parsing
	 */
	public static Message parseMessage(String jsonMessage) throws ParserException {

		Object parsed;

		try {
			parsed = JSONValue.parseWithException(jsonMessage);
		} catch (ParseException e) {
			throw new ParserException("Unable to parse packet. Error: " + e.toString() + ". Message:'" + jsonMessage + "'");
		}

		return parseJSONValueToMessage(parsed);
	}

	/**
	 * Parses the JSON structure into Message.
	 * @param  parsed
	 * @return Message object on success
	 * @throws ParserException The JSON input could not be parsed to a Message
	 */
	private static Message parseJSONValueToMessage(Object parsed) throws ParserException {
		if(!(parsed instanceof JSONObject)) {
			throw new ParserException("Invalid message format. Expecting JSONObject");
		}
		
		JSONObject message = (JSONObject)parsed;
		
		validateObjectField(message);
		
		Command command = Command.parse((String) message.get("command"));
		
        Payload payload = parsePayload(command, message.get("payload"));

        int playerid = -1;

        // playerid is required on all messages except join_game
        if(command != Command.JOIN_GAME) {
            validateType(message, "player_id", Number.class);
            playerid = ((Long) message.get("player_id")).intValue();
        }

        // TODO Signature check + ack ID

		return new Message(command, false, playerid, payload, null);
	}
	
	/**
	 * Checks the JSONObject conforms at a basic level to "2. Json communication structure"
	 * Ensures existance of fields and correct type of fields
	 * 
	 * TODO: Here might be a good time to verify signatures
	 * 
	 * @param object
	 * @throws ParserException
	 */
	private static void validateObjectField(JSONObject object) throws ParserException {
		validateType(object, "command", String.class);
		validateType(object, "signature", String.class);

		if(object.get("ack_id") != null) {
			validateType(object, "ack_id", Number.class);
		}
	}

	public static void validateType(JSONObject object, String key, Class<?> class1) throws ParserException {
		if(object.get(key) != null && class1.isAssignableFrom(object.get(key).getClass())) {
			return;
		}
		
		throw new ParserException("Cannot parse message. " + key + " must be present and of correct type (" + class1.toString() + ").");
	}

    private static Payload parsePayload(Command command, Object payloadObj) throws ParserException {
        switch(command) {

            case JOIN_GAME:
                if(!(payloadObj instanceof JSONObject)) {
                    throw new ParserException("Invalid packet format. Expected 'payload' to be JSON Object");
                }
                return new JoinGamePayload((JSONObject)payloadObj);

            case JOIN_ACCEPT:
                if(!(payloadObj instanceof JSONObject)) {
                    throw new ParserException("Invalid packet format. Expected 'payload' to be JSON Object");
                }
                return new AcceptJoinGamePayload((JSONObject)payloadObj);

            case JOIN_REJECT:
                if(!(payloadObj instanceof JSONObject)) {
                    throw new ParserException("Invalid packet format. Expected 'payload' to be JSON Object");
                }
                return new RejectJoinGamePayload((JSONObject)payloadObj);

            case ACKNOWLEDGEMENT:
            case DEPLOY:
            case ATTACK:
            case CAPTURE:
            case FORTIFY:
                // payload can be null.

            case TRADE_IN_CARDS:
                // payload can be null.

            default:
                throw new ParserException("Unsupported Message type. " + command);
        }
    }
}
