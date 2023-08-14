package abstraction;

import game.Game;

public class ComputeTop {

    public static boolean[][] apply(int threshold, boolean[][] midCells) {
        boolean[][] newCells = new boolean[Game.TOP_ROW][Game.TOP_COLUMN];
        
        int totalAliveMiddleCells = 0;
        for (int i = 0; i < midCells.length; i++) {
            for (int j = 0; j < midCells[i].length; j++) {
                if (midCells[i][j]) {
                    totalAliveMiddleCells++;
                }
            }
        }

        newCells[0][0] = totalAliveMiddleCells >= threshold;
        
        return newCells;
    }
}
