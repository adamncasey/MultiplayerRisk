package ui.game.cards;

import java.util.List;
import logic.Card;

public interface CardTradeEventHandler {
	void onCardsPicked(List<Card> cards);
}
