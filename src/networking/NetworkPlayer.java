package networking;

import java.util.ArrayList;
import java.util.List;

import logic.Board;
import logic.Card;
import logic.Move;
import player.IPlayer;
import player.PlayerController;

public class NetworkPlayer implements IPlayer {

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

    }

	@Override
	public Move getMove(Move move) {
		// Read a message from the network
		
		switch(move.getStage()) {
		case CARD_DRAWN:
			break;
		case CHOOSE_ATTACK_DICE:
			break;
		case CHOOSE_DEFEND_DICE:
			break;
		case CLAIM_TERRITORY:
			break;
		case DECIDE_ATTACK:
			break;
		case DECIDE_FORTIFY:
			break;
		case END_ATTACK:
			break;
		case FORTIFY_TERRITORY:
			break;
		case GAME_BEGIN:
			break;
		case GAME_END:
			break;
		case OCCUPY_TERRITORY:
			break;
		case PLACE_ARMIES:
			break;
		case PLAYER_ELIMINATED:
			break;
		case REINFORCE_TERRITORY:
			break;
		case SETUP_BEGIN:
			break;
		case SETUP_END:
			break;
		case START_ATTACK:
			break;
		case START_FORTIFY:
			break;
		case TRADE_IN_CARDS:
			break;
		default:
			break;
			
		}
		
		// Apply message information to the move object
		
		// return move
		return null;
	}
}
