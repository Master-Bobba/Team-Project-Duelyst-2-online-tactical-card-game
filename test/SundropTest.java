import com.fasterxml.jackson.databind.node.ObjectNode;
import events.Initalize;
import org.junit.Test;
import play.libs.Json;
import structures.GameState;
import structures.basic.Player;
import structures.basic.Unit;
import structures.basic.spellcards.SpellCard;
import structures.basic.spellcards.Sundrop;

import static org.junit.Assert.*;

/**
 * Tests for Sundrop Elixir spell card.
 *
 * @author Odhran Combe
 */
public class SundropTest {

    // Check playing the card doesn't bring a unit over its starting health
    @Test
    public void maxHealthTest(){
        // Initialise game
        GameState gameState = new GameState();
        Initalize initalize = new Initalize();
        ObjectNode eventMessage = Json.newObject();
        initalize.processEvent(null, gameState, eventMessage);

        // Get human avatar
        Unit avatar = GameState.getCurrentPlayer().getAvatar();

        // Cast spell on avatar
        SpellCard sundrop = new Sundrop();
        assertTrue(sundrop.castSpell(avatar, GameState.getBoard()[1][2])); // check spell has been successfully cast

        assertTrue(avatar.getHealth() <= avatar.getMaxHealth()); // check new health isn't over max


    }
}
