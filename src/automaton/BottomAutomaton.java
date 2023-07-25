package automaton;

import java.awt.Dimension;
import java.util.ArrayList;

/*
 * This class represents the bottom automaton, which is the one that contains
 * the grid. It's the base of the program, and it is here that we apply the
 * rules to get to the next iteration.
 */
public class BottomAutomaton extends AbstractAutomaton {
    // Constructor of the automaton.
    public BottomAutomaton(Dimension dim, boolean[][] cells, Rules rules) {
        // Bottom level and given dimension.
        this.level = AutomatonLevel.BOTTOM;
        this.size = dim;

        // No sub automata as it is the bottom one.
        this.subAutomata = null;

        // Updating the super automata.
        this.superAutomata = new ArrayList<AbstractAutomaton>();

        // Setting up the cell grid.
        this.cellGrid = new CellGrid(dim, cells, rules, AutomatonLevel.BOTTOM);

        // Setting up the rules.
        this.rules = rules;
    }

    /*
     * This function adds an automaton as a super automaton of the current
     * one. This means the automaton 'auto' is one level above.
     */
    public void addSuperAutomaton(AbstractAutomaton auto) {
        this.superAutomata.add(auto);
    }

    /*
     * This function updates the CellGrid grid to the next iteration.
     */
    public void nextIteration() {
        AutomatonUtils.nextIteration(size, cellGrid, rules);
    }

    public CellGrid getGrid() {
        return this.cellGrid;
    }

    @Override
    protected boolean[][] computeNewCells(boolean[][] subAutomatonCells) {
        // TODO Auto-generated method stub
        return null;
    }
}
