package Server;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fazecast.jSerialComm.SerialPort;
import com.formdev.flatlaf.FlatLightLaf;

import Server.ABC;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.CRC32;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//Machine Interface GUI   ......###########........... TCP/IP , Serial Communication 
public class ClientGUI {

	
	static Map res = ReadPropertyFile.getPropertyValues();
	//static String server_ip = (String) res.get("server_ip");
	//static String server_port = (String) res.get("server_port");
	   private static final String FILE_NAME = "c:/TcpFiles/property/monitor_data.txt"; // File to save the data
	   private static final String LOG_FILE_NAME = "c:/TcpFiles/property/monitor_log.txt";
	   private static final String PARAM_FILE_NAME="c:/TcpFiles/property/monitor_param_data.xlsx";
	   private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 
	public static void cllientConnect(String serverip, String portNumber) {
    //    String hostname = server_ip;
        int port =Integer.parseInt(portNumber);

        
        
        
        
        
      //  String serverAddress = server_ip; // Replace with your server's IP address
     //   int serverPort = 12345; // Replace with your server's port number
        
        try {
            // Connect to the server
            Socket socket = new Socket(serverip, port);
            System.out.println("Connected to server");
            GenericServer.logMessage("Connected to server On : "+serverip+" : "+portNumber, Color.BLACK);
            System.out.println("Server Ip :"+serverip);
            System.out.println("Port :"+portNumber);
            saveToFile("Connected to server On : "+serverip+" : "+portNumber,LOG_FILE_NAME);
           
            // Setup input and output streams
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            // Communication loop
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String message;
            int red = -1;
			byte[] buffer = new byte[8000 * 1024]; // a read buffer of 5KiB
			byte[] redData;
			StringBuilder clientData = new StringBuilder();
			String redDataText = "";
			char fsChar = 0x1C;
		 	
		 	char crChar=0x0D;
		 	
		 	
		 	
		 	
		 	String data="MSH|^~\\&|LIS|LAB|myla|BMX|20240820163600||OML^O33^OML_O33|MSG-20240820-163600-0377|P|2.5.1|||NE|AL||UNICODE UTF-8 \n\n"
		 	+ "PID|||400450243||PAA^DONI^^^^^nill|PADMAJA|19620820|F|||nill^^nill^^nill^nill|||||S| \n\n"
		 	+ "PV1||E|ACUTE-A M C U-III^AMC  6N-27(ICU)^^AIIMSMG^^^^^AMC  6N-27(ICU)^||||^RAJESH BOLLAM||||||||||||1657550     \n\n"
		 	+ "SPM|1|68182||RESP^RESP^99BMx|||^^|^^|||P^Patient^||||||20240820163525|\n\n"
		 	+ "SAC||||24008672 \n\n"
		 	+ "ORC|NW||||||||20240820163525\n\n"
		 	+ "TQ1|||||||||R^^\n\n"
		 	+ "OBR|1|714647||SU^SU^99BMx||||||^^||||||^RAJESH BOLLAM";
		 	
		 	StringBuffer orderPacket = new StringBuffer();
		 	orderPacket.append("MSH|^~\\&|LIS|LAB|myla|BMX|20240820163600||OML^O33^OML_O33|MSG-20240820-163600-0377|P|2.5.1|||NE|AL||UNICODE UTF-8").append("\n");
		 	orderPacket.append("PID|||400450243||PAA^DONI^^^^^nill|PADMAJA|19620820|F|||nill^^nill^^nill^nill|||||S|").append("\n");
		 	orderPacket.append("PV1||E|ACUTE-A M C U-III^AMC  6N-27(ICU)^^AIIMSMG^^^^^AMC  6N-27(ICU)^||||^RAJESH BOLLAM||||||||||||1657550").append("\n");;
		 	orderPacket.append("SPM|1|68182||RESP^RESP^99BMx|||^^|^^|||P^Patient^||||||20240820163525|").append("\n");
		 	orderPacket.append("SAC||||24008672").append("\n");
		 	orderPacket.append("ORC|NW||||||||20240820163525").append("\n");
		 	orderPacket.append("TQ1|||||||||R^^").append("\n");
		 	orderPacket.append("OBR|1|714647||SU^SU^99BMx||||||^^||||||^RAJESH BOLLAM").append("\n");
		 //	GenericServer.logMessage("Data Sent :"+orderPacket, Color.MAGENTA);
			  
		 	//output.write(orderPacket.toString());
			//output.flush();
		 	
			while (true) {
			while ((red = socket.getInputStream().read(buffer)) > -1) // **Code Starts For Receiving Client
				// Messages
{
				
				redData = new byte[red];
				
				System.arraycopy(buffer, 0, redData, 0, red);
				
				redDataText = new String(redData,"UTF-8"); 
				 // redDataText= redDataText.replaceAll("\\s", "");
				 // redDataText= redDataText.replaceAll(Character.toString((char) 0x00), "");
				 char[] character_array = redDataText.toCharArray();
				System.out.println("Data  RECIEVED :- " + redDataText);
				saveToFile("Data  RECIEVED :- " + redDataText,LOG_FILE_NAME);
				 GenericServer.logMessage("Data  RECIEVED :- " + redDataText, Color.black);
				String[] strData=redDataText.split(Character.toString(fsChar));
				
				
						
						
					//	for(String strPKT:strData) {
						//	System.out.println("MESSAGE PART RECIEVED2:- " + redDataText);
							
							 	String sampleName = "";
							 	String msgtype="";
							 	boolean isStandBy=false;
							 	GenericServer.logMessage("Monitoring starts :", Color.MAGENTA);
							 //	redDataText=redDataText.replaceAll("\\s","");
							 	
							 	String[] segments1 = redDataText.split(Character.toString(crChar));
							   Map<String, List> mp = new HashMap();  		//added by Rohit...
								List <String> testCode = new ArrayList<>();
								List <String> testValue = new ArrayList<>();
								List<String> sampleNo=new ArrayList<>();
						       for (String segment : segments1) {
						        	
						       
						        	if (segment.contains("MSH")) {
						        		
							      		String[] parts = segment.split("\\|");

							      		
							      		String value=parts[8];
							      		String[] MSH = value.split("\\^");
							      	
							      		msgtype=MSH[0];
							      		System.out.println("Incoming msg Type :"+msgtype);
							      		saveToFile("Incoming msg Type :"+msgtype,LOG_FILE_NAME);
							      		GenericServer.logMessage("Incoming msg Type :"+msgtype, Color.black);
							      	    if(msgtype.equals("ORU") ) {  ///sending ACK when result recieved
							      	    	 Date currentDate = new Date();

							      	        // Define the desired date format
							      	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

							      	        // Format the date
							      	        String formattedDate = dateFormat.format(currentDate);

							      	        // Print the formatted date
							      	        System.out.println("Formatted Date: " + formattedDate);
							      	    	String controlid=parts[9];
							      	    	String resACK1="MSH|^~\\&|host||cobas pure||YYYYMMDDHHMMSS||ACK^R22^ACK|CONTROLID||2.5.1||||NE||UNICODE UTF-8|\n"
								        	 		+ "MSA|AA|CONTROLID||\n"
								        	 		+ "";
							      	    	StringBuilder msg = new StringBuilder();
							      	    	
							      	    	
							      	    	msg.append("MSH|^~\\&|||||YYYYMMDDHHMMSS||ACK^R01|1|P|2.3.1||||||UNICODE||").append("\n");
							      	    	msg.append("MSA|AA|1||||0");
							      	    //	msg.append("MSH|^~\\&|host||cobas pure||YYYYMMDDHHMMSS||ACK^R22^ACK|CONTROLID||2.5.1||||NE||UNICODE UTF-8|").append("\n");
							      	    //	msg.append("MSA|AA|CONTROLID||").append("\n");
							      	    //	msg.append("MSA|AA|CONTROLID||").append("\n");
							      	    	msg.append((char)0x1C).append("\n");
							      	    	
							      	    	String resACK=msg.toString();
							      	    	resACK=resACK.replaceAll("CONTROLID",controlid);
							      	    	resACK=resACK.replaceAll("YYYYMMDDHHMMSS",formattedDate);
							      	    	
							      	    	//cout.print(resACK);
							      	  	
											output.write(resACK);
											output.flush();
							      	    	System.out.println("ACK SENT :" + resACK);
							      	    	GenericServer.logMessage("ACK SENT :" + resACK, Color.BLUE);
							      	    	saveToFile("ACK SENT :" + resACK,LOG_FILE_NAME);
							      	    }
							      	    
							      	    
							      	}
						        	
						        	
						        	if(msgtype.equals("ORU")) {
						        		
						        		if (segment.contains("OBR")) {
						        			
						        			//String[] obtPkt=segment.split("\\|");
						        			//sampleName=obtPkt[2];
						        			//System.out.println("Sample No :  "+sampleName);
						        								        			
						        		}
						        	 
						        		
						        	}
						       }
							
						System.out.println("11111");
						       if(msgtype.equals("ORU")) {
						    	   System.out.println("22222");
						       ///if message type is ORU only
						       List<String[]> obxSegments = new ArrayList<>();
					            String[] lines = redDataText.split(Character.toString(crChar));
//OBX||CE|2305^WorkState||1^Standby||||||F
					            String pid = "";
					            for (String line : lines) {
					            	line=line.replace("\n", "");
					                String[] segments = line.split("\\|");

					                if (segments[0].contains("PID")) {
					                    String str[]=  segments[3].split("\\^");
					                    pid=str[0];
					                } else if (segments[0].contains("OBX")) {
					                	segments.toString().replace("\n", "");
					                    obxSegments.add(segments);
//					                    String[] val=segments[5].split("\\^");
					                  //  if(segments.toString().contains("WorkState") ||segments.toString().contains("2305") ) {
//					                    	if(val[0].contains("1")){// || val[1].contains("Standby")) {
//					                    		
//					                    		isStandBy=true;	
//					                    		GenericServer.logMessage("Monitor in Standby Mode :", Color.MAGENTA);
//					                    			
//					                    	}
					                    		
					                   // }
					                    
					                }
					            }

					            Workbook workbook;
					            Sheet sheet;
					            FileInputStream fis = null;
System.out.println("33333");
					            try {
					                fis = new FileInputStream(PARAM_FILE_NAME);
					                workbook = new XSSFWorkbook(fis);
					                sheet = workbook.getSheetAt(0);
					                System.out.println("4444");
					            } catch (IOException e) {
					                // File doesn't exist or can't be read; create a new workbook and sheet
					                workbook = new XSSFWorkbook();
					                sheet = workbook.createSheet("Param Data");
					                System.out.println("5555");
					                // Create Header Row
					                Row headerRow = sheet.createRow(0);
					                headerRow.createCell(0).setCellValue("PID");
					                headerRow.createCell(1).setCellValue("Measurement ID");
					                headerRow.createCell(2).setCellValue("Measurement Name");
					                headerRow.createCell(3).setCellValue("Measurement Sub Id");
					                headerRow.createCell(4).setCellValue("Value");
					                headerRow.createCell(5).setCellValue("Unit");
					                headerRow.createCell(6).setCellValue("Packet Timestamp");
					                headerRow.createCell(7).setCellValue("Timestamp");
					            } finally {
					                if (fis != null) {
					                    try {
					                        fis.close();
					                    } catch (IOException e) {
					                        e.printStackTrace();
					                    }
					                }
					            }

					            // Find the last row number
					            int rowNum = sheet.getLastRowNum() + 1;

					            // Populate the Excel sheet with OBX data
					            for (String[] obxSegment : obxSegments) {
					                Row row = sheet.createRow(rowNum++);
					                row.createCell(0).setCellValue(pid);

					                String[] idAndName = obxSegment[3].split("\\^");
					                row.createCell(1).setCellValue(idAndName[0]);
					                row.createCell(2).setCellValue(idAndName[1]);
					                row.createCell(3).setCellValue(obxSegment[4]);
					                row.createCell(4).setCellValue(obxSegment[5]);
					                row.createCell(5).setCellValue(obxSegment[6]);
					                row.createCell(5).setCellValue(obxSegment[14]);
					                row.createCell(6).setCellValue(TIMESTAMP_FORMAT.format(new Date()));
					                System.out.println("66666");
					            }

					            try (FileOutputStream fileOut = new FileOutputStream(PARAM_FILE_NAME)) {
					                workbook.write(fileOut);
					                System.out.println("Data written to Excel");
					                saveToFile("Data written to Excel",LOG_FILE_NAME);
					            } catch (IOException e) {
					                e.printStackTrace();
					            }
					            
					            
					            List<OBXSegment> obxSegmentList = new ArrayList<>();
					            for (String[] obxSegment : obxSegments) {
					                OBXSegment segment = new OBXSegment();
					                segment.setPatient_id(pid);

					                String[] idAndName = obxSegment[3].split("\\^");
					                segment.setParam_id(idAndName[0]);
					                segment.setParam_name(idAndName[1]);
					                segment.setParam_value(obxSegment[5]);
					                
					                segment.setParam_unit(obxSegment[6]);
					                segment.setParam_referenceRange("");
					                segment.setPackettimestamp(obxSegment[14]);
					                segment.setTimestamp(TIMESTAMP_FORMAT.format(new Date()));

					                obxSegmentList.add(segment);
					            }

					            // Convert list to JSON
					            ObjectMapper mapper = new ObjectMapper();
					            mapper.enable(SerializationFeature.INDENT_OUTPUT);
					            String jsonString = mapper.writeValueAsString(obxSegmentList);

					            System.out.println(jsonString);
					            saveToFile(jsonString,FILE_NAME);
					            GenericServer.logMessage("Data written to Excel for PID :" + pid, Color.BLUE);
					            
					            saveToFile("Data written to Excel for PID :" + pid,LOG_FILE_NAME);
					            
					            //api call
					            
					           
					            
					            OT_EQP_SERVICE objDao = new OT_EQP_SERVICE();
					            String response=objDao.saveData(jsonString);
					            System.out.println(response);
					           
					          /*  try {
					                // Define the URL of the API endpoint
					                URL url = new URL("http://10.226.28.174:8380/OT_INTEGRATION/service/api/saveresult/");

					            	//  URL url = new URL("http://10.10.11.25:8082/OT_INTEGRATION/service/api/saveresult/");

					                // Create a connection object
					                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					                
					                // Set the request method to POST
					                connection.setRequestMethod("POST");
					                
					                // Set request headers if needed
					                connection.setRequestProperty("Content-Type", "application/json");
					              //  connection.setRequestProperty("Accept", "application/json");

					                // Enable input and output streams
					                connection.setDoOutput(true);
					                System.out.println(connection.getURL());
					                // Create the JSON payload
					                String jsonInputString = "{\r\n"
					                		+ "    \"patient_id\": \"1212\",\r\n"
					                		+ "    \"param_id\": \"150356\",\r\n"
					                		+ "    \"param_name\": \"MDC_TEMP_AWAY\",\r\n"
					                		+ "    \"param_value\": \"99.0\",\r\n"
					                		+ "    \"param_unit\": \"266560^MDC_DIM_FAHR^MDC\",\r\n"
					                		+ "    \"param_referenceRange\": \"\",\r\n"
					                		+ "    \"timestamp\": \"20240813160323.0000+0530\"\r\n"
					                		+ "}";
					                
					                // Write the JSON payload to the output stream
					                try (OutputStream os = connection.getOutputStream()) {
					                    byte[] input1 = jsonString.getBytes("UTF-8");
					                    os.write(input1, 0, input1.length);
					                }

					                // Get the response code
					                int responseCode = connection.getResponseCode();
					                connection.getResponseMessage();
					                saveToFile("URL : " + connection.getURL(),LOG_FILE_NAME);
					                System.out.println("Response Code: " + responseCode +connection.getResponseMessage());
					                saveToFile("Response Code: " + responseCode +connection.getResponseMessage(),LOG_FILE_NAME);
					                // Handle the response (not shown in this example)

					            } catch (Exception e) {
					                e.printStackTrace();
					            }
*/					            
					            
					            //end api call
					            
					            
					            
					            
					            
					            
					            
					            try {
					                workbook.close();
					            } catch (IOException e) {
					                e.printStackTrace();
					            }
						//}
						       }
						
					
}
			}	
          } catch (Exception e) {
            e.printStackTrace();
        }
    
        
        
        
	}
        
	static void saveToFile(String data, String jsonFileName) {
	       try (FileWriter fw = new FileWriter(jsonFileName, true);
	            BufferedWriter bw = new BufferedWriter(fw);
	            PrintWriter out = new PrintWriter(bw)) {
	           String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	           out.println("Reecived << "+timestamp + " - " + data);
	       } catch (IOException e) {
	           System.err.println("Error writing to file: " + e.getMessage());
	       }
	   } 
       
}
