package networking.networkplayer;

import logic.Card;
import logic.move.Move;
import logic.move.MoveChecker;
import logic.state.Board;
import logic.state.Player;
import networking.*;
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

    // Used to store a message which is referred to in multiple Move Stages.
    private Message unprocessedMessage;

    public NetworkPlayer(NetworkClient client, int localPlayerID, boolean broadcastLocalPlayer) {
        this.client = client;
        this.localPlayerID = localPlayerID;

        delegatedLocalBroadcast = broadcastLocalPlayer;

        players = client.router.getAllPlayers();
        unprocessedMessage = null;
    }

    @Override
    public void updatePlayer(Move previousMove) {
        if(!delegatedLocalBroadcast || previousMove.getUID() != localPlayerID) {
            // We don't want to broadcast an update if this isn't the local player.
            return;
        }

        // convert Move into Message
        MoveProcessResult result = gameMoveToNetworkMessage(previousMove);
        if(result.moreWorkNeeded || !result.responseNeeded) {
            return;
        }

        Message msg = result.message;

        assert msg != null;

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
        if(unprocessedMessage == null) {
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
        } else {
            msg = unprocessedMessage;
            unprocessedMessage = null;
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

        // If we finished processing that message and the message had an ackId:
        if(unprocessedMessage == null && msg.ackId != null) {
            response = Acknowledgement.acknowledgeMessage(msg, localPlayerID);

            client.router.sendToAllPlayers(response);
            System.out.println("Sent acknowledgement");

            // receive acknowledgements from all players but us and the person who sent the message.
            List<Integer> responses = readAcknowledgementsIgnorePlayerid(msg, msg.playerid);
            System.out.println("Received acknowledgement from " + responses.size() + "players");
        }
	}

    @Override
    public void setup(Player player, List<String> names, Board board, MoveChecker checker) {
        this.moveChecker = checker;
    }

    @Override
    public void nextMove(String currentMove) {

    }

    // State which is required between calls to getMove

    // TODO This is a bit of a hack.. I don't like the weird state going on here
    ArrayList<int[]> partialDeployment = new ArrayList<>();
    int storedSourceTerritory = -1;
    int storedDestinationTerritory = -1;

    /* Maps between game Move events and protocol Message */
    private MoveProcessResult gameMoveToNetworkMessage(Move move)
    {
        switch(move.getStage()) {
            case CLAIM_TERRITORY:
            case REINFORCE_TERRITORY:
                return new MoveProcessResult(new Message(Command.SETUP, move.getUID(), new IntegerPayload(move.getTerritory()), true));

            case TRADE_IN_CARDS: {
                List<Card> cardSet = move.getToTradeIn();
                Payload payload;
                if (cardSet.size() > 0) {
                    int numSets = 1; // TODO Handle multiple sets when logic supports that

                    int[][] setsTradedIn = new int[numSets][];
                    for (int i = 0; i < numSets; i++) {

                        setsTradedIn[i] = new int[cardSet.size()];
                        for (int j = 0; j < cardSet.size(); j++) {
                            setsTradedIn[i][j] = cardSet.get(j).getID();
                        }
                    }
                    payload = new PlayCardsPayload(setsTradedIn);
                } else {
                    payload = null;
                }

                return new MoveProcessResult(new Message(Command.PLAY_CARDS, move.getUID(), payload, true));
            }
            case PLACE_ARMIES: {
                // We need to save up these message until getExtraArmies == 0

                // Add this moves deployment to persistent list
                partialDeployment.add(new int[]{move.getTerritory(), move.getArmies()});

                if (move.getExtraArmies() == 0) {
                    // Create DeployPayload
                    int[][] deployments = partialDeployment.toArray(new int[partialDeployment.size()][]);
                    partialDeployment.clear();

                    return new MoveProcessResult(new Message(Command.DEPLOY, move.getUID(), new DeployPayload(deployments), true));
                }

                return MoveProcessResult.MORE_WORK_NEEDED;
            }
            case DECIDE_ATTACK: {
                // If no, send null ATTACK command
                if (!move.getDecision()) {
                    return new MoveProcessResult(new Message(Command.ATTACK, move.getUID(), null));
                }
                return MoveProcessResult.MORE_WORK_NEEDED;
            }
            case START_ATTACK:
            case START_FORTIFY: {
                // If these are not -1, there's a programming bug in my code, or the logic called START_ATTACK twice without calling CHOOSE_ATTACK_DICE.
                assert storedDestinationTerritory == -1;
                assert storedSourceTerritory == -1;

                storedSourceTerritory = move.getFrom();
                storedDestinationTerritory = move.getTo();

                return MoveProcessResult.MORE_WORK_NEEDED;
            }
            case CHOOSE_ATTACK_DICE: {
                // Send Command.ATTACK with from/to and numArmies.
                int numArmies = move.getAttackDice();

                ArmyMovementPayload payload = new ArmyMovementPayload(storedSourceTerritory, storedDestinationTerritory, numArmies);
                storedSourceTerritory = storedDestinationTerritory = -1;

                return new MoveProcessResult(new Message(Command.ATTACK, move.getUID(), payload, true));
            }
            case CHOOSE_DEFEND_DICE: {
                // Send Command.DEFEND with numArmies
                int numArmies = move.getDefendDice();

                return new MoveProcessResult(new Message(Command.DEFEND, move.getUID(), new IntegerPayload(numArmies), true));
            }

            case OCCUPY_TERRITORY: {
                // Send Command.ATTACK_CAPTURE with numArmies
                int numArmies = move.getArmies();

                return new MoveProcessResult(new Message(Command.ATTACK_CAPTURE, move.getUID(), new IntegerPayload(numArmies), true));
            }

            case DECIDE_FORTIFY: {
                // If no, send null Command.FORTIFY.
                if (!move.getDecision()) {
                    return new MoveProcessResult(new Message(Command.FORTIFY, move.getUID(), null));
                }
                return MoveProcessResult.MORE_WORK_NEEDED;
            }
            // START_FORTIFY handled in same place as START_ATTACK.
            case FORTIFY_TERRITORY: {
                // Send Command.FORTIFY with from/to and numArmies.
                int numArmies = move.getArmies();

                ArmyMovementPayload payload = new ArmyMovementPayload(storedSourceTerritory, storedDestinationTerritory, numArmies);
                storedSourceTerritory = storedDestinationTerritory = -1;

                return new MoveProcessResult(new Message(Command.FORTIFY, move.getUID(), payload, true));
            }

            case END_ATTACK:
            case PLAYER_ELIMINATED:
            case CARD_DRAWN:
            case SETUP_BEGIN:
            case SETUP_END:
            case GAME_BEGIN:
            case GAME_END:
                return MoveProcessResult.NO_RESPONSE_NEEDED;
            default:
                throw new RuntimeException("Unknown move stage: " + move.getStage().name());
        }
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
            case PLAY_CARDS: {
                // No cards traded in
                if (msg.payload == null) {
                    move.setToTradeIn(new ArrayList<>());
                    break;
                }

                PlayCardsPayload cards = (PlayCardsPayload) msg.payload;
                // TODO handle multiple card sets played.
                // TODO Get Card Object from Player.getHand()
                if (cards.cardSetsPlayed.length > 0) {
                    Integer[] set = ArrayUtils.toObject(cards.cardSetsPlayed[0]);
                    // Need a way to get Card objects from IDs
                    //move.setToTradeIn(Arrays.asList(set));
                }
                break;
            }
            case DEPLOY: {
                DeployPayload payload = (DeployPayload) msg.payload;

                // TODO Check that move.getExtraArmies() is the same as the total number of armies we still want to deploy.
                // Else leave_game something's broken.

                // Get first deployment.
                if (payload.deployments.length < 1) {
                    // TODO Error reporting.
                    throw new RuntimeException("Message received should have at least one deployment.");
                }
                int[] deployment = payload.deployments[0];

                move.setTerritory(deployment[0]);
                move.setArmies(deployment[1]);

                if (move.getExtraArmies() != 0) {
                    // Create new Message with reduced Payload (remove the processed deployment from the message)
                    // TODO This is a hack. Not only is there some slightly gross state in here, but it's a hack on top of a hack.

                    int newLength = payload.deployments.length - 1;

                    int[][] reducedDeployments = new int[newLength][];
                    System.arraycopy(payload.deployments, 1, reducedDeployments, 0, newLength);

                    Message reducedMessage = new Message(msg.command, msg.playerid, new DeployPayload(reducedDeployments), msg.ackId);

                    // More work to be done using this message..
                    unprocessedMessage = reducedMessage;
                }

                break;
            }
            case ATTACK: {
                switch (move.getStage()) {
                    case DECIDE_ATTACK: {
                        // Did this player decide to attack?
                        move.setDecision(msg.payload != null);

                        // This message isn't entirely processed yet.
                        unprocessedMessage = msg;
                        break;
                    }
                    case START_ATTACK: {
                        // Apply the from and to parameters of the attack message we received in DECIDE_ATTACK
                        ArmyMovementPayload payload = (ArmyMovementPayload) msg.payload;

                        move.setFrom(payload.sourceTerritory);
                        move.setTo(payload.destinationTerritory);

                        // This message still isn't entirely processed yet.
                        unprocessedMessage = msg;
                        break;
                    }
                    case CHOOSE_ATTACK_DICE: {
                        // Apply the numArmies paramter of the attack message received in DECIDE_ATTACK
                        ArmyMovementPayload payload = (ArmyMovementPayload) msg.payload;
                        move.setAttackDice(payload.numArmies);

                        break;
                    }
                }
                break;
            }
            case DEFEND: {
                // Apply num armies parameter
                IntegerPayload payload = (IntegerPayload)msg.payload;

                move.setDefendDice(payload.value);
                break;
            }
            case ATTACK_CAPTURE: {
                // Apply num armies parameter
                IntegerPayload payload = (IntegerPayload)msg.payload;

                move.setArmies(payload.value);
                break;
            }

            // TODO This is identical code to case ATTACK:
            case FORTIFY:
                switch(move.getStage()) {
                    case DECIDE_FORTIFY: {
                        // Is this player fortifying?
                        move.setDecision(msg.payload != null);

                        // This message isn't entirely processed yet.
                        unprocessedMessage = msg;
                        break;
                    }
                    case START_FORTIFY: {
                        // Apply the from and to parameters of the fortify message received in DECIDE_FORTIFY
                        ArmyMovementPayload payload = (ArmyMovementPayload) msg.payload;

                        move.setFrom(payload.sourceTerritory);
                        move.setTo(payload.destinationTerritory);

                        // This message still isn't entirely processed yet.
                        unprocessedMessage = msg;
                        break;
                    }
                    case FORTIFY_TERRITORY: {
                        // Apply the num armies parameters of the fortify message we recived back in DECIDE_FORTIFY.
                        ArmyMovementPayload payload = (ArmyMovementPayload) msg.payload;

                        move.setArmies(payload.numArmies);
                        break;
                    }
                }

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
