package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class TnCobas {

    static Map<String, String> res = ReadPropertyFile.getPropertyValues(); // Adjusted for generic use
    public static void main(String[] args) {
        int port = Integer.parseInt(res.get("server_port"));
        System.out.println("ASTM Server started on port " + port);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                System.out.println("Waiting for connection from analyzer...");
                Socket clientSocket = serverSocket.accept(); // Accept connection
                System.out.println("Connected to analyzer: " + clientSocket.getInetAddress());

                // Create a new thread to handle the client connection
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler extends Thread {
    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            StringBuilder messageBuffer = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                messageBuffer.append(line).append("\n");

                // Optional: End of message can be detected here (e.g., using ETX or L terminator)
                if (line.startsWith("L|")) {
                    break;
                }
            }

            System.out.println("Raw ASTM Message Received:\n" + messageBuffer);

            // Parse the ASTM message
            AstmParser.parse(messageBuffer.toString());
        } catch (IOException e) {
            System.out.println("Error handling client connection: " + e.getMessage());
        } finally {
            try {
                clientSocket.close(); // Close the client socket when done
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Connection closed.");
        }
    }
}

class AstmParser {
    public static void parse(String astmData) {
        String[] lines = astmData.split("\n");
        String sampleNumber = null;

        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("P|")) {
                // Extract sample number from P| line
                String[] fields = line.split("\\|");
                if (fields.length > 3) {
                    sampleNumber = fields[3]; // Extract sample number
                }
            } else if (line.startsWith("R|")) {
                // Extract test code and value from R| line
                String[] fields = line.split("\\|");
                if (fields.length > 4) {
                    String[] testCodeParts = fields[2].split("\\^");

                    if (Integer.parseInt(fields[1]) < 40) {
                        String testCode = testCodeParts.length > 3 ? testCodeParts[3] : "Unknown";
                        String testValue = fields[3];

                        System.out.println("Test Code: " + testCode + ", Value: " + testValue);

                        // Insert data into the database
                        ABCbkp.insert_SysmexXN350A(testCode, testValue, sampleNumber);
                    }
                }
            }
        }
    }
}
