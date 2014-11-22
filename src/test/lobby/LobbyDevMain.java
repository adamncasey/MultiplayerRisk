package test.lobby;

import java.util.ArrayList;
import java.util.Arrays;

import networking.LobbyClient;
import networking.PortInUseException;
import lobby.GameLobby;
import lobby.ILobby;
import lobby.Lobby;
import lobby.LobbyMulticastThread;
import lobby.LobbyFinder;
import lobby.LocalGameLobby;

public class LobbyDevMain {

	public static void main(String[] args) {
        
		System.setProperty("java.net.preferIPv4Stack" , "true");
		
		ILobby lobby = new Lobby();
		LocalGameLobby localLobby;
		
		// Create local lobby.
		try {
			
			localLobby = lobby.createGameLobby("James' Lobby", 1);
			
			ArrayList<LobbyClient> players;
			while(true) {
                
                try {
                    Thread.sleep(3000);
                    players = localLobby.getPlayers();
                    System.out.println("Lobby players: " + players.size());
                    System.out.println(players.toString());
                } 
                catch (InterruptedException e) { }
            }
			
			
		} catch (PortInUseException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		
		
//		LobbyMulticastThread t = new LobbyMulticastThread();
//		t.start();
//		
//		LobbyFinder f = new LobbyFinder();
//		f.findLocalLobbies();
		
	}
}
