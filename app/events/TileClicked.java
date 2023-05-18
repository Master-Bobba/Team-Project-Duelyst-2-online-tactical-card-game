package events;

import java.util.Stack;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import game.logic.Gui;
import game.logic.Utility;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Playable;
import structures.basic.Player;
import structures.basic.SpecialUnits.RangedAttack;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.spellcards.SpellCard;


/**
 * Indicates that the user has clicked an object on the game canvas, in this case a tile.
 * The event returns the x (horizontal) and y (vertical) indices of the tile that was
 * clicked. Tile indices start at 1.
 * 
 * { 
 *   messageType = “tileClicked”
 *   tilex = <x index of the tile>
 *   tiley = <y index of the tile>
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class TileClicked implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		
		// to avoid events handling while the AI player is playing
		if (GameState.getCurrentPlayer().equals(GameState.getAIPlayer()) ) {
			return;
		}
		
		int tilex = message.get("tilex").asInt();
		int tiley = message.get("tiley").asInt();
		
		// if there is a unit on the tile and the unit is not an enemy unit
		if (gameState.board[tilex][tiley].getOccupier() != null) {  // check if selected tile has a unit on it
			/*
			if there are previous actions, and the last action was drawing a spell card,
			play the spellcard
			 */
			if(!(GameState.previousAction.isEmpty())) {
				if (GameState.previousAction.peek() instanceof SpellCard) {
					handleSpellCasting(gameState.board[tilex][tiley].getOccupier(), gameState.board[tilex][tiley], out);
					return;
				}
			}

			if (gameState.previousAction.isEmpty()) {
				
				if (!gameState.enemy.getUnits().contains(gameState.board[tilex][tiley].getOccupier())) {
					
					Unit unit = gameState.board[tilex][tiley].getOccupier();
					
					if (!unit.hasMoved() && !unit.hasAttacked()) {
						gameState.validMoves = Utility.determineValidMoves(gameState.board, unit);
						Gui.highlightTiles(out, gameState.validMoves, 1);
						gameState.validAttacks = Utility.determineTargets(gameState.board[unit.getPosition().getTilex()][unit.getPosition().getTiley()], gameState.validMoves, GameState.enemy, gameState.board);
						Gui.highlightTiles(out, gameState.validAttacks, 2);
						gameState.previousAction.push(unit);
						
					} else if (unit.hasMoved() && !unit.hasAttacked()) {
						if (unit instanceof RangedAttack) {
							gameState.validAttacks = ((RangedAttack) unit).specialAbility(gameState.board);
						} else {
							gameState.validAttacks = Utility.getValidTargets(gameState.board[unit.getPosition().getTilex()][unit.getPosition().getTiley()], gameState.enemy, gameState.board);
						}
						Gui.highlightTiles(out, gameState.validAttacks, 2);
						gameState.previousAction.push(unit);
						
					} else {
						BasicCommands.addPlayer1Notification(out, "Unit can no longer move or attack", 5);
					}
				} else {
					
					BasicCommands.addPlayer1Notification(out, "Cannot select enemy units", 5);
				}
				
			} else {
//				if player previously clicked on a Unit 
				if (gameState.previousAction.peek() instanceof Unit) {

					//get unit from stack
					Unit unit = (Unit) GameState.getPreviousAction();
					Gui.removeHighlightTiles(out, GameState.board); //clearing board 
					
					// Determine if Adjacent or Distanced Attack aka. move and attack
					if (Utility.getValidTargets(GameState.board[unit.getPosition().getTilex()][unit.getPosition().getTiley()], GameState.getAIPlayer(), GameState.board).contains(gameState.board[tilex][tiley])) {
						Utility.adjacentAttack(unit, GameState.board[tilex][tiley].getOccupier());
						
					} else if (gameState.validAttacks.contains(GameState.board[tilex][tiley])) {
						Utility.distancedAttack(unit, GameState.board[tilex][tiley].getOccupier());
					} 
					
				}

			}// check if tile is free - can only move to an empty place 		
		} else if (gameState.board[tilex][tiley].getOccupier() == null && !gameState.previousAction.isEmpty()) {  
			if (GameState.previousAction.peek() instanceof Unit){
				if(gameState.validMoves.contains(gameState.board[tilex][tiley])) { // check if unit can move to selected tile
				
					Unit unit = (Unit) GameState.getPreviousAction(); //get unit from stack 
					Utility.moveUnit(unit, gameState.board[tilex][tiley]);
				}
//				if player previously clicked on a Card
				
			} else if( GameState.previousAction.peek() instanceof Card) {	
				System.out.println("PLACE UNIT CARD ON BOARD");
				
				Card card = (Card) GameState.previousAction.peek();
				Tile tile = GameState.board[tilex][tiley];
				Player player = GameState.getCurrentPlayer();
				Player enemy = GameState.enemy;
		
				if (Utility.validMove(out, card, player, enemy, tile, GameState.board)){
					Gui.removeHighlightTiles(out, GameState.board);
					Utility.placeUnit(out, card, player, tile);
					GameState.emptyPreviousAction();
				}
				return;
			}
		}

	}
	
	// Spell card helper function
	private void handleSpellCasting(Unit target, Tile targetTile, ActorRef out) {
		// if player does not have enough mana, skip.
		// if(!(GameState.getCurrentPlayer().getMana() >= GameState.getCurrentPlayer().getCard(CardClicked.getHandPosition()).getManacost())) return;

		SpellCard spellCard = (SpellCard)GameState.previousAction.pop();
		spellCard.highlightTargets(out);
		
		boolean successfulSpell = spellCard.castSpell(target, targetTile);

		if(successfulSpell) {
			//decrement mana from player
			GameState.getCurrentPlayer().setMana(GameState.getCurrentPlayer().getMana()- GameState.getCurrentPlayer().getCard(CardClicked.getHandPosition()).getManacost());
			CardClicked.clearHighlighted();
			Gui.removeHighlightTiles(out, GameState.getBoard());
			GameState.getCurrentPlayer().removeFromHand(CardClicked.getHandPosition()); // remove card from hand
		} else {
			BasicCommands.addPlayer1Notification(out, "Invalid selection!", 5);
			CardClicked.clearHighlighted();
			Gui.removeHighlightTiles(out, GameState.getBoard());
		}
	}	
	
	
}			

