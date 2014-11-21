package test.lobby;

import lobby.LobbyMulticastThread;
import lobby.LobbyFinder;

public class LobbyDevMain {

	public static void main(String[] args) {
        
		System.setProperty("java.net.preferIPv4Stack" , "true");
		
		
		
		LobbyMulticastThread t = new LobbyMulticastThread();
		t.start();
		
		LobbyFinder f = new LobbyFinder();
		f.findLocalLobbies();
		
	}
}
