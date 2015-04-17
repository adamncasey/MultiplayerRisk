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
import player.IPlayer;

import java.util.*;


public class NetworkPlayer implements IPlayer {
    final NetworkClient client;
    // The Playerid whose moves this NetworkPlayer is delegated to broadcast.
    // Used to send the local player's moves over the network.
    // TODO Consider whether wrapping the local IPlayer in a "NetworkBroadcastPlayer" might be cleaner.
    final int localPlayerID;
    final boolean delegatedLocalBroadcast;
    
    final int remotePlayerID;
    
    MoveChecker moveChecker;
    Player player;
    Set<NetworkClient> players;

    private LocalPlayerHandler localPlayerHandler;

    // Used to store a message which is referred to in multiple Move Stages.
    private Message unprocessedMessage;


    // State which is required between calls from Game Logic.
    // TODO I don't like the weird state going on here
    ArrayList<int[]> partialDeployment = new ArrayList<>();
    int storedSourceTerritory = -1;
    int storedDestinationTerritory = -1;
    boolean receivedNullAttack = false;
    boolean receivedNullFortify = false;

    boolean eliminatationKnown = false;

    public NetworkPlayer(NetworkClient client, int remotePlayerID, int localPlayerID, boolean broadcastLocalPlayer) {
        this.client = client;
        this.localPlayerID = localPlayerID;

        delegatedLocalBroadcast = broadcastLocalPlayer;

        players = client.router.getAllPlayers();
        unprocessedMessage = null;
        
        this.remotePlayerID = remotePlayerID;
    }

    @Override
    public String getPlayerName() {
        return client.name;
    }

    public int getPlayerID() {
        return this.client.playerid;
    }

    @Override
    public void setup(Player player, Map<Integer, String> names, Board board, MoveChecker checker, LocalPlayerHandler localPlayerHandler) {
        this.moveChecker = checker;
        this.player = player;
    }

    @Override
    public void updatePlayer(Move previousMove) {
        // TODO Refactor. This is more complicated than it should be.
        MoveProcessResult result = null;

        // Handle this NetworkPlayer's updatePlayer.
        if(previousMove.getUID() == player.getUID()) {
            if(!eliminatationKnown && player.isEliminated()) {
                // send LEAVE_GAME with continue listening = true.
            }

            if(previousMove.getStage() == Move.Stage.DECIDE_ATTACK) {
                if(previousMove.getDecision() || receivedNullAttack) {
                    receivedNullAttack = false;
                    return;
                }

                // Receive ATTACK payload: null and acknowledge this.
                // TODO: Weird hack, might work.
                getMove(previousMove);

                return;
            }

            if(previousMove.getStage() == Move.Stage.DECIDE_FORTIFY) {
                System.out.println("Potentially getting DECIDE_FORTIFY message");
                if(previousMove.getDecision() || receivedNullFortify) {

                    System.out.println("Not getting DECIDE_FORTIFY because :" + previousMove.getDecision() + " " + receivedNullFortify);
                    receivedNullFortify = false;
                    return;
                }

                // Receive FORTIFY payload: null and acknowledge this.
                // TODO: Weird hack, might work.
                getMove(previousMove);
                System.out.println("Getting DECIDE_FORTIFY message");

                return;
            }

            return;
        }

        // Decide whether or not we want to perform a broadcast for the delegated player.
        if(result == null && (!delegatedLocalBroadcast || previousMove.getUID() != localPlayerID)) {
            // We don't want to broadcast an update if this isn't the local player.
            // TODO IMPORTANT: EXCEPT In the case of a defend message and we sent the attack command.
            // In that case, we need to send the roll command.
            return;
        }

        // convert Move into Message
        result = gameMoveToNetworkMessage(previousMove);
        if(result.moreWorkNeeded || !result.responseNeeded) {
            return;
        }

        Message msg = result.message;

        assert msg != null;

        // Send Message
        client.router.sendToAllPlayers(msg);

        if(msg.ackId == null) {
            // We don't need to receive acknowledgements from other players.
            return;
        }

        // Receive acknowledgements from all players but the playerid of the message
        System.out.println("Waiting for acknowledgements from " + players.size() + "players");
        List<Integer> responses = readAcknowledgementsIgnorePlayerid(msg, msg.playerid);
        System.out.println("Received acknowledgement from " + responses.size() + "players");
    }

