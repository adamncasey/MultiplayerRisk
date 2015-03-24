package networking.message.payload;


/**
 * Created by Adam on 04/02/2015.
 */
public class PingPayload extends Payload {
    public final int numPlayers;

    public PingPayload(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    @Override
    public Object getJSONValue() {
        return numPlayers;
    }
}
