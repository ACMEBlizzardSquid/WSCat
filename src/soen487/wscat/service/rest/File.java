package soen487.wscat.service.rest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import soen487.wscat.marfcat.Marfcat;
import soen487.wscat.marfcat.MarfcatIn;
import soen487.wscat.marfcat.MarfcatInItem;
import soen487.wscat.marfcat.utils.FileDownloader;
import soen487.wscat.parser.WSDLParser;
import soen487.wscat.service.WSCat;
import soen487.xml.XMLReader;

@Path("/files")
public class File {
    
        private String MARFCAT_IN_PATH = "MARFCAT_IN.xml";

	/**
	 * Get the file entry in the repository at <code>id</code>
	 * @param id
	 * @return the file entry element
	 * @throws IOException
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
        @Path("/{id}")
	public String getFileEntry(@PathParam("id") int id) throws IOException{
            MarfcatIn marfcatIn = new MarfcatIn(MARFCAT_IN_PATH);
            MarfcatInItem item = marfcatIn.getItemById(id);
            return item.toString();
	}
    
	/**
	 * Get all file entries
	 * @param id
	 * @return
	 * @throws IOException
	 */
        @GET
	@Produces(MediaType.APPLICATION_XML)
	public String getFileEntries() throws IOException {
            MarfcatIn marfcatIn = new MarfcatIn(MARFCAT_IN_PATH);
            ArrayList<MarfcatInItem> items = marfcatIn.getAllItems();
            String outputString = "<files>";
            for (MarfcatInItem item : items) {
                outputString += item.toString();
            }
            outputString += "</files>";
            return outputString;
	}

        /**
         * Insert a new file entry element in the repository
         * @param uri
         * @return
         */
	@POST
	@Consumes(MediaType.TEXT_XML)
	public String postFileEntry(String file){
            
            StringBuilder sb = new StringBuilder();
            try {
                Marfcat marf = new Marfcat();
                MarfcatIn marfIn = new MarfcatIn(MARFCAT_IN_PATH);
                
                //get MarfcatInItem from post
                MarfcatInItem marfcatInItem = new MarfcatInItem(file);  
                
                //save original wsdl uri to add in <description>
                marfcatInItem.setOriginalWsdlUri(marfcatInItem.getPath());
                
                //download wsdl
                String wsdlFile = XMLReader.readAsString(marfcatInItem.getPath());
                String localPath = FileDownloader.download(wsdlFile);

                //change <file> path to the just downloaded local wsdl
                marfcatInItem.setPath(localPath);

                //add to marfIn and analyze
                marfIn.addItem(marfcatInItem);
                String marfInPath = marfIn.getPath();
                marfIn.write();
                String MARFCAT_OUT = marf.analyze(marfInPath); 
                
                
                //Get results
                BufferedReader br = new BufferedReader(new FileReader(MARFCAT_OUT));
                String line = br.readLine();
                while (line != null) {
                    sb.append(line);
                    sb.append("\n");
                    line = br.readLine();
                }
                br.close();
                
            } catch(IOException | InterruptedException e) {
                
            }
            return sb.toString();
	}

	// Question e
	/**
	 * Update file entry element in repository
	 * @param id
	 * @param fileEntry
	 * @return
	 */
	@PUT
	@Consumes(MediaType.TEXT_XML)
        @Path("/{id}")
	public String putFileEntry(@PathParam("id") int id, String fileEntry){
            StringBuilder sb = new StringBuilder();
            try {
                Marfcat marf = new Marfcat();
                MarfcatIn marfIn = new MarfcatIn(MARFCAT_IN_PATH);
                
                //Update <file>
                MarfcatInItem marfcatInItem = marfIn.updateItemById(id, fileEntry);
                
                //Get results
                sb.append(marfcatInItem.toString());
                
            } catch(IOException | InterruptedException e) {
                e.printStackTrace();
            }
            return sb.toString();
	}

} 
