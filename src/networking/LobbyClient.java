package networking;

import networking.message.payload.AcceptJoinGamePayload;
import networking.message.Message;
import networking.message.payload.RejectJoinGamePayload;
import networking.parser.Parser;

/**
 * Handles accept / reject process.
 *
 * On accepting LobbyClient, a NetworkClient object is generated.
 * This is to be used for all future communication with this player.
 */
public class LobbyClient {

	protected LobbyClient(IConnection conn, double[] supportedVersions, String[] supportedFeatures, int hostPlayerid, String playerName) {
        this.conn = conn;
        this.supportedVersions = supportedVersions;
		this.supportedFeatures = supportedFeatures;
        this.hostPlayerid = hostPlayerid;
        this.name = playerName;
	}

	public final double[] supportedVersions;
	public final String[] supportedFeatures;
    public final int hostPlayerid;
    public final String name;

    private int playerid;
    private IConnection conn;

    public IConnection getConnection() {
        return conn;
    }

    public int getPlayerid() {
        return playerid;
    }
	
	public boolean accept(int playerid) {
		// send message JOIN_ACCEPT (player_id, ack timeout, move timeout)

        AcceptJoinGamePayload payload = new AcceptJoinGamePayload(playerid, conn.getTimeout(), conn.getTimeout());

		Message msg = new Message(Command.JOIN_ACCEPT, hostPlayerid, payload);
		try {
			conn.sendBlocking(Parser.stringifyMessage(msg));
		}
		catch(ConnectionLostException e) {
			return false;
		}

        this.playerid = playerid;

		return true;
	}
	
	public void reject(String rejectMessage) {
        // send message JOIN_REJECT (player_id, ack timeout, move timeout)
        Message msg = new Message(Command.JOIN_REJECT, hostPlayerid, new RejectJoinGamePayload(rejectMessage));
        try {
            conn.sendBlocking(Parser.stringifyMessage(msg));
        } catch (ConnectionLostException e) {
            return;
        }

        conn.kill();
    }
}
