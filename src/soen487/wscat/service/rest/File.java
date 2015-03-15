package soen487.wscat.service.rest;

import javax.ws.rs.PathParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

@Path("files/{id}")
public class File {

	// Question c
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public String getFileEntry(@PathParam("id") int id){
		return Integer.toString(id);
	}

	// TODO: Not clear if it consumes XML or produces XML (Question d)
	// By default RestEasy (ff plugins) sends POST as x-www-form-urlencoded so I get 415
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	public String postFileEntry(@FormParam("uri") String uri){
		return uri;
	}

	// Question e
	@PUT
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String putFileEntry(@PathParam("id") int id, @FormParam("file") String fileEntry){
		return Integer.toString(id) + " " + fileEntry;
	}

} 
