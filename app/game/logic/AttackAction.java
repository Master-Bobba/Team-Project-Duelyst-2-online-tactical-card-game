package game.logic;

import structures.basic.Tile;
import structures.basic.Unit;

/**
 *  This class is used to match units that can attack with a potential target, represented by a tile on which
 *  an enemy unit is positioned. 
 *  Value is also used to store the evaluated score assigned to the specific action. 
 * 
 *  @author Bozhidar Ayvazov
 */

public class AttackAction {
	
	public Unit unit;
	public Tile tile;
	public Integer value;
	
	public AttackAction(Unit unit, Tile tile) {
		// TODO Auto-generated constructor stub
		this.unit = unit;
		this.tile = tile;
		this.value = -1;
	}
	public AttackAction() { //to allow for overriding constructor in child classes. 
		
	}

}
