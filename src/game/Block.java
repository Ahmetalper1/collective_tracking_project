package game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;

import automaton.Cell;
import logger.LoggerInstance;
import utils.TickHistory;

/*
 * The block class represents a set of cells that are connected (neighbors).
 * A block contains cells from the bottom automaton. It has a center, which is
 * the average of every cell's X and Y position. It also keeps trace of its
 * previous centers, to be able to draw and/or calculate the shape of its
 * movement.
 */
public class Block
{
	// List of the cells of the block.
	private List<Cell> cells;
	
	// Unique block ID.
	private int ID;
	
	// Type of block (Bottom or Middle) and ID of the automaton (index in the Game list).
	private BlockType type;
	
	/*
	 *  The automatonID is only used for Middle Blocks when we have to detect
	 *  from which automaton the block comes from, to display it on the right
	 *  graph.
	 */
	private int automatonID;
	
	// Default automaton ID for bottom blocks (only 1 bottom automaton..).
	public static int BOTTOM_BLOCK_AUTOMATON_ID = -1;
	
	/*
	 * Tells us if the block life has ended (merged into another one or divided
	 * into two or more blocks).
	 */
	private boolean active = true;
	
	/*
	 *  Current center of the block. It's the average X and Y coordinates
	 *  of all the cells.
	 */
	private Point center;
	
	/*
	 * The Xhistory and Yhistory lists keep trace of the X and Y coordinates
	 * the center of the block had in the past. Its length is the number of
	 * iterations since the creation of the block.
	 */
	private List<Double> Xhistory;
	private List<Double> Yhistory;
	
	private TickHistory tickHistory;
	
	
	//ada added
	//function approximating the xy-history points
	private PolynomialFunction blockTrajectoryFunction;
	

	// Constructor taking the unique ID and the first cell of the block.
	public Block(int ID, Cell firstBlockCell, BlockType type, int automatonID)
	{
		LoggerInstance.LOGGER.log(Level.FINEST, "New block found ! ID : " + ID);
		
		// Assigning the ID.
		this.ID = ID;
		
		// Assigning type.
		this.type = type;
		this.automatonID = automatonID;
		
		// Creating the cells list and adding it the first cell.
		cells = new ArrayList<Cell>();
		cells.add(firstBlockCell);
		
		// Instantiating the center point.
		this.center = new Point(0, 0);
		
		// Creating the empty history.
		Xhistory = new ArrayList<Double>();
		Yhistory = new ArrayList<Double>();
		
		this.tickHistory = new TickHistory();
	}
	
	// Adds a cell to the block.
	public void addCell(Cell cell)
	{
		this.cells.add(cell);
	}
	
	// Removes a cell from the block.
	public void removeCell(Cell cell)
	{
		this.cells.remove(cell);
	}
	
	// This function updates the center after its cells have been updated.
	public void updateCenter(int tick)
	{
		// This will contain the sum of every cell coordinates.
		int allXPositions = 0;
		int allYPositions = 0;
		
		// Making the sum of every cell coordinates.
		for (Cell cell : cells)
		{
			// Adding the X coord.
			allXPositions += cell.getPoint().x;
			
			/*
			 *  We are doing this subtraction because in the grid, the row 0 is
			 *  the top one. In fact when we will display history on a graph,
			 *  we will need the row 0 to be the bottom one. This manipulation
			 *  inverses the Y axis.
			 */
			allYPositions += Game.BOTTOM_ROW - cell.getPoint().y;
		}
		
		if (cells.size() != 0 || type == BlockType.BOTTOM)
		{
			// Updating the current center of the block.
			center.x = allXPositions / cells.size();
			center.y = allYPositions / cells.size();
		}
		
		tickHistory.FillMissingTicks(tick - 1);
		tickHistory.addTick(Double.valueOf(center.x), Double.valueOf(center.y));
		
		// If the point already exists in the history, no need to add it again.
		//Ada-TODO: consider commenting this out: it can be interesting to add same point several times when analysing trajectory
		/*
		for (int i = 0; i < Xhistory.size(); i++)
		{
			if (Xhistory.get(i) == center.x && Yhistory.get(i) == center.y)
				return;
		}
		*/
		
		// Adding the current center to the history.
		Xhistory.add(Double.valueOf(center.x));
		Yhistory.add(Double.valueOf(center.y));
	}
	
	public List<Cell> getCells()
	{
		return this.cells;
	}
	
	public List<Double> getXHistory()
	{
		return this.Xhistory;
	}
	
	public List<Double> getYHistory()
	{
		return this.Yhistory;
	}
	
	public int getID()
	{
		return this.ID;
	}
	
	public boolean getStatus()
	{
		return this.active;
	}
	
	public void setStatus(boolean newStatus)
	{
		this.active = newStatus;
	}
	
	public TickHistory getTickHistory()
	{
		return this.tickHistory;
	}
	
	public int getAutomatonID()
	{
		return this.automatonID;
	}
	
	public BlockType getType()
	{
		return this.type;
	}
	
	public PolynomialFunction getBlockTrajectoryFunction() {
		return blockTrajectoryFunction;
	}

	public void setBlockTrajectoryFunction(PolynomialFunction blockTrajectoryFunction) {
		this.blockTrajectoryFunction = blockTrajectoryFunction;
	}
	
	public void shiftXofHistoryPoint(int xIndex, Double shiftVal) {
		double shiftedXvalue = this.Xhistory.get(xIndex);
		shiftedXvalue = shiftedXvalue + shiftVal;
		this.Xhistory.set(xIndex, shiftedXvalue);
	}
	
}

