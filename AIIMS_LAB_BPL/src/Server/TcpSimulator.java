package Server;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TcpSimulator {
    public static void main(String[] args) {
        String serverAddress = "10.226.28.174"; // Server's IP address
        int port = 9000; // Server's listening port
        String filePath = "config/hl7data.txt"; // Path to your HL7 data file

        try (Socket socket = new Socket(serverAddress, port);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to server. Sending HL7 data...");
            System.out.println("Type 'SEND' to send the HL7 data or 'EXIT' to close the connection.");

            while (true) {
                System.out.print("Command: ");
                String command = scanner.nextLine().trim();

                if ("EXIT".equalsIgnoreCase(command)) {
                    System.out.println("Closing connection...");
                    break; // Exit the loop and close the socket
                } else if ("SEND".equalsIgnoreCase(command)) {
                    // Read HL7 data from the file
                    String hl7Data = readHL7FromFile(filePath);

                    // Send HL7 data to the server
                    writer.write(hl7Data);
                    writer.flush();
                    System.out.println("HL7 data sent successfully.");
                } else {
                    System.out.println("Unknown command. Please type 'SEND' or 'EXIT'.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Reads HL7 data from a file
    private static String readHL7FromFile(String filePath) throws IOException {
        StringBuilder hl7Data = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                hl7Data.append(line).append("\r"); // HL7 segment separator
            }
        }

        return hl7Data.toString();
    }
}
