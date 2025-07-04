package Server.AiimsJammu;

import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONObject;

import Server.ABC;
import Server.AIIMSLAB;
import Server.HL7MessageGenerator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class hl7Reciver {

	static final String FILE_NAME = "./machineLog.txt"; // File to save the data
	   private static final byte START_BLOCK = 0x0B; // VT
	    private static final byte END_BLOCK = 0x1C;   // FS
	    private static final byte CARRIAGE_RETURN = 0x0D; // CR


	    public static void handleVitrosClient(Socket clientSocket) {
	        try (
	            InputStream input = clientSocket.getInputStream();
	            OutputStream output = clientSocket.getOutputStream()
	        ) {
	            StringBuilder hl7Builder = new StringBuilder();
	            boolean isInMessage = false;
	            int byteRead;

	            while ((byteRead = input.read()) != -1) {
	                char ch = (char) byteRead;

	                if (ch == 0x0B) { // VT: Start of MLLP message
	                    isInMessage = true;
	                    hl7Builder.setLength(0); // Clear previous data
	                } else if (ch == 0x1C) { // FS: End of MLLP message (start)
	                    int next = input.read();
	                    if (next == 0x0D) { // CR follows FS: confirmed message end
	                        String receivedMessage = hl7Builder.toString();

	                        // ✅ LOG & PROCESS MESSAGE
	                        System.out.println("Received HL7 Message:\n" + receivedMessage);
	                        AIIMSLAB.saveToFile("Received: " + receivedMessage, AIIMSLAB.FILE_NAME);
	                        // ✅ Send ACK
	                        String ack = HL7MessageGenerator.generateAckResponseVitros(receivedMessage);
	                       // String framedAck = "\u000b" + ack + "\u001c\r";
	                        output.write(ack.getBytes(StandardCharsets.UTF_8));
	                        output.flush();
	                        System.out.println("ACK Sent:\n" + ack);
	                        AIIMSLAB.saveToFile("ACK Sent: " + ack, AIIMSLAB.FILE_NAME);

	                        // ✅ Parse segments (optional)
	                        String sampleNumber = "";
	                        JSONArray tests = new JSONArray();
	                        String[] segments = receivedMessage.split("\r");

	                        for (String segment : segments) {
	                            String[] fields = segment.split("\\|");

	                            if (fields[0].equals("OBR") && fields.length > 3) {
	                                sampleNumber = fields[2];
	                            } else if (fields[0].equals("OBX")) {
	                                String testCode = (fields.length > 3) ? fields[3] : "";
	                                String testValue = (fields.length > 5) ? fields[5] : "";

	                                JSONObject testResult = new JSONObject();
	                                testResult.put("testCode", testCode);
	                                testResult.put("testValue", testValue);
	                                tests.put(testResult);

	                                ABC.insert_GenExpert(testCode, testValue, sampleNumber);
	                                
	                            }
	                        }

	                    
	                        isInMessage = false; // Reset for next message
	                    }
	                } else if (isInMessage) {
	                    hl7Builder.append(ch);
	                }
	            }

	        } catch (Exception e) {
	            System.err.println("Error handling client: " + e.getMessage());
	            AIIMSLAB.saveToFile("Stack Trace: " + AIIMSLAB.getStackTraceAsString(e), FILE_NAME);
	            e.printStackTrace();
	        }
	    }


	

}
