package networking.message.payload;


/**
 * Parent class of all Payload Types.
 */
public abstract class Payload {

    /**
     * Formats the Payload into a structure which is passed to the JSON Encoder.
     * If payload is a JSONObject, this should return a Map for example.
     * @return Map / List / String / Number / null
     */
    public abstract Object getJSONValue();
}
