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
import java.util.logging.Level;
import java.util.logging.Logger;


public class NetworkPlayer implements IPlayer {
    final NetworkClient client;
    final int localPlayerID;  // Local Player ID required to send error messages
    final int remotePlayerID;
    
    MoveChecker moveChecker;
    Player player;

    // Used to store a message which is referred to in multiple Move Stages.
    private Message unprocessedMessage;

    private Logger logger;

    // State which is required between calls from Game Logic.
    boolean receivedNullFortify = false;

    public NetworkPlayer(NetworkClient client, int remotePlayerID, int localPlayerID) {
        this.client = client;
        this.localPlayerID = localPlayerID;

        unprocessedMessage = null;
        
        this.remotePlayerID = remotePlayerID;

        logger = Logger.getLogger("NetworkPlayer ("+client.getName()+")");
    }

    @Override
    public String getPlayerName() {
        return client.getName();
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
        // Handle this NetworkPlayer's updatePlayer.
        if(previousMove.getUID() == player.getUID()) {

            if(previousMove.getStage() == Move.Stage.DECIDE_FORTIFY) {
                logger.log(Level.FINE, "Potentially getting DECIDE_FORTIFY message");
                if(previousMove.getDecision() || receivedNullFortify) {

                    logger.log(Level.FINE, "Not getting DECIDE_FORTIFY because :" + previousMove.getDecision() + " " + receivedNullFortify);
                    receivedNullFortify = false;
                    return;
                }

                // Receive FORTIFY payload: null and acknowledge this.
                getMove(previousMove);
                logger.log(Level.FINE, "Getting DECIDE_FORTIFY message");

                return;
            }
        }
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

            // receive acknowledgements from all players but us and the person who sent the message.
            Networking.readAcknowledgementsIgnorePlayerid(msg, msg.playerid, client.router.getAllPlayers());
        }
	}

    @Override
    public void nextMove(String currentMove, int uid) {
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
	                case DECIDE_ATTACK: {
	                	move.setDecision(false);
	                	unprocessedMessage = msg;
	                	return MessageProcessResult.COMPLETE;
	                }
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
                    break;
                }
            }
        }

        return result;
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
