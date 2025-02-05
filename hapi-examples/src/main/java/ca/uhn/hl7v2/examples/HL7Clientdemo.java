package ca.uhn.hl7v2.examples;
import java.io.IOException;
import java.util.Map;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.CanonicalModelClassFactory;
import ca.uhn.hl7v2.parser.PipeParser;

public class HL7Clientdemo {
	static Map res = ReadPropertyFile.getPropertyValues();
	final static String orderip = (String) res.get("orderip");
	final static String resultip = (String) res.get("resultip");
    public static void main(String[] args) throws LLPException {
    	
    	
        String serverHost =resultip;
        int serverPort = 10001; // Replace with the actual port number
        
        
        System.out.println("Server Running on :"+serverHost+":"+serverPort);
        // Create an HL7 message
        //String hl7Message = "Your HL7 Message Here";
        String hl7Message="MSH|^~\\&|LIS|LIS|HALIA|HALIA|20231004002512||OML^O33^OML_O33|20231004002512|P|2.5|||||||\r" + 
          		"PID|||230909H0888^^^LIS^PI||m^rohan^^||20230910|F|||^^^^^^||||||||||||||||||||N|AL\r" + 
          		"PV1||N|||||||||||||||||||||||||||||||||||||||||||\r" + 
          		"SPM|1|1||BLOOD||||InterLink|||||||||202309100017||||||\r" + 
          		"ORC|NW|33531|1|1|||||20231004002512||||3267||||||||^^^^^^^^^3267\r" + 
          		"TQ1|||||||20231004002512||R\r" + 
          		"OBR|1|1|1|CBC^CBC profile^LIS|||||||BLOOD||||||||||||||P\r" + 
          		"ORC|NW|33531|1|1|||||20230910001837||||3267||||||||^^^^^^^^^3267\r" + 
          		"TQ1|||||||20231004002512||R\r" + 
          		"OBR|1|1|1|DIF^DIF profile^LIS|||||||BLOOD||||||||||||||P\r" + 
          		"";
        // Initialize the HAPI context and parser
        HapiContext context = new DefaultHapiContext();
        PipeParser parser = context.getPipeParser();
        
        try {
            // Parse the HL7 message string into a Message object
            Message hl7ParsedMessage = parser.parse(hl7Message);
            
            // Create a connection to the Horiba server
            Connection connection = context.newClient(serverHost, serverPort, false);
            Initiator initiator = connection.getInitiator();
            
            // Send the HL7 message
            Message response = initiator.sendAndReceive(hl7ParsedMessage);
            
            // Handle the response (if needed)
            if (response != null) {
                String responseMessage = parser.encode(response);
                System.out.println("Received Response: " + responseMessage);
            }
            
            // Close the connection
            connection.close();
        } catch (HL7Exception | IOException e) {
            e.printStackTrace();
        }
    }
}
