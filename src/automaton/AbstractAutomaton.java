package automaton;

import java.awt.Dimension;
import java.util.List;

/*
 * This class represents any automaton. It contains the basic attributes and
 * methods needed for an automaton to work. The three other automaton classes
 * (bottom, middle and top) will extend this class for optimization.
 */
abstract public class AbstractAutomaton
{	
	// Level of the automaton, either bottom, middle or top.
	protected AutomatonLevel level;
	
	// Dimension of the automaton, contains width and height.
	protected Dimension size;
	
	/*
	 *  List of all the sub automata (for middle and top only !).
	 *  These are the ones that will give the input to the current automaton.
	 */
	protected List<AbstractAutomaton> subAutomata;
	
	/*
	 * List of all superAutomata (for bottom and middle only !).
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
}
