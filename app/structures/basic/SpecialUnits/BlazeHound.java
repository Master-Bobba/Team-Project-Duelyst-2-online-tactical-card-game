package structures.basic.SpecialUnits;

import structures.GameState;

import structures.basic.Unit;

import java.util.Set;

/**
 * A class for the BlazeHound unit
 * Extends the basic unit class
 * @author bozhidar ayvazov
 *
 */

public class BlazeHound extends Unit {

    private final String name = "Blaze Hound";

    
    public static void specialAbility() {
        GameState.getHumanPlayer().drawCard();
        GameState.getAIPlayer().drawCard();
    }
}
