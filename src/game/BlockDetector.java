package game;

import java.util.logging.Level;

import automaton.Cell;
import automaton.UpperCA;
import logger.LoggerInstance;
import utils.CheckCoords;

/*
 * The BlockDetector class holds the static method that detects new blocks on
 * the bottom automaton: DetectBlocks().
 */
public class BlockDetector
{
	// Static counter for blocks of the bottom automaton. 
	public static int blockIDBottom = 0;
	
	// Static counter for blocks of the middle automaton.
	public static int blockIDMiddle = 0;
	
	/*
	 * This function goes through the whole automaton and detects alive cells without blocks.
	 */
	public static void DetectBlocks(Game game)
	{
		LoggerInstance.LOGGER.log(Level.FINEST,
				"Starting to look for new blocks ...");
		
		// Retrieving bottom automaton cells.
		Cell[][] bottomCells = game.getBottomAutomaton().getGrid().getCells();
		// Iterating through the whole bottom automaton.
		for (int i = 0; i < Game.BOTTOM_ROW; i++)
		{
			for (int j = 0; j < Game.BOTTOM_COLUMN; j++)
			{
				// If one alive cell is found and has no block, we launch a BFS-like search to identify all the new block.
				if (bottomCells[i][j].isAlive() && bottomCells[i][j].getBlockId() == -1)
				{
					BFSEquivalent(j, i, blockIDBottom, bottomCells, BlockType.BOTTOM, Block.BOTTOM_BLOCK_AUTOMATON_ID);
					blockIDBottom++;
				}
			}
		}
		
		for (UpperCA middleAutomaton : game.getMiddleAutomaton())
		{
			// Retrieving middle automaton cells.
			Cell[][] middleCells = middleAutomaton.getGrid().getCells();
			// Iterating through the whole middle automaton.
			for (int i = 0; i < Game.MIDDLE_ROW; i++)
			{
				for (int j = 0; j < Game.MIDDLE_COLUMN; j++)
				{
					// If one alive cell is found and has no block, we launch a BFS-like search to identify all the new block.
					if (middleCells[i][j].isAlive() && middleCells[i][j].getBlockId() == -1)
					{
						BFSEquivalent(j, i, blockIDMiddle, middleCells, BlockType.MIDDLE, middleAutomaton.getID());
						blockIDMiddle++;
					}
				}
			}
		}
	}
	
	/*
	 * This function launches a BFS-like search on the automaton from
	 * the given cell at coordinates X and Y. If it has alive neighbors with
	 * no blocks, that means it's part of the new one. This is used for both bottom and middle automata.
	 */
	private static void BFSEquivalent(int x, int y, int blockID, Cell[][] cells, BlockType blockType, int automatonID)
	{
		// Setting up the actual cell to the correct blockID if not already set.
		if (cells[y][x].getBlockId() == -1)
		{
			// Setting the current cell with the correct blockID.
			cells[y][x].setBlockId(blockID, true);
			
			/*
			 *  If the block was not created (1st call of this function), we
			 *  create a new block.
			 */
			if (blockType == BlockType.BOTTOM)
			{
				if (blockID >= Game.bottomBlocks.size())
				{
					Game.bottomBlocks.add(new Block(blockID, cells[y][x], BlockType.BOTTOM, Block.BOTTOM_BLOCK_AUTOMATON_ID));
				}
				else
				{
					Game.bottomBlocks.get(blockID).addCell(cells[y][x]);
				}
			}
			else if (blockType == BlockType.MIDDLE)
			{
				if (blockID >= Game.middleBlocks.size())
				{
					Game.middleBlocks.add(new Block(blockID, cells[y][x], BlockType.MIDDLE, automatonID));
				}
				else
				{
					Game.middleBlocks.get(blockID).addCell(cells[y][x]);
				}
			}
		}
		
		/*
		 * We then check here every neighbor of the current cell to see
		 * if any is alive and has not any block yet. If so, we re-launch the
		 * recursive function on this cell.		
		 */
		
		for (int i = y - 1; i <= y + 1; i++)
		{
			for (int j = x - 1; j <= x + 1; j++)
			{
				if ((i != y || j != x) && CheckCoords.verifyCoordinatesMiddle(i, j) && cells[i][j].isAlive() && cells[i][j].getBlockId() == -1)
				{
					BFSEquivalent(j, i, blockID, cells, blockType, automatonID);
				}
			}
		}
	}

	/*
	 * This function detects blocks splitting and blocks merging. 
	 * 		--> Ada: it does NOT seem to detect any splitting&merging
	 * It also updates the center of every block in the blocks list.
	 */
	public static void updateBlockCenteres(Game game, int tick)//ada: method used to be called:updateBlocks
	{
		LoggerInstance.LOGGER.log(Level.FINEST, 
				"Updating center of all blocks ...");
		
		// Updating each block's center.
		for (Block block : Game.bottomBlocks)
		{
			// Verifying that the block we're currently on is alive.
			if (block.getStatus())
			{
				block.updateCenter(tick);
			}
		}
		
		// Updating each block's center.
		for (Block block : Game.middleBlocks)
		{
			// Middle blocks do not die. //Ada-TODO: why is that?
			block.updateCenter(tick);
		}
	}
	
