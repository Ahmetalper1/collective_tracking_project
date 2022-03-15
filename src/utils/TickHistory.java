package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import game.Block;
import game.Game;
import game.Simulator;

/*
 * This class represent the history of a block during all the ticks of the
 * simulation. If the block disappeared, is dead, or not created yet, a
 * placeholder value (-1) will be put in history to show it. Therefore we
 * can have a value for every tick.
 */
public class TickHistory
{
	public final static char COORD_SEPARATOR = ';';
	public final static char SEPARATOR = ',';
	
	// History for both coordinates. Index in this list is the tick number.
	private List<Double> XTicksHistory;
	private List<Double> YTicksHistory;
		
	public TickHistory()
	{
		this.XTicksHistory = new ArrayList<Double>();
		this.YTicksHistory = new ArrayList<Double>();
	}
	
	// Adding a tick to the history.
	public void addTick(Double X, Double Y)
	{
		XTicksHistory.add(X);
		YTicksHistory.add(Y);
	}
	
	
	/*
	 * Filling up the history with default value (-1), representing a dead
	 * block, until tick given as parameter.
	 */
	public void FillMissingTicks(int tick)
	{
		while (XTicksHistory.size() <= tick)
		{
			XTicksHistory.add(Double.valueOf(-1));
			YTicksHistory.add(Double.valueOf(-1));
		}
	}
	
	public List<Double> getYTicksHistory()
	{
		return this.YTicksHistory;
	}
	
	public List<Double> getXTicksHistory()
	{
		return this.XTicksHistory;
	}
	
	/*
	 *  Filling the output file of the bottom automaton with history of every
	 *  bottom block.
	 */
	public static void FillBottomFile(FileWriter FW) throws IOException
	{
		// Filling header.
		FW.append("Ticks");
		for(int i = 0; i < Game.bottomBlocks.size(); i++)
		{
			FW.append(",Bottom Block " + i + " X");
			FW.append(",Bottom Block " + i + " Y");
		}
		
		FW.append('\n');
		
		/*
		 *  For each tick, append the X and Y coordinate of each block to the
		 *  output file.
		 */
		for (Integer i = 0; i < Simulator.LIMIT_STEP_ANALYSIS; i++)
		{
			FW.append(i.toString());
			
			for (Block block : Game.bottomBlocks)
			{
				if (block.getTickHistory().getYTicksHistory().get(i) != -1)
				{
					FW.append("," + block.getTickHistory()
						.getXTicksHistory().get(i));
					FW.append("," + block.getTickHistory()
						.getYTicksHistory().get(i));
				}
				else
				{
					FW.append(",");
				}
			}
			
			FW.append('\n');
		}
	}
	
	// Same behavior as the FillBottomFile function, but with Middle blocks.
	public static void FillMiddleFile(FileWriter FW) throws IOException
	{
		// Filling header.
		FW.append("Ticks");
		for (int i = 0; i < Game.middleBlocks.size(); i++)
		{
			FW.append(",Middle Block " + i + " X");
			FW.append(",Middle Block " + i + " Y");
		}

		FW.append('\n');

		/*
		 * For each tick, append the X and Y coordinate of each block to the
		 * output file.
		 */
		for (Integer i = 0; i < Simulator.LIMIT_STEP_ANALYSIS; i++)
		{
			FW.append(i.toString());

			for (Block block : Game.middleBlocks)
			{
				if (block.getTickHistory().getYTicksHistory().get(i) != -1)
				{
					FW.append("," 
							+ block.getTickHistory().getXTicksHistory().get(i));
					FW.append(","
							+ block.getTickHistory().getYTicksHistory().get(i));
				}
				else
				{
					FW.append(",");
				}
			}

			FW.append('\n');
		}
	}
}
