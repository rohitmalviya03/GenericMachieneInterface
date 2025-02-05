package server;

//server.java bkp




import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import server.ABC;

public class Manglagiri {
	static String text = "";
	static List frameList = new ArrayList<>();
	static List query_Packet_List = new ArrayList(); // List of all collected Query Packets
	static List order_Packet_List = new ArrayList();
	static StringBuffer order_packet_buffer = new StringBuffer();
	// static int frame_list_counter = 0;
	static int status = 0;
	static int sam_count = 0;
	// static StringBuffer frame1 = new StringBuffer();
	// static StringBuffer frame2 = new StringBuffer();;
	static StringBuffer cobas;
	static StringBuffer host;
	static StringBuffer mode;
	static StringBuffer sampleNo;
	static StringBuffer Query_Message_Id;
	static int counterPID_Packet;
	static int p_packet_count = 1;
	static StringBuffer Query_Message_sender;
	static StringBuffer Query_Message_receiver;
	static StringBuffer Query_Message_date_time;
	static StringBuffer Test_Code;
	static StringBuffer packet = new StringBuffer("741^50076^1^^S1^SC");
	static int StxCounter = 1;
	static StringBuffer newS = new StringBuffer();
	static List testCode = new ArrayList();
	static List<String> Sample_code = new ArrayList<String>();
	static String headerVal = null;
	static int count_ack_BA400_s = 0;

	static List testCode_mapList = new ArrayList();
	static char stx;
	int port;
	static String name_pat = "";
	static String name_pat_last = "";
	static String name_pat_first = "";
	static int count_ack_BA400_o1 = 0;
	static int p = 0;
	static Map<String, List> mp = new HashMap();  	
	static StringBuffer Query_String = new StringBuffer();
	ServerSocket server = null;
	Socket client = null;
	ExecutorService pool = null;
	int clientcount = 0;
	static int Query_Counter = 0;

	static Map res = ReadPropertyFile.getPropertyValues();
	static String formatid = (String) res.get("formatid");
	static String server_port = (String) res.get("serverport");
	static Path path1;
	static String currentDirectory;
	static String path_HIMS_LOG = "";
	static String path_MachineData = "";

	static int server_port1 = 54000; //Integer.parseInt((String) res.get("server_port")); //10002	;//Integer.parseInt((String) res.get("serverport"));
	static String[] argument;

	static List frameList1 = new ArrayList<>();
	static List frameList2 = new ArrayList<>();
	static List frameList3 = new ArrayList<>();
	static List frameList3_1 = new ArrayList<>();
	static List frameList4 = new ArrayList<>();
	static List main_frameList = new ArrayList<>();

	public static void main(String[] args) throws IOException {
		
		System.out.println(server_port);
		argument = args;
		Path currentRelativePath = Paths.get("");
		currentDirectory = currentRelativePath.toAbsolutePath().toString();
		System.out.println("Current absolute path is: " + currentDirectory);
		path_HIMS_LOG = currentDirectory + "\\HIMS_log.txt";
		path_MachineData = currentDirectory + "\\Machine_log.txt";
		try {
			File myObj = new File(path_HIMS_LOG);

			if (myObj.createNewFile()) {
				System.out.println("File created: " + myObj.getName());
				FileWriter fw = new FileWriter(path_HIMS_LOG);
				fw.write("");
				fw.close();
			} else {
				System.out.println("File already exists.");
				FileWriter fw = new FileWriter(path_HIMS_LOG, false);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("");
				bw.close();
			}
		} catch (IOException e1) {
			System.out.println("An error occurred.");
			e1.printStackTrace();

		}

		try {
			File myObj = new File(path_MachineData);

			if (myObj.createNewFile()) {
				System.out.println("File created: " + myObj.getName());
				FileWriter fw = new FileWriter(path_MachineData);
				fw.write("");
				fw.close();
			} else {
				System.out.println("File already exists.");
				FileWriter fw = new FileWriter(path_MachineData, false);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("");
				bw.close();
			}
		} catch (IOException e1) {
			System.out.println("An error occurred.");
			e1.printStackTrace();

		}
		
		System.out.println("Format ID==:" + formatid);

		Manglagiri serverobj = new Manglagiri(server_port1);
		serverobj.startServer();
	}

	Manglagiri(int port) {
		this.port = port;
		pool = Executors.newFixedThreadPool(500);
	}

	public void startServer() throws IOException {
	
		server = new ServerSocket(server_port1);
		System.out.println("SERVER BOOTED ON PORT: " + this.port);
		System.out.println("ANY CLIENT CAN STOP THE SERVER BY SENDING -1"); // Server Started On Port --------po

		while (true) {
			
			client = server.accept();
			clientcount++;
			ServerThread runnable = new ServerThread(client, clientcount, this);

			pool.execute(runnable);

		}

	}

