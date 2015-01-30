package lobby;

import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import lobby.handler.HostLobbyEventHandler;
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

    private HostLobbyEventHandler handler;
	
	
	public LocalGameLobby(String friendlyName, HostLobbyEventHandler handler) {
        this.handler = handler;
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
			throw new RuntimeException("Unexpected Error: UnknownHostException thrown.");
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
		LobbyMulticastThread multicastThread = new LobbyMulticastThread(friendlyName);
        multicastThread.start();

		// Listen for new clients.
		try {
			server = new ServerSocket(DEFAULT_GAME_PORT);

			while (lobbyOpen) {
				newClient = server.accept();
				lobbyClient = Network.getLobbyClient(new Connection(newClient));

                String result = handler.onPlayerJoinRequest(lobbyClient);

                if(result == null) {
                    // accept client
                    int playerid = players.size() + 1;
                    if(lobbyClient.accept(playerid)) {
                        players.add(lobbyClient);

                        handler.onPlayerJoin(playerid);
                    }
                }
                else {
                    lobbyClient.reject(result);
                }
			}
			
			server.close();

            multicastThread.setLobbyOpen(false);
			
		} catch (Exception e) {
			// TODO: Log exception.
            throw new RuntimeException("Exception occured in Host Lobby loop.");
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
