package structures.basic.SpecialUnits;

import structures.basic.Unit; 
/**
 * This class represents the units that can attack twice per turn 
 */
public abstract class AttackTwice extends Unit {
	private int attackCount = 0;
	private boolean attacked;
	
	public int getAttackCount() {
		return attackCount;
	}
	
	/**
	 * Overriding Unit setAttacked method
	 */
	
	public void setAttacked() {
		attacked = false; 
		attackCount+=1; //increase attack count after each attack
	}
	
	/**
	 * set attacked to true, once the unit has attacked twice
	 * @return attacked
	 */
	public boolean hasAttacked() {
		if(attackCount == 2) { 
			attacked = true;
		}
		return this.attacked;
	}
	/**
	 * reset attacked
	 */
	public void clearAttacked() {
		this.attacked = false;
		this.attackCount = 0;
	}
	/**
	 * set attack count 
	 */
	public void setAttackCount() {
		this.attackCount = 2;
	}

}
