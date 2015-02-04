package lobby;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.stream.Collectors;

import lobby.handler.HostLobbyEventHandler;
import lobby.handler.LobbyEventHandler;
import networking.*;
import networking.message.Message;

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
     * Call this to stop accepting new clients and begin the game.
     */
    public synchronized void startGame() {
        this.lobbyOpen = false;
    }

	/**
	 * Background thread for the Lobby.
	 */
	public void run() {
        // Get Clients
        try {
            ServerSocket server = createServerSocket();

            while (isLobbyOpen()) {
                LobbyClient client = getClient(server);

                if(client != null) {
                    players.add(client);
                }
            }

            server.close();
        } catch (Exception e) {
            // TODO: Log/handle exception properly.
            handler.onFailure(e);
            throw new RuntimeException("Exception occurred in whilst getting client in Host Lobby loop." + e.getMessage());
        }
        // At this point it makes sense to promote people to NetworkPlayers
        // Only sendToAll, receiveFrom, receiveFromMultiple from now on.

        try {
            pingMessage();

            readyMessage();

            decidePlayerOrder();

            shuffleCards();
        } catch(InterruptedException e) {
            // TODO Log exception?
            handler.onFailure(e);
        }
	}

    private ServerSocket createServerSocket() throws IOException {
        if(listenAddress != null) {
            return new ServerSocket(port, 0, listenAddress);
        }

        return new ServerSocket(port);
    }

    private void pingMessage() throws InterruptedException {
        sendPingToAll(this.players, handler);

        handler.onPingStart();

        receivePingFromAll(this.players, handler);
    }

    private static void sendPingToAll(List<LobbyClient> players, LobbyEventHandler handler) {
        int numPlayers = players.size();

        for(LobbyClient client : players) {
            try {
                client.sendPing(numPlayers);
            } catch(ConnectionLostException e) {
                //TODO Spec currently has no real way of handling this problem
                handler.onFailure(e);
            }
        }
    }

    private static boolean receivePingFromAll(List<LobbyClient> players, LobbyEventHandler handler) throws InterruptedException {

        ExecutorCompletionService<Message> executor = readMessageFromClients(players);

        boolean success = true;

        for(int i=0; i<players.size(); i++) {
            Message value = null;
            try {
                value = executor.take().get();

                if(value.command != Command.PING) {
                    //TODO Handle receive invalid message from client
                    throw new RuntimeException("Unhandled Invalid message received from client");
                }

                // TODO: Currently trusting the playerid received in the message. Possibly fine, probably bad.
                handler.onPingReceive(value.playerid);

                // TODO Forward message to other users.
                        // This functionality should probably be lower level
                System.out.println("Should be forwarding all received messages to other users");

            } catch (ExecutionException e) {
                Throwable ex = e.getCause();

                handler.onFailure(ex);
                // TODO tidy this up when onFailure is confirmed to be a working error handler.
                // TODO Should we also log this?
                e.printStackTrace();
                ex.printStackTrace();

                success = false;
            }
        }

        return success;
    }

    private void readyMessage() {
        // sendBlocking Ready message

        handler.onReady();

        // Receive from all ready acknowledgement
            // Check acknowledgement error is zero
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
        LobbyClient lobbyClient = Networking.getLobbyClient(new Connection(newClient));

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

    private static ExecutorCompletionService<Message> readMessageFromClients(List<LobbyClient> clients) {
        List<IConnection> connections = clients.stream()
                .map( LobbyClient::getConnection )
                .collect(Collectors.toList());

        return Networking.readMessageFromConnections(connections);
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
}
