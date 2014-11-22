package lobby;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * LobbyMulticastThread: Broadcasts a lobby on the local network.
 * @author James
 *
 */
public class LobbyMulticastThread extends Thread {
	
	public static final int BROADCAST_PORT = 4446;
	public static final String BROADCAST_IP_ADDRESS = "230.0.0.1";
	
	private boolean lobbyOpen = true;
	
	DatagramSocket socket = null;
	DatagramPacket packet;
	InetAddress group;
	
	byte[] buf = new byte[256];
	
	String message = "THIS IS A LOBBY!";
	
	public LobbyMulticastThread(String lobbyName) {
		buf = message.getBytes();
	}
	
	
	public void run() {
		try {
			socket = new DatagramSocket();
            group = InetAddress.getByName(BROADCAST_IP_ADDRESS);
            packet = new DatagramPacket(buf, buf.length, group, BROADCAST_PORT);
            
            while(lobbyOpen) {
                socket.send(packet);
                System.out.println("Sent packet");
                
                try {
                    sleep(1000);
                } 
                catch (InterruptedException e) { }
            }
            

		} catch (Exception e) {
			// TODO: Log exception ?
			e.printStackTrace();
		}
	}

	// ================================================================================
	// Accessors
	// ================================================================================

	public boolean isLobbyOpen() {
		return lobbyOpen;
	}

	public void setLobbyOpen(boolean lobbyOpen) {
		this.lobbyOpen = lobbyOpen;
	}
}
