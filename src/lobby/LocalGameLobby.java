package lobby;

import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import networking.Connection;
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
	
	
	public LocalGameLobby(String friendlyName) {
		this.friendlyName = friendlyName;
	}

	// ================================================================================
	// Override
	// ================================================================================

	@Override
	public String getHostIPAddress() {
		String address = "Unknown";
		
		try {
			address = Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return address;
	}

	@Override
	public ArrayList<LobbyClient> getPlayers() {
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
				lobbyClient = Network.getLobbyClient(new Connection(newClient));
				
				if(lobbyClient.accept(players.size() + 1)) {
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
