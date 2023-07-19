package automaton;

import java.awt.Dimension;
import java.util.ArrayList;

import game.Game;
import utils.CheckCoords;

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
        boolean[][] nextCells = new boolean[Game.BOTTOM_ROW][Game.BOTTOM_COLUMN];

        for (int i = 0; i < Game.BOTTOM_ROW; i++) {
            for (int j = 0; j < Game.BOTTOM_COLUMN; j++) {
                int neighbourCount = countNeighbors(i, j);
                applyRules(i, j, neighbourCount, nextCells);
            }
        }

        cellGrid.updateCells(nextCells);
    }

    /*
     * This function counts the number of alive neighbors for a cell at height i
     * and width j.
     */
    private int countNeighbors(int i, int j) {
        int neighbourCount = 0;

        neighbourCount += getNeighborState(i - 1, j - 1);
        neighbourCount += getNeighborState(i - 1, j);
        neighbourCount += getNeighborState(i - 1, j + 1);
        neighbourCount += getNeighborState(i, j - 1);
        neighbourCount += getNeighborState(i, j + 1);
        neighbourCount += getNeighborState(i + 1, j - 1);
        neighbourCount += getNeighborState(i + 1, j);
        neighbourCount += getNeighborState(i + 1, j + 1);

        return neighbourCount;
    }

    /*
     * This function retrieves the state of the neighbor cell at position (i, j),
     * considering it as alive if it falls within the grid and its state is 1.
     * Otherwise, it returns 0.
     */
    private int getNeighborState(int i, int j) {
        if (CheckCoords.verifyCoordinatesBottom(i, j)) {
            return cellGrid.getState(i, j);
        }
        return 0; // Default value for cells outside the grid
    }

    /*
     * This function applies the rules and updates the state of the cell to the
     * cells array, which represents the next generation cells. It also updates
     * the blockID of the cell if necessary.
     */
    private void applyRules(int i, int j, int neighbourCount, boolean[][] cells) {
        boolean isAlive = cellGrid.getState(i, j) == 1;
        boolean[] aliveRules = rules.getAliveRules();
        boolean[] deadRules = rules.getDeadRules();

        if (isAlive && aliveRules[neighbourCount]) {
            setNewBlock(i, j, cellGrid);
            cells[i][j] = true;
        } else if (!isAlive && deadRules[neighbourCount]) {
            setNewBlock(i, j, cellGrid);
            cells[i][j] = true;
        } else {
            if (cellGrid.getCells()[i][j].getBlockId() != -1) {
                Game.bottomBlocks.get(cellGrid.getCells()[i][j].getBlockId())
                        .removeCell(cellGrid.getCells()[i][j]);
                cellGrid.getCells()[i][j].setBlockId(-1, false);
            }
            cells[i][j] = false;
        }
    }

    /*
     * This function assigns the cell to a new block based on its neighbors' blocks.
     * If no suitable neighbor block is found, it sets the cell's blockID to -1.
     */
    private void setNewBlock(int i, int j, CellGrid cellGrid) {
        boolean blockSet = false;

        for (int temp_i = i - 1; temp_i <= i + 1 && !blockSet; temp_i++) {
            for (int temp_j = j - 1; temp_j <= j + 1 && !blockSet; temp_j++) {
                if (!(temp_i == i && temp_j == j) && CheckCoords.verifyCoordinatesBottom(temp_i, temp_j)
                        && cellGrid.getCells()[temp_i][temp_j].getBlockId() != -1) {
                    if (cellGrid.getCells()[i][j].getBlockId() != -1) {
                        Game.bottomBlocks.get(cellGrid.getCells()[i][j].getBlockId())
                                .removeCell(cellGrid.getCells()[i][j]);
                    }

                    cellGrid.getCells()[i][j].setBlockId(cellGrid.getCells()[temp_i][temp_j].getBlockId(), false);

                    Game.bottomBlocks.get(cellGrid.getCells()[temp_i][temp_j].getBlockId())
                            .addCell(cellGrid.getCells()[i][j]);

                    blockSet = true;
                }
            }
        }

        if (!blockSet) {
            setPreviousBlock(i, j, cellGrid);
        }
    }

    /*
     * This function assigns the cell to the block of a suitable neighbor based on
     * the previous block ID. If no suitable neighbor block is found, it leaves the
     * cell's blockID as it is (-1 if it doesn't belong to any block).
     */
    private void setPreviousBlock(int i, int j, CellGrid cellGrid) {
        for (int temp_i = i - 1; temp_i <= i + 1; temp_i++) {
            for (int temp_j = j - 1; temp_j <= j + 1; temp_j++) {
                if (!(temp_i == i && temp_j == j) && CheckCoords.verifyCoordinatesBottom(temp_i, temp_j)
                        && cellGrid.getCells()[temp_i][temp_j].getPreviousBlockId() != -1) {
                    if (cellGrid.getCells()[i][j].getBlockId() != -1) {
                        Game.bottomBlocks.get(cellGrid.getCells()[i][j].getBlockId())
                                .removeCell(cellGrid.getCells()[i][j]);
                    }

                    cellGrid.getCells()[i][j].setBlockId(cellGrid.getCells()[temp_i][temp_j].getPreviousBlockId(), false);

                    Game.bottomBlocks.get(cellGrid.getCells()[temp_i][temp_j].getPreviousBlockId())
                            .addCell(cellGrid.getCells()[i][j]);
                }
            }
        }
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
