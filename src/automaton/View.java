package automaton;

import java.awt.*;
import javax.swing.*;

import game.Game;

public class View {

    // Dimensions for the Bottom Automaton.
    public static final int BOTTOM_WIDTH = Game.BOTTOM_COLUMN * 15;
    public static final int BOTTOM_HEIGHT = Game.BOTTOM_ROW * 15;

    // Dimensions for the Upper Automaton.
    public static final int UPPER_WIDTH = 300;
    public static final int UPPER_HEIGHT = 150;

    // This represents the space between two cells, vertically and horizontally.
    public static final int GAP_BETWEEN_CELLS = 1;

    private JFrame frame;
    private AutomatonLevel level;

    public View(Dimension dim, Cell[][] cells, AutomatonLevel level) {
        frame = new JFrame();
        frame.getContentPane().setBackground(Color.BLACK);
        this.level = level;

        frame.setLayout(new GridLayout(dim.height, dim.width, GAP_BETWEEN_CELLS, GAP_BETWEEN_CELLS));

        for (int i = 0; i < dim.height; i++) {
            for (int j = 0; j < dim.width; j++) {
                frame.add(cells[i][j].getGraphic());
            }
        }

        setFrameSizeBasedOnLevel();
    }

    private void setFrameSizeBasedOnLevel() {
        switch (level) {
            case BOTTOM:
                frame.setSize(BOTTOM_WIDTH, BOTTOM_HEIGHT);
                break;
            case UPPER:
                frame.setSize(UPPER_WIDTH, UPPER_HEIGHT);
                break;
            default:
                throw new IllegalArgumentException("Invalid automaton level.");
        }
    }

    public JFrame getFrame() {
        return this.frame;
    }

    public void updateFrame() {
        if (frame != null) {
            SwingUtilities.updateComponentTreeUI(frame);
        }
    }
}
