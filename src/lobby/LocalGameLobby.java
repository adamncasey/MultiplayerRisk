package lobby;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import networking.LobbyClient;
import networking.Network;

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
	private int maxPlayers = 1;

	ArrayList<LobbyClient> players = new ArrayList<LobbyClient>();
	
	boolean lobbyOpen = true;

	// ================================================================================
	// Override
	// ================================================================================

	@Override
	public String getHostIPAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<LobbyClient> getPlayers() {
		// TODO Auto-generated method stub
		return players;
	}

	@Override
	public String getFriendlyName() {
		return this.friendlyName;
	}

	// ================================================================================
	// Functions
	// ================================================================================

	/**
	 * Open the lobby to network players.
	 */
	public void run() {
		ServerSocket server;
		Socket newClient;
		LobbyClient lobbyClient;

		// Start broadcasting the lobby.
		LobbyMulticastThread t = new LobbyMulticastThread(friendlyName);
		t.start();

		// Listen for new clients.
		try {
			server = new ServerSocket(DEFAULT_GAME_PORT);

			while (lobbyOpen && players.size() < maxPlayers) {
				newClient = server.accept();
				lobbyClient = Network.getLobbyClient(newClient);
				
				if(lobbyClient.accept()) {
					players.add(lobbyClient);
				}
			}
			
			server.close();
			
			t.setLobbyOpen(false);
			
		} catch (Exception e) {
			// TODO: Log exception.
			e.printStackTrace();
		}
	}
	
	/**
	 * Close the lobby.
	 */
	public void close() {
		this.lobbyOpen = false;
	}

	
	// ================================================================================
	// Accessors
	// ================================================================================

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}
}
