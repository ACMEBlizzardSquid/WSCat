package soen487.wscat.parser;

public class ParserFactory {
	public static final String WSDL_PARSER = "WSDL";
	public static final String REST_PARSER = "REST";
	public static final String WADL_PARSER = "WADL";
	public static DocumentParser getInstance(String type){
		if(type.equalsIgnoreCase(WADL_PARSER))
			return new WADLParser();
		if(type.equalsIgnoreCase(REST_PARSER))
			return new RESTParser();
		if(type.equalsIgnoreCase(WSDL_PARSER))
			return new WSDLParser();
		throw new IllegalArgumentException(type+ " is defined");
	}
}
