package lobby;

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
	public RemoteGameLobby[] searchForLobbies();
	
	/**
	 * Joins a game lobby.
	 * @param lobby The lobby to join.
	 * @return True if joined successfully.
	 */
	public boolean joinGameLobby(RemoteGameLobby lobby);
	
	/**
	 * Creates a game lobby.
	 * @return The game lobby created.
	 * @throws PortInUseException If the port is already in use.
	 */
	public LocalGameLobby createGameLobby(String friendlyName) throws PortInUseException;
}
