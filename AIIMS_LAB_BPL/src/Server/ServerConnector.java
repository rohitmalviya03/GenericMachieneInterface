package Server;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;


public class ServerConnector {
	
	
	PrintStream cout;
	static Map res = ReadPropertyFile.getPropertyValues();
	static String server_ip = (String) res.get("server_ip");
	static String server_port = (String) res.get("server_port");
	 private static ServerSocket serverSocket;
	    private static boolean listening = true; // Flag to control listening state
    public static int sendToServer(String hl7Message) throws Exception {
        String serverAddress = server_ip;//"10.226.28.174";  // Replace with actual server IP
        int port = Integer.parseInt(server_port);//8002;  // Replace with actual server port
        
       // AIIMSLAB.updateConnectionStatusLabel("LIS is trying to connect on : "+ server_ip +" : "+server_port );
        int res=0;
        try (Socket socket = new Socket(serverAddress, port);
             OutputStream out = socket.getOutputStream()) {
        	 InputStream in = socket.getInputStream();

        	
        //	  AIIMSLAB.updateConnectionStatusLabel("LIS is Connected on : "+ server_ip +" : "+server_port );
        	  System.out.println("LIS is Connected on : "+ server_ip +" : "+server_port );
        	  //System.out.println(hl7Message);
        	  AIIMSLAB.saveToFile("LIS is Connected on : "+ server_ip +" : "+server_port , AIIMSLAB.FILE_NAME);
          	 
            out.write(hl7Message.getBytes());
            out.flush();
            AIIMSLAB.saveToFile("Sent :  "+ hl7Message , AIIMSLAB.FILE_NAME);
            res=1;
          //  new Thread(new ResponseReceiver(socket)).start();
            
            
        } catch (Exception e) {
        	res=0;
        	   AIIMSLAB.updateConnectionStatusLabel("Error connecting to server : " + e.getMessage() );
        	   System.out.println("Error connecting to server : " + e.getMessage() );
        	 //  AIIMSLAB.addLogEntry("Something Went Wrong");
        	   AIIMSLAB.saveToFile("Stack Trace: " + AIIMSLAB.getStackTraceAsString(e), AIIMSLAB.FILE_NAME);
				
            throw new Exception("Error connecting to server: " + e.getMessage());
         
        }
        
        return res;
    }
    
    public static String receiveFromServer() {
    	 int port =Integer.parseInt(server_port);  // The port to listen on
    	   StringBuilder response = new StringBuilder();
         try {
             serverSocket = new ServerSocket(port);
             System.out.println("Listening on port " + port);

             // Continuously listen for incoming connections
             String resACK1="MSH|^~\\&|host||cobas pure||YYYYMMDDHHMMSS||ACK^R22^ACK|CONTROLID||2.5.1||||NE||UNICODE UTF-8|\r"
	        	 		+ "MSA|AA|CONTROLID||\r"
	        	 		+ "";
             
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
                     AIIMSLAB.saveToFile("Stack Trace: " + AIIMSLAB.getStackTraceAsString(e), AIIMSLAB.FILE_NAME);
         			
                 }
             }
         } catch (Exception e) {
        	  AIIMSLAB.saveToFile("Stack Trace: " + AIIMSLAB.getStackTraceAsString(e), AIIMSLAB.FILE_NAME);
  			
             System.err.println("Error starting server: " + e.getMessage());
         }
		return response.toString();
    }
    
    
    
    
    
    
    
    // Runnable class for receiving data
    private static class ResponseReceiver implements Runnable {
        private final Socket socket;

        public ResponseReceiver(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (InputStream in = socket.getInputStream()) {
                StringBuilder response = new StringBuilder();
                byte[] buffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = in.read(buffer)) != -1) {
                    response.append(new String(buffer, 0, bytesRead));
                    // You may want to add a condition to break the loop if you expect a specific end condition
                }

                // Print or process the server response
                System.out.println("Response from server: " + response.toString());

            } catch (Exception e) {
                System.out.println("Error receiving data: " + e.getMessage());
            }
        }
    }
    
    
    private static void processReceivedData(String data) {
        // Process the received data as needed (e.g., updating UI, logging, etc.)
        System.out.println("Received: " + data); // For demonstration purposes
        // Implement your logic to update the UI or handle the received data
    }
}


