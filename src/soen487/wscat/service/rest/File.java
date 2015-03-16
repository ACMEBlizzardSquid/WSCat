package soen487.wscat.service.rest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

	// Question c
	@GET
	@Produces(MediaType.APPLICATION_XML)
        @Path("/{id}")
	public String getFileEntry(@PathParam("id") int id) throws IOException{
		return Integer.toString(id);
	}
        
        @GET
	@Produces(MediaType.APPLICATION_XML)
	public String getFileEntries(@PathParam("id") int id) throws IOException{
		return Integer.toString(id);
	}

	// TODO: Not clear if it consumes XML or produces XML (Question d)
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	public String postFileEntry(@FormParam("uri") String uri){
            
            StringBuilder sb = new StringBuilder();
            try {
                //download wsdl
                String wsdlFile = XMLReader.readAsString(uri);
                String localPath = FileDownloader.download(wsdlFile);
                
                MarfcatIn marfIn = new MarfcatIn();
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
            } catch(Exception e) {
                
            }
            return sb.toString();
	}

	// Question e
	@PUT
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String putFileEntry(@PathParam("id") int id, @FormParam("file") String fileEntry){
		return Integer.toString(id) + " " + fileEntry;
	}

} 
