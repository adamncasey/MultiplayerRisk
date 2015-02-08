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
import networking.message.AcknowledgementPayload;
import networking.message.Message;
import networking.message.PingPayload;

/**
 * LocalGameLobby: A hosted game lobby to which network players can join.
 */
public class LocalGameLobby extends GameLobby {

    private static final int HOST_PLAYERID = 0;

	boolean lobbyOpen = true;
    ServerSocket server;

    private final HostLobbyEventHandler handler;
    private final InetAddress listenAddress;
    private final int port;

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
		throw new RuntimeException("Not implemented");
	}

    /**
     * Call this to stop accepting new clients and begin the game.
     */
    public synchronized void startGame() {
        // Closing the socket unblocks the accept() call
        closeSocket(this.server);
        this.lobbyOpen = false;
    }

	/**
	 * Background thread for the Lobby.
	 */
	public void run() {

        ArrayList<LobbyClient> lobbyClients = new ArrayList<LobbyClient>();

        // Get Clients
        try {
            server = createServerSocket();

            while (isLobbyOpen()) {
                LobbyClient client = getClient(server, lobbyClients);

                if(client != null) {
                    lobbyClients.add(client);
                }
            }

            closeSocket(server);
        } catch (Exception e) {
            // TODO: Log/handle exception properly.
            handler.onFailure(e);
            throw new RuntimeException("Exception occurred in whilst getting client in Host Lobby loop." + e.getMessage());
        }
        // Handles all network traffic for routing purposes (messages received should be re-broadcast etc)
        GameRouter router = setupGameRouter(lobbyClients);

        try {
            pingMessage(router);

            readyMessage(router);

            decidePlayerOrder(router);

            shuffleCards(router);
        } catch(InterruptedException e) {
            // TODO Log exception?
            handler.onFailure(e);
        }
	}

    private GameRouter setupGameRouter(List<LobbyClient> clients) {
        GameRouter router = new GameRouter();

        for(LobbyClient client : clients) {
            router.addRoute(new NetworkClient(router, client.getPlayerid()), client.getConnection());
        }

        return router;
    }

    private void closeSocket(ServerSocket socket) {
        if(!socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                // TODO Not sure we care if there is an exception on close.
            }
        }
    }

    private ServerSocket createServerSocket() throws IOException {
        if(listenAddress != null) {
            return new ServerSocket(port, 0, listenAddress);
        }

        return new ServerSocket(port);
    }

    private boolean pingMessage(GameRouter router) throws InterruptedException {
        boolean result = sendPingToAll(router);

        return result && receivePingFromAll(router);
    }

    private boolean sendPingToAll(GameRouter router) {
        int numPlayers = router.getNumPlayers();

        PingPayload payload = new PingPayload(numPlayers);

        Message msg = new Message(Command.PING, 0, payload);

        router.sendToAllPlayers(msg);
        handler.onPingStart();

        return true;
    }

    private boolean receivePingFromAll(GameRouter router) throws InterruptedException {

        ExecutorCompletionService<Message> executor = router.receiveFromAllPlayers();

        boolean success = true;

        for(int i=0; i<router.getNumPlayers(); i++) {
            Message value;
            try {
                value = executor.take().get();

                success = success && handlePingReply(value);

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

    private boolean handlePingReply(Message msg) {
        if(msg.command != Command.PING) {
            //TODO Properly handle receive invalid message from client
            handler.onFailure(new RuntimeException("Unhandled Invalid message received from client"));
            return false;
        }

        // TODO: Verify we receive a ping from each playerid, not just the right number? Done by GameRouter?
        handler.onPingReceive(msg.playerid);
        return true;
    }

    private boolean readyMessage(GameRouter router) throws InterruptedException {
        Message msg = sendReadyToAll(router);

        // Receive from all ready acknowledgement
        if(msg == null) {
            return false;
        }

        return receiveReadyFromAll(router, msg);
    }

    private boolean receiveReadyFromAll(GameRouter router, Message msg) throws InterruptedException {
        // Ensure we receive an acknowledgement from all players for that ready message

        ExecutorCompletionService<Message> executor = Networking.readAcknowledgementsForMessage(router, msg);

        boolean success = true;

        for(int i=0; i<router.getNumPlayers(); i++) {
            try {
                // Verifies that each acknowledgement was received without error/exception.
                executor.take().get();
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

    private Message sendReadyToAll(GameRouter router) {
        Message msg = new Message(Command.READY, 0, null);

        router.sendToAllPlayers(msg);
        handler.onReady();

        return msg;
    }

    private void decidePlayerOrder(GameRouter router) {
        // Roll Dice.

        // Number retrieved determines order
        int firstplayer = 0; // rand from dice

        // Re-arrange for the correct play order
        //Collections.rotate(players, firstplayer);
    }
    private void shuffleCards(GameRouter router) {
        // Roll Dice

        // for 0 <= i < decksize
            // swap(card[i], card[rand from dice]);
    }

    private LobbyClient getClient(ServerSocket server, List<LobbyClient> players) throws IOException {
        Socket newClient;
        try {
            newClient = server.accept();
        } catch(SocketException e) {
            // Potentially the game has been started
            closeSocket(server);
            return null;
        }
        LobbyClient lobbyClient = Networking.getLobbyClient(new Connection(newClient), HOST_PLAYERID);

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
