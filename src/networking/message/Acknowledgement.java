package networking.message;

import networking.Command;
import networking.message.payload.IntegerPayload;

/**
 * Created by Adam on 10/02/2015.
 */
public class Acknowledgement {
    public static Message acknowledgeMessage(Message message, int ourPlayerid) {
        IntegerPayload newPayload = new IntegerPayload(message.ackId);

        Message response = new Message(Command.ACKNOWLEDGEMENT, ourPlayerid, newPayload, false);

        return response;
    }
}
