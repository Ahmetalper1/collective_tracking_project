package game;

import java.awt.Color;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
//https://knowm.org/open-source/xchart/xchart-example-code/
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler.LegendPosition;

import automaton.AutomatonLevel;
import logger.LoggerInstance;

/*
 * This class is made to draw a graph of the behavior of the automaton.
 * When the display() method is called, it will create a graph representing all
 * the blocks and their movement, then display it.
 */
public class Graph
{
	// TODO:: Change these values to a private mini size.;
	public static int MINIMUM_HISTORY_SIZE = 2;
	public static Integer DEFAULT_BOTTOM_ID = -1;
	
	// Current game.
	private Game game;
	
	// Level of the graph.
	AutomatonLevel level;
	
	// ID of the automaton (index in the list for the middle ones).
	int ID;
	
	// Title of the graph for lisibility.
	private String title;
		
	// Constructor.
	public Graph(Game game, AutomatonLevel level, Integer ID)
	{
		this.game = game;
		this.level = level;
		this.ID = ID;
		
		if (level != AutomatonLevel.BOTTOM)
		{
			Graph.MINIMUM_HISTORY_SIZE = 2;
			this.title = "Middle Automaton | Threshold : "
				+ game.getMiddleAutomaton().get(ID).getThreshold()
				+ " | Granularity : " + Game.granularity.toString()
				+ " | Size : " + Game.MIDDLE_ROW + "x" + Game.MIDDLE_COLUMN
				+ ".";
		}
		else
		{
			this.title = "Bottom Automaton | No Abstraction | Size : "
				+ Game.BOTTOM_ROW + "x" + Game.BOTTOM_COLUMN + ".";
		}
	}
	
	/*
	 * This functions displays the graph containing the movements of the
	 * blocks found in the bottom automaton.
	 */
	public void display()
	{	
		LoggerInstance.LOGGER.log(Level.FINEST, "Displaying graph ...");
		
		// Creating the chart (graph) with the correct size.
		XYChart chart = new XYChart(800, 600);
		
		chart.getStyler().setChartTitleBoxBorderColor(Color.WHITE);
		chart.setTitle(title);

		// Setting the chart to scatter, which is only points on the graph.
		chart.getStyler()
			.setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Scatter);
	    
	    // Legend is OUTSITE the chart. Otherwise it hides a part of it.
	    chart.getStyler().setLegendPosition(LegendPosition.OutsideE);
	    
	    // Size of the dots.
	    chart.getStyler().setMarkerSize(16);
	    
	    // Setting up the colors for a nice display.
	    chart.getStyler().setChartBackgroundColor(Color.DARK_GRAY);
	    chart.getStyler().setPlotBackgroundColor(Color.DARK_GRAY);
	    chart.getStyler().setAxisTickLabelsColor(Color.WHITE);
	    
	    if (this.level == AutomatonLevel.BOTTOM)
	    {
		    /*
		     * For every block found in the bottom automaton, we get its history
		     * (both X and Y history), and give it to the chart that
		     * automatically transforms it into dots on the graph.
		     */
		    for (int i = 0; i < Game.bottomBlocks.size(); i++)
		    {
		    	Block botBlock = Game.bottomBlocks.get(i);
		    	if (botBlock.getXHistory().size() >= MINIMUM_HISTORY_SIZE
		    		&& botBlock.getYHistory().size() >= MINIMUM_HISTORY_SIZE)
				{
					chart.addSeries(level.toString() + " Block " + i,
						Game.bottomBlocks.get(i).getXHistory(),
						Game.bottomBlocks.get(i).getYHistory());
					
					//ada: add regression function line
				    PolynomialFunction blockTrajectoryFunction = botBlock.getBlockTrajectoryFunction();
				    if(blockTrajectoryFunction != null) {
				    	List<Double> xList = Game.bottomBlocks.get(i).getXHistory();
				    	List<Double> yList = Game.bottomBlocks.get(i).getYHistory();
				    	double firstX = xList.get(0).doubleValue();
				    	double lastX = xList.get(xList.size()-1).doubleValue();
				    	//double firstY = yList.get(0).doubleValue();
				    	//double lastY = yList.get(yList.size()-1).doubleValue();
				    	double firstFcY = blockTrajectoryFunction.value(firstX);
				    	double lastFcY = blockTrajectoryFunction.value(lastX);
				    	double[] xFc = {firstX, lastX};//{0, 20};
				    	double[] yFc = {firstFcY, lastFcY};//{0, 20};
				    	XYSeries lineSeries = chart.addSeries("linear regression_" + botBlock.getID(), xFc, yFc);
				    	lineSeries.setXYSeriesRenderStyle(XYSeriesRenderStyle.Line);
				    }
				    else {
				    	System.out.println("Trajectory function NULL for block: " + botBlock.getID());
				    }
				    //end ada
				}
		    }
		    
		    
	    }
	    else
	    {
	    	for (int i = 0; i < Game.middleBlocks.size(); i++)
		    {
	    		Block midBlock = Game.middleBlocks.get(i);
		    	if (midBlock.getXHistory().size() >= MINIMUM_HISTORY_SIZE
		    		&& midBlock.getYHistory().size() >= MINIMUM_HISTORY_SIZE
		    		&& midBlock.getAutomatonID() == this.ID)
				{
					chart.addSeries(level.toString() + " Block " + i,
						Game.middleBlocks.get(i).getXHistory(),
						Game.middleBlocks.get(i).getYHistory());
					
					//ada: add regression function line
				    PolynomialFunction blockTrajectoryFunction = midBlock.getBlockTrajectoryFunction();
				    if(blockTrajectoryFunction != null) {
				    	List<Double> xList = Game.middleBlocks.get(i).getXHistory();
				    	List<Double> yList = Game.middleBlocks.get(i).getYHistory();
				    	double firstX = xList.get(0).doubleValue();
				    	double lastX = xList.get(xList.size()-1).doubleValue();
				    	//double firstY = yList.get(0).doubleValue();
				    	//double lastY = yList.get(yList.size()-1).doubleValue();
				    	double firstFcY = blockTrajectoryFunction.value(firstX);
				    	double lastFcY = blockTrajectoryFunction.value(lastX);
				    	double[] xFc = {firstX, lastX};//{0, 20};
				    	double[] yFc = {firstFcY, lastFcY};//{0, 20};
				    	XYSeries lineSeries = chart.addSeries("linear regression_" + midBlock.getID(), xFc, yFc);
				    	lineSeries.setXYSeriesRenderStyle(XYSeriesRenderStyle.Line);
				    }
				    else {
				    	System.out.println("Trajectory function NULL for block: " + midBlock.getID());
				    }
				    //end ada
				}
		    	else if (midBlock.getXHistory().size() >= MINIMUM_HISTORY_SIZE
			    		&& midBlock.getYHistory().size() >= MINIMUM_HISTORY_SIZE)
		    	{
		    		System.out.print(Game.middleBlocks.get(i).getAutomatonID());
		    	}
		    }
	    }
		
		// Show the graph.
		new SwingWrapper(chart).displayChart();
	}
	
	public Game getGame()
	{
		return this.game;
	}
}
