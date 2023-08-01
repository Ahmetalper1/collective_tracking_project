package automaton;

import java.awt.*;
import javax.swing.*;

import game.Game;

/*
 * This class represents the visual part of the automaton. This is what is 
 * going to be displayed when the simulator is launched.
 */
public class View
{
	// View sýnýfýnda BOTTOM_WIDTH ve BOTTOM_HEIGHT sabitleri
	public static int BOTTOM_WIDTH = Game.BOTTOM_COLUMN * 15;
	public static int BOTTOM_HEIGHT = Game.BOTTOM_ROW * 15;

	// Dimensions for the Middle Automaton.
	public static int MIDDLE_WIDTH = 300;//250;
	public static int MIDDLE_HEIGHT = 150;//125;
	
	// Dimensions for the Top Automaton.
	public static int TOP_WIDTH = 150;//250;
	public static int TOP_HEIGHT = 75;//125;
	
	// This represents the space between two cells, vertically and horizontally.
	public static int GAP_BETWEEN_CELLS = 1;
	
	// The frame that contains everything and will be displayed.
	private JFrame frame;
	
	// Automaton level.
	private AutomatonLevel level;
	
	// Constructor of the class.
	public View(Dimension dim, Cell[][] cells, AutomatonLevel level)
	{
		// Instantiating the frame.
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.BLACK);
		
		// Setting the given level.
		this.level = level;
		
		/*
		 * Setting the frame layout to GridLayout. This means it will be
		 * organized as a grid of dim.height rows and dim.width columns.
		 */
		frame.setLayout(new GridLayout(dim.height, dim.width,
				GAP_BETWEEN_CELLS, GAP_BETWEEN_CELLS));
		
		// Filling up the frame with the cells' JButtons.
		for (int i = 0; i < dim.height; i++)
		{
			for (int j = 0; j < dim.width; j++)
			{
				frame.add(cells[i][j].getGraphic());
			}
		}
		
		// Setting the frame size according to the automaton level.
		if (this.level == AutomatonLevel.BOTTOM)
		{
			frame.setSize(BOTTOM_WIDTH, BOTTOM_HEIGHT);
		}
		else if (this.level == AutomatonLevel.MIDDLE)
		{
			frame.setSize(MIDDLE_WIDTH, MIDDLE_HEIGHT);
		}
		if (this.level == AutomatonLevel.TOP)
		{
			frame.setSize(TOP_WIDTH, TOP_HEIGHT);
		}
	}
	
	public JFrame getFrame()
	{
		return this.frame;
	}
	
	public void updateFrame()
	{
		if (frame != null)
			SwingUtilities.updateComponentTreeUI(frame);
	}
}