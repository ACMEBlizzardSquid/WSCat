package soen487.wscat.logger;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *
 * @author connorbode
 */
public class Logger {
    public static void log (Exception e, String severity) {
        
        // get the source
        String source = "..";
        
        // get the stack trace
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String message = sw.toString();
        
        // call the logging service
        logger.Logger_Service service = new logger.Logger_Service();
        logger.Logger logger = service.getLoggerPort();
        logger.log(source, message, severity);
    }
}
