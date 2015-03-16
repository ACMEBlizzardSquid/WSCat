package soen487.wscat.service.rest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
	// TODO: Not clear if it consumes XML or produces XML (Question d)
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	public String postFileEntry(@FormParam("uri") String uri){
            
            StringBuilder sb = new StringBuilder();
            try {
                //download wsdl
                String wsdlFile = XMLReader.readAsString(uri);
                String localPath = FileDownloader.download(wsdlFile);
                
                MarfcatIn marfIn = new MarfcatIn(MARFCAT_IN_PATH);
                Marfcat marf = new Marfcat();
                MarfcatInItem marfcatInItem = new MarfcatInItem();
                marfcatInItem.setPath(localPath);               
                marfcatInItem.loadFileInfo();
                marfIn.addItem(marfcatInItem);
                String marfInPath = marfIn.getPath();
                marfIn.write();
                String MARFCAT_OUT = marf.analyze(marfInPath);    
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
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String putFileEntry(@PathParam("id") int id, @FormParam("file") String fileEntry){
            //TODO: what does "update a file" mean ? should we get the wsdl again ? 
            // sending a <file> over rest doesnt make much sense, so Im assuming 
            //it has something to do with the WSDL.
            /*try {
                //For now, updating cve
                MarfcatInItem marfcatInItem = marfIn.updateItem(id);
            } catch(IOException | InterruptedException e) {
                
            }*/
            return Integer.toString(id) + " " + fileEntry;
	}

} 
