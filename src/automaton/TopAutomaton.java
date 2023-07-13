package automaton;

import java.awt.Dimension;
import java.util.ArrayList;

import abstraction.ComputeTop;

/*
* This class represents the top automaton. It gets input from the middle
* automaton and analyzes the data in order to produce a human readable output.
*/
public class TopAutomaton extends AbstractAutomaton
{
	private int threshold;
	
	// Constructor of the automaton.
	public TopAutomaton(Dimension dim, boolean[][] cells, int threshold)
	{
		// Top level and given dimension.
		this.level = AutomatonLevel.TOP;
		this.size = dim;
		
		// Setting up the abstraction of the automaton.
		this.threshold = threshold;

		// Setting up the sub automata, the middle one.
		this.subAutomata = new ArrayList<AbstractAutomaton>();

		// No super automata as the automaton is the top one.
		this.superAutomata = null;

		/*
		 * There is no need for a CellGrid for this automaton as it is the top
		 * one and its goal is only to analyze the data and not display or
		 * compute it.
		 */
		this.cellGrid = new CellGrid(dim, cells, null, AutomatonLevel.TOP);
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
	 * This function updates the state of the top automaton
	 * thanks to the informations retrieved from the middle automaton.
	 */
	public void updateCells()
	{
		/*
		 * This is retrieving the cells from the subAutomaton
		 * (the middle automaton).
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
		boolean[][] topCells = ComputeTop.apply(threshold, tempCells);
		
		// We update our cells with the results of the abstraction.
		this.cellGrid.updateCells(topCells);
	}
	
	public CellGrid getGrid()
	{
		return this.cellGrid;
	}
	
	public int getThreshold()
	{
		return this.threshold;
	}
}

