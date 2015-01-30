package networking;

import java.net.InetAddress;

public class LobbyServer {
	private InetAddress address;
	
	public LobbyServer(InetAddress address) {
		this.setAddress(address);
	}
	
	public boolean join() {
		// Create Connection
		return false;
	}

	public InetAddress getAddress() {
		return address;
	}

	public void setAddress(InetAddress address) {
		this.address = address;
	}
	
}
