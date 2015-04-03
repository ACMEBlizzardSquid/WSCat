package soen487.wscat.parser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.rexcrawler.Page;
import org.rexcrawler.Reduced;

/**
 * WADL Parser
 * 
 * Training parser based on 
 * http://www.programmableweb.com/apis/directory
 *
 * @author shake0
 *
 */
public class WADLParser extends DocumentParser {
	
	public WADLParser() {
		this.wadls = new LinkedList<>();
	}

	@Override
	protected HttpURLConnection makeConnection(String url)
			throws MalformedURLException, IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setRequestProperty("User-Agent", "ELinks/0.9.3 (textmode; Linux 2.6.9-kanotix-8 i686; 127x41)");
		
		// Safaty
		if(url.contains("google")){
			try {
				double sec = 10*Math.random();
				Thread.sleep((long) (sec*1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return conn;
	}
	
	@Override
	public boolean parsePage(Page page) throws IOException {
		String url = page.getConnection().getURL().toString();
		if(! url.contains("google")){
			// wadl
			String doc = getDescription(page.getContent());
			if(doc != null){
				wadls.add(new SimpleEntry<String, String>(url, doc));
			}
		}
		return true;
	}
	
	@Override
	public List<String> filterLinks(Page page, List<String> links) {
		if(page.getConnection().getURL().toString().contains("google")){
			// Google search
			// TODO: Submit the next Google page to parse
			return getResultLinks(links);
		}
		links.clear();
		return links;
	}
	
	private List<String> getResultLinks(List<String> links){
		List<String> results = new ArrayList<>();
		for(String link: links){
			if(link.startsWith("/url?")){
				Matcher m = Pattern.compile("q=(.*?)&amp").matcher(link);
				if(m.find()) results.add(m.group(1));
			}
		}
		return results;
	}
	
	private String getDescription(String wadl){
		Pattern regex = Pattern.compile("<wadl:doc>(.*?)</wadl:doc>", Pattern.DOTALL);
		Matcher m = regex.matcher(wadl);
		if(m.find()){
			return m.group(1).trim();
		}
		return null;
	}
	
	//--------------------------------------------
	// Paging
	
	public static String BuildRequest(String query){
		return BuildRequest(query, 0, 10);
	}
	
	public static String BuildRequest(String query, int start, int limit){
		String base = "http://www.google.com/search?";
		return base + "q=" +query + "&start="+start + "&num="+limit;
	}
	
	//--------------------------------------------
	// Data
	
	@Override
	public List<SimpleEntry<String, String>> get(){
		return this.wadls;
	}
	
	@Reduced
	private List<SimpleEntry<String, String>> wadls;
}
