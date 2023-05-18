package events;

import com.fasterxml.jackson.databind.JsonNode;
import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.AIPlayer;
import structures.basic.Player;
import utils.BasicObjectBuilders;


/*
 * Indicates that both the core game loop in the browser is starting, meaning
 * that it is ready to recieve commands from the back-end.
 * 
 * { 
 *   messageType = “initalize”
 * }
 * 
 * @author Dr. Richard McCreadie
 * 
 */

public class Initalize implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		
		Player humanPlayer = createHumanPlayer(out);

		GameState.setTotalUnits(); 

		gameState.setHumanPlayer(humanPlayer);
		gameState.setCurrentPlayer(humanPlayer);
		gameState.gameInitalised = true;
		gameState.emptyPreviousAction();

		
		gameState.something = true;
		
		/*
		 *  Create the board of Tile Objects
		 */
		for (int i = 0; i < gameState.board.length; i++) {
			for(int j = 0; j < gameState.board[0].length; j++) {
				gameState.board[i][j] = BasicObjectBuilders.loadTile(i, j);
				BasicCommands.drawTile(out, gameState.board[i][j], 0);
				try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
			}
		}
		
		humanPlayer.createAvatar(out);
	


		AIPlayer enemy = new AIPlayer();
		GameState.enemy = enemy; //Set AI player as enemy
		enemy.createAvatar(out);
	}
	
	
	/*
	 * Create the Human Player
	 */
	public Player createHumanPlayer(ActorRef out) {
		Player player1 = new Player();
		for (int i = 0; i < 3; i++) {
			player1.drawCard();
		}

		return player1;
	}
	
	
}


