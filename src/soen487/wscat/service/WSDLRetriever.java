package soen487.wscat.service;

import java.io.IOException;
import java.lang.InterruptedException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.AbstractMap.SimpleEntry;
import java.util.LinkedList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.rexcrawler.Crawler;
import org.rexcrawler.Page;

import soen487.wscat.marfcat.MarfcatIn;
import soen487.wscat.marfcat.MarfcatInItem;
import soen487.wscat.marfcat.utils.FileDownloader;
import soen487.wscat.parser.DocumentParser;
import soen487.wscat.parser.ProgrammableWebParser;
import soen487.wscat.parser.WSCatParser;
import soen487.wscat.parser.WSDLParser;

@WebService(serviceName="WSDLRetrieverWebService", name="WSDLRetrieverWebService")
public class WSDLRetriever {
	
	@WebMethod
	public List<String> retrieveWSDLs(String pstrSeedURI, 
			Integer piLimit) throws IOException, InterruptedException {
		return retrieve(pstrSeedURI, piLimit +1, new WSCatParser());
	}
	
	private static List<String> retrieve(String rootSearch, int searchLimit, DocumentParser parser) 
			throws IOException, InterruptedException {
		
		// Crawl
		Crawler crawler = new Crawler();
		crawler.setHandler(parser);
		//crawler.setChunkSize(8);
		crawler.setSearchLength(searchLimit);
		crawler.run(new URL(rootSearch));
		
		// Marfcat
		MarfcatIn marf = new MarfcatIn();
		
		for(SimpleEntry<String, String> se : parser.get()){
			System.out.println("\nCATEGORY: "+se.getValue()+" -- WSDL: "+se.getKey());
			// Save WSDL
			try{
				String wsdlURL  = se.getKey();
				Page   page     = new Page((HttpURLConnection) new URL(wsdlURL).openConnection());
				String filename = FileDownloader.getRandomName(page.toString());
				String path     = FileDownloader.download(page.getContent(), filename);
				
				// Save WDSL-Category relationship
                                MarfcatInItem item = new MarfcatInItem();
                                item.setPath(path);
                                item.setCVE(se.getValue());
                                item.loadFileInfo();
				marf.addItem(item);
			} catch(IOException e){
				e.printStackTrace();
				continue;
			}
		}
		
		// Write MARFCAT-IN
		List<String> marfcatInPaths = new LinkedList<String>();
                marf.write();
		marfcatInPaths.add(marf.getPath());
		
		return marfcatInPaths;
	}
	
	@WebMethod(exclude=true)
	public static void main(String[] args) throws IOException, InterruptedException {
		// Grab ProgrammableWeb
		retrieve(ProgrammableWebParser.ROOT, 100 , new ProgrammableWebParser());
		// Example
//		retrieve("http://data.serviceplatform.org/wsdl_grabbing/service_repository-wsdls/valid_WSDLs/", 
//				10, new WSCatParser());
	}
}
