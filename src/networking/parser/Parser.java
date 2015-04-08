package networking.parser;

import networking.message.Message;
import networking.message.payload.*;
import org.json.simple.JSONArray;
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
        // TODO: Non-playable host was added ~12th Feb 2015 which breaks this assumption
        // TODO player_id is not sent on initialise_game, players_joined, accept_join_game, reject_join_game, join_game
        // TODO player_id can be null for ping, ready.
        if(command != Command.JOIN_GAME) {
            validateType(message, "player_id", Number.class);
            playerid = ((Long) message.get("player_id")).intValue();
        }

        Long ackId = null;

        if(message.get("ack_id") != null) {
            validateType(message, "ack_id", Long.class);

            ackId = (Long)message.get("ack_id");
        }

		return new Message(command, playerid, payload, ackId);
	}
	
	/**
	 * Checks the JSONObject conforms at a basic level to "2. Json communication structure"
	 * Ensures existence of fields and correct type of fields
	 * 
	 * @param object
	 * @throws ParserException
	 */
	private static void validateObjectField(JSONObject object) throws ParserException {
		validateType(object, "command", String.class);

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

    public static boolean validateType(Object obj, Class<?> class1) {
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
                validatePayloadType(payloadObj, String.class);
                return new RejectJoinGamePayload((String)payloadObj);

            case ACKNOWLEDGEMENT:
                validatePayloadType(payloadObj, JSONObject.class);
                return new AcknowledgementPayload((JSONObject)payloadObj);

            case READY:
                return null;
            case PING: // Null or num players
                if(payloadObj == null) {
                    return null;
                }
            case SETUP: //territory ID
            case DRAW_CARD: // card ID being drawn
            case DEFEND: // Num Armies to defend 1/2
            case TIMEOUT:
                // TODO Single int ATTACK_CAPTURE command is not in spec. Need to conform or get protocol changed.
                return singleIntegerPayload(payloadObj);

            case FORTIFY: // Null or ArmyMovement
            case ATTACK: // ArmyMovement Payload or null
                if(payloadObj == null) {
                    return null;
                }
            case ATTACK_CAPTURE:
                validatePayloadType(payloadObj, JSONArray.class);
                return new ArmyMovementPayload((JSONArray) payloadObj);

            case PLAY_CARDS: // Null or Array of Integer Triple
                if(payloadObj == null) {
                    return null;
                }
                validatePayloadType(payloadObj, JSONObject.class);
                return new PlayCardsPayload((JSONObject)payloadObj);

            case DEPLOY: // Array of integer pair (Territory ID, Num Armies)
                validatePayloadType(payloadObj, JSONArray.class);
                return new DeployPayload((JSONArray)payloadObj);

            case DICE_HASH:
            case DICE_ROLL_NUM:
                validatePayloadType(payloadObj, String.class);
                return new StringPayload((String)payloadObj);

            case INITIALISE_GAME:
                validatePayloadType(payloadObj, JSONObject.class);
                return new InitialiseGamePayload((JSONObject)payloadObj);

            case LEAVE_GAME:
            case PLAYERS_JOINED:
            default:
                throw new ParserException("Unsupported Message type. " + command);
        }
    }

    private static Payload singleIntegerPayload(Object payloadObj) throws ParserException {

        validatePayloadType(payloadObj, Long.class);
        int value = ((Long)payloadObj).intValue();

        return new IntegerPayload(value);
    }
}
