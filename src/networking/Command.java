package networking;

public enum Command {
	JOIN("join"),
	ACKNOWLEDGEMENT("acknowledgement"),
	TRADE_IN_CARDS("trade_in_cards"),
	DEPLOY_ARMIES("deploy"),
	ATTACK("attack"),
	CAPTURE_REINFORCE("attack_capture"),
	FORTIFY("fortify"),
	DICE_ROLL("roll"),
	DICE_HASH("roll_hash"),
	DICE_ROLL_NUM("roll_number");
	
	
	Command(String name) {
		this.name = name;
	}
	
	public final String name;
}
