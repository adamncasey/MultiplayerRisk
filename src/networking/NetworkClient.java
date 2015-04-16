package networking;

import networking.message.Message;
import networking.parser.ParserException;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Adam on 08/02/2015.
 */
public class NetworkClient {

    public final int playerid;
    public final String name;
    public final boolean hostPlayer;

    public final GameRouter router;
    private BlockingQueue<Object> messageQueue;

    public NetworkClient(GameRouter router, int playerid, String name, boolean isHost) {
        this.hostPlayer = isHost;
        this.router = router;
        this.playerid = playerid;
        this.name = name;

        this.messageQueue = new LinkedBlockingQueue<>();
    }

    public Message readMessage() throws TimeoutException, ConnectionLostException, ParserException {

        System.out.println("readMessage NetworkClient playerid: " + playerid);
        Object obj;
        try {
            // TODO: Timeout?
            obj = messageQueue.take();
        } catch (InterruptedException e) {
            TimeoutException ex = new TimeoutException();
            ex.addSuppressed(e);

            throw ex;
        }

        if(obj instanceof Message) {
            return (Message)obj;
        }

        if(obj instanceof TimeoutException) {
            throw (TimeoutException)obj;
        }

        if(obj instanceof ConnectionLostException) {
            throw (ConnectionLostException)obj;
        }

        if(obj instanceof ParserException) {
            throw (ParserException)obj;
        }

        throw new RuntimeException("Received invalid object in messageQueue: " + obj.getClass().toString() + " : " + obj.toString());
    }

    public void sendMessage(Message message) throws ConnectionLostException {
        throw new RuntimeException("Not implemented sending messages directly to specific NetworkClients");

    }

    protected void addMessageToQueue(Message msg) {

        // TODO Handle leave_game messages here?
        // leave_game would be sent to show that this player has left the game.
        // For leave game, we could put a ConnectionLostException in the message queue.

        addToQueue(msg);
    }

    protected void addExceptionToQueue(Exception ex) {
        addToQueue(ex);
    }

    private void addToQueue(Object obj) {
        messageQueue.add(obj);
        System.out.println("Added object to queue playerid: " + playerid);
    }
}
