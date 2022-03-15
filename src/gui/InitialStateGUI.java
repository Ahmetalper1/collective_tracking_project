package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import game.Game;
import listeners.SaveButtonListener;
import patterns.Blinker;
import patterns.Block;
import patterns.Glider;
import patterns.GliderGun;
import patterns.Herschel;
import patterns.Pattern;
import listeners.ClearButtonListener;
import listeners.ColouredButtonListener;

/*
 * This class is the GUI for the Initial State Selection. It is a grid of cells
 * you can turn ON/OFF by clicking on it. It represents the initial state of
 * the Level 0 automaton at iteration 0.
 */
public class InitialStateGUI extends JFrame
{
	// This is used for the size of each cell (button).
	// TODO:: Change these hardcoded values.
	public static final int BUTTON_WIDTH = 67;
	public static final int BUTTON_HEIGHT = 25;
	
	// The size of the grid displayed.
	// TODO:: Change these hardcoded values.
	public static final int ROWS = Game.BOTTOM_ROW;
	public static final int COLUMNS = Game.BOTTOM_COLUMN;

	// The panel used by the JFrame.
	private JPanel grid;

	// Layout of the menu.
	private GridLayout layout;

	// The two dimensional array that holds all the coloured buttons displayed.
	private ColouredButton cells[][];
	
	// Simple save and clear buttons.
	private JButton save;
	private JButton clear;
	
	// Constructor of the class.
	public InitialStateGUI()
	{
		// Setting up the layout and the panel.
		this.setLayout(new BorderLayout());
		layout = new GridLayout(ROWS, COLUMNS);
		grid = new JPanel(layout);
		
		// Setting up the buttons as well as their respective listeners.
		save = new JButton("Save");
		save.addActionListener(new SaveButtonListener(this));
		clear = new JButton("Clear");
		clear.addActionListener(new ClearButtonListener(this));
		
		// Instantiating all the cells.
		cells = new ColouredButton[ROWS][COLUMNS];

		// Creating each cell.
		for (int i = 0; i < ROWS; i++)
		{
			for (int j = 0; j < COLUMNS; j++)
			{
				/*
				 * Creating each button by default dead, with its coordinates as
				 * visible text. We also set its size and listener.
				 */
				ColouredButton button = 
						new ColouredButton(ColouredButton.DEAD_COLOUR);
				button.addActionListener(new ColouredButtonListener(button));
				button.setText(i + " ; " + j);
				button.setPreferredSize(
						new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
				cells[i][j] = button;
				grid.add(cells[i][j]);
			}
		}
		
		// Indicating the size of the grid.
		this.setSize(1500, 1000);
		this.add(grid, BorderLayout.CENTER);
		
		// Placing at the right spot the save and clear buttons.
		JPanel savePanel = new JPanel();
		savePanel.add(save);
		savePanel.add(clear);
		this.add(savePanel, BorderLayout.SOUTH);
	}
	
	/*
	 * This function can update the initial state cells from the selected
	 * pattern. The pattern is selected by the user on the initial menu.
	 */
	public void selectPattern(Pattern pattern)
	{
		switch (pattern)
		{
			case GLIDER:
				Glider.generate(cells);
				break;
			case BLINKER:
				Blinker.generate(cells);
				break;
			case BLOCK:
				Block.generate(cells);
				break;
			case HERSCHEL:
				Herschel.generate(cells);
				break;
			case GLIDER_GUN:
				GliderGun.generate(cells);
			default:
				break;
		}
	}
	
	public ColouredButton[][] getCells()
	{
		return this.cells;
	}
}
