package automaton;

/*
 * This class represents the rules used by a CellGrid object. It corresponds
 * to the buttons of the menu GUI that the user checked or not.
 */
public class Rules
{
	// The size of the arrays will ALWAYS be 9, from 0 to 8 neighbors.
	boolean[] aliveRules;
	boolean[] deadRules;
	
	// Constructor of the class.
	public Rules(boolean[] aliveRules, boolean[] deadRules)
	{
		// Initiating the array by cloning give rules.
		this.aliveRules = aliveRules.clone();
		this.deadRules = deadRules.clone();
	}
	
	public boolean[] getAliveRules()
	{
		return this.aliveRules;
	}
	
	public boolean[] getDeadRules()
	{
		return this.deadRules;
	}
}
