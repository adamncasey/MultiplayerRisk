package networking.message;

import networking.Command;
import networking.message.payload.AcknowledgementPayload;
import networking.message.payload.Payload;

/**
 * Created by Adam on 10/02/2015.
 */
public class Acknowledgement {
    public static Message acknowledgeMessage(Message message, int ourPlayerid) {
        AcknowledgementPayload newPayload = new AcknowledgementPayload(message.ackId);

        Message response = new Message(Command.ACKNOWLEDGEMENT, ourPlayerid, newPayload, false);

        return response;
    }
}
