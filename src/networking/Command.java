package networking;

import networking.parser.ParserException;

public enum Command {
	JOIN("join"),
	JOIN_ACCEPT("accept_join_game"),
	ACKNOWLEDGEMENT("acknowledgement"),
	TRADE_IN_CARDS("trade_in_cards"),
	DEPLOY("deploy"),
	ATTACK("attack"),
	CAPTURE("attack_capture"),
	FORTIFY("fortify"),
	DICE_ROLL("roll"),
	DICE_HASH("roll_hash"),
	DICE_ROLL_NUM("roll_number");
	
	
	Command(String name) {
		this.name = name;
	}
	
	public final String name;

	public static Command parse(String string) throws ParserException {
		for(Command cmd : Command.values()) {
			if(cmd.name.equals(string)) {
				return cmd;
			}
		}
		
		throw new ParserException("Cannot parse " + string + " to enum Command");
	}
}
