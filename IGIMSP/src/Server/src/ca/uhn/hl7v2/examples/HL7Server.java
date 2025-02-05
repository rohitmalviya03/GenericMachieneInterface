package ca.uhn.hl7v2.examples;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HL7Server {

    public static void main(String[] args) {
        int port = 54000; // Choose a suitable port number
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("HL7 server is  54000 listening on port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                System.out.println("port: " + clientSocket.getLocalPort());
                System.out.println("Client connected: " + clientSocket.getLocalAddress());
                

                // Handle the client's request in a new thread
                Thread clientThread = new Thread(() -> handleClient(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (InputStream inputStream = clientSocket.getInputStream();
             OutputStream outputStream = clientSocket.getOutputStream()) {
            
            byte[] buffer = new byte[1024];
            int bytesRead;
            StringBuilder hl7Message = new StringBuilder();

            // Read the incoming HL7 message
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                hl7Message.append(new String(buffer, 0, bytesRead));
                if (hl7Message.toString().endsWith("\r")) {
                    break; // HL7 message ends with '\r'
                }
            }

            // Process the received HL7 message here
            System.out.println("Received HL7 message: ");
            System.out.println(hl7Message.toString());

            // Send a response (if needed)
            String responseMessage = "ACK Message"; // Modify as needed
            outputStream.write(responseMessage.getBytes());
            outputStream.flush();
            System.out.println("Sent response: " + responseMessage);

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
