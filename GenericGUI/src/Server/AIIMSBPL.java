package Server;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Server.ReadPropertyFile;

import java.io.*;
import java.net.*;

import java.io.*;
import java.net.*;

public class AIIMSBPL {
	static Map res = ReadPropertyFile.getPropertyValues();
	static String server_ip = (String) res.get("server_ip");
	static String server_port = (String) res.get("server_port");
    public static void main(String[] args) {
        String hostname = server_ip;
        int port =Integer.parseInt(server_port);

        
        
        
        
        
        String serverAddress = server_ip; // Replace with your server's IP address
        int serverPort = 12345; // Replace with your server's port number
        
        try {
            // Connect to the server
            Socket socket = new Socket(hostname, port);
            System.out.println("Connected to server");
            System.out.println("Server Ip :"+server_ip);
            System.out.println("Port :"+port);
            // Setup input and output streams
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            // Communication loop
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String message;
            int red = -1;
			byte[] buffer = new byte[800 * 1024]; // a read buffer of 5KiB
			byte[] redData;
			StringBuilder clientData = new StringBuilder();
			String redDataText = "";
			char fsChar = 0x1C;
		 	
		 	char crChar=0x0D;
			while ((red = socket.getInputStream().read(buffer)) > -1) // **Code Starts For Receiving Client
				// Messages
{
				
				redData = new byte[red];
				
				System.arraycopy(buffer, 0, redData, 0, red);
				
				redDataText = new String(redData,"UTF-8"); 
			//	  redDataText = redDataText.replaceAll("\\s", ""); 
				 char[] character_array = redDataText.toCharArray();
				System.out.println("MESSAGE PART RECIEVED RRRR:- " + redDataText);
				
				
				String[] strData=redDataText.split(Character.toString(fsChar));
				
				
						
						
					//	for(String strPKT:strData) {
						//	System.out.println("MESSAGE PART RECIEVED2:- " + redDataText);
							
							 	String sampleName = "";
							 	String msgtype="";
							 	
							   String[] segments1 = redDataText.split(Character.toString(crChar));
							   Map<String, List> mp = new HashMap();  		//added by Rohit...
								List <String> testCode = new ArrayList<>();
								List <String> testValue = new ArrayList<>();
								List<String> sampleNo=new ArrayList<>();
								
						//		redDataText=redDataText.replaceAll("\\s","");
								System.out.println("MESSAGE PART RECIEVED 2:- " + redDataText);
						        for (String segment : segments1) {
						        	
						        	segment=segment.replaceAll("\\s", "");
						        	if (segment.contains("MSH")) {
						        		
							      		String[] parts = segment.split("\\|");

							      		
							      		String value=parts[8];
							      		String[] MSH = value.split("\\^");
							      	
							      		msgtype=MSH[0];
							      		System.out.println("Incoming msg Type :"+msgtype);
							      		
							      	    if(msgtype.equals("ORU") ) {  ///sending ACK when result recieved
							      	    	 Date currentDate = new Date();

							      	        // Define the desired date format
							      	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

							      	        // Format the date
							      	        String formattedDate = dateFormat.format(currentDate);

							      	        // Print the formatted date
							      	        System.out.println("Formatted Date: " + formattedDate);
							      	    	String controlid=parts[9];
							      	    	String resACK1="MSH|^~\\&|host||cobas pure||YYYYMMDDHHMMSS||ACK^R22^ACK|CONTROLID||2.5.1||||NE||UNICODE UTF-8|\r"
								        	 		+ "MSA|AA|CONTROLID||\r"
								        	 		+ "";
							      	    	StringBuilder msg = new StringBuilder();
							      	    	
							      	    	
							      	    	msg.append("MSH|^~\\&|||||YYYYMMDDHHMMSS||ACK^R01|1|P|2.3.1||||||UNICODE||").append("\r");
							      	    	msg.append("MSA|AA|1||||0");
							      	    //	msg.append("MSH|^~\\&|host||cobas pure||YYYYMMDDHHMMSS||ACK^R22^ACK|CONTROLID||2.5.1||||NE||UNICODE UTF-8|").append("\r");
							      	    //	msg.append("MSA|AA|CONTROLID||").append("\r");
							      	    //	msg.append("MSA|AA|CONTROLID||").append("\r");
							      	    	msg.append((char)0x1C).append("\r");
							      	    	
							      	    	String resACK=msg.toString();
							      	    	resACK=resACK.replaceAll("CONTROLID",controlid);
							      	    	resACK=resACK.replaceAll("YYYYMMDDHHMMSS",formattedDate);
							      	    	
							      	    	//cout.print(resACK);
							      	  	
											output.write(resACK);
											output.flush();
							      	    	System.out.println("ACK SENT :" + resACK);
							      	  
							      	    
							      	    }
							      	    
							      	    
							      	}
						        	
						        	
						        	
						        	if(msgtype.equals("ORU")) {
						        		
						        		if (segment.contains("OBR")) {
						        			
						        			String[] obtPkt=segment.split("\\|");
						        			sampleName=obtPkt[2];
						        			System.out.println("Sample No :  "+sampleName);
						        								        			
						        		}
						        		
										
										  else if (segment.contains("OBX")) {
										  
										  String[] obxPkt=segment.split("\\|"); String
										  testNm=obxPkt[3].split("\\^")[0]; 
										  String testval=obxPkt[4]; //BLD,LEU,PRO
										  
										  if(testNm.equals("GLU") || testNm.equals("BLD") ||
										  testNm.equals("LEU")||testNm.equals("PRO")||
										  testNm.equals("NIT")||testNm.equals("URO")||testNm.equals("BIL")||
										  testNm.equals("KET")||testNm.equals("PH")||testNm.equals("SG")
										  ||testNm.equals("VC")||testNm.equals("ALB")||testNm.equals("Cr")||
										  testNm.equals("Wbc") ||testNm.equals("Rbc") ||testNm.equals("Crystal")
										  ||testNm.equals("Epithelia")||testNm.equals("Crystal")
										  ||testNm.equals("RbcPF") ||testNm.equals("WbcPF")||testNm.equals("Crystal")
										  ||testNm.equals("Crystal") ||testNm.equals("Crystal")
										  ||testNm.equals("Crystal") ||testNm.equals("Crystal")
										  
										  ) {
										  
										  System.out.println("Test Code :"+testNm);
										  System.out.println("Test Value :"+testval); testCode.add(testNm);
										  testValue.add(testval);
										  
										  if(!sampleName.equals("")) {
											   ABC.insert_GenExpert(testNm, testval,	sampleName); } else { // System.out.println("sample no is  null"); }
										  }
										  
										  }
										  
										  }
										  
										 
						        		
						        	}
						       }
							
						
						
						//}
						
						
					
}
			
			
			
          /*  while (true) {
                // Send message to server
//                System.out.print("Enter message to send: ");
//                message = reader.readLine();
//                output.println(message);

                // Receive message from server
                String receivedMessage = input.readLine();
                if (receivedMessage != null) {
                
                	System.out.println("Received from server: " + receivedMessage);
                	
                }
                
              //  while(receivedMessage!=null) {
                	
                	 
             //   }

                // Process received data (replace with your processing logic)
               
            }*/

            // Close the socket
           // socket.close();
         //   System.out.println("Connection closed");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        
        
        
        
        
        
        
        
        
    
}
