package patterns;

import game.Game;
import gui.ColouredButton;
import utils.PatternCellSetter;

/*
 * The blinker is a pattern of the GoL that consists of 3 cells 'blinking', with
 * a period of 2.
 */
public class Blinker
{
	/*
	 * This function puts a blinker on the cells given as parameter to the
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
		
		PatternCellSetter.setCellON(cells, 2, 0);
		PatternCellSetter.setCellON(cells, 2, 1);
		PatternCellSetter.setCellON(cells, 2, 2);
	}
}
