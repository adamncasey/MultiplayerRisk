package test.lobby;

import lobby.LobbyFinder;
import lobby.LobbyMulticastThread;

public class LobbyFinderMain {

	public static void main(String[] args) {
		System.setProperty("java.net.preferIPv4Stack" , "true");
		
//		LobbyMulticastThread t = new LobbyMulticastThread("James");
//		t.start();
		
		LobbyFinder f = new LobbyFinder();
		f.findLocalLobbies();

	}

}
