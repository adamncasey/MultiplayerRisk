package networking;

import java.util.List;

import logic.Board;
import logic.Card;
import logic.Move;
import networking.message.Message;
import player.IPlayer;

public class NetworkPlayer implements IPlayer {
    final NetworkClient client;

    public NetworkPlayer(NetworkClient client) {
        this.client = client;
    }

	@Override
	public boolean isEliminated() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void eliminate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nextMove(int currentPlayer, String currentMove) {
		// TODO Auto-generated method stub
		
	}

    @Override
    public void updatePlayer(Board board, List<Card> hand, int currentPlayer, Move previousMove) {
        if(currentPlayer != client.playerid) {
            // We don't want to broadcast an update if this isn't the local player.
            return;
        }

        // convert Move into Message
        Message msg = gameMoveToNetworkMessage(previousMove);

        // Send Message
        try {
            client.sendMessage(msg);
        } catch (ConnectionLostException e) {
            // TODO Better way to handle disconnection.
            throw new RuntimeException("Lost connection. No way of handling this right now");
        }

    }

	@Override
	public Move getMove(Move move) {
		// Read a message from the network
		
		// Apply message information to the move object
		
		// return move
		return null;
	}

    private Message gameMoveToNetworkMessage(Move move) {
        switch(move.getStage()) {

        }

        throw new RuntimeException("Not implemented");
    }
}
