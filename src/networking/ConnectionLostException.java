package networking;

import java.io.IOException;

public class ConnectionLostException extends IOException {
	public ConnectionLostException(String msg) {
		super(msg);
	}
	
	public ConnectionLostException() {
		super();
	}
	
	private static final long serialVersionUID = -2837902819203315043L;

}
