package automaton;

import java.awt.*;

/*
 * This class represents the grid of an automaton. It contains informations
 * about the state of the automaton as well as the rules used.
 */
public class CellGrid
{
	// The rules used by this grid.
	private Rules rules;
	
	// All the cells of the grid, true meaning it's alive and false dead.
	private Cell[][] cells;
	
	// Size of the grid.
	private Dimension size;
	
	// The related view that will display the grid.
	private View view;
	
	// Automaton level.
	private AutomatonLevel level;
	
	// Constructor of the class.
	public CellGrid(Dimension size, boolean[][] cells, Rules rules,
			AutomatonLevel level)
	{
		// Setting the attributes.
		this.level = level;
		this.rules = rules;
		this.size = size;
		
		/*
		 *  Creating the Cell[][] array, and instantiating every Cell object
		 *  with the given cells boolean array.
		 */
		this.cells = new Cell[size.height][size.width];
		for (int i = 0; i < size.height; i++)
		{
			for (int j = 0; j < size.width; j++)
			{
				this.cells[i][j] =
						new Cell(cells[i][j], new Point(j, i), level);
			}
		}
		
		// Instantiating the view used to display the automaton.
		this.view = new View(this.size, this.cells, level);
	}
	
	// Return the state of cell[i][j] as an integer.
	public int getState(int i, int j)
	{
		if (this.cells[i][j].isAlive())
			return 1;
		return 0;
	}
	
	/*
	 * This updates the grid of cells.
	 */
	public void updateCells(boolean[][] newCells)
	{
		for (int i = 0; i < size.height; i++)
		{
			for (int j = 0; j < size.width; j++)
			{
				this.cells[i][j].updateState(newCells[i][j]);
			}
		}
		
		// Updating the frame so it displays the potential changes.
		view.updateFrame();
	}
	/*
	 * This functions displays the grid.
	 */
	public void display(int widthOffset, int heightOffset, String title)
	{
		this.view.getFrame().setTitle(title);
		
		// Updating the position.
		this.view.getFrame().setLocation(widthOffset, heightOffset);
		
		// Displaying the grid.
		this.view.getFrame().setVisible(true);
	}
	
	public Cell[][] getCells()
	{
		return this.cells;
	}
	
	public Rules getRules()
	{
		return this.rules;
	}
	
	public AutomatonLevel getLevel()
	{
		return this.level;
	}
}
