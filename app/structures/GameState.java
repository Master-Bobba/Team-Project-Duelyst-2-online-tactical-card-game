package structures;

import java.awt.datatransfer.Clipboard;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;

import structures.basic.Player;
import structures.basic.Position;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.spellcards.SpellCard;
import structures.basic.Card;
import structures.basic.Playable;

/**
 * This class can be used to hold information about the on-going game.
 * Its created with the GameActor.
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class GameState {



	private static Player currentPlayer; // store who's round it currently is
	private static Player humanPlayer;

	private int turnNumber = 1;


	
	public static Player enemyPlayer;

	
	private static int totalUnits;

	public static Player enemy;

	public boolean gameInitalised = false;
	public boolean something = false;
	
//	storing validMoves and valid Attacks
	public static Set<Tile> validMoves = new HashSet<>();
	public static Set<Tile> validAttacks = new HashSet<>();
	
//	stack of actions taken by the player
	public static Stack<Playable> previousAction = new Stack<Playable>();	
	public static Tile[][] board = new Tile[9][5]; // should be made private
	
	public static Object getPreviousAction() {
		return previousAction.pop();
	}	
	
	/**
	 * Saves the selection (any action) done by the player on to a stack 
	 * to be used in the execution of the action when further selection by the 
	 * player is made.
	 * @param action
	 * @return 
	 */
	public static void setPreviousAction(Playable action) {
		previousAction.push(action);
		//System.out.println(action.type);
		System.out.println("pushed to stack" + action);
	}
	/**
	 * Clears any actions saved on the stack
	 * @param
	 * @return
	 */
	public static void emptyPreviousAction(){
		previousAction.clear();
		System.out.println("emptyprev");
	}

	public static Object peekPreviousAction(){
		return previousAction.peek();
	}
	
	public static Player getCurrentPlayer() {
		return currentPlayer;
	}

	public static void setCurrentPlayer(Player player) {
		currentPlayer = player;
	}

	public void setHumanPlayer(Player player) {
		humanPlayer = player;
	}

	public static Player getHumanPlayer() {
		return humanPlayer;
	}

	public static Player getEnemyPlayer() { // this is repeating - should go
		return enemyPlayer;
	}

	public static void modifiyTotalUnits(int mod){
		totalUnits += mod;
	}
	

	public static Player getAIPlayer() {
		return enemy;
	}

	public static Tile[][] getBoard() {
		return board;
	}


	public int getTurnNumber() {
		return turnNumber;
	}

	public void incrementTurn(){
		turnNumber++;
	}
	
	/**
	 * Switches the gamestate between players
	 * and 
	 * increments the turn count
	 * @param
	 * @return
	 */
	public void handOverControl() {
		System.out.println(getCurrentPlayer() == getHumanPlayer());
		if(getCurrentPlayer() == getHumanPlayer()){
			setCurrentPlayer(getAIPlayer());
		}
		else {
			setCurrentPlayer(getHumanPlayer());
			incrementTurn();
		}

		// give new current player appropriate mana at beginning of their turn
		getCurrentPlayer().setMana(getTurnNumber() + 1); // TODO CHANGED FOR TEST. CHANGE BACK TO +1
	}
	public static Player getAiPlayer() {
		return enemy;
	}
	/**
	 * Get the player who is currently NOT on the board. 
	 * @return
	 */
	public static Player getOtherPlayer() {
		if (getCurrentPlayer().equals(humanPlayer)) {
			return getAiPlayer();
		} else {
			return getHumanPlayer();
		}
	}
	

	public static void setTotalUnits(){
		totalUnits = 0;
	}

	public static int getTotalUnits(){
		return totalUnits;
	}

}

