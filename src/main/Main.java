package main;

import gui.Menu;
import logger.LoggerInstance;

public class Main
{
	// The unique main function of the program.
	public static void main(String[] args)
	{
		// Instantiating the logger for the program.
		LoggerInstance.initLogger();
		
		/*
		 *  Creating the GUI and displaying it. The menu itself will call
		 *  listeners for the execution of the program.
		 */
		Menu menu = new Menu();
		menu.setVisible(true);
	}
}
