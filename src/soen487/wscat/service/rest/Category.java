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
import soen487.wscat.marfcat.Marfcat;

@Path("/categories")
public class Category {

    
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
