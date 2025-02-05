package ca.uhn.hl7v2.examples;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.app.SimpleServer;
import ca.uhn.hl7v2.app.ThreadedServer;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v26.message.ORM_O01;
import ca.uhn.hl7v2.model.v26.message.ORM_O01_RESPONSE;
import ca.uhn.hl7v2.parser.CanonicalModelClassFactory;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;
import ca.uhn.hl7v2.parser.PipeParser;

public class Hl7com {

    public static void main(String[] args) {
        // Start the HL7 server to receive requests
        startHL7Server();

        // Send a sample HL7 request message to the LIS
        sendHL7Request();
    }

    public static void startHL7Server() {
        SimpleServer server = new ThreadedServer(12345, new MyHandler(), new PipeParser(), new CanonicalModelClassFactory());

        System.out.println("HL7 server started on port 12345...");
        server.startAndWait();
    }

    public static void sendHL7Request() {
        try {
            Connection connection = new Connection("localhost", 12345);
            connection.open();

            ORM_O01 ormMessage = createSampleORMMessage();
            
            Initiator initiator = connection.getInitiator();
            Message response = initiator.sendAndReceive(ormMessage);

            if (response instanceof ORM_O01_RESPONSE) {
                // Handle the response message here
                System.out.println("Received ORM response: " + response.encode());
            } else {
                System.err.println("Received an unexpected response.");
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ORM_O01 createSampleORMMessage() throws HL7Exception {
        ORM_O01 ormMessage = new ORM_O01();
        // Build and populate the ORM message here as needed

        return ormMessage;
    }

    static class MyHandler extends ca.uhn.hl7v2.app.ApplicationAdapter {
        @Override
        public Message processMessage(Message message) throws HL7Exception, EncodingNotSupportedException {
            // Handle incoming HL7 messages (requests) here
            System.out.println("Received HL7 message: " + message.encode());

            // Create a response message (ORM_O01_RESPONSE) and return it
            ORM_O01_RESPONSE response = new ORM_O01_RESPONSE();
            // Populate the response message

            return response;
        }
    }
}
