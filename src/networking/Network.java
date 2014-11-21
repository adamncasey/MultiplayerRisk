package networking;

import player.IPlayer;

import java.net.Socket;

// Interface used by GameManager / NetworkPlayer?
public class Network {
	// Create Lobby
	// Join Lobby
	
	// ...


	/**
	 * Gets a risk player connection from the socket
	 * @param socket
	 * @return
	 */
	public static LobbyClient getLobbyClient(Socket socket) {
		// socket.accept

		// readMessage

		// message.command == Command.JOIN

		// store informaion in new Lobby Client

		// return client
		return null;
	}

	public static LobbyClient acceptLobbyClient(LobbyClient client) {
		// send message JOIN_ACCEPT (player_id, ack timeout, move timeout)

		//
		return null;
	}

	//public static void rejectLobbyClient(LobbyClient client, String rejectMessage);


}
