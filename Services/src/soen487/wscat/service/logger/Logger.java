package soen487.wscat.service.logger;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

import java.util.HashMap;

/**
 * Provides error logging functionality as a web service
 * @author connorbode
 */
@WebService(serviceName = "Logger")
public class Logger {

    /**
     * Logs an error to the database
     * @param source The origin of the error
     * @param message The error message
     * @param severity A number representing the severity of the error.  Higher numbers are more severe.
     * @return True on success
     */
    @WebMethod(operationName = "log")
    public boolean log(
        @WebParam(name = "source") String source, 
        @WebParam(name = "message") String message,
        @WebParam(name = "severity") String severity
    ) {
        
        DBConnector db = new DBConnector();
        boolean result = true;
        
        // build the insert query
        HashMap<String,String> insert = new HashMap<String,String>();
        insert.put("source", source);
        insert.put("message", message);
        insert.put("severity", severity);
        
        // attempt to perform the insert
        try {    
            db.connect();
            db.insert(insert);
        } catch (Exception e) {
            result = false;
        } finally {
            db.close();
        }
        return result;
    }
}
