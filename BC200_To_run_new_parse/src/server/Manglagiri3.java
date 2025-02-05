package server;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.text.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.concurrent.*;

public class Manglagiri3 {
    static String text = "";
    static int server_port1 = 54000;
    static String currentDirectory;
    static String path_HIMS_LOG = "";
    static String path_MachineData = "";
    static String msgtype="";
    
    static Map<String, List> mp = new HashMap();  		//added by Rohit...
	static List <String> testCode = new ArrayList<>();
	static List <String> testValue = new ArrayList<>();
	List<String> sampleNo=new ArrayList<>();
	static PrintStream cout;

    public static void main(String[] args) throws IOException {
        System.out.println(server_port1);

        Path currentRelativePath = Paths.get("");
        currentDirectory = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current absolute path is: " + currentDirectory);

        path_HIMS_LOG = currentDirectory + "\\HIMS_log.txt";
        path_MachineData = currentDirectory + "\\Machine_log.txt";

        Manglagiri3 serverobj = new Manglagiri3(server_port1);
        serverobj.startServer();
    }

    Manglagiri3(int port) {
        // Your constructor code here
    }

    public void startServer() throws IOException {
        ServerSocket server = new ServerSocket(server_port1);
        System.out.println("SERVER BOOTED ON PORT: " + server_port1);
        System.out.println("ANY CLIENT CAN STOP THE SERVER BY SENDING -1");

        ExecutorService pool = Executors.newFixedThreadPool(500);

        while (true) {
            Socket client = server.accept();
            ServerThread runnable = new ServerThread(client);
            pool.execute(runnable);
        }
    }

    private static class ServerThread implements Runnable {
        Socket client;
        BufferedReader cin;
        

        ServerThread(Socket client) throws IOException {
            this.client = client;
            cin = new BufferedReader(new InputStreamReader(client.getInputStream()));
            cout = new PrintStream(client.getOutputStream());
        }

        public void run() {
            try {
            	
            	   StringBuilder receivedMessageBuilder = new StringBuilder();
                   String line;
                   
                   // Read until the client closes the connection or sends a termination character
                   while ((line = cin.readLine()) != null) {
                       receivedMessageBuilder.append(line);
                       receivedMessageBuilder.append("\r");
                       if (line.isEmpty()) {
                           // End of message
                           break;
                       }
                   }
                   String receivedMessage = receivedMessageBuilder.toString();
                   String receivedMessageType = extractMessageType(receivedMessage);

                   System.out.println("receivedMessage"+receivedMessage);
                   
                if (receivedMessageType.equals("QBP")) {
                    Thread qbpThread = new Thread(new QBPHandler(receivedMessage));
                    qbpThread.start();
                } else if (receivedMessageType.equals("OUL")) {
                    Thread oulThread = new Thread(new OULHandler(receivedMessage));
                    oulThread.start();
                } else {
                    // Handle other message types as needed
                }
            } catch (IOException ex) {
                System.out.println("Error: " + ex);
            }
        }

        private String extractMessageType(String message) {
        	
        	  String[] segments1 = message.split("\r");
        	  for (String segment : segments1) {
			        // System.out.println("Segment: " + segment);
			         

			         
			         
			      	if (segment.contains("MSH")) {
			      		String[] parts = segment.split("\\|");

			      		
			      		String value=parts[8];
			      		String[] MSH = value.split("\\^");
			      	
			      		msgtype=MSH[0];
			      		System.out.println("Incoming msg Type :"+msgtype);
			      	}}
            // Logic to extract message type (QBP, OUL, etc.) from the message
            return msgtype; // For demonstration purposes, assume QBP
        }
    }

    private static class QBPHandler implements Runnable {
        private String message;
		private String pidmsg;
		private String[] sampleNOblock;
		private String sampleNOblock2;
		private String rackNo;
		private String RackSeq;
		private String SEC2val;
		private String modifiedTimeStr;
		private String MSHsegment;
		private String msgcontrolID;
		private String timeZoneMachine;
		private String sampleNO;

        public QBPHandler(String message) {
            this.message = message;
        }

