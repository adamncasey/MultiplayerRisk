package lobby;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;

import networking.message.payload.*;
import player.IPlayer;
import settings.Settings;
import lobby.handler.JoinLobbyEventHandler;
import lobby.handler.LobbyEventHandler;
import networking.*;
import networking.message.*;
import networking.parser.ParserException;

/**
 * IRemoteGameLobby: A remote network game lobby which could be joined.
 * @author James
 *
 */
public class RemoteGameLobby extends Thread {

    JoinLobbyEventHandler handler;
    InetAddress address;
    int port;

    int playerid = -1;

    boolean nonPlayingHost;

    // TODO Implement timeouts in networking

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

    // TODO: Write custom Exceptions with user friendly error messages. (instead of just throwing RuntimeException)
    private void joinLobby() throws IOException {

        GameRouter router = new GameRouter();
        NetworkClient host = new NetworkClient(router, LocalGameLobby.HOST_PLAYERID, true);


        IConnection conn = tcpConnect(address, port);
        handler.onTCPConnect();

        router.addRoute(host, conn);


        sendJoinGame(router);

        if(!handleJoinGameResponse(host)) { // callbacks: onJoinAccepted or onJoinRejected
            return;
        }
        // Now we have a playerid


        int firstPlayer;
        List<NetworkClient> otherPlayers = new LinkedList<>();
        otherPlayers.add(host);
        try {
            List<NetworkClient> nonHostOtherPlayers = handlePings(router, conn, host); // callbacks: onPingReceive
            otherPlayers.addAll(nonHostOtherPlayers);

            if(!handleReady(router, host, nonHostOtherPlayers)) {
                return; // callbacks: onReady + onReadyAcknowledge
            }

            if (!receiveInitialiseGame(router, host)) {
                return;
            }

            firstPlayer = decidePlayerOrder(); // callbacks: onDicePlayerOrder + onDiceHash + onDiceNumber

            //shuffleCards(); // callbacks: onDiceCardShuffle + onDiceHash + onDiceNumber
        } catch(InterruptedException e) {
            // TODO Tidy up logging: Log exception?
            handler.onFailure(e);
            return;
        }

        LinkedList<IPlayer> playersBefore = new LinkedList<>();
        LinkedList<IPlayer> playersAfter = new LinkedList<>();
        LobbyUtil.createIPlayersInOrder(otherPlayers, firstPlayer, playerid, playersBefore, playersAfter);

        // TODO Pass cards up to onLobbyComplete handler
        handler.onLobbyComplete(playersBefore, playersAfter, null);
    }


    private void addOtherPlayersToRouter(GameRouter router, IConnection conn, Collection<NetworkClient> players) {
        for(NetworkClient client : players) {
            router.addRoute(client, conn);
        }
    }

    private IConnection tcpConnect(InetAddress address, int port) throws IOException {
    	Socket soc = new Socket();
    	soc.connect(new InetSocketAddress(address, port), Settings.socketTimeout);
    	soc.setSoTimeout(Settings.socketTimeout);
        return new Connection(soc);
    }

    private void sendJoinGame(GameRouter router) throws ConnectionLostException {

        JoinGamePayload payload = new JoinGamePayload(new double[] { 0.1 }, new String[] {});

        Message msg = new Message(Command.JOIN_GAME, payload);

        router.sendToAllPlayers(msg);
    }

    private boolean handleJoinGameResponse(NetworkClient host) throws IOException {
        Message msg;
        try {
            msg = host.readMessage();
        } catch(ParserException e) {
            // TODO Clean up logging
            e.printStackTrace();
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
        }
        else {
            throw new RuntimeException("Incorrect message received. Expected accept/reject_join_game. Received: " + msg.command.toString());
        }
    }

    /**
     *
     * @param router
     * @param conn
     * @param host
     * @return Collection of NetworkClients - Containing all other players in the game (Not host or this local player)
     * @throws InterruptedException
     */
    private List<NetworkClient> handlePings(GameRouter router, IConnection conn, NetworkClient host) throws InterruptedException {
        int numplayers = receiveHostPing(host);
        handler.onPingStart();

        if(numplayers < 2) {
            throw new RuntimeException("Invalid number of players received from host. " + numplayers);
        }
        List<NetworkClient> players = setupOtherPlayers(router, conn, numplayers);

        // Send ping to all other players
        sendPing(router);

        // Receive ping from all other players
        // TODO Try to receive ping from all other players.
        // TODO But if we get a ready command from the host, we should stop trying.
        receivePingResponseFromConnections(players);
        return players;
    }

