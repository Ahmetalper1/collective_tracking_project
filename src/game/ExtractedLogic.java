package game;

import automaton.AutomatonLevel;
import automaton.Cell;
import java.util.List;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;

//ahmet:BlockDetectorClass
//ahmet: these methods is for BlockDetector class. I created these methods for getting rid of code repetation.

public class ExtractedLogic{

    static void DetectBlocks(Game game, Cell[][] cells, int numRows, int numColumns, int blockID,
            List<Block> blockList, BlockType blockType, int automatonID)
    {
        // Iterating through the whole automaton.
        for (int i = 0; i < numRows; i++)
        {
            for (int j = 0; j < numColumns; j++)
            {
                /*
                 * If one alive cell is found and has no block, we launch a
                 * BFS-like search to identify all the new block.
                 */
                if (cells[i][j].isAlive() && cells[i][j].getBlockId() == -1)
                {
                    BFSEquivalent(j, i, blockID, cells, blockList, blockType, automatonID);
                }
            }
        }
    }
	 /*
	  * This function launches a BFS-like search on the automaton from
	  * the given cell at coordinates X and Y. If it has alive neighbors with
	  * no blocks, that means it's part of the new one. This is exclusively used
	  * by the DetectBlocksBottom() method.
	  */

    private static void BFSEquivalent(int x, int y, int blockID, Cell[][] cells, 
		 List<Block> blockList,BlockType blockType, int automatonID)
    {
	 // Setting up the actual cell to the correct blockID if not already set.
	 if (cells[y][x].getBlockId() == -1)
	 {
		 // Setting the current cell with the correct blockID.
		 cells[y][x].setBlockId(blockID, true);

		 // If the block was not created (1st call of this function), we create a new block.
		 if (blockID >= blockList.size())
		 {
			 blockList.add(new Block(blockID, cells[y][x], blockType, automatonID));
		 }
		 // Adding the cell to the block.
		 else
		 {
			 blockList.get(blockID).addCell(cells[y][x]);
		 }
	 }
    }
    
    //ahmet:GRAPH CLASS
    //ahmet: these methods is for graph classes i extracted duplicated logics here.
    
    static void addRegressionLine(XYChart chart, Block block) {
        PolynomialFunction blockTrajectoryFunction = block.getBlockTrajectoryFunction();
        if (blockTrajectoryFunction != null) {
            List<Double> xList = block.getXHistory();
            List<Double> yList = block.getYHistory();
            double firstX = xList.get(0);
            double lastX = xList.get(xList.size() - 1);
            double firstFcY = blockTrajectoryFunction.value(firstX);
            double lastFcY = blockTrajectoryFunction.value(lastX);
            double[] xFc = { firstX, lastX };
            double[] yFc = { firstFcY, lastFcY };
            XYSeries lineSeries = chart.addSeries("linear regression_" + block.getID(), xFc, yFc);
            lineSeries.setXYSeriesRenderStyle(XYSeriesRenderStyle.Line);
        } else {
            System.out.println("Trajectory function NULL for block: " + block.getID() +
                    " with " + block.getXHistory().size() + " points ");
        }
    }
    
    /*ahmet:I edited the code for adding bottom block series and middle block series into separate methods 
    That way code become more organized*/
    //ahmet: i extracted the same logic use in these two method to a new method. 
    
    static void addBlockSeries(XYChart chart, List<Block> blocks, AutomatonLevel level) {
        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            if (Graph.isValidBlock(block)) {
                chart.addSeries(level.toString() + " Block " + i,
                        block.getXHistory(),
                        block.getYHistory());
                ExtractedLogic.addRegressionLine(chart, block);
            }
        }
    }
}