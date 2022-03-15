package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;

import abstraction.GranularitySelection;
import listeners.ExitButtonListener;
import listeners.InitialStateListener;
import listeners.StartButtonListener;
import patterns.Pattern;

/*
 * This class is representing the GUI for the user. This class contains the
 * start button, quit button as well as the rules to be applied on the Level 0
 * automaton.
 */
public class Menu extends JFrame
{
	// Default size of window.
	public static int DEFAULT_WIDTH_GUI = 1300;
	public static int DEFAULT_HEIGHT_GUI = 300;
	
	// Simple button quitting the whole application.
	private JButton quitButton;
	
	// Simple button starting the simulator.
	private JButton startButton;
	
	// Pattern menu for initial state.
	private JComboBox<Pattern> startPatterns;
	private JLabel startPatternsLabel;
	
	/*
	 *  Button made to open the initial state grid for the user to select which
	 *  cells are on at the iteration zero. The InitialStateGUI is the GUI
	 *  for the selection of alive/dead cells.
	 */
	private JButton initialStateButton;
	private InitialStateGUI initialStateGUI;
	
	/*
	 * The button used to take the user input concerning the log filtering.
	 * The associated label it here as well.
	 */
	private JRadioButtonMenuItem logButton;
	private JLabel logLabel;
	
	// Label to indicate rule selection.
	private JLabel ruleLabel;
	
	// Labels for Alive and Dead rules.
	private JLabel aliveLabel;
	private JLabel deadLabel;
	
	// Labels to indicate which button is for which case for the rules.
	private JLabel[] neighbourCounts;
	
	// Buttons for alive rules.
	private JRadioButtonMenuItem[] aliveRules;

	// Buttons for dead rules.
	private JRadioButtonMenuItem[] deadRules;
	
	/*
	 * This text field is the one that will hold the number of iterations before
	 * displaying the graph. The JLabel is the label for the text field.
	 */
	private JFormattedTextField limitStep;
	private JLabel limitStepLabel;
	
	// List to select the granularity the middle automata will adopt.
	private JComboBox<GranularitySelection.Granularity> middleGranularity;
	private JLabel granularityLabel;
	
	// Elements used to get the thresholds wanted by the user.
	private JTextField middleThresholds;
	private JLabel middleThresholdsLabel;
	private JTextField topThresholds;
	private JLabel topThresholdsLabel;
	
	// This panel is holding all of the elements displayed.
	private JPanel panel;
	
	public Menu()
	{
		// Creating the layout of the panel.
		GridBagConstraints c = new GridBagConstraints();
		GridBagLayout layout = new GridBagLayout();
		
		// Creating the panel here.
		this.panel = new JPanel(layout);
		
		// Adding all the elements function by function to the layout.
		AddQuitStartButtons(c, layout);
		AddRules(c, layout);
		AddAliveRules(c, layout);
		AddDeadRules(c, layout);
		AddLogButton(c, layout);
		AddStepSelection(c, layout);
		AddInitialPatterns(c, layout);
		setPatternAL();
		AddGranularitySelection(c, layout);
		AddMiddleThresholds(c, layout);
		AddTopThresholds(c, layout);
		
		// Creating the InitialStateGUI and adding the right button.
		initialStateGUI = new InitialStateGUI();
		AddInitialStateSelection(c, layout);
		
		// Adding the panel to the JFrame (here the Menu object).
		this.add(panel);
		
		// This is the title of the window displayed.
		this.setTitle("Automata Simulator");
		
		// Setting the size of the window according to statics (top of file).
		this.setSize(DEFAULT_WIDTH_GUI, DEFAULT_HEIGHT_GUI);
	}
	
	/*
	 * This function adds the quit and start button to the layout.
	 */
	private void AddQuitStartButtons(GridBagConstraints c, GridBagLayout layout)
	{
		// Setting up coordinates for exit button.
		c.gridx = 10;
		c.gridy = 6;
		
		// Adding the exit button to layout.
		quitButton = new JButton("Exit");
		quitButton.addActionListener(new ExitButtonListener());
		layout.addLayoutComponent(quitButton, c);
		
		// No need to re-set c.gridy as it is already at 4.
		c.gridx = 0;
		
		// Adding the start button to layout.
		startButton = new JButton("Start");
		startButton.addActionListener(new StartButtonListener(this));
		layout.addLayoutComponent(startButton, c);
		
		panel.add(quitButton);
		panel.add(startButton);
	}
	
