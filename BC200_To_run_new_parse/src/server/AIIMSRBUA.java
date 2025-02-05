package server;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class AIIMSRBUA {

    private String ipAddress;
    private int port1;
    private int port2;
    private String format;
    private boolean transmitQCData;
    private String timing;

    public AIIMSRBUA(String ipAddress, int port1, int port2, String format, boolean transmitQCData, String timing) {
        this.ipAddress = ipAddress;
        this.port1 = port1;
        this.port2 = port2;
        this.format = format;
        this.transmitQCData = transmitQCData;
        this.timing = timing;
    }

    public void connect() {
        connectToPort(port1);
        connectToPort(port2);
    }

    private void connectToPort(int port) {
        try (Socket socket = new Socket(ipAddress, port);
             OutputStream outputStream = socket.getOutputStream()) {
             
            String connectionMessage = "Connection established using format: " + format;
            outputStream.write(connectionMessage.getBytes());
            System.out.println("Connected to " + ipAddress + " on port " + port);
            
            // Add logic for transmitting QC data and other information here
            if (transmitQCData) {
                String qcDataMessage = "Transmitting QC data...";
                outputStream.write(qcDataMessage.getBytes());
                System.out.println(qcDataMessage);
            }

        } catch (IOException e) {
            System.err.println("Failed to connect to " + ipAddress + " on port " + port + ": " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Configuration details from the image
        String ipAddress = "10.226.28.174";
        int port1 = 50001;
        int port2 = 50002; // Assuming both ports are the same as per the image
        String format = "ASTM1381-02/ASTM1894-97";
        boolean transmitQCData = true; // Yes (results and lot info.)
        String timing = "Always";

        AIIMSRBUA lisConfig = new AIIMSRBUA(ipAddress, port1, port2, format, transmitQCData, timing);
        lisConfig.connect();
    }
}
