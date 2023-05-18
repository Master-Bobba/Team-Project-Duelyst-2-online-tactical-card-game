import org.junit.Test;
import structures.GameState;
import structures.basic.Player;
import static org.junit.Assert.*;

public class ManaGainTest {

    @Test
    public void manaGain() {
        GameState game = new GameState();
        Player player = new Player();
        Player enemy = new Player();
        game.setHumanPlayer(player);
        game.setCurrentPlayer(player);
        GameState.enemy = enemy;
        assertEquals(player.getMana(), game.getTurnNumber() + 1);
        game.handOverControl();
        game.handOverControl(); // simulate AI handing over control after their turn.
        assertEquals(player.getMana(), game.getTurnNumber() + 1);

    }
}
