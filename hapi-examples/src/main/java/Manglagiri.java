import ca.uhn.hl7v2.DefaultHapiContext;
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

public class Manglagiri {

    private static String msgcontrolID;
	private static String timeZoneMachine;
	private static String modifiedTimeStr;
	private static String RackSeq;

	public static void main(String[] args) {
        try {
            // Start the server
            startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startServer() throws IOException {
        // Create a server socket to listen for incoming connections
    	
    	 String hl7MessageString = null;
    	 String[] sampleNOblock=null;
     	String sampleNOblock2=null;
     	String sampleNO=null;
     	String rackNo=null;
     	String SEC2val=null;
     	String pidmsg=null;
     	String MSHsegment=null;
        try (ServerSocket serverSocket = new ServerSocket(54000)) { // Replace with your desired port number
            System.out.println("Server started. Listening for incoming connections...");

            // Accept incoming connections from clients
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                    StringBuilder messageBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        messageBuilder.append(line).append("\r"); // Append line terminator as HL7 messages are terminated by carriage return
                    }

                    // Parse the HL7 message
                    
                    
                     hl7MessageString = messageBuilder.toString();
                    PipeParser pipeParser = new PipeParser();
                   // Message hl7Message = pipeParser.parse(hl7MessageString);

                    // Handle the HL7 message (e.g., process, log, respond)
                    System.out.println("Received HL7 message: " + hl7MessageString);
                    // Handle the HL7 message according to your application's logic
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Handle the client connection
                
               // handleClientConnection(clientSocket);
                
                
                String[] segments = hl7MessageString.split("\r");
                
                String sampleName = null;
                for (String segment : segments) {
                    System.out.println("Segment: " + segment);
                    
                	if (segment.startsWith("MSH") ||segment.contains("QBP^Q11^QBP_Q11")) {
                 		String[] parts = segment.split("\\|");
                 		
                  		MSHsegment=segments[0];
                  		//String[] msgpart=pidmsg.split("\\|");
                  		msgcontrolID=parts[9];
                  		timeZoneMachine=parts[6];
                  		
                  		 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssZ");

                         try {
                             // Parse the given time string into a Date object
                             Date givenTime = sdf.parse(timeZoneMachine);

                             // Create Calendar object and set time to the given time
                             Calendar cal = Calendar.getInstance();
                             cal.setTime(givenTime);

                             // Add 15 seconds
                             cal.add(Calendar.SECOND, 15);

                             // Format the modified time
                              modifiedTimeStr = sdf.format(cal.getTime());

                             System.out.println(modifiedTimeStr);
                         } catch (ParseException e) {
                             e.printStackTrace();
                         }
                  		System.out.println("MSG CONTROL ID"+msgcontrolID+"TimeZone"+timeZoneMachine);
                  		
                  		System.out.println("modified time zone"+modifiedTimeStr);
                    	
                  		 }
                    
                    
                 	if (segment.startsWith("QPD")) {
                 		String[] parts = segment.split("\\|");
                 		
                  		 pidmsg=segments[1];
                        //	System.out.println("Patient Information :"+pidmsg);
                        	
                        	String[] msgpart=pidmsg.split("\\|");
                        	
                        	 sampleNOblock=msgpart[3].split("\\^");
                        	 sampleNOblock2=msgpart[4];
                        	 sampleNO=sampleNOblock[0];
                        	 rackNo=msgpart[4];
                        	 SEC2val=msgpart[2];
                        	 RackSeq=msgpart[5];
                        	System.out.println("sample no :"+sampleNO +"Valueat second place::"+SEC2val +"Rack NO:"+rackNo);

                 	
                 	}
                 	}
                
                
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                String formattedTimestamp = now.format(formatter);
//                String hl7Message="MSH|^~\\&|Host||cobas pure||YYYYMMDDHHMMSSQZZzz||OML^O33^OML_O33|MSG_CONTROLID|P|2.5.1|||NE|AL||UNICODE UTF-8|||LAB-27R^ROCHE\r" + 
//              	 		"PID|||578||^^^^^^U||19901024|U\r\n" + 
//              	 		"SPM|1|SAM_NO&BARCODE||SERPLAS^^99ROC|||||||P^^HL70369||||||20240306102814||||||||||FSBT^^99ROC\r\n" + 
//              	 		"SAC|||SAM_NO^BARCODE|||||||RACKNO|1||||||||||||||||||^1^1\r\n" + 
//              	 		"ORC|NW||||||||YYYYMMDDHHMMSSQZZ\r\n" + 
//              	 		"TQ1|||||||||R^^HL70485\r\n" + 
//              	 		"OBR|1|\"\"||10032^^99ROC\r\n" + 
//              	 		"TCD|10032^^99ROC|1^^1\r\n" + 
//              	 		"ORC|NW||||||||YYYYMMDDHHMMSSQZZ\r\n" + 
//              	 		"TQ1|||||||||R^^HL70485\r\n" + 
//              	 		"OBR|1|\"\"||10088^^99ROC\r\n" + 
//              	 		"TCD|10088^^99ROC|1^^1\r\n" ;
////              	 		"ORC|NW||||||||20240306102814\r\n" + 
//              	 		"TQ1|||||||||R^^HL70485\r\n" + 
//              	 		"OBR|1|\"\"||10120^^99ROC\r\n" + 
//              	 		"TCD|10120^^99ROC|1^^1\r\n" + 
//              	 		"ORC|NW||||||||20240306102814\r\n" + 
//              	 		"TQ1|||||||||R^^HL70485\r\n" ;
                
                String hl7Message="MSH|^~\\&|host||cobas pure||YYYYMMDDHHMMSSQZZ||OML^O33^OML_O33|10001|P|2.5.1|||NE|AL|UNICODE UTF-8|||LAB-28R^ROCHE\r" + 
                		"PID|||SAM_NO||^^^^^^U||19271006|M\r" + 
                		"SPM|1|SAM_NO&BARCODE||SERPLAS^^99ROC|||||||P^^HL70369||||||YYYYMMDDHHMMSSQZZ||||||||FSBT^^99ROC\r" + 
                		"SAC|||SAM_NO^BARCODE|||||||RACKNO|RACKEQ||||||||||||||||||^1^1\r" + 
                		"ORC|NW||||||||YYYYMMDDHHMMSSQZZ\r" + 
                		"TQ1|||||||||R^^HL70485\r" + 
                		"OBR|1|\"\"||8714^^99ROC\r" + 
                		"TCD|8714^^99ROC|1^:^2\r" ; 
//                		"ORC|NW||||||||YYYYMMDDHHMMSSQZZ\r" + 
//                		"TQ1|||||||||R^^HL70485\r" ;
//                		"OBR|1|\"\"||8717^^99ROC\r" + 
//                		"TCD|8717^^99ROC|1^:^1";
             	
               // String str [] =pidmsg.split("\\|");
                
               // str[12]="S";
                //int lastIndex = pidmsg.length() - 1;
              //  pidmsg = pidmsg.substring(0, lastIndex) + "S";
              //  System.out.println("QPD"+pidmsg);
                String data="MSH|^~\\&|Host||cobas pure||YYYYMMDDHHMMSSQZZ||RSP^K11^RSP_K11|MSG_CONTROLID|P|2.5.1|||NE|AL||UNICODE UTF-8|||LAB-27R^ROCHE\r" + 
                 		"MSA|AA|MSG_CONTROLID\r" + 
                 		"QAK|SECONDVAL|OK|INISEQ^^99ROC\r" + 
                 		pidmsg+"\r"+"";
                data=data.replaceAll("MSG_CONTROLID", String.valueOf(Integer.parseInt(msgcontrolID)));
                data=data.replaceAll("SAM_NO", sampleNO);
                String str1 [] =modifiedTimeStr.split("\\+");
                String spmdate=str1[0];
                data=data.replaceAll("YYYYMMDDHHMMSSQZZ", spmdate);
                data=data.replaceAll("SECONDVAL", SEC2val);
                

                hl7Message = hl7Message.replaceAll("YYYYMMDDHHMMSSQZZzz", modifiedTimeStr);
                hl7Message = hl7Message.replaceAll("YYYYMMDDHHMMSSQZZ", spmdate);
                
                hl7Message = hl7Message.replaceAll("RACKEQ", RackSeq);
                hl7Message = hl7Message.replaceAll("SAM_NO", sampleNO);
                hl7Message = hl7Message.replaceAll("YYYYMMDDhhmmss", modifiedTimeStr); 
                hl7Message = hl7Message.replaceAll("SAM_NO", sampleNO);
                hl7Message = hl7Message.replaceAll("MSG_CONTROLID", String.valueOf(Integer.parseInt(msgcontrolID)+1));
                hl7Message = hl7Message.replaceAll("RACKNO", rackNo);
                hl7Message = hl7Message.replaceAll("ProcessingID", "1000"); 
                
                Socket clientSocket2 = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                // Send the processed data back to the client
                OutputStream outputStream2 = clientSocket2.getOutputStream();
                outputStream2.write(data.getBytes());
                //outputStream2.flush();
                outputStream2.write(hl7Message.getBytes());
                outputStream2.flush();
                System.out.println("Data sent to client RSP: " + data+"\r"+hl7Message);
                
                
                
               
                hl7Message = hl7Message.replaceAll("YYYYMMDDHHMMSSQZZzz", formattedTimestamp);
                hl7Message = hl7Message.replaceAll("YYYYMMDDHHMMSSQ", formattedTimestamp);
                hl7Message = hl7Message.replaceAll("SAM_NO", sampleNO);
                hl7Message = hl7Message.replaceAll("YYYYMMDDhhmmss", formattedTimestamp); 
                hl7Message = hl7Message.replaceAll("SAM_NO", sampleNO);
                hl7Message = hl7Message.replaceAll("MSG_CONTROLID", msgcontrolID+1);
                hl7Message = hl7Message.replaceAll("RACKNO", rackNo);
                hl7Message = hl7Message.replaceAll("ProcessingID", "1000");
                Socket clientSocket1 = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket1.getInetAddress().getHostAddress());

                // Send the processed data back to the client
//                OutputStream outputStream = clientSocket1.getOutputStream();
//                outputStream.write(hl7Message.getBytes());
//                outputStream.flush();
               System.out.println("Data sent to client: " + hl7Message);
                
                
                ///
            }
        }
    }

   }
