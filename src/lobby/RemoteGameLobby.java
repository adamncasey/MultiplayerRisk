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

    // TODO Implement timeouts in networking
    int acknowledgement_timeout = 1000; // TODO: Move default values to settings
    int move_timeout = 10000;

    public RemoteGameLobby(IPlayer localPlayer, InetAddress address, int port, JoinLobbyEventHandler handler) {
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

    // TODO: Write custom Exceptions with user friendly error messages. (instead of just throwing IOException/RuntimeException)
    private void joinLobby() throws IOException {
        IConnection conn = tcpConnect(address, port);
        handler.onTCPConnect();

        GameRouter router = new GameRouter();
        NetworkClient host = new NetworkClient(router, LocalGameLobby.HOST_PLAYERID);
        router.addRoute(host, conn);

        sendJoinGame(router);

        if(!handleJoinGameResponse(host)) { // callbacks: onJoinAccepted or onJoinRejected
            return;
        }
        // Now we have a playerid

        try {
            Collection<NetworkClient> otherPlayers = handlePings(router, host); // callbacks: onPingStart + onPingReceive

            addOtherPlayersToRouter(router, conn, otherPlayers);

            handleReady(router, host, otherPlayers); // callbacks: onReady + onReadyAcknowledge

            //decidePlayerOrder(); // callbacks: onDicePlayerOrder + onDiceHash + onDiceNumber

            //shuffleCards(); // callbacks: onDiceCardShuffle + onDiceHash + onDiceNumber
        } catch(InterruptedException e) {
            // TODO Tidy up logging: Log exception?
            handler.onFailure(e);
        }
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
     * @param host
     * @return Collection of NetworkClients - Containing all other players in the game (Not host or this local player)
     * @throws InterruptedException
     */
    private Collection<NetworkClient> handlePings(GameRouter router, NetworkClient host) throws InterruptedException {

        int numplayers = receiveHostPing(host);
        handler.onPingStart();

        if(numplayers < 2) {
            throw new RuntimeException("Invalid number of players received from host. " + numplayers);
        }

        Set<NetworkClient> players = setupOtherPlayers(router, numplayers);

        // Send ping to all other players
        sendPing(router);

        // Receive ping from all other players
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

        return handlePingMessage(msg);
    }

    private Set<NetworkClient> setupOtherPlayers(GameRouter router, int numplayers) {
        Set<NetworkClient> clients = new HashSet<>();

        for(int i=1; i<numplayers; i++) {
            if(i != this.playerid) {
                clients.add(new NetworkClient(router, i));
            }
        }

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

        if(msg.payload instanceof PingPayload) {
            return ((PingPayload)msg.payload).numPlayers;
        }

        return -1;
    }

    private void sendPing(GameRouter router) {
        Message ping = new Message(Command.PING, playerid, null);

        router.sendToAllPlayers(ping);
    }

    private void handleReady(GameRouter router, NetworkClient host, Collection<NetworkClient> players) {
        // receive Ready Message
        Message msg = receiveReadyFromHost(host);

        // acknowledge
        acknowledgeMessage(msg, router);

        // receive all other acknowledgements

        //TODO Finish this
        readAcknowledgementsForMessageFromPlayers(router, msg, players, handler);
    }

    protected static void readAcknowledgementsForMessageFromPlayers(GameRouter router, Message message, Collection<NetworkClient> players, LobbyEventHandler handler) {
        if(players.size() == 0) { // TODO this zero check should be somewhere else. Duplicated currently.
            return;
        }
        ExecutorCompletionService<Message> ecs = Networking.readMessageFromConnections(players);

        for(NetworkClient client : players) {
            try {
                Future<Message> ack = ecs.take();

                processAcknowledgement(message, ack, handler);
            } catch (InterruptedException e) {
                throw new RuntimeException("Timeout?");
            }
        }
    }

    // TODO refactor somewhere useful (Used across Local/Remote GameLobby)
    protected static void processAcknowledgement(Message message, Future<Message> futureAck, LobbyEventHandler handler) throws InterruptedException {
        Message ack;
        try {
            ack = futureAck.get();
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
            throw new RuntimeException("Unable to receive ping message: " + e.getClass().toString() + e.getMessage());
        }

        if(ack.command != Command.ACKNOWLEDGEMENT) {
            throw new RuntimeException("invalid message");
        }

        if(!(ack.payload instanceof AcknowledgementPayload)) {
            throw new RuntimeException("invalid message");
        }

        AcknowledgementPayload payload = (AcknowledgementPayload)ack.payload;
        if(payload.ack_id != message.ackId) {
            throw new RuntimeException("Bad acknowledgement");
        }

        if(payload.response_code != 0 ) {
            throw new RuntimeException("Bad response code. Error!");
        }

        // Otherwise ALL OK.
        handler.onReadyAcknowledge(ack.playerid);
    }

    private void acknowledgeMessage(Message msg, GameRouter router) {
        Message response = Acknowledgement.acknowledgeMessage(msg, 0, null, this.playerid, false);

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
}
