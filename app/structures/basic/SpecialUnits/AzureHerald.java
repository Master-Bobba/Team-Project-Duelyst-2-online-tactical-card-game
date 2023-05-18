package structures.basic.SpecialUnits;

import structures.GameState;

import structures.basic.Unit;


/**
 * Class for the Azure Herald special unit class
 * Extends the basic Unit class
 * @author bozhidar ayvazov
 *
 */

public class AzureHerald extends Unit {

    private final String name = "Azure Herald";
    
    public static void specialAbility(Unit avatar) {
        avatar.setHealth(Math.min(avatar.getHealth() + 3, 20));
    }
}
