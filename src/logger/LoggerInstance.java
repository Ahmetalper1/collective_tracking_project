package logger;

import java.util.logging.ConsoleHandler;
import java.util.logging.*;

/*
 * This class holds the one instance of the Logger used in the whole program.
 */
public class LoggerInstance
{
	// This is the instance of the logger we use to do everything.
	public static final Logger LOGGER =
			Logger.getLogger(Logger.class.getName());
	
	/*
	 * The ConsoleHandler and the LogFormatter are settings to customize the
	 * Logger output and detection (see Logging.Level).
	 */
	public static final ConsoleHandler CH = new ConsoleHandler();
	public static final LogFormatter format = new LogFormatter();
	
	// Initialization of the Logger.
	public static void initLogger()
	{
		LOGGER.setLevel(Level.ALL);
		CH.setFormatter(format);
		LOGGER.addHandler(CH);
		LOGGER.setUseParentHandlers(false);
	}
	
	// This function decides what levels to show.
	public static void setupLogger(boolean log)
	{
		// It's here that we can select.
		if (log)
		{
			// Logging everything.
			CH.setLevel(Level.ALL);
		}
		else
		{
			// Logging only WARNING + SEVERE.
			CH.setLevel(Level.WARNING);
		}
	}
}