        public void run() {
            // Logic to handle QBP message
            // Parse the message, process it, and send response
        	System.out.println("QBP Recived");
			 String[] segments = message.split("\r");
             
             
             for (String segment : segments) {
               //  System.out.println("Segment: " + segment);
                 
             	if (segment.contains("QBP^Q11^QBP_Q11")) {
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
               		
               	//	System.out.println("modified asdtime zone"+modifiedTimeStr);
                 	
               		 }
                 
             	
             	if (segment.startsWith("QPD") || segment.contains("INIBAR^^99ROC")) {
              		String[] parts = segment.split("\\|");
              		
               		 pidmsg=segments[1];
                     //	System.out.println("Patient Information :"+pidmsg);
                     	
                     	String[] msgpart=pidmsg.split("\\|");
                     	
                     	 sampleNOblock=msgpart[3].split("\\^");
                     	 sampleNOblock2=msgpart[4];
                     	  sampleNO = sampleNOblock[0];
                     	 rackNo=msgpart[4];
                     	 SEC2val=msgpart[2];
                     	 RackSeq=msgpart[5];
                     	System.out.println("sample no.:"+sampleNO +"Valueat second place::"+SEC2val +"Rack NO:"+rackNo);

                     	ABC obj =  new ABC();
		                	final StringBuffer Test_Code;
		                	String it = obj.getSampleDtl(sampleNO);
		            		String[] kvPairs = it.split(";");
		            		
		            		String[] kv = it.split(";");
			            	
		            		String k[] = kv.toString().split(";");
		            		
		            		
		            		
		                	
		            		for(String data:kv) {
		            			
		            			if(!(data.equals("NA")|| data.equals("UU")|| data.equals("VV")|| data.equals("WW") || data=="NA" || data=="VV"|| data=="UU"|| data=="WW") ) {
		            			testCode.add(data);
		            			}
		            		}
		            		
		            		System.out.println("Test Code <<--- :: --->>>" +testCode);
		                }

             }		
        	
             
             
             
             LocalDateTime now = LocalDateTime.now();
             DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
             String formattedTimestamp = now.format(formatter);
             
             StringBuilder msg = new StringBuilder();	
         	msg.append((char)0x0B).append("MSH|^~\\&|Host||cobas pure||YYYYMMDDHHMMSSQZZ").append("||OML^O33^OML_O33|ORMSG_CONTROLID|P|2.5.1|||NE|AL||UNICODE UTF-8|||LAB-28R^ROCHE").append("\r");
         	msg.append("PID|||SAM_NO||VINAY KANTH^^^^^^U||19940305|M").append("\r");
     //    	msg.append("SPM|1|SAM_NO&BARCODE||SERPLAS^^99ROC|||||||P^^HL70369||||||YYYYMMDDHHMMSSQZZ").append("+0530").append("||||||||SC^^99ROC").append("\r");
         	msg.append("SPM|1|SAM_NO&BARCODE||SERPLAS^^99ROC|||||||P^^HL70369||||||||||||||||SC^^99ROC").append("\r");

         	msg.append("SAC|||SAM_NO^BARCODE|||||||RACKNO|RACKEQ||||||||||||||||||^1^1").append("\r");
         	

             StringBuilder testCodedetail = new StringBuilder();	
         	
             testCodedetail.append("ORC|NW||||||||YYYYMMDDHHMMSSQZZ").append("\r");
             testCodedetail.append("TQ1|||||||||R^^HL70485").append("\r");
             testCodedetail.append("OBR|1|4711||testno^^99ROC").append("\r"); //10207 : FSH //20090 :ALB2-G
             testCodedetail.append("TCD|testno^^99ROC|1^:^3").append("\r");

             int size=testCode.size();
         	for(int k=0;k<testCode.size()-1;k++) {
         		//String abcc=msg.toString();
         		int a=size-(k+1);
//         		if(a==0) {
//         			a=size-1;
//         		}
         		msg.append("ORC|NW||||||||YYYYMMDDHHMMSSQZZ").append("\r");
         		msg.append("TQ1|||||||||R^^HL70485").append("\r");
         		msg.append("OBR|1|4711||"+testCode.get(k)+"^^99ROC").append("\r"); //10207 : FSH //20090 :ALB2-G
         		
         		msg.append("TCD|"+testCode.get(k)+"^^99ROC|1^:^"+(a)).append("\r");

         		
         	}
         	
         	
//         	
//         	msg.append("ORC|NW||||||||YYYYMMDDHHMMSSQZZ").append("\r");
//         	msg.append("TQ1|||||||||R^^HL70485").append("\r");
//         	msg.append("OBR|1|4711||10032^^99ROC").append("\r"); //10207 : FSH //20090 :ALB2-G
//         	msg.append("TCD|10032^^99ROC|1^:^3").append("\r");
//         	msg.append("ORC|NW||||||||YYYYMMDDHHMMSSQZZ").append("\r");
//         	msg.append("TQ1|||||||||R^^HL70485").append("\r");
//         	msg.append("OBR|1|4711||10120^^99ROC").append("\r"); //10207 : FSH //20090 :ALB2-G
//         	msg.append("TCD|10120^^99ROC|1^:^2").append("\r");
//
//         	msg.append("ORC|NW||||||||YYYYMMDDHHMMSSQZZ").append("\r");
//         	msg.append("TQ1|||||||||R^^HL70485").append("\r");
//         	msg.append("OBR|1|4711||10172^^99ROC").append("\r"); //10207 : FSH //20090 :ALB2-G
//         	msg.append("TCD|10172^^99ROC|1^:^1").append("\r");

         	msg.append((char)0x1C).append("\r");
         	
         String hl7Message;
         hl7Message=msg.toString();
         
         String str [] =pidmsg.split("\\|");
         
         // str[12]="S";
          int lastIndex = pidmsg.length() - 1;
          pidmsg = pidmsg.substring(0, lastIndex) +"R";  //"S";
          //System.out.println("QPD"+pidmsg);
         
          

    
          
          StringBuilder ackMessage = new StringBuilder();	
          ackMessage.append((char)0x0B).append("MSH|^~\\&|Host||cobas pure||YYYYMMDDHHMMSSQZZ").append("+0530").append("||RSP^K11^RSP_K11|MSG_CONTROLID|P|2.5.1|||NE|AL||UNICODE UTF-8|||LAB-27R^ROCHE").append((char)0x0D);
          ackMessage.append("MSA|AA|MSA_MSG_CONTROLID").append((char)0x0D);
          ackMessage.append("QAK|SECONDVAL|OK|INIBAR^^99ROC").append((char)0x0D).append(pidmsg).append((char)0x0D);
          ackMessage.append((char)0x1C).append((char)0x0D);
          
          String data;
          data=ackMessage.toString();
          
          data=data.replaceAll("MSG_CONTROLID", String.valueOf(Integer.parseInt(msgcontrolID)+100));
          data=data.replaceAll("MSA_MSG_CONTROLID", String.valueOf(Integer.parseInt(msgcontrolID)));
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
          hl7Message = hl7Message.replaceAll("ORMSG_CONTROLID", String.valueOf(Integer.parseInt(msgcontrolID)+101));
          hl7Message = hl7Message.replaceAll("RACKNO", rackNo);
          hl7Message = hl7Message.replaceAll("ProcessingID", "1000"); 
			 
          
          cout.print(data);
          cout.print(hl7Message);
          
          System.out.println("ACK Sent : "+data);
          System.out.println("OML Message Sent : "+hl7Message);
        }
    }

