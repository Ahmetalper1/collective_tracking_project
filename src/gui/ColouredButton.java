package gui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/*
 * This class is meant to design the colored button you can see when selecting
 * the initial state for the Level 0 automaton. 
 */
public class ColouredButton extends JButton
{
	// Colors used to display the buttons.
	public static final Color LIVE_COLOUR = Color.MAGENTA; // Default alive.
	public static final Color DEAD_COLOUR = Color.GRAY; // Default dead.
	public static final Color BORDER_COLOUR = Color.DARK_GRAY; // Border.
	public static final Color BUTTON_MARGIN_COLOUR = Color.DARK_GRAY; // Margin.

	// This represents the current color (state) of the cell.
	private Color colour = DEAD_COLOUR;

	// Default constructor.
	public ColouredButton()
	{
		super.setContentAreaFilled(false);
	}

	// Constructor that sets up the button with a color.
	public ColouredButton(Color colour)
	{
		super.setContentAreaFilled(false);
		this.colour = colour;

		Border buttonBorder = new LineBorder(BUTTON_MARGIN_COLOUR);
		this.setBorder(buttonBorder);
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		g.setColor(this.colour);
		g.fillRect(0, 0, getWidth(), getHeight());
		super.paintComponent(g);
	}

	public Color getColour()
	{
		return colour;
	}

	public void setColour(Color colour)
	{
		this.colour = colour;
		this.repaint();
	}
}