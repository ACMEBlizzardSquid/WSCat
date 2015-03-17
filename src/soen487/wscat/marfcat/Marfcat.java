package soen487.wscat.marfcat;

import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import marf.apps.MARFCAT.MARFCATApp;

/**
 * MARFCAT
 * A facade around the MARFCAT library
 * 
 * @author c_bode
 */
public class Marfcat {
    
    public static final String MARFCAT_LIB = "/WEB-INF/lib";    
    private static String rootPath = System.getProperty("user.dir");        // the root application path
    private static String CONSOLE_LOG_PATH = "log-console.txt";
    private static String ERROR_LOG_PATH = "log-error.txt";
    private MARFCATApp oApp = new MARFCATApp();
    private PrintStream console = System.out;
    private PrintStream err     = System.err;
    private File consoleFile;
    private File errorFile;
    private FileOutputStream consoleOut;
    private FileOutputStream errorOut;
    private PrintStream consolePrinter;
    private PrintStream errorPrinter;
    
    /**
     * Initializes the facade
     * @throws IOException 
     */
    public Marfcat ()
            throws IOException {
    }
    
    /**
     * Redirect STDOUT and STDERR to a randomly generated log files
     * @throws FileNotFoundException 
     */
    private void redirectOutput () 
            throws FileNotFoundException {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        System.out.println(uuid);
        redirectOutput(uuid);
    }
    
    /**
     * Redirects STDOUT and STDERR to log files
     * @param file The prefix of the redirect
     * @throws FileNotFoundException 
     */
    private void redirectOutput (String file) 
            throws FileNotFoundException {
        String prefix = "log-";
        consoleFile = new File(prefix + file + "-console.txt");
        errorFile = new File(prefix + file + "-error.txt");
        consoleOut = new FileOutputStream(consoleFile, true);
        errorOut = new FileOutputStream(errorFile, true);
        consolePrinter = new PrintStream(consoleOut);
        errorPrinter = new PrintStream(errorOut);
        System.setOut(consolePrinter);
        System.setErr(errorPrinter);
    }
    
    /**
     * Resets STDOUT and STDERR
     * @throws IOException 
     */
    private void resetOutput () 
            throws IOException {
        consolePrinter.close();
        errorPrinter.close();
        consoleOut.close();
        errorOut.close();
        System.setOut(console);
        System.setErr(err);
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
        redirectOutput();
        try {
            marf.apps.MARFCAT.MARFCATApp.main(options);
        } catch (Exception e) {
            System.out.println(e);
        }
        resetOutput();
        
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
        
        String inputBlah = inputFilePath.replace("/", "");
        
        redirectOutput(inputBlah);
        try {
            marf.apps.MARFCAT.MARFCATApp.main(options);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        resetOutput();
        
        // return path to output file
        String filePath = rootPath + "/report-" + inputBlah + "noprepreprawfftcheb-" + uuid + ".xml";
        return filePath;
    }
    
    /**
     * Retrieves the log for a given ID
     * @param id The ID of the log to retrieve
     * @return The log contents
     */
    public String getLog (String id) {
        String path = "log-" + id + "-console.txt";
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            return new String(encoded);
        } catch (IOException e) {
            return null;
        }
    }
    
    /**
     * Retrieves the error log for the given
     * @param id
     * @return
     * @throws IOException 
     */
    public String getErrorLog (String id) {
        String path = "log-" + id + "-error.txt";
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            return new String(encoded);
        } catch (IOException e) {
            return null;
        }
    }
    
    public String getAllLogs () {
        Pattern logPattern = Pattern.compile("log-([A-Za-z0-9]*)-console.txt");
        Pattern errorPattern = Pattern.compile("log-([A-Za-z0-9]*)-error.txt");
        File folder = new File(".");
        File[] listOfFiles = folder.listFiles();
        String output = "LOGS \n";

        for (int i = 0; i < listOfFiles.length; i++) {
          if (listOfFiles[i].isFile()) {
            Matcher logMatcher = logPattern.matcher(listOfFiles[i].getName());
            Matcher errorMatcher = errorPattern.matcher(listOfFiles[i].getName());
            if (logMatcher.find()) {
                output += "\n" + listOfFiles[i].getName();
                output += "\n\n" + getLog(logMatcher.group(1));
            }
            if (errorMatcher.find()) {
                output += "\n" + listOfFiles[i].getName();
                output += "\n\n" + getErrorLog(errorMatcher.group(1));
            }
          }
        }
        
        return output;
    }
    
    
    /**
     * Returns a list of available categories for MARFCAT.  
     * @return The list of available categories for MARFCAT as a HASHMAP where
     *          the key is a String (the category) and the value is the Integer
     *          ID of the category.
     */
    public HashMap<String, Integer> getCategories () {
       return marf.apps.MARFCAT.SATE.SATEDb.MAP_CVES_TO_IDS;
    }
}
