package lobby;

import java.util.ArrayList;

import networking.LobbyClient;

/**
 * ILobby: Interface representing a game lobby.
 * @author James
 *
 */
public abstract class GameLobby extends Thread {

	/**
	 * Gets information about the players in the lobby.
	 * @return Players in the lobby.
	 */
	public abstract ArrayList<LobbyClient> getPlayers();
}
