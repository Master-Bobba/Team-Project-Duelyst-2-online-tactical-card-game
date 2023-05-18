package structures.basic.spellcards;

import akka.actor.ActorRef;

import structures.basic.Tile;
import structures.basic.Unit;
import structures.GameState;
import structures.basic.Player;
import structures.basic.SpecialUnits.*;

import java.util.ArrayList;

public class Staff extends SpellCard {


    @Override
    public boolean castSpell(Unit target, Tile targetTile) {
        this.handleSpellThief();
        return false;
    }

    @Override
    public void highlightTargets(ActorRef out) {

    }


    /*
     * Checks if unit has SpellThief by checking unit id, if so, applies the affect
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
        return null;

    }
}
