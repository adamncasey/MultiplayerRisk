package player;

import logic.Player;
import logic.Board;
import logic.Move;

public interface PlayerController {
    public void setup(Player player, Board board);
    public Move getMove(Move move);
}
