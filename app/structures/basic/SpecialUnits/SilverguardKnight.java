package structures.basic.SpecialUnits;

import game.logic.Gui;

/**
 * 
 * @author Bozhidar Ayvazov
 */

public class SilverguardKnight extends Provoke {
	
	@SuppressWarnings("unused")
	private String name = "SilverguardKnight";
	
	/**
	 * for special ability Avatar Damaged
	 */
	public void buffAttack() {
		System.out.println("BUFFED");
		
		this.setAttack(this.getAttack() + 2);
		
		Gui.setUnitStats(this, this.getHealth(), this.getAttack());
	}
}
