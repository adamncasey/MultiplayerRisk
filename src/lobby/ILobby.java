package lobby;

import java.util.ArrayList;

import lobby.handler.HostLobbyEventHandler;
import networking.LobbyServer;
import networking.PortInUseException;

/**
 * Interface through which UI performs lobby related actions.
 * @author James
 * TODO: Is this interface necessary?
 */
public interface ILobby {

	/**
	 * Searches the network for game lobbies.
	 */
	void startSearchingForLobbies();
	
	/**
	 * Ends the lobby search.
	 */
	void stopSearchingForLobbies();
	
	/**
	 * Searches the network for game lobbies.
	 * @return Game lobbies found on the network.
	 */
	ArrayList<LobbyServer> findLobbies();
	
	/**
	 * Joins a game lobby.
	 * @param lobby The lobby to join.
	 * @return True if joined successfully.
	 */
	boolean joinGameLobby(LobbyServer lobby);
	
	/**
	 * Creates a game lobby.
	 * @return The game lobby created.
	 * @throws networking.PortInUseException If the port is already in use.
	 */
	LocalGameLobby createGameLobby(String friendlyName, int noOfPlayers, HostLobbyEventHandler handler) throws PortInUseException;
}
