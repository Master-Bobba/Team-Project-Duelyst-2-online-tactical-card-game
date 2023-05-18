package structures.basic.spellcards;

import akka.actor.ActorRef;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;
import game.logic.Gui;
import game.logic.Utility;
import structures.GameState;
import structures.basic.SpecialUnits.Pureblade;
import structures.basic.Player;

import java.util.ArrayList;
import java.util.Set;

/**
 *
 * AI spell card. Adds +2 attack to avatar
 *
 */
public class Ykir extends SpellCard {

	@Override
	public boolean castSpell(Unit target, Tile targetTile) {
		if (GameState.getCurrentPlayer() == GameState.getHumanPlayer()) {
			if (target.getId() != 100)
				return false;
		} else {
			if (target.getId() != 101)
				return false;
		}
		this.handleSpellThief();	
		
		target.setAttack(target.getAttack() + 2);
		
        Gui.setUnitStats(target, target.getHealth(), target.getAttack());
        Gui.playEffectAnimation(BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff), targetTile);
                
        return true;

	}
	@Override
	public void highlightTargets(ActorRef out) {
		// TODO Auto-generated method stub
		ArrayList<Unit> targets = new ArrayList<>();
		
		Unit avatar = null;
        
        for (Unit unit : GameState.getCurrentPlayer().getUnits()) {
        	if (unit.getId() == 100 || unit.getId() == 101) {
        		avatar = unit;
        	}
        }
        targets.add(avatar);
        Set<Tile> positions = Utility.getSpellTargetPositions(targets);
        Gui.highlightTiles(out, positions, 2);
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

	@Override
	public ArrayList<Unit> getTargets() {
		ArrayList<Unit> targets = new ArrayList<>();
		//Unit avatar = null;

		for (Unit unit : GameState.getCurrentPlayer().getUnits()) {
			if (unit.getId() == 100 || unit.getId() == 101) {
				targets.add(unit);
			}
		}
		System.out.print("Ykir avatar null: ");
		return targets;
	}

}
