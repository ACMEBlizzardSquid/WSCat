package soen487.wscat.parser;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;

import org.rexcrawler.CrawlerHandler;
import org.rexcrawler.Page;

/**
 * This is the default parser used in WSDLRetriever
 * It is meant to be a simple WSDL retriever that meet
 * the assignment requirements.
 * 
 * I'm going to capture the wsdl:description since is 
 * the only feature that may be available in every WSDL.
 * 
 * @author shake0
 *
 */
public class WSCatParser extends WSDLParser {

	@Override
	public boolean parsePage(Page page) throws IOException {
		// TODO Define if download WSDL or not
		return false;
	}

	@Override
	public List<SimpleEntry<String, String>> getWSDLs() {
		// TODO Auto-generated method stub
		return null;
	}

}
