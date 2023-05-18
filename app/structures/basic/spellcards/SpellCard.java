package structures.basic.spellcards;

import akka.actor.ActorRef;
import structures.basic.*;
import java.util.ArrayList;

/**
 *
 * A base contract for spell cards that all other spell cards
 * draw from.
 *
 */
public abstract class SpellCard extends Card implements Playable{

    /**
     * perform ability. Report back if successful.
     * @param target the unit you wish to target
     * @param targetTile the location of the unit you wish to target
     * @return boolean - true if successful, else - false
     */
    public abstract boolean castSpell(Unit target, Tile targetTile);

    /**
     * Display on GUI the tiles that the player can select
     * @param out
     */
    public abstract void highlightTargets(ActorRef out); // highlight valid targets for a particular spellcard.

    /**
     * Get all the targets that this spell can be performed on
     * @return ArrayList<Unit> - units
     */
    public abstract ArrayList<Unit> getTargets(); // get targets of this card

}
