package soen487.wscat.parser;

import java.io.IOException;
import java.net.URL;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;

import org.rexcrawler.Crawler;

/**
 * Test the parser are working
 * @author shake0
 *
 */
public class ParserTest {
	public static Crawler crawler;

	public static void main(String[] args) throws IOException {
		List<SimpleEntry<String,String>> rs;
		
		// 1. WSDL Service
		rs = parse(new URL("http://www.programmableweb.com/api/ebay"), new WSDLParser());
		System.out.println(rs.get(0).getKey() + " :: " + rs.get(0).getValue());
		
		// 2. NON WSDL Service
		rs = parse(new URL("http://www.programmableweb.com/api/twitter"), new WSDLParser());
		System.out.println(rs.size());
		
		// 3. WSDL Service with no link
		rs = parse(new URL("http://www.programmableweb.com/api/shopello"), new WSDLParser());
		System.out.println(rs.size());
		
		// 4. REST Service
		rs = parse(new URL("http://www.programmableweb.com/api/twitter"), new RESTParser());
		System.out.println(rs.get(0).getKey() + " :: " + rs.get(0).getValue());
		
		// 5. NON REST Service
		rs = parse(new URL("http://www.programmableweb.com/api/shopello"), new RESTParser());
		System.out.println(rs.size());
		
		// 6. REST Service with no link
		rs = parse(new URL("http://www.programmableweb.com/api/ebay"), new RESTParser());
		System.out.println(rs.size());
	}
	
	public static List<SimpleEntry<String,String>> parse(URL target, DocumentParser parser){
		crawler = new Crawler(); // workaround for bug (Crawler.status = 0)
		crawler.setHandler(parser).run(target);
		return parser.get();
	}
}
