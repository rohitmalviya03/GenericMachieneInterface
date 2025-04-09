package Server;


import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONArray;
import org.json.JSONObject;

public class PatientMonitorClientbkp {

    // MLLP constants
    private static final byte START_BLOCK1 = 0x0B; // <VT>
    private static final byte END_BLOCK = 0x1C;   // <FS>
    private static final byte CARRIAGE_RETURN = 0x0D; // <CR>
    static Map res = ReadPropertyFile.getPropertyValues();
    static String server_ip = (String) res.get("server_ip");
    private static String  cr_no=null;
    
    
	static int server_port =  Integer.parseInt((String)res.get("server_port"));//res.get("server_port");
	private static boolean isMsgEnd;
	private static final ExecutorService executorService = Executors.newFixedThreadPool(5);
    // Logger
   // private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(PatientMonitorClient.class);

    // Method to encapsulate HL7 message using MLLP
    private static byte[] mllpEncode(String message) {
        byte[] messageBytes = message.getBytes(java.nio.charset.StandardCharsets.ISO_8859_1);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(START_BLOCK1);
        outputStream.write(messageBytes, 0, messageBytes.length);
        outputStream.write(END_BLOCK);
        outputStream.write(CARRIAGE_RETURN);
        return outputStream.toByteArray();
    }

