package soen487.wscat.client.rest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Pm2Client {
	
	public static final String TARGET_BASE = "http://localhost:8080/WSCat/api/";

	public static StringBuffer doGetXML(int id) throws IOException{
		URL target = new URL(TARGET_BASE+"files/"+id);
		HttpURLConnection connection = (HttpURLConnection)target.openConnection();
		
		//Get Response    
		InputStream is = connection.getInputStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		String line;
		StringBuffer response = new StringBuffer(); 
		while((line = rd.readLine()) != null) {
		  response.append(line);
		  response.append('\r');
		}
		return response;
	}
	public static StringBuffer doGetText(int id) throws IOException{
		URL target = new URL(TARGET_BASE+"logs/"+id);
		HttpURLConnection connection = (HttpURLConnection)target.openConnection();
		
		//Get Response    
		InputStream is = connection.getInputStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		String line;
		StringBuffer response = new StringBuffer(); 
		while((line = rd.readLine()) != null) {
		  response.append(line);
		  response.append('\r');
		}
		return response;
	}
	public static StringBuffer doPostXML(String WSDLUrl) throws IOException{
		URL target = new URL(TARGET_BASE+"files/");
		HttpURLConnection connection = (HttpURLConnection)target.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", 
	           "application/x-www-form-urlencoded");

	    connection.setRequestProperty("Content-Length", "" + 
	               Integer.toString(WSDLUrl.getBytes().length));
	    connection.setRequestProperty("Content-Language", "en-US");
	    
	    //Send request
		DataOutputStream wr = new DataOutputStream (
		            connection.getOutputStream ());
		wr.writeBytes (WSDLUrl);
		wr.flush ();
		wr.close ();
			
		//Get Response    
		InputStream is = connection.getInputStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		String line;
		StringBuffer response = new StringBuffer(); 
		while((line = rd.readLine()) != null) {
		  response.append(line);
		  response.append('\r');
		}
		return response;
	}
	public static StringBuffer doPutXML(String WSDLUrl, int id) throws IOException{
		URL target = new URL(TARGET_BASE+"files/"+id);
		HttpURLConnection connection = (HttpURLConnection)target.openConnection();
		connection.setRequestMethod("PUT");
		connection.setRequestProperty("Content-Type", 
	           "application/x-www-form-urlencoded");

	    connection.setRequestProperty("Content-Length", "" + 
	               Integer.toString(WSDLUrl.getBytes().length));
	    connection.setRequestProperty("Content-Language", "en-US");
	    
	    //Send request
		DataOutputStream wr = new DataOutputStream (
		            connection.getOutputStream ());
		wr.writeBytes (WSDLUrl);
		wr.flush ();
		wr.close ();
			
		//Get Response    
		InputStream is = connection.getInputStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		String line;
		StringBuffer response = new StringBuffer(); 
		while((line = rd.readLine()) != null) {
		  response.append(line);
		  response.append('\r');
		}
		return response;
	}
	
	public static int prompt(String msg){
		System.out.print("Select[1-4]: ");
		Scanner scan = new Scanner(System.in);
		return scan.nextInt();
	}
	public static String promptStr(String msg){
		System.out.print("Select[1-4]: ");
		Scanner scan = new Scanner(System.in);
		return scan.nextLine();
	}
	
	public static void main(String[] args) {
		System.out.println("REST Java client PM2");
		System.out.println("--- MENU ---\n"
				+ "1. GET  file\n"
				+ "2. GET  log\n"
				+ "3. POST file\n"
				+ "4. PUT  file\n");
		
		try{
			StringBuffer response;
			switch(prompt("Select[1-4]: ")){
			case 1:
				response = doGetXML(prompt("Insert file id: "));
				System.out.println(response.toString());
				break;
			case 2:
				response = doGetText(prompt("Insert log id: "));
				System.out.println(response.toString());
				break;
			case 3:
				response = doPostXML(promptStr("Insert WSDL Url: "));
				System.out.println(response.toString());
				break;
			case 4:
				response = doPutXML(promptStr("Insert WSDL Url: "), prompt("Insert file id: "));
				System.out.println(response.toString());
				break;
			default:
				System.err.println("Undefined option");
			}
		} catch(IOException e){
			e.printStackTrace();
		}
		System.out.println("---- END ----");
	}

}
