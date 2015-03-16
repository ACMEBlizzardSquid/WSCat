package soen487.wscat.service.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("logs/{id}")
public class Log {

	//Question c
	/**
	 * Get the log
	 * @param id
	 * @return
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getFileEntry(@PathParam("id") int id){
		return Integer.toString(id);
	}
} 
