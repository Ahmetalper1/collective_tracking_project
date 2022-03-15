package logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/*
 * This LogFormatter class is a Formatter for the Logger. It allows us to
 * customize the Logger output of the program.
 */
public class LogFormatter extends Formatter
{
    /*
     * We cannot change colors inside of Eclipse, we need a plugin. This
     * function is the one that is called when the Logger logs something in
     * the console.
     */
	@Override
	public String format(LogRecord rec)
	{
		StringBuilder builder = new StringBuilder();

        builder.append("[");
        builder.append(date(rec.getMillis()));
        builder.append("]");
        
        builder.append(" [");
        builder.append(rec.getLevel().getName());
        builder.append("]");

        builder.append(" [");
        builder.append(rec.getSourceClassName() + ": ");
        builder.append(rec.getSourceMethodName());
        builder.append("]");
        
        builder.append(" - ");
        builder.append(rec.getMessage());
        builder.append("\n");
        
        return builder.toString();
	}
	
	// Simple function to get the date.
	private String date(long millisecs) {
        SimpleDateFormat date_format =
        		new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date resultdate = new Date(millisecs);
        return date_format.format(resultdate);
    }
}
