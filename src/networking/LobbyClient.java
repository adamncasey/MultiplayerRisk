package networking;

import networking.message.AcceptJoinGamePayload;
import networking.message.Message;
import networking.message.RejectJoinGamePayload;

import java.util.HashMap;
import java.util.Map;

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

	// TODO A better way of handling this value.
	private static final int OUR_PLAYER_ID = 0;

	protected LobbyClient(IConnection conn, double[] supportedVersions, String[] supportedFeatures) {
        this.conn = conn;
        this.supportedVersions = supportedVersions;
		this.supportedFeatures = supportedFeatures;
	}

	public final double[] supportedVersions;
	public final String[] supportedFeatures;
	
	public boolean accept(int playerid) {
		// send message JOIN_ACCEPT (player_id, ack timeout, move timeout)

        AcceptJoinGamePayload payload = new AcceptJoinGamePayload(playerid, conn.getTimeout(), conn.getTimeout());

		Message msg = new Message(Command.JOIN_ACCEPT, OUR_PLAYER_ID, payload);
		try {
			conn.send(msg.toString());
		}
		catch(ConnectionLostException e) {
			return false;
		}

		return true;
	}
	
	public void reject(String rejectMessage) {
		// send message JOIN_REJECT (player_id, ack timeout, move timeout)
		Message msg = new Message(Command.JOIN_REJECT, OUR_PLAYER_ID, new RejectJoinGamePayload(rejectMessage));
		try {
			conn.send(msg.toString());
		}
		catch(ConnectionLostException e) {
			return;
		}

		conn.kill();
	}
	
	public void sendPing() {
		
	}
	
	public void receivePing() {
		
	}

	private IConnection conn;
}
