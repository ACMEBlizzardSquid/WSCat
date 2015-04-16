package logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.QueryParam;

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
@Path("/logs")
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
     * Builds a JSON representation of a linked list
     * @param list The list to encode in json
     * @return The json
     */
    public String buildJson (LinkedList<HashMap<String, String>> list) {
        String json = "[";
        for (HashMap<String,String> item : list) {
            json += buildJson(item) + ",";
        }
        json = json.substring(0, json.length() - 1);
        json += "]";
        if (list.size() == 0) {
            json = "[]";
        }
        return json;
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
                response = Response.status(404).build();
            } else {
                String json = buildJson(results.getFirst());
                response = Response.ok(json).build();
            }
        } catch (Exception e) {
            response = Response.serverError().build();
        } finally {
            db.close();
        }
        
        return response;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public Response getLogs (
            @QueryParam("date") String date
    ) {
        DBConnector db = new DBConnector();
        Response response;
        LinkedList<HashMap<String,String>> results;
        
        // build the query
        HashMap<String,String> query = new HashMap<String,String>();
        if (date != null) {
            query.put("created_at", date);
        }
        
        // execute the query
        try {
            db.connect();
            results = db.find(query);
            String json = buildJson(results);
            response = Response.ok(json).build();
        } catch (Exception e) {
            response = Response.serverError().build();
        } finally {
            db.close();
        }
        
        return response;
    }
}
