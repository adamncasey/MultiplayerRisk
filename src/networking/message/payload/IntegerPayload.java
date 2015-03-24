package networking.message.payload;


public class IntegerPayload extends Payload {
    public final int value;

    public IntegerPayload(int value) {
        this.value = value;
    }

    @Override
    public Object getJSONValue() {
        return value;
    }
}
