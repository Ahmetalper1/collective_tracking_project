package game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler.LegendPosition;

import automaton.AutomatonLevel;
import logger.LoggerInstance;

public class Graph {
    private static int MINIMUM_HISTORY_SIZE = 2;
    private static Integer DEFAULT_BOTTOM_ID = -1;

    private Game game;
    private AutomatonLevel level;
    private int ID;
    private String title;

    public Graph(Game game, AutomatonLevel level, int ID) {
        this.game = game;
        this.level = level;
        this.ID = ID;

        if (level != AutomatonLevel.BOTTOM) {
            Graph.MINIMUM_HISTORY_SIZE = 2;
            this.title = "Middle Automaton | Threshold: " +
                    game.getMiddleAutomaton().get(ID).getThreshold() +
                    " | Granularity: " + Game.granularity.toString() +
                    " | Size: " + Game.MIDDLE_ROW + "x" + Game.MIDDLE_COLUMN + ".";
        } else {
            this.title = "Bottom Automaton | No Abstraction | Size: " +
                    Game.BOTTOM_ROW + "x" + Game.BOTTOM_COLUMN + ".";
        }
    }

    public void display() {
        LoggerInstance.LOGGER.log(Level.FINEST, "Displaying graph ...");

        XYChart chart = createChart();

        if (this.level == AutomatonLevel.BOTTOM) {
            addBottomBlockSeries(chart);
        } else {
            addMiddleBlockSeries(chart);
        }

        new SwingWrapper<>(chart).displayChart();
    }
    
    //ahmet: I added new method for chart creation for readability and organization reasons.
    
    private XYChart createChart() {
        XYChart chart = new XYChart(800, 600);
        chart.getStyler().setChartTitleBoxBorderColor(Color.WHITE);
        chart.setTitle(title);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Scatter);
        chart.getStyler().setLegendPosition(LegendPosition.OutsideE);
        chart.getStyler().setMarkerSize(16);
        chart.getStyler().setChartBackgroundColor(Color.DARK_GRAY);
        chart.getStyler().setPlotBackgroundColor(Color.DARK_GRAY);
        chart.getStyler().setAxisTickLabelsColor(Color.WHITE);
        return chart;
    }
    
    /*ahmet:I edited the code for adding bottom block series and middle block series into separate methods 
    That way code become more organized*/
    //ahmet: i extracted the same logic use in these two method to a new method. 
    private void addBlockSeries(XYChart chart, List<Block> blocks) {
        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            if (isValidBlock(block)) {
                chart.addSeries(level.toString() + " Block " + i,
                        block.getXHistory(),
                        block.getYHistory());
                addRegressionLine(chart, block);
            }
        }
    }

    private void addBottomBlockSeries(XYChart chart) {
        addBlockSeries(chart, Game.bottomBlocks);
    }

    private void addMiddleBlockSeries(XYChart chart) {
        List<Block> relevantBlocks = new ArrayList<>();
        for (Block midBlock : Game.middleBlocks) {
            if (isValidBlock(midBlock) && midBlock.getAutomatonID() == this.ID) {
                relevantBlocks.add(midBlock);
            }
        }
        addBlockSeries(chart, relevantBlocks);
    }

    //ahmet:I this method to check if a block has a valid history size.that way we avoid duplicating the validation logic in multiple places.
    
    private boolean isValidBlock(Block block) {
        return block.getXHistory().size() >= MINIMUM_HISTORY_SIZE &&
                block.getYHistory().size() >= MINIMUM_HISTORY_SIZE;
    }

    private void addRegressionLine(XYChart chart, Block block) {
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

    public Game getGame() {
        return game;
    }

    public static Integer getDefaultBottomId() {
        return DEFAULT_BOTTOM_ID;
    }

    public static void setDefaultBottomId(Integer defaultBottomId) {
        DEFAULT_BOTTOM_ID = defaultBottomId;
    }
}
