package lobby;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;

import lobby.handler.JoinLobbyEventHandler;
import networking.*;
import org.json.simple.JSONObject;

/**
 * IRemoteGameLobby: A remote network game lobby which could be joined.
 * @author James
 *
 */
public class RemoteGameLobby extends GameLobby {

    JoinLobbyEventHandler handler;
    InetAddress address;
    int port;

    int playerid = -1;
    int acknowledgement_timeout = 1000; // TODO: Move default values to settings
    int move_timeout = 10000;

    public RemoteGameLobby(InetAddress address, int port, JoinLobbyEventHandler handler) {
        this.address = address;
        this.port = port;
        this.handler = handler;
    }

    public void run() {
        try {
            joinLobby();
        }
        catch(Throwable e) {
            handler.onFailure(e);
        }
    }

    // TODO: Write custom Exceptions with user friendly error messages. (instead of just throwing an IOException)
    private void joinLobby() throws IOException {
        IConnection conn = tcpConnect(address, port);
        handler.onTCPConnect();

        sendJoinGame(conn);

        handleJoinGameResponse(conn); // callbacks: onJoinAccepted or onJoinRejected

        handlePings(); // callbacks: onPingStart + onPingReceive

        handleReady(); // callbacks: onReady + onReadyAcknowledge

        decidePlayerOrder(); // callbacks: onDicePlayerOrder + onDiceHash + onDiceNumber

        shuffleCards(); // callbacks: onDiceCardShuffle + onDiceHash + onDiceNumber
    }

    private IConnection tcpConnect(InetAddress address, int port) throws IOException {
        return new Connection(new Socket(address, port));
    }

    private void sendJoinGame(IConnection conn) throws ConnectionLostException {
        Map<String, List<Object>> payload = new HashMap<>();

        payload.put("supported_versions", Arrays.asList((Object)0.1f));
        payload.put("supported_features", new ArrayList<>());

        Message msg = new Message(Command.JOIN, payload);

        conn.send(msg.toString());
    }

    private boolean handleJoinGameResponse(IConnection conn) throws IOException {
        Message msg = Network.readMessage(conn);

        if(msg == null) {
            throw new IOException("Network error: Invalid or no message received from host.");
        }

        if(msg.command == Command.JOIN_ACCEPT) {
            if(!(msg.payload instanceof JSONObject)) {
                throw new IOException("Packet received does not match specification");
            }

            JSONObject payload = (JSONObject) msg.payload;

            if(!(payload.get("player_id") instanceof Integer)) {
                throw new IOException("Packet received does not match specification");
            }

            this.playerid = (Integer)payload.get("player_id");

            handler.onJoinAccepted(this.playerid);
        }
        else if(msg.command == Command.JOIN_REJECT) {
            // TODO Reject message get it from payload.
            handler.onJoinRejected(result);
            return false;
        } else {
            throw new IOException("Incorrect message received. Expected accept/reject_join_game. Received: " msg.command.toString());
        }

    }

	@Override
	public ArrayList<LobbyClient> getPlayers() {
		// TODO Auto-generated method stub
		return null;
	}
}
