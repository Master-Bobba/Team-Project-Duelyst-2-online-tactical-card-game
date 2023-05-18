
import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import events.Initalize;
import game.logic.Utility;
import play.libs.Json;
import structures.GameState;
import structures.basic.SpecialUnits.FireSpitter;
import structures.basic.Unit;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

import static game.logic.Utility.adjacentAttack;
import static game.logic.Utility.distancedAttack;
import static org.junit.Assert.*;

/**
 * To test attacking functionality of the game
 * 
 * @author bozhidarayvazov
 *
 */

public class AttackTest {
	
	@Test
	public void distancedAttackTest() {
		
		GameState gameState = new GameState();
		Initalize initalize = new Initalize();
		
		ObjectNode eventMessage = Json.newObject();
		initalize.processEvent(null, gameState, eventMessage);
		
		Unit avatar = GameState.getCurrentPlayer().getAvatar();
		
		
		Unit defender = BasicObjectBuilders.loadUnit(StaticConfFiles.u_pyromancer, 69, Unit.class);
		defender.setPositionByTile(gameState.board[4][2]); 
		gameState.board[4][2].setOccupier(defender);
		
		gameState.getAiPlayer().setUnit(defender);
		GameState.modifiyTotalUnits(1);
		defender.setHealth(4);
		defender.setAttack(3);
		
		/**
		 * test the attacking function
		 */
		
		assertTrue(defender.getHealth() == 4);
		
		Utility.distancedAttack(avatar, defender);
		
		assertTrue(defender.getHealth() == (4 - avatar.getAttack()));
		
		/**
		 * 
		 * Test the movement to target functionality as well
		 */
		
		assertTrue(avatar.getPosition().getTilex() == GameState.getBoard()[3][2].getTilex());
		assertTrue(avatar.getPosition().getTiley() == GameState.getBoard()[3][2].getTiley());
		
		
	}
	
	@Test
	public void distancedAttackTestTwo() {
		
		GameState gameState = new GameState();
		Initalize initalize = new Initalize();
		
		ObjectNode eventMessage = Json.newObject();
		initalize.processEvent(null, gameState, eventMessage);
		
		Unit avatar = GameState.getCurrentPlayer().getAvatar();
		
		
		Unit defender = BasicObjectBuilders.loadUnit(StaticConfFiles.u_pyromancer, 69, Unit.class);
		defender.setPositionByTile(gameState.board[4][2]); 
		gameState.board[4][2].setOccupier(defender);
		
		gameState.getAiPlayer().setUnit(defender);
		GameState.modifiyTotalUnits(1);
		defender.setHealth(4);
		defender.setAttack(3);
		
		/**
		 * 
		 * Test the movement to target functionality as well
		 */
		assertTrue(avatar.getPosition().getTilex() == GameState.getBoard()[1][2].getTilex());
		assertTrue(avatar.getPosition().getTiley() == GameState.getBoard()[1][2].getTiley());
		
		Utility.distancedAttack(avatar, defender);
				
		assertTrue(avatar.getPosition().getTilex() == GameState.getBoard()[3][2].getTilex());
		assertTrue(avatar.getPosition().getTiley() == GameState.getBoard()[3][2].getTiley());
		
		
	}

	/**
	 * Test the RangedAttack unit ability to attack any enemy unit on the board
	 */
	@Test
	public void rangedAttackTest() {
		GameState gameState = new GameState();
		Initalize initialize = new Initalize();
		ObjectNode eventMessage = Json.newObject();
		initialize.processEvent(null, gameState, eventMessage);

		Unit fireSpitter = BasicObjectBuilders.loadUnit(StaticConfFiles.u_fire_spitter, 70, FireSpitter.class);
		fireSpitter.setHealth(3);
		fireSpitter.setAttack(2);

		fireSpitter.setPositionByTile(gameState.board[0][0]);
		gameState.board[0][0].setOccupier(fireSpitter);

		Unit aiAvatar = gameState.board[7][2].getOccupier();

		distancedAttack(fireSpitter, aiAvatar);

		assertEquals(18, aiAvatar.getHealth());
	}

	/**
	 * Test to see if units counterattack after they've been attacked
	 */
	@Test
	public void counterAttackTest() {
		GameState gameState = new GameState();
		Initalize initialize = new Initalize();
		ObjectNode eventMessage = Json.newObject();
		initialize.processEvent(null, gameState, eventMessage);

		Unit humanAvatar = gameState.board[1][2].getOccupier();
		Unit aiAvatar = gameState.board[7][2].getOccupier();

		humanAvatar.setPositionByTile(gameState.board[6][2]);
		gameState.board[6][2].setOccupier(humanAvatar);

		adjacentAttack(humanAvatar, aiAvatar);

		assertEquals(18, humanAvatar.getHealth());
	}
	

}
