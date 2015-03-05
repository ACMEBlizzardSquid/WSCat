package soen487.wscat.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.AbstractMap.SimpleEntry;
import java.util.LinkedList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.rexcrawler.Crawler;
import org.rexcrawler.CrawlerHandler;
import org.rexcrawler.Page;

import soen487.wscat.marfcat.MarfcatIn;
import soen487.wscat.marfcat.MarfcatInItem;
import soen487.wscat.marfcat.utils.FileDownloader;
import soen487.wscat.parser.ProgrammableWebParser;
import soen487.wscat.parser.WSCatParser;
import soen487.wscat.parser.WSDLParser;

@WebService
public class WSDLRetriever {

	/*
	 * TODO:
	 * What this method is suppose to return??
	 */	
	private static List<String> retrieve(String rootSearch, int seachLimit, WSDLParser parser)
			throws IOException, InterruptedException {
		
		// Crawl
		Crawler crawler = new Crawler();
		crawler.setHandler(parser);
		crawler.setChunkSize(8);
		crawler.setSearchLength(seachLimit);
		crawler.run(new URL(ProgrammableWebParser.ROOT));
		
		// Marfcat
		MarfcatIn marf = new MarfcatIn();
		
		for(SimpleEntry<String, String> se : parser.getWSDLs()){
			System.out.println("\nCATEGORY: "+se.getValue()+" -- WSDL: "+se.getKey());
			// Save WSDL
			try{
				String wsdlURL  = se.getKey();
				Page   page     = new Page((HttpURLConnection) new URL(wsdlURL).openConnection());
				String filename = FileDownloader.getRandomName(page.toString());
				String path     = FileDownloader.download(page.getContent(), filename);
				
				// Save WDSL-Category relationship
				marf.addItem(new MarfcatInItem(path, se.getValue()));
			} catch(IOException e){
				e.printStackTrace();
				continue;
			}
		}
		
		// Write MARFCAT-IN
		List<String> marfcatIn = new LinkedList<>();
		marfcatIn.add(marf.write());
		
		return marfcatIn;
	}
	
	@WebMethod(operationName = "retrieveWSDLs")
	public List<String> retrieve(@WebParam(name = "wsdlURI") String pstrSeedURI, 
			@WebParam(name = "limit") Integer piLimit) throws IOException, InterruptedException {
		return retrieve(pstrSeedURI, piLimit, new WSCatParser());
	}
	
	@WebMethod(exclude=true)
	public static void main(String[] args) throws IOException, InterruptedException {
		retrieve(ProgrammableWebParser.ROOT, 100 , new ProgrammableWebParser());
	}
}
