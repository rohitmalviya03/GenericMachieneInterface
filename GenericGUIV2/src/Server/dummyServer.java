package Server;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class dummyServer {
    private static final int PORT =50002;  // Port number for the server
    private static final String FILE_PATH = "c:/TcpFiles/property/testdata.txt";  // Path to the text file

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Waiting for connection...");

            // Accept incoming client connection
            while(true) {
            try (Socket clientSocket = serverSocket.accept()) {
                System.out.println("Client connected.");

                // Set up input stream to read data from the file
                
                try (BufferedReader fileReader = new BufferedReader(new FileReader(FILE_PATH));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    // Read the file line by line and send it to the client
                    String line;
                    while ((line = fileReader.readLine()) != null) {
                        out.println(line);
                    }

                    System.out.println("File data sent to the client.");
                }
            } catch (IOException e) {
                System.err.println("Client handling error: " + e.getMessage());
            }
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}
