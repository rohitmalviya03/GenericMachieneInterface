package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HL7Client {

    public static void main(String[] args) {
        String serverAddress = "10.226.17.67"; // Replace with the server's IP address or hostname
        int serverPort = 12345; // Use the same port as the server

        try (Socket socket = new Socket(serverAddress, serverPort);
             OutputStream outputStream = socket.getOutputStream();
             InputStream inputStream = socket.getInputStream()) {

        	System.out.println(inputStream.toString());
            // Construct an HL7 message (modify as needed)
            String hl7Message = "Your HL7 Message\r"; // HL7 messages end with '\r'
            
            // Send the HL7 message to the server
            outputStream.write(hl7Message.getBytes());
            outputStream.flush();
            System.out.println("Sent HL7 message: ");
            System.out.println(hl7Message);

            // Receive and process the response (if any)
            byte[] buffer = new byte[1024];
            int bytesRead;
            StringBuilder response = new StringBuilder();

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                response.append(new String(buffer, 0, bytesRead));
                if (response.toString().endsWith("\r")) {
                    break; // HL7 response ends with '\r'
                }
            }

            System.out.println("Received response: ");
            System.out.println(response.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
