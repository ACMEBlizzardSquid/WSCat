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
 * Specific parser for 
 * http://www.programmableweb.com/apis/directory
 * 
 * WSDL Parser
 * 
 * @author shake0
 *
 */
public class ProgrammableWebParser extends DocumentParser {
	
	public ProgrammableWebParser() {
		this.wsdls = new LinkedList<>();
		this.lastListingPage = new AtomicInteger(0);
	}

	@Override
	public boolean parsePage(Page page) throws IOException {
		if(isServicePage(page.getConnection().getURL())){
			// Get category
			String categoryRegex = "Primary Category</label>\\s*.span.\\s*.a href=\"([a-zA-Z0-9/]*)\"";
			Matcher match = Pattern.compile(categoryRegex, Pattern.DOTALL).matcher(page.getContent());
			if(match.find()){
				System.out.print(".");
				this.categoryString = match.group(1);
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
			ListIterator<String> it = links.listIterator();
			String url, wsdl=null, category=null;
			// Search for a WSDL link
			while(it.hasNext()){
				url = it.next();
				if(isWSDLLink(url))     wsdl = url;
				if(isCategoryLink(url)) category = url;
			}
			// Save WSDL link
			if(wsdl != null && category != null){
				this.wsdls.add(new SimpleEntry<String, String>
				(wsdl, getCategory(categoryString)));
			}
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
	
	private String        categoryString;
	private AtomicInteger lastListingPage;
}
