package networking;

/**
 * The format network messages are parsed into/serialised from.
 */
public class Message {
	public Message(Command command, boolean signed, long playerId, Object payload) {
		this.command = command;
		this.payload = payload;
		this.signed = signed;
		this.playerId = playerId;
	}
	
	public final Command command;
	
	public final Object payload;
	
	public final boolean signed; // Not filled in at the moment.
	
	public final long playerId;
}
