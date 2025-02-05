package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class resultProcess {
	// Method to handle each client connection in its own thread
	static void handleClient(Socket clientSocket) {
		new Thread(() -> {
			try {
				InputStream input = clientSocket.getInputStream();
				OutputStream output = clientSocket.getOutputStream();

				BufferedReader reader = new BufferedReader(new InputStreamReader(input));
				PrintWriter writer = new PrintWriter(output, true);

				String line;
				StringBuilder receivedData = new StringBuilder();


				int order_packet_buffer_counter = 0;
				int red = -1;
				byte[] buffer = new byte[800 * 1024]; // a read buffer of 5KiB
				byte[] redData;
				StringBuilder clientData = new StringBuilder();
				String redDataText = "";

				while ((red = clientSocket.getInputStream().read(buffer)) > -1) // **Code Starts For Receiving Client
					// Messages
				{					

					redData = new byte[red];

					System.arraycopy(buffer, 0, redData, 0, red);

					redDataText = new String(redData, "UTF-8"); // assumption that client sends data UTF-8 encoded
					System.out.println(redDataText);



					//Stream<T> stream;


					// When data is received, log it and send back an ACK
					String receivedMessage = redDataText.toString();
					System.out.println("Received message: " + receivedMessage);
					//  AIIMSLAB.updateStatusLabel("Data received: " + receivedMessage);
					 AIIMSLAB.saveToFile("Received : "+receivedMessage, AIIMSLAB.FILE_NAME);
					// Send ACK response
					String ackResponse = AIIMSLAB.generateAckResponse(receivedMessage);
					//if(!ackResponse.equals("NO_ACK")) {
						writer.println(ackResponse);  // Send ACK response
						writer.flush();
						System.out.println("Sent ACK response: " + ackResponse);
						 AIIMSLAB.saveToFile("Sent ACK response: " + ackResponse,  AIIMSLAB.FILE_NAME);
					//}

					//Process Result



					String[] msgPart=receivedMessage.split("\r");

					String[] mshpart;


					String[] msgtypeField;
					String msgType="";

					//   AIIMSLAB.saveToFile("Recieved MSG Type : " + msgType,  AIIMSLAB.FILE_NAME);


					String[] SPMMSGFILED;

					String[] msgSection = null;
					String sampleNo="";
					String sampleType="";
					String organismType="";
					
					String micValue="";
					String[] mainresult;
					String[] antiBiotic;
					
//					for(int i=0;i<msgPart.length;i++) {
//						System.out.println("MSG SECTION:: "+msgPart[i]);
//						
//						
//						msgSection=msgPart[i].split("\\|");
//				}
					
					for(int j=0;j<msgPart.length;j++) {

						
						 msgSection=msgPart[j].split("\\|");
						
						for(int i =0;i<msgSection.length;i++) {
							if(msgSection[i].equals("MSH")) {
								msgType=msgSection[8];
								System.out.println("MSG TYPE: "+msgType);
								 AIIMSLAB.saveToFile("MSG TYPE: "+msgType,  AIIMSLAB.FILE_NAME);

							}

							else if(msgSection[i].equals("SPM")) {
								sampleNo=msgSection[3];
								sampleType=msgSection[4];
								
								System.out.println("SAMPLE NO :"+sampleNo);
								System.out.println("SAMPLLE Type"+sampleType);
								 AIIMSLAB.saveToFile("SAMPLE NO :"+sampleNo,  AIIMSLAB.FILE_NAME);
								 AIIMSLAB.saveToFile("SAMPLLE Type"+sampleType,  AIIMSLAB.FILE_NAME);

							}

							else if(msgSection[i].equals("OBX")) {
								if(msgSection[1].equals("1")) {
								organismType=msgSection[5];
								System.out.println("Organism type :"+organismType);
								 AIIMSLAB.saveToFile("Organism type :"+organismType ,  AIIMSLAB.FILE_NAME);
								}
								else {
									
									micValue=msgSection[5];
									System.out.println("MIC Value :"+micValue);
									 AIIMSLAB.saveToFile("MIC Value :"+micValue,  AIIMSLAB.FILE_NAME);
								}
								
								if(Integer.parseInt(msgSection[1])>2) {
								mainresult=msgSection[8].split("\\^");
									antiBiotic=msgSection[3].split("\\^");
								System.out.println("antiBiotic LIS CODE :"+antiBiotic[0]);
								 AIIMSLAB.saveToFile("antiBiotic :"+antiBiotic[0],  AIIMSLAB.FILE_NAME);
								System.out.println("antiBiotic Name :"+antiBiotic[1]);
								 AIIMSLAB.saveToFile("antiBiotic :"+antiBiotic[1],  AIIMSLAB.FILE_NAME);
								System.out.println("Result  :"+mainresult[0] +" :: "+mainresult[1] );
								 AIIMSLAB.saveToFile("mainresult :"+mainresult,  AIIMSLAB.FILE_NAME);
							
								}
								
							}
						}
						/*	if(msgSection[0].equals("MSH")) {
							msgType=msgSection[8];
							System.out.println("MSG TYPE: "+msgType);

						}

						else if(msgSection[0].equals("SPM")) {
							sampleNo=msgSection[3];
							sampleNo=msgSection[4];
							
							System.out.println(sampleNo);
							System.out.println(sampleNo);

						}

						else if(msgSection[0].equals("OBX")) {
							
							sampleType=msgSection[5];
							System.out.println(sampleType);

						}

*/

					}




					//end Resullt Process














				}

			} catch (IOException e) {
				AIIMSLAB.updateStatusLabel("Error handling client: " + e.getMessage());
				    AIIMSLAB.saveToFile("Stack Trace: " +  AIIMSLAB.getStackTraceAsString(e),  AIIMSLAB.FILE_NAME);
					
				e.printStackTrace();
			} 
			
			
			/* finally {
				try {
					clientSocket.close();  // Close the client socket after communication
					updateConnectionStatusLabel("Client disconnected.");
				} catch (IOException ex) {
					 AIIMSLAB.updateStatusLabel("Error closing client socket: " + ex.getMessage());
				}
			}*/
		}).start();
	}
}