	private static class ServerThread implements Runnable {

		Manglagiri server = null;
		Socket client = null;
		BufferedReader cin;
		PrintStream cout;
		Scanner sc = new Scanner(System.in);
		int id;
		char s;
		String si;
		private String modifiedTimeStr;
		private String msgcontrolID;
		private String timeZoneMachine;
		private String MSHsegment;
		private String pidmsg;
		private String[] sampleNOblock;
		private String sampleNOblock2;
		private String sampleNO;
		private String rackNo;
		private String SEC2val;
		private String RackSeq;
		private List testValue;

		ServerThread(Socket client, int count, Manglagiri serverR) throws IOException {

			this.client = client;
			this.server = serverR;
			this.id = count;
			System.out.println("CONNECTION " + id + " ESTABLISHED WITH CLIENT " + client);

			//System.out.println("test-----------HORRIBA");
		

			if (this.client != null) {
				
				String connSucces = "Connection Established successful";
				System.out.println("connected on  ....." + this.client);
				
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
				LocalDateTime now = LocalDateTime.now();

				FileWriter fw11 = new FileWriter(path_MachineData, true);
				BufferedWriter bw = new BufferedWriter(fw11);

				bw.write("connection :" + connSucces);
				bw.write("\n");
				bw.write("ip_port------- :" + this.client);
				bw.write("\n");
				bw.write("Date and Time :" + now);
				bw.write("\n");
				
				bw.flush();
				bw.close();

			} else {

				String connFailure = "Connection not able to established";
				String fileName = "C:\\TcpFiles\\property\\data_Out1.txt";
				FileWriter fw11 = new FileWriter(path_MachineData, true);
				BufferedWriter bw = new BufferedWriter(fw11);
				
				bw.write("connection :" + connFailure);
				bw.flush();
				bw.close();

			}

			
			cin = new BufferedReader(new InputStreamReader(client.getInputStream())); // Obj To recieve MSg from Client
			cout = new PrintStream(client.getOutputStream()); // Obj For Sending Msg to CLient

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		public void run() { /// Evertime a Client gets Connect Run method is Called

			System.gc();

			int x = 1, flag = 0, forAck = 0, status = 0;
			int checkBit = 0;
			int sendAck = 0;
			boolean out = false;
			int replyFlag = 0;
			System.out.println(client.getInetAddress());
			StringBuffer reply = new StringBuffer();
			// List<StringBuffer> frameList = new ArrayList<>();
			try {
				char eot = ''; // End-OF-Transmission Bit character
				char enq = '';
				char ack = '';
				String enn = "";
				// char ack = '';

				while (true) { 
					int order_packet_buffer_counter = 0;
					int red = -1;
					byte[] buffer = new byte[800 * 1024]; // a read buffer of 5KiB
					byte[] redData;
					StringBuilder clientData = new StringBuilder();
					String redDataText = "";

					while ((red = client.getInputStream().read(buffer)) > -1) // **Code Starts For Receiving Client
																				// Messages
					{					
					
						redData = new byte[red];
					
						System.arraycopy(buffer, 0, redData, 0, red);
						
						redDataText = new String(redData, "UTF-8"); // assumption that client sends data UTF-8 encoded
						System.out.println("MESSAGE PART RECIEVED:- " + redDataText);

						// ---------------------------------------

						FileWriter fw11 = new FileWriter(path_MachineData, true);
						BufferedWriter bw = new BufferedWriter(fw11);

						bw.write(" MESSAGE PART RECIEVED:-  :" + redDataText);
						bw.write("\n");
						bw.flush();
						bw.close();
						// added by
						String ackno = "ACK Received";
						String fileName = "C:\\TcpFiles\\property\\data_Out1.txt";

						FileWriter fw2 = new FileWriter(path_MachineData, true);
						BufferedWriter bw2 = new BufferedWriter(fw2);

						bw2.write("Acknowledgment  :" + ack);
						bw2.write("\n");
						bw2.flush();
						bw2.close();

					
						 char[] character_array = redDataText.toCharArray();
			//		char[] character_array = redDataText.substring(0,redDataText.length()-1 ).toCharArray();
		
						 
						 
						 
						 
						 
						 
						 
						 //result
						 
					     	int count=0;
					        String[] segments1 = redDataText.split("\r");
					        String sampleName = null;
					       // String sampleName = null;
					     // Print each segment
					     
					        String msgtype="";
					        
					        Map<String, List> mp = new HashMap();  		//added by Rohit...
							List <String> testCode = new ArrayList<>();
							List <String> testValue = new ArrayList<>();
							List<String> sampleNo=new ArrayList<>();
					        for (String segment : segments1) {
					        // System.out.println("Segment: " + segment);
					         

					         
					         
					      	if (segment.contains("MSH")) {
					      		String[] parts = segment.split("\\|");

					      		
					      		String value=parts[8];
					      		String[] MSH = value.split("\\^");
					      	
					      		msgtype=MSH[0];
					      		System.out.println("Incoming msg Type :"+msgtype);
					      	}
					         
					         
					         
					         
					         
					        if(msgtype.equals("OUL") || segment.contains("SPM")) {
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
								if(parts[2].equals("NM") || parts[2].equals("1")) {

					             // Check if there are at least 5 elements in the array
					             if (parts.length > 3) {
					                 // Extract and print the value after the 4th pipe
					                 String value = parts[3];
					                 
					                // if(value.equals("WBC")|| value.equals("NEU#") || value.equals("LYM#") || value.equals("MON#") || value.equals("EOS#") || value.equals("BAS#") || value.equals("IMG#") || value.equals("NEU%") || value.equals("LYM%") || value.equals("MON%") || value.equals("EOS%") || value.equals("BAS%") || value.equals("IMG%") || value.equals("RBC") || value.equals("HGB") || value.equals("HCT") || value.equals("MCV") || value.equals("MCH") || value.equals("MCHC") || value.equals("RDW-CV") || value.equals("RDW-SD") || value.equals("PLT") || value.equals("MPV") || value.equals("PDW") || value.equals("PCT") || value.equals("PLCC") || value.equals("NRBC#") || value.equals("NRBC%") || value.equals("PLCR") ) {
					                  			
					                 String[] tc = value.split("\\^");
					                // System.out.println("Test Code " + tc[0]);
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
					                 
					                 continue;
					             }
					            
					    	
					    	}}
					    	
					         }
					         
					         else {
					        	 
					        	 
					        	 
					        	 
					         }
					     }
					     System.out.println("Sample No:"+sampleName);
					        System.out.println("Total Test Fetched from MSG"+count);
					      System.out.println("Total Test Entered"+testCode.size());  
					      
					      System.out.println(testValue);  
					         
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
					        }
						 
						 //result
						 
						 
					        if(msgtype.equals("QBP")) {
						 
						 String[] segments = redDataText.split("\r");
			                
			               
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
			                        	 sampleNO=sampleNOblock[0];
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
					            			if(!testCode.isEmpty()) {
					            				if(!testCode.contains(data)){
					            					testCode.add(data);
					            					
					            				}
					            				
					            			}
					            			else {
					            				testCode.add(data);
					            				}
					            			}
					            		}
					            		
					            		System.out.println("Test Code <<--- :: --->>>" +testCode);
					            		
					            		
					            		
					            		
					            		
					            		
					            		
					                }
			                 	
			                	//to handle result

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
		                	for(int k=0;k<testCode.size();k++) {
		                		//String abcc=msg.toString();
		                		
		                		msg.append("ORC|NW||||||||YYYYMMDDHHMMSSQZZ").append("\r");
		                		msg.append("TQ1|||||||||R^^HL70485").append("\r");
		                		msg.append("OBR|1|4711||"+testCode.get(k)+"^^99ROC").append("\r"); //10207 : FSH //20090 :ALB2-G
		                		msg.append("TCD|"+testCode.get(k)+"^^99ROC|1^:^"+(size-(k+1))).append("\r");

		                		
		                	}
		                	
		                	
//		                	
//		                	msg.append("ORC|NW||||||||YYYYMMDDHHMMSSQZZ").append("\r");
//		                	msg.append("TQ1|||||||||R^^HL70485").append("\r");
//		                	msg.append("OBR|1|4711||10032^^99ROC").append("\r"); //10207 : FSH //20090 :ALB2-G
//		                	msg.append("TCD|10032^^99ROC|1^:^3").append("\r");
//		                	msg.append("ORC|NW||||||||YYYYMMDDHHMMSSQZZ").append("\r");
//		                	msg.append("TQ1|||||||||R^^HL70485").append("\r");
//		                	msg.append("OBR|1|4711||10120^^99ROC").append("\r"); //10207 : FSH //20090 :ALB2-G
//		                	msg.append("TCD|10120^^99ROC|1^:^2").append("\r");
//
//		                	msg.append("ORC|NW||||||||YYYYMMDDHHMMSSQZZ").append("\r");
//		                	msg.append("TQ1|||||||||R^^HL70485").append("\r");
//		                	msg.append("OBR|1|4711||10172^^99ROC").append("\r"); //10207 : FSH //20090 :ALB2-G
//		                	msg.append("TCD|10172^^99ROC|1^:^1").append("\r");

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
					
				
					cin.close();
					client.close();
					cout.close();
				
				}
				

				
				
			} catch (IOException ex) {
				System.out.println("Error : " + ex);
			}

		}
	
	
	}
	
	
	}

	// -----------------
