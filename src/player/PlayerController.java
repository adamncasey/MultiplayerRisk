package player;

import logic.*;

import java.util.ArrayList;

public interface PlayerController {

    public void setUID(int uid);

    public void updateAI(ArrayList<Card> hand, Board board, int currentPlayer, Move previousMove);

    public Move getMove(Move move);

}
