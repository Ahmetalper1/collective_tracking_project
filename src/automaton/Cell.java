package automaton;

import javax.swing.*;

import java.awt.*;

/*
 * This class represents a cell of the grid. It is used in the CellGrid class,
 * which contains a two dimensional array of Cells. Cell class contains a
 * boolean describing the state of the cell (alive/dead), and a button which
 * will be displayed on the grid to the user.
 */
public class Cell
{
	/*
	 * The colors array represents the color the blocks can take on the grid
	 * viewed by the user. The colorNB int is used to make a modulo in case
	 * the number of existing blocks exceeds 6.
	 */
	public static Color[] colors =
			{
					Color.MAGENTA,
					Color.GREEN,
					Color.CYAN,
					Color.YELLOW,
					Color.BLUE,
					Color.RED,
			};
	public static int colorNB = 6;
	
	// Level of the automaton containing the cell.
	private AutomatonLevel level;
	
	// State of the cell: if the cell is alive or not.
	private boolean isON;
	
	// It is the graphic that is displayed via the CellGrid.
	private JButton graphic;
	
	// This is the block number of the cell.
	private int blockID;
	
	// This is the block number of the cell at the previous iteration.
	private int previousBlockID;
	
	// The current point of the cell, containing its coordinates on the grid.
	private Point point;
	
	// Constructor setting up the state and the visual of the cell.
	public Cell(boolean isON, Point point, AutomatonLevel level)
	{
		// Updating the state.
		this.isON = isON;
		
		// By default, the cell is not part of any block.
		blockID = -1;
		previousBlockID = -1;
		
		// Setting the point with the given one.
		this.point = point;
		
		// Setting up the level.
		this.level = level;
		
		/*
		 *  Creating a new button. If the initial state of the cell is alive,
		 *  we have to set the button to alive, which is setting its color
		 *  to blue.
		 */
		graphic = new JButton();
		if (isON)
			graphic.setBackground(Color.MAGENTA);
		else
			graphic.setBackground(Color.GRAY);
		
		// We make here the button not clickable.
		graphic.setEnabled(false);
	}
	
	/*
	 * This will update the state of the cell. Setting it up to alive or dead
	 * from the boolean given.
	 */
	public void updateState(boolean state)
	{
		if (state)
		{
			this.isON = true;
			if (blockID != -1)
			{
				graphic.setBackground(Cell.colors[blockID % Cell.colorNB]);
			}
			else
			{
				graphic.setBackground(Color.MAGENTA);
			}
		}
		else
		{
			this.isON = false;
			graphic.setBackground(Color.GRAY);
		}
	}
	
	public JButton getGraphic()
	{
		return this.graphic;
	}
	
	public boolean isAlive()
	{
		return this.isON;
	}
	
	public int getBlock()
	{
		return this.blockID;
	}
	
	public int getPreviousBlock()
	{
		return this.previousBlockID;
	}
	
	public void setBlock(int blockID, boolean overridePrevious)
	{
		if (!overridePrevious)
		{
			// Updating the previous block and the current one.
			this.previousBlockID = this.blockID;
		}
		else
		{
			this.previousBlockID = blockID;
		}
		
		this.blockID = blockID;

	}
	
	public Point getPoint()
	{
		return this.point;
	}
	
	public AutomatonLevel getLevel()
	{
		return this.level;
	}
}
