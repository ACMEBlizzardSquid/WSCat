package soen487.wscat.service.logger;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.WebServiceContext;
import javax.servlet.http.HttpServletRequest;
import javax.annotation.Resource;

import java.util.HashMap;

/**
 * Provides error logging functionality as a web service
 * @author connorbode
 */
@WebService(serviceName = "Logger")
public class Logger {
    
    @Resource
    WebServiceContext context;

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
        
        // get the source IP to track the origin
        MessageContext mc = context.getMessageContext();
        HttpServletRequest req = (HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST);
        String IP = req.getRemoteAddr();
        String host = req.getRemoteHost();
        String referer = req.getHeader("Referer");
        
        // build the insert query
        HashMap<String,String> insert = new HashMap<String,String>();
        insert.put("source", source);
        insert.put("message", message);
        insert.put("severity", severity);
        insert.put("referer", referer);
        insert.put("ip", IP);
        insert.put("host", host);
        
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
