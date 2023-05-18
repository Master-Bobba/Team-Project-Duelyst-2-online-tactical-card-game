package game.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


import akka.actor.ActorRef;

import commands.BasicCommands;
import events.CardClicked;
import structures.GameState;
import structures.basic.SpecialUnits.*;
import structures.basic.Player;

import structures.basic.SpecialUnits.Provoke;
import structures.basic.SpecialUnits.SilverguardKnight;
import structures.basic.SpecialUnits.*;

import structures.basic.SpecialUnits.FireSpitter;
import structures.basic.SpecialUnits.Pyromancer;
import structures.basic.SpecialUnits.RangedAttack;

import structures.basic.SpecialUnits.Windshrike;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.Position;

import structures.basic.UnitAnimationType;

import structures.basic.BigCard;
import structures.basic.Card;
import structures.basic.UnitAnimationSet;
import structures.basic.EffectAnimation;
import structures.basic.Playable;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

public class Utility {

    /**
     * This class is the utility class where methods with some main logic of the game will be provided
     * Most of these methods are shared between the human and the AI player.
     */
    private static ActorRef out;


    public Utility(ActorRef out) {
        Utility.out = out;
    }

    /**
     * Determines and returns a set of tiles where a unit can attack
     *
     * @param tile
     * @param positions
     * @param enemy
     * @param board
     * @return Set<Tile>
     * 
     * @author Bozhidar Ayvazov
     */
    public static Set<Tile> determineTargets(Tile tile, Set<Tile> positions, Player enemy, Tile[][] board) {

        // Using Set so that the Tile Objects do not repeat for the last condition
        Set<Tile> validAttacks = new HashSet<>();

        if (tile.getOccupier() instanceof RangedAttack) {
            System.out.println("Determine Target - Ranged Attack");
            if (tile.getOccupier().hasAttacked()) {
                return null;
            } else if (!tile.getOccupier().hasAttacked()) {
                return RangedAttack.specialAbility(board);
            }
        }
        // this will be used when the unit is disabled as is right next to a provoke unit
        if (positions == null && !checkProvoker(tile).isEmpty()) {
            Set<Position> p = checkProvoker(tile);
            for (Position pos : p)
                validAttacks.add(board[pos.getTilex()][pos.getTiley()]);
            return validAttacks;
        }


        // Has Attacked already
        if (tile.getOccupier().hasAttacked()) {
            return null;

            // Has moved but has not attacked - consider only the current position
        } else if (tile.getOccupier().hasMoved() && !tile.getOccupier().hasAttacked()) {
            validAttacks.addAll(getValidTargets(tile, enemy, board));

            // Has not moved nor attacked - consider all possible movements as well.
        } else if (!tile.getOccupier().hasMoved() && !tile.getOccupier().hasAttacked()) {
            System.out.println("has NOT moved NOR attacked");
            	validAttacks.addAll(getValidTargets(tile, enemy, board));
            for (Tile position : positions) {
                validAttacks.addAll(getValidTargets(position, enemy, board));
                
            }
            
        }
        return validAttacks;
    }

    /**
     * Checks if provoke unit is present on the board and around the tile on which
     * an alleged enemy unit (target) is located
     *
     * @param tile
     * @return Set<Position>
     * 
     * @author Bozhidar Ayvazov
     */
    public static Set<Position> checkProvoker(Tile tile) {

        Set<Position> provoker = new HashSet<>();

        for (Unit unit : GameState.getOtherPlayer().getUnits()) {

            int tilex = tile.getTilex();
            int tiley = tile.getTiley();

            if (Math.abs(tilex - unit.getPosition().getTilex()) < 2 && Math.abs(tiley - unit.getPosition().getTiley()) < 2) {
            	if(unit.getId() == 3 || unit.getId() == 10 || unit.getId() == 6 || unit.getId() == 16 || unit.getId() == 20 || unit.getId() == 30) {
                    System.out.println("Provoker in the house");
                    provoker.add(unit.getPosition());
                }
            }
        }
        return provoker;
    }

