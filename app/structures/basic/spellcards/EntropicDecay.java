package structures.basic.spellcards;

import akka.actor.ActorRef;


import structures.basic.Tile;
import structures.basic.Unit;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;
import game.logic.Gui;
import game.logic.Utility;
import structures.GameState;
import structures.basic.*;
import structures.basic.SpecialUnits.Pureblade;

import java.util.ArrayList;
import java.util.Set;
/**
 *
 * AI spell card. Reduces a non-avatar unit
 * to 0 health
 *
 */
public class EntropicDecay extends SpellCard {

    @Override
    public boolean castSpell(Unit target, Tile targetTile) {

    	if(target.getId() == 100 || target.getId() == 101)
    		return false; // Players cannot target player avatars.
        if(GameState.getCurrentPlayer().getUnits().contains(target))
        	return false; // 'Friendly fire will not be tolerated!'
        
        this.handleSpellThief();
        
        // Kill target
        target.setHealth(0);
        Gui.setUnitStats(target, target.getHealth(), target.getAttack());
        Gui.playEffectAnimation(BasicObjectBuilders.loadEffect(StaticConfFiles.f1_martyrdom), targetTile);

        Utility.checkEndGame(target);

        return true;
    }

    @Override
    public void highlightTargets(ActorRef out) {

        ArrayList<Unit> targets = new ArrayList<>();
        
        if (GameState.getCurrentPlayer() == GameState.getHumanPlayer()) {
            targets = GameState.getAIPlayer().getUnits();            
        } else {
            targets = GameState.getHumanPlayer().getUnits();  
        }
        // Remove player avatar from targets
        Unit avatar = null;

        for (Unit unit : GameState.getOtherPlayer().getUnits()) {
            if (unit.getId() == 100 || unit.getId() == 101) {
                avatar = unit;
            }
        }

        targets.remove(avatar);
        // Get tile positions and highlight them
        Set<Tile> positions = Utility.getSpellTargetPositions(targets);
        Gui.highlightTiles(out,positions,2);
        
    }

    /**
	 * This function checks if a unit with SpellThief is on the board and handles
	 * the logic for this.
     * @author Daniel
	 */
    public void handleSpellThief(){
        Player enemy = GameState.getHumanPlayer();
        for (Unit unit : enemy.getUnits()){
            if (unit.getClass().equals(Pureblade.class)){
                Pureblade p = (Pureblade)unit;
                p.specialAbility();
                return;
            }
        }        
    }
    

    public ArrayList<Unit> getTargets() { // Overloaded method for use by AI

        ArrayList<Unit> targets = new ArrayList<>();

        for (Unit unit : GameState.getOtherPlayer().getUnits()) {
            if (unit.getId() != 100) {
                targets.add(unit);
            }
        }
        return targets;
    }
    
}
