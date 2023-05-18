package structures.basic.SpecialUnits;

import structures.GameState;
import structures.basic.Tile;
import structures.basic.Unit;

import java.util.HashSet;
import java.util.Set;

/**
 * abstract class with RangedAttack method for RangedAttack unit to extend
 */

public abstract class RangedAttack extends Unit {

    /**
     * RangedAttack method, returns all enemy units on the board as possible targets.
     * @param board
     * @return Set<Tile>
     */
    public static Set<Tile> specialAbility(Tile[][] board) {
        System.out.println("SPECIAL ABILITY IS CALLED");

        Set<Tile> rangedAttacks = new HashSet<>();

        for (Unit unit : GameState.getOtherPlayer().getUnits()) {

            int x = unit.getPosition().getTilex();
            int y = unit.getPosition().getTiley();

            rangedAttacks.add(board[x][y]);
        }
        return rangedAttacks;
    }
}
