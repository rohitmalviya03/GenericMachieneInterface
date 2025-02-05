package Server;

import java.io.*;
import java.net.Socket;

public class HL7MLLPClient {

    private static final char START_BLOCK = 0x0B; // <VT> (0B in HEX)
    private static final char END_BLOCK_1 = 0x1C; // <FS> (1C in HEX)
    private static final char END_BLOCK_2 = 0x0D; // <CR> (0D in HEX)

    /**
     * Connects to PACS server and sends HL7 message using MLLP.
     * 
     * @param hl7Message the HL7 message to send
     * @param serverIP the IP address of the PACS server
     * @param serverPort the port number to connect to
     * @throws IOException if an I/O error occurs
     */
    public static void sendHL7Message(String hl7Message, String serverIP, int serverPort) throws IOException {
        Socket socket = null;
        BufferedWriter writer = null;
        BufferedReader reader = null;

        try {
            // Connect to PACS server
            socket = new Socket(serverIP, serverPort);
            System.out.println("Connected to PACS server at " + serverIP + ":" + serverPort);

            // Prepare to write to the socket (send HL7 message)
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

            // Wrap HL7 message with MLLP framing
            String framedMessage = START_BLOCK + hl7Message + END_BLOCK_1 + END_BLOCK_2;

            // Send HL7 message
            writer.write(framedMessage);
            writer.flush();
            System.out.println("HL7 message sent.");

            // Wait for acknowledgment from PACS
            StringBuilder ackResponse = new StringBuilder();
            String responseLine;
            while ((responseLine = reader.readLine()) != null) {
                ackResponse.append(responseLine);
            }
            System.out.println("Received ACK: " + ackResponse.toString());

        } finally {
            // Close resources
            if (writer != null) {
                writer.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (socket != null) {
                socket.close();
            }
            System.out.println("Connection closed.");
        }
    }

    public static void main(String[] args) {
        try {
            // Example HL7 message (Replace with dynamically generated message)
            String hl7Message = "MSH|^~\\&|HIS_APP|HIS_FACILITY|PACS_APP|PACS_FACILITY|20241014131415||ORM^O01|MsgCtrlId_ORM|P|2.3\r"
            		+ "PID|1||PatientID||PatientLast^First^Middle^^Title||20000514|F|||||Phone^Email^History\r"
            		+ "PV1|1|||||||RefPhyID^RefPhyName||||||||||OP||50\r"
            		+ "ORC|NW|||||||||||RadiologistID\r"
            		+ "OBR|1|HOID01|CenterID|TestID^TestName||||||||||||||||||||CR||||||||||TechnicianID\r"
            		+ "";

            // PACS server IP and port (replace with actual server details)
            String pacsServerIP = "10.226.28.174"; // Example IP
            int pacsServerPort = 50002;        // Example port

            // Send the HL7 message to PACS server
            sendHL7Message(hl7Message, pacsServerIP, pacsServerPort);

        } catch (IOException e) {
            System.err.println("Error sending HL7 message: " + e.getMessage());
        }
    }
}
