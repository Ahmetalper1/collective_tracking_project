package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import gui.InitialStateGUI;

/*
 * This class is the listener for the InitialStateSelection Button on the menu
 * GUI. This will open up another window where the user will be able to
 * setup the alive cells at iteration 0.
 */
public class InitialStateListener implements ActionListener
{
	private InitialStateGUI gui;
	
	// Constructor of InitialStateListener.
	public InitialStateListener(InitialStateGUI gui)
	{
		this.gui = gui;
	}
	
	/*
	 *  This function is called when the InitialStateSelection JButton is
	 *  clicked by the user. We just set the GUI visible.
	 */
	@Override
	public void actionPerformed(ActionEvent event)
	{
		if (!gui.isVisible())
		{
			gui.setVisible(true);
		}
	}
}
