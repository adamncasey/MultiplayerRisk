package lobby;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import networking.Connection;
import networking.IConnection;
import networking.PortInUseException;
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
	 * @throws networking.PortInUseException
	 *             If the application port is already in use
	 */
	public void open() throws PortInUseException {
		ServerSocket server;
		Socket newClient;
		IConnection newConnection;

		// TODO: Start broadcasting the lobby.
		LobbyMulticastThread t = new LobbyMulticastThread();
		t.start();

		// Listen for new clients.
		try {
			server = new ServerSocket(DEFAULT_GAME_PORT);

			while (true) {
				newClient = server.accept();
				newConnection = new Connection(newClient);

				// Create IPlayer from the new connection & add to IPlayer.
				// -> players.add(new NetworkPlayer(...));
			}
		} catch (Exception e) {
			throw new PortInUseException();
		}
	}
}
