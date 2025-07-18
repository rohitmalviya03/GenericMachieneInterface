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
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
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
public class ABGDELHI {

	private static final char START_BLOCK = 0x0B; // <VT> (0B in HEX)
	private static final char END_BLOCK_1 = 0x1C; // <FS> (1C in HEX)
	private static final char END_BLOCK_2 = 0x0D; // <CR> (0D in HEX)
	static StringBuffer order_packet_buffer = new StringBuffer();

	static Map res = ReadPropertyFile.getPropertyValues();
	//static String server_ip = (String) res.get("server_ip");
	//static String server_port = (String) res.get("server_port");
	//  private static final String FILE_NAME = "c:/TcpFiles/property/monitor_data.txt"; // File to save the data
	////  private static final String PARAM_FILE_NAME="c:/TcpFiles/property/monitor_param_data.xlsx";
	private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static void cllientConnect(String serverip, String portNumber) {
		//    String hostname = server_ip;
		int port =Integer.parseInt(portNumber);






		//  String serverAddress = server_ip; // Replace with your server's IP address
		//   int serverPort = 12345; // Replace with your server's port number

		try {
			// Connect to the server
			Socket socket = new Socket(serverip, port);
			dbConnection objDao = new dbConnection();

			System.out.println("Connected to server");
			//GenericServer.logMessage("Connected to server On : "+serverip+" : "+portNumber, Color.BLACK);
			System.out.println("Server Ip :"+serverip);
			System.out.println("Port :"+portNumber);

			GenericServer.saveToFile("Connected to server On : "+serverip+" : "+portNumber, GenericServer.FILE_NAME);
			GenericServer.saveToFile("Server Ip :"+serverip, GenericServer.FILE_NAME);
			GenericServer.saveToFile("Port :"+portNumber, GenericServer.FILE_NAME);
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


			char crnull= 0x00;

			char eot = ''; // End-OF-Transmission Bit character
			char enq = '';
			char ack = '';
			char stx = '';
			char etx = '';
			char etb = '';
			char cr = 13;
			char lf = 10;

			while ((red = socket.getInputStream().read(buffer)) > -1) // **Code Starts For Receiving Client
				// Messages
			{
				GenericServer.saveToFile("ABG SOLUTION  ",GenericServer.FILE_NAME);

				//char crChar=0x0D;   //CR
				String sampleno="";
				List testCode = new ArrayList<>();
				List testValue = new ArrayList<>();
				redData = new byte[red];

				System.arraycopy(buffer, 0, redData, 0, red);

				redDataText = new String(redData,"UTF-8"); 
				//System.out.println("Data  RECIEVED  22:- " + redDataText);
				//redDataText= redDataText.replaceAll("\\s", "");
				//redDataText= redDataText.replaceAll(Character.toString((char) 0x00), "");
				char[] character_array = redDataText.toCharArray();

				String[] datapacket = redDataText.split(Character.toString(crChar));

				//System.out.println(datapacket[0]);



				if(character_array.length == 1) {


					if (character_array[0] == enq) {
						System.out.println("Data Recived "+redDataText);
						//System.out.println(redDataText);
						GenericServer.saveToFile(redDataText,GenericServer.FILE_NAME);

						output.write("");
						output.flush();
						System.out.println("ACK SENT: " + ack);
						GenericServer.saveToFile("ACK SENT: " + ack,GenericServer.FILE_NAME);



					} else if (character_array[0] == ack) {
						System.out.println("Data Recived "+redDataText);
						//System.out.println(redDataText);
						GenericServer.saveToFile(redDataText,GenericServer.FILE_NAME);

						output.write("");
						output.flush();
						System.out.println("ACK SENT: " + ack);
						GenericServer.saveToFile("ACK SENT: " + ack,GenericServer.FILE_NAME);
					}

					else if (character_array[0] == ack) {
						System.out.println("Data Recived "+redDataText);
						//System.out.println(redDataText);
						GenericServer.saveToFile(redDataText,GenericServer.FILE_NAME);

						output.write("");
						output.flush();
						System.out.println("ACK SENT: " + ack);
						GenericServer.saveToFile("ACK SENT: " + ack,GenericServer.FILE_NAME);



					}


				}
				else {
					System.out.println("Data Recived "+redDataText);
					//System.out.println(redDataText);
					GenericServer.saveToFile(redDataText,GenericServer.FILE_NAME);
					output.write("");
					output.flush();
					System.out.println("ACK SENT: " + ack);
					GenericServer.saveToFile("ACK SENT: " + ack,GenericServer.FILE_NAME);


					order_packet_buffer.append(redDataText);


				}


				if(character_array[0] == eot) {

					System.out.println("Data Recived "+redDataText);
					//System.out.println(redDataText);
					GenericServer.saveToFile(redDataText,GenericServer.FILE_NAME);
					output.write("");
					output.flush();
					System.out.println("ACK SENT: " + ack);
					GenericServer.saveToFile("ACK SENT: " + ack,GenericServer.FILE_NAME);

					//	System.out.println("Final Data :: "+order_packet_buffer);
					String finalData=filterData(order_packet_buffer.toString());
					order_packet_buffer =new StringBuffer();
					System.out.println("Final Data after remoce etb strx :: "+finalData);



					String[] dataSegmetns =finalData.split(Character.toString(cr));

					for(String str:dataSegmetns) {


						str=	str.replaceAll("\n", "");
						str=	str.replaceAll("\\^", "");

						String[] packetSegment =str.split("\\|");

						if(packetSegment[0].equals("R")) {

							//;;String[] tc=packetSegment[2];//.split("\\^");
							testCode.add(packetSegment[2]);
							testValue.add(packetSegment[3]);

						}
						else if(packetSegment[0].equals("P")) {
							if(packetSegment[0].length()>2) {
								sampleno=packetSegment[3];
							}
							else {
								sampleno="--";
							}
						}
						else if(packetSegment[0].equals("O")) {


						}



					}
					System.out.println(testCode);
					System.out.println(testValue);
					GenericServer.saveToFile("TestCode details: " + testCode,GenericServer.FILE_NAME);
					GenericServer.saveToFile("Testvalue details: " + testValue,GenericServer.FILE_NAME);

					objDao.insertParaValue(testCode,testValue,sampleno);
					sampleno="";
					testCode=null;
					testValue=null;
					System.out.println("No Exception");





				}






				/*

					try {
						for(int i=3;i<datapacket.length-4;i++) {

							String[] strData=datapacket[i].split("\\,");
							strData[0] = strData[0].replace("\n", "").replace("\r", "");

							testCode.add(strData[0]);
							testValue.add(strData[1]);



						}

						System.out.println("testCode"+testCode);
						System.out.println("testValue"+testValue);


						System.out.println(testCode.size());
						System.out.println(testValue.size());
						GenericServer.saveToFile("testCode  :"+testCode,GenericServer.FILE_NAME);
						GenericServer.saveToFile("testValue  :"+testValue,GenericServer.FILE_NAME);


					}
					catch (Exception e) {
						e.printStackTrace();
						// TODO: handle exception
					}
				 */

			}

		} catch (Exception e) {
			e.printStackTrace();

			GenericServer.saveToFile("TestCode details: " + getStackTraceAsString(e),GenericServer.FILE_NAME);

		}




	}

	private static String getStackTraceAsString(Exception e) {
		// TODO Auto-generated method stub
		return null;
	}

	private static void saveToFile(String data) {
		try (FileWriter fw = new FileWriter(GenericServer.FILE_NAME, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			//out.println("Reecived << "+timestamp + " - " + data);
		} catch (IOException e) {
			System.err.println("Error writing to file: " + e.getMessage());
		}
	} 



	public static String filterData(String text) {
		int count = 0;
		char[] ch = text.toCharArray();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < ch.length; i++) {

			if (Character.isDigit(ch[i])) {
				/*
				 * System.out.println("Character "+ch[i]); int a =
				 * Character.getNumericValue(ch[i]); System.out.println("Numeric Value "+a);
				 * if(a==1) { System.out.println("inside ifffffff"); out = true; break; }
				 */
			}
			if (ch[i] == 5) {
				// forAck = 1;
			}
			if (ch[i] == 4)// check EOT bit and out from loop
			{
				// flag=1;
				break;
			}
			if (ch[i] == 23) {
				count = i + 6;
				i = count;
			} else {
				count = 0;

				buf.append(ch[i]);

			}
		}
		return buf.toString();
	}

}
