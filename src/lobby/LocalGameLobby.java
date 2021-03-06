package lobby;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;

import lobby.handler.HostLobbyEventHandler;
import logic.state.Board;
import logic.state.Deck;
import networking.*;
import networking.message.Message;
import networking.message.payload.InitialiseGamePayload;
import networking.message.payload.IntegerPayload;
import networking.message.payload.PlayersJoinedPayload;
import player.IPlayer;

/**
 * LocalGameLobby: A hosted game lobby to which network players can join.
 */
public class LocalGameLobby extends Thread {

    public static final int HOST_PLAYERID = 0;

	boolean lobbyOpen = true;
	boolean lobbyCancelled = false;
	
    ServerSocket server;

    private final HostLobbyEventHandler handler;
    private final InetAddress listenAddress;
    private final int port;
    private final String name;

    private Deck deck;

    public LocalGameLobby(HostLobbyEventHandler handler, int port, String name) {
        this(handler, port, null, name);
    }

	public LocalGameLobby(HostLobbyEventHandler handler, int port, InetAddress listenAddress, String name) {
        this.handler = handler;
        this.listenAddress = listenAddress;
        this.port = port;
        this.name = name;
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
        router.startRouting();
        ArrayList<LobbyClient> lobbyClients = new ArrayList<>();
        List<NetworkClient> netClients = new LinkedList<>();

        // Get Clients
        try {

            server = createServerSocket();

            while (isLobbyOpen() && !isLobbyCancelled()) {
                int newplayerid = lobbyClients.size() + 1;

                LobbyClient client = getClient(server, newplayerid);

                if(client != null) {
                    lobbyClients.add(client);

                    NetworkClient newPlayer = new NetworkClient(router, newplayerid, client.name);
                    router.addRoute(newPlayer, client.getConnection());

                    netClients.add(newPlayer);

                    playersJoined(router, netClients);
                }
            }

            closeSocket(server);
        } catch (Exception e) {
            handler.onFailure(e);
            return;
        }

        if(!isLobbyCancelled()) {
        	
            setupRouterForwarding(router, lobbyClients);

            // At this point the router should have all knowledge required to send messages to appropriate users.
            // lobbyClients is a weird mix (needs refactoring). Will only contain lobby-specific information

            int firstPlayer;

            try {
                if(!pingMessage(router)) {
                    return;
                }

                if(!readyMessage(router)) {
                    return;
                }

                // Pick version & features compatible with players...
                initialiseMessage(router);

                firstPlayer = LobbyUtil.decidePlayerOrder(router, HOST_PLAYERID, netClients, handler);

                Board board = new Board();
                this.deck = board.getDeck();
                LobbyUtil.shuffleCards(router, LocalGameLobby.HOST_PLAYERID, netClients, deck, handler); // perhaps GameRouter is needed here?

            } catch(InterruptedException e) {
                handler.onFailure(e);
                return;
            }
            
        	// collect the IPlayers together.
            List<IPlayer> playersBefore = new LinkedList<>();
            List<IPlayer> playersAfter = new LinkedList<>();

            LobbyUtil.createIPlayersInOrder(netClients, firstPlayer, HOST_PLAYERID, playersBefore, playersAfter);

            handler.onLobbyComplete(playersBefore, playersAfter, deck, new NetworkLocalPlayerHandler(router));
        }
	}

    private void playersJoined(GameRouter router, Collection<NetworkClient> otherClients) {
        // Generate Players Joined message
        PlayersJoinedPayload.PlayerInfo[] playerInfos = new PlayersJoinedPayload.PlayerInfo[otherClients.size() + 1];
        playerInfos[0] = new PlayersJoinedPayload.PlayerInfo(LocalGameLobby.HOST_PLAYERID, this.name);

        int i=1;
        for(NetworkClient client : otherClients) {
            playerInfos[i++] = new PlayersJoinedPayload.PlayerInfo(client.playerid, client.getName());
        }

        Message msg =  new Message(Command.PLAYERS_JOINED, LocalGameLobby.HOST_PLAYERID, new PlayersJoinedPayload(playerInfos), false);

        router.sendToAllPlayers(msg);
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
                // This exception would have no impact on any part of the game.
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

        IntegerPayload payload = new IntegerPayload(numPlayers);

        Message msg = new Message(Command.PING, HOST_PLAYERID, payload);

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

                success = false;
            }
        }

        return success;
    }

    private boolean handlePingReply(Message msg) {
        if(msg.command != Command.PING) {
            handler.onFailure(new RuntimeException("Unhandled Invalid message received from client"));
            return false;
        }

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

        return RemoteGameLobby.readyAcknowledgements(router, null, router.getAllPlayers(), msg, handler);
    }

    private Message sendReadyToAll(GameRouter router) {
        Message msg = new Message(Command.READY, HOST_PLAYERID, null, true);

        router.sendToAllPlayers(msg);
        handler.onReady();

        return msg;
    }

    private void initialiseMessage(GameRouter router) {
        double version = 1.0;
        String[] features = new String[0];

        // TODO This version number shouldn't be hard coded. However finding a subset of supported features / versions is full of problems.
        InitialiseGamePayload payload = new InitialiseGamePayload(version, features);
        Message msg = new Message(Command.INITIALISE_GAME, HOST_PLAYERID, payload, false);

        router.sendToAllPlayers(msg);

        handler.onInitialiseGame(version, features);
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

                handler.onPlayerJoin(newplayerid, lobbyClient.name);
                return lobbyClient;
            }
        }

        lobbyClient.reject(result);
        return null;
    }

    /**
     * Checks the lobbyOpen value in a thread-safe manner.
     * @return lobbyOpen
     */
    private synchronized boolean isLobbyOpen() {
        return lobbyOpen;
    }
    
    private synchronized boolean isLobbyCancelled() {
        return lobbyCancelled;
    }

    public synchronized void cancelLobby() {
    	lobbyCancelled = true;
    	lobbyOpen = false;
    	closeSocket(server);
    }
}
