package networking;

/**
 * The format network messages are parsed into/serialised from.
 */
public class Message {
	Command command;
	
	Object payload;
	
	String signature; // May not make it into final protocol
	
	String playerId;
}
