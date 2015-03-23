package player;

import logic.move.Move;
import logic.state.Board;
import logic.state.Player;

public interface PlayerController {
    public void setup(Player player, Board board);
    public void getMove(Move move);
}
