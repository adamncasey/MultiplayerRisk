package lobby;

import logic.rng.Int256;
import logic.rng.RNG;
import networking.*;
import networking.message.Message;
import networking.message.payload.StringPayload;
import networking.parser.ParserException;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;

/**
 * Created by Adam on 08/04/2015.
 */
public class LobbyDiceRoll {
    public static List<Integer> rollDice(GameRouter router, int ourPlayerid, List<NetworkClient> otherPlayers, int numDice, int numFaces) throws DiceRollException {
        RNG rng = new RNG();

        // Send ROLL_HASH
        sendRollHash(router, ourPlayerid, rng.hash);

        // Receive all ROLL_HASHs
        Map<Integer, Int256> hashes = receiveRollHashes(otherPlayers);

        // Send ROLL_NUMBER
        sendRollNumber(router, ourPlayerid, rng.number);

        // RECEIVE all ROLL_NUMBERs
        Map<Integer, Int256> numbers = receiveRollNumbers(otherPlayers);
        if(hashes.size() != numbers.size()) {
            throw new DiceRollException("Number of roll hashes received does not match number of roll numbers");
        }

        for(Map.Entry<Integer, Int256> entry : numbers.entrySet()) {
            Int256 hash = hashes.get(entry.getKey());

            Int256 calculated = Int256.fromHash(entry.getValue());

            if(!hash.compare(calculated)) {
                throw new DiceRollException("Invalid Dice Hash received for player: " + entry.getKey() + ". ");
            }
        }

        List<Int256> rolls = new ArrayList<>(numbers.values());
        rolls.add(rng.number);

        List<Integer> results = RNG.getDiceRolls(rolls, numDice, numFaces);

        assert(results.size() > 0);

        return results;
    }

    private static Map<Integer, Int256> receiveRollNumbers(List<NetworkClient> otherPlayers) throws DiceRollException {
        return receiveRollMessage(Command.DICE_ROLL_NUM, otherPlayers);
    }

    private static Map<Integer, Int256> receiveRollHashes(List<NetworkClient> otherPlayers) throws DiceRollException {
        return receiveRollMessage(Command.DICE_HASH, otherPlayers);
    }

    private static Map<Integer, Int256> receiveRollMessage(Command command, List<NetworkClient> otherPlayers) throws DiceRollException {
        HashMap<Integer, Int256> result = new HashMap<>();

        ExecutorCompletionService<Message> ecs = Networking.readMessageFromConnections(otherPlayers);

        for(NetworkClient client : otherPlayers) {
            try {
                Future<Message> msg = ecs.take();

                processDiceMessage(msg, command, result);
            } catch (InterruptedException e) {
                throw new RuntimeException("Timeout?");
            }
        }

        return result;
    }

    private static void processDiceMessage(Future<Message> futureMsg, Command command, Map<Integer, Int256> result) throws InterruptedException, DiceRollException {
        Message msg;
        try {
            msg = futureMsg.get();
        } catch (ExecutionException e) {
            Throwable e2 = e.getCause();

            if(e2 instanceof ConnectionLostException
                    || e2 instanceof TimeoutException
                    || e2 instanceof ParserException) {
                throw new RuntimeException("Unable to receive ping message: " + e2.getClass().toString() + e2.getMessage());
            }
            // TODO tidy up logging
            e.printStackTrace();
            e2.printStackTrace();
            throw new DiceRollException("Unable to get dice roll from a player");
        }

        if(msg.command != command) {
            throw new DiceRollException("Invalid message received. Expecting: " + command.name() + ". Received: " + msg.command.name());
        }

        result.put(msg.playerid, Int256.fromString(((StringPayload)msg.payload).value));
    }


    public static void sendRollHash(GameRouter router, int ourPlayerid, Int256 hash) {
        sendRoll(Command.DICE_HASH, router, ourPlayerid, hash);
    }
    public static void sendRollNumber(GameRouter router, int ourPlayerid, Int256 hash) {
        sendRoll(Command.DICE_ROLL_NUM, router, ourPlayerid, hash);
    }
    public static void sendRoll(Command command, GameRouter router, int ourPlayerid, Int256 hash) {
        Message msg = new Message(command, ourPlayerid, new StringPayload(hash.string), false);

        router.sendToAllPlayers(msg);
    }

    public static class DiceRollException extends Exception {
        public DiceRollException(String msg) {
            super(msg);
        }
    }
}
