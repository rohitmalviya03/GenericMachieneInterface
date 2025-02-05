package application;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import application.*;

public class ServerConnector {
	
	

	static Map res = ReadPropertyFile.getPropertyValues();
	static String server_ip = (String) res.get("server_ip");
	static String server_port = (String) res.get("server_port");
	 private static ServerSocket serverSocket;
	    private static boolean listening = true; // Flag to control listening state
    public static void sendToServer(String hl7Message) throws Exception {
        String serverAddress = server_ip;//"10.226.28.174";  // Replace with actual server IP
        int port = Integer.parseInt(server_port);//8002;  // Replace with actual server port
        Main.updateConnectionStatusLabel("LIS is trying to connect on : "+ server_ip +" : "+server_port );
        try (Socket socket = new Socket(serverAddress, port);
             OutputStream out = socket.getOutputStream()) {
        	
        	  Main.updateConnectionStatusLabel("LIS is Connected on : "+ server_ip +" : "+server_port );
            out.write(hl7Message.getBytes());
            out.flush();
        } catch (Exception e) {
        	   Main.updateConnectionStatusLabel("Error connecting to server : " + e.getMessage() );
            throw new Exception("Error connecting to server: " + e.getMessage());
         
        }
    }
    
    public static String receiveFromServer() {
    	 int port = Integer.parseInt(server_port);  // The port to listen on
    	   StringBuilder response = new StringBuilder();
         try {
             serverSocket = new ServerSocket(port);
             System.out.println("Listening on port " + port);

             // Continuously listen for incoming connections
             while (listening) {
                 try (Socket clientSocket = serverSocket.accept(); // Accept incoming client connections
                      BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                      
                  
                     String line;

                     // Read data from the client line by line
                     while ((line = in.readLine()) != null) {
                         response.append(line).append("\n");
                     }

                     // Process the received data
                     processReceivedData(response.toString().trim());
                 } catch (Exception e) {
                     System.err.println("Error handling client connection: " + e.getMessage());
                 }
             }
         } catch (Exception e) {
             System.err.println("Error starting server: " + e.getMessage());
         }
		return response.toString();
    }
    
    private static void processReceivedData(String data) {
        // Process the received data as needed (e.g., updating UI, logging, etc.)
        System.out.println("Received: " + data); // For demonstration purposes
        // Implement your logic to update the UI or handle the received data
    }
}
