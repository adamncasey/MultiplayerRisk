package networking;

import networking.message.AcceptJoinGamePayload;
import networking.message.Message;
import networking.message.PingPayload;
import networking.message.RejectJoinGamePayload;

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
			conn.sendBlocking(msg.toString());
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
            conn.sendBlocking(msg.toString());
        } catch (ConnectionLostException e) {
            return;
        }

        conn.kill();
    }

	public void sendPing(int numPlayers) throws ConnectionLostException {
        Message msg = new Message(Command.PING, OUR_PLAYER_ID, new PingPayload(numPlayers));

        conn.sendBlocking(msg.toString());
	}

	public IConnection getConnection() {
        return conn;
	}

	private IConnection conn;
}
