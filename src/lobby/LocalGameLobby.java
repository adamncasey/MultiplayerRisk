package lobby;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;

import lobby.handler.HostLobbyEventHandler;
import networking.Connection;
import networking.LobbyClient;
import networking.Network;

/**
 * LocalGameLobby: A hosted game lobby to which network players can join.
 */
public class LocalGameLobby extends GameLobby {

	ArrayList<LobbyClient> players = new ArrayList<LobbyClient>();
	
	boolean lobbyOpen = true;

    private HostLobbyEventHandler handler;
    private InetAddress listenAddress;
    private int port;

    public LocalGameLobby(HostLobbyEventHandler handler, int port) {
        this(handler, port, null);
    }

	public LocalGameLobby(HostLobbyEventHandler handler, int port, InetAddress listenAddress) {
        this.handler = handler;
        this.listenAddress = listenAddress;
        this.port = port;
	}

	@Override
	public ArrayList<LobbyClient> getPlayers() {
		return players;
	}

	/**
	 * Background thread for the Lobby.
	 */
	public void run() {
        // Get Clients
        try {
            ServerSocket server = new ServerSocket(port, 0, listenAddress);

            while (isLobbyOpen()) {
                LobbyClient client = getClient(server);

                if(client != null) {
                    players.add(client);
                }
            }

            server.close();
        } catch (Exception e) {
            // TODO: Log/handle exception properly.
            throw new RuntimeException("Exception occurred in whilst getting client in Host Lobby loop.");
        }

        pingClients();

        readyMessage();

        decidePlayerOrder();

        shuffleCards();
	}

    private void pingClients() {
        // send to all Ping Message

        handler.onPingStart();

        // receive from all ping reply
            handler.onPingReceive(0);
    }
    private void readyMessage() {
        // send Ready message

        handler.onReady();

        // Receive from all ready acknowledgement
            handler.onReadyAcknowledge(0);
    }
    private void decidePlayerOrder() {
        // Roll Dice.

        // Number retrieved determines order
        int firstplayer = 0; // rand from dice

        // Re-arrange for the correct play order
        Collections.rotate(players, firstplayer);
    }
    private void shuffleCards() {
        // Roll Dice

        // for 0 <= i < decksize
            // swap(card[i], card[rand from dice]);
    }

    private LobbyClient getClient(ServerSocket server) throws IOException {
        Socket newClient = server.accept();
        LobbyClient lobbyClient = Network.getLobbyClient(new Connection(newClient));

        String result = handler.onPlayerJoinRequest(lobbyClient);

        if(result == null) {
            // accept client
            int playerid = players.size() + 1;
            if(lobbyClient.accept(playerid)) {

                handler.onPlayerJoin(playerid);
                return lobbyClient;
            }
        }

        lobbyClient.reject(result);
        return null;
    }

    private LobbyMulticastThread startMulticastThread() {
        // TODO: This is not spec'ed, not used at the moment
        // Start broadcasting the lobby.

        LobbyMulticastThread multicastThread = new LobbyMulticastThread("" /*friendlyName*/);
        multicastThread.start();

        return multicastThread;
        // TODO: Also needs stopMulticastThread
    }

    /**
     * Checks the lobbyOpen value in a thread-safe manner.
     * @return lobbyOpen
     */
    private synchronized boolean isLobbyOpen() {
        return lobbyOpen;
    }

	/**
	 * Stops accepting new clients and begins the game
	 */
	public synchronized void startGame() {
		this.lobbyOpen = false;
	}
}
