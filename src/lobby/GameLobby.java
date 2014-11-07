package lobby;

import player.IPlayer;

/**
 * ILobby: Interface representing a game lobby.
 * @author James
 *
 */
public abstract class GameLobby {
	
	/**
	 * Default listening port.
	 */
	public static final int DEFAULT_GAME_PORT = 4444;
	
	// The max number of players in a lobby.
	public static final int MAX_LOBBY_SIZE = 10; 

	/**
	 * Gets the IP address of the lobby.
	 * @return IP Address of the lobby host.
	 */
	public abstract String getHostIPAddress();
	
	/**
	 * Gets information about the players in the lobby.
	 * @return Players in the lobby.
	 */
	public abstract IPlayer[] getPlayers();
	
	/**
	 * Gets the friendly name of the lobby.
	 * @return Friendly name of the lobby.
	 */
	public abstract String getName();
	
	
}