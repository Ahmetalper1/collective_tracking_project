package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;

import logger.LoggerInstance;

/*
 * This class contains utils static functions that handle the creation of the
 * file containing the history of blocks center.
 */
public class OutputFiles
{
	/*
	 * This function creates the directory for the history file.
	 */
	public static void CreateBlocksHistoryDirectory()
	{
		Path blockHistoryDir = Paths.get(System.getProperty("user.dir")
				+ "/BlockHistory");
		
		if (Files.exists(blockHistoryDir))
		{
			LoggerInstance.LOGGER.log(Level.WARNING,
					"Block History Directory already exists, not creating it.");
		}
		else
		{
			LoggerInstance.LOGGER.log(Level.WARNING,
					"Block History Directory does not exist, creating it.");
			try
			{
				Files.createDirectories(blockHistoryDir);
			}
			catch (IOException e)
			{
				LoggerInstance.LOGGER.log(Level.SEVERE, "IOException in "
						+ "CreateBlocksHistoryDirectory(): Could not create "
						+ "new directory.");
			}
		}
	}
	
	/*
	 * This creates or empty the file containing the data.
	 */
	public static void CreateBlocksHistoryFiles(String patternSelected)
	{
		File blockHistoryBottomFile = new File(System.getProperty("user.dir")
				+ "/BlockHistory/Block_History_Bottom_" + patternSelected
				+ ".txt");
		
		File blockHistoryMiddleFile = new File(System.getProperty("user.dir")
				+ "/BlockHistory/Block_History_Middle_" + patternSelected
				+ ".txt");
		
		// Creating the file if it does not exist.
		if (!blockHistoryBottomFile.exists())
		{
			LoggerInstance.LOGGER.log(Level.WARNING,
					"Creating Bottom Block History output file.");
			try
			{
				blockHistoryBottomFile.createNewFile();
			}
			catch (IOException e)
			{
				LoggerInstance.LOGGER.log(Level.SEVERE,
					"IOException in CreateBlocksHistoryFiles(): Could not "
					+ "create Bottom Blocks History File.");
			}
		}
		
		// Creating the file if it does not exist.
		if (!blockHistoryMiddleFile.exists())
		{
			LoggerInstance.LOGGER.log(Level.WARNING,
					"Creating Middle Block History output file.");
			try
			{
				blockHistoryMiddleFile.createNewFile();
			}
			catch (IOException e)
			{
				LoggerInstance.LOGGER.log(Level.SEVERE,
					"IOException in CreateBlocksHistoryFiles(): Could not "
					+ "create Middle Blocks History File.");
			}
		}		
	}
	
	/*
	 * This function erases the content of the files and replaces it with a
	 * generic header with the name of the pattern and the date in it.
	 */
	public static void EraseAndFillBlockHistoryFiles(String patternSelected,
			FileWriter bottomFW, FileWriter middleFW)
	{
		// This gets the date and time.
		DateTimeFormatter dtf = DateTimeFormatter
				.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();
		
		// Writing the header in the Bottom Blocks History file.
		try
		{
			bottomFW.write("BOTTOM BLOCKS HISTORY | PATTERN SELECTED : "
					+ patternSelected + " | LAUNCH DATE : " + dtf.format(now));
			
			bottomFW.append("\n\n");
		}
		catch (IOException e)
		{
			LoggerInstance.LOGGER.log(Level.SEVERE, "IOException in " + 
				"EraseAndFillBlockHistoryFiles(): Could not open FileWriter " +
				"on 'Block_History_Bottom_" + patternSelected + ".txt' file.");
		}
		
		// Writing the header in the Middle Blocks History file.
		try
		{
			middleFW.write("MIDDLE BLOCKS HISTORY | PATTERN SELECTED : "
					+ patternSelected + " | LAUNCH DATE : " + dtf.format(now));
			
			middleFW.append("\n\n");
		}
		catch (IOException e)
		{
			LoggerInstance.LOGGER.log(Level.SEVERE, "IOException in " + 
				"EraseAndFillBlockHistoryFiles(): Could not open FileWriter " +
				"on 'Block_History_Middle_" + patternSelected + ".txt' file.");
		}
	}
}
