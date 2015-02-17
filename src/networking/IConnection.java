package networking;


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
     *
	 * @param message The message payload. Only the exact string is sent
	 * @return Response code.
	 * @throws ConnectionLostException If the connection is lost.
	 */
	public void sendBlocking(String message) throws ConnectionLostException;
	
	/**
	 * Receives a message.
	 * @return Received message.
	 * @throws ConnectionLostException If the connection is lost.
	 * @throws TimeoutException If response times-out.
	 */
	public String receiveLine() throws ConnectionLostException, TimeoutException;

    /**
     * Can be used to receive a message from the socket in an asynchronous manner.
     * @return FutureTask which can be executed to perform receiveLine
     *
     * Calling get() may throw an exception, which when calling getCause(), may be of type:
     *      - ConnectionLostException
     *      - TimeoutException
     */
    //TODO re-evaluate the need for this
    //public FutureTask<String> receiveLine();
	
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
