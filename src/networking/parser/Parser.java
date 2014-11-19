package networking.parser;

import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

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
			throw new ParserException("Unable to parse packet. Error: " + e.getMessage() + ". Message:'" + jsonMessage + "'");
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
		throw new ParserException("Unsupported Message type");
		
		//return null;
	}
}
