package abstraction;

import automaton.Cell;
import automaton.UpperCA;
import game.Block;
import game.BlockDetector;
import game.BlockType;
import game.Game;
import utils.CheckCoords;

/*
 * This class only holds the AssignBlockMiddleMethod(), which is in charge
 * of assigning a block to the cell (i, j) of the middle automaton given.
 * It deduces it from the neighbors. In case none is found, it creates a new
 * one.
 */
public class AssignBlockMiddle {
    // Static method for block assigning called by every abstraction method.
    public static void AssignBlockMiddleMethod(int i, int j, UpperCA upperCA) {
        Cell[][] middleCells = upperCA.getGrid().getCells();
        

        // If the cell indices are out of bounds, we return.
        if (i < 0 || i >= middleCells.length || j < 0 || j >= middleCells[i].length) {
            return;
        }

        // If the cell already has a block (previously alive), we stay.
        if (CheckCoords.verifyCoordinatesMiddle(i, j) && middleCells[i][j].getBlockId() != -1) {
            return;
        }

        // The temp variables are used to go through the neighbors.
        int temp_i = i == 0 ? 0 : i - 1;
        int temp_j = j == 0 ? 0 : j - 1;

        /*
         * We check if any neighbor is alive and already part of a block. If so,
         * we assign this one to the current cell, and stop.
         */
        for (; temp_i <= i + 1; temp_i++) {
            for (int innerTemp_j = temp_j; innerTemp_j <= j + 1; innerTemp_j++) {
                if ((temp_i != i || innerTemp_j != j)
                        && CheckCoords.verifyCoordinatesMiddle(temp_i, innerTemp_j)
                        && temp_i >= 0 && temp_i < middleCells.length
                        && innerTemp_j >= 0 && innerTemp_j < middleCells[0].length) {
                    if (middleCells[temp_i][innerTemp_j].getBlockId() != -1) {
                        middleCells[i][j].setBlockId(middleCells[temp_i][innerTemp_j].getBlockId(), false);
                        Game.middleBlocks.get(middleCells[temp_i][innerTemp_j].getBlockId()).addCell(middleCells[i][j]);
                        return;
                    }
                }
            }
            temp_j = j == 0 ? 0 : j - 1; // Reinitialize temp_j for the next iteration
        }
        /*
         * In the case no neighbor was found alive or previously alive, we
         * assign a completely new block to the cell and increment the
         * middle block counter.
         */
        middleCells[i][j].setBlockId(BlockDetector.blockIDMiddle, false);
        Game.middleBlocks.add(new Block(BlockDetector.blockIDMiddle++,
                middleCells[i][j], BlockType.MIDDLE, upperCA.getID()));
    }
}
