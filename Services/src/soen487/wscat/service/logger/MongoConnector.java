package soen487.wscat.service.logger;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import java.util.HashMap;
import java.net.UnknownHostException;

/**
 * Manages connections and performs queries to mongo instance
 * @author connorbode
 */
public class MongoConnector {
    
    private MongoClient client;
    private DB database;
    private DBCollection collection;
    
    /**
     * Initializes the connection to the database
     */
    public MongoConnector () throws UnknownHostException {
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
        collection.insert(insert);
    }
    
    /**
     * Closes the connection to the database
     */
    public void close () {
        client.close();
    }
}
