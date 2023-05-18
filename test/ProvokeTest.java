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
import structures.basic.SpecialUnits.SilverguardKnight;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;


/**
 * To test Provoke Unit Ability
 * 
 * @author bozhidarayvazov
 *
 */

public class ProvokeTest {
	
	@Test
	public void ProvokeTestOne() {
		
		GameState gameState = new GameState();
		Initalize initalize = new Initalize();
		
		ObjectNode eventMessage = Json.newObject();
		initalize.processEvent(null, gameState, eventMessage);
		
		Unit avatar = GameState.getCurrentPlayer().getAvatar();
		
		
		Unit defender = BasicObjectBuilders.loadUnit(StaticConfFiles.u_pyromancer, 69, Unit.class);
		defender.setPositionByTile(gameState.board[2][2]); 
		gameState.board[2][2].setOccupier(defender);
		
		gameState.getAiPlayer().setUnit(defender);
		GameState.modifiyTotalUnits(1);
		defender.setHealth(4);
		defender.setAttack(3);
		
		SilverguardKnight provoke = (SilverguardKnight) BasicObjectBuilders.loadUnit(StaticConfFiles.u_pyromancer, 70, SilverguardKnight.class);
		defender.setPositionByTile(gameState.board[2][3]); 
		gameState.board[2][3].setOccupier(defender);
		
		gameState.getAiPlayer().setUnit(defender);
		GameState.modifiyTotalUnits(1);
		defender.setHealth(4);
		defender.setAttack(3);
		
		
		Tile avatarPosition = GameState.getBoard()[avatar.getPosition().getTilex()][avatar.getPosition().getTiley()];
		
		Set<Tile> validAttacks = Utility.getValidTargets(avatarPosition, GameState.getOtherPlayer(), GameState.getBoard());
		
		/*
		 * 1 attack tile is expected
		 */
		assertTrue(validAttacks.size() == 1);
		
	}
	
	@Test
	public void ProvokeTestTwo() {
		
		GameState gameState = new GameState();
		Initalize initalize = new Initalize();
		
		ObjectNode eventMessage = Json.newObject();
		initalize.processEvent(null, gameState, eventMessage);
		
		Unit avatar = GameState.getCurrentPlayer().getAvatar();
		
		
		Unit defender = BasicObjectBuilders.loadUnit(StaticConfFiles.u_pyromancer, 69, Unit.class);
		defender.setPositionByTile(gameState.board[2][2]); 
		gameState.board[2][2].setOccupier(defender);
		
		gameState.getAiPlayer().setUnit(defender);
		GameState.modifiyTotalUnits(1);
		defender.setHealth(4);
		defender.setAttack(3);
		
		Unit provoke = (SilverguardKnight) BasicObjectBuilders.loadUnit(StaticConfFiles.u_silverguard_knight, 70, SilverguardKnight.class);
		provoke.setPositionByTile(gameState.board[2][3]); 
		gameState.board[2][3].setOccupier(provoke);
		
		gameState.getAiPlayer().setUnit(provoke);
		GameState.modifiyTotalUnits(1);
		provoke.setHealth(4);
		provoke.setAttack(3);
		
		
		Tile avatarPosition = GameState.getBoard()[avatar.getPosition().getTilex()][avatar.getPosition().getTiley()];
		
		Set<Tile> validAttacks = Utility.getValidTargets(avatarPosition, GameState.getOtherPlayer(), GameState.getBoard());
		
		/*
		 * is it the provoke unit?
		 */
				
		assertTrue(validAttacks.remove(gameState.getBoard()[provoke.getPosition().getTilex()][provoke.getPosition().getTiley()]));
		
	}

}
