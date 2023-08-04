
package abstraction;

import automaton.UpperCA;
import game.Game;

public class ComputeMiddle {
    public static int DEFAULT_SQUARE_SIZE = 10;

    /*
     * This function computes the new middle automaton cells, thanks to the
     * bottom ones, and the threshold selected.
     */
    public static boolean[][] apply(int threshold, boolean[][] bottomCells, UpperCA automaton) {
        boolean[][] newCells = new boolean[Game.MIDDLE_ROW][Game.MIDDLE_COLUMN];
        int square_size = DEFAULT_SQUARE_SIZE;

        switch (Game.granularity) {
            case SQUARE_2x2:
                square_size = 2;
                break;
            case SQUARE_3x3:
                square_size = 3;
                break;
            case SQUARE_4x4:
                square_size = 4;
                break;
            case SQUARE_8x8:
                square_size = 8;
                break;
            default:
                break;
        }

        for (int i = 0; i < Game.MIDDLE_ROW; i++) {
            for (int j = 0; j < Game.MIDDLE_COLUMN; j++) {
                /* 
                 * This counter represents the number of alive cells in the
                 * square_size x square_size sub grid we are working on.
                 */
                int counter = 0;
                for (int h = 0; h < square_size; h++) {
                    for (int w = 0; w < square_size; w++) {
                        int row = i * square_size + h;
                        int col = j * square_size + w;
                        if (row >= 0 && row < bottomCells.length &&
                            col >= 0 && col < bottomCells[row].length &&
                            bottomCells[row][col]) {
                            counter++;
                        }
                    }
                }

                if (counter >= threshold) {
                    newCells[i][j] = true;

                    if (automaton != null) {
                        AssignBlockMiddle.AssignBlockMiddleMethod(i, j, automaton);
                    }
                } else if (automaton != null && i >= 0 && i < automaton.getGrid().getCells().length &&
                           j >= 0 && j < automaton.getGrid().getCells()[i].length &&
                           automaton.getGrid().getCells()[i][j].getBlockId() != -1) {
                    Game.middleBlocks.get(automaton.getGrid().getCells()[i][j].getBlockId())
                        .removeCell(automaton.getGrid().getCells()[i][j]);
                    automaton.getGrid().getCells()[i][j].setBlockId(-1, false);
                }
            }
        }

        return newCells;
    }
}
