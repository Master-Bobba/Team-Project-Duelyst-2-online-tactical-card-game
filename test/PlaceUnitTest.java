package test;

import events.Initalize;
import structures.GameState;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

import akka.actor.ActorRef;
import commands.BasicCommands;
import game.logic.Gui;
import game.logic.Utility;
import events.CardClicked;
import events.TileClicked;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;
import structures.basic.Card;
import structures.basic.Tile;
import structures.basic.AIPlayer;
import structures.basic.Player;
import structures.basic.Unit;
import structures.basic.SpecialUnits.AzureHerald;
import structures.basic.SpecialUnits.IroncliffGuardian;

import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

public class PlaceUnitTest {

    /**
     * validTiles tests if the validtiles function returns the correct number of tiles
     * that a given unit card can be placed onto
     */
    @Test
    public void validTiles(){

		GameState gameState = new GameState();
		Initalize initalize = new Initalize();
		
		ObjectNode eventMessage = Json.newObject();
		initalize.processEvent(null, gameState, eventMessage);
		
		Player player1 = GameState.getCurrentPlayer();
        AIPlayer player2 = (AIPlayer)GameState.enemy;
        player2.getAvatar();
        player2.getUnits();

        //* Regular unit test */

        Card card = BasicObjectBuilders.loadCard(StaticConfFiles.c_comodo_charger, 0, Card.class);

        Set<Tile> s = Utility.cardPlacements(card, player1, player2, GameState.board);

        Assert.assertEquals(8, s.size());

        //* Airdrop unit test */

        card = BasicObjectBuilders.loadCard(StaticConfFiles.c_ironcliff_guardian, 6, Card.class);
        s = Utility.cardPlacements(card, player1, player2, GameState.board);

        Assert.assertEquals(43, s.size());
    }

    /**
     * placeUnit determines if the units are added to the tile properly and if correct
     * SpecialUnit objects are created if needed
     */
    public void placeUnit(){
        GameState gameState = new GameState();
		Initalize initalize = new Initalize();
		
		ObjectNode eventMessage = Json.newObject();
		initalize.processEvent(null, gameState, eventMessage);
		
		Player player1 = GameState.getCurrentPlayer();
        AIPlayer player2 = (AIPlayer)GameState.enemy;

        player2.getAvatar();
        player2.getUnits();

        //* Regular unit test */
        Tile tilea = GameState.board[0][0];

        Card card = BasicObjectBuilders.loadCard(StaticConfFiles.c_comodo_charger, 0, Card.class);

        Utility.placeUnit(null, card , player1, tilea);

        Unit u = tilea.getOccupier();
        boolean a = u.equals(Unit.class); 

        Assert.assertTrue(a);


        //* IroncliffGuardian checks if correct class is instansiated*/

        tilea = GameState.board[0][1];

        card = BasicObjectBuilders.loadCard(StaticConfFiles.c_ironcliff_guardian, 6, Card.class);

        Utility.placeUnit(null, card , player1, tilea);

        u = tilea.getOccupier();
        a = u.equals(IroncliffGuardian.class); 

        Assert.assertTrue(a);

        //* Azure Hearld - Checks if correct class is generated and human player health increased*/

        tilea = GameState.board[0][2];

        card = BasicObjectBuilders.loadCard(StaticConfFiles.c_azure_herald, 5, Card.class);

        Utility.placeUnit(null, card , player1, tilea);

        int p1Health1 = player1.getHealth();

        u = tilea.getOccupier();
        a = u.equals(AzureHerald.class); 

        Assert.assertTrue(a);

        int p1Health2 = player1.getHealth();
        Assert.assertFalse(p1Health1 == p1Health2);
    }    
}
