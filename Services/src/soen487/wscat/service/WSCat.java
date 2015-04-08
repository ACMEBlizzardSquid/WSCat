package soen487.wscat.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.InterruptedException;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.servlet.ServletContext;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.rexcrawler.Crawler;
import org.xml.sax.SAXException;

import soen487.wscat.marfcat.Marfcat;
import soen487.wscat.marfcat.MarfcatIn;
import soen487.wscat.marfcat.MarfcatInItem;
import soen487.wscat.marfcat.utils.FileDownloader;
import soen487.wscat.parser.DocumentParser;
import soen487.wscat.parser.ParserFactory;
import soen487.wscat.parser.WSDLParser;

@WebService
public class WSCat {
	
	@Resource private WebServiceContext context;
	private boolean initialized = false;
	private String type = null;
	
	/**
	 * Initialization
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	@WebMethod
	public boolean generateTrainSet(){
		try{
			ServletContext servletContext = (ServletContext) context.getMessageContext().get(MessageContext.SERVLET_CONTEXT);
			type = servletContext.getInitParameter("WSCAT");
			if(type == null)
				throw new IOException("Invalid WSCAT");
			String marfcatInPath = "/tmp/TrainSet.marfcatin";
			MarfcatIn trainFile = ParserFactory.getInstance(type).getTrainSet(marfcatInPath, 10);
			trainFile.write();
			new Marfcat().train(marfcatInPath);
			initialized=true;
		}
		catch(IOException e) { System.err.println(e.getLocalizedMessage());}
		catch(InterruptedException e) { System.err.println(e.getLocalizedMessage());}
		return initialized;
	}
	
	@WebMethod(operationName = "isInitialized")
	public boolean isInitialized() {
		ServletContext servletContext = (ServletContext) context.getMessageContext().get(MessageContext.SERVLET_CONTEXT);
		type = servletContext.getInitParameter("WSCAT.type");
		return initialized;
	}

	public String getType() {
		return type;
	}
	
    /**
     * Submits a file to analyze 
     * @param file The string representation of the file
     * @return The MARFCAT_IN output
     * @throws IOException
     * @throws InterruptedException 
     */
    @WebMethod(operationName = "analyzeFile")
    public String analyzeFile(@WebParam(name = "file") String file) 
            throws IOException, InterruptedException {
        
        String localPath = FileDownloader.download(file);
        MarfcatIn marfIn = new MarfcatIn();
        Marfcat marf = new Marfcat();
        MarfcatInItem marfcatInItem = new MarfcatInItem();
        marfcatInItem.setPath(localPath);
        marfcatInItem.loadFileInfo();
        marfIn.addItem(marfcatInItem);
        String marfInPath = marfIn.getPath();
        marfIn.write();
        String MARFCAT_OUT = marf.analyze(marfInPath);    
        BufferedReader br = new BufferedReader(new FileReader(MARFCAT_OUT));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null) {
            sb.append(line);
            sb.append("\n");
            line = br.readLine();
        }
        br.close();
        return sb.toString();
    }

    /*
     Submit a repository URI where WSDLs are linked (for now will
     simply call WSDLRetrieverService’s default retrieveWSDLs() web method and return 
     MARFCAT-IN file or its entries on success.
     */
    @WebMethod(operationName = "submitWSDLRepo", exclude=true)
    public List<String> submitWSDLRepo(@WebParam(name = "repoURI") String repoURI) 
    		throws IOException, InterruptedException {
        WSDLRetrieverWebService_Service wsdlRetrieverService = new WSDLRetrieverWebService_Service();
        WSDLRetrieverWebService wsdlRetriever = wsdlRetrieverService.getWSDLRetrieverWebServicePort();
        
        List<String> wsdls = null;
        try {
            wsdls = wsdlRetriever.retrieveWSDLs(repoURI, null);
        } catch(Exception e) {};
        return wsdls;
    }

    
    /**
     * Trains Marfcat on a provided file
     * @param file A string representation of the file 
     * @param category The category that the file belongs to
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws InterruptedException 
     */
    @WebMethod(operationName = "trainOnFile")
    public String trainOnFile (
            @WebParam(name = "file") String file, 
            @WebParam(name = "category") String category
    ) 
            throws ParserConfigurationException, SAXException, IOException,
                    InterruptedException {
        
        // download the file
        String localPath = FileDownloader.download(file);

        // submit the file for training
        return train(localPath, category);
    }
    
    /**
     * Trains Marfcat using information retrieved from the supplied URI
     * @param URI The URI to download from
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws InterruptedException 
     */
    @WebMethod(operationName = "trainOnURI", exclude=true)
    public void trainOnURI (@WebParam(name = "URI") String URI) 
            throws ParserConfigurationException, SAXException, IOException, 
                    InterruptedException {
        //FIXME
//        // download URI
//        String doc = XMLReader.readAsString(URI);
//        String localPath = FileDownloader.download(doc);
//        String documentation = utils.wsdl.WSDL.getDocumentation(doc);
//        
//        // submit the file for training
//        train(localPath, documentation);
    }
    
    /**
     * Trains Marfcat on a locally saved file
     * @param localPath The local path of the file
     * @param category The subject of the file
     */
    private String train (String localPath, String category) 
            throws IOException, InterruptedException {
        
        
        MarfcatIn marfIn = new MarfcatIn();
        MarfcatInItem item = new MarfcatInItem();
        item.setPath(localPath);
        item.setCVE(category);
        item.loadFileInfo();
        marfIn.addItem(item);
        String marfPath = marfIn.getPath();
        marfIn.write();
        
        // train on MARFCAT_IN file
        Marfcat marf = new Marfcat();
        String STDOUT = marf.train(marfPath);
        BufferedReader br = new BufferedReader(new FileReader(STDOUT));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null) {
            sb.append(line);
            sb.append("\n");
            line = br.readLine();
    }
        br.close();
        return sb.toString();
    }
}
