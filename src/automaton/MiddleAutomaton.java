package automaton;

import java.awt.Dimension;
import java.util.ArrayList;

import abstraction.ComputeMiddle;

/*
 * This class represents the middle automaton, which is basically the exact same
 * as the bottom one but with a bigger granularity.
 */
public class MiddleAutomaton extends AbstractAutomaton {
    // Threshold (in percent) used by the automaton.
    private int threshold;

    // Unique ID of the automaton.
    private int ID;

    // Constructor of the automaton.
    public MiddleAutomaton(Dimension dim, boolean[][] cells, int threshold, int ID) {
        // Middle level and given dimension, as well as its ID.
        this.level = AutomatonLevel.MIDDLE;
        this.size = dim;
        this.ID = ID;

        // Setting up the threshold.
        this.threshold = threshold;

        // Setting up the sub automata, the bottom one.
        this.subAutomata = new ArrayList<AbstractAutomaton>();

        // Updating the super automata.
        this.superAutomata = new ArrayList<AbstractAutomaton>();

        /*
         * Setting up the cell grid. The rules are null because this automaton
         * is only getting input from the bottom one and not computing itself.
         */
        this.cellGrid = new CellGrid(dim, cells, null, AutomatonLevel.MIDDLE);
    }

    /*
     * This function adds an automaton as a sub automaton of the current
     * one. This means the automaton 'auto' is one level under.
     */
    public void addSubAutomaton(AbstractAutomaton auto) {
        this.subAutomata.add(auto);
    }

    /*
     * This function adds an automaton as a super automaton of the current
     * one. This means the automaton 'auto' is one level above.
     */
    public void addSuperAutomaton(AbstractAutomaton auto) {
        this.superAutomata.add(auto);
    }
    //Ahmet
    /*
     * Computes and returns the new cells based on the given sub automaton cells.
     */
    protected boolean[][] computeNewCells(boolean[][] subAutomatonCells) {
        return ComputeMiddle.apply(threshold, subAutomatonCells, this);
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
