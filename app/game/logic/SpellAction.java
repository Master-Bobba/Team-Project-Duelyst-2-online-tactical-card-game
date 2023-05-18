package game.logic;

import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.spellcards.SpellCard;

/**
 * Class used for storing information about possible actions involving spells the AI could perform.
 * Stores the spell card in question, and a value attribute indicating the 'usefulness' of the action
 * (extends from AttackAction)
 *
 */
public class SpellAction extends AttackAction{

    // track position of spell card in player's hand so can be removed later + spellcard used.
    int spellCardHandPos;
    SpellCard spellCard;

    public SpellAction(Unit unit, Tile tile, SpellCard spellcard){
        super(unit, tile);
        this.spellCard = spellcard;
        this.spellCardHandPos = spellCardHandPos;
    }
}