    private int receiveHostPing(NetworkClient host) {
        Message msg;
        try {
            msg = host.readMessage();
        } catch (TimeoutException | ConnectionLostException | ParserException e) {
            e.printStackTrace();
            handler.onFailure(e);
            throw new RuntimeException("Invalid message received from host");
        }

        int numplayers = handlePingMessage(msg);

        if(msg.playerid == null) {
            nonPlayingHost = true;
        } else {
            if(msg.playerid < 0) {
                throw new RuntimeException("Host did not specific playerid. Playerid must be explicitly null for non playing host");
            }
            nonPlayingHost = false;
        }

        return numplayers;
    }

    private List<NetworkClient> setupOtherPlayers(GameRouter router, IConnection conn, int numplayers) {
        List<NetworkClient> clients = new LinkedList<>();

        for(int i=1; i<numplayers; i++) {
            if(i != this.playerid) {
                System.out.println("Added playerid " + i);
                clients.add(new NetworkClient(router, i, false));
            }
        }

        addOtherPlayersToRouter(router, conn, clients);

        return clients;
    }

    private void receivePingResponseFromConnections(Collection<NetworkClient> connections) throws InterruptedException {
        System.out.println("receivePingResponseFromConnections " + connections.size());
        if(connections.size() == 0) {
            return;
        }

        ExecutorCompletionService<Message> ecs = Networking.readMessageFromConnections(connections);

        for(NetworkClient client : connections) {
            try {
                Future<Message> msg = ecs.take();

                processPingMessage(msg);
            } catch (InterruptedException e) {
                throw new RuntimeException("Timeout?");
            }
        }
    }

    private void processPingMessage(Future<Message> futureMsg) throws InterruptedException {

        Message msg;
        try {
             msg = futureMsg.get();
        } catch (ExecutionException e) {
            Throwable e2 = e.getCause();

            if(e2 instanceof ConnectionLostException
                    || e2 instanceof TimeoutException
                    || e2 instanceof ParserException) {
                throw new RuntimeException("Unable to receive ping message: " + e2.getClass().toString() + e2.getMessage());
            }
            // TODO tidy up logging
            e.printStackTrace();
            e2.printStackTrace();
            handler.onFailure(e2);
            return;
        }
        handlePingMessage(msg);
    }

    private int handlePingMessage(Message msg) {
        if(msg.command != Command.PING) {
            throw new RuntimeException("Invalid message");
        }

        handler.onPingReceive(msg.playerid);

        if(msg.payload instanceof IntegerPayload) {
            return ((IntegerPayload)msg.payload).value;
        }

        return -1;
    }

    private void sendPing(GameRouter router) {
        Message ping = new Message(Command.PING, playerid, null);

        router.sendToAllPlayers(ping);
    }

    private boolean handleReady(GameRouter router, NetworkClient host, Collection<NetworkClient> players) {
        // receive Ready Message
        Message msg = receiveReadyFromHost(host);

        // acknowledge
        acknowledgeMessage(msg, router);

        // receive all other acknowledgements
        return readyAcknowledgements(router, players, msg, handler);
    }

    protected static boolean readyAcknowledgements(GameRouter router, Collection<NetworkClient> players, Message msg, LobbyEventHandler handler) {

        List<Integer> responses = Networking.readAcknowledgementsForMessageFromPlayers(router, msg, players);

        for(int playerid : responses) {
            handler.onReadyAcknowledge(playerid);
        }

        if(responses.size() != players.size()) {
            handler.onFailure(new IOException("Did not receive acknowledgement from all players"));
            return false;
        }

        return true;
    }

    private void acknowledgeMessage(Message msg, GameRouter router) {
        Message response = Acknowledgement.acknowledgeMessage(msg, this.playerid);

        router.sendToAllPlayers(response);
    }

    private Message receiveReadyFromHost(NetworkClient host) {
        Message msg;
        try {
            msg = host.readMessage();
        } catch (TimeoutException | ConnectionLostException | ParserException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to receive ping message: " + e.getClass().toString() + " " + e.getMessage());
        }

        if(msg.command != Command.READY) {
            throw new RuntimeException("Invalid message type");
        }

        handler.onReady();

        return msg;
    }

    private boolean receiveInitialiseGame(GameRouter router, NetworkClient host) {
        Message msg;
        try {
            msg = host.readMessage();
        } catch (TimeoutException | ConnectionLostException | ParserException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to receive initialise_game message: " + e.getClass().toString() + " " + e.getMessage());
        }

        InitialiseGamePayload payload = (InitialiseGamePayload)msg.payload;

        handler.onInitialiseGame(payload.version, payload.features);

        return true;
    }

    private int decidePlayerOrder() {
        return 0;
    }
}
