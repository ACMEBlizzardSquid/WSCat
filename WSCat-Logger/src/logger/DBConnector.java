package logger;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import java.util.HashMap;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import org.bson.types.ObjectId;

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
    public void connect () throws UnknownHostException {
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
        insert.put("created_at", new Date());
        collection.insert(insert);
    }
    
    /**
     * Retrieves all logs
     * @return The results
     */
    public LinkedList<HashMap<String,String>> find ()
            throws ParseException {
        return find(new HashMap<String,String>());
    }
    
    /**
     * Performs a search for documents in the collection
     * @param query The query to send
     * @return The results
     */
    public LinkedList<HashMap<String,String>> find (HashMap<String,String> query)
        throws ParseException {
        
        // build mongo query
        BasicDBObject mongoQuery = new BasicDBObject();
        for (String key : query.keySet()) {
            if (key == "_id") {
                mongoQuery.put(key, new ObjectId(query.get(key)));
            } else if (key == "created_at") {
                String dateValue = query.get(key);
                String[] values = dateValue.split("-");
                DateFormat parser = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date startDate = parser.parse(values[0]);
                Date endDate = parser.parse(values[1]);
                mongoQuery.put("created_at", BasicDBObjectBuilder
                    .start("$gte", startDate)
                    .add("$lte", endDate)
                    .get());
            } else {
                mongoQuery.put(key, query.get(key));
            }
        }
        
        // perform find query
        DBCursor cursor;
        if (mongoQuery.size() == 0)
            cursor = collection.find();
        else 
            cursor = collection.find(mongoQuery);
        
        // iterate through results
        LinkedList<HashMap<String, String>> result = new LinkedList<HashMap<String, String>>();
        try {
            while (cursor.hasNext()) {
                DBObject next = cursor.next();
                HashMap<String, String> item = new HashMap<String, String>();
                for (String key : next.keySet()) {
                    item.put(key, next.get(key).toString());
                }
                result.push(item);
            }
        } catch (Exception e) {
            System.out.println(e);
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
