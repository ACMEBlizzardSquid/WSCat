package soen487.wscat.service.rest;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import soen487.wscat.marfcat.Marfcat;

@Path("/categories")
public class Category {

    /**
     * Retrieves a given category by ID 
     * @param id The ID of the category to retrieve
     * @return A JSON representation of the category
     * @throws IOException If communication with the MARFCAT exe fails.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getCategory(@PathParam("id") int id) 
            throws IOException {
        
        // get all marfcat categories
        Marfcat marf = new Marfcat();
        HashMap<String, Integer> categories = marf.getCategories();
        
        // build json response
        Map.Entry<String, Integer> selection = null;
        for (Map.Entry<String, Integer> entry : categories.entrySet()) {
            if (entry.getValue() == id) {
                selection = entry;
            }
        }
        
        String response;
        if (selection == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            JsonBuilderFactory builder = Json.createBuilderFactory(null);
            response = builder.createObjectBuilder()
                    .add("name", selection.getKey())
                    .add("id", selection.getValue())
                    .build()
                    .toString();
        }
        
        return Response.ok(response).build();
    }
    
    /**
     * Retrieves all categories
     * @return A JSON representation of all categories
     * @throws IOException 
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getCategories() 
            throws IOException {
        
        // get marfcat categories
        Marfcat marf = new Marfcat();
        HashMap<String, Integer> categories = marf.getCategories();
        
        // build json response
        JsonBuilderFactory builder = Json.createBuilderFactory(null);
        JsonArrayBuilder responseBuilder = builder.createArrayBuilder();
        for (Map.Entry<String, Integer> entry : categories.entrySet()) {
            JsonObject obj = builder.createObjectBuilder()
                    .add("name", entry.getKey())
                    .add("id", entry.getValue())
                    .build();
            
            responseBuilder.add(obj);
        }
        JsonArray response = responseBuilder.build();
            
        return response.toString();
    }
} 
