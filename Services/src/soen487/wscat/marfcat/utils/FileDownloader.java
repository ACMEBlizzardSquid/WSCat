package soen487.wscat.marfcat.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.UUID;

/**
 * Used to save a file received via SOAP to disk
 * @author connorbode
 */
public class FileDownloader {
    
	// UNIX specific
	public static final String DIRECTORY = "/tmp/";
	
    /**
     * Persists a file to disk in a random, unique and temporary location
     * @param receivedFile The file to persist
     * @return The location of the temporary file
     * @throws IOException 
     */
    public static String download (String receivedFileContent, String uuid) 
            throws IOException {
        String path = DIRECTORY + uuid;
        new File(DIRECTORY).mkdirs();
        FileOutputStream fos = new FileOutputStream(path);
        PrintStream out = new PrintStream(fos);
        out.print(receivedFileContent);
        out.close();
        File file = new File(path);
        file.setReadable(true);
        file.setWritable(true);
        file.setExecutable(true);
        return path;
    }
    
    /**
     * @see #download(String, String)
     * @param receivedFileContent
     * @return The location of the temporary file
     * @throws IOException
     */
    public static String download (String receivedFileContent)
            throws IOException {
        return download(receivedFileContent, getRandomName());
    }
    
    /**
     * Get generic random name
     * @return A random alphanumeric string
     */
    public static String getRandomName(){
    	return UUID.randomUUID().toString().replace("-", "");
    }
    
    /**
     * Get random name by hashing hashing <code>name</code>.
     * This method is used to forge the same name for a duplicate
     * @return A random alphanumeric string
     */
    public static String getRandomName(String name){
    	return Integer.toString(name.hashCode()).replace("-", "1");
    }
}
