package soen487.wscat.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.InterruptedException;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import soen487.wscat.marfcat.Marfcat;
import soen487.wscat.marfcat.MarfcatIn;
import soen487.wscat.marfcat.MarfcatInItem;
import soen487.wscat.marfcat.utils.FileDownloader;

@WebService
public class WSCat {

    /**
     * Submits a WSDL file to analyze 
     * @param wsdlFile The string representation of the WSDL file
     * @return The MARFCAT_IN output
     * @throws IOException
     * @throws InterruptedException 
     */
    @WebMethod(operationName = "submitWSDLToAnalyze")
    public String submitWSDLToAnalyze(@WebParam(name = "wsdlFile") String wsdlFile) 
            throws IOException, InterruptedException {
        
        String localPath = FileDownloader.download(wsdlFile);
        MarfcatIn marfIn = new MarfcatIn();
        Marfcat marf = new Marfcat();
        marfIn.addItem(new MarfcatInItem(localPath));
        String marfInPath = marfIn.write();
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
    @WebMethod(operationName = "submitWSDLRepo")
    public List<String> submitWSDLRepo(@WebParam(name = "repoURI") String repoURI) 
    		throws IOException, InterruptedException{
        WSDLRetrieverService wsdlRetrieverService = new WSDLRetrieverService();
        WSDLRetriever wsdlRetriever = wsdlRetrieverService.getWSDLRetrieverPort();
        
        return wsdlRetriever.retrieveWSDLs(repoURI, null);
    }

    
    /**
     * Trains Marfcat on a provided WSDL file
     * @param wsdlFile A string representation of the WSDL file 
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws InterruptedException 
     */
    @WebMethod(operationName = "trainOnWSDL")
    public void trainOnWSDL (@WebParam(name = "wsdlFile") String wsdlFile) 
            throws ParserConfigurationException, SAXException, IOException,
                    InterruptedException {
        
        // format the WSDL file
        String localPath = FileDownloader.download(wsdlFile);
       //FIXME
        //String documentation = utils.wsdl.WSDL.getDocumentation(wsdlFile);

        // submit the file for training
        //train(localPath, documentation);
    }
    
    /**
     * Trains Marfcat using information retrieved from the supplied URI
     * @param URI The URI to download from
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws InterruptedException 
     */
    @WebMethod(operationName = "trainOnURI")
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
     * Trains Marfcat on a locally saved WSDL file
     * @param localPath The local path of the WSDL file
     * @param subject The subject of the WSDL file
     */
    private void train (String localPath, String subject) 
            throws IOException, InterruptedException {
        
        
        MarfcatIn marfIn = new MarfcatIn();
        marfIn.addItem(new MarfcatInItem(localPath, "CVE-2009-3548"));
        String marfPath = marfIn.write();
        
        // train on MARFCAT_IN file
        Marfcat marf = new Marfcat();
        marf.train(marfPath);
    }
}
