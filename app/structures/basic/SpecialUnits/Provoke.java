package structures.basic.SpecialUnits;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import structures.GameState;
import structures.basic.Tile;
import structures.basic.Unit;


/**
 * abstract class for the units with the special ability provoke
 * 
 * @author Bozhidar Ayvazov
 */

public abstract class Provoke extends Unit{
	
	private String name = null;
	private Set<Tile> validAttacks;
	
	
	/**
	 * Returns the tile on which the provoke unit is positioned
	 *	@param tile 
	 */
	public Set<Tile> attractAttack(Tile tile){
		
		/*
		 * All this can be deleted...
		 */
		System.out.println("Attack Attracted");
		/*
		 * How many provoke units does the enemy has on the boards
		 */
		validAttacks = new HashSet<>();	
		validAttacks.add(GameState.board[this.getPosition().getTilex()][this.getPosition().getTiley()]);

		return validAttacks;
	}
	/**
	 * Disables the movement of a provoked unit
	 * @param other
	 */
	public void disableUnit(Unit other) {
		
		System.out.println("Unit disabled");
		other.setMoved();
	
	}
}