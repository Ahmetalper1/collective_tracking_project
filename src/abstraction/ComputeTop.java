package abstraction;

import game.Game;

public class ComputeTop
{
	/*
	 * This function computes the new top automaton cell, thanks to the
	 * middle cells, and the threshold selected.
	 */
	public static boolean[][] apply(int threshold, boolean[][] midCells)
	{
		boolean[][] newCells = new boolean[Game.TOP_ROW][Game.TOP_COLUMN];
		
		int totalAliveMiddleCells = 0;
		for (int i = 0; i < Game.MIDDLE_ROW; i++)
		{
			for (int j = 0; j < Game.MIDDLE_COLUMN; j++)
			{
				if (midCells[i][j])
					totalAliveMiddleCells++;
			}
		}

		/*
		 * The following commented line is used if the percentage is wanted
		 * instead of the number of cells.
		 */
		// if ((Double.valueOf(totalAliveMiddleCells) / Double.valueOf(Game.MIDDLE_ROW * Game.MIDDLE_COLUMN)) * 100 >= threshold)
		if (totalAliveMiddleCells >= threshold)
		{
			newCells[0][0] = true;
		}
		
		return newCells;
	}
}