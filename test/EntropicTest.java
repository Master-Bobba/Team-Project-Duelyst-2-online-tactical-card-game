import com.fasterxml.jackson.databind.node.ObjectNode;
import events.Initalize;
import org.junit.Test;
import play.libs.Json;
import structures.GameState;
import structures.basic.Unit;
import structures.basic.spellcards.EntropicDecay;
import structures.basic.spellcards.SpellCard;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Tests for the Entropic Decay spell card.
 *
 * @author Odhran Combe
 */
public class EntropicTest {

    // Check avatars aren't targeted by this spellcard and that Entropic Decay cannot be cast on them.
    @Test
    public void noAvatar() {
        // Initialise game
        GameState gameState = new GameState();
        Initalize initalize = new Initalize();
        ObjectNode eventMessage = Json.newObject();
        initalize.processEvent(null, gameState, eventMessage);

        // Get human and AI avatars
        Unit human = GameState.getHumanPlayer().getAvatar();
        Unit AI = GameState.getAIPlayer().getAvatar();

        // Get targets for Entropic Decay
        SpellCard entropic = new EntropicDecay();
        ArrayList<Unit> targets = entropic.getTargets();

        assertFalse(targets.contains(human)); // Check that both human and Ai avatars aren't targeted
        assertFalse(targets.contains(AI));

    }


    // Ensure Entropic Decay cannot be cast on either human or AI avatar
    @Test
    public void noCastOnAvatar(){
        // Initialise game
        GameState gameState = new GameState();
        Initalize initalize = new Initalize();
        ObjectNode eventMessage = Json.newObject();
        initalize.processEvent(null, gameState, eventMessage);

        // Get human and AI avatars
        Unit human = GameState.getHumanPlayer().getAvatar();
        Unit AI = GameState.getAIPlayer().getUnits().get(0);

        // try to cast spell on either player. Ensure method was unsuccessful.
        SpellCard entropic = new EntropicDecay();

        assertFalse(entropic.castSpell(human,GameState.getBoard()[human.getPosition().getTilex()][human.getPosition().getTiley()]));
        assertFalse(entropic.castSpell(AI,GameState.board[7][2]));

        assertNotEquals(0, AI.getHealth());
        assertNotEquals(0, human.getHealth());
    }

    // Check Entropic Decay kills target and removes from board
    @Test
    public void checkDeath() {
        // Initialise game
        GameState gameState = new GameState();
        Initalize initalize = new Initalize();
        ObjectNode eventMessage = Json.newObject();
        initalize.processEvent(null, gameState, eventMessage);

        // Get non-avatar unit to cast spell on
        Unit target = BasicObjectBuilders.loadUnit(StaticConfFiles.u_ironcliff_guardian, 69, Unit.class);
        target.setPositionByTile(gameState.board[4][4]);
        gameState.board[4][4].setOccupier(target);

        SpellCard entropic = new EntropicDecay();
        entropic.castSpell(target, gameState.board[4][4]);

        assertEquals(target.getHealth(), 0); // assert that the target's health has been set to 0
        assertNull(gameState.board[4][4].getOccupier()); // Check unit is removed from board.

    }
}
