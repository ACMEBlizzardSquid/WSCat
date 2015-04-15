package soen487.wscat.service.logger;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.util.HashMap;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Date;

/**
 * Manages connections and performs queries to mongo instance
 * @author connorbode
 */
public class DBConnector {
    
    private MongoClient client;
    private DB database;
    private DBCollection collection;
    
    /**
     * Initializes the connection to the database
     */
    public DBConnector () throws UnknownHostException {
        String databaseName = "wscat";
        String collectionName = "logs";
        String connectionString = "mongodb://beeeepel:waaallee@ds061661.mongolab.com:61661/wscat"; // i know this shouldn't be stored here but w/e
        MongoClientURI uri = new MongoClientURI(connectionString);
        client = new MongoClient(uri);
        database = client.getDB(databaseName);
        collection = database.getCollection(collectionName);
    }
    
    /**
     * Inserts a document into the database
     * @param obj Key-value pairs that should be inserted
     */
    public void insert (HashMap<String, String> obj) {
        BasicDBObject insert = new BasicDBObject();
        for (String key : obj.keySet()) {
            insert.put(key, obj.get(key));
        }
        insert.put("created_at", new Date().toString());
        collection.insert(insert);
    }
    
    /**
     * Retrieves all logs
     * @return The results
     */
    public LinkedList<HashMap<String,String>> find () {
        return find(new HashMap<String,String>());
    }
    
    /**
     * Performs a search for documents in the collection
     * @param query The query to send
     * @return The results
     */
    public LinkedList<HashMap<String,String>> find (HashMap<String,String> query) {
        
        // build mongo query
        BasicDBObject mongoQuery = new BasicDBObject();
        for (String key : query.keySet()) {
            mongoQuery.put(key, query.get(key));
        }
        
        // perform find query
        DBCursor cursor = collection.find(mongoQuery);
        
        // iterate through results
        LinkedList<HashMap<String, String>> result = new LinkedList<HashMap<String, String>>();
        try {
            while (cursor.hasNext()) {
                DBObject next = cursor.next();
                HashMap item = new HashMap(next.toMap());
                result.push(item);
            }
        } finally {
            cursor.close();
        }
        return result;
    }
    
    /**
     * Closes the connection to the database
     */
    public void close () {
        client.close();
    }
}
