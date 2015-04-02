package soen487.wscat.parser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.rexcrawler.Crawler;

/** 
 * <em>JUnit</em><br/>
 * Parsers test
 * 
 * @author shake0
 *
 */
public class ParserTest {
	private Crawler crawler;
	private List<SimpleEntry<String,String>> rs;
	
	public List<SimpleEntry<String,String>> parse(URL target, DocumentParser parser){
		crawler.setHandler(parser).run(target);
		return parser.get();
	}
	
	@Before
	public void setUp(){
		crawler = new Crawler();
	}
	
	@Test
	public void parseWSDLService() throws IOException{
		rs = parse(new URL("http://www.programmableweb.com/api/ebay"), new WSDLParser());
		assertTrue(rs.size()>0);
		assertEquals("http://developer.ebay.com/webservices/latest/eBaySvc.wsdl", rs.get(0).getKey());
		assertEquals("search", rs.get(0).getValue());
	}
	
	@Test
	public void parseNonWSDLService() throws IOException{
		rs = parse(new URL("http://www.programmableweb.com/api/twitter"), new WSDLParser());
		assertEquals(0, rs.size());
	}
	
	@Test
	public void parserWSDLServiceWithMissingLink() throws IOException{
		rs = parse(new URL("http://www.programmableweb.com/api/shopello"), new WSDLParser());
		assertEquals(0, rs.size());
	}
	
	@Test
	public void parseRESTService() throws IOException{
		rs = parse(new URL("http://www.programmableweb.com/api/twitter"), new RESTParser());
		assertTrue(rs.size() > 0);
		assertEquals("http://twitter.com/statuses/", rs.get(0).getKey());
		assertEquals("social", rs.get(0).getValue());
	}
	
	@Test
	public void parseNonRESTService() throws IOException {
		rs = parse(new URL("http://www.programmableweb.com/api/shopello"), new RESTParser());
		assertEquals(0, rs.size());
	}
	
	@Test
	public void parseRESTServiceWithMissingLink() throws IOException {
		rs = parse(new URL("http://www.programmableweb.com/api/ebay"), new RESTParser());
		assertEquals(0, rs.size());
	}
}