    public static boolean isValidMLLPMessage(String message) {
        if (message == null || message.length() < 3) {
            return false; // Too short to be a valid MLLP message
        }

        // Check start and end characters
        return message.charAt(0) == START_BLOCK1 &&
               message.charAt(message.length() - 2) == END_BLOCK &&
               message.charAt(message.length() - 1) == CARRIAGE_RETURN;
    }
    // Generate current timestamp
    private static String generateTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date());
    }

    // Generate unique query ID
    private static String generateQueryId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 15);
    }

    // Parse HL7 message to JSON
    private static String parseHl7ToJson(String hl7Message) {
        try {
            // Here, you need to use an HL7 parsing library, or implement your own
            // Since this is a simplified example, I'll assume the message is valid
            JSONArray jsonData = new JSONArray();
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String packetTimestamp = generateTimestamp() + "-0500";
            String machineId = "1001";
            String patientId = cr_no;//"989262400001438";
            List<Map<String, String>> dataList = new ArrayList<>();
            String[] segments = hl7Message.split("\r");

            // Example of parsing (you'd need to parse actual HL7 message here)

            for (String segment : segments) {
            	
            	
                if (segment.startsWith("OBX")) { 
                JSONObject data = new JSONObject();
                String[] fields = segment.split("\\|");
                
                String paramField = fields.length > 3 ? fields[3] : "";
                String paramValue = fields.length > 5 ? fields[5] : "";
                String paramUnit = fields.length > 6 ? fields[6] : "";
                String paramReferenceRange = fields.length > 7 ? fields[7] : "";
                
               String str[]= paramField.split("\\^");
                if(fields[2].equals("NM")) {
                data.put("patient_id", patientId);
                data.put("param_id", str[0]);
                data.put("param_name", str[0]);
                data.put("param_value", paramValue);
                data.put("param_unit", paramUnit);
                data.put("param_referenceRange", paramReferenceRange);
                data.put("timestamp", timestamp);
                data.put("packettimestamp", packetTimestamp);
                data.put("machineId", GenericServer.machineId);

                jsonData.put(data);
                }
                }
            }
            return jsonData.toString();
        } catch (Exception e) {
        	 GenericServer.saveToFile("Stack trace"+GenericServer.getStackTraceAsString(e), GenericServer.FILE_NAME);
          //  logger.error("Error parsing HL7 message to JSON: ", e);
            return null;
        }
    }

    // Connect to monitor using socket
    private static Socket connectToMonitor(String host, int port) {
        try {
        	
            Socket socket = new Socket(host, port);
            System.out.println("Connection established with " + host + ":" + port);
            GenericServer.saveToFile("Connection established with " + host + ":" + port, GenericServer.FILE_NAME);
            
            //logger.info("Connection established with " + host + ":" + port);
            return socket;
        } catch (IOException e) {
        	e.printStackTrace();
        	 GenericServer.saveToFile("Stack trace"+GenericServer.getStackTraceAsString(e), GenericServer.FILE_NAME);
           // logger.error("Connection error: ", e);
            return null;
        }
    }

    // Send query message using MLLP
    private static void sendQueryMessage(Socket socket, String qryMessage) {
        try {
            byte[] encodedMessage = mllpEncode(qryMessage);
            socket.getOutputStream().write(encodedMessage);
            socket.getOutputStream().flush();
            
            System.out.println("Sent QRY message: " + new String(encodedMessage, java.nio.charset.StandardCharsets.ISO_8859_1));
         

            GenericServer.saveToFile("Sent QRY message: " + new String(encodedMessage, java.nio.charset.StandardCharsets.ISO_8859_1), GenericServer.FILE_NAME);
            // logger.debug("Sent QRY message: " + new String(encodedMessage, java.nio.charset.StandardCharsets.ISO_8859_1));
        } catch (IOException e) {
        	 GenericServer.saveToFile("Stack trace"+GenericServer.getStackTraceAsString(e), GenericServer.FILE_NAME);
           // logger.error("Error sending QRY message: ", e);
        }
    }

    // Send TCP echo every second
    private static void sendTcpEcho(Socket socket, AtomicBoolean running) {
        String echoMessage = "MSH|\\&|||||||ORU^R01|106|P|2.3.1|";
        try {
            while (running.get()) {
                byte[] encodedEchoMessage = mllpEncode(echoMessage);
                socket.getOutputStream().write(encodedEchoMessage);
                socket.getOutputStream().flush();
                System.out.println("Sent TCP Echo message."+encodedEchoMessage);
                

                GenericServer.saveToFile("Sent TCP Echo message. "+echoMessage, GenericServer.FILE_NAME);
               // logger.info("Sent TCP Echo message.");
                Thread.sleep(5000); // 1 second
            }
        } catch (IOException | InterruptedException e) {
       
        	 GenericServer.saveToFile("Stack trace"+GenericServer.getStackTraceAsString(e), GenericServer.FILE_NAME);
        	 GenericServer.saveToFile("Retrying to Connect", GenericServer.FILE_NAME);
        	 closeSocket(socket);

             // Reconnect
             socket = retryConnection();
           // logger.error("Error sending echo message: ", e);
        }
    }

    // Receive and process real-time data from patient monitor
    private static void receiveData(Socket socket, AtomicBoolean running) {
        try (BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream())) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            //System.out.println( socket.getInputStream());
           
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            
            while (running.get()) {
                bytesRead = inputStream.read(buffer);
                if (bytesRead == -1) {
                	//System.out.println("Connection closed by server.");

                    GenericServer.saveToFile("Connection closed by server.", GenericServer.FILE_NAME);
                  //  logger.warn("Connection closed by server.");
                    break;
                }
                byteArrayOutputStream.write(buffer, 0, bytesRead);
                byte[] data = byteArrayOutputStream.toByteArray();

                
                
                Thread threadObj = new Thread(() -> {
                // Process complete messages in the buffer
                int startIndex = -1;
                int endIndex = -1;
                
                 isMsgEnd = false;
                for (int i = 0; i < data.length; i++) {
                    if (data[i] == START_BLOCK1 && startIndex == -1) { // Store the first occurrence only
                        startIndex = i;
                    }
                    
                    if (data[i] == END_BLOCK ) {
                        endIndex = i; // Store the last occurrence of END_BLOCK
                        isMsgEnd =true;
                    }
                   

                   // System.out.println("Index: " + i + " -> " + (char) data[i]);
                }

                System.out.println(" S Index: " + startIndex + " L Index " + endIndex);
                String message = new String(data, java.nio.charset.StandardCharsets.ISO_8859_1);
                // logger.info("Received data: " + message);

                // System.out.println("Received data: " + message);
                 char fsChar = 0x1C; // FS
                 //message= message.replaceAll(Character.toString(fsChar), "");
                 
                 GenericServer.saveToFile("Received data: " + message, GenericServer.FILE_NAME);
            
                 
                boolean isMSgR= isValidMLLPMessage(message);
                 
                 char fsChar2 = '\u001C'; // ASCII 28
                 if (isMSgR) {
                    byte[] completeMessage = Arrays.copyOfRange(data, startIndex + 1, endIndex);
                   // String message1 = new String(completeMessage, java.nio.charset.StandardCharsets.ISO_8859_1);
                   // logger.info("Received data: " + message);

                   // System.out.println("Received data: " + message);
                    // Convert HL7 message to JSON
                    String jsonOutput = parseHl7ToJson(message);
                    GenericServer.saveToFile("jsonOutput :"+jsonOutput, GenericServer.FILE_NAME);
                    if (jsonOutput != null) {
                     //   logger.info("Converted to JSON: " + jsonOutput);
//System.out.println("Converted to JSON: " + jsonOutput);
                        // Send data to API
                        // Example API call:
                    	
                    	if(!cr_no.equals(null)) {
                       //  sendDataToApi(jsonOutput);
                    		 GenericServer.saveToFile("CAll save api", GenericServer.FILE_NAME);
                    		// executorService.submit(() -> 
                    		// sendDataToApi(jsonOutput);
                    	}
                    	else {
                    		 GenericServer.saveToFile("Cr no. not present", GenericServer.FILE_NAME);
                    	}
                    }
                //}
                    
                 }
                 else {
                	 GenericServer.saveToFile("FS Not found ", GenericServer.FILE_NAME);
                	 
                	 
                 }
               // else {
                	
                //	System.out.println("MSG Incomplete");
              //  }
                });

             // Start the thread
             threadObj.start();
            }
                
                
        } catch (Exception e) {
        	 GenericServer.saveToFile("Stack trace"+GenericServer.getStackTraceAsString(e), GenericServer.FILE_NAME);
        	 GenericServer.saveToFile("Retrying to Connect", GenericServer.FILE_NAME);
        	 retryConnection();
        	e.printStackTrace();
           // logger.error("Error receiving data: ", e);
        }
    }

    // Method to simulate sending data to API
    private static void sendDataToApi(String jsonOutput) {
        // Example HTTP request using HTTPClient (replace with actual HTTP client code)
    	
    	//System.out.println(jsonOutput);
    	//  GenericServer.saveToFile("jsonOutput: " + jsonOutput, GenericServer.FILE_NAME);
    	  
    	  try {
				// Define the URL of the API endpoint
				// edb://localhost:5444/aiims_jodhpur
				// https://aiimsjodhpur.prd.dcservices.in
				// URL url = new
				// URL("http://10.226.28.174:8380/OT_INTEGRATION/service/api/saveresult/");

				URL url = new URL(GenericServer.aiimsUrl + "/OT_INTEGRATION/service/api/saveresult/");

				// URL url = new
				// URL("https://aiimsjodhpur.uat.dcservices.in/OT_INTEGRATION/service/api/saveresult/");

				// Create a connection object
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();

				// Set the request method to POST
				connection.setRequestMethod("POST");

				// Set request headers if needed
				connection.setRequestProperty("Content-Type", "application/json");
				// connection.setRequestProperty("Accept", "application/json");

				// Enable input and output streams
				connection.setDoOutput(true);
				System.out.println(connection.getURL());
				// Create the JSON payload
				String jsonInputString = "{\r\n" + "    \"patient_id\": \"1212\",\r\n"
						+ "    \"param_id\": \"150356\",\r\n" + "    \"param_name\": \"MDC_TEMP_AWAY\",\r\n"
						+ "    \"param_value\": \"99.0\",\r\n" + "    \"param_unit\": \"266560^MDC_DIM_FAHR^MDC\",\r\n"
						+ "    \"param_referenceRange\": \"\",\r\n"
						+ "    \"timestamp\": \"20240813160323.0000+0530\"\r\n" + "}";

				// Write the JSON payload to the output stream
				try (OutputStream os = connection.getOutputStream()) {
					byte[] input1 = jsonOutput.getBytes("UTF-8");
					os.write(input1, 0, input1.length);
				}

				// Get the response code
				int responseCode = connection.getResponseCode();
				connection.getResponseMessage();
				GenericServer.saveToFile("URL : " + connection.getURL(), GenericServer.FILE_NAME);
				System.out.println("Response Code: " + responseCode + connection.getResponseMessage());
				GenericServer.saveToFile("Response Code: " + responseCode + connection.getResponseMessage(), GenericServer.FILE_NAME);
				// Handle the response (not shown in this example)

			} catch (Exception e) {
				
				e.printStackTrace();
				 GenericServer.saveToFile("Stack trace"+GenericServer.getStackTraceAsString(e), GenericServer.FILE_NAME);
			}
    	  
    	  
    	  
    	  
    	  
    	  
    	  
       // logger.info("Sending data to API: " + jsonOutput);
    }

    public static Socket connectMonitor(String serverIp ,String serverPort,String crno) {
        String host = serverIp;//"10.226.28.174"; // Replace with actual IP address
        int port = Integer.parseInt((String)serverPort); // Port for real-time interface
        cr_no=crno;
        System.out.println("Start .... Fetching data");
         GenericServer.saveToFile("Start .... Fetching data", GenericServer.FILE_NAME);
         GenericServer.saveToFile("Patient Cr no."+cr_no, GenericServer.FILE_NAME);
        // Generate dynamic timestamp and query ID
        String timestamp = generateTimestamp();
        String queryId = generateQueryId();

        Socket clientSocket = connectToMonitor(host, port);
        if (clientSocket == null) return clientSocket;

        // Corrected QRY message with dynamic timestamp and query ID
        String qryMessage = "MSH|^~\\&|||||||QRY^R02|1203|P|2.3.1\r"
                + "QRD|" + timestamp + "|R|I|" + queryId + "|||||RES\r"
                + "QRF|MON||||0&0^1^1^0^101&160&161&200\r"
                + "QRF|MON||||0&0^1^1^0^500&501&502\r";

        // Delay the query message by 10 seconds
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(() -> sendQueryMessage(clientSocket, qryMessage), 10, TimeUnit.SECONDS);

        // Start threads for sending echo and receiving data
        AtomicBoolean running = new AtomicBoolean(true);
        Thread echoThread = new Thread(() -> sendTcpEcho(clientSocket, running));
        Thread receiveThread = new Thread(() -> receiveData(clientSocket, running));

        echoThread.start();
        receiveThread.start();

        // Keep main thread alive
        try {
            while (true) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
        	 GenericServer.saveToFile("Stack trace"+GenericServer.getStackTraceAsString(e), GenericServer.FILE_NAME);
        	e.printStackTrace();
         //   logger.info("Shutting down...");
		} 
        finally {
            running.set(false);
            try {
                echoThread.join();
                receiveThread.join();
                clientSocket.close();
                GenericServer.saveToFile("\"Connection closed.\"", GenericServer.FILE_NAME);
            } catch (InterruptedException | IOException e) {
            	 // GenericServer.saveToFile("Error during shutdown: ", GenericServer.FILE_NAME);
            	 	 GenericServer.saveToFile("Stack trace"+GenericServer.getStackTraceAsString(e), GenericServer.FILE_NAME);
            }
        

    }
        return clientSocket;
}
    
    
    
    private static Socket retryConnection() {
        Socket newSocket = null;
        int retryCount = 0;
        int maxRetries = 5;
        int retryDelay = 3000; // Start with 3 seconds

        while (retryCount < maxRetries) {
            try {
            	  int port = Integer.parseInt((String)GenericServer.server_port); // Port for real-time interface
            	    
            //	GenericServer.server_ip,GenericServer.server_port,GenericServer.cr_no
               // newSocket = new Socket(GenericServer.server_ip, port); // Use your actual server IP and port
               // System.out.println(getTimestamp() + " - Reconnected to server: 192.168.0.103:4601");
                GenericServer.saveToFile("Reconnected to server: 192.168.0.103:4601", GenericServer.FILE_NAME);
                connectMonitor(GenericServer.server_ip, GenericServer.server_port,GenericServer.cr_no);
                return newSocket; // Return new socket if successful
            } catch (Exception e) {
                retryCount++;
               // System.err.println(getTimestamp() + " - Reconnection attempt " + retryCount + " failed. Retrying...");
                GenericServer.saveToFile("Reconnection attempt " + retryCount + " failed. Retrying...", GenericServer.FILE_NAME);
                sleep(retryDelay);
                retryDelay *= 2; // Exponential backoff
            }
        }

        // If max retries exceeded, log failure
       // System.err.println(getTimestamp() + " - Maximum reconnection attempts reached. Could not reconnect.");
        GenericServer.saveToFile("Maximum reconnection attempts reached. Could not reconnect.", GenericServer.FILE_NAME);
        return null; // Return null if connection could not be re-established
    }
    
    private static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ignored) { }
    }

    
    
    
    
    
    
    
    
    
    private static void closeSocket(Socket socket) {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("Closed old socket connection.");
                GenericServer.saveToFile("Closed old socket connection.", GenericServer.FILE_NAME);
            }
        } catch (IOException e) {
            System.err.println("Failed to close old socket: " + e.getMessage());
            GenericServer.saveToFile("Failed to close old socket: " + e.getMessage(), GenericServer.FILE_NAME);
        }
    }

}