    /**
     * Determines the neighbouring tiles on which enemy units that can be attacked are positioned.
     * Returns only the tiles with units that are 1 tile away from the provided tile
     *
     * @param tile
     * @param enemy
     * @param board
     * @return Set<Tile>
     * 
     * @author Bozhidar Ayvazov
     */
    public static Set<Tile> getValidTargets(Tile tile, Player enemy, Tile[][] board) {

        Set<Tile> validAttacks = new HashSet<>();

        Set<Position> provoker = checkProvoker(tile);
        
        if (!provoker.isEmpty()) {
            for (Position pos : provoker) {
                validAttacks.add(board[pos.getTilex()][pos.getTiley()]);
            }
            return validAttacks;
        }
        
        for (Unit unit : enemy.getUnits()) {
            int unitx = unit.getPosition().getTilex();
            int unity = unit.getPosition().getTiley();

            if (Math.abs(unitx - tile.getTilex()) < 2 && Math.abs(unity - tile.getTiley()) < 2) {
                validAttacks.add(board[unitx][unity]);
            }
        }
        return validAttacks;
    }


    /**
     * Performs the adjacent attack
     *
     * @param attacker
     * @param defender
     * @return
     * 
     * @author Bozhidar Ayvazov
     */
	public static void adjacentAttack(Unit attacker, Unit defender) {
	
		if (!attacker.hasAttacked()) {
						
			Gui.performAttack(attacker);
			
			BasicCommands.playUnitAnimation(out, defender, UnitAnimationType.hit);
			try {Thread.sleep(750);} catch (InterruptedException e) {e.printStackTrace();}
			BasicCommands.playUnitAnimation(out, attacker, UnitAnimationType.idle);
			BasicCommands.playUnitAnimation(out, defender, UnitAnimationType.idle);
			
			defender.setHealth(defender.getHealth()-attacker.getAttack());
			
			// check if silverguard Knight, one or more, are on the board
			checkSilverKnight(defender);
			
			Gui.setUnitStats(defender, defender.getHealth(), defender.getAttack());
			
			attacker.setAttacked(); 
			
			checkEndGame(defender);
			counterAttack(attacker, defender);
		}
	}
	
	/**
	 * Checks if a SilverGuard Knight(SK) is present on the board;
	 * If the damdaged unit is the Human's avatar then buff all friendly SK units
	 * 
	 * @param unit
     * 
     * @author Bozhidar Ayvazov
	 */
	public static void checkSilverKnight(Unit defender) {
		if(defender.getId() == 100) {
			for (Unit  unit: GameState.getHumanPlayer().getUnits()) {
				if (unit instanceof SilverguardKnight && GameState.getHumanPlayer().getUnits().contains(unit)) {
					((SilverguardKnight) unit).buffAttack();
				}
			}
		}
	}
	
	/**
	 * Performs the distances attack by determining the best position to move to perform the attack,
	 * moves to that position and performs adjacenet attack.
	 * Alternatively will only perform ranged attack if that ability is available
	 * @param attacker
     * @param defender
     * 
     * @author Bozhidar Ayvazov
	 */
	public static void distancedAttack(Unit attacker, Unit defender) {
        System.out.println("Distanced Attack Activated");

        if (!attacker.hasAttacked() && attacker instanceof RangedAttack) {
            Gui.performAttack(attacker);
            BasicCommands.playUnitAnimation(out, attacker, UnitAnimationType.idle);

            EffectAnimation projectile = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_projectiles);
            //try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
            BasicCommands.playProjectileAnimation(out, projectile, 0, GameState.getBoard()[attacker.getPosition().getTilex()][attacker.getPosition().getTiley()], GameState.getBoard()[defender.getPosition().getTilex()][defender.getPosition().getTiley()]);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            BasicCommands.playUnitAnimation(out, defender, UnitAnimationType.hit);
            try {
                Thread.sleep(750);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            BasicCommands.playUnitAnimation(out, attacker, UnitAnimationType.idle);
            BasicCommands.playUnitAnimation(out, defender, UnitAnimationType.idle);

            // check if silverguard Knight, one or more, are on the board
            checkSilverKnight(defender);

            defender.setHealth(defender.getHealth() - attacker.getAttack());
            Gui.setUnitStats(defender, defender.getHealth(), defender.getAttack());


            attacker.setAttacked(); // commented out to test that unit dies

            checkEndGame(defender);
            counterAttack(attacker, defender);
        }

        if (!attacker.hasAttacked() && !attacker.hasMoved()) {

            // Get the valid tiles from which the unit can be attacked
            GameState.validMoves = Utility.determineValidMoves(GameState.getBoard(), attacker); // THATS NEEDED FOR THE AI
            ArrayList<Tile> validTiles = getValidAttackTiles(defender);

            int minScore = Integer.MAX_VALUE;
            Tile closestTile = null;

            // Find the closest/optimal position to attack from by scoring each option
            for (Tile tile : validTiles) {
                int score = 0;
                score += Math.abs(tile.getTilex() - attacker.getPosition().getTilex());
                score += Math.abs(tile.getTiley() - attacker.getPosition().getTiley());
                if (score < minScore && tile.getOccupier() == null) {
                    minScore = score;
                    closestTile = tile;
                }

            }
            // move unit to the closest tile
            if (closestTile != null) {
                System.out.println("The closest tile is: x = " + closestTile.getTilex() + " and y = " + closestTile.getTiley() + " score " + minScore);
                moveUnit(attacker, closestTile);
                if (minScore < 2) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                adjacentAttack(attacker, defender);
            }
        }
    }
	
