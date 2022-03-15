package patterns;

import game.Game;
import gui.ColouredButton;
import utils.PatternCellSetter;

/*
 * The blinker is a pattern of the GoL that consists of 4 cells being static,
 * making a block.
 */
public class Block
{
	/*
	 * This function puts a block on the cells given as parameter to the
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
		
		PatternCellSetter.setCellON(cells, 2, 2);
		PatternCellSetter.setCellON(cells, 2, 3);
		PatternCellSetter.setCellON(cells, 3, 2);
		PatternCellSetter.setCellON(cells, 3, 3);
	}
}