	/*
	 * This function adds the Labels and Buttons for the Rule selection to
	 * the layout.
	 */
	private void AddRules(GridBagConstraints c, GridBagLayout layout)
	{
		// Adding the rule label to the layout.
		c.gridx = 0;
		c.gridy = 2;
		ruleLabel = new JLabel("Rules :");
		layout.addLayoutComponent(ruleLabel, c);
		panel.add(ruleLabel);
		
		// Counter for the label text (representing the number of neighbors).
		Integer counter = 0;
		
		// Instantiating the labels.
		neighbourCounts = new JLabel[9];
		
		// For all the labels, we will create them and add them to the layout.
		for (JLabel label : neighbourCounts)
		{
			// Shifting the position for each label. (No need to change gridy.)
			c.gridx++;
			
			// Getting the string object of the counter.
			label = new JLabel(counter.toString());
			
			// Adding the label to the layout.
			layout.addLayoutComponent(label, c);
			
			// Adding the label to the panel.
			panel.add(label);
			
			counter++;
		}
	}
	
	/*
	 * This function adds the label for alive rules as well as the
	 * JRadioButtonMenuItems buttons to the panel.
	 */
	private void AddAliveRules(GridBagConstraints c, GridBagLayout layout)
	{
		// Setting up coordinates for Alive Rules line.
		c.gridx = 0;
		c.gridy = 3;
		
		// Adding Alive label to the GUI.
		aliveLabel = new JLabel("Alive :");
		layout.addLayoutComponent(aliveLabel, c);
		panel.add(aliveLabel);
		
		// Instantiating the alive buttons.
		aliveRules = new JRadioButtonMenuItem[9];
		
		for (int i = 0; i < 9; i++)
		{
			c.gridx++;
			
			// Adding the button to the GUI.
			aliveRules[i] = new JRadioButtonMenuItem();
			layout.addLayoutComponent(aliveRules[i], c);
			panel.add(aliveRules[i]);
		}
		
		// Game of Life default rules.
		aliveRules[2].setSelected(true);
		aliveRules[3].setSelected(true);
	}
	
	/*
	 * This function adds the label for dead rules as well as the
	 * JRadioButtonMenuItems buttons to the panel.
	 */
	private void AddDeadRules(GridBagConstraints c, GridBagLayout layout)
	{
		// Setting up coordinates for Dead Rules line.
		c.gridx = 0;
		c.gridy = 4;
		
		// Adding the Dead label to the GUI.
		deadLabel = new JLabel("Dead :");
		layout.addLayoutComponent(deadLabel, c);
		panel.add(deadLabel);
		
		// Instantiating the dead buttons.
		deadRules = new JRadioButtonMenuItem[9];
		
		for (int i = 0; i < 9; i++)
		{
			c.gridx++;
			
			// Add the button to the GUI.
			deadRules[i] = new JRadioButtonMenuItem();
			layout.addLayoutComponent(deadRules[i], c);
			panel.add(deadRules[i]);
		}
		
		deadRules[3].setSelected(true);
	}
	
	/*
	 * This adds the button for the Initial State Selection window to pop up.
	 */
	private void AddInitialStateSelection(GridBagConstraints c,
			GridBagLayout layout)
	{
		c.gridx = 10;
		c.gridy = 3;
		
		/*
		 *  Adding the button to the panel and adding an action listener to the
		 *  button in order to open the right window when clicked.
		 */
		initialStateButton = new JButton("Initial State Selection");
		initialStateButton.addActionListener(new 
				InitialStateListener(initialStateGUI));
		layout.addLayoutComponent(initialStateButton, c);
		panel.add(initialStateButton);
	}
	
	/*
	 * This function adds the detailed log button that enables or not the 
	 * logs (FINEST > or WARNING >).
	 */
	private void AddLogButton(GridBagConstraints c, GridBagLayout layout)
	{
		// Adding the space above the LogButton.
		c.gridx = 0;
		c.gridy = 7;
		
		JLabel spaceLabelTop = new JLabel(" ");
		layout.addLayoutComponent(spaceLabelTop, c);
		panel.add(spaceLabelTop);
		
		
		// Adding the Log Button Label.
		c.gridy++;
		
		logLabel = new JLabel("Detailed log ?");
		layout.addLayoutComponent(logLabel, c);
		panel.add(logLabel);
		
		// Adding the Log Button itself.
		c.gridx++;
		
		logButton = new JRadioButtonMenuItem();
		layout.addLayoutComponent(logButton, c);
		panel.add(logButton);
	}
	
	/*
	 * This function adds the JComboBox selection for initial patterns.
	 */
	private void AddInitialPatterns(GridBagConstraints c, GridBagLayout layout)
	{
		// Adding the Initial pattern JLabel.
		c.gridx = 13;
		c.gridy++;
		
		startPatternsLabel = new JLabel("Select initial pattern");
		layout.addLayoutComponent(startPatternsLabel, c);
		panel.add(startPatternsLabel);
		
		// Adding the JComboBox selection itself.
		c.gridy++;
		
		startPatterns = new JComboBox<Pattern>(Pattern.values());
		//ada added:
		startPatterns.setSelectedIndex(1);;//Glider pattern
		//end ada 
		layout.addLayoutComponent(startPatterns, c);
		panel.add(startPatterns);
		
		// Adding the space under the box.
		c.gridy++;
		
		JLabel spaceLabel = new JLabel("       ");
		layout.addLayoutComponent(spaceLabel, c);
		panel.add(spaceLabel);
	}
	
