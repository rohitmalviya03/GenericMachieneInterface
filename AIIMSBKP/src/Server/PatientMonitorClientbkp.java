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
    private static final byte START_BLOCK = 0x0B; // <VT>
    private static final byte END_BLOCK = 0x1C;   // <FS>
    private static final byte CARRIAGE_RETURN = 0x0D; // <CR>
    static Map res = ReadPropertyFile.getPropertyValues();
    static String server_ip = (String) res.get("server_ip");
	static String server_port = (String) res.get("server_port");

    // Logger
    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(PatientMonitorClient.class);

    // Method to encapsulate HL7 message using MLLP
    private static byte[] mllpEncode(String message) {
        byte[] messageBytes = message.getBytes(java.nio.charset.StandardCharsets.ISO_8859_1);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
         outputStream.write(messageBytes, 0, messageBytes.length);
        outputStream.write(END_BLOCK);
        outputStream.write(CARRIAGE_RETURN);
        return outputStream.toByteArray();
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
            String patientId = "989262400001438";

            // Example of parsing (you'd need to parse actual HL7 message here)
            if (hl7Message.contains("OBX")) {
                JSONObject data = new JSONObject();
                data.put("patient_id", patientId);
                data.put("param_id", "12345");
                data.put("param_name", "SpO2");
                data.put("param_value", "98");
                data.put("param_unit", "%");
                data.put("param_referenceRange", "95-100");
                data.put("timestamp", timestamp);
                data.put("packettimestamp", packetTimestamp);
                data.put("machineId", machineId);

                jsonData.put(data);
            }
            return jsonData.toString();
        } catch (Exception e) {
            logger.error("Error parsing HL7 message to JSON: ", e);
            return null;
        }
    }

    // Connect to monitor using socket
    private static Socket connectToMonitor(String host, int port) {
        try {
        	
            Socket socket = new Socket(host, port);
            logger.info("Connection established with " + host + ":" + port);
            return socket;
        } catch (IOException e) {
            logger.error("Connection error: ", e);
            return null;
        }
    }

    // Send query message using MLLP
    private static void sendQueryMessage(Socket socket, String qryMessage) {
        try {
            byte[] encodedMessage = mllpEncode(qryMessage);
            socket.getOutputStream().write(encodedMessage);
            socket.getOutputStream().flush();
            logger.debug("Sent QRY message: " + new String(encodedMessage, java.nio.charset.StandardCharsets.ISO_8859_1));
        } catch (IOException e) {
            logger.error("Error sending QRY message: ", e);
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
                logger.info("Sent TCP Echo message.");
                Thread.sleep(1000); // 1 second
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Error sending echo message: ", e);
        }
    }

    // Receive and process real-time data from patient monitor
    private static void receiveData(Socket socket, AtomicBoolean running) {
        try (BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream())) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while (running.get()) {
                bytesRead = inputStream.read(buffer);
                if (bytesRead == -1) {
                    logger.warn("Connection closed by server.");
                    break;
                }
                byteArrayOutputStream.write(buffer, 0, bytesRead);
                byte[] data = byteArrayOutputStream.toByteArray();

                // Process complete messages in the buffer
                int startIndex = -1;
                int endIndex = -1;
                for (int i = 0; i < data.length; i++) {
                    if (data[i] == START_BLOCK) startIndex = i;
                    if (data[i] == END_BLOCK && i + 1 < data.length && data[i + 1] == CARRIAGE_RETURN) endIndex = i;
                }

                if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
                    byte[] completeMessage = Arrays.copyOfRange(data, startIndex + 1, endIndex);
                    String message = new String(completeMessage, java.nio.charset.StandardCharsets.ISO_8859_1);
                    logger.info("Received data: " + message);

                    // Convert HL7 message to JSON
                    String jsonOutput = parseHl7ToJson(message);
                    if (jsonOutput != null) {
                        logger.info("Converted to JSON: " + jsonOutput);

                        // Send data to API
                        // Example API call:
                        // sendDataToApi(jsonOutput);
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Error receiving data: ", e);
        }
    }

    // Method to simulate sending data to API
    private static void sendDataToApi(String jsonOutput) {
        // Example HTTP request using HTTPClient (replace with actual HTTP client code)
        logger.info("Sending data to API: " + jsonOutput);
    }

    public static void main(String[] args) {
        String host = server_ip;//"10.226.28.174"; // Replace with actual IP address
        int port = Integer.parseInt(server_port); // Port for real-time interface

        // Generate dynamic timestamp and query ID
        String timestamp = generateTimestamp();
        String queryId = generateQueryId();

        Socket clientSocket = connectToMonitor(host, port);
        if (clientSocket == null) return;

        // Corrected QRY message with dynamic timestamp and query ID
        String qryMessage = "MSH|^~\\&|||||||QRY^R02|1203|P|2.3.1\r"
                + "QRD|" + timestamp + "|R|I|" + queryId + "|||||RES\r"
                + "QRF|MON||||0&0^1^1^0^101&160&161&200\r";

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
            logger.info("Shutting down...");
        } finally {
            running.set(false);
            try {
                echoThread.join();
                receiveThread.join();
                clientSocket.close();
                logger.info("Connection closed.");
            } catch (InterruptedException | IOException e) {
                logger.error("Error during shutdown: ", e);
            }
        }
    }
}

