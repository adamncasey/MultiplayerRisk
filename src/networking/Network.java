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
		// new IConnection
		
		// readMessage

		// message.command == Command.JOIN

		// store information in new Lobby Client

		// return client
		return null;
	}

	/**
	 * 
	 * @param socket
	 * @return
	 */
	public static LobbyServer getLobbyServer(String ipAddress, int port) {
		return null;
		
	}
}
