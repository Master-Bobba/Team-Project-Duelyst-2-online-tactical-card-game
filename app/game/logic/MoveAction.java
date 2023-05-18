package game.logic;

import structures.basic.Tile;
import structures.basic.Unit;

/**
 * This class is used for AI to create potential moves, stores a unit that can move
 * as well as a tile it could move to.
 * Value stores the score of the move after it's been evaluated.
 */
public class MoveAction {

    public Unit attacker;
    public Tile moveToTile;
    public Integer value;


    public MoveAction(Unit unit, Tile moveTile) {
        this.attacker = unit;
        this.moveToTile = moveTile;
        this.value = -1;
    }
}
