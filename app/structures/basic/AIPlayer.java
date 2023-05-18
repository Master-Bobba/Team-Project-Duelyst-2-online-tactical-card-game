package structures.basic;

import game.logic.Gui;
import structures.GameState;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

import java.util.NoSuchElementException;

import akka.actor.ActorRef;
import commands.BasicCommands;

/**
 * Class used to represent AI player.
 * Methods mostly come from player; overrides are made to prevent changes to GUI
 * (E.g, don't remove card from human player's deck when AI draws a card).
 *
 */

public class AIPlayer extends Player{

    public AIPlayer() {
        super(20, 0, 2);
        // Draw first three cards
        for(int i = 0; i < 3; i++) {
            drawCard();

        }
    }


    @Override
    public void setMana(int mana){
        this.mana = Math.min(mana, 9); // cap the max amount of mana a player can have at 9, as per GUI.
        Gui.displayAIMana(this);
    }
    @Override
    public void setHealth(int health){
        this.health = health;
        Gui.displayAIHP(this);
    }
    @Override // see method in Player
    public void drawCard() {
        if (deck.isEmpty()) throw new NoSuchElementException("Deck is empty");
        if (cardsInHand == 6) { // if no space in hand, card is lost
            deck.drawTopCard();
            return;
        }
        // if is space in hand, find first free spot in hand and place card from deck in it.
        int i = 0;
        while(hand[i] != null && i < hand.length - 1) {
            i++;
        }
        Card current = deck.drawTopCard();

        current.setPositionInHand(i + 1);

        hand[i] = current;
        cardsInHand++;
    }
    
    
    @Override // see method in Player
    public void removeFromHand(int position) { // remove a card from the hand at a given position
        hand[position-1] = null; // Set position to null to remove card. Use range 1 - 6 to reflect front-end display logic.
        cardsInHand--;
    }
    
    /**
	 * Creates the AI avatar. Sets their health and attack stats. Sets their position on the board.
	 * Displays the avatar and their stats on the board
	 * @param out
	 */
    public void createAvatar(ActorRef out) {
		Unit enemyAvatar = BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, 101, Unit.class);
		enemyAvatar.setPositionByTile(GameState.board[7][2]); 
		GameState.board[7][2].setOccupier(enemyAvatar);
		BasicCommands.drawUnit(out, enemyAvatar, GameState.board[7][2]);
		Gui.setUnitStats(enemyAvatar, 20, 2);
		enemyAvatar.setHealth(20);
		enemyAvatar.setAttack(2);
		GameState.modifiyTotalUnits(1);
		GameState.enemy.setUnit(enemyAvatar);
    }

}
