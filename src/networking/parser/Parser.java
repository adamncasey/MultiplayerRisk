package networking.parser;

import networking.message.Message;
import networking.message.payload.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import networking.Command;
import settings.Settings;
import java.util.*;

public class Parser {

    public static String stringifyMessage(Message message) {
        String innerMessage = message.toString();

        if(!Settings.SUPPORT_WRAPPER_MESSAGES) {
            return innerMessage;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("message", innerMessage);

        JSONObject obj = new JSONObject(map);

        return JSONValue.toJSONString(obj) + "\n";
    }

    /**
     * Parses a message starting from the outer wrapper format.
     * This wrapper format is described in the protocol in section 5.1.1
     * @param msgString
     * @return
     */
    public static Message parseOuterMesage(String msgString) throws ParserException {
        Object parsed;

        try {
            parsed = JSONValue.parseWithException(msgString);
        } catch(ParseException e) {
            throw new ParserException("Unable to parse outer packet. Error: " + e.toString() + ". Message:'" + msgString + "'");
        }

        return parseOuterJSONObjectToMessage(parsed);
    }

    private static Message parseOuterJSONObjectToMessage(Object parsed) throws ParserException {
        if(!(parsed instanceof JSONObject)) {
            throw new ParserException("Invalid message format. Expecting JSONObject");
        }

        JSONObject wrapper = (JSONObject)parsed;

        validateType(wrapper, "message", String.class);

        String innerMessage = (String)wrapper.get("message");

        return parseMessage(innerMessage);
    }

    /*public static String unescapeJson(String innerMessage) {

        innerMessage = innerMessage.replaceAll(Pattern.quote("\\\""), "\"");
        innerMessage = innerMessage.replaceAll(Pattern.quote("\\n"), "\n");

        innerMessage = innerMessage.replaceAll(Pattern.quote("\\\\"), "\\");

        return innerMessage;
    }

    public static String escapeJson(String innerMessage) {

        // find replace all back slash with \\
        innerMessage = innerMessage.replace("\\", "\\\\");

        // find replace all new lines with \n
        innerMessage = innerMessage.replace("\n", "\\n");

        // find replace all double quotes with \"
        innerMessage = innerMessage.replace("\"", "\\\"");

        return innerMessage;
    }*/

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

    public static final Collection<Command> NO_PLAYERID_COMMANDS = Collections.unmodifiableCollection(Arrays.asList(Command.INITIALISE_GAME, Command.PLAYERS_JOINED, Command.JOIN_ACCEPT, Command.JOIN_REJECT, Command.JOIN_GAME));
    public static final Collection<Command> NULL_PLAYERID_COMMANDS = Collections.unmodifiableCollection(Arrays.asList(Command.READY, Command.PING));

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

        Integer playerid = null;

        // player_id is needed on most messages, but not all.
        // player_id is null on NULL_PLAYERID_COMMANDS.
        // player_id is not specified at all on NO_PLAYERID_COMMANDS
        // player_id must be an integer >= 0
        if(NO_PLAYERID_COMMANDS.contains(command)) {
            // playerid shouldn't be sent in this message.
            if(message.containsKey("player_id")) {
                System.out.println("Message Warning: " + command.name() + " should not contain player_id field");
            }
            playerid = -1;
        } else {
            Object obj = message.get("player_id");

            if(obj == null) {
                playerid = null;
            }
            else {
                validateType(obj, Long.class);
                playerid = ((Long)obj).intValue();

                if(playerid < 0) {
                    throw new ParserException("Invalid playerid. Playerid must be not be negative");
                }
            }
        }

        Integer ackId = null;

        if(message.get("ack_id") != null) {
            validateType(message, "ack_id", Long.class);

            ackId = ((Long)message.get("ack_id")).intValue();
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

        if(!object.containsKey("payload")) {
            throw new ParserException("Message must contain payload field.");
        }

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


            case READY:
                return null;
            case PING: // Null or num players
                if(payloadObj == null) {
                    return null;
                }
            case SETUP: // Territory ID
            case DEFEND: // Num Armies to defend 1/2
            case TIMEOUT: // Player ID being booted
            case ACKNOWLEDGEMENT: // Ack_ID being acknowledged
                return singleIntegerPayload(payloadObj);

            case FORTIFY: // Null or ArmyMovement
                if(payloadObj == null) {
                    return null;
                }
            case ATTACK: // ArmyMovement Payload or null
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
                validatePayloadType(payloadObj, JSONObject.class);
                return new LeaveGamePayload((JSONObject)payloadObj);

            case PLAYERS_JOINED:
                validatePayloadType(payloadObj, JSONArray.class);
                return new PlayersJoinedPayload((JSONArray)payloadObj);
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
