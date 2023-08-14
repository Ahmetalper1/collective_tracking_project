package abstraction;

import game.Game;

public class ComputeMiddle {

    public static int DEFAULT_SQUARE_SIZE = 10;

    public static boolean[][] apply(int threshold, boolean[][] bottomCells) {
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

                newCells[i][j] = counter >= threshold;
            }
        }

        return newCells;
    }
}
