import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.node.ObjectNode;


import events.Initalize;
import play.libs.Json;
import structures.GameState;

public class InitalisationTest {

	@Test
	public void checkInitalised() {
		
		
		GameState gameState = new GameState();
		Initalize initalizeProcessor = new Initalize();
		
		assertFalse(gameState.gameInitalised);
		
		ObjectNode eventMessage = Json.newObject();
		initalizeProcessor.processEvent(null, gameState, eventMessage);
		
		assertTrue(gameState.gameInitalised);
	}
}
