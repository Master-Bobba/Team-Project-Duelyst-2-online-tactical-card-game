import com.fasterxml.jackson.databind.node.ObjectNode;
import events.Initalize;
import org.junit.Test;
import play.libs.Json;
import structures.GameState;
import structures.basic.SpecialUnits.Windshrike;
import structures.basic.Tile;
import structures.basic.Unit;

import java.util.Set;

import static game.logic.Utility.*;
import static org.junit.Assert.assertEquals;

public class MovesTests {

    @Test
    public void validMovesTest() {
        GameState gameState = new GameState();
        Initalize initialize = new Initalize();
        ObjectNode eventMessage = Json.newObject();
        initialize.processEvent(null, gameState, eventMessage);

        Unit unit = gameState.board[1][2].getOccupier();

        Set<Tile> validTiles = determineValidMoves(gameState.board, unit);

        assertEquals(11, validTiles.size());
    }

    @Test
    public void flyingAbilityTilesTest() {
        GameState gameState = new GameState();
        Initalize initialize = new Initalize();
        ObjectNode eventMessage = Json.newObject();
        initialize.processEvent(null, gameState, eventMessage);

        Unit windshrike = new Windshrike();
        windshrike.setPositionByTile(gameState.board[0][0]);
        gameState.board[0][0].setOccupier(windshrike);

        Set<Tile> validTiles = determineValidMoves(gameState.board, windshrike);

        assertEquals(42, validTiles.size());
    }


}
