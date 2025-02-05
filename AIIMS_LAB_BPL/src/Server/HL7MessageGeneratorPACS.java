package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class HL7MessageGeneratorPACS {

	

    private static final char START_BLOCK = 0x0B; // <VT> (0B in HEX)
    private static final char END_BLOCK_1 = 0x1C; // <FS> (1C in HEX)
    private static final char END_BLOCK_2 = 0x0D; // <CR> (0D in HEX)
    public static String generateHL7Message(String patientID, String patientName, String radiologistID, String testID, String testName, String modality, String technicianID) {
        // Get current timestamp
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        // Generate a unique message control ID
        String messageControlID = UUID.randomUUID().toString();

        // MSH Segment
        String msh = "MSH|^~\\&|HIS_APP|HIS_FACILITY|PACS_APP|PACS_FACILITY|" + timestamp + "||ORM^O01|" + messageControlID + "|P|2.3";
        
        // PID Segment
        String pid = "PID|1||" + patientID + "||" + patientName + "||||||Phone^Email^History";
        
        // PV1 Segment
        String pv1 = "PV1|1|||||||RefPhyID^RefPhyName||||||||||OP||50";
        
        // ORC Segment
        String orc = "ORC|NW|||||||||||" + radiologistID;
        
        // OBR Segment
        String obr = "OBR|1|HOID01|CenterID|" + testID + "^" + testName + "||||||||||||||||||||" + modality + "||||||||||" + technicianID;

        // Combine segments
        String hl7Message = msh + "\r" + pid + "\r" + pv1 + "\r" + orc + "\r" + obr;

        return hl7Message;
    }

    public static void main(String[] args) {
        // Example usage
        String hl7Message = generateHL7Message(
            "12345",                          // Patient ID
            "Doe^John^M",                     // Patient Name
            "RAD123",                         // Radiologist ID
            "T123",                           // Test ID
            "Chest X-Ray",                    // Test Name
            "CR",                             // Modality
            "TECH123"                         // Technician ID
        );

        // Print HL7 message
        Socket socket = null;
        BufferedWriter writer = null;
        BufferedReader reader = null;
        try {
        	   String serverIP = "10.226.28.174"; // Example IP
               int serverPort = 50002;        // Example port

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
            System.out.println("Received ACK: " + ackResponse.toString());}
        catch (Exception e) {
        	e.printStackTrace();
			// TODO: handle exception
		}
        System.out.println(hl7Message);
    }
}
