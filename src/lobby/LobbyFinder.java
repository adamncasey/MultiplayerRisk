package lobby;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Finds game lobbies on the local network.
 * 
 * @author James
 *
 */
public class LobbyFinder {
	public InetAddress[] findLocalLobbies() {

		try {
			
			MulticastSocket socket = new MulticastSocket(LobbyMulticastThread.BROADCAST_PORT);
			InetAddress address = InetAddress.getByName(LobbyMulticastThread.BROADCAST_IP_ADDRESS);
			socket.joinGroup(address);

			DatagramPacket packet;

			for (int i = 0; i < 5; i++) {

				byte[] buf = new byte[256];
				packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);

				String received = new String(packet.getData(), 0,
						packet.getLength());
				System.out.println("Recieved Packet!: " + received);
			}

			socket.leaveGroup(address);
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
