package soen487.wscat.parser;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;

import org.rexcrawler.CrawlerHandler;

/**
 * Abstract WSDL parser
 * @author shake0
 *
 */
public abstract class WSDLParser extends CrawlerHandler {

	/**
	 * Get the WSDL parsed
	 * @return The list of tuples (WSDL_url, category)
	 */
	public abstract List<SimpleEntry<String, String>> getWSDLs();
	
	protected boolean isWSDLLink(String url){
		if(url.contains("WSDL") || url.contains("wsdl"))
			return true;
		return false;
	}

}
