package networking;

import networking.parser.ParserException;

public enum Command {
	JOIN_GAME("join_game"),
	JOIN_ACCEPT("accept_join_game"),
	JOIN_REJECT("reject_join_game"),
	PLAYERS_JOINED("players_joined"),
	PING("ping"),
	READY("ready"),
	INITIALISE_GAME("initialise_game"),
	SETUP("setup"),
	PLAY_CARDS("play_cards"),
	DRAW_CARD("draw_card"),
	DEPLOY("deploy"),
	ATTACK("attack"),
	DEFEND("defend"),
	ATTACK_CAPTURE("attack_capture"),
	FORTIFY("fortify"),
	ACKNOWLEDGEMENT("acknowledgement"),
    DICE_ROLL("roll"),
	DICE_HASH("roll_hash"),
	DICE_ROLL_NUM("roll_number"),
	TIMEOUT("timeout"),
	LEAVE_GAME("leave_game");
	
	
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
