package soen487.wscat.marfcat.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintStream;

/**
 * ProcessMonitor
 * Used to monitor an IO stream from a running process.
 * 
 * @author c_bode
 */
public class StreamMonitor implements Runnable {
    
    final BufferedReader in;
    final PrintStream out;
    boolean save = false;
    StringBuilder output = new StringBuilder();
    
    /**
     * Initializes a new monitor for the input stream
     * @param stream The input stream
     * @param err If true, direct the input to System.err; else to System.out
     */
    public StreamMonitor (InputStream stream, boolean err) {
        in = new BufferedReader(
                new InputStreamReader(stream));
        
        if (err) {
            out = System.err;
        } else {
            out = System.out;
        }
    }
    
    /**
     * Default constructor for the stream monitor.  Directs input to System.out
     * @param stream The input stream
     */
    public StreamMonitor (InputStream stream) {
        this(stream, false);
    }
    
    /**
     * Sets whether the StreamMonitor should save output to a String.  The 
     * output can be retrieved later using getOutput
     * @param save If true, the StreamMonitor will save all output
     */
    public void setSave (boolean save) {
        this.save = save;
    }
    
    /**
     * Retrieves the output in the StringBuilder
     * @return The output from the stringbuilder
     */
    public String getOutput () {
        return output.toString();
    }
    
    /**
     * Runs the stream monitor
     */
    public void run () {
        try {
            int ch;
            char c;
            while ((ch = in.read()) != -1) {
                c = (char) ch; 
                if (save) {
                    output.append(c);
                } else {
                    out.print(c);
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}