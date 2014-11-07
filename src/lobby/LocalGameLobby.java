package lobby;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import player.IPlayer;

/**
 * ILocalGameLobby: A locally hosted game lobby to which network players can
 * join.
 * 
 * @author James
 *
 */
public class LocalGameLobby extends GameLobby {

	// ================================================================================
	// Properties
	// ================================================================================

	private String friendlyName;

	ArrayList<IPlayer> players = new ArrayList<IPlayer>();

	// ================================================================================
	// Constructors
	// ================================================================================

	public LocalGameLobby() {

	}

	// ================================================================================
	// Common
	// ================================================================================

	@Override
	public String getHostIPAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPlayer[] getPlayers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return this.friendlyName;
	}

	// ================================================================================
	// Functions
	// ================================================================================

	/**
	 * Open the lobby to network players.
	 * 
	 * @throws PortInUseException If the application port is already in use
	 */
	public void open() throws PortInUseException {
		ServerSocket server;
		Socket newClient;
		IConnection newConnection;

		// TODO: Start broadcasting the lobby.
		LobbyMulticastThread t = new LobbyMulticastThread();
		t.run();

		// Listen for new clients.
		try {
			server = new ServerSocket(DEFAULT_GAME_PORT);
		} catch (IOException e) {
			throw new PortInUseException();
		}

		while (true) {
			try {
				newClient = server.accept();
				newConnection = new Connection(newClient);

				// Create IPlayer from the new connection & add to IPlayer.
				// -> players.add(new NetworkPlayer(...));

			} catch (IOException e) {
				// TODO: Log exception ?
				e.printStackTrace();
			}
		}
	}
}
