package lobby;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;

import lobby.handler.HostLobbyEventHandler;
import networking.*;
import networking.message.Message;
import networking.message.PingPayload;

/**
 * LocalGameLobby: A hosted game lobby to which network players can join.
 */
public class LocalGameLobby extends Thread {

    protected static final int HOST_PLAYERID = 0;

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
        GameRouter router = new GameRouter();
        ArrayList<LobbyClient> lobbyClients = new ArrayList<>();

        // Get Clients
        try {

            server = createServerSocket();

            while (isLobbyOpen()) {
                int newplayerid = lobbyClients.size() + 1;

                LobbyClient client = getClient(server, newplayerid);

                if(client != null) {
                    lobbyClients.add(client);

                    NetworkClient newPlayer = new NetworkClient(router, newplayerid);
                    router.addRoute(newPlayer, client.getConnection());

                    //TODO Should also set up message mirroring connections
                }
            }

            closeSocket(server);
        } catch (Exception e) {
            // TODO: Log/handle exception properly.
            handler.onFailure(e);
            throw new RuntimeException("Exception occurred in whilst getting client in Host Lobby loop." + e.getMessage());
        }

        setupRouterForwarding(router, lobbyClients);

        // At this point the router should have all knowledge required to send messages to appropriate users.
        // lobbyClients is a weird mix (needs refactoring). Will only contain lobby-specific information

        try {
            pingMessage(router);

            readyMessage(router);

            decidePlayerOrder(router);

            shuffleCards(router);

            // Pick version & features compatible with players.

            // initialise game.
        } catch(InterruptedException e) {
            // TODO Log exception?
            handler.onFailure(e);
        }
	}

    private void setupRouterForwarding(GameRouter router, List<LobbyClient> clients) {
        // Tells the GameRouter to forward messages received from clients to every other client
        for(LobbyClient client : clients) {
            for(LobbyClient client2 : clients) {
                if(client2 != client) {
                    router.addBridge(client.getConnection(), client2.getConnection());
                }
            }
        }
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
        int numPlayers = router.getNumPlayers() + 1;

        PingPayload payload = new PingPayload(numPlayers);

        Message msg = new Message(Command.PING, 0, payload);

        router.sendToAllPlayers(msg);
        handler.onPingStart();

        return true;
    }

    private boolean receivePingFromAll(GameRouter router) throws InterruptedException {

        ExecutorCompletionService<Message> executor = Networking.readMessageFromConnections(router.getAllPlayers());

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

        RemoteGameLobby.readAcknowledgementsForMessageFromPlayers(router, msg, router.getAllPlayers(), handler);

        return true;
    }

    private Message sendReadyToAll(GameRouter router) {
        Message msg = new Message(Command.READY, 0, null, true);

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

    private LobbyClient getClient(ServerSocket server, int newplayerid) throws IOException {
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
            if(lobbyClient.accept(newplayerid)) {

                handler.onPlayerJoin(newplayerid);
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
    
    public synchronized void closeLobby() {
    	lobbyOpen = false;
    	try {
    		if(server != null) {
    			server.close();
    		}
		} catch (Exception e) {
		}
    }
}
