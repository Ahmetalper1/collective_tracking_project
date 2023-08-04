package automaton;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import abstraction.ComputeMiddle;
import abstraction.ComputeTop;

/*
 * This class represents the combined automaton, which includes the functionalities
 * of the previous bottom, middle, and top automaton classes.
 */
public class UpperCA extends AbstractAutomaton {
    // Threshold (in percent) used by the automaton.
    private int threshold;

    // Unique ID of the automaton.
    private int ID;

    // Constructor of the automaton for middle automaton.
    public UpperCA(Dimension dim, boolean[][] cells, int threshold, int ID) {
        // Set the level, dimension, and ID for middle automaton.
        this.level = AutomatonLevel.MIDDLE;
        this.size = dim;
        this.ID = ID;

        // Setting up the threshold.
        this.threshold = threshold;

        // Setting up the sub automata for middle and top.
        this.subAutomata = new ArrayList<AbstractAutomaton>();

        // Updating the super automata for bottom and middle.
        this.superAutomata = new ArrayList<AbstractAutomaton>();

        /*
         * Setting up the cell grid. The rules are null because this automaton
         * is getting input from the bottom one and not computing itself.
         */
        this.cellGrid = new CellGrid(dim, cells, null, AutomatonLevel.MIDDLE);
    }

    // Constructor of the automaton for top automaton.
    public UpperCA(Dimension dim, boolean[][] cells, int threshold) {
        // Set the level and dimension for top automaton.
        this.level = AutomatonLevel.TOP;
        this.size = dim;

        // Setting up the threshold.
        this.threshold = threshold;

        // Setting up the sub automata for middle and top.
        this.subAutomata = new ArrayList<AbstractAutomaton>();

        /*
         * Setting up the cell grid. The rules are null because this automaton
         * is getting input from the bottom one and not computing itself.
         */
        this.cellGrid = new CellGrid(dim, cells, null, AutomatonLevel.TOP);
    }

    /*
     * This function adds an automaton as a sub automaton of the current
     * one. This means the automaton 'auto' is one level under.
     */
    public void addSubAutomaton(AbstractAutomaton auto) {
        this.subAutomata.add(auto);
    }


    /*
     * Computes and returns the new cells based on the given sub automaton cells.
     * This method will determine which computation method to use based on the
     * automaton's level.
     */
    protected boolean[][] computeNewCells(boolean[][] subAutomatonCells) {
        boolean[][] newCells;

        if (this.level == AutomatonLevel.MIDDLE) {
            newCells = ComputeMiddle.apply(threshold, subAutomatonCells, this);
        } else if (this.level == AutomatonLevel.TOP) {
            newCells = ComputeTop.apply(threshold, subAutomatonCells);
        } else {
            // This case should not occur in the UpperCA.
            throw new RuntimeException("Invalid automaton level.");
        }
        return newCells;
    }

    public CellGrid getGrid() {
        return this.cellGrid;
    }

    public int getThreshold() {
        return this.threshold;
    }

    public int getID() {
        return this.ID;
    }
}