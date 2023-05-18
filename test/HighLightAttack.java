
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Set;

import com.fasterxml.jackson.databind.node.ObjectNode;

import events.Initalize;
import game.logic.Utility;
import play.libs.Json;
import structures.GameState;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;


/**
 * To test Issue #7 Board Highligh: Attack
 * 
 * @author bozhidarayvazov
 */


public class HighLightAttack {
	
	@Test
	public void checkHighlightAttacks() {
		
		GameState gameState = new GameState();
		Initalize initalize = new Initalize();
		
		ObjectNode eventMessage = Json.newObject();
		initalize.processEvent(null, gameState, eventMessage);
		
		Unit avatar = GameState.getCurrentPlayer().getAvatar();
		Tile avatarPosition = GameState.getBoard()[avatar.getPosition().getTilex()][avatar.getPosition().getTiley()];
		
		Set<Tile> validAttacks = Utility.getValidTargets(avatarPosition, GameState.getOtherPlayer(), GameState.getBoard());
		
		/*
		 * the two avatars are too far from one another so no attacks
		 */
		assertTrue(validAttacks.size() == 0);
	}
	
	@Test
	public void checkhighlighAttacksTwo() {
		GameState gameState = new GameState();
		Initalize initalize = new Initalize();
		
		ObjectNode eventMessage = Json.newObject();
		initalize.processEvent(null, gameState, eventMessage);
		
		Unit avatar = GameState.getCurrentPlayer().getAvatar();

		// summon another unit close the avatar
		Unit unitTwo = BasicObjectBuilders.loadUnit(StaticConfFiles.u_pyromancer, 69, Unit.class);
		unitTwo.setPositionByTile(gameState.board[2][2]); 
		gameState.board[2][2].setOccupier(unitTwo);
		//BasicCommands.drawUnit(out, unitTwo, gameState.board[4][2]);
		gameState.getAiPlayer().setUnit(unitTwo);
		GameState.modifiyTotalUnits(1);
		
		
		Tile avatarPosition = GameState.getBoard()[avatar.getPosition().getTilex()][avatar.getPosition().getTiley()];
		
		Set<Tile> validAttacks = Utility.getValidTargets(avatarPosition, GameState.getOtherPlayer(), GameState.getBoard());
		
		/*
		 * 1 attack tile is expected
		 */
		assertTrue(validAttacks.size() == 1);
		
	}
	
	@Test
	public void checkhighlighAttacksThree() {
		GameState gameState = new GameState();
		Initalize initalize = new Initalize();
		
		ObjectNode eventMessage = Json.newObject();
		initalize.processEvent(null, gameState, eventMessage);
		Unit avatar = GameState.getCurrentPlayer().getAvatar();
		
		// summon another unit close the avatar
		Unit unitTwo = BasicObjectBuilders.loadUnit(StaticConfFiles.u_pyromancer, 69, Unit.class);
		unitTwo.setPositionByTile(gameState.board[2][2]); 
		gameState.board[2][2].setOccupier(unitTwo);
		//BasicCommands.drawUnit(out, unitTwo, gameState.board[4][2]);
		gameState.getAiPlayer().setUnit(unitTwo);
		GameState.modifiyTotalUnits(1);
		
		Tile avatarPosition = GameState.getBoard()[avatar.getPosition().getTilex()][avatar.getPosition().getTiley()];
		
		Set<Tile> validAttacks = Utility.getValidTargets(avatarPosition, GameState.getOtherPlayer(), GameState.getBoard());
		
		
		assertFalse(validAttacks.size() == 2);
		
		// summon another unit close the avatar
		Unit unitThree = BasicObjectBuilders.loadUnit(StaticConfFiles.u_pyromancer, 69, Unit.class);
		unitThree.setPositionByTile(gameState.board[1][3]); 
		gameState.board[1][3].setOccupier(unitThree);
		//BasicCommands.drawUnit(out, unitTwo, gameState.board[4][2]);
		gameState.getAiPlayer().setUnit(unitThree);
		GameState.modifiyTotalUnits(1);
		
		
		avatarPosition = GameState.getBoard()[avatar.getPosition().getTilex()][avatar.getPosition().getTiley()];
		
		validAttacks = Utility.getValidTargets(avatarPosition, GameState.getOtherPlayer(), GameState.getBoard());
		
		/*
		 * 1 attack tile is expected
		 */
		assertTrue(validAttacks.size() == 2);
		
	}
	
}
