package Server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fazecast.jSerialComm.SerialPort;

public class AdviaNew {

    private static List<String> resultPacketList = new ArrayList();
	private static String sampleNo;
	private static String tcc;
	private static int nakCount=0;
	private static String lastpkttobeSent;;


	public static void main(String[] args) {
		

		Map res = ReadPropertyFile.getPropertyValues();
        String portName =     (String) res.get("portname");
        
      //  static String portname = (String) res.get("portname");
    	
        char cr = 13;
		char lf = 10;
		char etx='';
		
		char stx='';
		char NAK='';
        SerialPort serialPort = SerialPort.getCommPort(portName);
        serialPort.setBaudRate(9600);
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 500, 0);
        System.out.println("***********Advia 2120 Solution*************");
        System.out.println("Port "+portName+" open successfully...");
        while(true) {
        if (serialPort.openPort()) {
            try {
                // Send initialize packet
               // String initializePacket = "dfbcvb";
                
                OutputStream outputStream = serialPort.getOutputStream();

                // Data to be sent
               // String dataToSend = "30hI25"+cr+lf+"";//stx+"0I 5e"+etx;//;"30hI73"+"";//"PS          ";// LIS green with this I pkt
                
                //String dataToSend = "0I "+cr+lf+"5E"+"";    //new initilization data packet Hexacode
              
                String dataToSend = "0I "+cr+lf+"^"+"";    //  new initilization data packet with charcter
                
               
                String newPacketLRC = "";
                
                
                // Send the data
                outputStream.write(dataToSend.getBytes());
                outputStream.flush();
                System.out.println("Communication Started.....Initlization packet Sent");
                System.out.println(dataToSend);
                
//              serialPort.getOutputStream().write(initializePacket.getBytes());

                // Read the response
                byte[] buffer = new byte[2048];
                int len;
                StringBuilder response = new StringBuilder();
                StringBuilder receivedData = new StringBuilder();
                StringBuilder resultPacket = new StringBuilder();
                
                // Create a thread to continuously read data from the port
                StringBuffer databuffer= new StringBuffer();
                String SampleNo=null;
                List<String> list = new ArrayList<String>();
                
                Map<String, List> mp = new HashMap();  //added by Rohit...
        		List <String> testCode = new ArrayList<>();
        		List <String> testValue = new ArrayList<>();
        		List <String> testCodeupdated = new ArrayList<>();
        		List <String> testValueupdated = new ArrayList<>();

                while (true) {
                
                	
                	 int numRead = serialPort.readBytes(buffer, buffer.length);
                     
                     if (numRead > 0) {
                         for (int i = 0; i < numRead; i++) {
                             char c = (char) buffer[i];
                             
                             //System.out.println("recieved char :"+c);
                             
                             
                             receivedData.append(c);
                             
                             char nextCh=(char) buffer[i+1];
                             byte[] buffer1 = new byte[1024];
                            
                             long bytesToWrite=10;
                             byte[] ackBytes = { 0x06 } ; // ASCII code for ACK character
                         	
                          //   serialPort.writeBytes(dataToSend.getBytes(), dataToSend.length()); 
                             
                        //     databuffer.toString().split("");
                          // Assuming port is your port object
                            // serialPort.writeBytes(ackBytes, ackBytes.length); 
 							//port.writeBytes(buffer1, bytesToWrite);
                             
                             
//                             if (c != stx) {
//                            	 
//                            	 databuffer.append(c); 
//                             }
//                             System.out.println("databuffer"+databuffer);
                             
//                            if(c=='0') {
//                            	String s="1S          "+cr+lf;
//                            	char lrc = calculateLRC(s);
//                            	String str="1S          "+cr+lf+lrc+"";
//                            	
//                            	 outputStream.write(str.getBytes());
//                                 outputStream.flush();
//                                // System.out.println("data sent after reciving first 0  "+str);
//                            	 
//                            }
//                            
//                            if(c==0) {
//                            	
//                            	String s="1S          "+cr+lf;
//                            	char lrc = calculateLRC(s);
//                            	String str="1S          "+cr+lf+lrc+"";
//                            	
//                            	 outputStream.write(str.getBytes());
//                                 outputStream.flush();
//                                // System.out.println("data sent after reciving first 0   "+str);
//                            	 
//                            }
                             
                             if(receivedData.toString().length()== 1 && receivedData.toString().equals("0")) {
                           	  System.out.println("Data Recieved From Machine ::  "+receivedData.toString());
            			     	String s="1S          "+cr+lf;
            			     	char lrc = calculateLRC(s);
            			     	String str="1S          "+cr+lf+lrc+"";
            			     	
            			     	 outputStream.write(str.getBytes());
            			          outputStream.flush();
            			          System.out.println("data sent after reciving first 0   "+str);
            			         receivedData.setLength(0);
            			     	 
                           	 
                           	 
                            }
                            
                            if(receivedData.toString().length()== 1 && receivedData.toString().equals(0)) {
                           	  System.out.println("Data Recieved From Machine ::  "+receivedData.toString());
             			     	String s="1S          "+cr+lf;
             			     	char lrc = calculateLRC(s);
             			     	String str="1S          "+cr+lf+lrc+"";
             			     	
             			     	 outputStream.write(str.getBytes());
             			          outputStream.flush();
             			          System.out.println("data sent after reciving first 0   "+str);
             			          receivedData.setLength(0);
             			     	 
                            	 
                            	 
                             }
                            
                            else if(receivedData.toString().length()== 1 && receivedData.toString().equals("") && nakCount<1) {  //NAK CHECK
                           	// System.out.println("Data Recieved from machine : "+ receivedData.toString());
                           	 
                           	 nakCount++;
                           	 
                           	 
                           	  System.out.println("Data Recieved From Machine ::  "+receivedData.toString());
             			     	String s="1S          "+cr+lf;
             			     	char lrc = calculateLRC(s);
             			     	String str="1S          "+cr+lf+lrc+"";
             			     	
             			     	 outputStream.write(lastpkttobeSent.getBytes());
             			          outputStream.flush();
             			          System.out.println("data sent after reciving NAK   "+lastpkttobeSent);
             			          receivedData.setLength(0);
             			     	 
                            	 
                            	 
                             }
                            
                            else if(receivedData.toString().equals("") && nakCount==1) {  // if Same msg reject twice we have to sent I packet to reinitilize message.
                           	 
                           	   outputStream.write(dataToSend.getBytes());
                                  outputStream.flush();
                                  System.out.println("Again NAK recieved I Packet Sent");
                                  System.out.println(dataToSend);
                                  nakCount=0;
                                  receivedData.setLength(0);
                            }
                            
                            
                             
                             
                             
                             

//                             	}
	                             	
	                             	 else if (c == etx) {
	                            	 receivedData.append(etx);
	                            	 System.out.println("::::::::Data Received from Machine:::::: " );
	                            	 System.out.println(receivedData.toString());
	                            	 
                            	  int indexofStx=receivedData.toString().indexOf(stx);
	                            	 char[] character_array = receivedData.toString().toCharArray();
	                            	 if(character_array[indexofStx+2] =='R') {
	                            		 resultPacket.append(receivedData.toString());
	                            		 resultPacketList.add(receivedData.toString());
                            		 char ch=character_array[indexofStx+1];
                            		 
                            		// int intValue = Character.getNumericValue(ch);
                            		 char na=(char) (ch+1);
                            		 
                            		// System.out.println("character_array[2]"+ na);
                            		// String str = String.valueOf(na);
                            		 String str1=na+"Z                  0"+cr+lf;
                            		 String Zpkt=""+na+"Z                  0"+cr+lf;
                            		 
                            		 //String Zpkt=""+na+"Z          "+cr+lf;
                            		 //Zpkt=""+na+"Z          "+cr+lf+lrc+etx;
                            		 char lrc = calculateLRC(str1);
                            		 Zpkt=ch+""+na+"Z                  0"+cr+lf+lrc+etx;
                            		 
                            		 lastpkttobeSent=Zpkt;
//                            		 serialPort.writeBytes(Zpkt.getBytes(), Zpkt.length()); 
//                            		 outputStream.write(ch);
//                                     outputStream.flush();
//                                     
                                     outputStream.write(Zpkt.getBytes());
                                     outputStream.flush();
                                     System.out.println("Data Sent after result captured   "+ch+"\n"+Zpkt);                                     
                            		 
                                     
                                   //  receivedData.setLength(0); 
                                     
                                     
                                     
                                     //pareing start
                                     
                                     
                                // System.out.println("RSS"+resultPacketList.size());
                                    	
                                     
                                     String str=receivedData.toString();//resultPacketList.get(ii);
                                    		 
                                    		  int indexofStx1=str.toString().indexOf(stx);
                                    		  String sampleName="";
                                    		 String[] resultSplit= str.split(" ");
                                    		 
                                    		 sampleName=resultSplit[1].replaceAll("0000", "");
                                    		 
                                    		 System.out.println("sample NO:"+sampleName);
                                    		 

                                    		  receivedData.setLength(0); 

                                    	        	
                                    	        	//appendRecordToFile(sampleName);
                                    	           // System.out.println(sampleName+" is not present in the log file.");
                                    	            // Perform alternative action or exit
                                    	        
                                    		   

                                    		 
                                    		 
                                    		 resultSplit= str.split(Character.toString(cr)+Character.toString(lf));
                                    		 String testresults=resultSplit[1];
                                    		 
                                    		 String[] testresultsData=testresults.split("  ");
                                    		 
                                    		 
                                    		 
                                    		  ArrayList<String> substrings = new ArrayList<>();
                                    	        int length = testresults.length();
                                    	        
                                    	        for (int n = 0; n < length; n += 9) {
                                    	            substrings.add(testresults.substring(n, Math.min(length, n + 9)).trim());
                                    	        }
                                    	        
                                    	        
                                    	        System.out.println("LIST   :"+substrings);
                                    	        System.out.println("substrings size   :"+substrings.size());
                                    	        
                                    	        
                                    	        
                                    	        for(int k=0;k<substrings.size();k++){  //28
                                    	        	
                                    	        	
                                    	        	
                                    	        	
                                    	        	String segment=substrings.get(k);
                                    	        	
                                    	        	if(!segment.contains(" ")) {
                                    	        		
                                    	        		int ind=substrings.indexOf(segment);
                                    	        		
                                    	        		//System.out.println("index of stirng:"+ind); 
                                    	        		
                                    	        		if(ind<9){
                                    	        			//System.out.println("ROHT"+segment.substring(0,1)); 
                                    	        			
                                    	        			if(testCode.isEmpty() && ind==0) {
                                    	        				testCode.add(segment.substring(0,1));
                                        	        			testValue.add(segment.substring(1));
                                    	        			}
                                    	        			if(!testCode.isEmpty() && !testCode.contains(segment.substring(0,1))) {
                                    	        				testCode.add(segment.substring(0,1));
                                        	        			testValue.add(segment.substring(1));
                                    	        			}
                                    	        			
                                    	        		}
                                    	        		else{
                                    	        			
                                    	        			segment.substring(0,2);
                                    	        			if(!testCode.isEmpty()&&!testCode.contains(segment.substring(0,1))) {
                                    	        			testCode.add(segment.substring(0,2));
                                    	        			testValue.add(segment.substring(2));
                                    	        			}
                                    	        		}
                                    	        
                                    	        	}
                                    	        	else {
                                    	        		testCode.add(segment.substring(0,2));
                                	        			testValue.add(segment.substring(2));
                                    	        		
                                    	        	}
                                    	        	
                                    	        	}
                                    	        

                                    	      receivedData.setLength(0); 
                                    		 System.out.println("Sample NO "+sampleName);
                                    		 System.out.println("TestCode "+testCode);
                                    		 
                                    		 System.out.println("TestValue "+testValue);
                                    		 
                                    		 System.out.println("TestCode "+testCode.size());
                                    		 
                                    		 System.out.println("TestValue "+testValue.size());
                                    		 
                                    		 int size=testValue.size();
                                    		 
                                    		 
                                    		 System.out.println("Testcode   :  Testvalue");
                                    		 for(int m=0;m<testCode.size();m++) {
                                    			 
                                    			 System.out.println(testCode.get(m)+"       :        "+testValue.get(m));
                                    			// System.out.println(testValue.get(i));
                                    		 }
//                                    		 System.out.println("TestCode Size : "+testCode.size());
//                                    		 System.out.println("TestValue Size ."+testValue.size());
                                    		 mp.put("TestCode",testCode);
                                    		 mp.put("TestValue",testValue);
                                    		 
                                    		
                                    		 
                                    		 //test code and test vale parsing start 
                                    		 

                                    		 
                                    		 
                                    		 //end parsing
                                    		 ABCbkp abc = new ABCbkp();
                                    		 
                                    		 abc.insert_SysmexXN350(mp,sampleName);   //type 5
                                    		 //resultPacketList.remove(i);
                                    		 testCode.clear();
                                    		 testValue.clear();
                                    		 
                                    	 }
	                            	 
	                            	 
                                    // }
                                     
                                     
                                     
                            	 //}
                            	 
                            	 else {
                            	 
                            	 try {
                            		 
                            		  int indexofStx1=receivedData.toString().indexOf(stx);
                            	 char indexChar = receivedData.charAt(indexofStx1+1);
                            	// System.out.println("indexChar"+indexChar);
                            	 String msg= processMachineData(Character.toString(indexChar));
//                               // Calculate LRC for the entire string
//                               
    
                               	String newPacket=msg.charAt(0)+"S          "+cr+lf;
                               
                              	char lrc = calculateLRC(newPacket);
                                newPacketLRC=indexChar+""+newPacket+lrc+"";
                                //outputStream.write(indexChar);
                               // outputStream.flush();
                                outputStream.write(newPacketLRC.getBytes());
                                outputStream.flush();
                                //System.out.println("Data Sent   "+indexChar);
                                System.out.println("Server ::   "+newPacketLRC);
                                
                                lastpkttobeSent=newPacketLRC;
                               //  databuffer.append(receivedData.toString());
                             
                                receivedData.setLength(0); // Clear the buffer
                                 
                                 
                                String data= receivedData.toString();
                                
                            	 }
                            	 
                            	 catch (Exception e) {
                                     System.err.println("An error occurred: " + e.getMessage());
                                     outputStream.write(dataToSend.getBytes());
                                     outputStream.flush();
                                    
                                    
                                 }
                            	 
                            	 }
                             
                             } else {
                                // receivedData.append(c);
                             }
                             
                         }
                         
                         
                       //  System.out.println("Data Buffer ::"+databuffer);
//                    if (serialPort.bytesAvailable() > 0) {
//                        // Read data from the serial port
//                       // byte[] buffer = new byte[1024];
//                        int bytesRead = serialPort.getInputStream().read(buffer);
//
//                        // Process the received data
//                      
//                        
//                        
//                        if (bytesRead > 0) {
//                            String receivedData = new String(buffer, 0, bytesRead);
//                            System.out.println("Received Data from Machine: " + receivedData);
//                           
//                            
//                            
//                            char indexChar = receivedData.charAt(1);
//                           // char resultChar = 0;
//                           
//                            
//                            
//                            //getMsgFormat(indexChar);
//                           // outputStream.write(sToken.getBytes());
//                            //outputStream.flush();
//                            //System.out.println("Send data to machine successss"+sToken);
//                         // System.out.println(";;;"+resultChar);
//                            String msg= processMachineData(Character.toString(indexChar));
//                           // Calculate LRC for the entire string
//                           
//
//                           	String newPacket=msg.charAt(0)+"S          "+cr+lf;
//                           
//                           	String lrc = calculateLRC(newPacket);
//                            newPacketLRC=indexChar+""+newPacket+lrc+"";
//                           //System.out.println("Next data to be sent::"+newPacketLRC);
//                           
//                           
//                        }
                    }
                     
                     else {
                    	// System.out.println("Result Packet List "+resultPacketList);
                    	// System.out.println("Result Packet List Size "+resultPacketList.size());
                    	 //for(String str:resultPacketList) {
                    	
                     
                     }

                  //  String dataToSend1 = "LIS_COMMAND";
                   // serialPort.getOutputStream().write(newPacketLRC.getBytes());
//                    outputStream.write(newPacketLRC.getBytes());
//                    outputStream.flush();
//                    System.out.println("Data Sent   "+newPacketLRC);
                    try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Close the port when done
                serialPort.closePort();
            }
        } else {
            System.out.println("Error: Unable to open the serial port.");
        }
    }}
    
    
    
    
    public  static String processMachineData(String msg) {
    	
    //    System.out.println("Packet From Machine::::" + msg);

        char lastChar = msg.charAt(msg.length() - 1);

        // Assuming ASCII characters
        int nextCharCode = (lastChar + 1) % 128;

        if (nextCharCode > 'Z') 
        {
            nextCharCode = '0';
        }

        char nextChar = (char) nextCharCode;

      // System.out.println("nextChar"+nextChar);
        return String.valueOf(nextChar) ;
    
    }
    
