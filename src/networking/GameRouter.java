package networking;

import networking.message.Message;
import networking.parser.ParserException;

import java.util.*;
import java.util.concurrent.*;

/**
 * Class represents the entire network of computers making up the game.
 * This class must route all communications for the game (beginning mid handshake)
 *
 * Can be used to abstract over some of the sending / receiving details:
 *     * Verifying Signatures
 *     * Routing of messages through other players
 *
 * The implementation of this class will likely need to change as the protocol progresses
 */
public class GameRouter {
    private Map<IConnection, Set<NetworkClient>> connections;
    private Executor connectionsExecutor;
    private Executor playersExecutor;
    private Map<NetworkClient, BlockingQueue<Message>> messageQueues;


    public GameRouter() {
        connections = new HashMap<>();

        updateExecutors();
    }

    public void addRoute(NetworkClient player, IConnection conn) {
        // Each playerid should only have one route
        removeAllRoutes(player);

        Set<NetworkClient> players = connections.get(conn);

        if(players == null) {
            players = new HashSet<>();
        }

        players.add(player);

        connections.put(conn, players);

        updateExecutors();
    }

    private void updateExecutors() {
        connectionsExecutor = Executors.newFixedThreadPool(connections.size());
        playersExecutor = Executors.newFixedThreadPool(getNumPlayers());
    }

    public void removeRoute(NetworkClient player, IConnection conn) {

        Set<NetworkClient> players = connections.get(conn);

        if(players != null && players.contains(player)) {
            players.remove(player);
        }

        connections.put(conn, players);

        updateExecutors();
    }

    public void removeAllRoutes(NetworkClient player) {
        for(IConnection conn : connections.keySet()) {
            removeRoute(player, conn);
        }
    }

    public int getNumPlayers() {
        HashSet<NetworkClient> players = new HashSet<>();

        connections.values().forEach(players::addAll);

        return players.size();
    }

    // sendToAllPlayers
    public void sendToAllPlayers(Message message) {
        throw new UnsupportedOperationException("Not implemented");

    }

    // receiveFromPlayer
    public Message receiveFromPlayerBlocking(int playerid) {
        throw new UnsupportedOperationException("Not implemented");
    }

    // receiveFromMultiple
    public ExecutorCompletionService<Message> receiveFromAllPlayers() {

        // Wait on all sockets.

        // Wait on all playerid message queues

        // On message on socket, post message to appropriate playerid queue.


        /*for(IConnection conn : connections.keySet()) {
            connectionsExecutor.execute(dispatchMessagesFromConnectionTask(conn, connections.get(conn)));
        }*/

        throw new RuntimeException("Not implemented");
    }

    public Callable<Void> dispatchMessagesFromConnectionTask(IConnection conn, Set<NetworkClient> players) {
        return new Callable<Void>() {

            @Override
            public Void call()  throws ParserException,
                    ConnectionLostException, TimeoutException {

                Set<NetworkClient> playersLeft = new HashSet<>(players);

                // TODO: Timeout
                while (playersLeft.size() > 0 /* && TIMEOUT */) {
                    playersLeft.remove(dispatchMessageTask(conn));
                }

                return null;
            }
        };
    }

    public int dispatchMessageTask(IConnection conn) throws ParserException,
            ConnectionLostException, TimeoutException {

        Message msg = Networking.readMessage(conn);
        BlockingQueue<Message> queue = messageQueues.get(msg.playerid);
        if(queue == null) {
            throw new RuntimeException("Received message from playerid for which GameRouter has no knowledge: " + msg.playerid);
        }
        queue.add(msg);

        return msg.playerid;
    }


    private Callable<Message> readMessageQueueTask(int playerid) {
        return () -> messageQueues.get(playerid).take();
    }
}
