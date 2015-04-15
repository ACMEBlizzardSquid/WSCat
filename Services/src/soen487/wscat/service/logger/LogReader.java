package soen487.wscat.service.logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;

import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author connorbode
 */
@Path("/")
public class LogReader {
    
    /**
     * Builds a JSON representation of a hashmap
     * @param map The hashmap to encode in json
     * @return The json
     */
    public String buildJson (HashMap<String, String> map) {
        JsonBuilderFactory builder = Json.createBuilderFactory(null);
        JsonObjectBuilder object = builder.createObjectBuilder();
        for (String key : map.keySet()) {
            object.add(key, map.get(key));
        }
        return object.build().toString();
    }
    
    /**
     * Retrieves a single log by ID
     * @param id The id of the log to retrieve
     * @return The log
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getLog (@PathParam("id") String id) {
        
        DBConnector db = new DBConnector();
        Response response;
        LinkedList<HashMap<String,String>> results;
        
        // build the query
        HashMap<String,String> query = new HashMap<String,String>();
        query.put("_id", id);
        
        // execute the query
        try {
            db.connect();
            results = db.find(query);
            if (results.size() == 0) {
                throw new Exception();
            }
            String json = buildJson(results.getFirst());
            response = Response.ok(json).build();
        } catch (Exception e) {
            response = Response.serverError().build();
        } finally {
            db.close();
        }
        
        return response;
    }
}
