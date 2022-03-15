package utils;

import game.Game;

/*
 * This class implements the verifyCoordinatesBottom() method that just verifies
 * if the given coordinates are valid or not.
 * It is the same thing for the verifyCoordinatesMiddle() method.
 */
public class CheckCoords
{
	public static boolean verifyCoordinatesBottom(int i, int j)
	{
		return (i >= 0)
				&& (j >= 0)
				&& (i < Game.BOTTOM_ROW)
				&& (j < Game.BOTTOM_COLUMN);
	}
	
	public static boolean verifyCoordinatesMiddle(int i, int j)
	{
		return (i >= 0)
				&& (j >= 0)
				&& (i < Game.MIDDLE_ROW)
				&& (j < Game.MIDDLE_COLUMN);
	}
}
