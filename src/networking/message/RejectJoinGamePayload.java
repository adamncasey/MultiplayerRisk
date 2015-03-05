package networking.message;

public class RejectJoinGamePayload extends Payload {
    public final String message;

    public RejectJoinGamePayload(String message) {
        this.message = message;
    }

    @Override
    public Object getJSONValue() {
        return this.message;
    }
}
