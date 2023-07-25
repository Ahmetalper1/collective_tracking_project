package automaton;

import java.awt.Dimension;
import game.Game;
import utils.CheckCoords;

public class AutomatonUtils {

    public static void nextIteration(Dimension size, CellGrid cellGrid, Rules rules) {
        // Creating the new grid.
        boolean[][] nextCells = new boolean[Game.BOTTOM_ROW][Game.BOTTOM_COLUMN];

        /*
         * ada: applying the rules to the CA (to all cells) We are here verifying the
         * number of alive neighbors that each cell has. This will allow us to define
         * its next state thanks to the rules.
         */
        for (int i = 0; i < Game.BOTTOM_ROW; i++) {
            for (int j = 0; j < Game.BOTTOM_COLUMN; j++) {
                // Variable to count the number of alive neighbors.
                int neighbourCount = countNeighbors(i, j, cellGrid, size);

                /*
                 * This applies the correct rule to the cell. It also updates its blockID
                 * according to its neighbors.
                 */
                applyRules(i, j, neighbourCount, nextCells, cellGrid, rules);
            }
        }

        // This indicates to the grid that it needs to update.
        cellGrid.updateCells(nextCells);
    }
    
	/*
	 * This function count the number of alive neighbors for a cell at height i
	 * and width j.
	 */
    
    public static int countNeighbors(int i, int j, CellGrid cellGrid, Dimension size)  {
    	// Variable to count the number of alive neighbors.
    			int neighbourCount = 0;
    			
    			if (i == 0)
    			{
    				// Top Left corner cell.
    				if (j == 0)
    				{
    					neighbourCount += cellGrid.getState(i + 1, j)
    							+ cellGrid.getState(i, j + 1)
    							+ cellGrid.getState(i + 1, j + 1);
    				}
    				// Top Right corner cell.
    				else if (j == size.width - 1)
    				{
    					neighbourCount += cellGrid.getState(i + 1, j)
    							+ cellGrid.getState(i, j - 1)
    							+ cellGrid.getState(i + 1, j - 1);
    				}
    				// Top row cells.
    				else
    				{
    					neighbourCount += cellGrid.getState(i, j + 1)
    							+ cellGrid.getState(i, j - 1)
    							+ cellGrid.getState(i + 1, j)
    							+ cellGrid.getState(i + 1, j - 1)
    							+ cellGrid.getState(i + 1, j + 1);
    				}
    			}
    			else if (i == size.height - 1)
    			{
    				 // Down Left cell.
    				if (j == 0)
    				{
    					neighbourCount += cellGrid.getState(i - 1, j)
    							+ cellGrid.getState(i - 1, j + 1)
    							+ cellGrid.getState(i, j + 1);
    				}
    				// Down Right cell.
    				else if (j == size.width - 1)
    				{
    					neighbourCount += cellGrid.getState(i - 1, j)
    							+ cellGrid.getState(i, j - 1)
    							+ cellGrid.getState(i - 1, j - 1);
    				}
    				// Down row cells.
    				else
    				{
    					neighbourCount += cellGrid.getState(i, j + 1)
    							+ cellGrid.getState(i, j - 1)
    							+ cellGrid.getState(i - 1, j)
    							+ cellGrid.getState(i - 1, j - 1)
    							+ cellGrid.getState(i - 1, j + 1);
    				}
    			}
    			else
    			{
    				// First column cells.
    				if (j == 0)
    				{
    					neighbourCount += cellGrid.getState(i + 1, j)
    							+ cellGrid.getState(i + 1, j + 1)
    							+ cellGrid.getState(i - 1, j)
    							+ cellGrid.getState(i - 1, j + 1)
    							+ cellGrid.getState(i, j + 1);
    				}
    				// Last column cells.
    				else if (j == size.width - 1)
    				{
    					neighbourCount += cellGrid.getState(i + 1, j)
    							+ cellGrid.getState(i + 1, j - 1)
    							+ cellGrid.getState(i - 1, j)
    							+ cellGrid.getState(i - 1, j - 1)
    							+ cellGrid.getState(i, j - 1);
    				}
    				// Any cell in the middle of the automaton.
    				else
    				{
    					neighbourCount += cellGrid.getState(i + 1, j)
    							+ cellGrid.getState(i, j + 1)
    							+ cellGrid.getState(i + 1, j + 1)
    							+ cellGrid.getState(i - 1, j)
    							+ cellGrid.getState(i, j - 1)
    							+ cellGrid.getState(i - 1, j - 1)
    							+ cellGrid.getState(i + 1, j - 1)
    							+ cellGrid.getState(i - 1, j + 1);
    				}
    			}
    			
    			return neighbourCount;
    }
    
    /*
	 * This function applies the rules and updates the state of the cell to the
	 * cells array. The cell array representing obviously the next generation
	 * cells. This function also updates blockID of the cell.
	 */

