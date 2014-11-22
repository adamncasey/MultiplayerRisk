package lobby;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;

import networking.LobbyServer;

/**
 * Finds game lobbies on the local network.
 * 
 * @author James
 *
 */
public class LobbyFinder extends Thread {
	boolean findLobbies = true;
	
	private ArrayList<LobbyServer> lobbies = new ArrayList<LobbyServer>();
	
	
	private boolean isNewLobby(LobbyServer newLobby) {
		boolean isNew = true;
		
		for(LobbyServer l : lobbies) {
			if(l.getAddress().equals(newLobby.getAddress())) {
				isNew = false;
				break;
			}
		}
		
		return isNew;
	}
	
	public ArrayList<LobbyServer> getLobbies() {
		return lobbies;
	}
	
	public void run() {
		MulticastSocket socket;
		InetAddress address;
		DatagramPacket packet;
		LobbyServer server;
		
		try {
			socket = new MulticastSocket(LobbyMulticastThread.BROADCAST_PORT);
			address = InetAddress.getByName(LobbyMulticastThread.BROADCAST_IP_ADDRESS);
			socket.joinGroup(address);
			
			while(findLobbies) {
				byte[] buf = new byte[256];
				packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);
				
				server = new LobbyServer(packet.getAddress());
				if(isNewLobby(server)) {
					lobbies.add(server);
					System.out.println("Discovered new lobby: " + new String(packet.getData(), 0,
							packet.getLength()) + ")");
				}

				String received = new String(packet.getData(), 0,
						packet.getLength());
				System.out.println("Recieved Packet!: " + received);
			}

			// Clean up
			socket.leaveGroup(address);
			socket.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Ends the search for lobbies (ends this thread).
	 */
	public void close() {
		findLobbies = false;
	}
}
