package soen487.wscat.marfcat;

import java.io.IOException;
import java.util.UUID;

/**
 * MARFCAT
 * A facade around the MARFCAT library
 * 
 * @author c_bode
 */
public class Marfcat {
    
    public static final String MARFCAT_LIB = "/WEB-INF/lib";
    
    private static String rootPath = System.getProperty("user.dir");        // the root application path
    
    /**
     * Initializes the facade
     * @throws IOException 
     */
    public Marfcat ()
            throws IOException {
    }
    
    /**
     * Trains MARFCAT on a supplied MARFCAT_IN file
     * @param inputFilePath The path to the MARFCAT_IN file
     * @throws IOException
     */
    public void train (String inputFilePath) 
            throws IOException, InterruptedException {
        
        // execute job
        String[] options = {
            "--train",
            "-nopreprep",
            "-raw",
            "-fft",
            "-eucl",
            inputFilePath
        };
        try {
            marf.apps.MARFCAT.MARFCATApp.main(options);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    /**
     * Generates a random temporary file path that is compatible with 
     * marfcat.jar
     * @return The file path
     */
    public static String generatePath () {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return "/tmp/" + uuid;
    }
    
    /**
     * Analyzes the MARFCAT_IN file and returns a path to the generated
     * MARFCAT_OUT file.
     * @param inputFilePath The path to the MARFCAT_IN file
     * @return The path to the MARFCAT_OUT file
     * @throws IOException
     * @throws InterruptedException 
     */
    public String analyze (String inputFilePath)
            throws IOException, InterruptedException {
        
        // generate UUID for file
        String uuid = UUID.randomUUID().toString().replace("-", "");
        
        // execute job
        String[] options = {
            "--batch-ident",
            uuid,
            inputFilePath,
            "-nopreprep",
            "-raw",
            "-fft",
            "-cheb"
        };
        try {
            marf.apps.MARFCAT.MARFCATApp.main(options);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        
        // return path to output file
        String inputBlah = inputFilePath.replace("/", "");
        String filePath = rootPath + "/report-" + inputBlah + "noprepreprawfftcheb-" + uuid + ".xml";
        return filePath;
    }
}
