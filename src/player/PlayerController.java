package player;

import logic.Player;
import logic.Board;
import logic.Move;

public interface PlayerController {
    public void setup(Player player, Board board);
    public void getMove(Move move);
}
