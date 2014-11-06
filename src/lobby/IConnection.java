package lobby;
import networking.Message;

/**
 * IConnection: Low level interface for connection to network entities.
 * @author James
 */
public interface IConnection {
	
	/**
	 * Default listening port.
	 */
	public static final int DEFAULT_LISTENING_PORT = 1000;
	
	/**
	 * The default timeout for receipt of a message.
	 */
	public static final int DEFAULT_TIMEOUT = 1000;
	
	/**
	 * Sends a message. 
	 * @param message The message payload.
	 * @return Response code.
	 * @throws ConnectionLostException If the connection is lost.
	 */
	public int sendMessage(Message message) throws ConnectionLostException;
	
	/**
	 * Receives a message.
	 * @return Received message.
	 * @throws ConnectionLostException If the connection is lost.
	 * @throws TimeoutException If response times-out.
	 */
	public Message recieveMessage() throws ConnectionLostException, TimeoutException;
	
	/**
	 * Sets the message timeout.
	 * @param milliseconds Length of the timeout in milliseconds.
	 */
	public void setTimeout(int milliseconds);
	
	/**
	 * Gets the message timeout.
	 * @return Timeout length in milliseconds.
	 */
	public int getTimeout();
	
	/**
	 * Sets the listening port number.
	 * @param port Port number
	 */
	public void setPort(int port);
	
	/**
	 * Sets the listening port number.
	 * @return Port number
	 */
	public int getPort();
	
	/**
	 * Cleanly kills the connection.
	 */
	public void kill();
}
