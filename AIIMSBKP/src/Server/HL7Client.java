package Server;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class HL7Client {
    private static final Logger logger = LoggerFactory.getLogger(HL7Client.class);
    static final char START_BLOCK = 0x0B;
     static final char END_BLOCK = 0x1C;
     static final char CARRIAGE_RETURN = 0x0D;
     static final ObjectMapper objectMapper = new ObjectMapper();

    static Map<String, String> res = ReadPropertyFile.getPropertyValues();
    static String HOST = res.get("server_ip");
    static int PORT = Integer.parseInt(res.get("server_port"));

    public static void main(String[] args) {
        new HL7Client().startClient();
    }

    public void startClient() {
        try (Socket socket = new Socket(HOST, PORT)) {
            logger.info("Connected to {}:{}", HOST, PORT);
            
            // Start sender and receiver threads
            Thread senderThread = new Thread(new HL7Sender(socket.getOutputStream()));
            Thread receiverThread = new Thread(new HL7Receiver(socket.getInputStream()));

            senderThread.start();
            receiverThread.start();

            senderThread.join();
            receiverThread.join();
        } catch (IOException | InterruptedException e) {
            logger.error("Connection error: ", e);
        }
    }
}

// ------------------ HL7 Sender ------------------
class HL7Sender implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(HL7Sender.class);
    private final OutputStream outputStream;
    private static final char START_BLOCK = 0x0B;
    private static final char END_BLOCK = 0x1C;
    private static final char CARRIAGE_RETURN = 0x0D;

    public HL7Sender(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        try {
            String hl7Query = START_BLOCK + "MSH|^~\\&|CLIENTAPP|DEPT|MONITOR|HOSP|202501011200||QRY^R02|123456|P|2.3" + END_BLOCK + CARRIAGE_RETURN;
            outputStream.write(hl7Query.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            logger.info("HL7 query sent: {}", hl7Query);
        } catch (IOException e) {
            logger.error("Error sending HL7 query: ", e);
        }
    }
}

// ------------------ HL7 Receiver ------------------
class HL7Receiver implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(HL7Receiver.class);
    private final InputStream inputStream;

    public HL7Receiver(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        StringBuilder buffer = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            int ch;
            while ((ch = reader.read()) != -1) {
                buffer.append((char) ch);
                if (ch == HL7Client.CARRIAGE_RETURN) {
                    String hl7Message = buffer.toString();
                    new Thread(new HL7Processor(hl7Message)).start();
                    buffer.setLength(0);
                }
            }
        } catch (IOException e) {
            logger.error("Error reading HL7 response: ", e);
        }
    }
}

// ------------------ HL7 Processor ------------------
class HL7Processor implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(HL7Processor.class);
    private final String hl7Message;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public HL7Processor(String hl7Message) {
        this.hl7Message = hl7Message;
    }

    @Override
    public void run() {
        try {
            String cleanedMessage = hl7Message.replace(String.valueOf(HL7Client.START_BLOCK), "")
                    .replace(String.valueOf(HL7Client.END_BLOCK), "").trim();
            PipeParser parser = new PipeParser();
            Message message = parser.parse(cleanedMessage);

            Map<String, Object> jsonOutput = parseHL7ToJSON(message);
            if (!jsonOutput.isEmpty()) {
                sendToAPI(jsonOutput);
            }
        } catch (Exception e) {
            logger.error("Error processing HL7 message: ", e);
        }
    }

    private Map<String, Object> parseHL7ToJSON(Message message) {
        Map<String, Object> result = new HashMap<>();
        result.put("patient_id", "989262400001438");
        result.put("machineId", "1001");
        result.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return result;
    }

    private void sendToAPI(Map<String, Object> jsonOutput) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(jsonOutput);
            logger.info("Converted JSON: {}", jsonPayload);
            // API call logic here
        } catch (IOException e) {
            logger.error("Error converting to JSON: ", e);
        }
    }
}