    //HEX LRC
    public static String calculateLRC1(String input) {
        int lrc = 0;
        for (int i = 0; i < input.length(); i++) {
            lrc ^= input.charAt(i); // XOR operation
        }
        return Integer.toHexString(lrc).toUpperCase(); // Convert to hexadecimal and uppercase
    }
    
    //charcter LRC
    
    public static char calculateLRC(String input) {
        int lrc = 0;
        for (int i = 0; i < input.length(); i++) {
            lrc ^= input.charAt(i); // XOR operation
            
        }
       // lrc=3;
        
//        System.out.println("LRC in int" + lrc);
//        System.out.println("LRC in char" + (char) lrc);
        if(lrc==03) {
        	
        	lrc=127;
        }
        //System.out.println("lrc  "+lrc);
        return (char) lrc;
    }

  
    	public static byte calculateLRC2(byte[] bytes) {
    		byte LRC = 0;
    		for (int i = 0; i < bytes.length; i++) {
    			LRC ^= bytes[i];
    		}
    		return LRC;
    	
    	}
    
    
    public static String getMsgFormat(char chr) {
    	
    	  String startingHex =Character.toString(chr);// "30h"; // 
          // Extract the numeric part from the input string
          String numericPart = startingHex.substring(0, startingHex.length() - 1);
          
          try {
              // Convert the numeric part to decimal
              int decimalValue = Integer.parseInt(numericPart, 16);

              // Increment the decimal value
              int nextDecimalValue = decimalValue + 1;

              // Convert the incremented decimal value back to hexadecimal
              String nextHexValue = Integer.toHexString(nextDecimalValue);

              // Get the ASCII character for the incremented hexadecimal value
              char asciiCharacter = (char) nextDecimalValue;

              // Display the results
              System.out.println("Starting Hexadecimal Value: " + startingHex);
              System.out.println("Next Hexadecimal Value: " + nextHexValue);
              System.out.println("Next ASCII Character: " + asciiCharacter);
          } catch (NumberFormatException e) {
              System.out.println("Invalid input format. Please provide a hexadecimal string ending with 'h'.");
          }
          return null;
}
    
    public static void createLogFile() {
        try {
        	
        	String FILE_PATH="D://Samplerecord.txt";
            File file = new File(FILE_PATH);
            if (file.createNewFile()) {
                System.out.println("Log file created: " + FILE_PATH);
            } else {
                System.out.println("Log file already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating the log file: " + e.getMessage());
        }
    }
    
    
    public static void appendRecordToFile(String record) {
        try {
            // Open the file for appending
        	

        	String FILE_PATH="D://Samplerecord.txt";
            FileWriter fileWriter = new FileWriter(FILE_PATH, true);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            // Write the record to the file
            printWriter.println(record);

            // Close the file
            printWriter.close();
            System.out.println("Record appended successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while appending the record: " + e.getMessage());
        }
    }

    
    
    public static boolean isDataPresent(String searchData) {
        try {
            // Open the file for reading
        	
        	String FILE_PATH="D://Samplerecord.txt";
            FileReader fileReader = new FileReader(FILE_PATH);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // Read each line and check for the presence of the search data
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains(searchData)) {
                    // Close the file and return true if the data is found
                    bufferedReader.close();
                    return true;
                }
            }

            // Close the file if the data is not found
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }
        // Return false if the data is not found or an error occurs
        return false;
    }
}


