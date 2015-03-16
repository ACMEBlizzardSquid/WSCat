package soen487.wscat.marfcat;

import java.io.IOException;
import java.lang.InterruptedException;
import soen487.wscat.marfcat.utils.StreamMonitor;

/**
 * This class represents a single item in a MARFCAT_IN file
 * @author connorbode
 */
public class MarfcatInItem {
    
    private String path;
    private String cve;
    private String type;
    private int bytes;
    private int words;
    private int lines;
    private int id;
    
    public void loadFileInfo () 
            throws IOException, InterruptedException {
        
        // get file info
        String command = "file " + path;
        Process process = Runtime.getRuntime().exec(command);
        StreamMonitor inMonitor = new StreamMonitor(process.getInputStream());
        inMonitor.setSave(true);
        inMonitor.run();
        process.waitFor();
        type = inMonitor.getOutput().replaceAll("^.+: ", "").replaceAll("\n", "");
        
        // get word count info
        // TODO: I get an error if the file is empty 0 0 0
        // if it simple you can use the flag -l (lines), -c (bypes), -w (words)
        // This error is generated from ProgrammableWebWSDLRetriever.java
        command = "wc " + path;
        process = Runtime.getRuntime().exec(command);
        inMonitor = new StreamMonitor(process.getInputStream());
        inMonitor.setSave(true);
        inMonitor.run();
        process.waitFor();
        String[] tmp = inMonitor.getOutput().split("[ ]+");
        lines = Integer.parseInt(tmp[1]);
        words = Integer.parseInt(tmp[2]);
        bytes = Integer.parseInt(tmp[3]);
    }
    
    /**
     * Gets the id attribute
     * @return the id attribute of the file
     */
    public int getId() {
        return id;
    }
    
    /**
     * Sets the id attribute of the file
     * @param id the id attribute of the file
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Gets the path to the file for the MarfcatInItem
     * @return The path to the file
     */
    public String getPath () {
        return path;
    }
    
    /**
     * Gets the category of the item
     * @return The category of the item
     */
    public String getCVE () {
        return cve;
    }
    
    /**
     * Sets the path for the MarfcatInItem
     * @param path The path to the MarfcatIn file
     */
    public void setPath (String path) {
        this.path = path;
    }
    
    /**
     * Sets the CVE for the MarfcatInItem
     * @param cve The CVE to set
     */
    public void setCVE (String cve) {
        this.cve = cve;
    }
    
    /**
     * Gets the type of the item
     * @return The type of the item
     */
    public String getType () {
        return type;
    }
    
    /**
     * Sets the type of this item
     * @param type The type of this item
     */
    public void setType (String type) {
        this.type = type;
    }
    
    /**
     * Gets the number of lines in the item
     * @return The number of lines in the item
     */
    public int getLines () {
        return lines;
    }
    
    /**
     * Sets the number of lines in the item
     * @param lines The number of lines in the item
     */
    public void setLines (int lines) {
        this.lines = lines;
    }
    
    /**
     * Gets the number of words in the item
     * @return The number of words in the item
     */
    public int getWords () {
        return words;
    }
    
    /**
     * Sets the number of words in this item
     * @param words The number of words in the item
     */
    public void setWords (int words) {
        this.words = words;
    }
    
    /**
     * Gets the number of bytes in the item
     * @return The number of bytes in the item
     */
    public int getBytes () {
        return bytes;
    }
    
    /**
     * Sets the number of bytes in the item
     * @param bytes The number of bytes in the item
     */
    public void setBytes (int bytes) {
        this.bytes = bytes;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  <file id=\"").append(this.getId()).append("\" path=\"").append(this.getPath()).append("\">");
        sb.append("    <meta>");
        sb.append("      <type>").append(this.getType()).append("</type>");
        sb.append("      <length lines=\"").append(this.getLines())
                .append("\" words=\"").append(this.getWords())
                .append("\" bytes=\"").append(this.getBytes()).append("\" />");
        sb.append("    </meta>");
        sb.append("    <location line=\"\" fraglines=\"\">");
        sb.append("      <meta>");
        sb.append("        <cve>").append(this.getCVE()).append("</cve>");
        sb.append("        <name cweid=\"\"></name>");
        sb.append("      </meta>");
        sb.append("      <fragment>");
        sb.append("      </fragment>");
        sb.append("      <explanation>");
        sb.append("      </explanation>");
        sb.append("    </location>");
        sb.append("  </file>");
        return sb.toString();
    }
}
