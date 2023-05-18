package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import game.logic.Gui;
import structures.GameState;
import structures.basic.Card;

import java.util.HashMap;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case
 * somewhere that is not on a card tile or the end-turn button.
 * 
 * { 
 *   messageType = “otherClicked”
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class OtherClicked implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		
		// to avoid events handling while the AI player is playing
		if (GameState.getCurrentPlayer().equals(GameState.getAIPlayer()) ) {
			return;
		}

		CardClicked.clearHighlighted();
		Gui.removeHighlightTiles(out, GameState.getBoard());

		if(!GameState.previousAction.isEmpty()) {
			GameState.previousAction.pop(); // remove the previous action, effectively cancelling it.
		}
	}

}


