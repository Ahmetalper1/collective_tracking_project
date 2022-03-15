package patterns;

import game.Game;
import gui.ColouredButton;
import utils.PatternCellSetter;

/*
 * The glider gun is the smallest canon in game of life, for more information,
 * please see the Conwaylife wiki.
 */
public class GliderGun
{
	/*
	 * This function puts a glider gun on the cells given as parameter to the
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
		
		// Left block.
		PatternCellSetter.setCellON(cells, 5, 0);
		PatternCellSetter.setCellON(cells, 6, 0);
		PatternCellSetter.setCellON(cells, 5, 1);
		PatternCellSetter.setCellON(cells, 6, 1);
		
		// Left parenthesis.
		PatternCellSetter.setCellON(cells, 5, 10);
		PatternCellSetter.setCellON(cells, 6, 10);
		PatternCellSetter.setCellON(cells, 7, 10);
		PatternCellSetter.setCellON(cells, 4, 11);
		PatternCellSetter.setCellON(cells, 8, 11);
		PatternCellSetter.setCellON(cells, 3, 12);
		PatternCellSetter.setCellON(cells, 9, 12);
		PatternCellSetter.setCellON(cells, 3, 13);
		PatternCellSetter.setCellON(cells, 9, 13);
		
		// Single middle cell.
		PatternCellSetter.setCellON(cells, 6, 14);
		
		// Middle part.
		PatternCellSetter.setCellON(cells, 4, 15);
		PatternCellSetter.setCellON(cells, 8, 15);
		PatternCellSetter.setCellON(cells, 5, 16);
		PatternCellSetter.setCellON(cells, 6, 16);
		PatternCellSetter.setCellON(cells, 7, 16);
		PatternCellSetter.setCellON(cells, 6, 17);
		
		// Right part.
		PatternCellSetter.setCellON(cells, 3, 20);
		PatternCellSetter.setCellON(cells, 4, 20);
		PatternCellSetter.setCellON(cells, 5, 20);
		PatternCellSetter.setCellON(cells, 3, 21);
		PatternCellSetter.setCellON(cells, 4, 21);
		PatternCellSetter.setCellON(cells, 5, 21);
		PatternCellSetter.setCellON(cells, 2, 22);
		PatternCellSetter.setCellON(cells, 6, 22);
		PatternCellSetter.setCellON(cells, 1, 24);
		PatternCellSetter.setCellON(cells, 2, 24);
		PatternCellSetter.setCellON(cells, 6, 24);
		PatternCellSetter.setCellON(cells, 7, 24);
		
		// Right block.
		PatternCellSetter.setCellON(cells, 3, 34);
		PatternCellSetter.setCellON(cells, 4, 34);
		PatternCellSetter.setCellON(cells, 3, 35);
		PatternCellSetter.setCellON(cells, 4, 35);
	}
	
}
