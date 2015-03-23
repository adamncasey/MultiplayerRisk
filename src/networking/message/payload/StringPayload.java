package networking.message.payload;


public class StringPayload extends Payload {
    String value;

    public StringPayload(String value) {
        this.value = value;
    }

    @Override
    public Object getJSONValue() {
        return value;
    }
}
