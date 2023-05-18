import structures.basic.Player;
import org.junit.Test;


import static org.junit.Assert.assertTrue;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

/**
 * Test key functionality related to the players.
 * E.g. creation of deck, drawing cards etc
 *
 * @author Odhran Combe
 */

public class PlayerTests {

    @Test
    public void deckCreationTest(){
        Player player = new Player();
        assertTrue(player.getDeck().getDeckSize() == 20);
    }

    @Test
    public void drawCard() {
        Player player = new Player();
        player.drawCard();

        assertTrue(player.getCard(1).getCardname().equals("Comodo Charger")); // Check first card is what expected to be
        assertTrue((player.getDeck().getDeckSize() == 19)); // Check deck has decreased in size
    }

    // Test to ensure the player cannot draw from an empty deck.
    @Test
    public void drawEmptyDeck(){
        Player player = new Player();
        int i = 0;
        while(i < 20) {
            player.drawCard();
            player.removeFromHand(1);
            i++;
        }
        assertThrows(NoSuchElementException.class, player::drawCard);
    }

    // Test to ensure that when the player tries to place a card from the deck into a full hand
    // that the card in question is lost.
    @Test
    public void overDraw() {
        Player player = new Player();
        int i = 0;
        while(i < 6) {
            player.drawCard();
            i++;
        }
        assertEquals("Ironcliff Guardian", player.getDeck().drawTopCard().getCardname());

    }
}
