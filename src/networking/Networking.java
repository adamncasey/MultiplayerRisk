package networking;

import lobby.handler.LobbyEventHandler;
import networking.message.payload.IntegerPayload;
import networking.message.payload.JoinGamePayload;
import networking.message.Message;
import networking.parser.Parser;
import networking.parser.ParserException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

// General "Networking Utilities" class.
public class Networking {
	/**
	 * Gets a risk player connection from the socket
	 * 
	 * @param socket
	 * @return A LobbyClient object, which may be used to accept or reject the
	 *         join request.
	 * @return null if an error occurs during the initial connection
	 */
	public static LobbyClient getLobbyClient(IConnection socket, int hostPlayerid) {

		Message message;
		try {
			message = Networking.readMessage(socket);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}

		if (message == null || message.command != Command.JOIN_GAME) {
			return null;
		}

		JoinGamePayload payload = (JoinGamePayload) message.payload;

		return new LobbyClient(socket, payload.supportedVersions,
				payload.supportedFeatures, hostPlayerid);
	}

	/**
	 * Reads a line from conn, and parses it as a JSON Object in the protocol
	 * format.
	 * 
	 * @param conn
	 *            - Connection to read from
	 * @return Message on success
     * @throws Exception on error. ParserException for invalid packet.
     * ConnectionLostException or TimeoutException for network related errors
	 */
	public static Message readMessage(IConnection conn) throws ParserException,
			ConnectionLostException, TimeoutException {
		// Assumes newline is equivalent to JSON Object boundary. Waiting on
		// representatives to formally agree on this
		String msgString = conn.receiveLine();
		if(msgString == null) { 
			throw new ParserException("Received null message");
		}
		else
		{
			return Parser.parseMessage(msgString);
		}
	}

    private static Callable<Message> readMessageAsync(NetworkClient client) {
        return new Callable<Message>() {

            @Override
            public Message call() throws Exception {
                return client.readMessage();
            }
        };
    }

    /**
     * Read a message from every connection given.
     * @param clients - Clients to read from
     * @return An ExecutorCompletionService object. Can be used to retrieve Messages as they arrive.
     */
    public static ExecutorCompletionService<Message> readMessageFromConnections(Collection<NetworkClient> clients) {
        if(clients.size() == 0) {
            throw new IllegalArgumentException("Cannot readMessage from empty collection of clients");
        }

        //TODO: This will create a new thread pool every call. We should be able to cache this.
        Executor executor = Executors.newFixedThreadPool(clients.size());

        ExecutorCompletionService<Message> ecs = new ExecutorCompletionService<>(executor);
        for(NetworkClient client : clients) {
            ecs.submit(Networking.readMessageAsync(client));
        }

        return ecs;
    }

    /* Return list of players which acknowledged message */
    public static List<Integer> readAcknowledgementsForMessageFromPlayers(GameRouter router, Message message, Collection<NetworkClient> players) {
        List<Integer> result = new LinkedList<>();
        if(players.size() == 0) {
            return result;
        }
        ExecutorCompletionService<Message> ecs = Networking.readMessageFromConnections(players);

        // TODO Timeout for ecs.take()

        for(NetworkClient client : players) {
            try {
                Future<Message> ack = ecs.take();

                System.out.println("Message from " + client.playerid);

                if(processAcknowledgement(message, ack)) {
                    result.add(client.playerid);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException("Timeout?");
            }
        }

        return result;
    }

    protected static boolean processAcknowledgement(Message message, Future<Message> futureAck) throws InterruptedException {
        Message ack;
        try {
            ack = futureAck.get();
        } catch (ExecutionException e) {
            Throwable e2 = e.getCause();

            if(e2 instanceof ConnectionLostException
                    || e2 instanceof TimeoutException
                    || e2 instanceof ParserException) {
                throw new RuntimeException("Unable to receive acknowledgement: " + e2.getClass().toString() + e2.getMessage());
            }
            // TODO tidy up logging
            e.printStackTrace();
            e2.printStackTrace();
            System.out.println("Unable to receive acknowledgement: " + e.getClass().toString() + e.getMessage());
            return false;
        }

        System.out.println("\tMessage::playerid " + ack.playerid);

        if(ack.command != Command.ACKNOWLEDGEMENT) {
            throw new RuntimeException("invalid message received: " + ack.command);
        }

        if(!(ack.payload instanceof IntegerPayload)) {
            throw new RuntimeException("invalid message");
        }

        IntegerPayload payload = (IntegerPayload)ack.payload;
        if(payload.value != message.ackId) {
            throw new RuntimeException("Bad acknowledgement ack_id. Expected " + message.ackId + ". Received: " + payload.value);
        }

        // Otherwise ALL OK.
        return true;
    }
}
