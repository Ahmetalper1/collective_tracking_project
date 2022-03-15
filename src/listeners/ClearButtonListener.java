package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import gui.ColouredButton;
import gui.InitialStateGUI;

/*
 * This class is the ActionListener for the 'Clear' button on the Initial
 * State Selection window. This button clears out all the cells and sets
 * the initial state back to 0 cells alive.
 */
public class ClearButtonListener implements ActionListener
{
	// This is the GUI holding the cells and the 'clear'/'save'/ buttons.
	private InitialStateGUI gui;
	
	// Constructor of the class.
	public ClearButtonListener(InitialStateGUI gui)
	{
		this.gui = gui;
	}
	
	/*
	 * This is called when the user has clicked on the 'Clear' button. When it
	 * happens, we just set all the cells to NOT selected and update their
	 * color.
	 */
	@Override
	public void actionPerformed(ActionEvent event)
	{
		for (int i = 0; i < InitialStateGUI.ROWS; i++)
		{
			for (int j = 0; j < InitialStateGUI.COLUMNS; j++)
			{
				gui.getCells()[i][j].setSelected(false);
				gui.getCells()[i][j].setColour(ColouredButton.DEAD_COLOUR);
			}
		}
		
	}
}
