package soen487.wscat.logger;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *
 * @author connorbode
 */
public class Logger {
    
    /**
     * Calls the Logger service
     * @param e The exception to log
     * @param severity The severity of the exception
     * @param extra Additional data
     */
    public static void log (Exception e, String severity, String extra) {
        
        // get the source
        String source = "..";
        
        // get the stack trace
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String stackTrace = sw.toString();
        
        // call the logging service
        logger.Logger_Service service = new logger.Logger_Service();
        logger.Logger logger = service.getLoggerPort();
        logger.log(source, e.toString(), stackTrace, severity, extra);
    }
    
    /**
     * Calls the Logger service
     * @param e The exception to log
     * @param severity The severity of the exception
     */
    public static void log (Exception e, String severity) {
        log(e, severity, "");
    }
}
