package networking;

import logic.move.Move;
import logic.move.MoveChecker;
import logic.state.Board;
import logic.state.Player;
import networking.message.Message;
import networking.parser.ParserException;
import player.IPlayer;


public class NetworkPlayer implements IPlayer {
    final NetworkClient client;

    public NetworkPlayer(NetworkClient client) {
        this.client = client;
    }

    @Override
    public void updatePlayer(Move previousMove) {
        if(previousMove.getUID() != client.playerid) {
            // We don't want to broadcast an update if this isn't the local player.
            return;
        }

        // convert Move into Message
        Message msg = gameMoveToNetworkMessage(previousMove);

        // Send Message
        client.router.sendToAllPlayers(msg);
    }

	@Override
	public void getMove(Move move) {
		// Read a message from the network
        Message msg;
        try {
            msg = client.readMessage();
        } catch (TimeoutException e) {
            e.printStackTrace();
            throw new RuntimeException("TimeoutException unhandled");
        } catch (ConnectionLostException e) {
            e.printStackTrace();
            throw new RuntimeException("ConnectionLostException unhandled");
        } catch (ParserException e) {
            e.printStackTrace();
            throw new RuntimeException("ParserException unhandled");
        }

        // Apply message information to the move object
        applyMessageAsMove(msg, move);
	}

    @Override
    public void setup(Player player, Board board, MoveChecker checker) {

    }

    @Override
    public void nextMove(String currentMove) {

    }

    private Message gameMoveToNetworkMessage(Move move) {
        switch(move.getStage()) {

            case CLAIM_TERRITORY:
                break;
            case REINFORCE_TERRITORY:
                break;
            case TRADE_IN_CARDS:
                break;
            case PLACE_ARMIES:
                break;
            case DECIDE_ATTACK:
                break;
            case START_ATTACK:
                break;
            case CHOOSE_ATTACK_DICE:
                break;
            case CHOOSE_DEFEND_DICE:
                break;
            case OCCUPY_TERRITORY:
                break;
            case DECIDE_FORTIFY:
                break;
            case START_FORTIFY:
                break;
            case FORTIFY_TERRITORY:
                break;
        }

        throw new RuntimeException("Not implemented");
    }



    private void applyMessageAsMove(Message msg, Move move) {
        // Change move object

        // Validate move object

        // Acknowledge / disconnect
    }
}
