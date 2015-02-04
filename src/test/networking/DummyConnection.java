package test.networking;

import networking.ConnectionLostException;
import networking.IConnection;
import networking.TimeoutException;

import java.util.concurrent.FutureTask;

/**
 * Created by Adam on 23/11/2014.
 */
public class DummyConnection implements IConnection {
    boolean wasKilled;
    String lastSentMessage;

    String lineToReceive;

    int milliseconds;
    @Override
    public void sendBlocking(String message) throws ConnectionLostException {
        if(wasKilled) {
            throw new ConnectionLostException();
        }
    }

    @Override
    public String receiveLineBlocking() throws ConnectionLostException, TimeoutException {
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
    public FutureTask<String> receiveLine() {
        throw new UnsupportedOperationException("Not implemented");
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
