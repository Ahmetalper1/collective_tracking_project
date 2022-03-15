package patterns;

import game.Game;
import gui.ColouredButton;
import utils.PatternCellSetter;

/*
 * The glider is a pattern of the GoL. See documentation of GoL for information.
 */
public class Herschel
{
	/*
	 * This function puts a herschel on the cells given as parameter to the
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
		
		PatternCellSetter.setCellON(cells, 15, 50);
		PatternCellSetter.setCellON(cells, 16, 50);
		PatternCellSetter.setCellON(cells, 17, 50);
		PatternCellSetter.setCellON(cells, 16, 51);
		PatternCellSetter.setCellON(cells, 18, 52);
		PatternCellSetter.setCellON(cells, 16, 52);
		PatternCellSetter.setCellON(cells, 17, 52);
	}
}