	/**
     * Gets the valid attack positions for distanced attacks (move first and then attack)
     * Determines from which positions a unit can be attacked
     * @param unit
     * @return ArrayList<Tile>
     * 
     * @author Bozhidar Ayvazov
     * 
     */
    public static ArrayList<Tile> getValidAttackTiles(Unit unit) {
        ArrayList<Tile> validTiles = new ArrayList<>();

        for (Tile tile : GameState.validMoves) {
            int unitx = unit.getPosition().getTilex();
            int unity = unit.getPosition().getTiley();
            if (Math.abs(unitx - tile.getTilex()) < 2 && Math.abs(unity - tile.getTiley()) < 2) {
                validTiles.add(tile);
            }

        }
        for (Tile tile : validTiles) {
            System.out.println("tile: x = " + tile.getTilex() + " and y = " + tile.getTiley());
        }
        return validTiles;
    }
    
    /**
     * This places a unit on the board, deciding which special unit class to instansite, if any
     * and then sets various stats on the GUI and unit class.
     * @param out
     * @param card
     * @param player
     * @param tile
     * @author Daniel
     */
    public static void placeUnit(ActorRef out, Card card, Player player, Tile tile) {
        String unit_conf = StaticConfFiles.getUnitConf(card.getCardname());
        int unit_id = card.getId();
        Unit unit;

        if (card.getCardname().equals("Silverguard Knight")) {
        	unit = (SilverguardKnight) BasicObjectBuilders.loadUnit(unit_conf, unit_id, SilverguardKnight.class);
        } else if (card.getCardname().equals("Ironcliff Guardian")) {
        	unit = (IroncliffGuardian) BasicObjectBuilders.loadUnit(unit_conf, unit_id, IroncliffGuardian.class);
        } else if (card.getCardname().equals("Rock Pulveriser")) {
        	unit = (RockPulveriser) BasicObjectBuilders.loadUnit(unit_conf, unit_id, RockPulveriser.class);
        } else if (card.getCardname().equals("Fire Spitter")) {
            unit = (FireSpitter) BasicObjectBuilders.loadUnit(unit_conf, unit_id, FireSpitter.class);
        } else if (card.getCardname().equals("Pyromancer")) {
            unit = (Pyromancer) BasicObjectBuilders.loadUnit(unit_conf, unit_id, Pyromancer.class);
        } else if (card.getCardname().equals("Azure Herald")) {
            unit = (AzureHerald) BasicObjectBuilders.loadUnit(unit_conf, unit_id, AzureHerald.class);
            AzureHerald.specialAbility(GameState.getCurrentPlayer().getUnits().get(0));
        } else if (card.getCardname().equals("Blaze Hound")) {
            unit = (BlazeHound) BasicObjectBuilders.loadUnit(unit_conf, unit_id, BlazeHound.class);
            BlazeHound.specialAbility();
        } else if (card.getCardname().equals("Serpenti")) {
            unit = (Serpenti) BasicObjectBuilders.loadUnit(unit_conf, unit_id, Serpenti.class);
        } else if (card.getCardname().equals("Azurite Lion")) {
            unit = (AzuriteLion) BasicObjectBuilders.loadUnit(unit_conf, unit_id, AzuriteLion.class);
        } else if (unit_id == 34 || unit_id == 24) {
            unit = (Windshrike) BasicObjectBuilders.loadUnit(unit_conf, unit_id, Windshrike.class);
        } else if (unit_id == 1 || unit_id == 13) {
            unit = (Pureblade) BasicObjectBuilders.loadUnit(unit_conf, unit_id, Pureblade.class);
        } else {
            unit = BasicObjectBuilders.loadUnit(unit_conf, unit_id, Unit.class);
        }

        unit.setPositionByTile(tile);
        unit.setMaxHealth(unit.getHealth());
        tile.setOccupier(unit);
        GameState.modifiyTotalUnits(1);

        // Plays annimations
        EffectAnimation effect = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon);
        BasicCommands.playEffectAnimation(out, effect, tile);
        BasicCommands.drawUnit(out, unit, tile);

