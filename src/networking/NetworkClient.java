package networking;

import networking.message.Message;
import networking.parser.ParserException;

/**
 * Created by Adam on 08/02/2015.
 */
public class NetworkClient {

    public final int playerid;
    public final GameRouter router;

    public NetworkClient(GameRouter router, int playerid) {
        this.router = router;
        this.playerid = playerid;
    }

    public Message readMessage() throws TimeoutException, ConnectionLostException, ParserException {
        throw new RuntimeException("Not implemented");
    }

    public void sendMessage(Message message) throws ConnectionLostException {
        throw new RuntimeException("Not implemented");

    }
}
