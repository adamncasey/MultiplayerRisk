package test.lobby;

import java.util.ArrayList;

import networking.LobbyServer;
import lobby.ILobby;
import lobby.Lobby;

public class LobbyFinderMain {

	public static void main(String[] args) {
		System.setProperty("java.net.preferIPv4Stack", "true");

		ILobby lobby = new Lobby();
		ArrayList<LobbyServer> lobbies;

		// Create local lobby.
		lobby.startSearchingForLobbies();

		while (true) {

			try {
				Thread.sleep(3000);
				lobbies = lobby.findLobbies();
				System.out.print("Network lobby servers: " + lobbies.size());
				System.out.println(lobbies.toString());
			} catch (InterruptedException e) {
			}
		}
	}
}
