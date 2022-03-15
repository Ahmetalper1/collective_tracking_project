package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

import javax.swing.JRadioButtonMenuItem;

import abstraction.GranularitySelection;
import abstraction.GranularitySelection.Granularity;
import automaton.Rules;
import game.Game;
import game.Simulator;
import gui.InitialStateGUI;
import gui.Menu;
import logger.LoggerInstance;
import patterns.Pattern;

/*
 * This class is the ActionListener for the 'Start' button on the main menu GUI
 * window. This will retrieve the informations from the GUI and transform it
 * into data to create the automata.
 */
public class StartButtonListener implements ActionListener
{
	private Menu menu;
	
	public StartButtonListener(Menu menu)
	{
		this.menu = menu;
	}
	
	// This is called when the user presses 'Start'.
	@Override
	public void actionPerformed(ActionEvent event)
	{
		// Setting up the logger to detailed or not thanks to the user choice.
		LoggerInstance.setupLogger(menu.getLogChoice().isSelected());
		
		// Getting the pattern the user selected.
		Pattern pattern = (Pattern) menu.getPattern().getSelectedItem();
		menu.getInitialStateGUI().selectPattern(pattern);
		Game.pattern = pattern;
		
		// Retrieving the thresholds selected by the user.
		Integer[] thresholdsMid = GranularitySelection
			.extractThresholds(menu.getMiddleThresholds().getText().toString());
		for (Integer threshold : thresholdsMid)
		{
			Game.MiddleThresholds.add(threshold);
		}
		
		// Retrieving the thresholds selected by the user.
		Integer[] thresholdsTop = GranularitySelection
			.extractThresholds(menu.getTopThresholds().getText().toString());
		for (Integer threshold : thresholdsTop)
		{
			Game.TopThresholds.add(threshold);
		}
		
		// Retrieving the middle granularity selected by the user.
		GranularitySelection.Granularity granularityMid =
				(Granularity) menu.getmiddleGranularity().getSelectedItem();
		Game.granularity = granularityMid;
		switch (granularityMid)
		{
			case SQUARE_2x2:
				Game.MIDDLE_ROW = Game.BOTTOM_ROW / 2;
				Game.MIDDLE_COLUMN = Game.BOTTOM_COLUMN / 2;
				break;
			case SQUARE_4x4:
				Game.MIDDLE_ROW = Game.BOTTOM_ROW / 4;
				Game.MIDDLE_COLUMN = Game.BOTTOM_COLUMN / 4;
				break;
			case SQUARE_8x8:
				Game.MIDDLE_ROW = Game.BOTTOM_ROW / 8;
				Game.MIDDLE_COLUMN = Game.BOTTOM_COLUMN / 8;
				break;
			default: //case SQUARE_10x10:
				Game.MIDDLE_ROW = Game.BOTTOM_ROW / 10;
				Game.MIDDLE_COLUMN = Game.BOTTOM_COLUMN / 10;
				break;
		}
		
		// Getting the limit iteration number indicated by the user.
		try
		{
			int limitStep =
					Integer.parseInt(menu.getLimitStep().getText().toString());
			Simulator.LIMIT_STEP_ANALYSIS = limitStep;
		}
		catch (Exception e)
		{
			LoggerInstance.LOGGER.log(Level.SEVERE, "Error: Wrong input for "
					+ "step limit: you can not enter " + 
					menu.getLimitStep().getText().toString() + ".");
			LoggerInstance.LOGGER.log(Level.SEVERE, "Setting the limit step to "
					+ "100 default steps.");
		}
		
		// Indicating via logging the state of the logger.
		if (menu.getLogChoice().isSelected())
		{
			LoggerInstance.LOGGER.log(Level.WARNING,
					"The logger is set up in detailed mode !");
		}
		else
		{
			LoggerInstance.LOGGER.log(Level.WARNING,
					"The logger is set up in non detailed mode !");
		}
		
		// Get the aliveRules from the menu GUI.
		boolean[] aliveRules = new boolean[9];
		int index = 0;
		for (JRadioButtonMenuItem button : menu.getAliveRules())
		{
			aliveRules[index] = button.isSelected();
			index++;
		}
			
		// Get the deadRules from the menu GUI.
		boolean[] deadRules = new boolean[9];
		index = 0;
		for (JRadioButtonMenuItem button : menu.getDeadRules())
		{
			deadRules[index] = button.isSelected();
			index++;
		}
		
		// Creating the rules.
		Rules rules = new Rules(aliveRules, deadRules);
		
		// Creating the cells for the future CellGrid.
		boolean[][] cells = new boolean[InitialStateGUI.ROWS]
				[InitialStateGUI.COLUMNS];
		
		// Filling the cells with what the user did set as initial state.
		for (int i = 0; i < InitialStateGUI.ROWS; i++)
		{
			for (int j = 0; j < InitialStateGUI.COLUMNS; j++)
			{
				cells[i][j] = menu.getInitialStateGUI().getCells()[i][j]
						.isSelected();
			}
		}
		
		// These two lines are closing the menu GUI and the InitialStateGUI.
		menu.getInitialStateGUI().dispose();
		menu.dispose();
		
		// Creating the game with the initial cells and selected rules.
		Game game = new Game(cells, rules);
		
		/*
		 * Instantiating the simulator. It extends the Thread class and can be
		 * launched via a separate thread with .start(). See Simulator class
		 * for more information.
		 */
		Simulator simulator = new Simulator(game);
		simulator.start();
	}
}
