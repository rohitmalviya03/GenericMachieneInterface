package ca.uhn.hl7v2.examples;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.HL7Service;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.protocol.ReceivingApplication;

public class HL7Client1 {

    // Define the list for sample numbers
    private static List<String> sampleNo = new ArrayList<>();
	
	static Map res = ReadPropertyFile.getPropertyValues();
	final static String orderip = (String) res.get("orderip");
	final static String resultip = (String) res.get("resultip");

	final static String orderport = (String) res.get("orderport");
    private static String instrumentHost = "10.226.28.174";//orderip;
    private static int instrumentPort = 10001; // Replace with the actual port number
    private static String lastSampleNo =null;
    private static  StringBuffer orderPacket = new StringBuffer();
    public static void main(String[] args) {
    	
    	
    	//System.out.println("Server Running on :"+instrumentHost);
       
        // Create and start a dedicated thread for updating the sampleNo list and sending data
        Thread sampleNoUpdaterThread = new Thread(() -> {
            while (true) {
                updateSampleNoList();
                try {
					sendDataToInstrument();
				} catch (LLPException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                try {
                    // Sleep for 30 minutes
                    Thread.sleep(1 * 60 * 1000); // 30 minutes in milliseconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        sampleNoUpdaterThread.start();

        // ... Rest of your code ...
    }

    // Method to update the sampleNo list
    private static void updateSampleNoList() {
        // Clear the existing list and add new sample numbers
        
    	ABC abc = new ABC();
    	String res=abc.getSampleDtlB("12345");
    	System.out.println("Res"+res);
    	
    	String [] sampleNOlist =res.split(";");
    	sampleNo.clear();
    	
    	for(String str:sampleNOlist) {
    		sampleNo.add(str);
    	}
//        sampleNo.add("NewSampleNo1");
//        sampleNo.add("NewSampleNo2");
//        sampleNo.add("NewSampleNo3");
    }

    // Method to send data to the instrument
    private static void sendDataToInstrument() throws LLPException {
        try {
//            Socket socket = new Socket(instrumentHost, 10001);
//            OutputStream outputStream = socket.getOutputStream();
//            
        	System.out.println("RESULT ip:"+resultip);
            String serverHost ="10.226.28.174";// resultip;//"10.226.52.149";
            int serverPort = 10001; // Replace with the actual port number
            
            
            //System.out.println("Server Running on :"+instrumentHost+":"+serverPort);
            System.out.println("LIS Connected with :"+instrumentHost+":"+serverPort);
            
            
            
            for (String sample : sampleNo) {
                // Create and send the request packet for each sample
//            	if(!lastSampleNo.equals("")&&lastSampleNo.equals(null)) {
//                	
//                	System.out.println("Last Sample uploaded is :" +lastSampleNo);
//                if(sample.equals(lastSampleNo)) {
//                
//                	
//                	
//                	
//                }}	
                String requestPacket = createRequestPacket(sample);
                //sampleNo.remove(sample);
                System.out.println("Order Packet :"+requestPacket);
                System.out.println("\n");
                
                HapiContext context = new DefaultHapiContext();
                PipeParser parser = context.getPipeParser();
                
                try {
                    // Parse the HL7 message string into a Message object
                    Message hl7ParsedMessage = parser.parse(requestPacket);
                    
                    // Create a connection to the Horiba server
                    Connection connection = context.newClient(instrumentHost, serverPort, false);
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
               
                //
                
                
               // outputStream.write(requestPacket.getBytes());
                //outputStream.flush();
                orderPacket.delete(0, orderPacket.length());
                //sampleNo.remove(0);
                // Sleep for a short duration (adjust as needed) before sending the next request
                lastSampleNo=sample;
                
                //write all sample no. which is uploaded in worklist
                String folderPath = "worklistdata/";

                // Create the folder if it doesn't exist
                File folder = new File(folderPath);
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                String currentDate = dateFormat.format(new Date());

                // Define the file name with the current date
                String fileName = folderPath +"data_" + currentDate + ".txt";

              
                try {
                    FileWriter fileWriter = new FileWriter(fileName, true); // true for append mode
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                    // Append data to the file
                    bufferedWriter.write(sample);
                    bufferedWriter.newLine(); 
                    bufferedWriter.close();
                    fileWriter.close();

                    System.out.println("Sample added to the file: " + fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
                Thread.sleep(1000); 
            }
            	System.out.println("Last Sample added "+lastSampleNo);



           // socket.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    
    
    
    
    
    
    
    
    private static String createRequestPacket(String sampleNo) {
      
//    	String hlmsg="MSH|^~\&|LIS|LIS|P8000|P8000|20230929214617||OML^O33^OML_O33|18698910009|P|2.5||||||||"
//    	 "PID|||114^^^LIS^PI||^^^||20230929||||Main Street^^Springfield^NY^65466^USA^ATC1||||||||||||||||||||N|AL"
//    	 "PV1||N|^^||||ATD^||||||||||ADD^||ABC123^LIS|||||||||||||||||||||||||20230926214617|20230926214617"
//    	 "SPM|1|2023097771||BLOOD||||MAIN LAB|||||||||20230926214617|20230926214617|||||"
//    	 "ORC|NW|2309260001|2309260001|2309260001|||||20230926214617||||||||||||^^^^^^^^^"
//    	 "TQ1|||||||20230926214617||R"
//    	 "OBR|1|2309260001|2309260001|CBC^CBC profile^P8000|||||||BLOOD||||||0033412364566||||||||P"
//    	
    	
    	
//    	MSH|^~\&|LIS|LIS|P8000|P8000|20230926214617||OML^O33^OML_O33|18698910009|P|2.5||||||||
//    	 PID|||000000^^^LIS^PI||^^^||20230926||||Main Street^^Springfield^NY^65466^USA^ATC1||||||||||||||||||||N|AL
//    	 PV1||N|^^||||ATD^||||||||||ADD^||ABC123^LIS|||||||||||||||||||||||||20230926214617|20230926214617
//    	 SPM|1|202309260001||BLOOD||||MAIN LAB|||||||||20230926214617|20230926214617|||||
//    	 ORC|NW|2309260001|2309260001|2309260001|||||20230926214617||||||||||||^^^^^^^^^
//    	 TQ1|||||||20230926214617||R
//    	 OBR|1|2309260001|2309260001|CBC^CBC profile^P8000|||||||BLOOD||||||0033412364566||||||||P
    	
    	 LocalDateTime now = LocalDateTime.now();
         
         // Define a custom date and time format
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
         
         // Format the current date and time using the custom format
         String formattedTimestamp = now.format(formatter);
    	Date timestamp = new Date();
    	System.out.println("Current Time Stamp : "+formattedTimestamp);
//    	String msg="MSH|^~\\&|LIS|LIS|P8000|P8000|20230929214617||OML^O33^OML_O33|18698910009|P|2.5||||||||\n"+
//    "PID|||77777^^^LIS^PI||^^^||20230928||||Main Street^^Springfield^NY^65466^USA^ATC1||||||||||||||||||||N|AL\n"+
//    "PV1||N|^^||||ATD^||||||||||ADD^||ABC123^LIS|||||||||||||||||||||||||20230926214617|20230926214617\n"+
//    "SPM|1|||BLOOD||||MAIN LAB|||||||||20230926214617|20230926214617|||||\n"+
//    "ORC|NW|2309260001|2309260001|2309260001|||||20230926214617||||||||||||^^^^^^^^^\n"+
//    "TQ1|||||||20230926214617||R\n"+
//    "OBR|1|2309260001|2309260001|CBC^CBC profile^P8000|||||||BLOOD||||||0033412364566||||||||P\n";
//    	
//    	
//    	String dd="MSH|^~\\&|LIS|LIS|HALIA|HALIA|20230939002512||OML^O33^OML_O33|20230939002512|P|2.5|||||||\r" + 
//    			"PID|||230909H1111^^^LIS^PI||989262300504521^DR MONIKA^^||20230910|F|||^^^^^^||||||||||||||||||||N|AL\r" + 
//    			"PV1||N|||||||||||||||||||||||||||||||||||||||||||\r" + 
//    			"SPM|1|1||BLOOD||||InterLink|||||||||202309100017||||||\r" + 
//    			"ORC|NW|33531|1|1|||||20230910001837||||3267||||||||^^^^^^^^^3267\r" + 
//    			"TQ1|||||||20230939002512||R\r" + 
//    			"OBR|1|1|1|CBC^CBC profile^LIS|||||||BLOOD||||||||||||||P\r" + 
//    			"ORC|NW|33531|1|1|||||20230910001837||||3267||||||||^^^^^^^^^3267\r" + 
//    			"TQ1|||||||20230939002512||R\r" + 
//    			"OBR|1|1|1|DIF^DIF profile^LIS|||||||BLOOD||||||||||||||P\r" + 
//    			"";
//    	
//    	
//    	
//    	String hl7Message = "MSH|^~\\&|LIS|LIS|P8000|P8000|20230926214617||OML^O33^OML_O33|18698910009|P|2.5||||||||\n" +
//                "PID|||55555000^^^LIS^PI||^^^||20230926||||Main Street^^Springfield^NY^65466^USA^ATC1||||||||||||||||||||N|AL\n" +
//                "PV1||N|^^||||ATD^||||||||||ADD^||ABC123^LIS|||||||||||||||||||||||||20230926214617|20230926214617\n" +
//                "SPM|1|202309260001||BLOOD||||MAIN LAB|||||||||20230926214617|20230926214617|||||\n" +
//                "ORC|NW|2309260001|2309260001|2309260001|||||20230926214617||||||||||||^^^^^^^^^\n" +
//                "TQ1|||||||20230926214617||R\n" +
//                "OBR|1|2309260001|2309260001|CBC^CBC profile^P8000|||||||BLOOD||||||0033412364566||||||||P"+"";

    	
    	//order packet
    	
//    	String msg="MSH|^~\\&|LIS|LIS|P8000|P8000|20231003161837||OML^O33^OML_O33|18698910009|P|2.5||||||||\r\n" + 
//    			"PID|||230926BR0054^^^LIS^PI||pat^name^^||20231003|M|||Main Street^^Springfield^NY^65466^USA^ATC1||||||||||||||||||||N|AL\r\n" + 
//    			"PV1||N|^^||||ATD^||||||||||ADD^||ABC123^LIS|||||||||||||||||||||||||20231003161837|20231003161837\r\n" + 
//    			"SPM|1|||BLOOD||||MAIN LAB|||||||||20231003161837|20231003161837|||||\r\n" + 
//    			"ORC|NW||||||||20231003161837||||||||||||^^^^^^^^^\r\n" + 
//    			"TQ1|||||||20231003161837||R\r\n" + 
//    			"OBR|1|||CBC^CBC profile^P8000|||||||BLOOD||||||0033412364566||||||||P";
//    
    	
    	 String hl7Message="MSH|^~\\&|LIS|LIS|HALIA|HALIA|20231004002512||OML^O33^OML_O33|20231004002512|P|2.5|||||||\r" + 
           		"PID|||230909H0696^^^LIS^PI||^^^||20230910||||^^^^^^||||||||||||||||||||N|AL\r" + 
           		"PV1||N|||||||||||||||||||||||||||||||||||||||||||\r" + 
           		"SPM|1|samid||BLOOD||||InterLink|||||||||202309100017||||||\r" + 
           		"ORC|NW||1|1|||||20231004002512||||3267||||||||^^^^^^^^^3267\r" + 
           		"TQ1|||||||20231004002512||R\r" + 
           		"OBR|1|1|1|CBC^CBC profile^LIS|||||||BLOOD||||||||||||||P\r" + 
           		"ORC|NW||1|1|||||20231004002512||||3267||||||||^^^^^^^^^3267\r" + 
           		"TQ1|||||||20231004002512||R\r" + 
           		"OBR|1|1|1|DIF^DIF profile^LIS|||||||BLOOD||||||||||||||P\r" + 
           		"";

    	 hl7Message = hl7Message.replaceAll("20231004002512", formattedTimestamp);
    	 hl7Message = hl7Message.replaceAll("230909H0696", sampleNo);
    	 hl7Message = hl7Message.replaceAll("samid", sampleNo);
      	
    	String newPatientId = sampleNo+"^^^LIS^PI";
        String newSpecimenId = sampleNo;

        // Split the message by line
        //String[] lines = hl7Message.split("\r");

//     for (int i = 0; i < lines.length; i++) {
//         if (lines[i].startsWith("PID|")) {
//             String[] segments = lines[i].split("\\|");
//             if (segments.length > 3) {
//                 segments[3] = newPatientId; // Update the Patient ID field
//                 lines[i] = String.join("|", segments); // Join the segments back together
//             }
//         } else if (lines[i].startsWith("SPM|")) {
//             // Split the SPM segment into fields using a regex that splits only on the first two occurrences of "|" to preserve the last four "|"
//             String[] segments = lines[i].split("\\|", 3);
//             if (segments.length > 2) {
//                 // Join the first two fields with "|" and concatenate the rest to preserve the last four "|"
//                 segments[2] = newSpecimenId + segments[2];
//                 lines[i] = String.join("|", segments); // Join the segments back together
//             }
//         }
//     }

     // Reconstruct the HL7 message
    // String modifiedHL7Message = String.join("\r", lines);
// Print the modified HL7 message
//System.out.println(modifiedHL7Message);
    	
    	
    	
//    	String MSH="MSH|^~\\\\&|LIS|LIS|HALIA|HALIA|20230921162512||OML^O33^OML_O33|20230910002512|P|2.5|||||||\r";
//        String PID="PID|||";
//        
//        		String PID1=sampleNo;
//        		
//        String PID2="^^^LIS^PI||989262300504521^";
//        		String pname= "";
//        	String PID4= "^^||20230910|F|||^^^^^^||||||||||||||||||||N|AL\r";
//        String PV1="PV1||N|||||||||||||||||||||||||||||||||||||||||||\r";
//        String SPM="SPM|1|";
//        String SPM1=sampleNo;
//        String SPM2="||BLOOD||||InterLink|||||||||202309100017||||||\r";
//        String ORC="ORC|NW|33531|1|1|||||20230910001837||||3267||||||||^^^^^^^^^3267\r";
//        String TQ1="TQ1|||||||20230910002512||R\r";
//        String OBR="OBR|1|1|1|DIF^DIF profile^LIS|||||||BLOOD||||||||||||||P\r";
//        String end="ORC|NW|33531|1|1|||||20230910001837||||3267||||||||^^^^^^^^^3267\r" + 
//          		"TQ1|||||||20230910002512||R\r" + 
//          		"OBR|1|1|1|DIF^DIF profile^LIS|||||||BLOOD||||||||||||||P\r" + 
//          		"";
//        
//        orderPacket.append("");
//        orderPacket.append(MSH);
//        orderPacket.append(PID);
//        orderPacket.append(PID1);
//        orderPacket.append(PID2);
//        orderPacket.append(pname);
//        orderPacket.append(PID4);
//        
//        orderPacket.append(PV1);
//        orderPacket.append(SPM);
//        orderPacket.append(SPM1);
//        orderPacket.append(SPM2);
//        
//        orderPacket.append(ORC);
//        orderPacket.append(TQ1);
//        orderPacket.append(OBR);
//        orderPacket.append(end);
        return hl7Message;
    }
    
    
    public static void manualsampleEntry(String sample) throws LLPException, IOException {
    	
    	
    	 try {
            // Socket socket = new Socket(instrumentHost, 10001);
            // OutputStream outputStream = socket.getOutputStream();
             
             //for (String sample : sampleNo) {
                 // Create and send the request packet for each sample
//             	if(!lastSampleNo.equals("")&&lastSampleNo.equals(null)) {
//                 	
//                 	System.out.println("Last Sample uploaded is :" +lastSampleNo);
//                 if(sample.equals(lastSampleNo)) {
//                 
//                 	
//                 	
//                 	
//                 }}	
    		    int serverPort = 54000; // Replace with the actual port number
    	          
                 String requestPacket = createRequestPacket(sample);
                 //sampleNo.remove(sample);
                 System.out.println("Order Packet :"+requestPacket+"::"+orderport);
                // outputStream.write(requestPacket.getBytes());
               //  outputStream.flush();
                
                 HapiContext context = new DefaultHapiContext();
                 PipeParser parser = context.getPipeParser();
                 
                 try {
                     // Parse the HL7 message string into a Message object
                     Message hl7ParsedMessage = parser.parse(requestPacket);
                     System.out.println("::::"+instrumentHost);
                     // Create a connection to the Horiba server
                     Connection connection = context.newClient("10.226.28.174", 10001, false);
                     Initiator initiator = connection.getInitiator();
                     
                     //HapiContext context = new DefaultHapiContext();
                   // HL7Service server = context.newServer(54000, false);

                     System.out.println("LIS Connected with :"+instrumentHost+":"+serverPort);
                     // Send the HL7 message
                    // server.
                     Message response = initiator.sendAndReceive(hl7ParsedMessage);
                    // ReceivingApplication<Message> handler = new ExampleReceiverApplication();
                     //server.registerApplication("ADT", "A01", handler);
                     //server.startAndWait();
                     /*
                      * We are going to register the same application to handle ADT^A02
                      * messages. Of course, we coud just as easily have specified a different
                      * handler.
                      */
                     //server.registerApplication("ADT", "A02", handler);
                     //server.registerApplication("OUL", "R22", handler);
                     //server.registerApplication("*", "*", handler);
                  //
                     
                     System.out.println("Server is connected");
                     
                     
                     
                     
//                     // Handle the response (if needed)
                    if (response != null) {
                         String responseMessage = parser.encode(response);
                         System.out.println("Received Response: " + responseMessage);
                     }
                    else {
                    	 

                         System.out.println("Response message not received"); 
                    }
//                     
//                     // Close the connection
                     connection.close();
                 } catch (HL7Exception e) {
                     e.printStackTrace();
                 } 
                 
                 
                 orderPacket.delete(0, orderPacket.length());
                 //sampleNo.remove(0);
                 // Sleep for a short duration (adjust as needed) before sending the next request
                 lastSampleNo=sample;
                 
                 //write all sample no. which is uploaded in worklist
                 String folderPath = "worklistdata/";

                 // Create the folder if it doesn't exist
                 File folder = new File(folderPath);
                 if (!folder.exists()) {
                     folder.mkdirs();
                 }
                 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                 String currentDate = dateFormat.format(new Date());

                 // Define the file name with the current date
                 String fileName = folderPath +"data_" + currentDate + ".txt";

               
                 try {
                     FileWriter fileWriter = new FileWriter(fileName, true); // true for append mode
                     BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                     // Append data to the file
                     bufferedWriter.write(sample);
                     bufferedWriter.newLine(); 
                     bufferedWriter.close();
                     fileWriter.close();

                     System.out.println("Sample added to the file: " + fileName);
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
                 
                 Thread.sleep(1000); 
             //}
 System.out.println("Last Sample Added : "+lastSampleNo);



             
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
    	
    }
}
