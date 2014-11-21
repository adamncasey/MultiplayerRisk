package networking.parser;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import networking.Command;
import networking.Message;

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
		
		validateMessageFields(message);
		
		Command command = Command.parse((String) message.get("command"));
		
		// TODO: Perhaps this switch statement could process payload, and be in function rather than here.
		switch(command) {
		
		case ACKNOWLEDGEMENT:
		case JOIN:
			// payload = Object.
				// supported_versions
				// supported_features
		case JOIN_ACCEPT:
			// payload = Object
				// player_id
				// acknowledgement_timeout
				// move_timeout
		case DEPLOY:
		case ATTACK:
		case CAPTURE:
		case FORTIFY:
			// payload can be null.
			
		case TRADE_IN_CARDS:
			// payload can be null.
			break;
			
		default:
			throw new ParserException("Unsupported Message type. " + command);
		}
		
		// TODO Signature check.
		
		return new Message(command, false, (Long) message.get("player_id"), message.get("payload"));
	}
	
	/**
	 * Checks the JSONObject conforms at a basic level to 2. Json communication structure
	 * Ensures existance of fields and correct type of fields
	 * 
	 * TODO: Here might be a good time to verify signatures
	 * 
	 * @param message
	 * @throws ParserException
	 */
	private static void validateMessageFields(JSONObject message) throws ParserException {
		validateType(message, "command", String.class);
		validateType(message, "signature", String.class);
		validateType(message, "player_id", Number.class);
		
		if(message.get("ack_id") != null) {
			validateType(message, "ack_id", Number.class);
		}
	}

	private static void validateType(JSONObject object, String key, Class<?> class1) throws ParserException {
		if(object.get(key) != null && class1.isAssignableFrom(object.get(key).getClass())) {
			return;
		}
		
		throw new ParserException("Cannot parse message. " + key + " must be present and of correct type (" + class1.toString() + ").");
	}
}
