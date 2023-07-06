package game;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import automaton.AutomatonLevel;
import automaton.MiddleAutomaton;
import automaton.TopAutomaton;
import automaton.View;
import logger.LoggerInstance;
import utils.TickHistory;

/*
 * This simulator class is the class that will be able to make the iterations.
 * It will update the automatons with its run() method. We HAVE to extend it
 * from the Thread class because the Automata's views are JFrames, therefore 
 * they are using EDT (Event Dispatch Thread). So in order to modify the view
 * while it's displayed, we need another thread.
 */

public class Simulator extends Thread {
	
	// Limit iteration. After this, we launch interpolations and display graph.	
    public static int LIMIT_STEP_ANALYSIS = 100;
    
	// The current game containing the automata
    private Game game;
    // The graph displaying the history of blocks.
    private Graph bottomGraph;
    private List<Graph> middleGraphs;
    
    // Constructor of the class.
    public Simulator(Game game) {
        LoggerInstance.LOGGER.log(Level.FINEST, "Creating the simulator ...");

        this.game = game;

        this.bottomGraph = new Graph(game, AutomatonLevel.BOTTOM, Graph.getDefaultBottomId());

        this.middleGraphs = new ArrayList<Graph>();

        createMiddleGraphs();

        Game.setupBlocks();
    }
    
	/*
	 * Overwritten method that is called when the simulator object is started
	 * with .start(). This creates a parallel thread.
	 */

    @Override
    public void run() {
        LoggerInstance.LOGGER.log(Level.FINEST, "Starting the iterations ...");
        
		/*
		 * This sets every automaton's JFrame to the right place according to
		 * the total number of automata.
		 */

        setWindowsLocations();

        int iteration = 0;

        while (true) {
            try {
    			// Stopping the thread to let some time to display/see.
                Thread.sleep(50);
        		
                // Detecting new blocks if new blocks are found !
                BlockDetector.DetectBlocksBottom(game);
                BlockDetector.DetectBlocksMiddle(game);//ada: commented temp!!!!!!!!!!!!!!!!!!!!!!!!
                
                // This updates every block center.
                BlockDetector.updateBlockCenteres(game, iteration);

                game.getBottomAutomaton().nextIteration();//applying the rules to the CA

                // Handles blocks divisions and merges.
                BlockDetector.DetectMergeAndDivisionBottom(game);

                // Updating all middle automata cells.
                for (MiddleAutomaton middle : game.getMiddleAutomaton()) {
                    middle.updateCells();
                }

                // Updating all the top automata cells.
                for (TopAutomaton top : game.getTopAutomaton()) {
                    top.updateCells();
                }
                
				/*
				 * This is executed when we reach the limit previously set by
				 * the user on the menu.
				 */                
                
                if (iteration == LIMIT_STEP_ANALYSIS) {
					// This is where we can start the analysis of the blocks.                	
                    LoggerInstance.LOGGER.log(Level.FINEST, "Starting the analysis of blocks ...");

                    // This counts the number of blocks, for printing purposes.
                    int blockCounter = 0;

					////ada: analysis moved here (from after the display)
					// Analyzing each block.
                    for (Block block : Game.bottomBlocks) {
                        System.out.println("Bottom Block " + blockCounter + ": " + HistoryAnalyzer.analyzeBlock(block));

                        block.getTickHistory().FillMissingTicks(LIMIT_STEP_ANALYSIS);

                        blockCounter++;
                    }

                    blockCounter = 0;

                    for (Block block : Game.middleBlocks) {
                        System.out.println("Middle Block " + blockCounter + " (Threshold = " +
                                game.getMiddleAutomaton().get(block.getAutomatonID()).getThreshold() + " cell(s))" +
                                ": " + HistoryAnalyzer.analyzeBlock(block));

                        block.getTickHistory().FillMissingTicks(LIMIT_STEP_ANALYSIS);

                        blockCounter++;
                    }
            		////
                    
					// Displaying every graph of movements of the blocks.
                    if (Game.bottomBlocks.size() > 0) {
                        bottomGraph.display();
                    }

                    for (Graph graph : this.middleGraphs) {
                        if (Game.middleBlocks.size() > 0) {
                            graph.display();
                        }
                    }

                    TickHistory.FillBottomFile(game.getBottomFW());
                    TickHistory.FillMiddleFile(game.getMiddleFW());

                    game.getBottomFW().close();
                    game.getMiddleFW().close();

                    return;
                }

                iteration++;
        	
                // If the program crashes, we catch it and exit safely.        
            } catch (InterruptedException e) {
                LoggerInstance.LOGGER.log(Level.FINEST, "InterruptedException in Simulator.run()! Stopping the program.");
                System.exit(1);
            } catch (IOException e) {
                LoggerInstance.LOGGER.log(Level.SEVERE, "IOException in Simulator.run(): " +
                        "Could not write the output files. Error message: " + e.getMessage());
            }
        }
    }

    private void setWindowsLocations() {
    	
    	// This is used to get the dimensions of the screen for JFrame position.
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

		// Displaying every automaton before launching the simulation.
        game.getBottomAutomaton().getGrid().display((screen.width / 2) - (View.BOTTOM_WIDTH / 2),
                screen.height - View.BOTTOM_HEIGHT,
                "Bottom Automaton | " + Game.BOTTOM_ROW + "x" + Game.BOTTOM_COLUMN + ".");

		/*
		 *  These offset represent the point on the screen where the window will
		 *  be located. We increment these while iterating through automata.
		 */
      
        int widthOffset = screen.width / (game.getMiddleAutomaton().size() + 1);
        int heightOffset = screen.height - View.BOTTOM_HEIGHT - View.MIDDLE_HEIGHT;

		/*
		 * Going through every automaton and setting its location in order for
		 * it to be centered with the other automata.
		 */
        
        for (MiddleAutomaton middle : game.getMiddleAutomaton()) {
            middle.getGrid().display(widthOffset - (View.MIDDLE_WIDTH / 2),
                    heightOffset, "Middle Automaton | " + Game.MIDDLE_ROW + "x" +
                            Game.MIDDLE_COLUMN + " | Threshold : " + middle.getThreshold() + ".");

            widthOffset += screen.width / (game.getMiddleAutomaton().size() + 1);
        }
		// Updating the offsets for the top automata
        widthOffset = screen.width / (game.getTopAutomaton().size() + 1);
        heightOffset = screen.height - View.BOTTOM_HEIGHT - View.MIDDLE_HEIGHT - View.TOP_HEIGHT;
		
        /*
		 * Going through every automaton and setting its location in order for
		 * it to be centered with the other automata.
		*/
        
        for (TopAutomaton top : game.getTopAutomaton()) {
            top.getGrid().display(widthOffset - (View.TOP_WIDTH / 2),
                    heightOffset, "Top Automaton | 1x1 | Threshold : " +
                            top.getThreshold() + "%.");

            widthOffset += screen.width / (game.getTopAutomaton().size() + 1);
        }
    }

    private void createMiddleGraphs() {
        for (int i = 0; i < this.game.getMiddleAutomaton().size(); i++) {
            this.middleGraphs.add(new Graph(this.game, AutomatonLevel.MIDDLE, i));
        }
    }
}
