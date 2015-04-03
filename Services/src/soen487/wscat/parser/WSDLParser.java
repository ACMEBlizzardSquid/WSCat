package soen487.wscat.parser;

import java.io.IOException;
import java.net.URL;
import java.util.AbstractMap.SimpleEntry;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.rexcrawler.Page;
import org.rexcrawler.Reduced;

/**
 * WSDL Parser
 * 
 * Training parser based on 
 * http://www.programmableweb.com/apis/directory
 *
 * @author shake0
 *
 */
public class WSDLParser extends DocumentParser {
	
	public WSDLParser() {
		this.wsdls = new LinkedList<SimpleEntry<String,String>>();
		this.lastListingPage = new AtomicInteger(0);
	}

	@Override
	public boolean parsePage(Page page) throws IOException {
		if(isServicePage(page.getConnection().getURL())){
			String categoryString = null, endpointString = null;
			boolean prot = false;
			
			// Show processing
			System.out.print(".");
			
			// Get category
			String categoryRegex = "Primary Category</label>\\s*.span.\\s*.a href=\"(.*?)\"";
			Matcher match = Pattern.compile(categoryRegex, Pattern.DOTALL).matcher(page.getContent());
			if(match.find()){
				categoryString = match.group(1);
			}
			else return true;
			
			// Get endpoint
			String endpointRegex = "Endpoint</label>\\s*.span.\\s*.a href=\"(.*?)\"";
			match = Pattern.compile(endpointRegex, Pattern.DOTALL).matcher(page.getContent());
			if(match.find()){
				endpointString = match.group(1);
			}
			else return true;
			
			// Get protocol
			String protocolRegex = "Protocol / Formats</label>\\s*.span.(.+?)./span";
			match = Pattern.compile(protocolRegex, Pattern.DOTALL).matcher(page.getContent());
			if(match.find()){
				prot = match.group(1).contains("SOAP");
			}
			else return true;
			
			// Insert service
			if(prot && categoryString != null && endpointString != null && isWSDLLink(endpointString)){
				this.wsdls.add(new SimpleEntry<String, String>
				(endpointString, getCategory(categoryString)));
			}
		}
		return true;
	}
	
	@Override
	public List<String> filterLinks(Page page, List<String> links) {
		if(isListingPage(page.getConnection().getURL())){
			ListIterator<String> it = links.listIterator();
			// keep only links to services
			while(it.hasNext()){
				if(! isServicePage(it.next()))
					it.remove();
			}
			// insert next listing page
			int listingPage = lastListingPage.incrementAndGet();
			if( listingPage < NPAGES)
				links.add(makeDirectoryURL(listingPage));
		}
		else{
			// No need to keep following this branch
			links.clear();
		}
		return links;
	}
	
	private boolean isServicePage(String domain){
		return domain.startsWith("http://www.programmableweb.com/api/");
	}
	
	private boolean isServicePage(URL domain){
		return isServicePage(domain.getProtocol()+"://"+domain.getAuthority()+domain.getPath());
	}
	
	private boolean isListingPage(URL domain){
		if(domain.getFile().contains("apis/directory"))
			return true;
		return false;
	}
	
	private boolean isCategoryLink(String url){
		if(url.contains("/category/"))
			return true;
		return false;
	}
	
	private boolean isWSDLLink(String url){
		if(url.contains("WSDL") || url.contains("wsdl"))
			return true;
		return false;
	}
	
	private String getCategory(String url){
		if(url != null)
			return url.substring(url.lastIndexOf('/') + 1);
		return "";
	}
	
	//--------------------------------------------
	// Paging
	
	@SuppressWarnings("unused")
	private int getListingPageNumber(URL domain){
		if(domain.getQuery() == null)
			return 0;
		
		int start = domain.getQuery().indexOf('=');
		return Integer.valueOf(domain.getQuery().substring(start + 1));
	}
	
	private String makeDirectoryURL(int pageNumber){
		String base = "http://www.programmableweb.com/apis/directory";
		if(pageNumber > 0)
			base += "?page="+pageNumber;
		return base;
	}
	
	//--------------------------------------------
	// Data
	
	@Override
	public List<SimpleEntry<String, String>> get(){
		return this.wsdls;
	}
	
	public static final String  ROOT   = "http://www.programmableweb.com/apis/directory";
	public static final Integer NPAGES = 116;
	
	@Reduced
	private List<SimpleEntry<String, String>> wsdls;
	
	private AtomicInteger lastListingPage;
}
