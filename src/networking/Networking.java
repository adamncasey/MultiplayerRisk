package networking;

import networking.message.payload.JoinGamePayload;
import networking.message.Message;
import networking.parser.Parser;
import networking.parser.ParserException;

import java.util.Collection;
import java.util.concurrent.*;

// Interface used by GameManager / NetworkPlayer?
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
	 * 
	 * @param
	 * @return
	 */
	public static LobbyServer joinLobby(String ipAddress, int port) {
		// Connect to IP address:port (TCP)

		// Send Join Message

		// Receive Accept (or reject) message
		return null;
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
	protected static Message readMessage(IConnection conn) throws ParserException,
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
}
