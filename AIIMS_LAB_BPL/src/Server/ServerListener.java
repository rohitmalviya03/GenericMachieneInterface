package Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerListener implements Runnable {
    private volatile boolean listening = true; // Make it volatile for safe thread communication
	PrintStream cout;
    @Override
    public void run() {
        int port = 50001; // The port to listen on
        StringBuilder response = new StringBuilder();

        
       String ACK="MSH|^~\\&|LAB|myla|LIS|LAB|YYYYMMDDHHMMSS||ACK^O33^OML_O33|CONTROLID|P|2.5.1|||NE|AL||UNICODE UTF-8\r"
       		+ "MSA|AA|MSGCONTROL|Message Accepted\r"
       		;
       
    
	    
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Listening on port " + port);

            // Continuously listen for incoming connections
            while (listening) {
                try (Socket clientSocket = serverSocket.accept(); // Accept incoming client connections
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            		cout = new PrintStream(clientSocket.getOutputStream()); // Obj For Sending Msg to CLient

                    String line;

                    // Read data from the client line by line
                    while ((line = in.readLine()) != null) {
                        response.append(line).append("\n");
                    }
                    
                    
                    String[] msgPart=response.toString().split("\r");
                    
                    String[] mshPart=msgPart[0].split("\\|");
                	String controlid=mshPart[9];
                	 Date currentDate = new Date();

		      	        // Define the desired date format
		      	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssZ");

		      	        // Format the date
		      	        String formattedDate = dateFormat.format(currentDate);

                    ACK=ACK.replaceAll("CONTROLID",controlid);
                    ACK=ACK.replaceAll("YYYYMMDDHHMMSS",formattedDate);
                    cout.print(ACK);
                    System.out.println("ACK SENT");
                    // Process the received data
                    processReceivedData(response.toString().trim());
                } catch (Exception e) {
                    System.err.println("Error handling client connection: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Error starting server: " + e.getMessage());
        }
    }

    public void stopListening() {
        listening = false; // Method to stop the server
    }

    private void processReceivedData(String data) {
        // Your processing logic here
        System.out.println("Received data RES: " + data);
    }
}

