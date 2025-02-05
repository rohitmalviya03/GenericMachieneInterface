package Server;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class MultiPortServer {

    // Define ports to listen on
    private static final int[] PORTS = {54321, 54322};  // Ports to listen on

    public static void main(String[] args) {
        System.out.println("Server is running...");

        // Executor to handle multiple threads (each for a different port)
        ExecutorService pool = Executors.newFixedThreadPool(PORTS.length);

        // Start a listener for each port
        for (int port : PORTS) {
            // Create a separate thread for each port
            pool.execute(new PortListener(port));
        }
    }

    // This class listens on a specific port and handles incoming client connections
    private static class PortListener implements Runnable {
        private final int port;

        public PortListener(int port) {
            this.port = port;
        }

        @Override
        public void run() {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Listening on port " + port);

                while (true) {
                    // Accept a new client connection
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New client connected on port " + port + ": " + clientSocket.getInetAddress());

                    // Handle the client in a separate thread
                    new Thread(new ClientHandler(clientSocket)).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // ClientHandler class that handles communication with a single client
    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (InputStream inputStream = clientSocket.getInputStream();
                 OutputStream outputStream = clientSocket.getOutputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                 PrintWriter writer = new PrintWriter(outputStream, true)) {

                String message;

                // Continuously read from the input stream
                while (true) {
                    // Check if data is available to read
                    if (reader.ready()) {  // Non-blocking check for available data
                        message = reader.readLine();  // Read a line of text from the client
                        if (message != null) {
                            System.out.println("Received on " + clientSocket.getPort() + ": " + message);
                            writer.println("Echo: " + message);  // Echo the message back to the client
                        }
                    }

                    // Optionally, you can add a small sleep to prevent tight loop if needed
                    try {
                        Thread.sleep(10); // Sleep to avoid excessive CPU usage when no data is available
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();  // Close client socket when done
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
