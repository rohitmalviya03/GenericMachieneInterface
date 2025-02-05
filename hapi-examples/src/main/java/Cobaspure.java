import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v24.message.ADT_A01;
import ca.uhn.hl7v2.parser.CanonicalModelClassFactory;
import ca.uhn.hl7v2.parser.PipeParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Cobaspure {

    private static final int SERVER_PORT = 54000;
    private static final ExecutorService executor = Executors.newCachedThreadPool();
    private static final PipeParser pipeParser = new PipeParser();

    public static void main(String[] args) {
        try {
            startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startServer() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server started. Listening for incoming connections...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                executor.submit(() -> handleClientConnection(clientSocket));
            }
        }
    }

    private static void handleClientConnection(Socket clientSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            StringBuilder messageBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                messageBuilder.append(line).append("\r");
            }

            String hl7MessageString = messageBuilder.toString();
            System.out.println("Received HL7 message: " + hl7MessageString);

            processHL7Message(hl7MessageString, clientSocket);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processHL7Message(String hl7MessageString, Socket clientSocket) {
        try {
            String[] segments = hl7MessageString.split("\r");
            String sampleNO = ""; // Extract sample number from the HL7 message
            String rackNo = ""; // Extract rack number from the HL7 message
            String SEC2val = ""; // Extract SEC2 value from the HL7 message
            String msgcontrolID = ""; // Extract message control ID from the HL7 message
            String responseMessage = "MSH|^~\\&|Host||cobas pure||yyyyMMddHHmmssQZZ||RSP^K11^RSP_K11|MSG_CONTROLID|P|2.5.1|||NE|AL||UNICODE UTF-8|||LAB-27R^ROCHE\r" +
                    "MSA|AA|MSG_CONTROLID\r" +
                    "QAK|SECONDVAL|OK|INISEQ^^99ROC\r" +
                    "PID|||SAM_NO||^^^^^^U||19271006|M\r" +
                    "SPM|1|SAM_NO&BARCODE||SERPLAS^^99ROC|||||||P^^HL70369||||||yyyyMMddHHmmssQZZ||||||||FSBT^^99ROC\r" +
                    "SAC|||SAM_NO^BARCODE|||||||RACKNO|RACKEQ||||||||||||||||||^1^1\r" +
                    "ORC|NW||||||||yyyyMMddHHmmssQZZ\r" +
                    "TQ1|||||||||R^^HL70485\r" +
                    "OBR|1|\"\"||8714^^99ROC\r" +
                    "TCD|8714^^99ROC|1^:^2\r";
            responseMessage = responseMessage.replaceAll("MSG_CONTROLID", msgcontrolID);
            responseMessage = responseMessage.replaceAll("SAM_NO", sampleNO);
            String[] msgParts = hl7MessageString.split("\\|");
            if (msgParts.length > 9) {
                msgcontrolID = msgParts[9];
            }
            if (msgParts.length > 12) {
                sampleNO = msgParts[12];
            }
            if (msgParts.length > 14) {
                rackNo = msgParts[14];
            }
            if (msgParts.length > 2) {
                SEC2val = msgParts[2];
            }
            String modifiedTimeStr = ""; // Calculate modified time
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssZ");
            if (msgParts.length > 6) {
                String timeZoneMachine = msgParts[6];
                Date givenTime = sdf.parse(timeZoneMachine);
                Calendar cal = Calendar.getInstance();
                cal.setTime(givenTime);
                cal.add(Calendar.SECOND, 15);
                modifiedTimeStr = sdf.format(cal.getTime());
            }
            responseMessage = responseMessage.replaceAll("yyyyMMddHHmmssQZZ", modifiedTimeStr);
            responseMessage = responseMessage.replaceAll("SECONDVAL", SEC2val);
            responseMessage = responseMessage.replaceAll("RACKNO", rackNo);
            sendResponse(responseMessage, clientSocket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendResponse(String responseMessage, Socket clientSocket) {
        try (OutputStream outputStream = clientSocket.getOutputStream()) {
            outputStream.write(responseMessage.getBytes());
            outputStream.flush();
            System.out.println("Response sent to client: " + responseMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
