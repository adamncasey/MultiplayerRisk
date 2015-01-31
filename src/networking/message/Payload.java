package networking.message;

import org.json.simple.JSONValue;

/**
 * Parent class of all Payload Types.
 */
public abstract class Payload {

    public abstract JSONValue getJSONValue();
}
