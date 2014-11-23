package networking;

import networking.parser.Parser;
import networking.parser.ParserException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import player.IPlayer;

import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

// Interface used by GameManager / NetworkPlayer?
public class Network {
	// Create Lobby
	// Join Lobby
	
	// ...

	/**
	 * Gets a risk player connection from the socket
	 * @param socket
	 * @return A LobbyClient object, which may be used to accept or reject the join request.
	 * @return null if an error occurs during the initial connection
	 */
	public static LobbyClient getLobbyClient(IConnection socket) {

		Message message = Network.readMessage(socket);

		if(message == null || message.command != Command.JOIN) {
			return null;
		}

		float[] supportedVersions;
		String[] supportedFeatures;

		if(!(message.payload instanceof JSONObject)) {
			return null;
		}

		JSONObject payload = (JSONObject) message.payload;

		try {
			Parser.validateType(payload, "supported_versions", JSONArray.class);
			List payload_list = (List)payload.get("supported_versions");

			supportedVersions = ArrayUtils.toPrimitive(Arrays.asList(payload_list).toArray(new Float[payload_list.size()]));

			Parser.validateType(payload, "supported_features", JSONArray.class);
			payload_list = (List)payload.get("supported_features");
			supportedFeatures = Arrays.asList(payload_list).toArray(new String[payload_list.size()]);
		}
		catch(ParserException e) {
			return null;
		}

		return new LobbyClient(supportedVersions, supportedFeatures);
	}

	/**
	 * 
	 * @param
	 * @return
	 */
	public static LobbyServer getLobbyServer(String ipAddress, int port) {
		return null;
	}

	/**
	 * Reads a line from conn, and parses it as a JSON Object in the protocol format.
	 * @param conn - Connection to read from
	 * @return Message on success
	 * @return null on failure.
	 * TODO: Decide if this is enough detail. Could be useful to provide different exceptions for IOError vs Bad Packet
	 */
	static Message readMessage(IConnection conn) {
		// Assumes newline is equivalent to JSON Object boundary. Waiting on representatives to formally agree on this
		Message message;
		try {
			String msgString = conn.receiveLine();

			message = Parser.parseMessage(msgString);
		} catch (ConnectionLostException e) {
			return null;
		} catch (TimeoutException e) {
			return null;
		} catch (ParserException e) {
			return null;
		}

		return message;
	}
}
