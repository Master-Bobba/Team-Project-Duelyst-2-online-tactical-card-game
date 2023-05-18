package structures.basic.SpecialUnits;

import structures.basic.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Windshrike class implementing Special Ability flying (move anywhere on the board)
 */
public class Windshrike extends Unit {

    private final String name = "Windshrike";

    /**
     * @param board
     * @return Set<Tile>
     */
    public static Set<Tile> specialAbility(Tile[][] board) {
        System.out.println("SPECIAL ABILITY IS CALLED");

        Set<Tile> validTiles = new HashSet<>();

        for (Tile[] row : board) {
            for (Tile tile : row) {
                if (tile.getOccupier() == null) {
                    validTiles.add(tile);
                }
            }
        }
        return validTiles;
    }
}
