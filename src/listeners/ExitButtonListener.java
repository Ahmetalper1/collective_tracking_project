package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * This class is the ActionListener for the 'Exit' button on the main menu
 * window. This button simply closes the application.
 */
public class ExitButtonListener implements ActionListener
{
	@Override
	public void actionPerformed(ActionEvent event)
	{
		System.exit(1);
	}
}
