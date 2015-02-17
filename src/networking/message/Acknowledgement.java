package networking.message;

import networking.Command;

/**
 * Created by Adam on 10/02/2015.
 */
public class Acknowledgement {
    public static Message acknowlegeMessage(Message message, int responseCode, Payload data, int ourPlayerid,
                                            boolean acknowledgementRequired) {
        AcknowledgementPayload newPayload = new AcknowledgementPayload(message.ackId, responseCode, data);

        Message response = new Message(Command.ACKNOWLEDGEMENT, ourPlayerid, newPayload, acknowledgementRequired);

        return response;
    }
}
