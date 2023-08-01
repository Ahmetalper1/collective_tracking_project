package automaton;

import java.awt.Dimension;
import java.util.List;

/*
 * This class represents any automaton. It contains the basic attributes and
 * methods needed for an automaton to work. The three other automaton classes
 * (bottom, middle, and top) will extend this class for optimization.
 */
public abstract class AbstractAutomaton {
    // Level of the automaton, either bottom, middle, or top.
    protected AutomatonLevel level;

    // Dimension of the automaton, contains width and height.
    protected Dimension size;

    /*
     * List of all the sub automata (for middle and top only!).
     * These are the ones that will give the input to the current automaton.
     */
    protected List<AbstractAutomaton> subAutomata;

    /*
     * List of all superAutomata (for bottom and middle only!).
     * These are the ones that will receive the output of the current automaton.
     */
    protected List<AbstractAutomaton> superAutomata;

    /*
     * The grid containing the cells and the rules applied to the grid
     * of the automaton, may be null for the top automaton.
     */
    protected CellGrid cellGrid;

    /*
     * The view is what makes the automaton visible. It consists of a JFrame
     * with some modification.
     */
    protected View view;

    /*
     * Rules that will be applied through every iteration.
     */
    protected Rules rules;

	//ahmet
    /*
     * This function updates the state of the automaton
     * based on the information retrieved from the sub automaton(s).
     */
    public void updateCells() {
        boolean[][] subAutomatonCells = retrieveSubAutomatonCells();
        boolean[][] newCells = computeNewCells(subAutomatonCells);
        this.cellGrid.updateCells(newCells);
    }
    //ahmet
    /*
     * Retrieves the cells from the sub automaton(s)
     * and returns them as a 2D boolean array.
     */
    protected boolean[][] retrieveSubAutomatonCells() {
        AbstractAutomaton subAutomaton = this.subAutomata.get(0);
        boolean[][] tempCells = new boolean[subAutomaton.size.height][subAutomaton.size.width];

        for (int i = 0; i < subAutomaton.size.height; i++) {
            for (int j = 0; j < subAutomaton.size.width; j++) {
                tempCells[i][j] = subAutomaton.cellGrid.getState(i, j) == 1;
            }
        }

        return tempCells;
    }

    /*
     * Computes and returns the new cells based on the given sub automaton cells.
     * This method is abstract and must be implemented by the subclasses.
     */
    protected abstract boolean[][] computeNewCells(boolean[][] subAutomatonCells);
}
