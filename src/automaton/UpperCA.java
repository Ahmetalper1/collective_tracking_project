package automaton;

import java.awt.Dimension;
import java.util.ArrayList;
import abstraction.ComputeMiddle;
import abstraction.ComputeTop;
import game.Game;

public class UpperCA extends AbstractAutomaton {
    private int threshold;
    private int ID;

    public UpperCA(Dimension dim, boolean[][] cells, int threshold, int ID) {
        this.level = AutomatonLevel.UPPER;
        this.size = dim;
        this.threshold = threshold;
        this.ID = ID;

        this.subAutomata = new ArrayList<AbstractAutomaton>();
        this.superAutomata = new ArrayList<AbstractAutomaton>();

        // Ensure that a valid cells array is passed to the CellGrid constructor
        if (cells == null) {
            cells = new boolean[dim.height][dim.width];
        }
        this.cellGrid = new CellGrid(this.size, cells, null, AutomatonLevel.UPPER);
    }

    public void addSubAutomaton(AbstractAutomaton auto) {
        this.subAutomata.add(auto);
    }

    protected boolean[][] computeNewCells(boolean[][] subAutomatonCells) {
        if (this.size.width == Game.MIDDLE_COLUMN && this.size.height == Game.MIDDLE_ROW) {
            return ComputeMiddle.apply(threshold, subAutomatonCells);
        } else if (this.size.width == Game.TOP_COLUMN && this.size.height == Game.TOP_ROW) {
            return ComputeTop.apply(threshold, subAutomatonCells);
        } else {
            throw new RuntimeException("Invalid automaton dimension: " + this.size.width + "x" + this.size.height);
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
