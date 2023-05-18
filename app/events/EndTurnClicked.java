package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import game.logic.Gui;
import game.logic.Minimax;
import structures.GameState;
import structures.basic.Unit;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case
 * the end-turn button.
 * 
 * { 
 *   messageType = “endTurnClicked”
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class EndTurnClicked implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
				
		Gui.removeHighlightTiles(out, GameState.getBoard());
		// Current player draws card, and loses all their unspent mana
		gameState.getCurrentPlayer().drawCard();
		gameState.getCurrentPlayer().setMana(0);
		CardClicked.clearHighlighted();
		
		for (Unit unit : gameState.getOtherPlayer().getUnits()) {
			unit.clearAttacked();
			unit.clearMoved();
		}

		gameState.handOverControl(); // give control to the other player once go is complete.

		if(!GameState.previousAction.isEmpty()) {
			GameState.previousAction.pop(); // remove the previous action, effectively cancelling it.
		}
		
		/*
		 * Create the AI Thread and run it if it's the AI player's turn
		 */
		
		if (GameState.getCurrentPlayer() == GameState.getAIPlayer()) {
			
			Minimax minimax = new Minimax(out, gameState, message);
			Thread ai = new Thread(minimax);
			ai.start();	
		}
		

	}


}
