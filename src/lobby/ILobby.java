package lobby;

import networking.PortInUseException;

/**
 * Interface through which UI performs lobby related actions.
 * @author James
 *
 */
public interface ILobby {

	/**
	 * Searches the network for game lobbies.
	 * @return Game lobbies found on the network.
	 */
	RemoteGameLobby[] searchForLobbies();
	
	/**
	 * Joins a game lobby.
	 * @param lobby The lobby to join.
	 * @return True if joined successfully.
	 */
	boolean joinGameLobby(RemoteGameLobby lobby);
	
	/**
	 * Creates a game lobby.
	 * @return The game lobby created.
	 * @throws networking.PortInUseException If the port is already in use.
	 */
	LocalGameLobby createGameLobby(String friendlyName, int noOfPlayers) throws PortInUseException;
}