    public static void applyRules(int i, int j, int neighbourCount, boolean[][] cells, CellGrid cellGrid, Rules rules) {
		// If the cell is alive, we apply alive rules.
		if (cellGrid.getState(i, j) == 1)
		{
			// If the rules states that the cell should be alive.
			if (rules.getAliveRules()[neighbourCount])
			{
				setNewBlock(i, j, cellGrid);
			}
			// According to the rules, the cell dies.
			else
			{
				/*
				 * If the cell in the previous iteration had a blockID, we set
				 * it back to -1 to indicate it is not part of any block.
				 * We also remove the cell from the block cells.
				 */
				if (cellGrid.getCells()[i][j].getBlockId() != -1)
				{
					Game.bottomBlocks.get(cellGrid.getCells()[i][j]
							.getBlockId())
							.removeCell(cellGrid
							.getCells()[i][j]);
					
					cellGrid.getCells()[i][j].setBlockId(-1, false);
				}
			}
			
			// Updating the state of the cell according to the rules.
			cells[i][j] = rules.getAliveRules()[neighbourCount];
		}
		else
		{
			// If the rules states that the cell should be alive.
			if (rules.getDeadRules()[neighbourCount])
			{
				setNewBlock(i, j, cellGrid);
			}
			// According to the rules, the cell dies.
			else
			{
				/*
				 * If the cell in the previous iteration had a blockID, we set
				 * it back to -1 to indicate it is not part of any block.
				 * We also remove the cell from the block cells.
				 */
				if (cellGrid.getCells()[i][j].getBlockId() != -1)
				{
					Game.bottomBlocks.get(cellGrid
							.getCells()[i][j]
							.getBlockId())
							.removeCell(cellGrid
							.getCells()[i][j]);
					
					cellGrid.getCells()[i][j].setBlockId(-1, false);
				}
			}
			
			// Updating the state of the cell according to the rules.
			cells[i][j] = rules.getDeadRules()[neighbourCount];
		}
    }
    
    public static void setNewBlock(int i, int j, CellGrid cellGrid) {
	boolean blockSet = false;
		
		/*
		 * We are here determining the block to which the
		 * cell belongs. In fact we scan all the neighbors of
		 * the cell that are alive and have a block. Once
		 * we found it we assign the same block.
		 */
		for (int temp_i = i - 1; temp_i <= i + 1 && !blockSet; temp_i++)
		{
			for (int temp_j = j - 1; temp_j <= j + 1 && !blockSet;
					temp_j++)
			{
				if (!(temp_i == i && temp_j == j) &&
					CheckCoords.verifyCoordinatesBottom(temp_i, temp_j)
					&& cellGrid.getCells()[temp_i][temp_j]
							.getBlockId() != -1)
				{	
					/*
					 *  If a block is found, we set the blockID
					 *  of the cell to the correct ID, and add
					 *  the cell to the block's cells.
					 *  We also remove the cell from the previous block.
					 */
					
					if (cellGrid.getCells()[i][j].getBlockId() != -1)
					{
						Game.bottomBlocks
								.get(cellGrid.getCells()[i][j]
								.getBlockId())
								.removeCell(cellGrid.getCells()[i][j]);
					}
					
					cellGrid.getCells()[i][j]
							.setBlockId(cellGrid
							.getCells()[temp_i][temp_j]
							.getBlockId(), false);
					
					Game.bottomBlocks
							.get(cellGrid.getCells()[temp_i][temp_j]
							.getBlockId())
							.addCell(cellGrid
							.getCells()[i][j]);
					
					blockSet = true;
				}
			}
		}
		
		if (!blockSet) {
			setPreviousBlock(i, j, cellGrid);//ada commented !!!!!!!!!!!!!
		}
    }

    
    public static void setPreviousBlock(int i, int j, CellGrid cellGrid) {
    	/*
		 * We are here determining the block to which the
		 * cell belongs. In fact we scan all the neighbors of
		 * the cell that are alive and have a block. Once
		 * we found it we assign the same block.
		 */
		for (int temp_i = i - 1; temp_i <= i + 1; temp_i++)
		{
			for (int temp_j = j - 1; temp_j <= j + 1;
					temp_j++)
			{
				if (!(temp_i == i && temp_j == j) &&
					CheckCoords.verifyCoordinatesBottom(temp_i, temp_j)
					&& cellGrid.getCells()[temp_i][temp_j]
							.getPreviousBlockId() != -1)
				{	
					/*
					 *  If a block is found, we set the blockID
					 *  of the cell to the correct ID, and add
					 *  the cell to the block's cells.
					 *  We also remove the cell from the previous block.
					 */
					
					if (cellGrid.getCells()[i][j].getBlockId() != -1)
					{
						Game.bottomBlocks
								.get(cellGrid.getCells()[i][j]
								.getBlockId())
								.removeCell(cellGrid.getCells()[i][j]);
					}
					
					cellGrid.getCells()[i][j]
							.setBlockId(cellGrid
							.getCells()[temp_i][temp_j]
							.getPreviousBlockId(), false);
					
					Game.bottomBlocks
							.get(cellGrid.getCells()[temp_i][temp_j]
							.getPreviousBlockId())
							.addCell(cellGrid
							.getCells()[i][j]);
				}
			}
		}
    }

}
