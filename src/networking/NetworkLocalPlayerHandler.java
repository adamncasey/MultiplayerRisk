package networking;

import logic.Card;
import logic.move.Move;
import networking.message.Message;
import networking.message.payload.*;
import networking.networkplayer.MoveProcessResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkLocalPlayerHandler extends LocalPlayerHandler {

    public final GameRouter router;
    final static Logger logger = Logger.getLogger("NetworkLocalPlayerHandler");

    // TODO Some of these state could prevented.
    ArrayList<int[]> partialDeployment = new ArrayList<>();
    int storedSourceTerritory = -1;
    int storedDestinationTerritory = -1;

    // Instead of Main methods instantiating this, it should come from networking?
    public NetworkLocalPlayerHandler(GameRouter router) {
        this.router = router;
    }

    public void sendMove(Move move) {
        System.out.println("sendMove: " + move.getStage());

        // convert Move into Message
        MoveProcessResult result = gameMoveToNetworkMessage(move);
        if(result.moreWorkNeeded || !result.responseNeeded) {
            return;
        }

        Message msg = result.message;

        assert msg != null;

        // Send Message
        router.sendToAllPlayers(msg);

        if(msg.ackId == null) {
            // We don't need to receive acknowledgements from other players.
            return;
        }

        Set<NetworkClient> players = router.getAllPlayers();

        // Receive acknowledgements from all players but the playerid of the message
        logger.log(Level.FINE, "Waiting for acknowledgements from " + players.size() + "players");
        List<Integer> responses = Networking.readAcknowledgementsIgnorePlayerid(msg, msg.playerid, players);
        logger.log(Level.FINE, "Received acknowledgement from " + responses.size() + "players");
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
                boolean applied = false;
                for(int[] deployment : partialDeployment) {
                    if(deployment[0] == move.getTerritory()) {
                        deployment[1] += move.getArmies();
                        applied = true;
                        break;
                    }
                }
                if(!applied) {
                    partialDeployment.add(new int[]{move.getTerritory(), move.getArmies()});
                }

                int numArmiesLeft = move.getCurrentArmies() + move.getExtraArmies() - move.getArmies();
                logger.log(Level.FINE, "PLACE_ARMIES to Network Message " + numArmiesLeft + " unplaced armies");
                if (numArmiesLeft == 0) {
                    logger.log(Level.FINE, "Command.DEPLOY sending");
                    // Create DeployPayload
                    int[][] deployments = partialDeployment.toArray(new int[partialDeployment.size()][]);
                    partialDeployment.clear();

                    return new MoveProcessResult(new Message(Command.DEPLOY, move.getUID(), new DeployPayload(deployments), true));
                }

                return MoveProcessResult.MORE_WORK_NEEDED;
            }
            case DECIDE_ATTACK: {
                /*if(!move.getDecision()) {
                    // Send a null attack message.
                    return new MoveProcessResult(new Message(Command.ATTACK, move.getUID(), null, true));
                }*/
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
            case GAME_END: {
                // Send a leave_game

                // Kill Networking.
            }


            case CARD_DRAWN:
            case END_ATTACK:
            case PLAYER_ELIMINATED:
            case SETUP_BEGIN:
            case SETUP_END:
            case GAME_BEGIN:
                return MoveProcessResult.NO_RESPONSE_NEEDED;
            default:
                throw new RuntimeException("Unknown move stage: " + move.getStage().name());
        }
    }
}