        BigCard bigCard = card.getBigCard();
        int attack = bigCard.getAttack();
        int health = bigCard.getHealth();
        unit.setMaxHealth(health);

        //Sets unit stats
        unit.setAttack(attack);
        unit.setHealth(health);

        unit.setAttacked();
        unit.setMoved();

        /*
         Checks if unit has double attack modifier 
        */

        if (unit_id == 36 || unit_id == 26 || unit_id == 17 || unit_id == 7)
            ((AttackTwice) unit).setAttackCount();

        GameState.modifiyTotalUnits(1);

        Gui.setUnitStats(unit, health, attack);
        GameState.getCurrentPlayer().setUnit(unit);

        int positionInHand = card.getPositionInHand();
        player.removeFromHand(positionInHand);

        if (player.equals(GameState.getHumanPlayer()))
            BasicCommands.deleteCard(out, positionInHand);

        player.updateMana(-card.getManacost());
        CardClicked.currentlyHighlighted.remove(card);

        if (GameState.getHumanPlayer() == player) {
            BasicCommands.setPlayer1Mana(out, player);
        } else {
            BasicCommands.setPlayer2Mana(out, player);
        }
    }

    /**
     *  This uses set logic to calculate and return the valid placements for a unit card
     *  Handles logic for airdrop
     * @param card
     * @param player
     * @param enemy
     * @param board
     * @return Set<Tile>
     * @author Daniel
     */
    public static Set<Tile> cardPlacements(Card card, Player player, Player enemy, Tile[][] board) {

        System.out.println("cardPlacement Utility");
        Set<Tile> validTiles = new HashSet<Tile>();
        int i, j;

        for (i = 0; i < board.length; i++) {
            for (j = 0; j < board[0].length; j++) {
                validTiles.add(board[i][j]);
            }
        }

        Set<Tile> playerUnits = getPlayerUnitPositions(player, board);
        Set<Tile> enemyUnits = getEnemyUnitPositions(enemy, board);

        /* if card can be played on all squares, return the board - occupied squares */
        if (card.getMoveModifier()) {
            validTiles.removeAll(playerUnits);
            validTiles.removeAll(enemyUnits);
            return validTiles;
        }

        int x, y;

        Set<Tile> validPlacements = new HashSet<Tile>();

        /* Add squares around player units to set. Return this minus occupied squares */
        for (Tile tile : playerUnits) {
            x = tile.getTilex();
            y = tile.getTiley();
            i = -1;
            j = -1;
            for (i = -1; i <= 1; i++) {
                for (j = -1; j <= 1; j++) {
                    if (x + i > 8 || y + j > 4 || x + i < 0 || y + j < 0)
                        continue;
                    validPlacements.add(board[x + i][y + j]);
                }
            }
        }

        validPlacements.removeAll(playerUnits);
        validPlacements.removeAll(enemyUnits);
        return validPlacements;
    }

    /**
     * returns a set of tiles which have player units on them
     * @param player
     * @param board
     * @return Set<Tile> 
     * @author Daniel
     */
    public static Set<Tile> getPlayerUnitPositions(Player player, Tile[][] board) {

        System.out.println("getting player unit positions");

        Set<Tile> s = new HashSet<Tile>();

        for (Unit u : GameState.getCurrentPlayer().getUnits()) {
            /* Add unit to set of player positions */
            s.add(GameState.getBoard()[u.getPosition().getTilex()][u.getPosition().getTiley()]);
        }
        return s;

    }
    
    /**
     * Returns a set of tiles which contain enemy units
     * @param enemy
     * @param board
     * @return Set<Tile>
     * @author Daniel
     */
    public static Set<Tile> getEnemyUnitPositions(Player enemy, Tile[][] board) {
        Set<Tile> s = new HashSet<Tile>();
        ArrayList<Unit> uList = enemy.getUnits();
        for (Unit unit : uList) {
            /* Add unit to set of enemy positions */
            s.add(board[unit.getPosition().getTilex()][unit.getPosition().getTiley()]);
        }
        return s;
    }
    
    /**
     * checks if a player can play a card on a given tile. returns boolean value
     * @param out
     * @param card
     * @param player
     * @param enemy
     * @param tile
     * @param board
     * @return boolean
     * @author Daniel
     */
    public static boolean validMove(ActorRef out, Card card, Player player, Player enemy, Tile tile, Tile[][] board) {
        if (card.getManacost() > player.getMana()) {
            return false;
        }
        Set<Tile> s = cardPlacements(card, player, enemy, board);
        if (s.contains(tile)) {
            return true;
        }
        return false;
    }

    /**
     * this function returns valid card placements so it can be passed to the board for highlighting
     * @param card
     * @param player
     * @param enemy
     * @param board
     * @return Set<Tile>
     */
    public static Set<Tile> showValidMoves(Card card, Player player, Player enemy, Tile[][] board) {
        Set<Tile> s = cardPlacements(card, player, enemy, board);
        return s;
    }

	/**
	 * This method will be called after a unit is attacked to check if the unit's health is <= 0 and therefore dead.
	 * This method ends the game if the killed unit is an avatar. 
	 * @param Unit  the Unit that was attacked
	 *
	 */
    public static void checkEndGame(Unit defender) {
        //unit death
        System.out.println(GameState.enemy.getHealth());
        if (defender.getHealth() <= 0) {
            BasicCommands.playUnitAnimation(out, defender, UnitAnimationType.death);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            GameState.board[defender.getPosition().getTilex()][defender.getPosition().getTiley()].setOccupier(null); //remove unit from tiles
            BasicCommands.deleteUnit(out, defender); //delete unit from board

            /*
             * Checks if unit is windshrike and applies the effect if so
             */
            if (defender.getClass().equals(Windshrike.class)) {
                GameState.enemy.drawCard();
            }

//		If unit is an AI unit
            if (GameState.getAiPlayer().getUnits().contains(defender)) {
                GameState.getAiPlayer().removeUnit(defender);

                if (GameState.getAiPlayer().getHealth() <= 0) {
                    BasicCommands.addPlayer1Notification(out, "Player 1 wins!", 20);
                    //game over:
                }

//		If unit belongs to the human player 
            } else if (GameState.getHumanPlayer().getUnits().contains(defender)) {
                GameState.getHumanPlayer().removeUnit(defender);
                if (GameState.getHumanPlayer().getHealth() <= 0) {
                    BasicCommands.addPlayer1Notification(out, "You lose!", 20);
                }
            }
        }
    }

	/**
	 * This method moves units on the board 
	 * @param Unit 	the unit to move 
	 * @param Tile 	the destination tile
	 */
    public static void moveUnit(Unit unit, Tile tile) {
	    if(!unit.hasMoved() && !unit.hasAttacked()) {
	        GameState.board[unit.getPosition().getTilex()][unit.getPosition().getTiley()].setOccupier(null); //clear unit from tile
	        
	        // Check if Y-movement will be necessary
	        if (checkDiagonalMovement(unit, tile))
	        	BasicCommands.moveUnitToTile(out, unit, tile, true);
	        else 
	        	BasicCommands.moveUnitToTile(out, unit, tile); //move unit to chosen tiles
	        
	        unit.setPositionByTile(tile); //change position of unit to new tiles
	
	        tile.setOccupier(unit); //set unit as occupier of tiles
	
	        unit.setMoved();
	        
	        Gui.removeHighlightTiles(out, GameState.board); //clearing board
	    } else {
	    	BasicCommands.addPlayer1Notification(out, "Unit cannot move again this turn", 2);
	    }

    }
    
    /**
     * Check if there is an enemy unit directly infront of directly behind the unit
     * If so will return true so that Y-movement is performed
     * 
     * @param unit
     * @return boolean
     * 
     * @author Bozhidar Ayvazov
     */
    public static Boolean checkYmovement(Unit unit) {
    	
    	int unitx = unit.getPosition().getTilex();
    	int unity = unit.getPosition().getTiley();
    	if ((unitx + 1) > 8 || (unitx -1)< 0)
    		return false;
    	if (GameState.getBoard()[unitx+1][unity].getOccupier() != null && GameState.getOtherPlayer().getUnits().contains(GameState.getBoard()[unitx+1][unity].getOccupier())) {
    		return true;
    	}
    	if (GameState.getBoard()[unitx-1][unity].getOccupier() != null && GameState.getOtherPlayer().getUnits().contains(GameState.getBoard()[unitx-1][unity].getOccupier())) {
    		return true;
    	}
    	
    	return false;
    	
    }
    
    /**
     * Check if the unit is to move diogonally
     * and if so will call checkYmovement() to check if y-movement will be necessary
     * @param unit
     * @param tile
     * @return boolean 
     * 
     * @author Bozhidar Ayvazov
     */
    public static boolean checkDiagonalMovement(Unit unit, Tile tile) {
    	
    	int unitx = unit.getPosition().getTilex();
    	int unity = unit.getPosition().getTiley();
    	
    	int score = 0;
    	
    	score += Math.abs(unitx - tile.getTilex());
    	score += Math.abs(unity - tile.getTiley());
    	
    	// if score = 2 then diogonal movement
    	if (score == 2) {
    		if(checkYmovement(unit))
    			return true;
    		else 
    			return false;
    	}
    	return false;
    	
    }
    
    /**
     * Check if the unit is neighbouring an enemy provoke unit
     * and disables the movement of the unit if so
     *  @param unit
     * 
     * @author Bozhidar Ayvazov
     */
    public static boolean checkProvoked(Unit unit) {

		for (Unit other : GameState.getOtherPlayer().getUnits()) {
		        	
        	int unitx = unit.getPosition().getTilex();
    		int unity = unit.getPosition().getTiley();
    		
    		if(other.getId() == 3 || other.getId() == 10 || other.getId() == 6 || other.getId() == 16 || other.getId() == 20 || other.getId() == 30) {
    			if (Math.abs(unitx - other.getPosition().getTilex()) <= 1 && Math.abs(unity - other.getPosition().getTiley()) <= 1) {
    				System.out.println("Unit is provoked!");
    				return true;
    			}
    		}

        }
        return false;
    }

    /**
     * Check all valid moves available to a unit.
     * Consider enemy and friendly unit and determines the moves appropriately
     * @param board
     * @param unit
     * @return Set<Tile>
     */
    public static Set<Tile> determineValidMoves(Tile[][] board, Unit unit) {

        Set<Tile> validTiles = new HashSet<>();
        
        if (checkProvoked(unit))
        	return null;   


        if (unit.getClass().equals(Windshrike.class) && !unit.hasMoved() && !unit.hasAttacked()) {
            return ((Windshrike) unit).specialAbility(board);
            

        } else if (!unit.hasMoved() && !unit.hasAttacked()) {
            int x = unit.getPosition().getTilex();
            int y = unit.getPosition().getTiley();

            // check one behind
            int newX = x - 1;
            if (newX > -1 && newX < board.length && board[newX][y].getOccupier() == null) {
                validTiles.add(board[newX][y]);
            }

            // if the nearby unit is a friendly unit, check the tile behind the friendly unit
            int newerX = newX - 1;
            if (newerX > -1 && newerX < board.length) {
                if (GameState.getCurrentPlayer().getUnits().contains(board[newX][y].getOccupier()) || board[newX][y].getOccupier() == null) {
                    //newX = x - 2;
                    if (board[newerX][y].getOccupier() == null) {
                        validTiles.add(board[newerX][y]);
                    }
                }
            }

            // check one ahead
            newX = x + 1;
            if (newX > -1 && newX < board.length && board[newX][y].getOccupier() == null) {
                validTiles.add(board[newX][y]);
            }
            // if one ahead is a friendly unit, check the tile ahead of the friendly unit
            newerX = newX + 1;
            if (newerX > -1 && newerX < board.length) {
                if (GameState.getCurrentPlayer().getUnits().contains(board[newX][y].getOccupier()) || board[newX][y].getOccupier() == null) {
                    // newX = x + 2;
                    if (board[newerX][y].getOccupier() == null) {
                        validTiles.add(board[newerX][y]);
                    }
                }
            }


            // check one up
            int newY = y - 1;
            if (newY > -1 && newY < board[0].length && board[x][newY].getOccupier() == null) {
                validTiles.add(board[x][newY]);
            }
            // if one up a friendly unit, check two up
            int newerY = newY - 1;
            if (newerY > -1 && newerY < board[0].length) {
                if (GameState.getCurrentPlayer().getUnits().contains(board[x][newY].getOccupier()) || board[x][newY].getOccupier() == null) {
                    //newY = y - 2;
                    if (board[x][newerY].getOccupier() == null) {
                        validTiles.add(board[x][newerY]);
                    }
                }
            }


            // check one down
            newY = y + 1;
            if (newY > -1 && newY < board[0].length && board[x][newY].getOccupier() == null) {
                validTiles.add(board[x][newY]);
            }
            // if one up a friendly unit, check two up
            newerY = newY + 1;
            if (newerY > -1 && newerY < board[0].length) {
                if (GameState.getCurrentPlayer().getUnits().contains(board[x][newY].getOccupier()) || board[x][newY].getOccupier() == null) {
                    //newY = y + 2;
                    if (board[x][newerY].getOccupier() == null) {
                        validTiles.add(board[x][newerY]);
                    }
                }
            }

            // diagonal tiles
            if (x + 1 < board.length && y + 1 < board[0].length && board[x + 1][y + 1].getOccupier() == null) {
                if (GameState.getCurrentPlayer().getUnits().contains(board[x + 1][y].getOccupier()) || board[x + 1][y].getOccupier() == null) {
                    validTiles.add(board[x + 1][y + 1]);
                } else if (GameState.getCurrentPlayer().getUnits().contains(board[x][y + 1].getOccupier()) || board[x][y + 1].getOccupier() == null) {
                    validTiles.add(board[x + 1][y + 1]);
                }
            }

            if (x - 1 >= 0 && y - 1 >= 0 && board[x - 1][y - 1].getOccupier() == null) {
                if (GameState.getCurrentPlayer().getUnits().contains(board[x - 1][y].getOccupier()) || board[x - 1][y].getOccupier() == null) {
                    validTiles.add(board[x - 1][y - 1]);
                } else if (GameState.getCurrentPlayer().getUnits().contains(board[x][y - 1].getOccupier()) || board[x][y - 1].getOccupier() == null) {
                    validTiles.add(board[x - 1][y - 1]);
                }
            }

            if (x + 1 < board.length && y - 1 >= 0 && board[x + 1][y - 1].getOccupier() == null) {
                if (GameState.getCurrentPlayer().getUnits().contains(board[x + 1][y].getOccupier()) || board[x + 1][y].getOccupier() == null) {
                    validTiles.add(board[x + 1][y - 1]);
                } else if (GameState.getCurrentPlayer().getUnits().contains(board[x][y - 1].getOccupier()) || board[x][y - 1].getOccupier() == null) {
                    validTiles.add(board[x + 1][y - 1]);
                }
            }

            if (x - 1 >= 0 && y + 1 < board[0].length && board[x - 1][y + 1].getOccupier() == null) {
                if (GameState.getCurrentPlayer().getUnits().contains(board[x - 1][y].getOccupier()) || board[x - 1][y].getOccupier() == null) {
                    validTiles.add(board[x - 1][y + 1]);
                } else if (GameState.getCurrentPlayer().getUnits().contains(board[x][y + 1].getOccupier()) || board[x][y + 1].getOccupier() == null) {
                    validTiles.add(board[x - 1][y + 1]);
                }
            }

        } else {
            // cannot move, return empty set
            return validTiles;
        }
        return validTiles;
    }

    /**
     * This allows a unit that's being attacked to counterattack.
     * Considers rangedAttack units and allows them to counterattack from the distance.
     * @param attacker
     * @param countAttacker
     * @return
     */
    public static void counterAttack(Unit attacker, Unit countAttacker) {
        int x = countAttacker.getPosition().getTilex();
        int y = countAttacker.getPosition().getTiley();

        int range = 1;

        if (countAttacker.getHealth() > 0) {
            if (countAttacker instanceof RangedAttack) {
                Gui.performAttack(countAttacker);

                BasicCommands.playUnitAnimation(out, attacker, UnitAnimationType.hit);
                BasicCommands.playUnitAnimation(out, attacker, UnitAnimationType.idle);
                try {
                    Thread.sleep(750);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                BasicCommands.playUnitAnimation(out, countAttacker, UnitAnimationType.idle);
                EffectAnimation projectile = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_projectiles);
                BasicCommands.playProjectileAnimation(
                        out, projectile, 0, GameState.getBoard()[countAttacker.getPosition().getTilex()][countAttacker.getPosition().getTiley()],
                        GameState.getBoard()[attacker.getPosition().getTilex()][attacker.getPosition().getTiley()]);

                // check if Silverguard Knight, one or more, are on the board
                checkSilverKnight(attacker);

                int newHealth = attacker.getHealth() - countAttacker.getAttack();
                attacker.setHealth(newHealth);
                Gui.setUnitStats(attacker, attacker.getHealth(), attacker.getAttack());

                checkEndGame(attacker);
                return;
            }
            for (int i = Math.max(0, x - range); i <= Math.min(GameState.board.length - 1, x + range); i++) {
                for (int j = Math.max(0, y - range); j <= Math.min(GameState.board[0].length - 1, y + range); j++) {
                    if (i == x && j == y) {
                        continue; // this is where the unit (countAttacker) is
                    } else if (attacker.getPosition().getTilex() == i & attacker.getPosition().getTiley() == j) {
                        Gui.performAttack(countAttacker);

                        BasicCommands.playUnitAnimation(out, attacker, UnitAnimationType.hit);
                        BasicCommands.playUnitAnimation(out, attacker, UnitAnimationType.idle);
                        try {
                            Thread.sleep(750);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        BasicCommands.playUnitAnimation(out, countAttacker, UnitAnimationType.idle);

                        // check if Silverguard Knight, one or more, are on the board
                        checkSilverKnight(attacker);

                        int newHealth = attacker.getHealth() - countAttacker.getAttack();
                        attacker.setHealth(newHealth);
                        Gui.setUnitStats(attacker, attacker.getHealth(), attacker.getAttack());

                        checkEndGame(attacker);
                    }
                }
            }
        }
    }

    /**
     * Get the tile positions of targeted units.
     * @param targets the units to get the tile positions of
     * @return set of tiles upon which the respective targets are located.
     */
    public static Set<Tile> getSpellTargetPositions(ArrayList<Unit> targets) {
        if (targets == null || targets.isEmpty()) return null; // if list of targets is null, then return no positions
        System.out.println(targets.size());
        Set<Tile> positions = new HashSet<>();

        for (Unit unit : targets) {
            if (unit == null) {
                System.out.println("Unit is null");
                continue;
            }
            int unitx = unit.getPosition().getTilex();
            int unity = unit.getPosition().getTiley();
            positions.add(GameState.getBoard()[unitx][unity]);
        }
        return positions;
    }
    /**
     * Converts the array of tile objects for the board to a set of tile objects
     * @param board
     * @return
     */
    public static Set<Tile> boardToSet(Tile[][] board) {
        Set<Tile> s = new HashSet<Tile>();
        for (Tile[] a : board) {
            s.addAll(Arrays.asList(a));
        }
        return s;
    }

}