    private static class OULHandler implements Runnable {
        private String message;
		private int count;
		private String sampleName;

        public OULHandler(String message) {
            this.message = message;
        }

        public void run() {
        	System.out.println("OUL");
        	
        	 String[] segments1 = message.split("\r");
       	  for (String segment : segments1) {
       		  System.out.println("Segment"+segment);
       		  
       		if (segment.contains("SPM")) { // to check sample no. in SPM section
	            // Split the line by the pipe character "|"
	            String[] parts = segment.split("\\|");

	            // Check if there are at least 7 elements in the array
	                // Extract and print the value after the 6th pipe
	                String value = parts[2];
	                System.out.println("ROHIT check 1"+value);
	                String[] pid = value.split("\\&");
	                 System.out.println("Patient Id: " + pid[0]);
	                 
	                 sampleName=pid[0];
	                 System.out.println("ROHIT check 2"+sampleName);
	                 //sampleNo.add(sampleName);
	                //System.out.println("Patiwnt Id " + pid);
	                 
	     
	    	
	    	}
       		  
        	if (segment.contains("OBX") || segment.startsWith("OBX")) {
	            // Split the line by the pipe character "|"
	    		 String[] parts = segment.split("\\|");
	    		 
	    		 
				count++;
				if(parts[2].equals("NM") &&  parts[1].equals("1")) {

	             // Check if there are at least 5 elements in the array
	             if (parts.length > 3) {
	                 // Extract and print the value after the 4th pipe
	                 String value = parts[3];
	                 
	                // if(value.equals("WBC")|| value.equals("NEU#") || value.equals("LYM#") || value.equals("MON#") || value.equals("EOS#") || value.equals("BAS#") || value.equals("IMG#") || value.equals("NEU%") || value.equals("LYM%") || value.equals("MON%") || value.equals("EOS%") || value.equals("BAS%") || value.equals("IMG%") || value.equals("RBC") || value.equals("HGB") || value.equals("HCT") || value.equals("MCV") || value.equals("MCH") || value.equals("MCHC") || value.equals("RDW-CV") || value.equals("RDW-SD") || value.equals("PLT") || value.equals("MPV") || value.equals("PDW") || value.equals("PCT") || value.equals("PLCC") || value.equals("NRBC#") || value.equals("NRBC%") || value.equals("PLCR") ) {
	                  			
	                 String[] tc = value.split("\\^");
	                 System.out.println("Test Code " + tc[0]);
	                 testCode.add(tc[0]);
	             }
	             
	             
	             if (parts.length > 5) {
	                 // Extract and print the value after the 4th pipe
	                 String value = parts[5];
	                 
	                // if(value.equals("WBC")|| value.equals("NEU#") || value.equals("LYM#") || value.equals("MON#") || value.equals("EOS#") || value.equals("BAS#") || value.equals("IMG#") || value.equals("NEU%") || value.equals("LYM%") || value.equals("MON%") || value.equals("EOS%") || value.equals("BAS%") || value.equals("IMG%") || value.equals("RBC") || value.equals("HGB") || value.equals("HCT") || value.equals("MCV") || value.equals("MCH") || value.equals("MCHC") || value.equals("RDW-CV") || value.equals("RDW-SD") || value.equals("PLT") || value.equals("MPV") || value.equals("PDW") || value.equals("PCT") || value.equals("PLCC") || value.equals("NRBC#") || value.equals("NRBC%") || value.equals("PLCR") ) {
	                  			
	                 //String[] tv = value.split("\\^");
	                 System.out.println("Test Value " + value);
	                 double number = Double.parseDouble(value);
		             double truncatedNumber = (int) (number * 10) / 10.0;
		            // System.out.println("truncatedNumber"+truncatedNumber);
		             String result = String.format("%.1f", truncatedNumber);
		            
	                 testValue.add(result);
	                 
	             }
	            
	    	
	    	}
	         }
       	  }
       	  
       	  
          System.out.println("Sample No:"+sampleName);
	        System.out.println("Total Test Fetched from MSG"+count);
	      System.out.println("Total Test Entered"+testCode.size());  
	      
	    //  System.out.println(testValue);  
	         
	      mp.put("TestCode", testCode);
	     	mp.put("TestValue", testValue);
	     	//mp.put("SampleNO",sampleNo);
	     	 
	        System.out.println(testCode);  
			   
	        System.out.println(testValue);  
			   

	        if(!mp.isEmpty()) {
	     	ABC abc =  new ABC();
	     	
	     	for(int i=0;i<testCode.size();i++) {
	     		System.out.println("Rohit ###### ABC ");
	      	abc.insert_SysmexXN350A(testCode.get(i),testValue.get(i),sampleName.toString().trim());
	     
	     	}
            // Logic to handle OUL message
            // Parse the message, process it, and send response
        }
    }
    }}
