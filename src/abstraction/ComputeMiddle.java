package abstraction;

import automaton.MiddleAutomaton;
import game.Game;

public class ComputeMiddle
{
	public static int DEFAULT_SQUARE_SIZE = 10;
	
	/*
	 * This function computes the new middle automaton cells, thanks to the
	 * bottom ones, and the threshold selected.
	 */
	public static boolean[][] apply(int threshold, boolean[][] bottomCells,
		MiddleAutomaton automaton)
	{
		boolean[][] newCells = new boolean[Game.MIDDLE_ROW][Game.MIDDLE_COLUMN];
		int square_size = DEFAULT_SQUARE_SIZE;
		
		switch (Game.granularity)
		{
			case SQUARE_2x2:
				square_size = 2;
				break;
			case SQUARE_3x3: //ada added case
				square_size = 3;
				break;
			case SQUARE_4x4:
				square_size = 4;
				break;
			case SQUARE_8x8:
				square_size = 8;
				break;
			default: // case SQUARE_10x10:
				break;
		}
		
		for (int i = 0; i < Game.MIDDLE_ROW; i++)
		{
			for (int j = 0; j < Game.MIDDLE_COLUMN; j++)
			{
				/* 
				 * This counter represents the number of alive cells in the
				 * square_size x square_size sub grid we are working on.
				 */
				int counter = 0;
				for (int h = 0; h < square_size; h++)
				{
					for (int w = 0; w < square_size; w++)
					{
						// If the cell is alive, we increment the counter.
						if (bottomCells[i * square_size + h]
									   [j * square_size + w])
							counter++;
					}
				}
				
				/*
				 * The following commented line is used if we want to use
				 * percentage instead of cell number.
				 */
				// if ((Double.valueOf(counter) / Double.valueOf(square_size * square_size) * 100) >= threshold)
				if (counter >= threshold)
				{
					newCells[i][j] = true;
					
					// Updating the cell's block.
					if (automaton != null)
					{
						AssignBlockMiddle.AssignBlockMiddleMethod(i, j, automaton);//ada commented: bad idea :)
					}
				}
				
				// Setting up the dead cell if the automaton exists.
				else if (automaton != null && automaton.getGrid().getCells()[i][j].getBlockId() != -1)
				{
					// Removing the cell from the block's list.
					Game.middleBlocks.get(automaton.getGrid().getCells()[i][j]
						.getBlockId())
						.removeCell(automaton.getGrid().getCells()[i][j]);
					
					// Setting the cell's blockID to -1 (dead).
					automaton.getGrid().getCells()[i][j].setBlockId(-1, false);
				}
			}
		}
		
		return newCells;
	}
}
