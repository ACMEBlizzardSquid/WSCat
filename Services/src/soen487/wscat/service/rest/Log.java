package soen487.wscat.service.rest;

import java.io.IOException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import soen487.wscat.marfcat.Marfcat;

@Path("/logs")
public class Log {

        @GET
        @Produces(MediaType.TEXT_PLAIN)
        public String getLogs() 
                throws IOException {
            Marfcat marfcat = new Marfcat();
            return marfcat.getAllLogs();
        }
	
	@GET
        @Path("/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getFileEntry(@PathParam("id") String id) 
                throws IOException {
            Marfcat marfcat = new Marfcat();
            String response = "*************************************** \n";
            response += " LOG \n";
            response += "***************************************\n\n";
            response += marfcat.getLog(id);
            response += "\n\n*************************************** \n";
            response += " ERROR \n";
            response += "*************************************** \n\n";
            response += marfcat.getErrorLog(id);
            return response;
	}
        
}