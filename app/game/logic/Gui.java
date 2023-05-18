package game.logic;

import akka.actor.ActorRef;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import commands.BasicCommands;
import structures.basic.*;
import structures.basic.Card;
import structures.basic.Unit;
import structures.basic.UnitAnimationType;


/**
 * This class will be used to display and update information on the screen
 * including updating tile highlights, stats and animations;
 *
 *  to highlight tiles on the screen
 *  Can be used for both movement and attacks
 *  mode 1 = movement and summon 
 *  mode 2 = attack 
 */

public class Gui {


	private static ActorRef out;

	public Gui(ActorRef out) {
		Gui.out = out;
	}
	
	/**
	 * Highlighting moves and targets
	 * @param out
	 * @param tiles
	 * @param mode
	 * 
	 * @author Bozhidar Ayvazov
	 */
	public static void highlightTiles(ActorRef out, Set<Tile> tiles, int mode) {
		
		if (tiles == null) {
			return;
		}
		for (Tile tile : tiles) {
			//try {Thread.sleep(2);} catch (InterruptedException e) {e.printStackTrace();}
			BasicCommands.drawTile(out, tile, mode);
			try {Thread.sleep(2);} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
	/**
	 * Removes the highlight of tiles aka. sets it to default value of 0
	 * @param out
	 * @param board
	 * 
	 * @author Bozhidar Ayvazov
	 */
	public static void removeHighlightTiles(ActorRef out, Tile[][] board) {

		Set<Tile> unhighlightedTiles = new HashSet<>();

		for (Tile[] tiles : board) {
			unhighlightedTiles.addAll(Arrays.asList(tiles).subList(0, board[0].length));
		}
		highlightTiles(out, unhighlightedTiles, 0);
	}

	
	/**
	 * Performs the attack animation on the screen
	 * @param unit
	 * 
	 * @author Bozhidar Ayvazov
	 */
	public static void performAttack(Unit unit) {
		BasicCommands.playUnitAnimation(out, unit, UnitAnimationType.attack);
		try {Thread.sleep(1500);} catch (InterruptedException e) {e.printStackTrace();}
	}

	/**
	 * Updates the stats of units on the screen
	 * @param unit
	 * @param health
	 * @param attack
	 * 
	 * @author Bozhidar Ayvazov
	 */
	public static void setUnitStats(Unit unit, int health, int attack) {
		// setUnitHealth
		
		try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
		
		// avoid negative health on the screen
		if (health < 0) 
			BasicCommands.setUnitHealth(out, unit, 0);
		else 
			BasicCommands.setUnitHealth(out, unit, health);
		
		try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitAttack(out, unit, attack);
		
	}
	
	/**
	 * Outputs a message on the screen
	 * @param message
	 */
	public static void printPlayerMessage(String message) {
		BasicCommands.addPlayer1Notification(out, message, 5);
	}
	
	/**
	 * Updates the stats of units on the screen
	 * @param unit
	 * @param health
	 */
	public static void setUnitStats(Unit unit, int health) {
		// setUnitHealth
		BasicCommands.setUnitHealth(out, unit, health);
		try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
	}

		
	/**
	 * Displays card in the hand of the player on the screen
	 * @param card
	 * @param position
	 */
	public static void displayCard(Card card, int position) {
		BasicCommands.drawCard(out, card, position, 0);
	}

	public static void displayHumanHP(Player player){
		BasicCommands.setPlayer1Health(out, player);
	}
	
	public static void displayHumanMana(Player player) {
		BasicCommands.setPlayer1Mana(out, player);
	}

	public static void displayAIHP(Player player) {
		BasicCommands.setPlayer2Health(out, player);
	}

	public static void displayAIMana(Player player) {
		BasicCommands.setPlayer2Mana(out, player);
	}

	// draw unit on board
	public static void drawUnit(Unit unit, Tile tile) {
		BasicCommands.drawUnit(out, unit, tile);
	}

	/**
	 * Highlights a card in the player's hand. Can be used to show a card is selected.
	 * @param card the card being highlighted
	 * @param position the position of the card in the player's hand (1 - 6).
	 */
	public static void highlightCard(Card card, int position) {
		BasicCommands.drawCard(out, card, position,1);
	}

	public static void setUnitHealth(Unit unit, int health) {
		BasicCommands.setUnitHealth(out, unit, health);
	}

	/**
	 * Display an effect animation on the GUI (e.g. spell effects).
	 * @param effect the animation to be played.
	 * @param tile the tile where the animation will be played on.
	 */
	public static void playEffectAnimation(EffectAnimation effect, Tile tile) {
		BasicCommands.playEffectAnimation(out, effect, tile);
	}

	/**
	 * Removes a card from the player's hand.
	 * @param position the position of the card to be removed in the player's hand (1 - 6).
	 */
	public static void deleteCard(int position) {
		BasicCommands.deleteCard(out, position);
	}

}

