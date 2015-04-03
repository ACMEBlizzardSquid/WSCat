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

        /**
         * Gets XML from the files endpoint
         * @return XML from the files endpoint
         * @throws IOException 
         */
	public static StringBuffer doGetXML() throws IOException{
		URL target = new URL(TARGET_BASE+"files/");
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
        
        /**
         * Gets the logs from the endpoint
         * @return The logs string
         * @throws IOException 
         */
	public static StringBuffer doGetText(String id) throws IOException{
		URL target = new URL(TARGET_BASE+"logs/");
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
        
        /**
         * POSTs XML to the /files endpoint
         * @param file The file to POST
         * @return
         * @throws IOException 
         */
	public static StringBuffer doPostXML(String file) throws IOException{
		URL target = new URL(TARGET_BASE+"files/");
		HttpURLConnection connection = (HttpURLConnection)target.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", 
	           "text/xml");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                connection.setRequestProperty("Content-Length", "" + 
                           Integer.toString(file.getBytes().length));
                connection.setRequestProperty("Content-Language", "en-US");

                //Send request
		DataOutputStream wr = new DataOutputStream (
		            connection.getOutputStream ());
		wr.writeBytes (file);
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
        
        /**
         * Updates a file at the /files/{id} endpoint
         * @param file The updated file
         * @param id The ID to update
         * @return
         * @throws IOException 
         */
	public static StringBuffer doPutXML(String file, int id) throws IOException{
                URL target = new URL(TARGET_BASE+"files/"+id);
                HttpURLConnection connection = (HttpURLConnection)target.openConnection();
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("Content-Type", 
                   "text/xml");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                connection.setRequestProperty("Content-Length", "" + 
                           Integer.toString(file.getBytes().length));
                connection.setRequestProperty("Content-Language", "en-US");

                //Send request
                DataOutputStream wr = new DataOutputStream (
                            connection.getOutputStream ());
                wr.writeBytes (file);
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
	
        /**
         * Prompts the user to enter an integer 
         * @param msg The prompt to display to the user
         * @return 
         */
	public static int prompt(String msg){
		System.out.print(msg);
		Scanner scan = new Scanner(System.in);
		return scan.nextInt();
	}
        
        /**
         * Prompts the user to enter a string
         * @param msg The prompt to display to the user
         * @return 
         */
	public static String promptStr(String msg){
		System.out.print(msg);
		Scanner scan = new Scanner(System.in);
		return scan.nextLine();
	}
	
	public static void main(String[] args) {
		System.out.println("REST Java client PM2");
		System.out.println("--- MENU ---\n"
				+ "1. GET  files\n"
				+ "2. GET  log\n"
				+ "3. POST file\n"
				+ "4. PUT  file\n");
		
		try{
			StringBuffer response;
			switch(prompt("Select[1-4]: ")){
			case 1:
				response = doGetXML();
				System.out.println(response.toString());
				break;
			case 2:
				response = doGetText(promptStr("Insert log id: "));
				System.out.println(response.toString());
				break;
			case 3:
				response = doPostXML(promptStr("Insert <file>: "));
				System.out.println(response.toString());
				break;
			case 4:
				response = doPutXML(promptStr("Insert <file>: "), prompt("Insert file id: "));
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
