package soen487.wscat.parser;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.rexcrawler.Page;
import org.rexcrawler.Reduced;

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
		
	public WSCatParser (){
		this.wsdls = new LinkedList<SimpleEntry<String, String>>();
	}

	@Override
	public boolean parsePage(Page page) throws IOException {
		// TODO Define if download WSDL or not
		String file  = page.getConnection().getURL().getFile();
		if (isWSDLLink(file)) {	
			String description = getDescription(page.getContent());
			if (description != null) {
				this.wsdls.add(new SimpleEntry<String, String>(page.getConnection().getURL().toString(), description));
			}
			else
				System.err.println("No WSDL documentation for "+page);
		}
		return true;
	}

	@Override
	public List<String> filterLinks(Page page, List<String> links){
		// Follow only WSDL links
		ListIterator<String> it = links.listIterator();
		while (it.hasNext()) {
			if(! isWSDLLink(it.next()))
				it.remove();
		}
		return links;
	}

	/*
	 * Retrieve wdsl:description value
	 */
	private String getDescription(String wsdlFile){
		String documentationRegex = "<wsdl:service .*>.*(<wsdl:doc.*>(.*?)</wsdl:doc.*>).*</wsdl:service";
		Matcher match = Pattern.compile(documentationRegex, Pattern.DOTALL).matcher(wsdlFile);
		if(match.find())
			return match.group(2);
		return null;
	}

	@Override
	public List<SimpleEntry<String, String>> getWSDLs() {
		return this.wsdls;
	}

	@Reduced
	private List<SimpleEntry<String, String>> wsdls;
}
