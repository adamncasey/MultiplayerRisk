package networking;

/**
 * Stores Client state through the Lobby process
 */
public class LobbyClient {
    /**
     * States:  Connected
     *          Accepted (Rejected clients are not tracked after disconnection)
     *          Waiting for PING
     *          Received PING
     *
     */
	
	public boolean accept() {
		// send message JOIN_ACCEPT (player_id, ack timeout, move timeout)
		return false;
	}
	
	public void reject() {
		
	}
	
	public void sendPing() {
		
	}
	
	public void receivePing() {
		
	}
}
