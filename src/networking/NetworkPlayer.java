package networking;

import com.sun.deploy.util.ArrayUtil;
import logic.Card;
import logic.move.Move;
import logic.move.MoveChecker;
import logic.state.Board;
import logic.state.Player;
import networking.message.Acknowledgement;
import networking.message.Message;
import networking.message.payload.*;
import networking.parser.ParserException;
import org.apache.commons.lang3.ArrayUtils;
import player.IPlayer;

import java.util.*;


public class NetworkPlayer implements IPlayer {
    final NetworkClient client;
    // The Playerid whose moves this NetworkPlayer is delegated to broadcast.
    // Used to send the local player's moves over the network.
    // TODO Consider whether wrapping the local IPlayer in a "NetworkBroadcastPlayer" might be cleaner.
    final int localPlayerID;
    final boolean delegatedLocalBroadcast;
    MoveChecker moveChecker;
    Set<NetworkClient> players;

    public NetworkPlayer(NetworkClient client, int localPlayerID, boolean broadcastLocalPlayer) {
        this.client = client;
        this.localPlayerID = localPlayerID;

        delegatedLocalBroadcast = broadcastLocalPlayer;

        players = client.router.getAllPlayers();
    }

    @Override
    public void updatePlayer(Move previousMove) {
        if(!delegatedLocalBroadcast || previousMove.getUID() != localPlayerID) {
            // We don't want to broadcast an update if this isn't the local player.
            return;
        }

        // convert Move into Message
        Message msg = gameMoveToNetworkMessage(previousMove);

        if(msg == null) {
            return;
        }

        // Send Message
        client.router.sendToAllPlayers(msg);

        // Receive acknowledgements from all players but broadcastPlayerID.


        NetworkClient removed = null;

        for(NetworkClient player : players) {
            if(client.playerid == localPlayerID) {
                players.remove(player);
                removed = player;
                break;
            }
        }

        System.out.println("Waiting for acknowledgements from " + players.size() + "players");

        List<Integer> responses = Networking.readAcknowledgementsForMessageFromPlayers(client.router, msg, players);
        System.out.println("Received acknowledgement from " + responses.size() + "players");

        if(removed != null)
            players.add(removed);
    }

	@Override
	public void getMove(Move move)  {
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

        if(!acceptedMove) {
            // We should send a leave_game and disconnect.
            response = new Message(Command.LEAVE_GAME, localPlayerID, new StringPayload("Received invalid move. Disconnecting"));

            client.router.sendToAllPlayers(response);
            // TODO Discuss how to handle this with Nathan
            throw new RuntimeException("NetworkPlayer sent move we consider invalid.");
        }
        response = Acknowledgement.acknowledgeMessage(msg, localPlayerID);

        client.router.sendToAllPlayers(response);
        System.out.println("Sent acknowledgement");

        // receive acknowledgements from all players but us and the person who sent the message.
        List<Integer> responses = readAcknowledgementsIgnorePlayerid(msg, msg.playerid);
        System.out.println("Received acknowledgement from " + responses.size() + "players");
	}

    @Override
    public void setup(Player player, List<String> names, Board board, MoveChecker checker) {
        this.moveChecker = checker;
    }

    @Override
    public void nextMove(String currentMove) {

    }

    /* Maps between game Move events and protocol Message */
    private Message gameMoveToNetworkMessage(Move move)
    {
        switch(move.getStage()) {
            case CLAIM_TERRITORY:
            case REINFORCE_TERRITORY:
                return new Message(Command.SETUP, move.getUID(), new IntegerPayload(move.getTerritory()), true);

            case TRADE_IN_CARDS:
                List<Card> cardSet = move.getToTradeIn();
                Payload payload;
                if(cardSet.size() > 0) {
                    int numSets = 1; // TODO Handle multiple sets when logic supports that

                    int[][] setsTradedIn = new int[numSets][];
                    for(int i=0; i<numSets; i++) {

                        setsTradedIn[i] = new int[cardSet.size()];
                        for(int j=0; j<cardSet.size(); j++) {
                            setsTradedIn[i][j] = cardSet.get(j).getID();
                        }
                    }
                    payload = new PlayCardsPayload(setsTradedIn);
                }
                else {
                    payload = null;
                }

                return new Message(Command.PLAY_CARDS, move.getUID(), payload, true);
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
            case END_ATTACK:
            case PLAYER_ELIMINATED:
            case CARD_DRAWN:
            case SETUP_BEGIN:
            case SETUP_END:
            case GAME_BEGIN:
            case GAME_END:
                return null;
            default:
                System.out.println("Unknown move stage: " + move.getStage().name());
                return null;
        }

        throw new RuntimeException("Not implemented");
    }

    private void networkMessageToGameMove(Message msg, Move move) {
        // Change move object
        switch(msg.command) {
            case SETUP:
                int territoryID = ((IntegerPayload)msg.payload).value;
                move.setTerritory(territoryID);
                break;
            case DRAW_CARD:
                break;
            case PLAY_CARDS:
                // No cards traded in
                if(msg.payload == null) {
                    move.setToTradeIn(new ArrayList<>());
                    break;
                }

                PlayCardsPayload cards = (PlayCardsPayload)msg.payload;
                // TODO handle multiple card sets played.
                // TODO Get Card Object from Player.getHand()
                if(cards.cardSetsPlayed.length > 0) {
                    Integer[] set = ArrayUtils.toObject(cards.cardSetsPlayed[0]);
                    // Need a way to get Card objects from IDs
                    //move.setToTradeIn(Arrays.asList(set));
                }
                break;
            case DEPLOY:
                DeployPayload payload = (DeployPayload)msg.payload;
                for(int[] deployment : payload.deployments) {
                    move.setTerritory(deployment[0]);
                    move.setArmies(deployment[1]);
                }
                break;
            case ATTACK:
                break;
            case DEFEND:
                break;
            case ATTACK_CAPTURE:
                break;
            case FORTIFY:
                break;

            case JOIN_GAME:
            case JOIN_ACCEPT:
            case JOIN_REJECT:
            case PING:
            case READY:
            case INITIALISE_GAME:
            case ACKNOWLEDGEMENT:
                break;
            case DICE_ROLL:
            case DICE_HASH:
            case DICE_ROLL_NUM:
                break;
            case KILL_GAME:
                // TODO End the game.
                break;
            case LEAVE_GAME:
                // TODO Turn this player into a neutral player.
                break;
            default:
                 throw new RuntimeException("Received unknown message command.");
        }
    }

    private List<Integer> readAcknowledgementsIgnorePlayerid(Message message, int ignoredPlayerID) {
        NetworkClient removed = null;

        for(NetworkClient player : players) {
            if(player.playerid == ignoredPlayerID) {
                players.remove(player);
                removed = player;
                break;
            }
        }

        System.out.println("Waiting for acknowledgements from " + players.size() + "players");

        List<Integer> responses = Networking.readAcknowledgementsForMessageFromPlayers(client.router, message, players);
        System.out.println("Received acknowledgement from " + responses.size() + "players");

        if(removed != null)
            players.add(removed);

        return responses;
    }
}
