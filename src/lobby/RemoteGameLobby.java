package lobby;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;

import networking.*;
import networking.message.AcceptJoinGamePayload;
import networking.message.JoinGamePayload;
import networking.message.Message;
import networking.message.RejectJoinGamePayload;

/**
 * IRemoteGameLobby: A remote network game lobby which could be joined.
 * @author James
 *
 */
public class RemoteGameLobby extends GameLobby {
	private String error;

    private InetAddress address;
    private int port;
    private int playerid = -1;
    
//    private int acknowledgement_timeout = 1000;
//    private int move_timeout = 10000;

    public RemoteGameLobby(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }

    public boolean joinLobby() {
    	boolean success = false;
    	
        IConnection conn;
		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(address, port), 3000);
			conn = new Connection(socket);
	        if(sendJoinGame(conn) && handleJoinGameResponse(conn)){
	        	success = true;
	        }

	        //TODO Implement these steps of handshake

	        //handlePings(); // callbacks: onPingStart + onPingReceive

	        //handleReady(); // callbacks: onReady + onReadyAcknowledge

	        //decidePlayerOrder(); // callbacks: onDicePlayerOrder + onDiceHash + onDiceNumber

	        //shuffleCards(); // callbacks: onDiceCardShuffle + onDiceHash + onDiceNumber
	        
		} catch (IOException e) {
			setError(e.getMessage());
		}

        return success;
    }

    private boolean sendJoinGame(IConnection conn) {
    	boolean success = false;
    	
        JoinGamePayload payload = new JoinGamePayload(new double[] { 0.1 }, new String[] {});
        Message msg = new Message(Command.JOIN_GAME, payload);
        
        try {
			conn.send(msg.toString());
			success = true;
		} catch (ConnectionLostException e) { setError(e.getMessage()); }
        
        return success;
    }

    private boolean handleJoinGameResponse(IConnection conn) {
    	boolean success = false;
    	
        try {
        	Message msg = Network.readMessage(conn);
        	if(msg.command == Command.JOIN_ACCEPT) {
                AcceptJoinGamePayload payload = (AcceptJoinGamePayload)msg.payload;
                this.playerid = payload.playerid;
                success = true;
            }
            else if(msg.command == Command.JOIN_REJECT) {
                RejectJoinGamePayload payload = (RejectJoinGamePayload)msg.payload;
                setError("Lobby join rejected");
            } else {
                setError("Incorrect message received. Expected accept/reject_join_game. Received: " + msg.command.toString());
            }
            
        } catch(Exception e) {
            setError(e.getMessage());
        }
        
        return success;
    }

	@Override
	public ArrayList<LobbyClient> getPlayers() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getError() {
		return error;
	}

	private void setError(String error) {
		this.error = error;
	}
}
