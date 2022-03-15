package patterns;

import game.Game;
import gui.ColouredButton;
import utils.PatternCellSetter;

/*
 * The glider is a pattern of the GoL that consists of 5 cells moving in a
 * desired direction. This particular generation places a glider in the top
 * left corner of the grid, and it is set to go down right.
 */
public class Glider
{
	/*
	 * This function puts a glider on the cells given as parameter to the
	 * function. It also wipes out everything that could be there already.
	 */
	public static void generate(ColouredButton[][] cells)
	{
		for (int i = 0; i < Game.BOTTOM_ROW; i++)
		{
			for (int j = 0; j < Game.BOTTOM_COLUMN; j++)
			{
				PatternCellSetter.setCellOFF(cells, i, j);
			}
		}
		
		PatternCellSetter.setCellON(cells, 0, 1);
		PatternCellSetter.setCellON(cells, 1, 2);
		PatternCellSetter.setCellON(cells, 2, 0);
		PatternCellSetter.setCellON(cells, 2, 1);
		PatternCellSetter.setCellON(cells, 2, 2);
	}
}
