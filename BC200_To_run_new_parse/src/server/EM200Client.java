package server;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

public class EM200Client {

    public static void main(String[] args) {
        String serverHost = "10.226.28.174";
        int serverPort = 5000;

        // File path containing multiple ASTM messages (payload only, no framing)
        String messageFile = "astmmsg.txt"; // Change as needed

        try (Socket socket = new Socket(serverHost, serverPort);
             OutputStream out = socket.getOutputStream();
             InputStream in = socket.getInputStream()) {

            // Step 1: ENQ handshake
            out.write(ASTMConstants.ENQ);
            out.flush();
            System.out.println("Sent ENQ");

            int response = in.read();
            if (response != ASTMConstants.ACK) {
                System.err.println("Did not receive ACK after ENQ. Got: " + response);
                return;
            }
            System.out.println("Received ACK");

            // Step 2: Read messages from file or fallback to default messages
            List<String> messages = readMessagesFromFile(messageFile);
            if (messages.isEmpty()) {
                System.out.println("No messages found in file. Sending default test messages.");
                messages = new ArrayList<>(Arrays.asList(
                    defaultPayload("10591351-R", "HARVINDER^KAUR", "GLU", "SERUM"),
                    defaultPayload("10591363-R", "GANGA^RAM", "GLU", "SERUM"),
                    defaultPayload("10591324-R", "RAVI", "GLU", "SERUM")
                ));
            }

            // Step 3: Send each message framed, wait for ACK after each
            for (String payload : messages) {
                byte[] framedMessage = createFramedASTMMessage(payload);
                out.write(framedMessage);
                out.flush();
                System.out.println("Sent ASTM message:\n" + payload);

                response = in.read();
                if (response != ASTMConstants.ACK) {
                    System.err.println("Did not receive ACK for message. Got: " + response);
                    break;
                }
                System.out.println("Received ACK for message");
            }

            // Step 4: Send EOT to finish session
            out.write(ASTMConstants.EOT);
            out.flush();
            System.out.println("Sent EOT. Session complete.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> readMessagesFromFile(String filePath) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)), "UTF-8");
            // Split messages by double newline (empty line)
            String[] rawMessages = content.split("\\r?\\n\\r?\\n");
            List<String> messages = new ArrayList<>();
            for (String msg : rawMessages) {
                if (!msg.trim().isEmpty()) {
                    messages.add(msg.trim());
                }
            }
            return messages;
        } catch (IOException e) {
            System.err.println("Could not read messages from file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private static String defaultPayload(String sampleId, String patientName, String testCode, String sampleType) {
        return
            "1H|^&||****||||||TBM||P|1394-97|20250520111952\r" +
            "P|1|" + sampleId + "|" + sampleId + "||" + patientName + "|||U||||||||||||||||||||||||||\r" +
            "O|1|" + sampleId + "|" + sampleId + "|^^^" + testCode + "|||||||N||||" + sampleType + "||||||||||O|||||\r" +
            "L|1|N\r";
    }

    private static byte[] createFramedASTMMessage(String payload) {
        int checksum = 0;
        byte[] payloadBytes = payload.getBytes();
        for (byte b : payloadBytes) {
            checksum += b & 0xFF;
        }
        checksum += ASTMConstants.ETX;
        checksum &= 0xFF;

        String checksumHex = String.format("%02X", checksum);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(ASTMConstants.STX);
        try {
            out.write(payloadBytes);
            out.write(ASTMConstants.ETX);
            out.write(checksumHex.getBytes());
            out.write(ASTMConstants.CR);
            out.write(ASTMConstants.LF);
        } catch (IOException e) {
            throw new RuntimeException("Error framing ASTM message", e);
        }

        return out.toByteArray();
    }
}

class ASTMConstants {
    public static final byte STX = 0x02;
    public static final byte ETX = 0x03;
    public static final byte EOT = 0x04;
    public static final byte ENQ = 0x05;
    public static final byte ACK = 0x06;
    public static final byte NAK = 0x15;
    public static final byte CR  = 0x0D;
    public static final byte LF  = 0x0A;
}
