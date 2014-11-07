package lobby;

/**
 * IConnection: Low level interface for connection to network entities.
 * @author James
 * 
 * Possible nice additions:
 * -> Error recovery.
 * -> Reconnection functionality.
 *  
 */
public interface IConnection {
	
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
	public void send(String message) throws ConnectionLostException;
	
	/**
	 * Receives a message.
	 * @return Received message.
	 * @throws ConnectionLostException If the connection is lost.
	 * @throws TimeoutException If response times-out.
	 */
	public String receive() throws ConnectionLostException, TimeoutException;
	
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
	 * Returns the remote port number to which this socket is connected.
	 * @return Port number
	 */
	public int getPort();
	
	/**
	 * Cleanly kills the connection.
	 */
	public void kill();
}
