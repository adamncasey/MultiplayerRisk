package networking;

import settings.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * Concrete implementation of IConnection
 * 
 * @see IConnection
 * @author James
 *
 */
public class Connection implements IConnection {

	// ================================================================================
	// Properties
	// ================================================================================

	private int timeout = DEFAULT_TIMEOUT;

	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;

	// ================================================================================
	// Constructors
	// ================================================================================

	public Connection(Socket socket) {
		this.socket = socket;

		//TODO The error message is currently ignored, I don't know how important this is
		establishConnection();
	}

	// ================================================================================
	// Functions
	// ================================================================================
	@Override
	public void sendBlocking(String message) throws ConnectionLostException {
		out.write(message);

		if (out.checkError())
			throw new ConnectionLostException();

		if(Settings.PRINT_NETWORK_TRAFFIC) {
			System.out.println("Sent: " + message);
		}
	}

	@Override
	public String receiveLine() throws ConnectionLostException, TimeoutException {
		try {
			String message = in.readLine();

			if(Settings.PRINT_NETWORK_TRAFFIC) {
				System.out.println("Received: " + message);
			}

			return message;
		} catch(SocketTimeoutException e1) {
			throw new TimeoutException();
		} catch (IOException e2) {
			throw new ConnectionLostException();
		}
	}

    @Override
	public void kill() {
		out.close();

		try {
			in.close();
		} catch (IOException e) {
		}

		try {
			socket.close();
		} catch (IOException e) {
		}
	}

	/**
	 * Establishes the connection through the socket, setting up input and
	 * output streams.
	 * 
	 * @return True if successful connection established.
	 */
	protected boolean establishConnection() {
		boolean success = false;

		try {
			this.out = new PrintWriter(socket.getOutputStream(), true);
			this.in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			success = true;
		} catch (IOException e) {
			// TODO: Log exception ?
			e.printStackTrace();
		}

		return success;
	}

	// ================================================================================
	// Accessors
	// ================================================================================

	@Override
	public void setTimeout(int milliseconds) {
		this.timeout = milliseconds;
		try {
			socket.setSoTimeout(milliseconds);
		} catch (SocketException e) {
			// TODO: Log exception ?
			e.printStackTrace();
		}
	}

	@Override
	public int getTimeout() {
		int t;

		try {
			t = socket.getSoTimeout();
		} catch (SocketException e) {
			t = this.timeout;
		}

		return t;
	}

	@Override
	public int getPort() {
		return socket.getPort();
	}
}