	@Override
	public void getMove(Move move)  {
		// Read a message from the network
        Message msg;
        MessageProcessResult result;
        // Keep receiving messages until we've completed handling network for this move.
        do {
            if (unprocessedMessage == null) {
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
            result = networkMessageToGameMove(msg, move);
        } while(result != MessageProcessResult.COMPLETE);

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
    public void nextMove(String currentMove, int uid) {
    }

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

                int numArmiesLeft = move.getCurrentArmies() + move.getExtraArmies() - move.getArmies();
                System.out.println("PLACE_ARMIES to Network Message " + numArmiesLeft + ": " + move.getCurrentArmies() + " "+ move.getExtraArmies() + " " + move.getArmies());
                if (numArmiesLeft == 0) {
                    System.out.println("PLACE_ARMIES Message sending");
                    // Create DeployPayload
                    int[][] deployments = partialDeployment.toArray(new int[partialDeployment.size()][]);
                    partialDeployment.clear();

                    return new MoveProcessResult(new Message(Command.DEPLOY, move.getUID(), new DeployPayload(deployments), true));
                }

                return MoveProcessResult.MORE_WORK_NEEDED;
            }
            case DECIDE_ATTACK: {
                if(!move.getDecision()) {
                    // Send a null attack message.
                    return new MoveProcessResult(new Message(Command.ATTACK, move.getUID(), null, true));
                }
                return MoveProcessResult.NO_RESPONSE_NEEDED;
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
                int from = move.getFrom();
                int to = move.getTo();

                return new MoveProcessResult(new Message(Command.ATTACK_CAPTURE, move.getUID(), new ArmyMovementPayload(from, to, numArmies), true));
            }

            case DECIDE_FORTIFY: {
                // If no, send null Command.FORTIFY.
                if (!move.getDecision()) {
                    return new MoveProcessResult(new Message(Command.FORTIFY, move.getUID(), null, true));
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

            case ROLL_HASH: {
                // Send roll_hash message.
                return new MoveProcessResult(new Message(Command.DICE_HASH, move.getUID(), new StringPayload(move.getRollHash()), false));
            }

            case ROLL_NUMBER: {
                // Send roll_number message.
                return new MoveProcessResult(new Message(Command.DICE_ROLL_NUM, move.getUID(), new StringPayload(move.getRollNumber()), false));
            }

            case CARD_DRAWN:
            case END_ATTACK:
            case PLAYER_ELIMINATED:
            case SETUP_BEGIN:
            case SETUP_END:
            case GAME_BEGIN:
            case GAME_END:
                return MoveProcessResult.NO_RESPONSE_NEEDED;
            default:
                throw new RuntimeException("Unknown move stage: " + move.getStage().name());
        }
    }

    private MessageProcessResult networkMessageToGameMove(Message msg, Move move) {
        // Change move object
        switch(msg.command) {
            case SETUP: {
                int territoryID = ((IntegerPayload) msg.payload).value;
                move.setTerritory(territoryID);
                return MessageProcessResult.COMPLETE;
            }
            case PLAY_CARDS: {
                // No cards traded in
                if (msg.payload == null) {
                    move.setToTradeIn(new ArrayList<>());
                    return MessageProcessResult.COMPLETE;
                }

                PlayCardsPayload cards = (PlayCardsPayload) msg.payload;
                // TODO handle multiple card sets played.
                // TODO Get Card Object from Player.getHand()
                if (cards.cardSetsPlayed.length > 0) {
                    int[] set = cards.cardSetsPlayed[0];

                    List<Card> cardsInHand = player.getHand();

                    List<Card> tradingIn = getCards(cardsInHand, set);

                    if(tradingIn.size() != 3) {
                        // TODO Error Handling

                        throw new RuntimeException("NetworkPlayer tried to trade in cards which were not in their hand in our game state.");
                    }

                    // Need a way to get Card objects from IDs
                    move.setToTradeIn(tradingIn);
                }
                return MessageProcessResult.COMPLETE;
            }
            case DEPLOY: {
                DeployPayload payload = (DeployPayload) msg.payload;

                // TODO Check that move.getExtraArmies() is the same as the total number of armies we still want to deploy.
                // TODO Else leave_game something's broken.

                // Get first deployment.
                if (payload.deployments.length < 1) {
                    // TODO Error reporting.
                    throw new RuntimeException("Message received should have at least one deployment.");
                }
                int[] deployment = payload.deployments[0];

                move.setTerritory(deployment[0]);
                move.setArmies(deployment[1]);

                int numArmiesLeft = move.getCurrentArmies() + move.getExtraArmies() - deployment[1];
                if (numArmiesLeft > 0) {
                    // Create new Message with reduced Payload (remove the processed deployment from the message)
                    // TODO This is a hack. Not only is there some slightly gross state in here, but it's a hack on top of a hack.

                    int newLength = payload.deployments.length - 1;

                    int[][] reducedDeployments = new int[newLength][];
                    System.arraycopy(payload.deployments, 1, reducedDeployments, 0, newLength);

                    Message reducedMessage = new Message(msg.command, msg.playerid, new DeployPayload(reducedDeployments), msg.ackId);

                    // More work to be done using this message..
                    unprocessedMessage = reducedMessage;
                }

                // TODO move to LocalHandler
                // Reset Null Attack / Fortify at a random point in the game...
                receivedNullAttack = false;
                receivedNullFortify = false;

                return MessageProcessResult.COMPLETE;
            }
            case ATTACK: {
                switch (move.getStage()) {
                    case DECIDE_ATTACK: {
                        // Did this player decide to attack?
                        if(msg.payload != null) {
                            move.setDecision(true);
                            unprocessedMessage = msg;
                            return MessageProcessResult.COMPLETE;
                        }
                        // Otherwise ....
                        receivedNullAttack = true;

                        // This message isn't entirely processed yet. Will process the rest on the next getMove call
                        return MessageProcessResult.COMPLETE;
                    }
                    case START_ATTACK: {
                        // Apply the from and to parameters of the attack message we received in DECIDE_ATTACK
                        ArmyMovementPayload payload = (ArmyMovementPayload) msg.payload;

                        move.setFrom(payload.sourceTerritory);
                        move.setTo(payload.destinationTerritory);

                        // This message still isn't entirely processed yet. Will process the rest on the next getMove call
                        unprocessedMessage = msg;
                        return MessageProcessResult.COMPLETE;
                    }
                    case CHOOSE_ATTACK_DICE: {
                        // Apply the numArmies paramter of the attack message received in DECIDE_ATTACK
                        ArmyMovementPayload payload = (ArmyMovementPayload) msg.payload;
                        move.setAttackDice(payload.numArmies);

                        return MessageProcessResult.COMPLETE;
                    }
                    default: {
                        throw new RuntimeException("Received Command.ATTACK during unexpected game.Move stage: " + move.getStage().name() + " ignoring message.");
                    }
                }
            }
            case DEFEND: {
                // Apply num armies parameter
                IntegerPayload payload = (IntegerPayload)msg.payload;

                move.setDefendDice(payload.value);
                return MessageProcessResult.COMPLETE;
            }
            case ATTACK_CAPTURE: {
                // Apply num armies parameter
                ArmyMovementPayload payload = (ArmyMovementPayload)msg.payload;

                move.setArmies(payload.numArmies);

                // Also receive the draw_cards command at this point
                return MessageProcessResult.COMPLETE;
            }

            // TODO This is identical code to case ATTACK. Refactor?
            case FORTIFY: {
                switch (move.getStage()) {
                    case DECIDE_FORTIFY: {
                        // Is this player fortifying?
                        if(msg.payload != null) {
                            move.setDecision(true);
                            unprocessedMessage = msg;
                            return MessageProcessResult.COMPLETE;
                        }

                        // Otherwise ....
                        receivedNullFortify = true;
                        return MessageProcessResult.COMPLETE;
                    }
                    case START_FORTIFY: {
                        // Apply the from and to parameters of the fortify message received in DECIDE_FORTIFY
                        ArmyMovementPayload payload = (ArmyMovementPayload) msg.payload;

                        move.setFrom(payload.sourceTerritory);
                        move.setTo(payload.destinationTerritory);

                        // This message still isn't entirely processed yet.
                        unprocessedMessage = msg;
                        return MessageProcessResult.COMPLETE;
                    }
                    case FORTIFY_TERRITORY: {
                        // Apply the num armies parameters of the fortify message we recived back in DECIDE_FORTIFY.
                        ArmyMovementPayload payload = (ArmyMovementPayload) msg.payload;

                        move.setArmies(payload.numArmies);
                        return MessageProcessResult.COMPLETE;
                    }
                    default: {
                        throw new RuntimeException("Received Command.FORTIFY during unexpected logic.Move state. I'm not sure we're supposed to get here. : " + move.getStage().name());
                    }
                }
            }
            case DICE_HASH: {
                StringPayload payload = (StringPayload)msg.payload;
                move.setRollHash(payload.value);
                return MessageProcessResult.COMPLETE;
            }
            case DICE_ROLL_NUM: {
                StringPayload payload = (StringPayload)msg.payload;
                move.setRollNumber(payload.value);
                return MessageProcessResult.COMPLETE;
            }

            case LEAVE_GAME:
                // TODO Turn this player into a neutral player.
                throw new RuntimeException("Not implemented.");

            case JOIN_GAME:
            case JOIN_ACCEPT:
            case JOIN_REJECT:
            case PLAYERS_JOINED:
            case PING:
            case READY:
            case INITIALISE_GAME:
            case ACKNOWLEDGEMENT:
            case TIMEOUT:
                // We shouldn't be receiving these messages right now. Ignore them.
                return MessageProcessResult.IGNORE_MESSAGE;
            default:
                 throw new RuntimeException("Received unknown message command.");
        }
    }

    private List<Card> getCards(List<Card> cards, int[] setTradedIn) {
        LinkedList<Card> result = new LinkedList<>();

        for(Card card : cards) {
            for(int id : setTradedIn) {
                if(card.getID() == id) {
                    result.add(card);
                }
            }
        }

        return result;
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
    
    @Override
    public String toString() {
    	return getPlayerName() != null ? getPlayerName() :  "";
    }

	@Override
	public int getPlayerid() {
		return remotePlayerID;
	}
}
