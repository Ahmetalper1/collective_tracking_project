package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import gui.InitialStateGUI;

/*
 * This class is the ActionListener for the 'Save' button on the Initial
 * State Selection window. This button saves the initial state selected by
 * the user and closes the window.
 */
public class SaveButtonListener implements ActionListener
{
	// This is the GUI holding the cells and the 'clear'/'save'/ buttons.
	private InitialStateGUI gui;
	
	// Constructor of the class.
	public SaveButtonListener(InitialStateGUI gui)
	{
		this.gui = gui;
	}
	
	/*
	 * This is called when the user has clicked on the 'Save' button. When it
	 * happens, we save the state of the grid and close the window.
	 */
	@Override
	public void actionPerformed(ActionEvent event)
	{	
		gui.setVisible(false);
	}
}
