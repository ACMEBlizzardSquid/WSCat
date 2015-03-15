package soen487.wscat.service.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("categories/{id}")
public class Category {

	// Question c
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getCategory(@PathParam("id") int id){
		return Integer.toString(id);
	}
} 
