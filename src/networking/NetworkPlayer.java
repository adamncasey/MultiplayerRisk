package networking;

import logic.move.Move;
import logic.move.MoveChecker;
import logic.move.WrongMoveException;
import logic.state.Board;
import logic.state.Player;
import networking.message.Acknowledgement;
import networking.message.Message;
import networking.message.payload.IntegerPayload;
import networking.message.payload.StringPayload;
import networking.parser.ParserException;
import player.IPlayer;


public class NetworkPlayer implements IPlayer {
    final NetworkClient client;
    MoveChecker moveChecker;

    public NetworkPlayer(NetworkClient client) {
        this.client = client;
    }

    @Override
    public void updatePlayer(Move previousMove) throws WrongMoveException {
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
	public void getMove(Move move) throws WrongMoveException {
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
        networkMessageToGameMove(msg, move);

        // Validate move object
        boolean acceptedMove = moveChecker.checkMove(move);

        // Acknowledge / disconnect
        Message response;

        if(acceptedMove) {
            response = Acknowledgement.acknowledgeMessage(msg, 0, null, client.playerid, false);
        } else {
            // We should send a leave_game and disconnect.
            response = new Message(Command.LEAVE_GAME, client.playerid, new StringPayload("Received invalid move. Disconnecting"));
        }

        client.router.sendToAllPlayers(response);
	}

    @Override
    public void setup(Player player, Board board, MoveChecker checker) {
        this.moveChecker = checker;
    }

    @Override
    public void nextMove(String currentMove) {

    }

    /* Maps between game Move events and protocol Message */
    private Message gameMoveToNetworkMessage(Move move) throws WrongMoveException
    {
        switch(move.getStage()) {
            case CLAIM_TERRITORY:
                return new Message(Command.SETUP, client.playerid, new IntegerPayload(move.getTerritory()), true);
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

    private void networkMessageToGameMove(Message msg, Move move) throws WrongMoveException {
        // Change move object
        switch(msg.command) {

            case JOIN_GAME:
                break;
            case JOIN_ACCEPT:
                break;
            case JOIN_REJECT:
                break;
            case ACKNOWLEDGEMENT:
                break;
            case PLAY_CARDS:
                break;
            case DEPLOY:
                break;
            case ATTACK:
                break;
            case ATTACK_CAPTURE:
                break;
            case FORTIFY:
                break;
            case DICE_ROLL:
                break;
            case DICE_HASH:
                break;
            case DICE_ROLL_NUM:
                break;
            case PING:
                break;
            case READY:
                break;
            case KILL_GAME:
                break;
            case INITIALISE_GAME:
                break;
            case DRAW_CARD:
                break;
            case DEFEND:
                break;
            case SETUP:
                int territoryID = ((IntegerPayload)msg.payload).value;
                move.setTerritory(territoryID);
                break;
        }
    }
}
