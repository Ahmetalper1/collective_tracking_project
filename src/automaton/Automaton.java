package automaton;

import java.awt.Dimension;
import java.util.ArrayList;

import abstraction.ComputeMiddle;
import abstraction.ComputeTop;

/*
 * This class represents an automaton, which can be either at the middle level
 * or the top level. It gets input from its parent automaton and analyzes the data 
 * in order to produce a human-readable output at the top level.
 */
public class Automaton extends AbstractAutomaton {
    private int threshold;
    private int ID; // Unique ID for the automaton.

    // Constructor of the automaton at the middle level.
    public Automaton(Dimension dim, boolean[][] cells, int threshold, int ID) {
        this(dim, cells, threshold);
        this.ID = ID;
    }

    // Constructor of the automaton at the top level.
    public Automaton(Dimension dim, boolean[][] cells, int threshold) {
        if (cells == null) {
            throw new IllegalArgumentException("Cells cannot be null.");
        }

        this.level = cells[0].length > 0 ? AutomatonLevel.MIDDLE : AutomatonLevel.TOP;
        this.size = dim;
        this.threshold = threshold;
        this.subAutomata = new ArrayList<>();
        this.cellGrid = new CellGrid(dim, cells, null, this.level);

        if (this.level == AutomatonLevel.TOP) {
            this.superAutomata = null;
        } else {
            this.superAutomata = new ArrayList<>();
        }
    }

    /*
     * This function adds an automaton as a sub automaton of the current one.
     * This means the automaton 'auto' is one level under.
     */
    public void addSubAutomaton(AbstractAutomaton auto) {
        this.subAutomata.add(auto);
    }

    /*
     * This function adds an automaton as a super automaton of the current one.
     * This means the automaton 'auto' is one level above.
     */
    public void addSuperAutomaton(AbstractAutomaton auto) {
        this.superAutomata.add(auto);
    }

    /*
     * Computes and returns the new cells based on the given sub automaton cells.
     */
    protected boolean[][] computeNewCells(boolean[][] subAutomatonCells) {
        if (this.level == AutomatonLevel.MIDDLE) {
            return ComputeMiddle.apply(threshold, subAutomatonCells, this);
        } else {
            return ComputeTop.apply(threshold, subAutomatonCells);
        }
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
