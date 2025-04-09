package Server;


import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

//import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TcpServerWATOServer {
	   private static final List<JSONObject> buffer = Collections.synchronizedList(new ArrayList<>());
	    private static final int SEND_INTERVAL = 2; // in minutes

    private static int PORT ;
    //private static final String API_URL = "https://aiimsjodhpur.prd.dcservices.in/OT_INTEGRATION/service/api/saveresult/";
   //t private static final ObjectMapper objectMapper = new ObjectMapper();
    private static String  cr_no =null;
    public static void connectWorkStation(String wsport,String crno) {
    	
    	  ScheduledExecutorService scheduler1 = Executors.newScheduledThreadPool(1);
          scheduler1.scheduleAtFixedRate(TcpServerWATOServer::sendBufferedData, SEND_INTERVAL, SEND_INTERVAL, TimeUnit.MINUTES);

    	cr_no=crno;
    	PORT = Integer.parseInt((String)wsport);
    	 GenericServer.saveToFile("Patient Cr no."+cr_no, GenericServer.FILE_NAME);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);
            GenericServer.saveToFile("Start .... Fetching data", GenericServer.FILE_NAME);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                GenericServer.saveToFile("Start .... Fetching data", GenericServer.FILE_NAME);
                // Handle client in a separate thread
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
        	GenericServer.saveToFile("Stack Trace: " + GenericServer.getStackTraceAsString(e), GenericServer.FILE_NAME);
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
    	
    	try (
    		    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    		    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
    		) {
    		    StringBuilder messageBuffer = new StringBuilder();
    		    char[] buffer = new char[1024]; // Buffer size for reading
    		    int bytesRead;

    		    // Read the message until end of stream or custom delimiter
    		    while ((bytesRead = in.read(buffer)) != -1) {
    		        messageBuffer.append(buffer, 0, bytesRead);

    		        // Check for the end of the HL7 message (e.g., '\r')
    		        if (messageBuffer.toString().endsWith("\r")) {
    		            break; // End of message detected
    		        }
    		    }

    		    String rawMessage = messageBuffer.toString().trim();
    		    System.out.println("Raw Message Received:\n" + rawMessage);
    		    GenericServer.saveToFile("Raw Message Received:\n" + rawMessage, GenericServer.FILE_NAME);
    		    if (rawMessage.isEmpty()) {
    		        System.out.println("Error: Empty message received.");
    		        GenericServer.saveToFile("Start .... Fetching data", GenericServer.FILE_NAME);
    		        return;
    		    }

    		    // Proceed with processing the message
    		    try {
    		        String jsonOutput = parseMessageToJson(rawMessage);
    		        if (jsonOutput == null) {
    		            System.err.println("Error: Failed to parse message to JSON.");
    		            return;
    		        }
    		        //System.out.println("Converted to JSON:\n" + jsonOutput);
    		        //GenericServer.saveToFile("Start .... Fetching data", GenericServer.FILE_NAME);
    		        
    		        
    		      //  if(!cr_no.equals(null)) {
    		        
    		        
					/*
					 * int responseCode = sendPostRequest(GenericServer.aiimsUrl , jsonOutput); if
					 * (responseCode != 200) {
					 * System.err.println("Error: Failed to send data to API. Response Code: " +
					 * responseCode); return; }
					 * 
					 * System.out.println("Data successfully sent to API. Response Code: " +
					 * responseCode); GenericServer.saveToFile("Start .... Fetching data",
					 * GenericServer.FILE_NAME); String ackMessage = generateAckMessage();
					 * out.println(ackMessage); out.flush(); System.out.println("ACK sent: " +
					 * ackMessage); GenericServer.saveToFile("Start .... Fetching data",
					 * GenericServer.FILE_NAME);
					 */
//    		    }
//    		        else {
//    		    	 GenericServer.saveToFile("Cr no not present", GenericServer.FILE_NAME);
//    		    	
//    		    }
    		        } catch (Exception e) {
    		        System.err.println("Error occurred while processing message: " + e.getMessage());
    		    	GenericServer.saveToFile("Stack Trace: " + GenericServer.getStackTraceAsString(e), GenericServer.FILE_NAME);
    		        
    		        e.printStackTrace();
    		    }
    		    
    		    
    		} catch (IOException e) {
    		    System.err.println("I/O Error while communicating with the client: " + e.getMessage());
    			GenericServer.saveToFile("Stack Trace: " + GenericServer.getStackTraceAsString(e), GenericServer.FILE_NAME);
    		     
    		    e.printStackTrace();
    		} finally {
    		    try {
    		        if (clientSocket != null && !clientSocket.isClosed()) {
    		            clientSocket.close();
    		            System.out.println("Client socket closed.");
    		        }
    		    } catch (IOException e) {
    		    	GenericServer.saveToFile("Stack Trace: " + GenericServer.getStackTraceAsString(e), GenericServer.FILE_NAME);
    		        
    		        System.err.println("Error closing client socket: " + e.getMessage());
    		    }
    		}
    }

    private static String parseMessageToJson(String rawMessage) {
        try {
            List<Map<String, String>> dataList = new ArrayList<>();
            String[] segments = rawMessage.split("\r");

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String packetTimestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "-0500";
            String machineId = "1002";
            String patientId = cr_no;

            for (String segment : segments) {
            	segment=segment.replaceAll("\\n", "");
            	   String[] fields22 = segment.split("\\|");
            	if (segment.startsWith("OBX") && fields22[2].equals("CWE") && fields22[5].contains("MDC_EVT_STAT_STANDBY")) {
            		
            		GenericServer.saveToFile("Machine Standby Got  Status :" + fields22[5], GenericServer.FILE_NAME);
      		      
            		break;
            		
            	}
            	
            	else if (segment.startsWith("OBX")) { 
                	
                	// Process only OBX segments
                    String[] fields = segment.split("\\|");
                    if(fields[2].equals("NM")) {
                    String paramField = fields.length > 3 ? fields[3] : "";
                    String paramValue = fields.length > 5 ? fields[5] : "";
                    String paramUnit = fields.length > 6 ? fields[6] : "";
                    String paramReferenceRange = fields.length > 7 ? fields[7] : "";

                   // Map<String, String> data = new HashMap<>();
                    JSONObject data = new JSONObject();
                    data.put("patient_id", patientId);
                    data.put("param_id", paramField.split("\\^")[0]);
                    data.put("param_name", paramField.split("\\^").length > 1 ? paramField.split("\\^")[1] : "");
                    data.put("param_value", paramValue);
                    data.put("param_unit", paramUnit);
                    data.put("param_referenceRange", paramReferenceRange);
                    data.put("timestamp", timestamp);
                    data.put("packettimestamp", packetTimestamp);
                    data.put("machineId", GenericServer.machineId);

                    synchronized (buffer) {
                        buffer.add(data); // Store JSON in buffer
                    }
                    }
                }
            }
            
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(dataList);
           // return "";//objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dataList);
        } catch (Exception e) {
        	GenericServer.saveToFile("Stack Trace: " + GenericServer.getStackTraceAsString(e), GenericServer.FILE_NAME);
            
            e.printStackTrace();
            return null;
        }
    }

    //
    
    

    private static void sendBufferedData() {
        synchronized (buffer) {
        	System.out.println("Call send buffer data");
        	  GenericServer.saveToFile("Call send buffer data", GenericServer.FILE_NAME);
         	   
            if (!buffer.isEmpty()) {
                JSONArray finalJsonArray = new JSONArray(buffer);
          	  GenericServer.saveToFile("Final Json"+finalJsonArray, GenericServer.FILE_NAME);
              
                sendDataToApi(finalJsonArray); // Send buffered JSON data
                buffer.clear(); // Clear buffer after sending
            }
        }
    }

    
    
    // Method to simulate sending data to API
    private static void sendDataToApi(JSONArray jsonArray) {
        try {
            // Define the API endpoint URL
            URL url = new URL(GenericServer.aiimsUrl + "/OT_INTEGRATION/service/api/saveresult/");

            // Create an HTTP connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            GenericServer.saveToFile("Call HIS API : " +GenericServer.aiimsUrl, GenericServer.FILE_NAME);

            // Set HTTP method to POST
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Convert JSON Array to String
            String jsonOutput = jsonArray.toString();

            // Write JSON payload to the request body
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonOutput.getBytes("UTF-8");
                os.write(input, 0, input.length);
            }

            // Get response from the API
            int responseCode = connection.getResponseCode();
            String responseMessage = connection.getResponseMessage();
            
            // Logging the response
            System.out.println("Response Code: " + responseCode + " " + responseMessage);
            GenericServer.saveToFile("Response Code: " + responseCode + " " + responseMessage, GenericServer.FILE_NAME);

            // Close connection
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
            GenericServer.saveToFile("Stack trace: " + GenericServer.getStackTraceAsString(e), GenericServer.FILE_NAME);
        }
        
    }

    private static String generateAckMessage() {
        return "MSH|^~\\&|WATO|HL7Server|||||ACK|1|P|2.3.1\rMSA|AA|1\r";
    }
}
