package lobby;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import networking.Message;


public class Connection implements IConnection {
	//================================================================================
    // Properties
    //================================================================================
	
	private int port	= DEFAULT_LISTENING_PORT;
	private int timeout	= DEFAULT_TIMEOUT;
	
	
	//================================================================================
    // Functions
    //================================================================================

	@Override
	public int sendMessage(Message message) throws ConnectionLostException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Message recieveMessage() throws ConnectionLostException,
			TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void kill() {
		// TODO Auto-generated method stub
		
	}
	
	
	//================================================================================
    // Accessors
    //================================================================================
	
	@Override
	public void setTimeout(int milliseconds) {
		this.timeout = milliseconds;
	}

	@Override
	public int getTimeout() {
		return this.timeout;
	}

	@Override
	public int getPort() {
		return port;
	}

	@Override
	public void setPort(int port) {
		this.port = port;
	}
}
