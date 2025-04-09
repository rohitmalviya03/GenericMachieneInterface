package Server;


import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONArray;
import org.json.JSONObject;

public class PatientMonitorClient {

    // MLLP constants
    private static final byte START_BLOCK1 = 0x0B; // <VT>
    private static final byte END_BLOCK = 0x1C;   // <FS>
    private static final byte CARRIAGE_RETURN = 0x0D; // <CR>
    static Map res = ReadPropertyFile.getPropertyValues();
    static String server_ip = (String) res.get("server_ip");
    private static String  cr_no=null;
    
    private static final List<JSONObject> buffer = Collections.synchronizedList(new ArrayList<>());
    private static final int SEND_INTERVAL = 1; // in minutes

	static int server_port =  Integer.parseInt((String)res.get("server_port"));//res.get("server_port");
	private static boolean isMsgEnd;
	private static final ExecutorService executorService = Executors.newFixedThreadPool(5);
    // Logger
   // private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(PatientMonitorClient.class);
	
	
	/*
	 * public static void main(String[] args) { // Start the scheduled task to send
	 * data every 2 minutes System.out.println("Call Pat Monitor");
	 * ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	 * scheduler.scheduleAtFixedRate(PatientMonitorClient::sendBufferedData,
	 * SEND_INTERVAL, SEND_INTERVAL, TimeUnit.MINUTES);
	 * 
	 * // Your HL7 message processing logic here... }
	 */
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
               message.charAt(message.length() - 2) ==  CARRIAGE_RETURN &&
               message.charAt(message.length() - 1) == END_BLOCK;
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
    public static void processHL7Message(String hl7Message) {
        try {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String packetTimestamp = generateTimestamp() + "-0500";
            List<JSONObject> jsonData = new ArrayList<>();
            String[] segments = hl7Message.split("\r");

            for (String segment : segments) {
            	segment=segment.replaceAll("\\n", "");
                if (segment.startsWith("OBX")) {
                    String[] fields = segment.split("\\|");

                    if (fields.length > 5 && "NM".equals(fields[2])) { // Numeric type
                        JSONObject data = new JSONObject();
                        String[] str = fields[3].split("\\^");

                        data.put("patient_id", cr_no);
                        data.put("param_id", str[0]);
                        data.put("param_name", str[0]);
                        data.put("param_value", fields[5]);
                        data.put("param_unit", fields.length > 6 ? fields[6] : "");
                        data.put("param_referenceRange", fields.length > 7 ? fields[7] : "");
                        data.put("timestamp", timestamp);
                        data.put("packettimestamp", packetTimestamp);
                        data.put("machineId", "1001");

                        synchronized (buffer) {
                            buffer.add(data); // Store JSON in buffer
                        }
                    }
                }
            }
        } catch (Exception e) {
            GenericServer.saveToFile("Stack trace" + GenericServer.getStackTraceAsString(e), GenericServer.FILE_NAME);
        }
    }

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
           //  socket = retryConnection();
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

                
                
               // Thread threadObj = new Thread(() -> {
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
            
                 
                boolean isMSgR= true;//isValidMLLPMessage(message);
                 
                 char fsChar2 = '\u001C'; // ASCII 28
                 if (isMSgR) {
                	 byteArrayOutputStream.reset();  // Clears existing data, making it empty

                   // byte[] completeMessage = Arrays.copyOfRange(data, startIndex + 1, endIndex);
                   // String message1 = new String(completeMessage, java.nio.charset.StandardCharsets.ISO_8859_1);
                   // logger.info("Received data: " + message);
                	 // data=null;
                   // System.out.println("Received data: " + message);
                    // Convert HL7 message to JSON
                    processHL7Message(message);
                  //  message = new String("");
              
                //}
                    
                 }
                 else {
                	 GenericServer.saveToFile("FS Not found ", GenericServer.FILE_NAME);
                	 
                	 
                 }
               // else {
                	
                //	System.out.println("MSG Incomplete");
              //  }
              //  });

             // Start the thread
           //  threadObj.start();
            }
                
                
        } catch (Exception e) {
        	 GenericServer.saveToFile("Stack trace"+GenericServer.getStackTraceAsString(e), GenericServer.FILE_NAME);
        	 GenericServer.saveToFile("Retrying to Connect", GenericServer.FILE_NAME);
        	// retryConnection();
        	e.printStackTrace();
           // logger.error("Error receiving data: ", e);
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


    public static Socket connectMonitor(String serverIp ,String serverPort,String crno) {
       
    	
    	  ScheduledExecutorService scheduler1 = Executors.newScheduledThreadPool(1);
          scheduler1.scheduleAtFixedRate(PatientMonitorClient::sendBufferedData, SEND_INTERVAL, SEND_INTERVAL, TimeUnit.MINUTES);

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
