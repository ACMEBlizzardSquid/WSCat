package soen487.wscat.parser;

import java.io.IOException;
import java.net.URL;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;

import org.rexcrawler.Crawler;
import org.rexcrawler.CrawlerHandler;

import soen487.wscat.marfcat.MarfcatIn;
import soen487.wscat.marfcat.MarfcatInItem;

/**
 * Common parser interface
 * @author shake0
 *
 */
public abstract class DocumentParser extends CrawlerHandler{
	/**
	 * Retrieve a list of <document, category> tuples
	 * @return List of categorized documents
	 */
	public abstract List<SimpleEntry<String,String>> get();
	
	/**
	 * Get the default root search of the crawler
	 * @return
	 * @throws IOException
	 */
	public abstract URL getRoot() throws IOException;
	
	/**
	 * Make the MARFCAT-IN file from this parser
	 * @param path for the MARFCAT-IN file
	 * @param length the maximum number of entry in the MARFCAT-IN
	 * @return the MARCATIN object
	 * @throws IOException
	 */
	public MarfcatIn getTrainSet(String path, int length) throws IOException{
		new Crawler()
		.setHandler(this)
		.setChunkSize(2)
		.setSearchLength(length)
		.run(this.getRoot());
		
		MarfcatIn trainSet = new MarfcatIn(path);
		for(SimpleEntry<String, String>entry: this.get()){
			MarfcatInItem item = new MarfcatInItem();
			item.setOriginalWsdlUri(entry.getKey());
			item.setCVE(entry.getValue());
			trainSet.addItem(item);
		}
		return trainSet;
	}
}
