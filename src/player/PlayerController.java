package player;

import logic.*;

import java.util.*;

public interface PlayerController {

    public void setUID(int uid);

    public void updateAI(List<Card> hand, Board board, int currentPlayer, Move previousMove);

    public Move getMove(Move move);

}
