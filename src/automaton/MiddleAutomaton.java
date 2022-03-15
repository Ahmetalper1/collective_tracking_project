package automaton;

import java.awt.Dimension;

import abstraction.ComputeMiddle;

import java.util.ArrayList;

/*
* This class represents the middle automaton, which is basically the exact same
* as the bottom one but with a bigger granularity.
*/
public class MiddleAutomaton extends AbstractAutomaton
{	
	// Threshold (in percent) use by the automaton.
	private int threshold;
	
	// Unique ID of the automaton.
	private int ID;
	
	// Constructor of the automaton.
	public MiddleAutomaton(Dimension dim, boolean[][] cells, int threshold,
			int ID)
	{
		// Middle level and given dimension, as well as its ID.
		this.level = AutomatonLevel.MIDDLE;
		this.size = dim;
		this.ID = ID;

		// Setting up the threshold.
		this.threshold = threshold;
		
		// Setting up the sub automata, the bottom one.
		this.subAutomata = new ArrayList<AbstractAutomaton>();

		// Updating the super automata.
		this.superAutomata = new ArrayList<AbstractAutomaton>();

		/*
		 * Setting up the cell grid. The rules are null because this automaton
		 * is only getting input from the bottom one and not computing itself.
		 */
		this.cellGrid = new CellGrid(dim, cells, null, AutomatonLevel.MIDDLE);
	}
	
	/*
	 * This function adds an automaton as a sub automaton of the current 
	 * one. This means the automaton 'auto' is one level under.
	 */
	public void addSubAutomaton(AbstractAutomaton auto)
	{
		this.subAutomata.add(auto);
	}
	
	/*
	 * This function adds an automaton as a super automaton of the current 
	 * one. This means the automaton 'auto' is one level above.
	 */
	public void addSuperAutomaton(AbstractAutomaton auto)
	{
		this.superAutomata.add(auto);
	}
	
	/*
	 * This function updates the state of the middle automaton
	 * thanks to the informations retrieved from the bottom automaton.
	 */
	public void updateCells()
	{
		/*
		 * This is retrieving the cells from the subAutomaton
		 * (the bottom automaton).
		 */
		boolean[][] tempCells = new boolean[this.subAutomata.get(0).size.height]
				[this.subAutomata.get(0).size.width];
		
		for (int i = 0; i < this.subAutomata.get(0).size.height; i++)
		{
			for (int j = 0; j < this.subAutomata.get(0).size.width; j++)
			{
				tempCells[i][j] = this.subAutomata.get(0).cellGrid
						.getState(i, j) == 1;
			}
		}
		
		// We apply the abstraction function to the input to get our new cells.
		boolean[][] newMiddleCells =
				ComputeMiddle.apply(threshold, tempCells, this);
		
		// We update our cells with the results of the abstraction.
		this.cellGrid.updateCells(newMiddleCells);
	}
	
	public CellGrid getGrid()
	{
		return this.cellGrid;
	}
	
	public int getThreshold()
	{
		return this.threshold;
	}
	
	public int getID()
	{
		return this.ID;
	}
}
