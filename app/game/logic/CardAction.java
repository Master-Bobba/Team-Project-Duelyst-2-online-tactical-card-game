package game.logic;

import structures.basic.Card;
import structures.basic.Tile;
import structures.basic.Unit;

/**
 * This class is used to rank the AI player's cards and also stores a reference to the original card
 *
 */

public class CardAction extends AttackAction{
	private Card card;

	public CardAction(Unit unit, Tile tile) {
		super(unit, tile);
	}
	
	public CardAction(Card card) {
		this.card = card;
	}
	
	/**
	 * Player Card associated with the card action
	 * @return card
	 */
	public Card getCard()  {
		return this.card;
	}
}
