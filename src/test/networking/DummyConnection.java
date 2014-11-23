package test.networking;

import networking.ConnectionLostException;
import networking.IConnection;
import networking.TimeoutException;

/**
 * Created by Adam on 23/11/2014.
 */
public class DummyConnection implements IConnection {
    boolean wasKilled;
    String lastSentMessage;

    String lineToReceive;

    int milliseconds;
    @Override
    public void send(String message) throws ConnectionLostException {
        if(wasKilled) {
            throw new ConnectionLostException();
        }
    }

    @Override
    public String receiveLine() throws ConnectionLostException, TimeoutException {
        if(wasKilled) {
            throw new ConnectionLostException();
        }
        if(lineToReceive == null) {
            throw new TimeoutException();
        }

        String line = lineToReceive;
        lineToReceive = null;
        return line;
    }

    @Override
    public void setTimeout(int milliseconds) {
        this.milliseconds = milliseconds;
    }

    @Override
    public int getTimeout() {
        return this.milliseconds;
    }

    @Override
    public int getPort() {
        return 0;
    }

    @Override
    public void kill() {
        wasKilled = true;
    }
}
