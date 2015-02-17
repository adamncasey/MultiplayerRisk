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
        System.out.println("parseMessage '" + jsonMessage +"'");
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

        Long ackId = null;

        if(message.get("ack_id") != null) {
            validateType(message, "ack_id", Long.class);

            ackId = (Long)message.get("ack_id");
        }

        // TODO Signature check
        boolean signed = message.get("signature") != null;
        // Forwarding messages will require storing signature in Message object.

		return new Message(command, signed, playerid, payload, ackId);
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
		if(object.get(key) != null && validateType(object.get(key), class1)) {
			return;
		}
		
		throw new ParserException("Cannot parse message. " + key + " must be present and of correct type (" + class1.toString() + ").");
	}

    private static void validatePayloadType(Object obj, Class<?> class1) throws ParserException {
        if(validateType(obj, class1)) {
            return;
        }

        throw new ParserException("Invalid packet format. Expected 'payload' to be " + class1.toString() + ". Is actually" + obj.getClass().toString());
    }

    private static boolean validateType(Object obj, Class<?> class1) {
        if(class1.isAssignableFrom(obj.getClass())) {
            return true;
        }

        return false;
    }

    private static Payload parsePayload(Command command, Object payloadObj) throws ParserException {
        switch(command) {

            case JOIN_GAME:
                validatePayloadType(payloadObj, JSONObject.class);
                return new JoinGamePayload((JSONObject)payloadObj);

            case JOIN_ACCEPT:
                validatePayloadType(payloadObj, JSONObject.class);
                return new AcceptJoinGamePayload((JSONObject)payloadObj);

            case JOIN_REJECT:
                validatePayloadType(payloadObj, JSONObject.class);
                return new RejectJoinGamePayload((JSONObject)payloadObj);

            case PING:
                if(payloadObj != null) {
                    validatePayloadType(payloadObj, Long.class);
                    return new PingPayload(((Long) payloadObj).intValue());
                }
                // Payload can be null
                return null;

            case ACKNOWLEDGEMENT:
                validatePayloadType(payloadObj, JSONObject.class);
                return new AcknowledgementPayload((JSONObject)payloadObj);

            case READY:
                return null;

            case DEPLOY:
            case ATTACK:
            case ATTACK_CAPTURE:
            case FORTIFY:
                // payload can be null.

            case TRADE_IN_CARDS:
                // payload can be null.

            default:
                throw new ParserException("Unsupported Message type. " + command);
        }
    }
}
