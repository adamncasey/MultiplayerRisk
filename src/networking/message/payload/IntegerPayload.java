package networking.message.payload;

/**
 * Created by Hattie on 23/03/2015.
 */
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
