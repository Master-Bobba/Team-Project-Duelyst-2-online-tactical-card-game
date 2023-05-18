package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import game.logic.Gui;
import game.logic.Utility;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Playable;
import structures.basic.spellcards.SpellCard;
import structures.basic.spellcards.Sundrop;
import structures.basic.spellcards.Truestrike;

import structures.basic.Player;
import structures.basic.Tile; 

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
/**
 * Indicates that the user has clicked an object on the game canvas, in this case a card.
 * The event returns the position in the player's hand the card resides within.
 * 
 * { 
 *   messageType = “cardClicked”
 *   position = <hand index position [1-6]>
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class CardClicked implements EventProcessor{
	public static HashMap<Card, Integer> currentlyHighlighted = new HashMap<>();
	private HashMap<String, SpellCard> spellcards = new HashMap<String,SpellCard>();
	//private SpellCard currentSpellcard;
	private static int handPosition;

//	public CardClicked() {
//		initaliseSpellcards();
//	}

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		
		// to avoid events handling while the AI player is playing
		if (GameState.getCurrentPlayer().equals(GameState.getAIPlayer()) ) {
			return;
		}
		
		System.out.println("processEvent CardClicked");
		
		this.handPosition = message.get("position").asInt();
		
		//highlightCard(handPosition, gameState);
		Gui.removeHighlightTiles(out, GameState.getBoard());
		
		try{
			Card card = highlightCard(handPosition, gameState);
			checkCardType(out, card);
		}catch (Exception E){
			return;
		}
	}
	
	/**
	 * Checks if the selected card is a unit card or a spell card, and 
	 * redirects it to the appropriate method to be handled
	 * @param out
	 * @param card
	 */

	public void checkCardType(ActorRef out, Card card){
		System.out.println("checkCardType CardClicked");
		if (!(card instanceof SpellCard)) {
			System.out.println("UNIT CARD");
			checkValidity(out, card);
			
		} else {
			System.out.println("SPELL CARD");
			//Gui.removeHighlightTiles(out, GameState.getBoard());
			if (GameState.getCurrentPlayer().getMana() >= card.getManacost()) {
				
				
				
				SpellCard currentSpellcard = (SpellCard)GameState.getHumanPlayer().getCard(handPosition);
				
				currentSpellcard.highlightTargets(out);
				//GameState.setPreviousAction(currentSpellcard); // <=== STACK
				//}
			} else {
				if (GameState.getCurrentPlayer().equals(GameState.getHumanPlayer())) {
					BasicCommands.addPlayer1Notification(out, "Not enough mana!", 2);
				} 
			}
		}
	}
	/**
	 * Check if the selected card can be validly played
	 * i.e. does the player has enought mana to play the card.
	 * @param out
	 * @param card
	 */
	public void checkValidity(ActorRef out, Card card){
		System.out.println("checkValidity CardClicked");
		Player human = GameState.getHumanPlayer();
		Player current = GameState.getCurrentPlayer();
		
		if (human == current){
			if (human.getMana() >= card.getManacost()){
				Player enemy = GameState.enemy;
				Set<Tile> s = Utility.cardPlacements(card, human, enemy, GameState.getBoard());
				Gui.removeHighlightTiles(out, GameState.getBoard());
				Gui.highlightTiles(out, s, 1);
			} else {
				if (GameState.getCurrentPlayer().equals(GameState.getHumanPlayer())) {
					BasicCommands.addPlayer1Notification(out, "Not enough mana, boi", 2);
				}
			}
		}
	}
	/**
	 * highlight the selected card and push it to the stack
	 * @param handPosition
	 * @param gameState
	 * @return
	 */
	
	public Card highlightCard(int handPosition, GameState gameState) {
		System.out.println("HighlightCard cardclicked");
		clearHighlighted();
		
		// highlighted the selected card
		Card highlightedCard = gameState.getHumanPlayer().getCard(handPosition);
		highlightedCard.setPositionInHand(handPosition);
		pushToPreviousAction(highlightedCard); //<=== STACK
		
		Gui.highlightCard(highlightedCard, handPosition);
		currentlyHighlighted.put(highlightedCard, handPosition);
		return highlightedCard;
	}


	/**
	 * removes the highlight from any highligted cards, and
	 * removes the previously selected card from the stack of previous moves.
	 */
	public static void clearHighlighted(){
		System.out.println("clearHighLighted CardClicked");

		// Unhighlight currently highlighted cards

		if (!currentlyHighlighted.isEmpty()) {
			currentlyHighlighted.forEach((key, value) -> {
				Gui.displayCard(key, value);
			});
			currentlyHighlighted.clear();
		}
		
		
	}
	/**
	 * push the selected card on to the stack of previous action 
	 * for future reference.
	 * @param card
	 */

	public static void pushToPreviousAction(Card card){
		System.out.println("pushToPreviousAction CardClicked");
		GameState.setPreviousAction(card);
	}



	public static int getHandPosition() {
		return handPosition;
	}
}

