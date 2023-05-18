import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import events.Initalize;
import game.logic.Utility;
import play.libs.Json;
import structures.GameState;
import structures.basic.Player;
import structures.basic.Position;
import structures.basic.Tile;
import structures.basic.Unit;

public class MoveUnitTest {
	
/**
 * Testing move unit functionality
 * @author Morvern Mackintosh
 */
	
	@Test
	public void moveUnit() {
		GameState gameState = new GameState();
		Initalize initalize = new Initalize();
		ObjectNode eventMessage = Json.newObject(); 
		initalize.processEvent(null, gameState, eventMessage); 
		Tile destinationTile = gameState.getBoard()[3][2];
		Unit unit = gameState.getHumanPlayer().getAvatar();
		
		// the unit's starting position
		int posX = unit.getPosition().getTilex(); 
		int posY = unit.getPosition().getTiley(); 
		
		Set<Tile> validMoves = Utility.determineValidMoves(gameState.getBoard(),unit);
		
		if(validMoves.contains(gameState.getBoard()[3][2])){
			Utility.moveUnit(unit, destinationTile);
			assertTrue(unit.hasMoved());
			assertTrue(gameState.getBoard()[posX][posY].getOccupier() == null); // unit's previous tile should be unoccupied 
			assertTrue(destinationTile.getOccupier().equals(unit));	// destination tile should be occupied by the avatar unit
			
//			try to move the same unit to a new tile, after having moved already
			Tile newTile = gameState.getBoard()[4][2]; 
			if(unit.hasMoved() == false) {
				Utility.moveUnit(unit, newTile);
			}
			assertTrue(newTile.getOccupier() == null); // unit should not have moved to the new tile
			assertTrue(unit.getPosition().getTilex() == 3 && unit.getPosition().getTiley() == 2); 
		}
		
	}
}