	/*
	 * This function is in charge of the detection of blocks division and/or
	 * merging. This will update the blocks status, and cells. It can also
	 * create new blocks when a division occurs.
	 */
	public static void DetectMergeAndDivisionBottom(Game game)
	{
		// Marks cells as already visited or not.
		boolean[][] visitedCells =
				new boolean[Game.BOTTOM_ROW][Game.BOTTOM_COLUMN];
		
		// Marks the blocks as already visited or not.
		boolean[] visitedBlocks = new boolean[Game.bottomBlocks.size()];
		
		// Real cells of the grid. Used to modify blocks.
		Cell[][] gameCells = game.getBottomAutomaton().getGrid().getCells();
		
		// Going through the whole grid.
		for (int i = 0; i < Game.BOTTOM_ROW; i++)
		{
			for (int j = 0; j < Game.BOTTOM_COLUMN; j++)
			{
				// Only iterating on alive, not visited cells part of a block.
				//@Ada-TODO: why? why only on cells that are already part of block? or is this the only viable option? 
				if (!visitedCells[i][j] && gameCells[i][j].isAlive())
				{
					if (visitedBlocks[gameCells[i][j].getBlockId()])//ada: new block split from an existing one?
					{
						// Block to be added to the game's list.
						Block newBlock = new Block(blockIDBottom++,
								gameCells[i][j], BlockType.BOTTOM,
								Block.BOTTOM_BLOCK_AUTOMATON_ID);
						
				 		// This fills up the newBlock block. See method.
						ReplaceWithNewBlockBottom(i, j, visitedCells, gameCells,
								newBlock);
						
						Game.bottomBlocks.add(newBlock);
					}
					else //ada: the cell belongs to a block that has not been visited yet
					{
						/*
						 * Exploring a block with the cell (i, j) as start.
						 * Verifying if there is a merge occurring.
						 */
						boolean isMergedBlock = BlockVerificationBFSBottom(
							i, j, visitedCells, gameCells,
							gameCells[i][j].getBlockId());

						// If there is a merge, we create a new block.  //ADA TODO: why create a new block? rather than merging one block into another? what happens to the previous blocks that were merged?
						if (isMergedBlock)
						{
							Block newBlock = new Block(blockIDBottom++,
								gameCells[i][j], BlockType.BOTTOM,
								Block.BOTTOM_BLOCK_AUTOMATON_ID);
							ReplaceWithNewBlockBottom(i, j, visitedCells,
								gameCells, newBlock);
							Game.bottomBlocks.add(newBlock);
						}
						else//ada: no merge (just an older block?)
						{
							// If no merge is found, the block is visited.
							visitedBlocks[gameCells[i][j].getBlockId()] = true;
						}
					}
				}
			}
		}
		
		/*
		 * Marking as dead all blocks that were not visited (either no cells
		 * left or merged with other ones).
		 */
		int index = 0;
		for (boolean visited : visitedBlocks)
		{
			if (!visited)
			{
				Game.bottomBlocks.get(index).setStatus(false);
			}
			index++;
		}
	}
	
	/*
	 * This function explores the block from a starting cell (i, j). It verifies
	 * that the block is not merged with another one, and simply marks it as
	 * visited. If anything happens, it calls another method to create a new
	 * block. The boolean returned indicates if the block verification went well
	 * (ie false if there was no merge) or not (true is there is a merge
	 * detected).
	 * 
	 * ada: if the blockId of cell(i+/-1,j+/-1) is different from blockId of cell(i,j), then there is a merge;
	 * ada: it does not create any new block here
	 */
	private static boolean BlockVerificationBFSBottom(int i, int j,
			boolean[][] visitedCells, Cell[][] gameCells, int blockID)
	{
		// This holds the return value.
		boolean result = false;
		
		// Marking the cell we are working on as visited already.
		visitedCells[i][j] = true;
		
		// Iterating through the whole grid.
		for (int offset_i = i - 1; offset_i <= i + 1; offset_i++)
		{
			for (int offset_j = j - 1; offset_j <= j + 1; offset_j++)
			{
				// If the cell exists, is alive and not visited.
				if (CheckCoords.verifyCoordinatesBottom(offset_i, offset_j)
						&& !visitedCells[offset_i][offset_j]
						&& gameCells[offset_i][offset_j].isAlive())
				{
					// We detect here a merge (the ID is unknown) !
					if (gameCells[offset_i][offset_j].getBlockId() != blockID)
					{
						return true;
					}
					// Everything is good, we stay on the right block.
					else
					{
						result = result || BlockVerificationBFSBottom(offset_i,
							offset_j, visitedCells, gameCells, blockID);
					}
				}
			}
		}
		return result;
	}
	
	/*
	 * This method simply explores with a BFS all the alive cells from an
	 * initial one on (i, j) and updates their block with the new ID blockID.
	 * It creates a new block and adds it to the game list of blocks.
	 */
	private static void ReplaceWithNewBlockBottom(int i, int j,
			boolean[][] visitedCells, Cell[][] gameCells, Block newBlock)
	{
		visitedCells[i][j] = true;
		
		// Iterating through the whole grid.
		for (int offset_i = i - 1; offset_i <= i + 1; offset_i++)
		{
			for (int offset_j = j - 1; offset_j <= j + 1; offset_j++)
			{	
				// If the cell exists, is alive and part of the new block.
				//ada: and NOT part of the new block? 
				if (CheckCoords.verifyCoordinatesBottom(offset_i, offset_j)
						&& gameCells[offset_i][offset_j].getBlockId()
							!= newBlock.getID() 
						&& gameCells[offset_i][offset_j].isAlive())
				{
					Cell currentCell = gameCells[offset_i][offset_j];
					
					// Removing old block, and setting the new one to the cell.
					Game.bottomBlocks.get(currentCell.getBlockId()).removeCell(currentCell);
					currentCell.setBlockId(newBlock.getID(), false);
					newBlock.addCell(currentCell);
					
					// Updating the state and the color of the cell.
					currentCell.updateState(true);
							
					// Applying it to any alive neighbor.
					ReplaceWithNewBlockBottom(offset_i, offset_j, visitedCells,
						gameCells, newBlock);
				}
			}
		}
	}
}