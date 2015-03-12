package soen487.wscat.marfcat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import soen487.wscat.marfcat.utils.StreamMonitor;

/**
 * This class is used to generate a MARFCAT_IN file.
 * @author connorbode
 */
public class MarfcatIn {
    
    private String rootPath;                // root application path
    private ArrayList<MarfcatInItem> items = new ArrayList<MarfcatInItem>();
    private int highestId;
    private String path;
    
    /**
     * Initializes the MarfcatIn object
     */
    public MarfcatIn () 
            throws IOException {
        
        this(Marfcat.generatePath());
    }
    
    
    public MarfcatIn (String path)
            throws IOException {
        
        // set paths
        rootPath = new File(".").getCanonicalPath();
        this.path = path;
        
        // get highest Id
        highestId = getHighestId();
    }
    
    /**
     * Adds an item to the items list
     * @param item The item to add
     */
    public void addItem (MarfcatInItem item) {
        item.setId(highestId++);
        items.add(item);
    }
    
    /**
     * Gets the unix 'file' utility version
     * @return The unix 'file' utility version
     */
    public String getFileUtilityVersion ()
            throws IOException, InterruptedException {
        // get file utility version
        Process process = Runtime.getRuntime().exec("file --version");
        StreamMonitor in = new StreamMonitor(process.getInputStream());
        in.setSave(true);
        in.run();
        process.waitFor();
        return in.getOutput();
    }
    
    /**
     * Gets the unix 'find' utility version
     * @return The unix 'find' utility version
     */
    public String getFindUtilityVersion () 
            throws IOException, InterruptedException {
        
        // get find utility version
        Process process = Runtime.getRuntime().exec("find --version ");
        StreamMonitor in = new StreamMonitor(process.getInputStream());
        in.setSave(true);
        in.run();
        process.waitFor();
        return in.getOutput().split("\n")[0]; // Removing Unwanted strings
    }
    
    /**
     * Gets the marf.jar version
     * @return The marf.jar version
     */
    public String getMarfUtilityVersion () 
            throws IOException, InterruptedException {
        
        // get marf utility version 
        String marfcatLibPath = rootPath + Marfcat.MARFCAT_LIB + "/marf.jar";
        String command = "java -jar " + marfcatLibPath + " --diagnostic";
        Process process = Runtime.getRuntime().exec(command);
        StreamMonitor in = new StreamMonitor(process.getInputStream());
        in.setSave(true);
        in.run();
        process.waitFor();
        return in.getOutput();
    }

    
    
    /**
     * Writes the MARFCAT_IN file
     * @throws FileNotFoundException
     * @throws IOException
     * @throws InterruptedException 
     */
    public void write() 
            throws FileNotFoundException, IOException, InterruptedException {
        
        // set up print writer
        FileWriter fw = new FileWriter(path, false);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw);
        
        // get current date
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        String dateString = timestamp.toString();
        
        // print header
        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        out.println("<dataset generated-by=\"WSCAT\" generated-on=\"" + dateString + "\">");
        out.println("  <description>");
        out.println("    <file-type-tool>");
        out.println(getFileUtilityVersion());
        out.println("    </file-type-tool>");
        out.println("    <find-tool>");
        out.println(getFindUtilityVersion());
        out.println("    </find-tool>");
        out.println("    <marf-tool>");
        out.println(getMarfUtilityVersion());
        out.println("    </marf-tool>");
        out.println("  </description>");
        
        // iterate items and print
        for(int i = 0; i < items.size(); i++) {
            MarfcatInItem item = items.get(i);
            out.println(item.toString());
        }
        
        // print closing tag
        out.println("</dataset>");
        out.close();
    }
    
    public int getHighestId () 
            throws FileNotFoundException, IOException {
        int highestId = 1;
        File file = new File(path);
        if (!file.exists()) {
            return highestId;
        }
        FileInputStream stream = new FileInputStream(file);
        char c;
        int i;
        String idMatch = "<file id=\"";
        int idMatchInt = 0;
        String buffer = "";
        boolean readId = false;
        while ((i = stream.read()) != -1) {
          c = (char) i;
          if (readId) {
              if (c != '"') {
                  buffer += c;
              } else {
                  int id = Integer.parseInt(buffer); // TODO: I get an error when appending an existing file
                  if (id > highestId) {
                      highestId = id;
                  }
                  buffer = "";
                  readId = false;
                  idMatchInt = 0;
              }
          } else if (c == idMatch.charAt(idMatchInt)) {
              idMatchInt += 1;
              if (idMatchInt == idMatch.length()) {
                  readId = true;
              }
          } else {
              idMatchInt = 0;
          }
        }
        stream.close();
        
        return highestId;
    }
    
    /**
     * Writes to the MARFCAT_IN file at the provided path.  If the file exists,
     * it is appended to.  Otherwise, it is created.
     * @param path The path to write to
     * @throws FileNotFoundException
     * @throws IOException
     * @throws InterruptedException 
     */
    public void append() 
            throws FileNotFoundException, IOException, InterruptedException {
  
        int i;
        
        File file = new File(path);
        if (!file.exists()) {
            write();
            return;
        }
        
        RandomAccessFile f = new RandomAccessFile(file, "rw");
        long length = f.length() - 1;
        byte b;
        do {                     
          length -= 1;
          f.seek(length);
          b = f.readByte();
        } while(b != 10);
        f.setLength(length+1);
        
        FileWriter fw = new FileWriter(path, true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw);
        int id;
        
        for(i = 0; i < items.size(); i++) {
            id = i + highestId + 1;
            MarfcatInItem item = items.get(i);
            out.println(item.toString());
        }
        out.println("</dataset>");
        out.close();
    }
    
    public String getPath () {
        return path;
    }
}