	private void setPatternAL()
	{
		startPatterns.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if ((Pattern) startPatterns.getSelectedItem()
					!= Pattern.PERSONAL)
				{
					initialStateButton.setEnabled(false);
				}
				else
				{
					initialStateButton.setEnabled(true);
				}
			};
		});
	}
	
	/*
	 * This function adds the text box where you can indicate the maximum
	 * number of iterations of the simulator.
	 */
	private void AddStepSelection(GridBagConstraints c, GridBagLayout layout)
	{
		// Adding the JLabel of the iteration number selection text box.
		c.gridx = 13;
		c.gridy = 2;
		
		// Creating the Label.
		limitStepLabel = new JLabel("Iteration Number");
		layout.addLayoutComponent(limitStepLabel, c);
		panel.add(limitStepLabel);
		
		// Adding the Text Box itself.
		c.gridy++;
		
		// This NumberFormat is used for the Text Field. Setting up to number.
		NumberFormat amountFormat;
		amountFormat = NumberFormat.getNumberInstance();
		amountFormat.setGroupingUsed(false);

		// Creating the Text Field.
		limitStep = new JFormattedTextField(amountFormat);
		limitStep.setColumns(3);
		limitStep.setValue(100);
		layout.addLayoutComponent(limitStep, c);
		panel.add(limitStep, c);
		
		// Adding the space under the text box.
		c.gridy++;
		
		JLabel spaceLabel = new JLabel("	");
		layout.addLayoutComponent(spaceLabel, c);
		panel.add(spaceLabel);
	}
	
	/*
	 * This method adds the JLabel and CheckBoxes for the middle abstraction
	 * selection on the menu.
	 */
	private void AddGranularitySelection(GridBagConstraints c,
			GridBagLayout layout)
	{
		c.gridx = 13;
		c.gridy = 2;
		
		JLabel spaceLabel = new JLabel("	");
		layout.addLayoutComponent(spaceLabel, c);
		panel.add(spaceLabel);
		
		c.gridx++;
		c.gridy++;
		
		granularityLabel = new JLabel("Select Level 1 Granularity");
		layout.addLayoutComponent(granularityLabel, c);
		panel.add(granularityLabel);
		
		c.gridy++;
		
		middleGranularity = new JComboBox<GranularitySelection.Granularity>
			(GranularitySelection.Granularity.values());
		layout.addLayoutComponent(middleGranularity, c);
		panel.add(middleGranularity);
		
		c.gridy++;
	}
	
	private void AddMiddleThresholds(GridBagConstraints c,
			GridBagLayout layout)
	{
		c.gridx = 15;
		c.gridy = 2;
		
		middleThresholdsLabel = new JLabel("Select your middle thresholds.");
		layout.addLayoutComponent(middleThresholdsLabel, c);
		panel.add(middleThresholdsLabel);
		
		c.gridy++;
		
		middleThresholds = new JFormattedTextField();
		middleThresholds.setColumns(3);
		middleThresholds.setText("1,2");//Ada changed; orig: 1
		layout.addLayoutComponent(middleThresholds, c);
		panel.add(middleThresholds);
	}
	
	private void AddTopThresholds(GridBagConstraints c,
			GridBagLayout layout)
	{
		c.gridx = 15;
		c.gridy = 5;
		
		topThresholdsLabel = new JLabel("Select your top thresholds.");
		layout.addLayoutComponent(topThresholdsLabel, c);
		panel.add(topThresholdsLabel);
		
		c.gridy++;
		
		topThresholds = new JFormattedTextField();
		topThresholds.setColumns(3);
		topThresholds.setText("");//ada changed; orig: 1
		layout.addLayoutComponent(topThresholds, c);
		panel.add(topThresholds);
	}
	
	public JRadioButtonMenuItem[] getAliveRules()
	{
		return this.aliveRules;
	}
	
	public JRadioButtonMenuItem[] getDeadRules()
	{
		return this.deadRules;
	}
	
	public InitialStateGUI getInitialStateGUI()
	{
		return this.initialStateGUI;
	}
	
	public JRadioButtonMenuItem getLogChoice()
	{
		return this.logButton;
	}
	
	public JComboBox<Pattern> getPattern()
	{
		return this.startPatterns;
	}
	
	public JFormattedTextField getLimitStep()
	{
		return this.limitStep;
	}
	
	public JComboBox<GranularitySelection.Granularity> getmiddleGranularity()
	{
		return this.middleGranularity;
	}
	
	public JTextField getMiddleThresholds()
	{
		return this.middleThresholds;
	}
	
	public JTextField getTopThresholds()
	{
		return this.topThresholds;
	}
}
