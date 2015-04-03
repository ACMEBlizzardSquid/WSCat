package soen487.wscat.parser;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;

import org.rexcrawler.CrawlerHandler;

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
}
