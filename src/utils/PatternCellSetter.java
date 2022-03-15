package utils;

import gui.ColouredButton;

public class PatternCellSetter
{
	// Setting on a specific ColouredButton.
	public static void setCellON(ColouredButton[][] cells, int i, int j)
	{
		cells[i][j].setSelected(true);
		cells[i][j].setColour(ColouredButton.LIVE_COLOUR);
	}
	
	// Setting off a specific ColouredButton.
	public static void setCellOFF(ColouredButton[][] cells, int i, int j)
	{
		cells[i][j].setSelected(false);
		cells[i][j].setColour(ColouredButton.DEAD_COLOUR);
	}
}
