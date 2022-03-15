package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import gui.ColouredButton;

/*
 * This class is the ActionListener for every cell on the Initial
 * State Selection window. This manages the colour and the state of each
 * cell when a user clicks on it.
 */
public class ColouredButtonListener implements ActionListener
{
	// The button we're looking at.
	ColouredButton button;
	
	// Constructor of the class.
	public ColouredButtonListener(ColouredButton button)
	{
		this.button = button;
	}
	
	/*
	 * This is called when the user has clicked on the cell. When it happens, 
	 * we just invert the actual state of the cell. If it was alive, it becomes
	 * dead, and if it was dead, we make it alive (color + state).
	 */
	@Override
	public void actionPerformed(ActionEvent event)
	{
		if (button.isSelected())
		{
			button.setSelected(false);
			button.setColour(ColouredButton.DEAD_COLOUR);
		}
		else
		{
			button.setSelected(true);
			button.setColour(ColouredButton.LIVE_COLOUR);
		}
	}

}
