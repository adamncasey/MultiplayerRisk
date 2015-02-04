package networking;

import networking.message.Message;

import java.util.List;
import java.util.concurrent.ExecutorCompletionService;

/**
 * Class represents the entire network of computers making up the game.
 * This class must route all communications for the game (beginning mid handshake)
 *
 * Can be used to abstract over some of the sending / receiving details:
 *     * Verifying Signatures
 *     * Routing of messages through other players
 *
 * The implementation of this class will likely change as the protocol progresses
 */
public class GameNetwork {
    List<GameClient> clients;

    public GameNetwork(List<LobbyClient> lobbyClients) {

    }

    // sendToAllPlayers
    public void sendToAllPlayers(Message message) {

    }

    // receiveFromPlayer
    public Message receiveFromPlayerBlocking(int playerid) {
        throw new UnsupportedOperationException("Not implemented");
    }

    // receiveFromMultiple
    public ExecutorCompletionService<Message> receiveFromMultiplePlayers(List<GameClient> players) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
