package lobby;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;

import lobby.handler.JoinLobbyEventHandler;
import networking.*;
import networking.message.AcceptJoinGamePayload;
import networking.message.JoinGamePayload;
import networking.message.Message;
import networking.message.RejectJoinGamePayload;
import networking.parser.ParserException;

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

        if(!handleJoinGameResponse(conn)) { // callbacks: onJoinAccepted or onJoinRejected
            return;
        }

        handlePings(); // callbacks: onPingStart + onPingReceive

        handleReady(); // callbacks: onReady + onReadyAcknowledge

        decidePlayerOrder(); // callbacks: onDicePlayerOrder + onDiceHash + onDiceNumber

        shuffleCards(); // callbacks: onDiceCardShuffle + onDiceHash + onDiceNumber
    }

    private void handlePings() {
        // Receive ping from host.

        // Send ping to all other players

        // Receive ping from all other players
    }

    private IConnection tcpConnect(InetAddress address, int port) throws IOException {
        return new Connection(new Socket(address, port));
    }

    private void sendJoinGame(IConnection conn) throws ConnectionLostException {

        JoinGamePayload payload = new JoinGamePayload(new double[] { 0.1 }, new String[] {});

        Message msg = new Message(Command.JOIN_GAME, payload);

        conn.sendBlocking(msg.toString());
    }

    private boolean handleJoinGameResponse(IConnection conn) throws IOException {
        Message msg;
        try {
            msg = Networking.readMessage(conn);
        } catch(ParserException e) {
            e.printStackTrace();
            // TODO we shouldn't be printing here
            System.out.println(e.getMessage() + " cannot handle response");
            return false;
        } catch (TimeoutException e) {
			e.printStackTrace();
			return false;
		}

        if(msg == null) {
            throw new IOException("Networking error: Invalid or no message received from host.");
        }

        if(msg.command == Command.JOIN_ACCEPT) {
            AcceptJoinGamePayload payload = (AcceptJoinGamePayload)msg.payload;

            this.playerid = payload.playerid;

            handler.onJoinAccepted(this.playerid);

            return true;
        }
        else if(msg.command == Command.JOIN_REJECT) {
            RejectJoinGamePayload payload = (RejectJoinGamePayload)msg.payload;
            handler.onJoinRejected(payload.message);
            return false;
        } else {
            throw new IOException("Incorrect message received. Expected accept/reject_join_game. Received: " + msg.command.toString());
        }
    }

	@Override
	public ArrayList<LobbyClient> getPlayers() {
		// TODO Auto-generated method stub
		return null;
	}
}
