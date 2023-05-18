import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import commands.BasicCommands;
import events.Initalize;
import game.logic.Utility;
import play.libs.Json;
import structures.GameState;
import structures.basic.Player;
import structures.basic.Unit;
import structures.basic.SpecialUnits.Serpenti;
import structures.basic.SpecialUnits.SilverguardKnight;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

/**
 * Test if a unit with 0 health is deleted from the player's list of units
 * @author Morvern Mackintosh
 */

public class UnitDeathTest {
	@Test
	public void unitDeath() {
		GameState gameState = new GameState();
		Initalize initalize = new Initalize();
		ObjectNode eventMessage = Json.newObject(); 
		initalize.processEvent(null, gameState, eventMessage);
		Player human = GameState.getHumanPlayer();
		Player enemy = GameState.getAIPlayer();
		
		Unit silverguardKnight = BasicObjectBuilders.loadUnit(StaticConfFiles.u_silverguard_knight, 70, SilverguardKnight.class); 
		silverguardKnight.setHealth(5); //set initial health
		human.setUnit(silverguardKnight);
		assertTrue(human.getUnits().contains(silverguardKnight)); //check that silverguard knight unit was successfully added to player's list of units
		assertTrue(silverguardKnight.getHealth() == 5); // check that health was successfully set 
		Unit attacker = BasicObjectBuilders.loadUnit(StaticConfFiles.u_serpenti, 71, Serpenti.class);
		attacker.setAttack(7);
		enemy.setUnit(attacker);
		assertTrue(enemy.getUnits().contains(attacker)); //check that serpenti unit was successfully added to enemy's list of units
		// Serpenti has 7 attack so should kill the unit
		silverguardKnight.setHealth(silverguardKnight.getHealth() - attacker.getAttack());
		Utility.checkEndGame(silverguardKnight); //call unit death and end game method
		assertTrue(silverguardKnight.getHealth() <= 0); //check that unit's health has changed
		assertFalse(human.getUnits().contains(silverguardKnight));  //check that unit has been removed from player's units
		
	}

}
