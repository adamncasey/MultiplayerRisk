package networking;

import networking.message.Message;
import networking.parser.ParserException;

/**
 * Created by Adam on 09/02/2015.
 */
public class ReadThread {
    private IConnection conn;
    private GameRouter router;

    private Thread thread;
    private boolean interrupted;

    public ReadThread(IConnection conn, GameRouter router) {
        this.conn = conn;
        this.router = router;
        this.thread = new Thread(readTask);
        this.interrupted = false;
    }

    public synchronized void start() {
        interrupted = false;
        thread.start();
    }

    public synchronized void stop() {
        interrupted = true;
    }

    private synchronized boolean interrupted() {
        return interrupted;
    }

    private Runnable readTask = new Runnable() {
        @Override
        public void run() {
        	// Remove the timeout for the read thread.
        	conn.setTimeout(0);
        	
            while(!interrupted()) {
                Message msg = null;
                try {
                    // read from thread
                    msg = Networking.readMessage(conn);

                } catch (TimeoutException e) {
                } catch (ParserException | ConnectionLostException e ) {
                    router.handleException(conn, e);
                    break;
                }

                if(msg != null) {
                    System.out.println("Received message from playerid " + msg.playerid);
                    router.handleMessage(conn, msg);
                }
                else
                {
                    System.out.println("null message");
                }
            }
        }
    };
}
