package game;

import java.awt.Dimension;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import abstraction.GranularitySelection.Granularity;
import automaton.BottomAutomaton;
import automaton.MiddleAutomaton;
import automaton.Rules;
import automaton.TopAutomaton;
import logger.LoggerInstance;
import patterns.Pattern;
import utils.OutputFiles;

/*
 * This class is the one that holds the entire data and every automaton used.
 * This is created right after the 'Start' button was pressed by the user.
 */
public class Game
{
	/*
	 * This automaton is the Level 0 automaton, containing the grid and giving
	 * the input to the middleAutomaton. It is also the one on which we will
	 * apply the rules.
	 */
	private BottomAutomaton bottomAutomaton;
	public static int BOTTOM_ROW = 40;
	public static int BOTTOM_COLUMN = 80;
	/*
	 * This automaton is really the bottomAutomaton but with a different
	 * granularity. It also serves as input for the top automaton/automata.
	 */
	private List<MiddleAutomaton> middleAutomaton;
	// TODO:: CHANGE THESE HARDCODED VALUES
	public static int MIDDLE_ROW = -1;
	public static int MIDDLE_COLUMN = -1;
	public static Granularity granularity = null;
	
	// This represents the thresholds used for the middle automaton.
	public static List<Integer> MiddleThresholds = new ArrayList<Integer>();
	
	/*
	 * This is the top automaton receiving data from the middle one and
	 * analyzing it. It then creates a readable output to interpret what is
	 * going on in the bottom automaton.
	 */
	private List<TopAutomaton> topAutomaton;
	public static int TOP_ROW = 1;
	public static int TOP_COLUMN = 1;
	
	// This represents the thresholds used for the top automaton.
	public static List<Integer> TopThresholds = new ArrayList<Integer>();
	
	// This variable is the pattern selected by the user. By default PERSONAL.
	public static Pattern pattern = Pattern.PERSONAL;
	
	// Those are the FileWriters related to the two blocks history files.
	private FileWriter bottomFW;
	private FileWriter middleFW;
	
	// This is the list of all the current blocks found in the bottom automaton.
	public static List<Block> bottomBlocks = new ArrayList<Block>();
	
	// This is the list of all the current blocks found in the middle automaton.
	public static List<Block> middleBlocks = new ArrayList<Block>();
	
	// Constructor of the class.
	public Game(boolean[][] cells, Rules rules)
	{
		LoggerInstance.LOGGER.log(Level.FINEST, "Game is starting !");
		
		middleAutomaton = new ArrayList<MiddleAutomaton>();
		topAutomaton = new ArrayList<TopAutomaton>();
		
		// Creating the bottom automaton with the given data.
		LoggerInstance.LOGGER.log(Level.FINEST,
				"Creating the bottom automaton ...");
		this.bottomAutomaton = new BottomAutomaton(
				new Dimension(BOTTOM_COLUMN, BOTTOM_ROW),
				cells, rules);
		
		/*
		 *  We use here one of the abstraction functions in the
		 *  abstraction.middle package.
		 */
		LoggerInstance.LOGGER.log(Level.FINEST,
				"Creating the middle automaton ...");
		
		int automatonCounter = 0;
		
		for (Integer thresholdMid : Game.MiddleThresholds)
		{
			boolean[][] middleCells = abstraction.ComputeMiddle
				.apply(thresholdMid, cells, null);
			
			
			MiddleAutomaton newMiddle = new MiddleAutomaton(
					new Dimension(MIDDLE_COLUMN, MIDDLE_ROW), middleCells,
					thresholdMid, automatonCounter);
			//ada changed to have larger midle windows
			/*
			MiddleAutomaton newMiddle = new MiddleAutomaton(//!!!!!!!!!!!!
					new Dimension(12, 26), middleCells,
					thresholdMid, automatonCounter);*/
			
			// Instantiating the middle automaton with the new cells.
			this.middleAutomaton.add(newMiddle);
			
			this.bottomAutomaton.addSuperAutomaton(newMiddle);
			newMiddle.addSubAutomaton(bottomAutomaton);
						
			for (Integer thresholdTop : Game.TopThresholds)
			{
				boolean[][] topCells = abstraction.ComputeTop
					.apply(thresholdTop, middleCells);

				TopAutomaton newTop = new TopAutomaton(
					new Dimension(TOP_COLUMN, TOP_ROW), topCells, thresholdTop);
				
				// Instantiating the top automaton with the new cells.
				this.topAutomaton.add(newTop);
				
				newMiddle.addSubAutomaton(newTop);
				newTop.addSubAutomaton(newMiddle);
			}
			
			automatonCounter++;
		}
		
		// Creating output files.
		OutputFiles.CreateBlocksHistoryDirectory();
		OutputFiles.CreateBlocksHistoryFiles(pattern.toString());
		
		// Setting up the FileWriters for the output files.
		try
		{
			this.bottomFW = new FileWriter(System.getProperty("user.dir")
					+ "/BlockHistory/Block_History_Bottom_" + pattern + ".txt");
			
			this.middleFW = new FileWriter(System.getProperty("user.dir")
					+ "/BlockHistory/Block_History_Middle_" + pattern + ".txt");
		}
		catch (IOException e)
		{
			LoggerInstance.LOGGER.log(Level.SEVERE, "IOException in Game()"
					+ " constructor: Could not create bottomFW or middleFW.");
		}
		
		// Filling output files header.
		OutputFiles.EraseAndFillBlockHistoryFiles(pattern.toString(),
				bottomFW, middleFW);
	}
	
	public BottomAutomaton getBottomAutomaton()
	{
		return this.bottomAutomaton;
	}
	
	public List<MiddleAutomaton> getMiddleAutomaton()
	{
		return this.middleAutomaton;
	}
	
	public List<TopAutomaton> getTopAutomaton()
	{
		return this.topAutomaton;
	}
	
	/*
	 * This method creates the blocks at the beginning of the simulation.
	 */
	public static void setupBlocks()
	{
		bottomBlocks = new ArrayList<Block>();
		middleBlocks = new ArrayList<Block>();
	}

	public FileWriter getBottomFW()
	{
		return this.bottomFW;
	}
	
	public FileWriter getMiddleFW()
	{
		return this.middleFW;
	}
}
