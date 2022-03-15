package abstraction;

import java.util.logging.Level;

import logger.LoggerInstance;

public class GranularitySelection
{
	/*
	 * Enumeration of the granularity the user can select for the middle
	 * automaton.
	 * /!\ A check is NOT performed on the bottom automaton dimensions
	 * beforehand.
 	 */
	public enum Granularity
	{
		SQUARE_2x2,
		SQUARE_4x4,
		SQUARE_8x8,
		SQUARE_10x10
	}
	
	/*
	 * This function parses the input string in the text box of the menu.
	 * This is used for both middle and top threshold selection.
	 */
	public static Integer[] extractThresholds(String input)
	{
		// If it is empty, no abstraction is selected.
		if (input.length() == 0)
			return new Integer[0];

		// Splitting the string with separator ','.
		String[] strThresholds = input.split(",");
		Integer[] thresholds = new Integer[strThresholds.length];

		try
		{
			/*
			 * For each threshold entered by the user, checking if it is in the
			 * correct scope, as well as if it is a number (exception handling).
			 */
			for (int i = 0; i < strThresholds.length; i++)
			{
				Integer intThreshold = Integer.valueOf(strThresholds[i]);

				if (intThreshold < 0 || intThreshold > 100)
				{
					LoggerInstance.LOGGER.log(Level.SEVERE,
						"Wrong threshold ! "
						+ "Thresholds must be between 0 and 100 included. "
						+ "You entered : "
						+ intThreshold + ".");
					LoggerInstance.LOGGER.log(Level.SEVERE,
						"Exiting simulation.");
					System.exit(1);
				}

				thresholds[i] = intThreshold;
			}
		}
		catch (NumberFormatException e)
		{
			LoggerInstance.LOGGER.log(Level.SEVERE,
					"Wrong threshold ! " + "There was an error converting one"
					+ "of your threshold into Integer."
					+ " Exception message : " + e.getMessage());
			LoggerInstance.LOGGER.log(Level.SEVERE, "Exiting simulation.");
			System.exit(1);
		}
		
		return thresholds;
	}
}
