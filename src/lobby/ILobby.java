package lobby;

import player.IPlayer;

/**
 * ILobby: Interface representing a game lobby.
 * @author James
 *
 */
public interface ILobby {

	/**
	 * Gets the IP address of the lobby.
	 * @return IP Address of the lobby host.
	 */
	public String getHostIPAddress();
	
	/**
	 * Gets information about the players in the lobby.
	 * @return Players in the lobby.
	 */
	public IPlayer[] getPlayers();
	
	/**
	 * Gets the friendly name of the lobby.
	 * @return Friendly name of the lobby.
	 */
	public String getName();
	
	
}
