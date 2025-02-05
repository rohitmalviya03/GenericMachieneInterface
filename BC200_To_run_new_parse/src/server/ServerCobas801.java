//server.java bkp


package server;



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
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerCobas801 {
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

	static StringBuffer Query_String = new StringBuffer();
	ServerSocket server = null;
	Socket client = null;
	ExecutorService pool = null;
	int clientcount = 0;
	static int Query_Counter = 0;

//	static Map res = ReadPropertyFile_manglagiri.getPropertyValues();
	static Map res = ReadPropertyFile.getPropertyValues();
	static String formatid = (String) res.get("formatid");
	static String server_port = (String) res.get("serverport");
	static Path path1;
	static String currentDirectory;
	static String path_HIMS_LOG = "";
	static String path_MachineData = "";

	//static int server_port1 = 6000	;//Integer.parseInt((String) res.get("serverport")); 6000 and 5500 for BA400 AIIMS maglagiri
	static int server_port1 = Integer.parseInt((String) res.get("server_port"));//50001	; //C:TcpFilessys
	static String[] argument;

	static List frameList1 = new ArrayList<>();
	static List frameList2 = new ArrayList<>();
	static List frameList3 = new ArrayList<>();
	static List frameList3_1 = new ArrayList<>();
	static List frameList4 = new ArrayList<>();
	static List main_frameList = new ArrayList<>();
	private static StringBuffer strbuf;

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
		/*
		 * if (formatid.equals("")) { // Vitros 560 Vitros_client.main(argument); }
		 */
		System.out.println("Format ID==:" + formatid);
//		if (formatid.equals("20038")) { // BA400_client
//			BA400_client.main(argument);
//		}
		
		ServerCobas801 serverobj = new ServerCobas801(server_port1);
		serverobj.startServer();
	}

	ServerCobas801(int port) {
		this.port = port;
		pool = Executors.newFixedThreadPool(5);
	}

	public void startServer() throws IOException {
		// System.out.println("SERVER BOOTED ON PORT==================: "+server_port);
		// System.out.println("SERVER BOOTED ON PORT==================: "+server_port1);

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

		ServerCobas801 server = null;
		Socket client = null;
		BufferedReader cin;
		PrintStream cout;
		Scanner sc = new Scanner(System.in);
		int id;
		char s;
		String si;

		ServerThread(Socket client, int count, ServerCobas801 serverR) throws IOException {

			this.client = client;
			this.server = serverR;
			this.id = count;
			System.out.println("CONNECTION " + id + " ESTABLISHED WITH CLIENT " + client);

			//System.out.println("test-----------HORRIBA");
		

			if (this.client != null) {
				/*
				 * FileWriter fw = new FileWriter("FileName", false); PrintWriter pw = new
				 * PrintWriter(fw, false); pw.flush(); pw.close();
				 */
				String connSucces = "Connection Established successful";
				System.out.println("connected on  ....." + this.client);
				// String fileName = "C:\\TcpFiles\\property\\log.txt";

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
				//String fileName = "C:\\TcpFiles\\property\\data_Out1.txt"; //6000 port
				String fileName = "C:\\TcpFilessys\\property\\data_Out1.txt"; //5500 port
				FileWriter fw11 = new FileWriter(path_MachineData, true);
				BufferedWriter bw = new BufferedWriter(fw11);

				bw.write("connection :" + connFailure);
				bw.flush();
				bw.close();

			}

			// rahulprasad end--

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

				
				while (true) { // To keep on chatting unless anyone of client or server shuts-DOWN
					int order_packet_buffer_counter = 0;
					int red = -1;
					byte[] buffer = new byte[5 * 1024]; // a read buffer of 5KiB
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
					//	String fileName = "C:\\TcpFiles\\property\\data_Out1.txt"; //6000 port
						String fileName = "C:\\TcpFilessys\\property\\data_Out1.txt"; //5500 port

						FileWriter fw2 = new FileWriter(path_MachineData, true);
						BufferedWriter bw2 = new BufferedWriter(fw2);

						bw2.write("Acknowledgment  :" + ack);
						bw2.write("\n");
						bw2.flush();
						bw2.close();

					
						 char[] character_array = redDataText.toCharArray();
			//		char[] character_array = redDataText.substring(0,redDataText.length()-1 ).toCharArray();
		
						if (character_array.length == 1) {
							if (character_array[0] == enq) {
								cout.print(ack);
								System.out.println("Server: " + ack);

							} else if (character_array[0] == eot) {

//								cout.print(ack);
//								System.out.println("Server1: " + ack);
//								cout.print(enq);
//								System.out.println("Server1: " + enq);
								if (order_packet_buffer != null && order_packet_buffer.length() != 0) 
								{
									System.out.println("enter eot to filter data and split function");
									redDataText = filterData(order_packet_buffer.toString());
									splitFunction(redDataText); // SEND FOR SAVE  // RAMJI Enable for BA400
									order_packet_buffer.delete(0, order_packet_buffer.length());// DELETE STRINGBUFFER
									/*
									 * cout.print(ack); System.out.println("Server1: " + ack);
									 */

									order_packet_buffer_counter = 0;
								}

								// ------------------
								else if (formatid.equals("20045") ) {
									
//									if (character_array[0] == enq) {
//										cout.print(ack);
//										System.out.println("Server: " + ack);
//										
//									}
									if (character_array[0] == ack || character_array[0] == eot) {
										if (query_Packet_List.size() != 0) // In Case of ACK receive check if query is
																			// not null then parse Query
										{
											StringBuilder out1 = new StringBuilder();
											for (Object o : query_Packet_List) {
												out1.append(o.toString());
												out1.append("\t");
											}

											System.out.println(" splitFunction Alinity");

											//splitFunction(out1.toString()); // Sends Query 1 bY 1

											query_Packet_List.clear(); // Clears query list cause we received ACK i.e
																		// reply with list
											if (frameList != null && frameList.size() != 0) {
												cout.print(enq);
												System.out.println("ENQ check");
												cout.print(frameList.get(0));
												System.out.println("Server2: " + frameList.get(0));

												
												frameList.remove(0);
												
												System.out.println("LAST frame"+frameList.size());
											} else {
												cout.print(eot); // if all Query Frames has been sent then send EOT
												System.out.println("Server: " + eot);

												query_Packet_List.clear(); // Clears List to so new list can be made
											}
										} else if (frameList != null && frameList.size() != 0) {
											cout.print(frameList.get(0)); // after every query's frames are sended
																			// automatically EOT will be send
											System.out.println("Server: " + frameList.get(0));
											frameList.remove(0);
										}

										else {
											cout.print(eot);
											System.out.println("Server: " + eot);
											query_Packet_List.clear();
										}

									}
								}
								// ----------------------
								else {
									cout.print(enq);
									System.out.println("Server: " + enq);

								}
							} else if (character_array[0] == ack) {

								// ------------------------
								if (formatid.equals("20011") ||formatid.equals("20012")|| formatid.equals("20043") || formatid.equals("2001145")) { // single packet vitrose
																							// 5600
									SimpleDateFormat time_formatter2 = new SimpleDateFormat("yyyyMMddHHmmss");
									String current_time_str2 = time_formatter2.format(System.currentTimeMillis());
									System.out.println("current_time_str2---" + current_time_str2);
									// cout.print(eot);
									// System.out.println("Server: " + eot);
									if (character_array[0] == ack || character_array[0] == eot) {
										if (query_Packet_List.size() != 0) // In Case of ACK receive check if query is
																			// not null then parse Query
										{
											StringBuilder out1 = new StringBuilder();
											for (Object o : query_Packet_List) {
												out1.append(o.toString());
												out1.append("\n");
											}

										System.out.println(" after eot ===packet================" + out1.toString());

											splitFunction(out1.toString()); // Sends Query 1 bY 1
//											try {
//												this.wait(50000);
//											} catch (InterruptedException e) {
											//
//												e.printStackTrace();
//											}
//											try {
//												TimeUnit.SECONDS.sleep(5);
//											} catch (InterruptedException e) {
//												// TODO Auto-generated catch block
//												e.printStackTrace();
//											}

//											try
//											{
//											    Thread.sleep(5000);
//											}
//											catch(InterruptedException ex)
//											{
//											    Thread.currentThread().interrupt();
//											}
											query_Packet_List.clear(); // Clears query list cause we received ACK i.e
										} // reply with list
										if (frameList != null && frameList.size() != 0) {
											System.out.println("  framelist index===" + p);
											SimpleDateFormat time_formatter1 = new SimpleDateFormat("yyyyMMddHHmmss");
											String current_time_str1 = time_formatter1
													.format(System.currentTimeMillis());
											System.out.println("frameList.size():- " + frameList.size());
											System.out.println("current_time_str:- " + current_time_str1);
											if (count_ack_BA400_o1 != 0) 
											{
												if(frameList.size() > p )
												{cout.print(frameList.get(p)); }
												else
												{	System.out.println("Server: framelist index equal to frameList.size() "+p );
													break;
												}
													// after every query's frames are sended
												// automatically EOT will be send
												System.out.println("Server: " + frameList.get(p));
												// ---------------------------------------

												FileWriter fl1 = new FileWriter(path_MachineData, true);
												BufferedWriter br1 = new BufferedWriter(fl1);

												br1.write("Server: " + frameList.get(p));
												br1.write("\n");
												br1.flush();
												br1.close();
												// --------------------------------------
												SimpleDateFormat time_formatter3 = new SimpleDateFormat(
														"yyyyMMddHHmmss");
												String current_time_str3 = time_formatter3
														.format(System.currentTimeMillis());
												System.out.println("current_time_str2---" + current_time_str3);
												p = p + 1;
												count_ack_BA400_o1 = count_ack_BA400_o1 - 1;

												if (count_ack_BA400_o1 == 0) {
//													Server.testCode.remove(0);
//
													
													ServerCobas801.testCode.clear();
													ServerCobas801.testCode_mapList.clear();
													frameList.clear();
													p = 0;
													System.out.println(" clear framelist===");

												}
											}
											// cout.print(frameList.get(0));
											// System.out.println("Server2: " + frameList.get(0));

											// frameList.remove(0);
										}
//											else {
//												cout.print(eot); // if all Query Frames has been sent then send EOT
//												System.out.println("Server: " + eot);
										//
//												query_Packet_List.clear(); // Clears List to so new list can be made
//											}

//										else if (frameList != null && frameList.size() != 0) {
//											cout.print(frameList.get(0)); // after every query's frames are sended
//																			// automatically EOT will be send
//											System.out.println("Server: " + frameList.get(0));
//											frameList.remove(0);
//										}

										else {
											cout.print(eot);
											System.out.println("Server: " + eot);
											query_Packet_List.clear();
										}

									}
								}

								// ------------------------

								else if (query_Packet_List.size() != 0) // In Case of ACK receive check if query is not
																		// null
								// then parse Query
								{
									for (int i = 0; i < query_Packet_List.size(); i++) {
										splitFunction((String) query_Packet_List.get(i)); // Sends Query 1 bY 1
									}
									query_Packet_List.clear(); // Clears query list cause we received ACK i.e reply with
																// list
									if (frameList != null && frameList.size() != 0) {
										
										cout.print(frameList.get(0));
										System.out.println("Server2: " + frameList.get(0));

										frameList.remove(0);
									} else {
										cout.print(eot); // if all Query Frames has been sent then send EOT
										System.out.println("Server: " + eot);

										query_Packet_List.clear(); // Clears List to so new list can be made
									}
								} else if (frameList != null && frameList.size() != 0) {
									cout.print(frameList.get(0)); // after every query's frames are sended automatically
																	// EOT will be send
									System.out.println("Server: " + frameList.get(0));
									frameList.remove(0);
								}

								else {
									cout.print(eot);
									System.out.println("Server: " + eot);
									query_Packet_List.clear();
								}

							} else {
								cout.print(ack);
								System.out.println("Server: " + ack);
							}

						} // *************Outer IF ENDS********
						else {
							if (checkQ(redDataText) == 1) {
								// order_packet_buffer.delete(0,order_packet_buffer.length() );
								cout.print(ack); // If Query Packet Comes Send ACK UNTIL EOT COMES and ADD IN LIST
								System.out.println("Server: " + ack);
								query_Packet_List.add(redDataText);
								//splitFunction(redDataText); // SEND FOR SAVE
								
							//RamjiMask 15-Mar-2024	
								System.out.println("  query_Packet_ADDED:======== " + query_Packet_List.toString());

							}
							// --------------------------------added by vipul//(formatid.equals("20005"))
//							else if (formatid.equals("20005")) {
//								if (Character.toUpperCase(redDataText.charAt(2)) == 'H') {
//									query_Packet_List.add(redDataText);
//									System.out.println(" HEADER ADDED TO query_Packet_List: ");
//								}
//							}
////
//							else if (formatid.equals("20011")) {
//
//								if (Character.toUpperCase(redDataText.charAt(2)) == 'H') {
//									query_Packet_List.clear();
//									query_Packet_List.add(redDataText);
//									System.out.println(" HEADER ADDED TO query_Packet_List: ");
//									cout.print(ack);
//									System.out.println("Server: " + ack);
//								} else {
//									cout.print(ack); // If Query Packet Comes Send ACK UNTIL EOT COMES and ADD IN LIST
//									System.out.println("Server: " + ack);
//
//									System.out.println("  after l packet query_Packet_ADDED:======== "
//											+ query_Packet_List.toString());
//
//								}
//							}

							// ----------------------------------------added by vipul

							else {
								if (recieved_Packet(redDataText) == 1) // if client sends order packet then send ACK
								{
									query_Packet_List.clear();
									System.out.println("query_Packet_List clear: ");
									/*
									 * if(formatid.equals("20010")) { splitFunction(redDataText); }
									 */
									order_packet_buffer.append(redDataText); // Future use to store all list of
																				// Order-Packets and canbe used to send
																				// 1 by
									order_packet_buffer.append(System.getProperty("line.separator"));
									order_packet_buffer_counter = 1;
									cout.print(ack);
									System.out.println("Server: " + ack);

									if (formatid.equals("20041")) { // 20034 //20041

										if (order_packet_buffer != null && order_packet_buffer.length() != 0) {

											System.out.println("filter data and split function");
											redDataText = filterData(order_packet_buffer.toString());
											splitFunction(redDataText); // SEND FOR SAVE
											order_packet_buffer.delete(0, order_packet_buffer.length());// DELETE
																										// STRINGBUFFER
											/*
											 * cout.print(ack); System.out.println("Server1: " + ack);
											 */

											order_packet_buffer_counter = 0;
										}

									}

								}
								/*
								 * else if(recieved_Packet1(redDataText)==1) // if client sends order packet
								 * then send ACK {
								 * 
								 * order_packet_buffer.append(redDataText); // Future use to store all list of
								 * Order-Packets and canbe used to send 1 by
								 * order_packet_buffer.append(System.getProperty("line.separator"));
								 * order_packet_buffer_counter = 1; cout.print(ack);
								 * System.out.println("Server: "+ack); }
								 */
								else if (order_packet_buffer_counter == 1) // Append till EOT comes
								{
									order_packet_buffer.append(redDataText);
									order_packet_buffer.append(System.getProperty("line.separator"));
									order_packet_buffer_counter = 1;
									cout.print(ack);
									System.out.println("Server: " + ack);

								} else {
									if (Character.toUpperCase(redDataText.charAt(1)) == 'H') {
										query_Packet_List.clear();
										query_Packet_List.add(redDataText);
										System.out.println(" HEADER ADDED TO query_Packet_List: ");

									}
									cout.print(ack); // if recieved packet is not of our use then send ack
									System.out.println("Server: " + ack);
								}

							}

						}

					} // INNER WHILE LOOP ENDS
					

					if (out)
						break;

				} // OUTER WHILE LOOP ENDS
				
				cin.close();
				client.close();
				cout.close();
				if (x == 0) {
					System.out.println("Server cleaning up.");
					System.exit(0);
				}
			} catch (IOException ex) {
				System.out.println("Error : " + ex);
			}

		}
	}

	// -----------------

	public static void parse_sys(List<String> list) {

		Map<String, List> mp = new HashMap();
		List lis = new ArrayList();
		List lis1 = new ArrayList();
		String ip = "";
		StringBuffer sample = new StringBuffer();

		StringBuffer sampleName = new StringBuffer();
		StringBuffer TestCode = new StringBuffer();
		StringBuffer TestValue = new StringBuffer();
		StringBuffer TestValue1 = new StringBuffer();
		// System.out.println("size:-- "+list.size());
		String stcode;
		String sTvalue;
		ABC abc = new ABC();

		int r = 0;
		for (String line : list) {
			if (line.length() > 1) {
				if (Character.toUpperCase(line.charAt(2)) == 'O') {
					// System.out.println("
					// Character.toUpperCase(line.charAt(2))========"+Character.toUpperCase(line.charAt(2)));

					char[] ch = line.toCharArray();
					int count = 0;
					for (int i = 0; i < ch.length; i++) {

						if (ch[i] == '|')
							count++;

						if (count == 3) {
							if (!(ch[i] == '|'))
								sampleName.append(ch[i]);
						}

					}

					System.out.println("SampleID=====================:- " + sampleName.toString().replace("^", ""));

				}

				if (Character.toUpperCase(line.charAt(2)) == 'R') {
					// System.out.println("
					// Character.toUpperCase(line.charAt(2))========"+Character.toUpperCase(line.charAt(2)));

					char[] ch = line.toCharArray();
					int count = 0;
					for (int i = 0; i < ch.length; i++) {

						if (ch[i] == '|')
							count++;

						if (count == 2) {
							if (!(ch[i] == '|'))
								TestCode.append(ch[i]);
						}

						if (count == 3) {
							if (!(ch[i] == '|'))
								TestValue.append(ch[i]);
						}

						if (count == 4) {
							if (!(ch[i] == '|'))
								TestValue1.append(ch[i]);
						}

					}

					System.out.println("TestCode :- " + TestCode.toString().replace("^", ""));
					System.out.println("TestValue :- " + TestValue);
					System.out.println("TestValue1 :- " + TestValue1);

				}
			}
			if (line.length() == 0 || line == null)
				break;

			char[] ch = line.toCharArray();
			int count = 0;
			for (int i = 0; i < ch.length; i++) {
				sample.append(ch[i]);
			}

			// System.out.println("packet:- "+sample);//Sample ID Read between '|' and '^'

			abc.insert_GenExpert((TestCode.toString().replace("^", "")).replace("|", ""), TestValue.toString(),
					sampleName.toString());

		}

		// System.out.println( "log print" + list);
	}

	// -----------------------------------------------------------

	// To remove ETB STX AND CHECKSUM
	public static String filterData(String text) {
		int count = 0;
		
		System.out.println("ROHIT 10APR-- ETBSTX Changes  with 8 Character Changes");
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
			if (ch[i] == 23) 
			{
				
//			1	i++;   //ETB
//			2		i++;  //BCC1
//			3		i++;  //BCC2
//			4		i++;  //CR
//					if (ch[i] == 02) 
//					{
//						count = i + 1;
//						i = count;
//					} 
//
//			5		i++; //LF
//					if (ch[i] == 02) 
//					{
//						count = i + 1; //STX+1
//						i = count;
//					}
//
//			6		i++;
//					if (ch[i] == 02) {
//						count = i + 1;
//						i = count;
//					}
//			7		i++;
//					if (ch[i] == 02) {
//						count = i + 1;
//						i = count;
//					}
//			8		i++;
//					if (ch[i] == 02) {
//						count = i + 1;
//						i = count;
//					}

				
				 count = i + 8;       //For Cobas 801 Changes [ETB ---- STX+1 Remove]
				 i = count;
												
//						i++;
//					if(ch[i]==02) {
//						count = i + 1;
//						i = count;
//					}
				
				/*
				 * count = i + 8; i = count;
				 */
			}
			
			else {
				
				count = 0;

				buf.append(ch[i]);

			}
		}
		
		return buf.toString();
	}

	// to Check which type of packet came if it is order packet then send for split
	// otherwise SEND ACK
	public static int recieved_Packet(String msg) {
		Reader inputString = new StringReader(msg);
		BufferedReader reader = new BufferedReader(inputString);
		List<String> list = new ArrayList<String>();
		String line;
		int order_packet = 0;
		try {
			while ((line = reader.readLine()) != null) {
				line.subSequence(0, line.length() - 1);
				list.add(line);
			}

			for (String str : list) {

				if (
						(str.length())>2 &&
						(
						Character.toUpperCase(str.charAt(0)) == 'O' || Character.toUpperCase(str.charAt(2)) == 'O'
						|| Character.toUpperCase(str.charAt(1)) == 'O' || Character.toUpperCase(str.charAt(1)) == 'R'
						|| Character.toUpperCase(str.charAt(2)) == 'R' || Character.toUpperCase(str.charAt(2)) == 'P'
						|| Character.toUpperCase(str.charAt(2)) == 'P') 
					) {
					order_packet = 1;
					break;
				} else
					order_packet = 0;
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return order_packet;
	}

	// check if every line in receive packet has STX syntax
	public static int recieved_Packet1(String msg) {
		Reader inputString = new StringReader(msg);
		BufferedReader reader = new BufferedReader(inputString);
		List<String> list = new ArrayList<String>();
		String line;
		int order_packet = 0;
		try {
			while ((line = reader.readLine()) != null) {
				line.subSequence(0, line.length() - 1);
				list.add(line);
			}

			for (String str : list) {

				if (Character.toUpperCase(str.charAt(2)) == 'H' || Character.toUpperCase(str.charAt(2)) == 'O'
						|| Character.toUpperCase(str.charAt(2)) == 'P' || Character.toUpperCase(str.charAt(2)) == 'R'
						|| Character.toUpperCase(str.charAt(2)) == 'L') {
					String output = "";

					for (int i = str.length() - 1; i >= 0; i--) {
						output = output + str.charAt(i);
					}

					if (Character.toUpperCase(output.charAt(2)) == '') {
						order_packet = 1;
						break;
					}
				} else {
					order_packet = 0;
				}
			}

		}

		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return order_packet;
	}

	// Check For if Packet contains 'Q' then reply a flag to reply ACK to machine
	public static int checkQ(String text) {
		StringBuffer buf = new StringBuffer();
		Reader inputString = new StringReader(text);
		BufferedReader reader = new BufferedReader(inputString);
		List<String> list = new ArrayList<String>();
		String str;
		System.out.println("INSIDE CHECK Q");
		int sendAck = 0;
		try {

			while ((str = reader.readLine()) != null) {
				str = str.substring(0, str.length() - 1);
				list.add(str);
			}
			// check for select query
			/// COMMENTED USE IT AS FOR CHECKING QUERY PART
			//if (list.size() == 1) {
			//	buf.append("NO Packet Available");
			//	return 0;
			//}
			
			String Q="";
			String Q1="";
			if(list.size()>1) {
				 Q = list.get(0);
				 Q1 = list.get(1);
			}
			else {
				
				 Q = list.get(0);
			}
			
			
			if (Q.length() > 2) {
				if (Character.toUpperCase(Q.charAt(0)) == 'Q' || Q.contains("Q|1|")) {
					sendAck = 1;
				}
			}
			if (Q1.length() > 2) {
				if (Character.toUpperCase(Q1.charAt(2)) == 'Q' || Q1.contains("Q|1|")) {
					sendAck = 1;
				}
			} 
			//else
//				sendAck = 0;
			/*
			 * else parse(list);
			 */

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("sendAck-------" + sendAck);
		return sendAck;
	}

	// Make Array of String of single Strings
	public static List splitFunction(String text) {
		StringBuffer buf = new StringBuffer();
		Reader inputString = new StringReader(text);
		BufferedReader reader = new BufferedReader(inputString);
		List<String> list = new ArrayList<String>();
		List frameList = new ArrayList<>();
		String str;
		try {

			while ((str = reader.readLine()) != null) {
				if (str != null && str.length() > 1) {
					// str = str.substring(0, str.length() - 1);
					
					
					list.add(str);
				}
			}
			// check for select query
			/// COMMENTED USE IT AS FOR CHECKING QUERY PART
		//	if (list.size() == 1) {
		//		buf.append("NO Packet Available");
		//		return frameList;
		//	}
			
			//System.out.println("<####>"+list.size()+"list------------------"+list);
			String Q = list.get(1);
			String Q1 = list.get(1);//1
			//RamjiMask BA400 15-Mar-2024			System.out.println("<####>");
			//RamjiMask BA400 15-Mar-2024			System.out.println("parsing start");
			System.out.println(" The Value LIST : " + list);
			//RamjiMask BA400 15-Mar-2024			
			System.out.println(" The Value of text : " + text);
			//RamjiMask BA400 15-Mar-2024			System.out.println(" The_Value_of Q" + Q);
			if (Character.toUpperCase(Q.charAt(2)) == 'Q' || Q.contains("Q|1|") || Q1.contains("Q|1|") ||Character.toUpperCase(Q1.charAt(0)) == 'Q') 
			{

				if (formatid.equals("20003"))// genexpert
				{
					frameList = parseSelect(list);
				}
				if (formatid.equals("20017"))// genexpert
				{
					frameList = parseSelect2500(list);
				}
			
				if (formatid.equals("20048"))//  20045 use for BA 400 BI directional mispa clinia  used for AAIIMS MAnglagiri format id 20045
				{
					frameList = common_parseSelect(list);
				}
				
				
				
				if (formatid.equals("20001111"))// genexpert same
				{
					frameList = parseSelect(list);
				}
				if (formatid.equals("20016"))// genexpert
				{
					frameList = parseSelect(list);
				}
			//	if (formatid.equals("20015"));//("20011"))// vitros 5600 //20016 for sysmex 2500
				//{
					
			//		frameList = common_parseSelect(list);
			//	}
				//if (formatid.equals("20012"))// vitros 5600
			//	{
				//	frameList = common_parseSelect(list);
			//	}

			} 
			else {
				// parse(list); genexpert format ID 20001
				// parse_sysmex800i(list); format ID 20002

				if (formatid.equals("200031")) {
					parse(list);
				}

			
				if (formatid.equals("20016")) { // SysmexXN350
					SysmexXN350A(list);
				}
				if(formatid.equals("20017"))
				{
					System.out.println("Updated on date 12 JAN");
					SysmexXN2500(list);}
					
				
				if (formatid.equals("20045")) { // Mindray mispa clinia
					mindray_MispaClinia(list);
				}
				

				if (formatid.equals("20051")) { // Mindray 20051
					System.out.println("inside 20051");
					mindray_6200(list);
				}
				
				  if (formatid.equals("20046")) {  //added for AIIMS BHU 
		               cobas6000_parsing(list);
		               
		            }
				
				
			
//				if (formatid.equals("20035")) { // Vitros 560
	//				Vitros_client.main(argument);
	//			}
			
				if (formatid.equals("20044")) { // simens_atalika
				//	parse_simens_atalika(list);
				}
				
				//if (formatid.equals("20050")) { // parse_AIIMSRB 18/10/2022
//				if (formatid.equals("20011")) { // parse_AIIMS Bathinda 18/02/2022	
//					parse_AIIMSRB(list);
//				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		if (buf != null)
			return frameList;
		else
			return null;
	}

	// PArse for Select Query
	public static List parseSelect(List<String> list) {
		StringBuffer packet = new StringBuffer();
		List<StringBuffer> frameList = new ArrayList<>();
		List testCode = new ArrayList();
		String line_header = list.get(0);
		//String line = list.get(1);
		String line = list.get(0);

		int count = 0;
		StringBuffer bf = new StringBuffer();
		StringBuffer bf1 = new StringBuffer();

		int count_header = 0;
		StringBuffer bf_header = new StringBuffer();

		// get message id
		if (Character.toUpperCase(line_header.charAt(2)) == 'H') {
			char[] ch = line_header.toCharArray();

			if (Character.toUpperCase(line_header.charAt(2)) == 'H') {

				for (int i = 0; i < ch.length; i++) {
					if (ch[i] == '|')
						count_header++;
					if (count_header == 2) {
						if (!(ch[i] == '|'))
							bf_header.append(ch[i]);
					}

				}
			}
		}

		// get message id

		if (Character.toUpperCase(line.charAt(0)) == 'Q' || line.contains("Q|1|")) {
			char[] ch = line.toCharArray();

			if (Character.toUpperCase(line.charAt(0)) == 'Q' || line.contains("Q|1|")) {

				for (int i = 0; i < ch.length; i++) {

					//if (ch[i] == '|')
					//	count++;

//					if (count == 2) {
//						if (!(ch[i] == '|'))
//							bf.append(ch[i]);
//					}
					
					if(ch[i]=='^')
						count++;
					if(count==2) {
						if (!(ch[i] == '^'))
							bf.append(ch[i]);
					}

				}

				//System.out.println("SampleName:- " + bf.toString().trim());// Sample ID Read between '|' and '^'

			}
			/*
			 * for(int i=0;i<ch.length;i++) { if(ch[i]==94)/// ascii value of '^' { count++;
			 * if(count==3) i++; } if(count==3&&count!=4) {
			 * 
			 * bf.append(ch[i]); } if(count==9) { break;
			 * 
			 * } if(count==7) { i++; System.out.println(ch[i]); newS.append(ch[i]); i++;
			 * newS.append(ch[i]); System.out.println("newS"+newS); } if(count>=3)
			 * packet.append(ch[i]); }
			 */

			/*
			 * for(int i=0;i<ch.length;i++) { if(ch[i]=='|') { count++; if(count==2) i++; }
			 * if(count==3&&count!=4) {
			 * 
			 * bf.append(ch[i]); } if(count==9) { break;
			 * 
			 * } if(count==7) { i++; System.out.println(ch[i]); newS.append(ch[i]); i++;
			 * newS.append(ch[i]); System.out.println("newS"+newS); } if(count>=3)
			 * packet.append(ch[i]); }
			 * 
			 * 
			 */

		}
		System.out.println("SampleNo " + bf);
		// get test code
		String it = ABC.getSampleDtl(bf.toString().replace("^", ""));
		String[] kvPairs = it.split("#");
		StringBuffer st = new StringBuffer(kvPairs[0]);
		StringBuffer st2 = new StringBuffer(kvPairs[1]);
		
		ServerCobas801.Test_Code= st;
		ServerCobas801.name_pat=st2.toString();
		//System.out.println("hllo"+Server.Test_Code+Server.name_pat);
		
		//Server.Test_Code = bf1.append(it);
//	  System.out.println("lt========= "+it);
		// String line2=(((it.replace("{",
		// System.out.print(" new line2---------------- "+line2);
		String[] kv = it.split("#");
		// String key = kv[0];
		// String[] kv1 = key.split(";");
		// String key1 = kv1[0];
		// String value = kv[1];
		// System.out.print(" kv[0] ---------------- "+kv[0]);
		// System.out.print(" kv1[0] ---------------- "+kv1[0]);

		String k[] = st.toString().split(";");
		
		
		for(String data:k) {
			
			testCode.add(data);
		}
		// System.out.print(" k[0] ---------------- "+k[0]);
		// Server.Test_Code = bf1.append(k[0]);
		System.out.println(" Test_Code ---------------- " + Test_Code);
		// TestCodeD

		// get test code
		// TransactionsDao dao = new TransactionsDao();
		// testCode = dao.selectTestCode(bf.toString()); //List of TestCodes from Select
		// Query
		// System.out.println("TESTCODE 0 :--- "+testCode.get(0));
		// Hard_coded TestCodes to make reply
		/*
		 * testCode.add("990");testCode.add("990");testCode.add("990");testCode.add(
		 * "990");testCode.add("990");
		 * testCode.add("991");testCode.add("991");testCode.add("991");testCode.add(
		 * "991");testCode.add("991");
		 * testCode.add("8685");testCode.add("8685");testCode.add("8685");testCode.add(
		 * "8685");testCode.add("8685");
		 * testCode.add("8781");testCode.add("8781");testCode.add("8781");testCode.add(
		 * "8781");testCode.add("8781");
		 * testCode.add("989");testCode.add("989");testCode.add("989");testCode.add(
		 * "989");testCode.add("989");
		 */
		/*
		 * testCode.add("8798");testCode.add("8798");testCode.add("8798");testCode.add(
		 * "8798");testCode.add("8798");
		 * testCode.add("8570");testCode.add("8570");testCode.add("8570");testCode.add(
		 * "8570");testCode.add("8570");
		 * testCode.add("8735");testCode.add("8735");testCode.add("8735");testCode.add(
		 * "8735");testCode.add("8735");
		 * testCode.add("8687");testCode.add("8687");testCode.add("8687");testCode.add(
		 * "8687");testCode.add("8687");
		 * testCode.add("8678");testCode.add("8678");testCode.add("8678");testCode.add(
		 * "8678");testCode.add("8678");
		 * testCode.add("8413");testCode.add("8413");testCode.add("8413");testCode.add(
		 * "8413");testCode.add("8413");
		 * testCode.add("8712");testCode.add("8712");testCode.add("8712");testCode.add(
		 * "8712");testCode.add("8712");
		 * testCode.add("8683");testCode.add("8683");testCode.add("8683");testCode.add(
		 * "8683");testCode.add("8683");
		 * testCode.add("8698");testCode.add("8698");testCode.add("8698");testCode.add(
		 * "8698");testCode.add("8698");
		 * testCode.add("8701");testCode.add("8701");testCode.add("8701");testCode.add(
		 * "8701");testCode.add("8701");
		 * testCode.add("8454");testCode.add("8454");testCode.add("8454");testCode.add(
		 * "8454");testCode.add("8454");
		 * testCode.add("8731");testCode.add("8731");testCode.add("8731");testCode.add(
		 * "8731");testCode.add("8731");
		 * testCode.add("8418");testCode.add("8418");testCode.add("8418");testCode.add(
		 * "8418");testCode.add("8418");
		 * testCode.add("8700");testCode.add("8700");testCode.add("8700");testCode.add(
		 * "8700");testCode.add("8700");
		 * testCode.add("8714");testCode.add("8714");testCode.add("8714");testCode.add(
		 * "8714");testCode.add("8714");
		 */

		/*
		 * testCode.add("8552");testCode.add("8552");testCode.add("8552");testCode.add(
		 * "8552");testCode.add("8552");
		 * testCode.add("8690");testCode.add("8690");testCode.add("8690");testCode.add(
		 * "8690");testCode.add("8690");
		 */
		ServerCobas801.testCode = testCode;

		ServerCobas801.packet = packet;
		// System.out.println("Packet "+Server.packet);
		// Server.packet.append("741");
		ServerCobas801.sampleNo = bf;
		ServerCobas801.Query_Message_Id = bf_header;
		// Make Packet for Reply
		for (int i = 0; i < list.size(); i++) {
			if (Character.toUpperCase(list.get(i).charAt(0)) == 'H')
				;
			{
				if (formatid.equals("200011"))// genexpert
				{
					frameList = headerParsing(list.get(0));
				}

				if (formatid.equals("200011"))// sys800i
				{
					frameList = headerParsing(list.get(0));
				}
				if (formatid.equals("20003"))// genexpert dinamic mtb sarscov testcode
				{
					frameList = headerParsing_GEN_MTB(list.get(0));
				}
				
//				if (formatid.equals("20016"))// sysmax 2500
//				{
//					frameList = headerParsing2500(list.get(0));
//				}
				if (formatid.equals("20016"))// sysmax 2500  // added by rohit for Bi Dir.. RB sysmax 200
				{
					frameList = sysmax2500RBparseBI(list.get(0));
				}
				
				if (formatid.equals("20046"))// 
				{
					 frameList = cobas6000_headerParsing_s(); ///cobas AIIMS BH
				
				}
				

				// frameList = headerParsing(list.get(0));
			}

			break;
		}
		// System.out.println("PAcket:-----"+packet);
		return frameList;
	}
	// ------------------------------------------------------------

	public static List sysmex1000_headerParsing_m() {
		SimpleDateFormat time_formatter2 = new SimpleDateFormat("yyyyMMddHHmmss");
		String current_time_str2 = time_formatter2.format(System.currentTimeMillis());
		System.out.println("current_time_str2---" + current_time_str2);
		StxCounter = 1;
	
		SimpleDateFormat time_formatter3 = new SimpleDateFormat("yyyyMMddHHmmss");
		String current_time_str3 = time_formatter3.format(System.currentTimeMillis());
		// System.out.println("current_time_str2 after 1 second wait---" +
		// current_time_str3);

		int CountPipe = 0, j = 0;
		char cr = 13;
		char lf = 10;
		int instance = 0;
		char stx = '';
		String mode1 = "";
		StringBuffer buf = new StringBuffer();
		StringBuffer cobas = new StringBuffer();
		StringBuffer host = new StringBuffer();
		StringBuffer mode = new StringBuffer();
		StringBuffer lines = new StringBuffer();
		StringBuffer frame1 = new StringBuffer();
		StringBuffer frame2 = new StringBuffer();
		StringBuffer frame3 = new StringBuffer();
		StringBuffer frame3_1 = new StringBuffer();
		StringBuffer frame4 = new StringBuffer();

		StringBuffer buf_header = new StringBuffer();
		StringBuffer buf_patient = new StringBuffer();
		StringBuffer buf_order = new StringBuffer();
		StringBuffer buf_order_1 = new StringBuffer();
		StringBuffer buf_termination = new StringBuffer();

		SimpleDateFormat time_formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String current_time_str = time_formatter.format(System.currentTimeMillis());

//		StringBuffer header1 = new StringBuffer(
//				"H|@^\\|GXM-88424724426|| LIS|||||CENTRAL RAILWAY HOSPITAL, SECUNDERABAD - 110000888^GeneXpert^6.4||P|1394-97|");

		StringBuffer header = new StringBuffer("H|\\^&|");
//		header.append(Query_Message_Id);
		header.append("||");
//	header.append("99^2.00");
//	header.append(Query_Message_sender);
		header.append("|||||");
//    header.append(Query_Message_receiver);
		header.append(Query_Message_sender.toString().trim());
		header.append("||P|E1394-97|");
		// header = header.append(Query_Message_date_time);
		header = header.append(current_time_str);

		StringBuffer line4 = new StringBuffer("L|1|N");
		StringBuffer line6 = new StringBuffer("|||F");
		StringBuffer line5 = new StringBuffer();
		char etb = 23;
		char eot = '';
		char etx = 3;
		counterPID_Packet = counterPID_Packet + 33;

		StringBuffer p = new StringBuffer("P|");
		p.append(p_packet_count);
		p.append("|");
		p.append("||");
		// p.append("|");
		p.append(ServerCobas801.Sample_code.get(0).toString().trim());
		p.append("|^");
		// p.append("Smith^Tom^J||19600315|M|||A|||icteru||||||01|||||A1|002||||||||");
		// p.append("RR^ABIGAIL^G||19780407|F||843 TALL OAKSDR^HAILVILLE, MD
		// 45831|||RASHAMDRA^SANJAY^V|S|||||||||||U7");
		if (name_pat_first.equals("null")) {
			name_pat_first = "";
			name_pat_last = "";
		}
		if (name_pat_last.equals("null"))
			name_pat_last = "";
		p.append(name_pat_first + "^" + name_pat_last);
//		 p.append(name_pat.trim()); 

		p.append("||||||||||||||||||||");
		StringBuffer c = new StringBuffer("C|1|I|Patient is complaining of shortness of breath and chest pain.|G");

		StringBuffer Order = new StringBuffer();
		StringBuffer Order1 = new StringBuffer("O|2");
		List list = new ArrayList();
		StringBuffer frame = new StringBuffer();
		StringBuffer framevip = new StringBuffer();
		StringBuffer response = new StringBuffer();
		StringBuffer time = new StringBuffer();

		try {

			System.out.println("TIME:- " + time);

			while (true) {

				buf_header.append(header);
				buf_header.append(cr);
				buf_patient.append(p);
				buf_patient.append(cr);

				System.out.println("testCode.size------- " + testCode.size());

				frameList.clear();
				frame1.append(stx);
				frame1.append(StxCounter);
				frame1.append(buf_header);
				buf_header.delete(0, buf_header.length());
				frame1.append(etx);
				frame1.append(CheckSum(frame1.toString())); // FRAME WITH STX STX COUNTER AND ETB
				frame1.append(cr);
				frame1.append(lf);
				frameList.add(frame1.toString());
				frame1.delete(0, frame1.length());
				System.out.println("frameList= " + frameList.toString());

				StxCounter = StxCounter + 1;

				frame2.append(stx);
				frame2.append(StxCounter);
				frame2.append(buf_patient);
				buf_patient.delete(0, buf_patient.length());
				frame2.append(etx);
				frame2.append(CheckSum(frame2.toString())); // FRAME WITH STX STX COUNTER AND ETB
				frame2.append(cr);
				frame2.append(lf);
				frameList.add(frame2.toString());
				frame2.delete(0, frame2.length());
				System.out.println("frameList= " + frameList.toString());

				// ====================================================================
				Order.append("O|1");
				// Order.append();
				Order.append("|");
				Order.append("2^1^" + ServerCobas801.Sample_code.get(0).toString().trim() + "^B");
				Order.append("||");
				for (int k = 0; k < testCode.size(); k++)

				{
					// Order.append(k + 1);
					Order.append("^^^^");
					Order.append(testCode.get(k));
					// Order.append("+1.0");
					if (k != testCode.size() - 1) {
						// Order.append("^2^1");
						Order.append("\\");

					}

					else {
						// Order.append("^^^");
					}

				}
				Order.append("|");
				Order.append("|||||||||||||||||||||O||||||");
				// Order.append(line6);
				Order.append(cr);

				buf_order.append(Order);
				Order.delete(0, Order.length());

				// System.out.println("buf_order= " + buf_order.toString());

				// --------------------------------------------
				// System.out.println("STRING LENGTH BEFORE FRAMING buf_order= " +
				// buf_order.length());

				// STRING LENGTH BEFORE FRAMING = 235
				int frameCount = 0, first = 0, mid = 0, remain = 0;
				frameCount = FrameCounter(buf_order.length());
				// System.out.println("Char At 0 "+buf.charAt(0));
				int length = buf_order.length();
				// frameList3.clear();
				for (int i = 1; i <= frameCount; i++) {
					if (length < 240)
						// if (length < 306)
						mid = mid + length;
					else if (length == 240)
						mid = mid + length;
					else if (length > 240) {
						mid = mid + 240;
					}
					System.out.println("First " + first + "  " + "last " + mid + " stx  " + stx);
					frame3.append(stx);
					StxCounter = StxCounter + 1;
					// System.out.println("Server.StxCounter " +StxCounter);
					frame3.append(StxCounter);
					// System.out.println("STX COUNTER:- " +StxCounter);

					frame3.append(buf_order.substring(first, mid));

					if (i == frameCount) {
						frame3.append(etx);
						// System.out.println("before checksum" + frame3.toString());
						frame3.append(CheckSum(frame3.toString())); // FRAME WITH STX STX COUNTER AND ETX
						// frame.append(CheckSum(buf.substring(first, mid))); //FRAME WITH ONLY
						// CHARACTERS
						frame3.append(cr);
						frame3.append(lf);
					} else

					{
						frame3.append(etb);
						// System.out.println("before checksum" + frame3.toString());
						frame3.append(CheckSum(frame3.toString())); // FRAME WITH STX STX COUNTER AND ETB
						// frame.append(CheckSum(buf.substring(first, mid)));
						frame3.append(cr);
						frame3.append(lf);
					}
					// length = length - mid;
					length = length - 240;
					first = mid;

					frameList.add(frame3.toString());
					/*
					 * if(i ==1) frame1.append(frame); else frame2.append(frame);
					 */
					frame3.delete(0, frame3.length());

				}

				buf_order.delete(0, buf_order.length());
				System.out.println("frameList= " + frameList.toString());

	

				line4.append(cr);

				buf.append(line4);
				buf_termination.append(line4);

				break;
			}

			// ========================================

			StxCounter = StxCounter + 1;

			frame4.append(stx);
			frame4.append(StxCounter);
			frame4.append(buf_termination);
			buf_termination.delete(0, buf_termination.length());
			frame4.append(etx);
			frame4.append(CheckSum(frame4.toString())); // FRAME WITH STX STX COUNTER AND ETB
			frame4.append(cr);
			frame4.append(lf);

			frameList.add(frame4.toString());
			frame4.delete(0, frame4.length());

			for (int i = 0; i < frameList.size(); i++) {
				count_ack_BA400_o1++;
			}
//				System.out.println("count_ack_BA400_o1------- " + count_ack_BA400_o1);
			System.out.println("Final String after checksum" + frameList.toString());

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		// Server.frameList = frameList;

		return frameList;
	}

	// ---------------------------------------------------------------------

	public static void mindray_6200(List<String> list) { 
		System.out.println("MIndray 6200-------------:::::Rohit M 08/10:::::");
		StringBuffer sampleName = new StringBuffer();
		StringBuffer TestCode;
		StringBuffer TestValue;		
		ABC abc = new ABC();
		Map<String, List> mp = new HashMap();  //added by Rohit...
		List <String> testCode = new ArrayList<>();
		List <String> testValue = new ArrayList<>();
		List <String> testCodeupdated = new ArrayList<>();
		List <String> testValueupdated = new ArrayList<>();
		
		int r = 0;
		for (String line : list) {
	
			if (line.length() == 0 || line == null)
				break;
			TestCode = new StringBuffer();
			TestValue = new StringBuffer();
			

			if (line.length() > 2) {
				if(line.contains("O|")) {
	
					char[] ch = line.toCharArray();
					int count = 0;
					int count_tn = 0;
					int count_or_cr = 0;
					for (int i = 0; i < ch.length; i++) {				

						if (ch[i] == '|') {
							count++;
							if (count == 3)
								i++;
						}
						if (count == 2) {
							if (!(ch[i] == '|'))
								sampleName.append(ch[i]);
						}

						if (count == 3) {
							break;
						}				
					}

					System.out.println("SampleName  =:- " + sampleName);
				}

					if(line.contains("R|")) {		
					int ct = 0;
					char[] ch = line.toCharArray();
					int count_pipe = 0;
					int count_carr = 0;
					for (int i = 0; i < ch.length; i++) {

						if (ch[i] == '|')
							count_pipe++;

						if (count_pipe == 2 && ch[i] == '^') {
							count_carr++;
							if (count_carr == 1)
								i++;
						}

						if (count_carr == 1 && !(ch[i] == '^')) {
							TestCode.append(ch[i]);
							
						}
					
						if (count_carr == 2) {
							break;
						}
					}
			

					int count = 0;
					int count1 = 0;
					for (int i = 0; i < ch.length; i++) {

						if (ch[i] == '|') {
							count1++;
							if (count1 == 3)
								i++;
						}
						if ( count == 4)
							break;
						if (count1 == 3 && !(ch[i] == '|')) {
							TestValue.append(ch[i]);
							String str2 = TestValue.toString().replace("^", "");
							TestValue.delete(0, TestValue.length());
				//			TestValue.append(str2);
							TestValue.append(str2.replaceAll("[^0-9.]", "").trim());
							
						}
					}
					
					
					
					StringBuffer testcode1 =new StringBuffer();;
					StringBuffer testvalue1=new StringBuffer();;
					
//					if(TestCode.toString().trim().contains("#")) {
//						TestCode.toString().replaceAll("#", "*");
//						testcode1.append(TestCode.toString().replaceAll("#", "*$"));
//						
//					}
//					else if(TestCode.toString().trim().contains("%"))
//					{
//						TestCode.toString().replaceAll("%", "_$");
//						testcode1.append(TestCode.toString().replaceAll("%", "_$"));
//						
//					}
//					
//					
//					System.out.println("testcode 2222"+testcode1);
//					
					
					
					//if(TestCode.toString().trim().length() >=3 && TestCode.toString().trim().length() <=6  )
					//{
									testCode.add(TestCode.toString().trim());
					//}
					
					//System.out.println("Testcode =:- " + TestCode.toString());	 testValue.add(TestValue.toString()); //added by rohit...
					//System.out.println("Testvalue  =:- " + TestValue);

					//if(TestValue.toString().trim().equals(""))
					testValue.add(TestValue.toString().trim());
					
					
				
					
				}
					
					
				
					
				//	System.out.println("test Code"+testCode +"test Value"+testValue);

				//if (!TestCode.toString().trim().equals("")&& r>14 && !sampleName.toString().trim().equals("")) {
			//	r++;
					StringBuffer Tv1 = new StringBuffer();
				//double Tv_db = Double.parseDouble(TestValue.toString());
				//TestValue = TestValue.delete(0, TestValue.length());
			
				//Tv1.append(String.format("%.2f", Tv_db));
				//System.out.println("Updated TestValue :- " + Tv1);
				//TestValue.append((Tv1.toString()));
		
			
//					if(TestCode.toString().trim().length() >=3 && TestCode.toString().trim().length() <=6  )
//					{	
//						//if(TestCode.toString().contains("%") || TestCode.toString().contains("#")&&TestCode.toString().contains("-") ) {
//							
//							
//							
//					//	}
//						
//							}
					
					
			//	}
					
					
			}

		}
		
		
		for(int i=0;i<testCode.size();i++) {
			String str= (String) testCode.get(i);
			String str1 ="";

			
//			if(str=="WBC" ||str=="NEU#" || str=="LYM#" || str=="MON#" || str=="EOS#" || str=="BAS#" || str=="IMG#" || str=="NEU%" || str=="LYM%" || str=="MON%" || str=="EOS%" || str=="BAS%" || str=="IMG%" || str=="RBC" || str=="HGB" || str=="HCT" || str=="MCV" || str=="MCH" || str=="MCHC" || str=="RDW-CV" || str=="RDW-SD" || str=="PLT" || str=="MPV" || str=="PDW" || str=="PCT" || str=="PLCC" || str=="NRBC#" || str=="NRBC%" || str=="PLCR" ) {
//				testCodeupdated.add(str);
//				testValueupdated.add(str1);
//				
//			}
			
			if(str.equals("WBC")|| str.equals("NEU#") || str.equals("LYM#") || str.equals("MON#") || str.equals("EOS#") || str.equals("BAS#") || str.equals("IMG#") || str.equals("NEU%") || str.equals("LYM%") || str.equals("MON%") || str.equals("EOS%") || str.equals("BAS%") || str.equals("IMG%") || str.equals("RBC") || str.equals("HGB") || str.equals("HCT") || str.equals("MCV") || str.equals("MCH") || str.equals("MCHC") || str.equals("RDW-CV") || str.equals("RDW-SD") || str.equals("PLT") || str.equals("MPV") || str.equals("PDW") || str.equals("PCT") || str.equals("PLCC") || str.equals("NRBC#") || str.equals("NRBC%") || str.equals("PLCR") ) {
				int index= testCode.indexOf(str);
				//System.out.println("index"+index +str);
				str1=(String) testValue.get(index);
			//	System.out.println("---->>"+str1);
				testCodeupdated.add(str);
				testValueupdated.add(str1);
			}
			
			
			
			
		
		}	
		//System.out.println("incoming Test Code "+testCode);
		//System.out.println("after filter Test Code "+testCodeupdated);
		//System.out.println("after filter Test value "+testValueupdated);
		
		
		
		
		mp.put("TestCode", testCodeupdated);
		mp.put("TestValue", testValueupdated);
		
		
		abc.insert_SysmexXN350(mp,sampleName.toString().trim());	
		
		

	}

	public static List parseSelect2500(List<String> list) {
		StringBuffer packet = new StringBuffer();
		List<StringBuffer> frameList = new ArrayList<>();
		List testCode = new ArrayList();
		String line = list.get(1);
		int count = 0;
		StringBuffer bf = new StringBuffer();

		if (Character.toUpperCase(line.charAt(0)) == 'Q') {
			char[] ch = line.toCharArray();

			if (Character.toUpperCase(line.charAt(0)) == 'Q') {

				for (int i = 0; i < ch.length; i++) {

					if (ch[i] == '|')
						count++;

					if (count == 2) {
						if (!(ch[i] == '|'))
							bf.append(ch[i]);
					}

				}

				System.out.println("SampleName:- " + bf);// Sample ID Read between '|' and '^'

			}
			/*
			 * for(int i=0;i<ch.length;i++) { if(ch[i]==94)/// ascii value of '^' { count++;
			 * if(count==3) i++; } if(count==3&&count!=4) {
			 * 
			 * bf.append(ch[i]); } if(count==9) { break;
			 * 
			 * } if(count==7) { i++; System.out.println(ch[i]); newS.append(ch[i]); i++;
			 * newS.append(ch[i]); System.out.println("newS"+newS); } if(count>=3)
			 * packet.append(ch[i]); }
			 */

			/*
			 * for(int i=0;i<ch.length;i++) { if(ch[i]=='|') { count++; if(count==2) i++; }
			 * if(count==3&&count!=4) {
			 * 
			 * bf.append(ch[i]); } if(count==9) { break;
			 * 
			 * } if(count==7) { i++; System.out.println(ch[i]); newS.append(ch[i]); i++;
			 * newS.append(ch[i]); System.out.println("newS"+newS); } if(count>=3)
			 * packet.append(ch[i]); }
			 * 
			 * 
			 */

		}
		System.out.println("SampleNo " + bf);
		// TransactionsDao dao = new TransactionsDao();
		// testCode = dao.selectTestCode(bf.toString()); //List of TestCodes from Select
		// Query
		ABC abc = new ABC();
		//testCode = abc.getSampleDtlB(bf.toString());
		abc.getSampleDtlB(bf.toString());
		System.out.println("testcode" + testCode);
		// System.out.println("TESTCODE 0 :--- "+testCode.get(0));
		// Hard_coded TestCodes to make reply
		/*
		 * testCode.add("990");testCode.add("990");testCode.add("990");testCode.add(
		 * "990");testCode.add("990");
		 * testCode.add("991");testCode.add("991");testCode.add("991");testCode.add(
		 * "991");testCode.add("991");
		 * testCode.add("8685");testCode.add("8685");testCode.add("8685");testCode.add(
		 * "8685");testCode.add("8685");
		 * testCode.add("8781");testCode.add("8781");testCode.add("8781");testCode.add(
		 * "8781");testCode.add("8781");
		 * testCode.add("989");testCode.add("989");testCode.add("989");testCode.add(
		 * "989");testCode.add("989");
		 */
		/*
		 * testCode.add("8798");testCode.add("8798");testCode.add("8798");testCode.add(
		 * "8798");testCode.add("8798");
		 * testCode.add("8570");testCode.add("8570");testCode.add("8570");testCode.add(
		 * "8570");testCode.add("8570");
		 * testCode.add("8735");testCode.add("8735");testCode.add("8735");testCode.add(
		 * "8735");testCode.add("8735");
		 * testCode.add("8687");testCode.add("8687");testCode.add("8687");testCode.add(
		 * "8687");testCode.add("8687");
		 * testCode.add("8678");testCode.add("8678");testCode.add("8678");testCode.add(
		 * "8678");testCode.add("8678");
		 * testCode.add("8413");testCode.add("8413");testCode.add("8413");testCode.add(
		 * "8413");testCode.add("8413");
		 * testCode.add("8712");testCode.add("8712");testCode.add("8712");testCode.add(
		 * "8712");testCode.add("8712");
		 * testCode.add("8683");testCode.add("8683");testCode.add("8683");testCode.add(
		 * "8683");testCode.add("8683");
		 * testCode.add("8698");testCode.add("8698");testCode.add("8698");testCode.add(
		 * "8698");testCode.add("8698");
		 * testCode.add("8701");testCode.add("8701");testCode.add("8701");testCode.add(
		 * "8701");testCode.add("8701");
		 * testCode.add("8454");testCode.add("8454");testCode.add("8454");testCode.add(
		 * "8454");testCode.add("8454");
		 * testCode.add("8731");testCode.add("8731");testCode.add("8731");testCode.add(
		 * "8731");testCode.add("8731");
		 * testCode.add("8418");testCode.add("8418");testCode.add("8418");testCode.add(
		 * "8418");testCode.add("8418");
		 * testCode.add("8700");testCode.add("8700");testCode.add("8700");testCode.add(
		 * "8700");testCode.add("8700");
		 * testCode.add("8714");testCode.add("8714");testCode.add("8714");testCode.add(
		 * "8714");testCode.add("8714");
		 */

		/*
		 * testCode.add("8552");testCode.add("8552");testCode.add("8552");testCode.add(
		 * "8552");testCode.add("8552");
		 * testCode.add("8690");testCode.add("8690");testCode.add("8690");testCode.add(
		 * "8690");testCode.add("8690");
		 */
		ServerCobas801.testCode = testCode;

		ServerCobas801.packet = packet;
		// System.out.println("Packet "+Server.packet);
		// Server.packet.append("741");
		ServerCobas801.sampleNo = bf;
		// Make Packet for Reply
		for (int i = 0; i < list.size(); i++) {
			if (Character.toUpperCase(list.get(i).charAt(0)) == 'H')
				;
			{
				if (formatid.equals("20001"))// genexpert
				{
					frameList = headerParsing(list.get(0));
				}

				if (formatid.equals("20003"))// genexpert
				{
					frameList = headerParsing(list.get(0));
				}

				if (formatid.equals("20002"))// sys800i
				{
					frameList = headerParsing(list.get(0));
				}

				// frameList = headerParsing(list.get(0));
			}

			break;
		}
		// System.out.println("PAcket:-----"+packet);
		return frameList;
	}

	
	//header parsing sysmax 2500
	
	public static List headerParsing(String line) {
		char[] array = line.toCharArray();
		int CountPipe = 0, j = 0;
		char cr = 13;
		char lf = 10;
		int instance = 0;
		char stx = '';
		String mode1 = "";
		StringBuffer buf = new StringBuffer();
		StringBuffer cobas = new StringBuffer();
		StringBuffer host = new StringBuffer();
		StringBuffer mode = new StringBuffer();
		StringBuffer lines = new StringBuffer();

		// H|@^\|GXM-88424724426||CENTRAL RAILWAY HOSPITAL, SECUNDERABAD -
		// 110000888^GeneXpert^6.4||||| LIS||P|1394-97|20211229171329
		// P|1|||p1
		// O|1|2912OP0250||^^^SARSCOV2|R|20071116133208|||||A||||ORH||||||||||Q

		// O|1||2912OP0250||^^SARSCOV2|R|20211229183655||||||A||||ORH||||||||||Q
		// O|1||2912OP0250|||R|20211229183655||||||P||||ORH||||||||||I
		// L|1|F

		// StringBuffer header = new StringBuffer("H|\\^&|||host|||||cobas
		// 8000^1.06|TSDWN|P|1|");
		SimpleDateFormat time_formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String current_time_str = time_formatter.format(System.currentTimeMillis());

		StringBuffer header = new StringBuffer(
				"H|@^\\|GXM-88424724426|| LIS|||||CENTRAL RAILWAY HOSPITAL, SECUNDERABAD - 110000888^GeneXpert^6.4||P|1394-97|");

		header = header.append(current_time_str);
		StringBuffer line3 = new StringBuffer("C|1|L|^^^^|G");
		StringBuffer line4 = new StringBuffer("L|1|F");
		StringBuffer line6 = new StringBuffer("||||P||||ORH||||||||||Q");
		StringBuffer line5 = new StringBuffer();
		char etb = 23;
		char eot = '';
		char etx = 3;
		StringBuffer p = new StringBuffer("P|1||||^^^^|||||||||||||||||||||||||||||");
		// StringBuffer Order = new StringBuffer("O|1||");
		StringBuffer Order = new StringBuffer("O|1");
		List list = new ArrayList();
		StringBuffer frame = new StringBuffer();
		StringBuffer response = new StringBuffer();
		StringBuffer time = new StringBuffer();

		try {
			for (int i = 0; i < array.length; i++) {
				// stx = array[0];
				if (array[i] == '|') {
					CountPipe++;
				}
				if (CountPipe == 4) {
					if (!(array[i] == '|'))
						cobas.append(array[i]);
					// System.out.println("In Array "+array[j]);
				}
				if (CountPipe == 9) {
					if (!(array[i] == '|'))
						host.append(array[i]);

				}
				if (CountPipe == 10) {
					if (!(array[i] == '|'))
						mode.append(array[i]);

				}
				if (CountPipe == 13) {
					if (!(array[i] == '|'))
						time.append(array[i]);
				}
			}
			ServerCobas801.cobas = cobas;
			ServerCobas801.host = host;
			System.out.println("TIME:- " + time);
			// mode.replace(2, 5, "DWN"); //Replace TSREQ TO TSDWN
			/*
			 * if(=="TSREQ") { System.out.println("in tsreq");
			 * 
			 * }
			 */
			ServerCobas801.stx = stx;
			ServerCobas801.mode = mode;
			// MAKING STRING FOR REPLYING TO CLIENT MACHINE
			// SimpleDateFormat time_formatter1 = new SimpleDateFormat("yyyyMMddHHmmss");
			// String current_time_str1 =
			// time_formatter1.format(System.currentTimeMillis());

			while (true) {
				// buf.append(stx);
				// buf.append(line.subSequence(2, 14));
				/*
				 * buf.append(line.subSequence(2, 8)); buf.append(line.subSequence(13, 15));
				 * System.out.println("line.subSequence(1, 14) "+line.subSequence(2, 8));
				 * System.out.println("line.subSequence(1, 14) "+line.subSequence(13, 15));
				 * //buf.append("||");
				 * 
				 * 
				 * buf.append(host); buf.append("|||||"); buf.append(cobas); buf.append("|");
				 * buf.append(mode);
				 */
				// buf.append("|P|1|");
				// buf.append("20190110165906");
				buf.append(header);
				// buf.append(current_time_str); //Uncommnet it later
				// buf.append("|");
				buf.append(cr); // Ascii of CR
				// Ascii of LF
				p.append(cr);
				buf.append(p);
				// Order.append(Server.packet);

				Order.append("|");

				Order.append(ServerCobas801.sampleNo.toString().replace("^", ""));
				// Order.append("^50087^1^^S1^SC^|");

				https: // hmis.rcil.gov.in/HISInvestigationG5/new_investigation/masters/MachineInterface.jsp?type=2&eqp=100007&hos=33201&sam=2102OP0220&uid=4323

				Order.append("||^^^SARSCOV2|R|");

				Order.append(current_time_str);
				Order.append("");
				if (ServerCobas801.testCode != null) {
					System.out.println("TEST-CODE LIST IS NOT NULL");
					for (int k = 0; k < ServerCobas801.testCode.size(); k++)

					{
						Order.append("^^^");
						Order.append(ServerCobas801.testCode.get(k));
						if (k != ServerCobas801.testCode.size() - 1)
							Order.append("^\\");
						else
							Order.append("^");
					}
				} else
					System.out.println("NO TEST CODE FOUND !!! TEST-CODE LIST IS NULL");

				// Order.append(cr);
				buf.append(Order);
				buf.append("");
				// buf.append("20181227190037"+"|"+"20181227190037");
				buf.append("|");
				buf.append(line6);

				buf.append(cr);
				// buf.append(line3);

				// buf.append(cr);

				line4.append(cr);

				buf.append(line4);

				break;
			}

			// CODE TO DELETE LF IF EXISTS IN STRING *** STARTS ***
			/*
			 * for(int i=0;i<buf.length();i++) { if(buf.charAt(i)!=10)
			 * faltu.append(buf.charAt(i)); else System.out.println("yesssss"); }
			 * buf.delete(0, buf.length()); buf = faltu;
			 */
			// CODE TO DELETE LF IF EXISTS IN STRING *** ENDS ***

			System.out.println("STRING LENGTH BEFORE FRAMING = " + buf.length());
			int frameCount = 0, first = 0, mid = 0, remain = 0;
			frameCount = FrameCounter(buf.length());
			// System.out.println("Char At 0 "+buf.charAt(0));
			int length = buf.length();

			for (int i = 1; i <= frameCount; i++) {
				if (length < 240)
					mid = mid + length;
				else if (length == 240)
					mid = mid + length;
				else if (length > 240) {
					mid = mid + 240;
				}
				System.out.println("First " + first + "  " + "last " + mid + " stx  " + stx);
				frame.append(stx);
				// System.out.println("Server.StxCounter "+Server.StxCounter);
				frame.append(ServerCobas801.StxCounter);
				System.out.println("STX COUNTER:- " + ServerCobas801.StxCounter);
				ServerCobas801.StxCounter++;
				frame.append(buf.substring(first, mid));

				if (i == frameCount) {
					frame.append(etx);
					frame.append(CheckSum(frame.toString())); // FRAME WITH STX STX COUNTER AND ETX
					// frame.append(CheckSum(buf.substring(first, mid))); //FRAME WITH ONLY
					// CHARACTERS
					frame.append(cr);
					frame.append(lf);
				} else

				{
					frame.append(etb);
					frame.append(CheckSum(frame.toString())); // FRAME WITH STX STX COUNTER AND ETB
					// frame.append(CheckSum(buf.substring(first, mid)));
					frame.append(cr);
					frame.append(lf);
				}
				// length = length - mid;
				length = length - 240;
				first = mid;

				response.append(frame);
				frameList.add(frame.toString());
				/*
				 * if(i ==1) frame1.append(frame); else frame2.append(frame);
				 */
				frame.delete(0, frame.length());

			}
			// response.append(eot);
			frameList.add(eot);
			ServerCobas801.StxCounter = 1;
			newS.delete(0, newS.length());
			// System.out.println("Final String "+response);
			/*
			 * UUID uuid = UUID.randomUUID(); //for random file name String Path =
			 * "C:\\Log"; String filename = uuid.toString().replaceAll("[\\s\\-()]", "");
			 * Path = Path+"\\"+filename+".txt"; byte[] byy =
			 * response.toString().getBytes(); try (FileOutputStream fos = new
			 * FileOutputStream(Path)) { // File written on path fos.write(byy);
			 * //fos.close(); There is no more need for this line since you had created the
			 * instance of "fos" inside the try. And this will automatically close the
			 * OutputStream } catch(Exception e) { e.printStackTrace(); }
			 */

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		// Server.frameList = frameList;
		return frameList;
	}

	
	// ---------------------------------------------------
	// ---------------------------------------------------------------------------
	// Header parsing and Reply Packet Generation Method
	public static List headerParsing2500(String line) {
		char[] array = line.toCharArray();
		//System.out.println("arr"+array);
		int CountPipe = 0, j = 0;
		char cr = 13;
		char lf = 10;
		int instance = 0;
		char stx = '';
		String mode1 = "";

		List<String> pkt = new ArrayList<>();
		StringBuffer buf = new StringBuffer();
		StringBuffer cobas = new StringBuffer();
		StringBuffer host = new StringBuffer();
		StringBuffer mode = new StringBuffer();
		StringBuffer lines = new StringBuffer();

		// H|@^\|GXM-88424724426||CENTRAL RAILWAY HOSPITAL, SECUNDERABAD -
		// 110000888^GeneXpert^6.4||||| LIS||P|1394-97|20211229171329
		// P|1|||p1
		// O|1|2912OP0250||^^^SARSCOV2|R|20071116133208|||||A||||ORH||||||||||Q

		// O|1||2912OP0250||^^SARSCOV2|R|20211229183655||||||A||||ORH||||||||||Q
		// O|1||2912OP0250|||R|20211229183655||||||P||||ORH||||||||||I
		// L|1|F

		// StringBuffer header = new StringBuffer("H|\\^&|||host|||||cobas
		// 8000^1.06|TSDWN|P|1|");
		SimpleDateFormat time_formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String current_time_str = time_formatter.format(System.currentTimeMillis());

		StringBuffer header = new StringBuffer(
				"H|\\^&|||||||||||E1394-97");//"1H|\\^&|||XN-20^00-01^11001^^^^12345678||||||||E1394-97");

		//header = header.append(current_time_str);
	//	StringBuffer line3 = new StringBuffer("C|1|L|^^^^|G");
		StringBuffer line4 = new StringBuffer("L|1|N");
		StringBuffer line6 = new StringBuffer("|||||N|||||||||||||||||||");
		StringBuffer line5 = new StringBuffer();
		char etb = 23;
		char eot = '';
		char etx = 3;
		StringBuffer p = new StringBuffer("P|1||||^^|||||||||||||||||||||||||||||");
		
		//add pat name
		
		//int a=p.toString().indexOf("^");
		//p.insert(a+1, Server.name_pat.trim());
		//System.out.println("p"+p);
		
		// StringBuffer Order = new StringBuffer("O|1||");
		StringBuffer Order = new StringBuffer("O|1");
		List list = new ArrayList();
		StringBuffer frame = new StringBuffer();
		StringBuffer response = new StringBuffer();
		StringBuffer time = new StringBuffer();

		try {
			for (int i = 0; i < array.length; i++) {
				// stx = array[0];
				if (array[i] == '|') {
					CountPipe++;
				}
				if (CountPipe == 4) {
					if (!(array[i] == '|'))
						cobas.append(array[i]);
					// System.out.println("In Array "+array[j]);
				}
				if (CountPipe == 9) {
					if (!(array[i] == '|'))
						host.append(array[i]);

				}
				if (CountPipe == 10) {
					if (!(array[i] == '|'))
						mode.append(array[i]);

				}
				if (CountPipe == 13) {
					if (!(array[i] == '|'))
						time.append(array[i]);
				}
			}
			ServerCobas801.cobas = cobas;
			ServerCobas801.host = host;
			System.out.println("TIME:- " + time);
			// mode.replace(2, 5, "DWN"); //Replace TSREQ TO TSDWN
			/*
			 * if(=="TSREQ") { System.out.println("in tsreq");
			 * 
			 * }
			 */
			ServerCobas801.stx = stx;
			ServerCobas801.mode = mode;
			// MAKING STRING FOR REPLYING TO CLIENT MACHINE
			// SimpleDateFormat time_formatter1 = new SimpleDateFormat("yyyyMMddHHmmss");
			// String current_time_str1 =
			// time_formatter1.format(System.currentTimeMillis());

			while (true) {
				// buf.append(stx);
				// buf.append(line.subSequence(2, 14));
				/*
				 * buf.append(line.subSequence(2, 8)); buf.append(line.subSequence(13, 15));
				 * System.out.println("line.subSequence(1, 14) "+line.subSequence(2, 8));
				 * System.out.println("line.subSequence(1, 14) "+line.subSequence(13, 15));
				 * //buf.append("||");
				 * 
				 * 
				 * buf.append(host); buf.append("|||||"); buf.append(cobas); buf.append("|");
				 * buf.append(mode);
				 */
				// buf.append("|P|1|");
				// buf.append("20190110165906");
				buf.append(header);
				// buf.append(current_time_str); //Uncommnet it later
				// buf.append("|");
				buf.append(cr); // Ascii of CR
				// Ascii of LF
				//p.append(cr);
				buf.append(p);
				// Order.append(Server.packet);
			
				Order.append("|");
				
				Order.append("^");
				Order.append("^");
				Order.append(ServerCobas801.sampleNo.toString().replace("^", "").trim());
				// Order.append("^50087^1^^S1^SC^|");
				Order.append("|");
				Order.append("|");
				
				https: // hmis.rcil.gov.in/HISInvestigationG5/new_investigation/masters/MachineInterface.jsp?type=2&eqp=100007&hos=33201&sam=2102OP0220&uid=4323

				//Order.append("||^^^SARSCOV2|R|");

				//Order.append(current_time_str); by Rohit..
				Order.append("");
				if (ServerCobas801.testCode != null) {
					System.out.println("TEST-CODE LIST IS NOT NULL");
					for (int k = 0; k < ServerCobas801.testCode.size(); k++)

					{
						Order.append("^^^");
						Order.append(ServerCobas801.testCode.get(k));
						if (k != ServerCobas801.testCode.size() - 1)
							Order.append("^^\\");
						else
							Order.append("^^");
					}
				} else
					System.out.println("NO TEST CODE FOUND !!! TEST-CODE LIST IS NULL");
				Order.append("|");
				Order.append("R");
				Order.append("|");
				Order.append(current_time_str);
				//Order.append("|");
				// Order.append(cr);
				buf.append(Order);
				buf.append("");
				// buf.append("20181227190037"+"|"+"20181227190037");
				buf.append("|");
				buf.append(line6);

				buf.append(cr);
				// buf.append(line3);

				// buf.append(cr);

				//line4.append(cr);

				buf.append(line4);
//frame.append(buf);
				Order.append(line6);
				pkt.add(header.toString());
				pkt.add(p.toString());				
				pkt.add(Order.toString());
				

				pkt.add(line4.toString());
				break;
			}

			// CODE TO DELETE LF IF EXISTS IN STRING *** STARTS ***
			/*
			 * for(int i=0;i<buf.length();i++) { if(buf.charAt(i)!=10)
			 * faltu.append(buf.charAt(i)); else System.out.println("yesssss"); }
			 * buf.delete(0, buf.length()); buf = faltu;
			 */
			// CODE TO DELETE LF IF EXISTS IN STRING *** ENDS ***

//			System.out.println("STRING LENGTH BEFORE FRAMING = " + buf.length());
//			int frameCount = 0, first = 0, mid = 0, remain = 0;
//			frameCount = FrameCounter(buf.length());
//			// System.out.println("Char At 0 "+buf.charAt(0));
			
			String abc ="";		
			//int length = buf.length();
			
			
			
			
			
			
			
//for(String line1:pkt) 


//{
//	System.out.println("STRING LENGTH BEFORE FRAMING = " + line1.length());
	int frameCount = 0, first = 0, mid = 0, remain = 0;
//	frameCount = FrameCounter(line1.length());
//	System.out.println("line"+line1);
//	int length = line1.length();
//			//for (int i = 1; i <= frameCount; i++) {
//				if (length < 240)
//					mid = mid + length;
//				else if (length == 240)
//					mid = mid + length;
//				else if (length > 240) {
//					mid = mid + 240;
//				}
				//System.out.println("First " + first + "  " + "last " + mid + " stx  " + stx);
				frame.append(stx);
				// System.out.println("Server.StxCounter "+Server.StxCounter);
				frame.append(ServerCobas801.StxCounter);
				//System.out.println("STX COUNTER:- " + Server.StxCounter);
				//Server.StxCounter++;
				//frame.append(buf.substring(first, mid));
//frame.append(line1);
				
				//if (i == frameCount) {
					frame.append(etx);
					//header.toString().replace("H", stx+Server.StxCounter+"H");
//					header.append(stx);
//					Order.append(stx);
//					p.append(stx);
//					line4.append(stx);
					
					
					header.insert(0, stx);
					header.insert(1, 1);
					
					Order.insert(0, stx);
					Order.insert(1, 3);
					
					p.insert(0,stx);
					p.insert(1, 2);
				
					line4.insert(0, stx);
					line4.insert(1, 4);
					header.append(etx);
					Order.append(etx);
					p.append(etx);
					line4.append(etx);
					header.append(CheckSum(header.toString())); // FRAME WITH STX STX COUNTER AND ETX
				
//					for(int i=0;i<pkt.size();i++) {
//						
//						Server.StxCounter++;
//					}
//					
					
					// frame.append(CheckSum(buf.substring(first, mid))); //FRAME WITH ONLY
					// CHARACTERS
					//String b=CheckSum(frame.toString());
			//abc.concat(line1.concat(b));
					p.append(CheckSum(p.toString())); // FRAME WITH STX STX COUNTER AND ETX
					
					line4.append(CheckSum(line4.toString())); // FRAME WITH STX STX COUNTER AND ETX
					Order.append(CheckSum(Order.toString())); // FRAME WITH STX STX COUNTER AND ETX
					
					
					//frame.append(cr);
					//frame.append(lf);
				//} else

				{
					//frame.append(etb);
					//frame.append(CheckSum(frame.toString())); // FRAME WITH STX STX COUNTER AND ETB
					// frame.append(CheckSum(buf.substring(first, mid)));
					//frame.append(cr);
					//frame.append(lf);
					//String b=CheckSum(frame.toString());
					//abc.concat(line1.concat(b));
				}
				// length = length - mid;
//				length = length - 240;
				first = mid;
				
			//}
			frameList.add('');
		frameList.add(header);
		frameList.add(p);
		frameList.add(Order);
		frameList.add(line4);
		abc="";
			
//}

//System.out.println("framelist"+frameList);
		//	System.out.println("frame"+frame.toString());
				response.append(frame);
//				frameList.add(frame.toString());
				/*
				 * if(i ==1) frame1.append(frame); else frame2.append(frame);
				 */
				frame.delete(0, frame.length());

			

			// response.append(eot);
			frameList.add(eot);
			ServerCobas801.StxCounter = 1;
			newS.delete(0, newS.length());
			// System.out.println("Final String "+response);
			/*
			 * UUID uuid = UUID.randomUUID(); //for random file name String Path =
			 * "C:\\Log"; String filename = uuid.toString().replaceAll("[\\s\\-()]", "");
			 * Path = Path+"\\"+filename+".txt"; byte[] byy =
			 * response.toString().getBytes(); try (FileOutputStream fos = new
			 * FileOutputStream(Path)) { // File written on path fos.write(byy);
			 * //fos.close(); There is no more need for this line since you had created the
			 * instance of "fos" inside the try. And this will automatically close the
			 * OutputStream } catch(Exception e) { e.printStackTrace(); }
			 */

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		// Server.frameList = frameList;
		return frameList;
	}

	// Header parsing and Reply Packet Generation Method
	public static List headerParsing_GEN_MTB(String line) {
		char[] array = line.toCharArray();
		int CountPipe = 0, j = 0;
		char cr = 13;
		char lf = 10;
		int instance = 0;
		char stx = '';
		String mode1 = "";
		StringBuffer buf = new StringBuffer();
		StringBuffer cobas = new StringBuffer();
		StringBuffer host = new StringBuffer();
		StringBuffer mode = new StringBuffer();
		StringBuffer lines = new StringBuffer();

		// H|@^\|GXM-88424724426||CENTRAL RAILWAY HOSPITAL, SECUNDERABAD -
		// 110000888^GeneXpert^6.4||||| LIS||P|1394-97|20211229171329
		// P|1|||p1
		// O|1|2912OP0250||^^^SARSCOV2|R|20071116133208|||||A||||ORH||||||||||Q

		// O|1||2912OP0250||^^SARSCOV2|R|20211229183655||||||A||||ORH||||||||||Q
		// O|1||2912OP0250|||R|20211229183655||||||P||||ORH||||||||||I
		// L|1|F

		// StringBuffer header = new StringBuffer("H|\\^&|||host|||||cobas
		// 8000^1.06|TSDWN|P|1|");
		SimpleDateFormat time_formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String current_time_str = time_formatter.format(System.currentTimeMillis());

		StringBuffer header1 = new StringBuffer(
				"H|@^\\|GXM-88424724426|| LIS|||||CENTRAL RAILWAY HOSPITAL, SECUNDERABAD - 110000888^GeneXpert^6.4||P|1394-97|");

		StringBuffer header = new StringBuffer("H|@^\\|");
		header.append(Query_Message_Id);
		header.append("|| LIS|||||CENTRAL RAILWAY HOSPITAL, SECUNDERABAD - 110000888^GeneXpert^6.4||P|1394-97|");

		header = header.append(current_time_str);
		StringBuffer line3 = new StringBuffer("C|1|L|^^^^|G");
		StringBuffer line4 = new StringBuffer("L|1|F");
		// StringBuffer line6 = new StringBuffer("||||P||||ORH||||||||||Q");
		StringBuffer line6 = new StringBuffer("||||A||||ORH||||||||||Q");
		StringBuffer line5 = new StringBuffer();
		char etb = 23;
		char eot = '';
		char etx = 3;
		StringBuffer p = new StringBuffer("P|1||||^^^^|||||||||||||||||||||||||||||");
		// StringBuffer Order = new StringBuffer("O|1||");
		StringBuffer Order = new StringBuffer("O|1");
		StringBuffer Order1 = new StringBuffer("O|2");
		List list = new ArrayList();
		StringBuffer frame = new StringBuffer();
		StringBuffer response = new StringBuffer();
		StringBuffer time = new StringBuffer();

		try {
			for (int i = 0; i < array.length; i++) {
				// stx = array[0];
				if (array[i] == '|') {
					CountPipe++;
				}
				if (CountPipe == 4) {
					if (!(array[i] == '|'))
						cobas.append(array[i]);
					// System.out.println("In Array "+array[j]);
				}
				if (CountPipe == 9) {
					if (!(array[i] == '|'))
						host.append(array[i]);

				}
				if (CountPipe == 10) {
					if (!(array[i] == '|'))
						mode.append(array[i]);

				}
				if (CountPipe == 13) {
					if (!(array[i] == '|'))
						time.append(array[i]);
				}
			}
			ServerCobas801.cobas = cobas;
			ServerCobas801.host = host;
			System.out.println("TIME:- " + time);
			// mode.replace(2, 5, "DWN"); //Replace TSREQ TO TSDWN
			/*
			 * if(=="TSREQ") { System.out.println("in tsreq");
			 * 
			 * }
			 */
			ServerCobas801.stx = stx;
			ServerCobas801.mode = mode;
			// MAKING STRING FOR REPLYING TO CLIENT MACHINE
			// SimpleDateFormat time_formatter1 = new SimpleDateFormat("yyyyMMddHHmmss");
			// String current_time_str1 =
			// time_formatter1.format(System.currentTimeMillis());

			while (true) {
				// buf.append(stx);
				// buf.append(line.subSequence(2, 14));
				/*
				 * buf.append(line.subSequence(2, 8)); buf.append(line.subSequence(13, 15));
				 * System.out.println("line.subSequence(1, 14) "+line.subSequence(2, 8));
				 * System.out.println("line.subSequence(1, 14) "+line.subSequence(13, 15));
				 * //buf.append("||");
				 * 
				 * 
				 * buf.append(host); buf.append("|||||"); buf.append(cobas); buf.append("|");
				 * buf.append(mode);
				 */
				// buf.append("|P|1|");
				// buf.append("20190110165906");
				buf.append(header);
				// buf.append(current_time_str); //Uncommnet it later
				// buf.append("|");
				buf.append(cr); // Ascii of CR
				// Ascii of LF
				p.append(cr);
				buf.append(p);
				// Order.append(Server.packet);

				Order.append("|");

				Order.append(ServerCobas801.sampleNo.toString().replace("^", ""));
				// Order.append("^50087^1^^S1^SC^|");
				// Order.append("||^^^SARSCOV2|R|");

				Order.append("||^^^");
				Order.append(Test_Code);
				Order.append("|R|");
				Order.append(current_time_str);
				Order.append("");
				// ---------SECOND ORDER
				Order1.append("|");
				Order1.append(ServerCobas801.sampleNo.toString().replace("^", ""));
				// Order.append("^50087^1^^S1^SC^|");
				Order1.append("||^^^MTBRIF|R|");
				Order1.append(current_time_str);
				Order1.append("");
				// -----------------------------
				if (ServerCobas801.testCode != null) {
					System.out.println("TEST-CODE LIST IS NOT NULL");
					for (int k = 0; k < ServerCobas801.testCode.size(); k++)

					{
						Order.append("^^^");
						Order.append(ServerCobas801.testCode.get(k));
						Order1.append("^^^");
						Order1.append(ServerCobas801.testCode.get(k));
						if (k != ServerCobas801.testCode.size() - 1) {
							Order.append("^\\");
							Order1.append("^\\");
						} else {
							Order.append("^");
							Order1.append("^");
						}
					}
				} else
					System.out.println("NO TEST CODE FOUND !!! TEST-CODE LIST IS NULL");

				// Order.append(cr);
				buf.append(Order);

				buf.append("");
				// buf.append("20181227190037"+"|"+"20181227190037");
				buf.append("|");
				buf.append(line6);

				// ===============SECOND ORDER PACKET ADD TO BUF

				// buf.append(cr); // Ascii of CR
				// Ascii of LF
				// Order1.append(cr);
				// buf.append(Order1);
				// buf.append("");
				// buf.append("20181227190037"+"|"+"20181227190037");
				// buf.append("|");
				// buf.append(line6);
				// -----------------------------------------------
				// ==============================================
				buf.append(cr);
				// buf.append(line3);

				// buf.append(cr);

				line4.append(cr);

				buf.append(line4);

				break;
			}

			// CODE TO DELETE LF IF EXISTS IN STRING *** STARTS ***
			/*
			 * for(int i=0;i<buf.length();i++) { if(buf.charAt(i)!=10)
			 * faltu.append(buf.charAt(i)); else System.out.println("yesssss"); }
			 * buf.delete(0, buf.length()); buf = faltu;
			 */
			// CODE TO DELETE LF IF EXISTS IN STRING *** ENDS ***

			System.out.println("STRING LENGTH BEFORE FRAMING = " + buf.length());
			// STRING LENGTH BEFORE FRAMING = 235
			int frameCount = 0, first = 0, mid = 0, remain = 0;
			frameCount = FrameCounter(buf.length());
			// System.out.println("Char At 0 "+buf.charAt(0));
			int length = buf.length();

			for (int i = 1; i <= frameCount; i++) {
				// if(length<240)
				if (length < 306)
					mid = mid + length;
				else if (length == 306)
					mid = mid + length;
				else if (length > 306) {
					mid = mid + 306;
				}
				System.out.println("First " + first + "  " + "last " + mid + " stx  " + stx);
				frame.append(stx);
				// System.out.println("Server.StxCounter "+Server.StxCounter);
				frame.append(ServerCobas801.StxCounter);
				System.out.println("STX COUNTER:- " + ServerCobas801.StxCounter);
				ServerCobas801.StxCounter++;
				frame.append(buf.substring(first, mid));

				if (i == frameCount) {
					frame.append(etx);
					frame.append(CheckSum(frame.toString())); // FRAME WITH STX STX COUNTER AND ETX
					// frame.append(CheckSum(buf.substring(first, mid))); //FRAME WITH ONLY
					// CHARACTERS
					frame.append(cr);
					frame.append(lf);
				} else

				{
					frame.append(etb);
					frame.append(CheckSum(frame.toString())); // FRAME WITH STX STX COUNTER AND ETB
					// frame.append(CheckSum(buf.substring(first, mid)));
					frame.append(cr);
					frame.append(lf);
				}
				// length = length - mid;
				length = length - 240;
				first = mid;

				response.append(frame);
				frameList.add(frame.toString());
				/*
				 * if(i ==1) frame1.append(frame); else frame2.append(frame);
				 */
				frame.delete(0, frame.length());

			}
			// response.append(eot);
			frameList.add(eot);
			ServerCobas801.StxCounter = 1;
			newS.delete(0, newS.length());
			// System.out.println("Final String "+response);
			/*
			 * UUID uuid = UUID.randomUUID(); //for random file name String Path =
			 * "C:\\Log"; String filename = uuid.toString().replaceAll("[\\s\\-()]", "");
			 * Path = Path+"\\"+filename+".txt"; byte[] byy =
			 * response.toString().getBytes(); try (FileOutputStream fos = new
			 * FileOutputStream(Path)) { // File written on path fos.write(byy);
			 * //fos.close(); There is no more need for this line since you had created the
			 * instance of "fos" inside the try. And this will automatically close the
			 * OutputStream } catch(Exception e) { e.printStackTrace(); }
			 */

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		// Server.frameList = frameList;
		return frameList;
	}
//=------------------------------------------- vitrose 5600 parse select

	// -----------------------------------------------

	// vitrose5600
	public static List common_parseSelect(List<String> list) {
		
		System.out.println("inside comman parse select...");
		StringBuffer packet = new StringBuffer();
		List<StringBuffer> frameList = new ArrayList<>();
		List testCode = new ArrayList();
	//	System.out.println("list"+list);
		String line_header = list.get(0);
		//String line = list.get(2);
		String line = list.get(1);
		Map<String, String> testCodeMap = new LinkedHashMap<String, String>();
		int count = 0;
		StringBuffer bf = new StringBuffer();
		StringBuffer bf1 = new StringBuffer();

		int count_header = 0;
		StringBuffer bf_header = new StringBuffer();
		StringBuffer bf_header_sender = new StringBuffer();
		StringBuffer bf_header_receiver = new StringBuffer();
		StringBuffer bf_header_datetime = new StringBuffer();
		ServerCobas801.testCode.clear();
		Sample_code.clear();

		// get message id
		if (Character.toUpperCase(line_header.charAt(2)) == 'H') {
			char[] ch = line_header.toCharArray();

			
			String dd []= line_header.split("\\|");
			
		//	System.out.println(dd);
					
			
			headerVal=dd[2];
			String abc=headerVal.substring(0,3);
			String a=abc;
		//int n=	Integer.parseInt(abc);
	//	n= n+2;
		//abc= Stringof(n);
		//headerVal=headerVal.replace(a, abc);
			//System.out.println("RRR"+headerVal);
			if (Character.toUpperCase(line_header.charAt(2)) == 'H') {

				for (int i = 0; i < ch.length; i++) {
					if (ch[i] == '|')
						count_header++;
					if (count_header == 2) {
						if (!(ch[i] == '|'))
							bf_header.append(ch[i]);// header msg id
						// System.out.println("bf_header:- " + bf_header);
					}
					// ------------bf_header_sender
					if (count_header == 4) {
						if (!(ch[i] == '|'))
							bf_header_sender.append(ch[i]);// header msg id
						// System.out.println("bf_header_sender:- " + bf_header_sender);
					}

					// ------------bf_header_reciver
					if (count_header == 9) {
						if (!(ch[i] == '|'))
							bf_header_receiver.append(ch[i]);// header msg id
						// System.out.println("bf_header_receiver:- " + bf_header_receiver);
					}
					if (count_header == 13) {
						if (!(ch[i] == '|'))
							bf_header_datetime.append(ch[i]);// header msg id
						// System.out.println("bf_header_receiver:- " + bf_header_receiver);
					}
				}
			}
		}
		// System.out.println("bf_header:- " + bf_header);
		// System.out.println("bf_header_sender:- " + bf_header_sender);
		// System.out.println("bf_header_receiver:- " + bf_header_receiver);
		// System.out.println("bf_header_datetime:- " + bf_header_datetime);
		// get message id

//		long num = Long.parseLong(bf_header_datetime.toString());
//		long last_digit = num % 10; // extract the last digit
//		num /= 10; // modify the original number
//		long second_to_last_digit = num % 10; // extract second last digit
//		String str = String.valueOf(second_to_last_digit) + String.valueOf(last_digit); // convert and add strings
//
//		System.out.println("str==========vvvvvvvvvvvvvvvvv=========" + str); // print desired string
//		long num1 = Long.parseLong(str);
//		long num2 = num1 + 2;
//		long num3;
//		String bc = String.valueOf(num2);
//		System.out.println("bc======" + bc);
//		System.out.println("num2======" + num2);
//		if ((String.valueOf(num2)).length() == 1) {
//			String ab = "0";
//			bc = String.valueOf(0).concat(String.valueOf(num2));
//			num3 = Long.parseLong(bc);
//			num2 = num3;
//			System.out.println("updated  bc======" + bc);
//
//		}
//		System.out.println("updatednum2======" + num2);
//
//		String ab = bf_header_datetime.toString().substring(0, bf_header_datetime.toString().length() - 2);
//		bf_header_datetime.delete(0, bf_header_datetime.length());
//		bf_header_datetime.append(ab.concat(String.valueOf(bc)));
//		System.out.println("ab==========vvvvvvvvv111111111111111111=========" + ab);
//		System.out.println("bf_header_datetime==========vvvvvvvvv111111111111111111=========" + bf_header_datetime);

		if (Character.toUpperCase(line.charAt(2)) == 'Q' || line.contains("Q|1|") ) {
			char[] ch = line.toCharArray();

			for (int i = 0; i < ch.length; i++) {

				if (ch[i] == '|')
					count++;

				if (count == 2) {
					if (!(ch[i] == '|'))
						bf.append(ch[i]);
				}

			}

			System.out.println("Sample No String from Query Packet:- " + bf);
			
			String[] str=bf.toString().split("\\^");
			 strbuf = new StringBuffer(str[2]);
		//	bf=strbuf;

		}
		
		
		ServerCobas801.Sample_code.add(bf.toString());
		// ---------------------------------------------------

		
		System.out.println("Sample No :"+ strbuf.toString());
		String line2 =  ABC.getSampleDtl_biolis50i(strbuf.toString().replace("^", ""));  // Get Query Information Vipul Ramji
		//RamjiMask BA400 15-Mar-2024		System.out.println("Response after Test Code  Selection---- " + line2); //null#null#null#null#230206BIO0335#
		int r = 0;
		String tc = "";
		// line2="1005;1268;1094;1065;1023;1021;1020;1406;1400;2002;1103;1101;1102;1069;1005;1268;1094;1065;1023;1021;1020;1406;1400;2002;1103;1101;1102;654;797;248;#Asitabh    #39 Yr     #F#230413BIO0554#"; // ALinity Check Ramji
//	line2="UREA-BUN-UV;ALBUNIM;NA;K+;CL-;BUN;PROTEIN TOTAL#RRR";
//		line2=	"ALBUMIN;BILI TOTAL DPD;BILI DIRECT DPD;ALT-GPT;ALP-AMP;PROTEIN TOTAL;AST-GOT;GLOBULIN;ALB/GLOB;GAMMA-GT;NA;BILI-INDIRECT;UREA-BUN-UV;Na+;K+;Cl-;NA;BUN;CREATININE;URIC ACID;#Penumaka Ravikumar    (Aarogyasri)#33 Yr     #M#0315240039#379132400351771#";
		String[] kvPairs = line2.split("#");
		for (String kvPair : kvPairs) {
			// System.out.print("kvPairs =============------ " + kvPair);
			if (r == 0)
				tc = kvPair;
			if (r == 1)
				name_pat = kvPair;
			r++;
			if (r == 2)
				break;

		}
		// System.out.print("Test_Code NOT Found- "+tc);
		 if((tc != null) && !tc.equals("null"))
		 {	 
		 // -----------
		//String[] kv1 = tc.split(":");
//		String[] kv2 = kv1[1].split(";");
		String[] kv2 = tc.split(";");
		for (String z : kv2) 
		{
			if(!(z.equals("") ||z.equals("null4") ||z.equals("null5") ||z.equals("null6") ||z.equals("NA")||z.equals("VV")||z.equals("WW")||z.equals("XX")||z.equals("YY")||z.equals("ZZ"))) {
				
				if (Server.testCode.contains(z)) {
		           // System.out.println(z + " is in the list.");
		        } else {
		        	Server.testCode.add(z);
					}
					
			}
		
		}
		
		
		//RamjiMask BA400 15-Mar-2024		System.out.println("Test_Code =============------ " + Server.testCode);

		String testCode1 = "";
		String testValue1 = "";
	//	-------------------------------------------
//		name_pat="Employee General Test";
	//	System.out.println("name_pat11111 =============------ " + name_pat);

		String[] nameP = name_pat.split(" ");
		if(nameP.length>=1) {
	//		System.out.println("nameP.length =============------ " + nameP.length);
		if(nameP.length==1) {
			name_pat_first=nameP[0];
		
	//		System.out.println("name_pat_first =============------ " + name_pat_first+"========"+name_pat_last);
			
		}
		if(nameP.length==2) {
			name_pat_first=nameP[0];
			name_pat_last=nameP[1];		
		//	System.out.println("name_pat_first =============------ " + name_pat_first+"========"+name_pat_last);

		}
		if(nameP.length==3) {
			name_pat_first=nameP[0];
			name_pat_last=nameP[2];		
	//		System.out.println("name_pat_first =============------ " + name_pat_first+"========"+name_pat_last);

		}
		}//TC
	}



		ServerCobas801.packet = packet;
		ServerCobas801.sampleNo = bf;
		ServerCobas801.Query_Message_Id = bf_header;
		ServerCobas801.Query_Message_sender = bf_header_sender;
		ServerCobas801.Query_Message_receiver = bf_header_receiver;
		ServerCobas801.Query_Message_date_time = bf_header_datetime;
		// Make Packet for Reply
		for (int i = 0; i < list.size(); i++) {
			if (Character.toUpperCase(list.get(i).charAt(0)) == 'H')
				;
			{
				if (formatid.equals("20001"))// genexpert
				{
					frameList = headerParsing(list.get(0));
				}

				if (formatid.equals("20001"))// sys800i
				{
					frameList = headerParsing(list.get(0));
				}
				if (formatid.equals("20002"))// genexpert
				{
					frameList = headerParsing_GEN_MTB(list.get(0));
				}

				if (formatid.equals("20"))// genexpert
				{
					frameList = headerParsing_GEN_MTB(list.get(0));// gims proxl
				}
			
				if (formatid.equals("20011"))// headerParsing_vitrose5600
				{
				
					frameList = headerParsing_vitrose5600_railtel(list.get(0));// vitrose5600
				}
				
				if (formatid.equals("20043"))// tecom tc6060
				{
				
					frameList = headerParsing_tecom_TC6060_singlePacket(list.get(0));// tecom
				}
				if (formatid.equals("20045"))// mispa clinia BA 400 format id 20045 for aiims manglagiri
				{
					frameList = headerParsing_Mispa_Clinia_s(list.get(0));// mispa clinia
				}
				if (formatid.equals("20046"))// 
				{
					 frameList = cobas6000_headerParsing_s(); ///cobas AIIMS BH
				
				}

				
				
			}
			
			break;
		}
		if (formatid.equals("20012"))// headerParsing_vitrose5600
		{
		
			frameList = sysmex1000_headerParsing_m();// vitrose5600
		}
		
		// System.out.println("PAcket:-----"+packet);
		return frameList;
	}
	private static String Stringof(int n) {
	// TODO Auto-generated method stub
	return null;
}

	// ------------------------------------------------------------


	// PACKET Format id=20043
	public static List headerParsing_tecom_TC6060_singlePacket(String line) {
		SimpleDateFormat time_formatter2 = new SimpleDateFormat("yyyyMMddHHmmss");
		String current_time_str2 = time_formatter2.format(System.currentTimeMillis());
		System.out.println("current_time_str2---" + current_time_str2);
		ServerCobas801.StxCounter = 1;
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException ex) {
//			Thread.currentThread().interrupt();
//		}
		SimpleDateFormat time_formatter3 = new SimpleDateFormat("yyyyMMddHHmmss");
		String current_time_str3 = time_formatter3.format(System.currentTimeMillis());
		System.out.println("current_time_str2 after 1 second wait---" + current_time_str3);
	
		int CountPipe = 0, j = 0;
		char cr = 13;
		char lf = 10;
		int instance = 0;
		char stx = '';
		String mode1 = "";
		StringBuffer buf = new StringBuffer();
		StringBuffer cobas = new StringBuffer();
		StringBuffer host = new StringBuffer();
		StringBuffer mode = new StringBuffer();
		StringBuffer lines = new StringBuffer();


		SimpleDateFormat time_formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String current_time_str = time_formatter.format(System.currentTimeMillis());

		StringBuffer header1 = new StringBuffer(
				"H|@^\\|GXM-88424724426|| LIS|||||CENTRAL RAILWAY HOSPITAL, SECUNDERABAD - 110000888^GeneXpert^6.4||P|1394-97|");

		StringBuffer header = new StringBuffer("H|\\^&|");
//		header.append(Query_Message_Id);
		header.append("||");
		header.append("^^");
		// header.append(Query_Message_receiver);
		header.append("|||||");
//		header.append(Query_Message_sender);
		header.append("||SA|1394-97|");
//		header = header.append(Query_Message_date_time);
		header = header.append(current_time_str);
		StringBuffer line3 = new StringBuffer("C|1|L|^^^^|G");
		StringBuffer line4 = new StringBuffer("L|1|N");
		StringBuffer line6 = new StringBuffer("|||F");
		StringBuffer line5 = new StringBuffer();
		char etb = 23;
		char eot = '';
		char etx = 3;
		counterPID_Packet = counterPID_Packet + 33;
	
		
		StringBuffer p = new StringBuffer("P|");
		p.append(p_packet_count);
		p.append("||");
		p.append(ServerCobas801.Sample_code.get(0).toString().replace("^", ""));
		p.append("||");
		p.append(name_pat.trim());
		// p.append("^name^M");
		p.append("|||||||||||||");
		p.append("||||||||||||||||");
		
		StringBuffer c = new StringBuffer("C|1|I|Patient is complaining of shortness of breath and chest pain.|G");


		StringBuffer Order = new StringBuffer();
		StringBuffer Order1 = new StringBuffer("O|2");
		List list = new ArrayList();
		StringBuffer frame = new StringBuffer();
		StringBuffer framevip = new StringBuffer();
		StringBuffer response = new StringBuffer();
		StringBuffer time = new StringBuffer();

		try {
		
			ServerCobas801.cobas = cobas;
			ServerCobas801.host = host;
			System.out.println("TIME:- " + time);
		
			ServerCobas801.stx = stx;
			ServerCobas801.mode = mode;
	

			while (true) {

				buf.append(header);
				// buf.append(current_time_str); //Uncommnet it later
				// buf.append("|");
				buf.append(cr);
				// Ascii of CR
				// Ascii of LF
				p.append(cr);
				buf.append(p);
				c.append(cr);
			
				// ==============================

				System.out.println("BA400_client.testCode.size()------- " + ServerCobas801.testCode.size());
				
				Order.append("O|1");
				// Order.append();
				Order.append("|^^|");
				Order.append(ServerCobas801.Sample_code.get(0).toString().replace("^", ""));
				Order.append("|");
				for (int k = 0; k < ServerCobas801.testCode.size(); k++)
				{
					Order.append(k + 1);
					Order.append("^");
					Order.append(ServerCobas801.testCode.get(k));
					Order.append("^^");
					if (k != ServerCobas801.testCode.size() - 1) {

						Order.append("\\");
						// Order.append("^^^");

					}
					/*
					 * else { Order.append("^");
					 * 
					 * }
					 */
				}
				Order.append("|R|");
				Order.append("|");
				Order.append("||||||||Serum|||1|||||||O|||||");
				// Order.append(line6);
				Order.append(cr);

				buf.append(Order);
				Order.delete(0, Order.length());

				//RamjiMask BA400 15-Mar-2024	System.out.println("buf= " + buf.toString());

				// ==============================================================
			

				line4.append(cr);

				buf.append(line4);

				break;
			}


			//RamjiMask BA400 15-Mar-2024		System.out.println("STRING LENGTH BEFORE FRAMING = " + buf.length());
			//RamjiMask BA400 15-Mar-2024 		System.out.println("frame BEFORE FRAMING = " + buf.toString());
			// STRING LENGTH BEFORE FRAMING = 235
			int frameCount = 0, first = 0, mid = 0, remain = 0;
			frameCount = FrameCounter(buf.length());
			// System.out.println("Char At 0 "+buf.charAt(0));
			int length = buf.length();

			for (int i = 1; i <= frameCount; i++) {
				if (length < 240)
					// if (length < 306)
					mid = mid + length;
				else if (length == 240)
					mid = mid + length;
				else if (length > 240) {
					mid = mid + 240;
				}
				//RamjiMask BA400 15-Mar-2024			System.out.println("First " + first + "  " + "last " + mid + " stx  " + stx);
				frame.append(stx);
				//RamjiMask BA400 15-Mar-2024			System.out.println("Server.StxCounter " + Server.StxCounter);
				frame.append(ServerCobas801.StxCounter);
				//RamjiMask BA400 15-Mar-2024			System.out.println("STX COUNTER:- " + Server.StxCounter);
				ServerCobas801.StxCounter++;
				frame.append(buf.substring(first, mid));

				if (i == frameCount) {
					frame.append(etx);
					System.out.println("before checksum" + frame.toString());
					frame.append(CheckSum(frame.toString())); // FRAME WITH STX STX COUNTER AND ETX
					// frame.append(CheckSum(buf.substring(first, mid))); //FRAME WITH ONLY
					// CHARACTERS
					frame.append(cr);
					frame.append(lf);
				} else

				{
					frame.append(etb);
					System.out.println("before checksum" + frame.toString());
					frame.append(CheckSum(frame.toString())); // FRAME WITH STX STX COUNTER AND ETB
					// frame.append(CheckSum(buf.substring(first, mid)));
					frame.append(cr);
					frame.append(lf);
				}
				// length = length - mid;
				length = length - 240;
				first = mid;

				response.append(frame);
				frameList.add(frame.toString());
				/*
				 * if(i ==1) frame1.append(frame); else frame2.append(frame);
				 */
				frame.delete(0, frame.length());

			}
			// response.append(eot);
			// frameList.add(eot);
			ServerCobas801.StxCounter = 1;
			newS.delete(0, newS.length());
	//		System.out.println("RJG BA400 Final String after checksum" + frameList.size());
			for (int i = 0; i < frameList.size(); i++) {
				count_ack_BA400_o1++;
			}
			System.out.println("count_ack_BA400_o1------- " + count_ack_BA400_o1);
			System.out.println("RJG BA400 Final String after checksum" + frameList.toString());
			/*
			 * UUID uuid = UUID.randomUUID(); //for random file name String Path =
			 * "C:\\Log"; String filename = uuid.toString().replaceAll("[\\s\\-()]", "");
			 * Path = Path+"\\"+filename+".txt"; byte[] byy =
			 * response.toString().getBytes(); try (FileOutputStream fos = new
			 * FileOutputStream(Path)) { // File written on path fos.write(byy);
			 * //fos.close(); There is no more need for this line since you had created the
			 * instance of "fos" inside the try. And this will automatically close the
			 * OutputStream } catch(Exception e) { e.printStackTrace(); }
			 */

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		// Server.frameList = frameList;

		return frameList;
	}

	// ----------------------------------------------------------------------------
	
	// ------------------------------------------------------------

	// PACKET Format id=20045
	public static List headerParsing_Mispa_Clinia_s(String line) {
		SimpleDateFormat time_formatter2 = new SimpleDateFormat("yyyyMMddHHmmss");
		String current_time_str2 = time_formatter2.format(System.currentTimeMillis());
		System.out.println("current_time_str2---" + current_time_str2);
		ServerCobas801.StxCounter = 1;

		SimpleDateFormat time_formatter3 = new SimpleDateFormat("yyyyMMddHHmmss");
		String current_time_str3 = time_formatter3.format(System.currentTimeMillis());
		System.out.println("current_time_str2 after 1 second wait---" + current_time_str3);
	
		int CountPipe = 0, j = 0;
		char cr = 13;
		char lf = 10;
		int instance = 0;
		char stx = '';
		String mode1 = "";
		StringBuffer buf = new StringBuffer();
		StringBuffer cobas = new StringBuffer();
		StringBuffer host = new StringBuffer();
		StringBuffer mode = new StringBuffer();
		StringBuffer lines = new StringBuffer();


		SimpleDateFormat time_formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String current_time_str = time_formatter.format(System.currentTimeMillis());

		StringBuffer header1 = new StringBuffer(
				"H|@^\\|GXM-88424724426|| LIS|||||CENTRAL RAILWAY HOSPITAL, SECUNDERABAD - 110000888^GeneXpert^6.4||P|1394-97|");

		StringBuffer header = new StringBuffer("");
//		header.append(Query_Message_Id);
//		header.append("||");
////		header.append("^^");
//	    header.append(Query_Message_receiver);
//		header.append("|||||");
////		header.append(Query_Message_sender);
//		header.append("||SA|1394-97|");
		
		header.append("H|\\^&|");
		header.append(headerVal);
		header.append("||Host|||||BA400||P|LIS2A|");
//		header = header.append(Query_Message_date_time);
		header = header.append(current_time_str);
		StringBuffer line3 = new StringBuffer("C|1|L|^^^^|G");
		StringBuffer line4 = new StringBuffer("L|1|N");
		StringBuffer line6 = new StringBuffer("|||F");
		StringBuffer line5 = new StringBuffer();
		char etb = 23;
		char eot = '';
		char etx = 3;
		counterPID_Packet = counterPID_Packet + 33;
	
		
		StringBuffer p = new StringBuffer("P|");
		p.append(p_packet_count);
		p.append("||");
		p.append(ServerCobas801.Sample_code.get(0).toString().replace("^", ""));
		p.append("||^");
		//p.append(name_pat.trim());
		// p.append("^name^M");
		p.append("");
		p.append("^||||||||||||||||||||");
		//p.append("^|||||||||||||||");
		//p.append("||||||||||||||");
		
		StringBuffer c = new StringBuffer("C|1|I|Patient is complaining of shortness of breath and chest pain.|G");


		StringBuffer Order = new StringBuffer();
		StringBuffer Order1 = new StringBuffer("O|2");
		List list = new ArrayList();
		StringBuffer frame = new StringBuffer();
		StringBuffer framevip = new StringBuffer();
		StringBuffer response = new StringBuffer();
		StringBuffer time = new StringBuffer();

		try {
		
			ServerCobas801.cobas = cobas;
			 ServerCobas801.host = host;
			System.out.println("TIME:- " + time);
		
			ServerCobas801.stx = stx;
			ServerCobas801.mode = mode;
	

			while (true) {

				buf.append(header);
				// buf.append(current_time_str); //Uncommnet it later
				// buf.append("|");
				buf.append(cr);
				// Ascii of CR
				// Ascii of LF
				p.append(cr);
				buf.append(p);
				c.append(cr);
			
				// ==============================

				//System.out.println("BA400_client.testCode.size()------- " + Server.testCode.size());
				//O|1|SPM01||^^^Test1|R|20130129101530|20130129092030
				LocalDateTime now = LocalDateTime.now();
		        
			    
		         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		         String formattedTimestamp = now.format(formatter);
		         
				for (int k = 0; k < ServerCobas801.testCode.size(); k++)
				{
					//if(!Server.testCode.get(k).equals("NA")) 
					//{
					Order.append("O|");
					// Order.append();
					Order.append(k + 1);
					Order.append("|");
					//Order.append("|1^1^1|");
					Order.append(ServerCobas801.Sample_code.get(0).toString().replace("^", ""));
					Order.append("|");
				
					Order.append("|^^^");
					Order.append(ServerCobas801.testCode.get(k));  
				//	Order.append("CREATININE");
					
					//Order.append("|R");
//					Order.append("^2^1");				
//					if (k != Server.testCode.size() - 1) {
//
//						Order.append("\\");
//						Order.append("\n");
//						
//						// Order.append("^^^");
//
//					}
//					/*
//					 * else { Order.append("^");
//					 * 
//					 * }
				//	 */
					
					Order.append("|R|");
					Order.append(formattedTimestamp+"|"+formattedTimestamp);
					Order.append("||||A||||SER||||||||||O");
					Order.append(cr);
					buf.append(Order);
					Order.delete(0, Order.length());
					//}
					//else {
					//	continue;
					//}
				}
				// Order.append(line6);
				
				

		// RAMJI BA400			System.out.println("RJG buf= " + buf.toString());

				// ==============================================================
			

				line4.append(cr);

				buf.append(line4);

				break;
			}


			System.out.println("STRING LENGTH BEFORE FRAMING = " + buf.length());
			//Ramji BA400			System.out.println("RJG frame BEFORE FRAMING = " + buf.toString());
			// STRING LENGTH BEFORE FRAMING = 235
			int frameCount = 0, first = 0, mid = 0, remain = 0;
			frameCount = FrameCounter(buf.length());
			// System.out.println("Char At 0 "+buf.charAt(0));
			int length = buf.length();

			for (int i = 1; i <= frameCount; i++) {
				if (length < 240)
					// if (length < 306)
					mid = mid + length;
				else if (length == 240)
					mid = mid + length;
				else if (length > 240) {
					mid = mid + 240;
				}
			//	System.out.println("First " + first + "  " + "last " + mid + " stx  " + stx);
				frame.append(stx);
			//	System.out.println("RJG Server.StxCounter " + Server.StxCounter);
				frame.append(ServerCobas801.StxCounter);
		//		System.out.println("RAG STX COUNTER:- " + Server.StxCounter);
				ServerCobas801.StxCounter++;
				frame.append(buf.substring(first, mid));

				if (i == frameCount) {
					frame.append(etx);
					//System.out.println("before checksum" + frame.toString());
					frame.append(CheckSum(frame.toString())); // FRAME WITH STX STX COUNTER AND ETX
					// frame.append(CheckSum(buf.substring(first, mid))); //FRAME WITH ONLY
					// CHARACTERS
					frame.append(cr);
					frame.append(lf);
				} else

				{
					frame.append(etb);
					//System.out.println("before checksum" + frame.toString());
					frame.append(CheckSum(frame.toString())); // FRAME WITH STX STX COUNTER AND ETB
					// frame.append(CheckSum(buf.substring(first, mid)));
					frame.append(cr);
					frame.append(lf);
				}
				// length = length - mid;
				length = length - 240;
				first = mid;

				response.append(frame);
				frameList.add(frame.toString());
				/*
				 * if(i ==1) frame1.append(frame); else frame2.append(frame);
				 */
				frame.delete(0, frame.length());

			}
			 //response.append(eot);
			// frameList.add(eot);
			ServerCobas801.StxCounter = 1;
			newS.delete(0, newS.length());
			System.out.println("RJG Final String after checksum" + frameList.size());
			for (int i = 0; i < frameList.size(); i++) {
				count_ack_BA400_o1++;
			}
			System.out.println("count_ack_BA400_o1------- " + count_ack_BA400_o1);
			System.out.println("Final String after checksum" + frameList.toString());
			/*
			 * UUID uuid = UUID.randomUUID(); //for random file name String Path =
			 * "C:\\Log"; String filename = uuid.toString().replaceAll("[\\s\\-()]", "");
			 * Path = Path+"\\"+filename+".txt"; byte[] byy =
			 * response.toString().getBytes(); try (FileOutputStream fos = new
			 * FileOutputStream(Path)) { // File written on path fos.write(byy);
			 * //fos.close(); There is no more need for this line since you had created the
			 * instance of "fos" inside the try. And this will automatically close the
			 * OutputStream } catch(Exception e) { e.printStackTrace(); }
			 */

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		// Server.frameList = frameList;

		return frameList;
	}

	// ----------------------------------------------------------------------------
	


	public static List sysmax2500RBparseBI(String line) {
		System.out.println("Sysmax Bi DIR with ENQ----2500");
		SimpleDateFormat time_formatter2 = new SimpleDateFormat("yyyyMMddHHmmss");
		String current_time_str2 = time_formatter2.format(System.currentTimeMillis());
		System.out.println("current_time---" + current_time_str2);

		ServerCobas801.StxCounter = 1;

		SimpleDateFormat time_formatter3 = new SimpleDateFormat("yyyyMMddHHmmss");
		String current_time_str3 = time_formatter3.format(System.currentTimeMillis());
//		System.out.println("current_time_str2 after 1 second wait---" + current_time_str3);
		char[] array = line.toCharArray();
		int CountPipe = 0, j = 0;
		char cr = 13;
		char lf = 10;
		int instance = 0;
		char stx = '';
		String mode1 = "";
		StringBuffer buf = new StringBuffer();
		StringBuffer cobas = new StringBuffer();
		StringBuffer host = new StringBuffer();
		StringBuffer mode = new StringBuffer();
		StringBuffer lines = new StringBuffer();
		StringBuffer frame1 = new StringBuffer();
		StringBuffer frame2 = new StringBuffer();
		StringBuffer frame3 = new StringBuffer();
		StringBuffer frame3_1 = new StringBuffer();
		StringBuffer frame4 = new StringBuffer();
		StringBuffer frame5 = new StringBuffer();

		StringBuffer buf_header = new StringBuffer();
		StringBuffer buf_patient = new StringBuffer();
		StringBuffer buf_order = new StringBuffer();
		StringBuffer buf_comment = new StringBuffer();
		StringBuffer buf_order_1 = new StringBuffer();
		StringBuffer buf_termination = new StringBuffer();

		SimpleDateFormat time_formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String current_time_str = time_formatter.format(System.currentTimeMillis());

		StringBuffer header = new StringBuffer("H|\\^&|||||||||||E1394-97|");
		//header.append("|||||||||P");	// for Alinity
		//header.append("||"); 			// for Vitros Ramji Masked
		//header.append("qnxa224"); 	// for Vitros Ramji Masked	
		//header.append("|||||");  		// for Vitros Ramji Masked
		//header.append("|||LIS2-A|");	// for Vitros Ramji Masked
	    //header = header.append(current_time_str);	// for Vitros Ramji Masked

		//StringBuffer line4 = new StringBuffer("L|1|N"); // for Vitros Ramji Masked
		StringBuffer line4 = new StringBuffer("L|1|N"); // for Alinity
	
		//StringBuffer line6 = new StringBuffer("|||O"); // for Vitros Ramji Masked
		StringBuffer line5 = new StringBuffer();
		char etb = 23;
		char eot = '';
		char etx = 3;
		counterPID_Packet = counterPID_Packet + 33;
	/*	 // for Vitros Ramji Masked
		StringBuffer p = new StringBuffer("P|");
		p.append(p_packet_count);
		p.append("|");
		p.append(Server.Sample_code.get(0).toString().replace("^", ""));
		p.append("|||");
		p.append(name_pat_last.trim());
		p.append("^");
		p.append(name_pat_first.trim());
	*/
		//Added for Alinity
		StringBuffer p = new StringBuffer("P|");
		p.append(p_packet_count);
		p.append("||||^^|||||||||||||||||||||||||||||");
		
		StringBuffer Order = new StringBuffer();
		StringBuffer Comment = new StringBuffer();
		StringBuffer Order1 = new StringBuffer("O|2");
		List list = new ArrayList();
		StringBuffer frame = new StringBuffer();
		StringBuffer framevip = new StringBuffer();
		StringBuffer response = new StringBuffer();
		StringBuffer time = new StringBuffer();

		try {

			ServerCobas801.cobas = cobas;
			ServerCobas801.host = host;
		//	System.out.println("TIME:- " + time);
		
			ServerCobas801.stx = stx;
			ServerCobas801.mode = mode;

			while (true) {

				buf_header.append(header);
				buf_header.append(cr);
				buf_patient.append(p);
				buf_patient.append(cr);
				p.delete(0, p.length());
			
				// ======================================================
				frameList.clear();
				
				frameList.add("");
				frame1.append(stx);
				frame1.append(ServerCobas801.StxCounter);
				frame1.append(buf_header);
				buf_header.delete(0, buf_header.length());
				frame1.append(etx);
				frame1.append(CheckSum(frame1.toString())); // FRAME WITH STX STX COUNTER AND ETB
				frame1.append(cr);
				frame1.append(lf);
				frameList.add(frame1.toString());
				frame1.delete(0, frame1.length());

				ServerCobas801.StxCounter = ServerCobas801.StxCounter + 1;
			
				frame2.append(stx);
				frame2.append(ServerCobas801.StxCounter);
				frame2.append(buf_patient);
				buf_patient.delete(0, buf_patient.length());
				frame2.append(etx);
				frame2.append(CheckSum(frame2.toString())); // FRAME WITH STX STX COUNTER AND ETB
				frame2.append(cr);
				frame2.append(lf);
				frameList.add(frame2.toString());
				frame2.delete(0, frame2.length());

				// ===========================Start for Vitros 5600=========================================
			/*	Order.append("O|1");
				// Order.append();
				Order.append("|");
				Order.append(Server.Sample_code.get(0).toString().replace("^", ""));
				Order.append("||^^^1.0000+");
				for (int k = 0; k < Server.testCode.size(); k++)

				{
					Order.append(Server.testCode.get(k));
					Order.append("+1.0");
					if (k != Server.testCode.size() - 1) {
						Order.append("\\");
					}
				}
				Order.append("|R|");
				Order.append("|");
				Order.append("||||N||||5|||||||");
				Order.append(line6);
				Order.append(cr);
			*/	
				//=========================END for Vitros 5600=========================================	
				// ===========================Start for Alinity=======O|1|002111522041500||^^^65|||||||A||||||||||||||O
				frame3.delete(0, frame3.length());
				int OrderCounter=0;
				int fCount  = 3;
				int loopSize = ServerCobas801.testCode.size();
			if(loopSize==0) //added this if condition for null Response 
			{
				Order.append("O|1|");
				//OrderCounter = k + 1;
			//Order.append(Integer.toString(OrderCounter));
				Order.append("^");
				Order.append("^");
				Order.append(ServerCobas801.sampleNo.toString().replace("^", "").trim());
				// Order.append("^50087^1^^S1^SC^|");
				Order.append("|");
				Order.append("|");
				Order.append("|");
				Order.append("^^^");
				Order.append("^^\\");
				Order.append("R");
				Order.append("|");
				Order.append(current_time_str);
				Order.append("|||||N|||||||||||||||||||");
				
				
				Order.append(cr);

				frame3.append(stx);
				ServerCobas801.StxCounter = ServerCobas801.StxCounter + 1;
				frame3.append(ServerCobas801.StxCounter);
				frame3.append(Order);
				frame3.append(etx);
				frame3.append(CheckSum(frame3.toString())); // FRAME WITH STX STX COUNTER AND ETX
				frame3.append(cr);
				frame3.append(lf);
				frameList.add(frame3.toString());
				frame3.delete(0, frame3.length());
				ServerCobas801.StxCounter=ServerCobas801.StxCounter+1;	
			}
			else
			{
				
				Order.append("O|1|");
				//OrderCounter = k + 1;
			//Order.append(Integer.toString(OrderCounter));
				Order.append("^");
				Order.append("^");
				Order.append(ServerCobas801.sampleNo.toString().replace("^", "").trim());
				// Order.append("^50087^1^^S1^SC^|");
				Order.append("|");
				Order.append("|");
				
				for (int k = 0; k < ServerCobas801.testCode.size(); k++)
				{

				//Order.append("O|1|"); // Need "O|1|" "O|2|" "O|3|"
				
				
				//Order.append("||^^^"); //RAMJI
				//Order.append(Server.testCode.get(k));
				//Order.append("||^^^349|||||||N||||||||||||||O"); //Hardcode
				Order.append("^^^");
				Order.append(ServerCobas801.testCode.get(k));
				if (k != ServerCobas801.testCode.size() - 1)
					Order.append("^^\\");
				else
					Order.append("^^");
				}
				
				//	Order.append(Server.testCode.get(k));
				//	if (k != Server.testCode.size() - 1) {
				//		Order.append("\\");
					
				//}
				
				Order.append("|");
				Order.append("R");
				Order.append("|");
				Order.append(current_time_str);
				Order.append("|||||N|||||||||||||||||||");
				
				Order.append(cr);
		
				//=========================END for Vitros 5600=========================================	
				
				buf_order.append(Order);
				Order.delete(0, Order.length());

				buf_comment.append(Comment);
				Comment.delete(0, Comment.length());

				// --------------------------------------------
				System.out.println("Alinity Frame Count Checking :" + ServerCobas801.StxCounter);
				int frameCount = 0, first = 0, mid = 0, remain = 0;
				frameCount = FrameCounter(buf_order.length());
				int length = buf_order.length();
				
				//for (int i = 1; i <= frameCount; i++) 
				//{
					if (length < 240)
						mid = mid + length;
					else if (length == 240)
						mid = mid + length;
					else if (length > 240) {
						mid = mid + 240;
					}
					frame3.append(stx);
					ServerCobas801.StxCounter = ServerCobas801.StxCounter + 1;
					frame3.append(fCount);
			
					frame3.append(buf_order.substring(first, mid));
                     //---------------------------------
			//		if (fCount != frameCount) 
					{
						frame3.append(etx);
						frame3.append(CheckSum(frame3.toString())); // FRAME WITH STX STX COUNTER AND ETX
						frame3.append(cr);
						frame3.append(lf);
					}
			//		else
			//		{
			//			frame3.append(etb);
			//			frame3.append(CheckSum(frame3.toString())); // FRAME WITH STX STX COUNTER AND ETB
			//			frame3.append(cr);
			//			frame3.append(lf);
			//		}
						length = length - 240;
					first = mid;
                   //-----------------------------------------
					frameList.add(frame3.toString());
					
					frame3.delete(0, frame3.length());

					
					fCount++;
					
					if(fCount==8)
					{
						fCount=0;
					}
				//}
				
				/*
				for (int i = 1; i <= frameCount; i++) 
				{
					if (Server.StxCounter == 7)
					{
						Server.StxCounter = 0;
						System.out.println("Alinity Frame Count Set 0 After 7 :" + Server.StxCounter);
					}
					if (length < 240)
						mid = mid + length;
					else if (length == 240)
						mid = mid + length;
					else if (length > 240) {
						mid = mid + 240;
					}
					frame3.append(stx);
					if(Server.StxCounter == 8)
					{	Server.StxCounter = -1;
						System.out.println("Alinity Initial Frame Count is 8 :" + Server.StxCounter);
					}
					Server.StxCounter = Server.StxCounter + 1;
					frame3.append(Server.StxCounter);
			
					frame3.append(buf_order.substring(first, mid));

					if (i == frameCount) {
						frame3.append(etx);
						frame3.append(CheckSum(frame3.toString())); // FRAME WITH STX STX COUNTER AND ETX
						frame3.append(cr);
						frame3.append(lf);
					} else

					{
						frame3.append(etb);
						frame3.append(CheckSum(frame3.toString())); // FRAME WITH STX STX COUNTER AND ETB
						frame3.append(cr);
						frame3.append(lf);
					}
						length = length - 240;
					first = mid;

					frameList.add(frame3.toString());
						frame3.delete(0, frame3.length());

				}
				*/
				buf_order.delete(0, buf_order.length());
				//System.out.println("frameList= " +k);
//				if(k==loopSize-1)
//				{
//					Server.StxCounter = fCount;
//				}
				//System.out.println("frameList= " + frameList.toString());
			//}//For Loop Ramji for Multiple Order Packet frame 3
		}//else		
				// -------------------------------------
			

				line4.append(cr);

				buf.append(line4);
				buf_termination.append(line4);

				break;
			}

			// ========================================
			frame5.append(stx);
			if(ServerCobas801.StxCounter==8)
			{	
					ServerCobas801.StxCounter=0;
			}
			else
			{
			ServerCobas801.StxCounter = ServerCobas801.StxCounter ;
			}
			frame5.append(ServerCobas801.StxCounter);
			Comment.append("C|1|L|Order comment.|G");
			Comment.append(cr);
			Comment.append(etx);
			//Comment.append("78");
			frame5.append(Comment);
			frame5.append(CheckSum(frame5.toString())); // FRAME WITH STX STX COUNTER AND ETB
			frame5.append(cr);
			frame5.append(lf);
			
			//frameList.add(frame5.toString());
			frame5.delete(0, frame5.length());
			
			ServerCobas801.StxCounter = ServerCobas801.StxCounter + 1;
			if(ServerCobas801.StxCounter==8)
			{	
					ServerCobas801.StxCounter=0;
			}
			else
			{
			ServerCobas801.StxCounter = ServerCobas801.StxCounter ;
			}

			
			// frameList4.clear();
			frame4.append(stx);
			frame4.append(ServerCobas801.StxCounter);
			frame4.append(buf_termination);
			buf_termination.delete(0, buf_termination.length());
			frame4.append(etx);
			frame4.append(CheckSum(frame4.toString())); // FRAME WITH STX STX COUNTER AND ETB
			frame4.append(cr);
			frame4.append(lf);

			frameList.add(frame4.toString());
			frame4.delete(0, frame4.length());

		
			for (int i = 0; i < frameList.size(); i++) {
				count_ack_BA400_o1++;
			}
				System.out.println("Final String after checksum" + frameList.toString());
			

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return frameList;
	}

	// ----------------------------------------------------------------------------


	
	
	// ----------------------------------------------------------------------------
	// ------------------------------------------------------------

	// Header parsing and Reply Packet Generation Method//========== vitrose5600
	// PACKET Format id=20011
	public static List headerParsing_vitrose5600_railtel(String line) {
		
		System.out.println("call 5600");
		SimpleDateFormat time_formatter2 = new SimpleDateFormat("yyyyMMddHHmmss");
		String current_time_str2 = time_formatter2.format(System.currentTimeMillis());
		System.out.println("current_time---" + current_time_str2);

		ServerCobas801.StxCounter = 1;

		SimpleDateFormat time_formatter3 = new SimpleDateFormat("yyyyMMddHHmmss");
		String current_time_str3 = time_formatter3.format(System.currentTimeMillis());
//		System.out.println("current_time_str2 after 1 second wait---" + current_time_str3);
		char[] array = line.toCharArray();
		int CountPipe = 0, j = 0;
		char cr = 13;
		char lf = 10;
		int instance = 0;
		char stx = '';
		String mode1 = "";
		StringBuffer buf = new StringBuffer();
		StringBuffer cobas = new StringBuffer();
		StringBuffer host = new StringBuffer();
		StringBuffer mode = new StringBuffer();
		StringBuffer lines = new StringBuffer();
		StringBuffer frame1 = new StringBuffer();
		StringBuffer frame2 = new StringBuffer();
		StringBuffer frame3 = new StringBuffer();
		StringBuffer frame3_1 = new StringBuffer();
		StringBuffer frame4 = new StringBuffer();
		StringBuffer frame5 = new StringBuffer();

		StringBuffer buf_header = new StringBuffer();
		StringBuffer buf_patient = new StringBuffer();
		StringBuffer buf_order = new StringBuffer();
		StringBuffer buf_comment = new StringBuffer();
		StringBuffer buf_order_1 = new StringBuffer();
		StringBuffer buf_termination = new StringBuffer();

		SimpleDateFormat time_formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String current_time_str = time_formatter.format(System.currentTimeMillis());

		StringBuffer header = new StringBuffer("H|\\^&|");
		header.append("|||||||||P");	// for Alinity
		//header.append("||"); 			// for Vitros Ramji Masked
		//header.append("qnxa224"); 	// for Vitros Ramji Masked	
		//header.append("|||||");  		// for Vitros Ramji Masked
		//header.append("|||LIS2-A|");	// for Vitros Ramji Masked
	    //header = header.append(current_time_str);	// for Vitros Ramji Masked

		//StringBuffer line4 = new StringBuffer("L|1|N"); // for Vitros Ramji Masked
		StringBuffer line4 = new StringBuffer("L|1"); // for Alinity
	
		//StringBuffer line6 = new StringBuffer("|||O"); // for Vitros Ramji Masked
		StringBuffer line5 = new StringBuffer();
		char etb = 23;
		char eot = '';
		char etx = 3;
		counterPID_Packet = counterPID_Packet + 33;
	/*	 // for Vitros Ramji Masked
		StringBuffer p = new StringBuffer("P|");
		p.append(p_packet_count);
		p.append("|");
		p.append(Server.Sample_code.get(0).toString().replace("^", ""));
		p.append("|||");
		p.append(name_pat_last.trim());
		p.append("^");
		p.append(name_pat_first.trim());
	*/
		//Added for Alinity
		StringBuffer p = new StringBuffer("P|");
		p.append(p_packet_count);
		p.append("|||********|^********************^||||||||||||||||||||******");
		
		StringBuffer Order = new StringBuffer();
		StringBuffer Comment = new StringBuffer();
		StringBuffer Order1 = new StringBuffer("O|2");
		List list = new ArrayList();
		StringBuffer frame = new StringBuffer();
		StringBuffer framevip = new StringBuffer();
		StringBuffer response = new StringBuffer();
		StringBuffer time = new StringBuffer();

		try {

			ServerCobas801.cobas = cobas;
			ServerCobas801.host = host;
			System.out.println("TIME:- " + time);
		
			ServerCobas801.stx = stx;
			ServerCobas801.mode = mode;

			while (true) {

				buf_header.append(header);
				buf_header.append(cr);
				buf_patient.append(p);
				buf_patient.append(cr);
				p.delete(0, p.length());
			
				// ======================================================
				frameList.clear();
				frame1.append(stx);
				frame1.append(ServerCobas801.StxCounter);
				frame1.append(buf_header);
				buf_header.delete(0, buf_header.length());
				frame1.append(etx);
				frame1.append(CheckSum(frame1.toString())); // FRAME WITH STX STX COUNTER AND ETB
				frame1.append(cr);
				frame1.append(lf);
				frameList.add(frame1.toString());
				frame1.delete(0, frame1.length());

				ServerCobas801.StxCounter = ServerCobas801.StxCounter + 1;
			
				frame2.append(stx);
				frame2.append(ServerCobas801.StxCounter);
				frame2.append(buf_patient);
				buf_patient.delete(0, buf_patient.length());
				frame2.append(etx);
				frame2.append(CheckSum(frame2.toString())); // FRAME WITH STX STX COUNTER AND ETB
				frame2.append(cr);
				frame2.append(lf);
				frameList.add(frame2.toString());
				frame2.delete(0, frame2.length());

				// ===========================Start for Vitros 5600=========================================
			/*	Order.append("O|1");
				// Order.append();
				Order.append("|");
				Order.append(Server.Sample_code.get(0).toString().replace("^", ""));
				Order.append("||^^^1.0000+");
				for (int k = 0; k < Server.testCode.size(); k++)

				{
					Order.append(Server.testCode.get(k));
					Order.append("+1.0");
					if (k != Server.testCode.size() - 1) {
						Order.append("\\");
					}
				}
				Order.append("|R|");
				Order.append("|");
				Order.append("||||N||||5|||||||");
				Order.append(line6);
				Order.append(cr);
			*/	
				//=========================END for Vitros 5600=========================================	
				// ===========================Start for Alinity=======O|1|002111522041500||^^^65|||||||A||||||||||||||O
				frame3.delete(0, frame3.length());
				int OrderCounter=0;
				int fCount  = 3;
				int loopSize = ServerCobas801.testCode.size();
			if(loopSize==0) //added this if condition for null Response 
			{
				System.out.println("Alinity Test Code Not Found : Save Blank Order Packet" );
				Order.append("O|1|");
				Order.append(ServerCobas801.Sample_code.get(0).toString().replace("^", ""));		//+ "230426BIO0115"
				Order.append( "||^^^|||||||N||||||||||||||O");
				Order.append(cr);

				frame3.append(stx);
				ServerCobas801.StxCounter = ServerCobas801.StxCounter + 1;
				frame3.append(ServerCobas801.StxCounter);
				frame3.append(Order);
				frame3.append(etx);
				frame3.append(CheckSum(frame3.toString())); // FRAME WITH STX STX COUNTER AND ETX
				frame3.append(cr);
				frame3.append(lf);
				frameList.add(frame3.toString());
				frame3.delete(0, frame3.length());
				ServerCobas801.StxCounter=ServerCobas801.StxCounter+1;	
			}
			else
			{
				for (int k = 0; k < ServerCobas801.testCode.size(); k++)
				{

				//Order.append("O|1|"); // Need "O|1|" "O|2|" "O|3|"
				Order.append("O|");
					OrderCounter = k + 1;
				Order.append(Integer.toString(OrderCounter));
				Order.append("|");
				//Order.append(Server.Sample_code.get(0).toString().replace("^", ""));
				Order.append("||^^^"); //RAMJI
				Order.append(ServerCobas801.testCode.get(k));
				//Order.append("||^^^349|||||||N||||||||||||||O"); //Hardcode
				
				
				
				//	Order.append(Server.testCode.get(k));
				//	if (k != Server.testCode.size() - 1) {
				//		Order.append("\\");
					
				//}
				Order.append("|||||||N||||||||||||||O");
				
				Order.append(cr);
		
				//=========================END for Vitros 5600=========================================	
				
				buf_order.append(Order);
				Order.delete(0, Order.length());

				buf_comment.append(Comment);
				Comment.delete(0, Comment.length());

				// --------------------------------------------
				System.out.println("Alinity Frame Count Checking :" + ServerCobas801.StxCounter);
				int frameCount = 0, first = 0, mid = 0, remain = 0;
				frameCount = FrameCounter(buf_order.length());
				int length = buf_order.length();
				
				//for (int i = 1; i <= frameCount; i++) 
				//{
					if (length < 240)
						mid = mid + length;
					else if (length == 240)
						mid = mid + length;
					else if (length > 240) {
						mid = mid + 240;
					}
					frame3.append(stx);
					ServerCobas801.StxCounter = ServerCobas801.StxCounter + 1;
					frame3.append(fCount);
			
					frame3.append(buf_order.substring(first, mid));
                     //---------------------------------
			//		if (fCount != frameCount) 
					{
						frame3.append(etx);
						frame3.append(CheckSum(frame3.toString())); // FRAME WITH STX STX COUNTER AND ETX
						frame3.append(cr);
						frame3.append(lf);
					}
			//		else
			//		{
			//			frame3.append(etb);
			//			frame3.append(CheckSum(frame3.toString())); // FRAME WITH STX STX COUNTER AND ETB
			//			frame3.append(cr);
			//			frame3.append(lf);
			//		}
						length = length - 240;
					first = mid;
                   //-----------------------------------------
					frameList.add(frame3.toString());
					
					frame3.delete(0, frame3.length());

					
					fCount++;
					
					if(fCount==8)
					{
						fCount=0;
					}
				//}
				
				/*
				for (int i = 1; i <= frameCount; i++) 
				{
					if (Server.StxCounter == 7)
					{
						Server.StxCounter = 0;
						System.out.println("Alinity Frame Count Set 0 After 7 :" + Server.StxCounter);
					}
					if (length < 240)
						mid = mid + length;
					else if (length == 240)
						mid = mid + length;
					else if (length > 240) {
						mid = mid + 240;
					}
					frame3.append(stx);
					if(Server.StxCounter == 8)
					{	Server.StxCounter = -1;
						System.out.println("Alinity Initial Frame Count is 8 :" + Server.StxCounter);
					}
					Server.StxCounter = Server.StxCounter + 1;
					frame3.append(Server.StxCounter);
			
					frame3.append(buf_order.substring(first, mid));

					if (i == frameCount) {
						frame3.append(etx);
						frame3.append(CheckSum(frame3.toString())); // FRAME WITH STX STX COUNTER AND ETX
						frame3.append(cr);
						frame3.append(lf);
					} else

					{
						frame3.append(etb);
						frame3.append(CheckSum(frame3.toString())); // FRAME WITH STX STX COUNTER AND ETB
						frame3.append(cr);
						frame3.append(lf);
					}
						length = length - 240;
					first = mid;

					frameList.add(frame3.toString());
						frame3.delete(0, frame3.length());

				}
				*/
				buf_order.delete(0, buf_order.length());
				//System.out.println("frameList= " +k);
				if(k==loopSize-1)
				{
					ServerCobas801.StxCounter = fCount;
				}
				//System.out.println("frameList= " + frameList.toString());
			}//For Loop Ramji for Multiple Order Packet frame 3
		}//else		
				// -------------------------------------
			

				line4.append(cr);

				buf.append(line4);
				buf_termination.append(line4);

				break;
			}

			// ========================================
			frame5.append(stx);
			if(ServerCobas801.StxCounter==8)
			{	
					ServerCobas801.StxCounter=0;
			}
			else
			{
			ServerCobas801.StxCounter = ServerCobas801.StxCounter ;
			}
			frame5.append(ServerCobas801.StxCounter);
			Comment.append("C|1|L|Order comment.|G");
			Comment.append(cr);
			Comment.append(etx);
			//Comment.append("78");
			frame5.append(Comment);
			frame5.append(CheckSum(frame5.toString())); // FRAME WITH STX STX COUNTER AND ETB
			frame5.append(cr);
			frame5.append(lf);
			
			frameList.add(frame5.toString());
			frame5.delete(0, frame5.length());
			
			ServerCobas801.StxCounter = ServerCobas801.StxCounter + 1;
			if(ServerCobas801.StxCounter==8)
			{	
					ServerCobas801.StxCounter=0;
			}
			else
			{
			ServerCobas801.StxCounter = ServerCobas801.StxCounter ;
			}

			
			// frameList4.clear();
			frame4.append(stx);
			frame4.append(ServerCobas801.StxCounter);
			frame4.append(buf_termination);
			buf_termination.delete(0, buf_termination.length());
			frame4.append(etx);
			frame4.append(CheckSum(frame4.toString())); // FRAME WITH STX STX COUNTER AND ETB
			frame4.append(cr);
			frame4.append(lf);

			frameList.add(frame4.toString());
			frame4.delete(0, frame4.length());

		
			for (int i = 0; i < frameList.size(); i++) {
				count_ack_BA400_o1++;
			}
				System.out.println("Final String after checksum" + frameList.toString());
			

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return frameList;
	}

	// ----------------------------------------------------------------------------



	// frame Counts
	public static int FrameCounter(int num) {
		int count = 1;

		int remain = 0;
		while (true) {
			if (num % 240 == 0) {
				count = num / 240;
				break;
			}
			remain = num / 240;
			count = count + remain;
			break;
		}

		System.out.println("Count " + count);
		return count;
	}

	// Parsing
	public static void parse(List<String> list) {
		Map<String, List> mp = new HashMap();
		List lis = new ArrayList();
		List lis1 = new ArrayList();
		String ip = "";
		StringBuffer sampleName = new StringBuffer();
		StringBuffer TestCode;
		StringBuffer TestValue;
		StringBuffer TestValue1;
		System.out.println("size:-- " + list.size());
		String stcode;
		String sTvalue;
		ABC abc = new ABC();

		int r = 0;
		for (String line : list) {
			if (line.length() == 0 || line == null)
				break;
			TestCode = new StringBuffer();
			TestValue = new StringBuffer();
			TestValue1 = new StringBuffer();
			System.out.println("line.length() " + line.length());
			if (line.length() > 2) {
				if (Character.toUpperCase(line.charAt(0)) == 'O') {
					char[] ch = line.toCharArray();
					int count = 0;
					for (int i = 0; i < ch.length; i++) {
						// if(ch[i]==94) // Ascii value for '^'
						// break;

						if (ch[i] == '|')
							count++;

						if (count == 2) {
							if (!(ch[i] == '|'))
								sampleName.append(ch[i]);
						}

						if (count == 3) {
							break;
						}

					}

					System.out.println("SampleName===========:- " + sampleName);// Sample ID Read between '|' and '^'

				}
				if (Character.toUpperCase(line.charAt(0)) == 'R') {
					r++;
					char[] ch = line.toCharArray();
					int count = 0;
					for (int i = 0; i < ch.length; i++) {

						if (ch[i] == '^') {
							count++;
							if (count == 3)
								i++;
						}

						if (count == 4) {
							break;
						}

						if (count == 3) {
							TestCode.append(ch[i]);
						}

						if (r != 1) {
							if (count == 6) {
								// TestCode.append(ch[i]);
							}

							if (count == 7) {
								// TestCode.append(ch[i]);
							}
						}

					}
					System.out.println(
							"Testcode==================:- " + (TestCode.toString().replace("^", "")).replace("|", ""));

					lis.add(TestCode.toString());

					int count1 = 0;
					for (int i = 0; i < ch.length; i++) {

						if (ch[i] == '|') {
							count1++;
							if (count1 == 3)
								i++;
						}
						if (ch[i] == '|' && count == 4)
							break;
						if (count1 == 3) {

							TestValue.append(ch[i]);
							String str2 = TestValue.toString().replace("^", "");
							// to get a StringBuffer result:
							TestValue.delete(0, TestValue.length());
							TestValue.append(str2);

						}
					}
					System.out.println("Testvalue==============:- " + TestValue);

					lis1.add(TestValue.toString());
				}

				if (!TestCode.toString().trim().equals("") && r > 1) {
					// System.out.println("call api======");
					// System.out.println("TestValue value======"+ TestValue);
					// System.out.println("TestCode2========="+ TestCode2);
					// System.out.println("sampleName value======"+ sampleName);
					if (((TestCode.toString().replace("^", "")).trim().equals("MTB"))
							|| ((TestCode.toString().replace("^", "")).trim().equals("Rif Resistance"))) {
						System.out.println("Testcode3-- only MTB or RIf Resistance detected or not =======:- "
								+ (TestCode.toString().replace("^", "")).trim());
						//
						// abc.insert_GenExpert_MTB((TestCode3.toString().replace("^","")).trim(),TestValue.toString(),
						// sampleName.toString().replace("-"," "));
					}
					abc.insert_GenExpert((TestCode.toString().replace("^", "")).replace("|", ""), TestValue.toString(),
							sampleName.toString());

				}
			}
		}

		// MachineData msh = new MachineData();
		// msh.save(mp,sampleName.toString(),ip);

	}



	// ------------------------------------------------------------------------------------------------------------
	public static String CheckSum(String bff) {
		int sum = 0, j = 0;
		StringBuffer bf = new StringBuffer(); // IF STRING COMES WITHOUT STX THEN APPEND STX
		StringBuffer string = new StringBuffer("0");
		if (bff.charAt(0) == 2) {
		} else {
			bf.append(ServerCobas801.stx);
			bf.append(bff);
			bff = bf.toString();
		}

		char[] ch = bff.toCharArray();
		for (int i = 1; i < ch.length; i++) // CHECKSUM WITH STX STX-COUNTER AND ETB OR ETX
		{
			String hexx = Integer.toHexString(ch[i]);
			int decimal = Integer.parseInt(hexx, 16);
			sum = sum + decimal;
		}
		sum = sum % 256;
		String str = Integer.toHexString(sum);
		// System.out.println("checksum:---"+checksum);
		// checksum = checksum.substring(1, checksum.length());
		str = str.toUpperCase(); // IF ALPHABET COMES THEN IT SHOULD BE IN UPPER CASE
		if (str.length() == 1) {
			string.append(str);
		}
	 //RAMJI BA400	System.out.println("Sum " + str + "    str" + string);
		if (str.length() == 1)
			return string.toString();
		else
			return str;
	}

	// ------------------------------------------------------------------

	// -------------------------------------------------------------------------
//	public static void Vitrose_5600_odisha(List<String> list) // Vitrose_5600
//	{
//		Map<String, List> mp = new HashMap();
//		List lis = new ArrayList();
//		List lis1 = new ArrayList();
//		String ip = "";
//		StringBuffer sampleName = new StringBuffer();
//		StringBuffer TestCode = null;
//		StringBuffer testCode1 = null;
//		StringBuffer TestValue;
//		System.out.println("size:-- " + list.size());
//		String stcode;
//		String sTvalue;
//		ABC abc = new ABC();
//		Map<String, String> testCodeMap = new LinkedHashMap<String, String>();
//
////		testCodeMap.put("306", "Amyl");
////		testCodeMap.put("315", "Urea");
////		testCodeMap.put("303", "Alb");
////		testCodeMap.put("300", "Glu");
////		testCodeMap.put("300", "Glu");
////		testCodeMap.put("314", "Crea");
////		testCodeMap.put("301", "Tp");
////		testCodeMap.put("910", "Glob");
////		testCodeMap.put("305", "Chol");
////		testCodeMap.put("304", "Trig");
////
////		testCodeMap.put("356", "Dhdl");
////		testCodeMap.put("912", "Vldl");
////		testCodeMap.put("302", "Uric");
////		testCodeMap.put("319", "Tbil");
////		testCodeMap.put("327", "Bc");
////		testCodeMap.put("320", "Ast");
////		testCodeMap.put("357", "Altv");
////		testCodeMap.put("321", "Alkp");
////		testCodeMap.put("308", "K+");
////		testCodeMap.put("325", "Lipa");
////
////		testCodeMap.put("311", "Phos");
////		testCodeMap.put("337", "Che");
////		testCodeMap.put("323", "Ldh");
////		testCodeMap.put("329", "Ckmb");
////		testCodeMap.put("344", "Crp");
////		testCodeMap.put("315", "Urea");
////		testCodeMap.put("916", "Ldl");
////		testCodeMap.put("300", "Glu");
////		testCodeMap.put("318", "Ca");
////		testCodeMap.put("309", "Na+");
////
////		testCodeMap.put("317", "Bu");
////		testCodeMap.put("300", "Glu");
////		testCodeMap.put("324", "Ck");
////		testCodeMap.put("303", "Alb");
//		testCodeMap.put("306", "AMYL");
//		testCodeMap.put("315", "Urea");
//		testCodeMap.put("303", "ALB");
//		testCodeMap.put("300", "GLU");
//		testCodeMap.put("300", "GLU");
//		testCodeMap.put("314", "CREA");
//		testCodeMap.put("301", "TP");
//		testCodeMap.put("910", "GLOB");
//		testCodeMap.put("305", "CHOL");
//		testCodeMap.put("304", "TRIG");
//
//		testCodeMap.put("356", "dHDL");
//		testCodeMap.put("912", "VLDL");
//		testCodeMap.put("302", "URIC");
//		testCodeMap.put("319", "TBIL");
//		testCodeMap.put("327", "Bc");
//		testCodeMap.put("320", "AST");
//		testCodeMap.put("357", "ALTV");
//		testCodeMap.put("321", "ALKP");
//		testCodeMap.put("308", "K+");
//		testCodeMap.put("325", "LIPA");
//
//		testCodeMap.put("311", "PHOS");
//		testCodeMap.put("337", "CHE");
//		testCodeMap.put("323", "LDH");
//		testCodeMap.put("329", "CKMB");
//		testCodeMap.put("344", "CRP");
//		testCodeMap.put("315", "UREA");
//		testCodeMap.put("916", "LDL");
//		testCodeMap.put("300", "GLU");
//		testCodeMap.put("318", "Ca");
//		testCodeMap.put("309", "Na+");
//
//		testCodeMap.put("317", "Bu");
//		testCodeMap.put("300", "GLU");
//		testCodeMap.put("324", "CK");
//		testCodeMap.put("303", "ALB");
//		int r = 0;
//		for (String line : list) {
//			r++;
//			if (line.length() == 0 || line == null)
//				break;
//			TestCode = new StringBuffer();
//			testCode1 = new StringBuffer();
//			TestValue = new StringBuffer();
//
//			System.out.println("line.length() " + line.length());
//			if (line.length() > 2) {
//				if (Character.toUpperCase(line.charAt(2)) == 'O') {
//					sampleName.delete(0, sampleName.length());
//					System.out.println(
//							" Character.toUpperCase(line.charAt(2))========" + Character.toUpperCase(line.charAt(2)));
//
//					char[] ch = line.toCharArray();
//					int count = 0;
//					int count_cr = 0;
//					for (int i = 0; i < ch.length; i++) {
//
//						if (ch[i] == '|')
//							count++;
//
//						if (count == 2 && (ch[i] == '^')) {
//							count_cr++;
//
//						}
//
//						if (count == 2) {
//							if (!(ch[i] == '^'))
//								sampleName.append(ch[i]);
//						}
//						if (count_cr == 1)
//							break;
//
//					}
//
//					System.out.println("SampleID:- " + sampleName.toString().replace("^", ""));
//
//				}
//
//				int countEX = 0;
//				if (Character.toUpperCase(line.charAt(2)) == 'R') {
//					System.out.println(
//							" Character.toUpperCase(line.charAt(2))========" + Character.toUpperCase(line.charAt(2)));
//
//					char[] ch = line.toCharArray();
//					int count = 0;
//					int count_plus = 0;
//					for (int i = 0; i < ch.length; i++) {
//
//						if (ch[i] == '|')
//							count++;
//						if (count == 2 && (ch[i] == '^')) {
//							countEX++;
//						}
//
//						if (countEX == 3 && (ch[i] == '+')) {
//							count_plus++;
//							if (count_plus == 1)
//								i++;
//						}
//
//						if (countEX == 3 && count_plus == 1) {
//							if (!(ch[i] == '+'))
//
//								TestCode.append(ch[i]);
//						}
//
//						if (count == 3) {
//
//							if (!(ch[i] == '|'))
//								TestValue.append(ch[i]);
//
//						}
//
//						if (count == 4) {
//							break;
//						}
//
//					}
//
//				}
//				System.out.print("testCode =============------ " + TestCode);
//				// -- ---------------------------------------
//				if (TestCode.length() > 1) {
//					for (Map.Entry<String, String> entry : testCodeMap.entrySet()) {
//
//						if (entry.getKey().equals(TestCode.toString()))
//							testCode1.append(entry.getValue());
//
//					}
//					System.out.print("testCode1 =============------ " + testCode1);
//				}
//
//				// --------------------------------------------
//
//				String Tc = (testCode1.toString().replace("!", "")).trim();
//				System.out.println("TestCode :- " + Tc);
//				// System.out.println("TestCode :- "+TestCode);
//				System.out.println("TestValue :- " + TestValue);
//				if (!Tc.trim().equals("") && r > 1) {
//					System.out.println("r value=" + r);
//					System.out.println("TestCode" + Tc);
//					abc.insert_SysmexXN350(Tc, (TestValue.toString().replace("|", "")).trim(),    
//							sampleName.toString().replace("|", "").trim());
//					TestCode.delete(0, TestCode.length());
//					TestCode.delete(0, testCode1.length());
//					TestCode.delete(0, TestValue.length());       //replaceAll("[^0-9.]", "")
//				}
//			}
//		}
//
//	}



//---------------------------------------------------------------------



//	public static void parse_AIIMSRB(List<String> list) // //18/10/2022
//	{
//		int ResultPacket=0;
//		Map<String, List> mp = new HashMap();
//		List lis = new ArrayList();
//		List lis1 = new ArrayList();
//		String ip = "";
//		StringBuffer sampleName = new StringBuffer();
//		StringBuffer TestCode = null;
//		StringBuffer TestCode1 = null;
//		StringBuffer TestValue;
//		StringBuffer TestValue1;
//		System.out.println("size:-- " + list.size());
//		String stcode;
//		String sTvalue;
//		ABC abc = new ABC();
//
//		int r = 0;
//		for (String line : list) {
//			r++;
//			if (line.length() == 0 || line == null)
//				break;
//			TestCode = new StringBuffer();
//			TestCode1 = new StringBuffer();
//			TestValue = new StringBuffer();
//			TestValue1 = new StringBuffer();
//
//			System.out.println("line.length() " + line.length());
//			if (line.length() > 2) {
//				if(line.contains("O|")){
//			//	if (Character.toUpperCase(line.charAt(2)) == 'O') {
//					sampleName.delete(0, sampleName.length());
//					System.out.println(
//							" Character.toUpperCase(line.charAt(2))========" + Character.toUpperCase(line.charAt(2)));
//					ResultPacket=0;	
//					char[] ch = line.toCharArray();
//					int count = 0;
//					int countCR = 0;
//					for (int i = 0; i < ch.length; i++) {
//
//						if (ch[i] == '|')
//							count++;
//						if (count == 3 && (ch[i] == '^')) {
//							countCR++;
//
//							if (countCR == 2)
//								i++;
//
//						}
//
//						if (count == 2) {
//							if (!(ch[i] == '|'))
//								sampleName.append(ch[i]);
//						}
//						if (count == 3)
//							break;
//					}
//
//					System.out.println("SampleID:- " + sampleName.toString().replace("^", ""));
//
//				}
//if(line.contains("R|")){
//		//		if (Character.toUpperCase(line.charAt(2)) == 'R') {
//					System.out.println(
//							" Character.toUpperCase(line.charAt(2))========" + Character.toUpperCase(line.charAt(2)));
//					ResultPacket++;
//					char[] ch = line.toCharArray();
//					int count = 0;
//					int countAs = 0;
//					int count_cr = 0;
//					for (int i = 0; i < ch.length; i++) {
//
//						if (ch[i] == '|')
//							count++;
//
//					
//
//						if (count == 2 && (ch[i] == '^')) {
//							countAs++;
//						}
//						if (count == 3 && (ch[i] == '^')) {
//							count_cr++;
//						}
//						if (countAs == 3) {
//
//							if (!(ch[i] == '^'))
//								TestCode.append(ch[i]);
//						}
//
//						if (count == 3) {
//							//if (!(ch[i] == '^'))
//							if (!(ch[i] == '|')) // for Alinity
//								TestValue.append(ch[i]);
//						}
//
//						
//						if (count_cr == 1) 
//							break;
//
//					}
//					System.out.println("TestCode :- " + TestCode);
//					System.out.println("TestValue :- " + TestValue);
//
//				}
//				
//			
//				//System.out.println("r value=" + r);
//				if (!TestCode.toString().trim().equals("") && r > 1 && !sampleName.toString().trim().equals("")) {
//		
//
//					//System.out.println("r value=" + r);
//					if(ResultPacket==1)
//					{abc.insert_SysmexXN350(TestCode.toString().trim(), TestValue.toString().replace("|", "").trim(),
//							sampleName.toString().replace("^", "").trim());
//						System.out.println("Only First Result Value Insert if ResultPacket" +ResultPacket);
//
//					}
//				}
//			}
//		}
//
//	}

	//ayamX 3500 OLD
	
	public static void SysmexXN350A(List<String> list) // SysmexXN350
	{
		Map<String, List> mp = new HashMap();
		List lis = new ArrayList();
		List lis1 = new ArrayList();
		String ip = "";
		StringBuffer sampleName = new StringBuffer();
		StringBuffer TestCode = null;
		StringBuffer TestCode1 = null;
		StringBuffer TestValue;
		StringBuffer TestValue1;
		System.out.println("size:-- " + list.size());
		String stcode;
		String sTvalue;
		ABC abc = new ABC();

		int r = 0;
		for (String line : list) {
			r++;
			if (line.length() == 0 || line == null)
				break;
			TestCode = new StringBuffer();
			TestCode1 = new StringBuffer();
			TestValue = new StringBuffer();
			TestValue1 = new StringBuffer();

			//System.out.println("line.length() " + line.length());
			if (line.length() > 2) {
				if (Character.toUpperCase(line.charAt(2)) == 'O') {
					System.out.println(
							" Character.toUpperCase(line.charAt(2))========" + Character.toUpperCase(line.charAt(2)));

					char[] ch = line.toCharArray();
					int count = 0;
					int countCR = 0;
					for (int i = 0; i < ch.length; i++) {

						if (ch[i] == '|')
							count++;
						if (count == 3 && (ch[i] == '^')) {
							countCR++;

							if (countCR == 2)
								i++;

						}

						if (countCR == 2) {
							if (!(ch[i] == '^'))
								sampleName.append(ch[i]);
						}
						if (countCR == 3)
							break;
					}

					System.out.println("SampleID:- " + sampleName.toString().replace("^", ""));

				}

				if (Character.toUpperCase(line.charAt(2)) == 'R') {
					System.out.println(
							" Character.toUpperCase(line.charAt(2))========" + Character.toUpperCase(line.charAt(2)));

					char[] ch = line.toCharArray();
					int count = 0;
					int countAs = 0;
					for (int i = 0; i < ch.length; i++) {

						if (ch[i] == '|')
							count++;

						if (count == 2) {
							if (!(ch[i] == '|'))
								TestCode.append(ch[i]);
						}

						if (count == 2 && (ch[i] == '^')) {
							countAs++;
						}

						if (countAs == 3) {

							if (!(ch[i] == '^'))
								TestCode1.append(ch[i]);
						}

						if (count == 3) {
							if (!(ch[i] == '|'))
								TestValue.append(ch[i]);
						}

						if (count == 4) {
							if (!(ch[i] == '|'))
								TestValue1.append(ch[i]);
						}

					}

				}
				
				StringBuffer Tv = new StringBuffer();
				String Tc = (TestCode1.toString().replace("^", "")).trim();
				System.out.println("TestCode :- " + Tc);
				System.out.println("TestCode1 :- " + TestCode1);
				System.out.println("TestValue :- " + TestValue);
				System.out.println("TestValue1 :- " + TestValue1);
				if (!Tc.trim().equals("") && r > 1) {

					if (TestValue.toString().contains("-")) {

						String Tv_db = (TestValue.toString().replace("-", " "));
						TestValue = TestValue.delete(0, TestValue.length());

						TestValue.append(Tv_db);
						System.out.println("Updated TestValue :- " + TestValue);
					}

					// String.format("%.2f", input)
					// -------------------------------
					if (Tc.equals("WBC")) {

						double Tv_db = Double.parseDouble(TestValue.toString());
						TestValue = TestValue.delete(0, TestValue.length());
						Tv_db = Tv_db * 1000;
						// Tv.append(df.format(String.valueOf(Tv_db)));
						Tv.append(String.format("%.2f", Tv_db));
						System.out.println("Updated TestValue :- " + Tv);
						TestValue.append((Tv.toString()).substring(0, Tv.length() - 3));

						System.out.println("Updated TestValue :- " + TestValue);
					}
					if (Tc.equals("PLT")) {
						double Tv_db = Double.parseDouble(TestValue.toString());
						TestValue = TestValue.delete(0, TestValue.length());
						Tv_db = Tv_db / 100;
						// Tv.append(df.format(String.valueOf(Tv_db)));
						Tv.append(String.format("%.2f", Tv_db));
						TestValue.append(Tv.toString());
						;
						System.out.println("Updated TestValue :- " + TestValue);

					}

					System.out.println("r value=" + r);
					System.out.println("TestCode" + Tc);
					abc.insert_SysmexXN350A(Tc, TestValue.toString().replace("^", "").trim(),
							sampleName.toString().replace("^", "").trim());
				}
			}
		}

	}
	
	
	// -------------------------------------------------------------------------
	// Parsing
	public static void SysmexXN350(List<String> list) // SysmexXN350
	{
		Map<String, List> mp = new HashMap();
		List lis = new ArrayList();
		List lis1 = new ArrayList();
		String ip = "";
		StringBuffer sampleName = new StringBuffer();
		StringBuffer TestCode = null;
		StringBuffer TestCode1 = null;
		StringBuffer TestValue;
		StringBuffer TestValue1;
		System.out.println("size:-- " + list.size());
		String stcode;
		String sTvalue;
		ABC abc = new ABC();

		int r = 0;
		for (String line : list) {
			r++;
			if (line.length() == 0 || line == null)
				break;
			TestCode = new StringBuffer();
			TestCode1 = new StringBuffer();
			TestValue = new StringBuffer();
			TestValue1 = new StringBuffer();

			//System.out.println("line.length() " + line.length());
			if (line.length() > 2) {
				if (Character.toUpperCase(line.charAt(2)) == 'O') {
					System.out.println(
							" Character.toUpperCase(line.charAt(2))========" + Character.toUpperCase(line.charAt(2)));

					char[] ch = line.toCharArray();
					int count = 0;
					int countCR = 0;
					for (int i = 0; i < ch.length; i++) {

						if (ch[i] == '|')
							count++;
						if (count == 3 && (ch[i] == '^')) {
							countCR++;

							if (countCR == 2)
								i++;

						}

						if (countCR == 2) {
							if (!(ch[i] == '^'))
								sampleName.append(ch[i]);
						}
						if (countCR == 3)
							break;
					}

					System.out.println("SampleID:- " + sampleName.toString().replace("^", ""));

				}

				if (Character.toUpperCase(line.charAt(2)) == 'R') {
					System.out.println(
							" Character.toUpperCase(line.charAt(2))========" + Character.toUpperCase(line.charAt(2)));

					char[] ch = line.toCharArray();
					int count = 0;
					int countAs = 0;
					for (int i = 0; i < ch.length; i++) {

						if (ch[i] == '|')
							count++;

						if (count == 2) {
							if (!(ch[i] == '|'))
								TestCode.append(ch[i]);
						}

						if (count == 2 && (ch[i] == '^')) {
							countAs++;
						}

						if (countAs == 3) {

							if (!(ch[i] == '^'))
								TestCode1.append(ch[i]);
						}

						if (count == 3) {
							if (!(ch[i] == '|'))
								TestValue.append(ch[i]);
						}

						if (count == 4) {
							if (!(ch[i] == '|'))
								TestValue1.append(ch[i]);
						}

					}

				}
				
				StringBuffer Tv = new StringBuffer();
				String Tc = (TestCode1.toString().replace("^", "")).trim();
				System.out.println("TestCode :- " + Tc);
				System.out.println("TestCode1 :- " + TestCode1);
				System.out.println("TestValue :- " + TestValue);
				System.out.println("TestValue1 :- " + TestValue1);
				if (!Tc.trim().equals("") && r > 1) {

					if (TestValue.toString().contains("-")) {

						String Tv_db = (TestValue.toString().replace("-", " "));
						TestValue = TestValue.delete(0, TestValue.length());

						TestValue.append(Tv_db);
						System.out.println("Updated TestValue :- " + TestValue);
					}

					// String.format("%.2f", input)
					// -------------------------------
					if (Tc.equals("WBC")) {

						double Tv_db = Double.parseDouble(TestValue.toString());
						TestValue = TestValue.delete(0, TestValue.length());
						Tv_db = Tv_db * 1000;
						// Tv.append(df.format(String.valueOf(Tv_db)));
						Tv.append(String.format("%.2f", Tv_db));
						System.out.println("Updated TestValue :- " + Tv);
						TestValue.append((Tv.toString()).substring(0, Tv.length() - 3));

						System.out.println("Updated TestValue :- " + TestValue);
					}
					if (Tc.equals("PLT")) {
						double Tv_db = Double.parseDouble(TestValue.toString());
						TestValue = TestValue.delete(0, TestValue.length());
						Tv_db = Tv_db / 100;
						// Tv.append(df.format(String.valueOf(Tv_db)));
						Tv.append(String.format("%.2f", Tv_db));
						TestValue.append(Tv.toString());
						;
						System.out.println("Updated TestValue :- " + TestValue);

					}

					System.out.println("r value=" + r);
					System.out.println("TestCode" + Tc);
					abc.insert_SysmexXN350(mp,
							sampleName.toString().replace("^", "").trim());
				}
			}
		}

	}

		//sysmax CA-2500
	
	
	public static void SysmexXN2500(List<String> list) // SysmexXN350
	{  System.out.println("inside 20017");
		Map<String, List> mp = new HashMap();
		List lis = new ArrayList();
		List lis1 = new ArrayList();
		String ip = "";
		StringBuffer sampleName = new StringBuffer();
		StringBuffer TestCode = null;
		StringBuffer TestCode1 = null;
		StringBuffer TestValue;
		StringBuffer TestValue1;
		System.out.println("size:-- " + list.size());
		String stcode;
		String sTvalue;
		ABC abc = new ABC();

		int r = 0;
		for (String line : list) {
			r++;
			if (line.length() == 0 || line == null)
				break;
			TestCode = new StringBuffer();
			TestCode1 = new StringBuffer();
			TestValue = new StringBuffer();
			TestValue1 = new StringBuffer();

			//System.out.println("line.length() " + line.length());
			if (line.length() > 2) {
				if (Character.toUpperCase(line.charAt(2)) == 'O') {
					System.out.println(
							" Character.toUpperCase(line.charAt(2))========" + Character.toUpperCase(line.charAt(2)));

					char[] ch = line.toCharArray();
					int count = 0;
					int countCR = 0;
					for (int i = 0; i < ch.length; i++) {

						if (ch[i] == '|')
							count++;
						if (count == 3 && (ch[i] == '^')) {
							countCR++;

							if (countCR == 2)
								i++;

						}

						if (countCR == 2) {
							if (!(ch[i] == '^'))
								sampleName.append(ch[i]);
						}
						if (countCR == 3)
							break;
					}

					System.out.println("SampleID:- " + sampleName.toString().replace("^", ""));

				}

				if (Character.toUpperCase(line.charAt(2)) == 'R') {
					System.out.println(
							" Character.toUpperCase(line.charAt(2))========" + Character.toUpperCase(line.charAt(2)));

					char[] ch = line.toCharArray();
					int count = 0;
					int countAs = 0;
					for (int i = 0; i < ch.length; i++) {

						if (ch[i] == '|')
							count++;

						if (count == 2) {
							if (!(ch[i] == '|'))
								TestCode.append(ch[i]);
						}

						if (count == 2 && (ch[i] == '^')) {
							countAs++;
						}

						if (countAs == 3) {

							if (!(ch[i] == '^'))
								TestCode1.append(ch[i]);
						}

						if (count == 3) {
							if (!(ch[i] == '|'))
								TestValue.append(ch[i]);
						}

						if (count == 4) {
							if (!(ch[i] == '|'))
								TestValue1.append(ch[i]);
						}

					}

				}
				
				StringBuffer Tv = new StringBuffer();
				String Tc = (TestCode1.toString().replace("^", "")).trim();
				System.out.println("TestCode :- " + Tc);
				System.out.println("TestCode1 :- " + TestCode1);
				System.out.println("TestValue :- " + TestValue);
				System.out.println("TestValue1 :- " + TestValue1);
				if (!Tc.trim().equals("") && r > 1) {

					if (TestValue.toString().contains("-")) {

						String Tv_db = (TestValue.toString().replace("-", " "));
						TestValue = TestValue.delete(0, TestValue.length());

						TestValue.append(Tv_db);
						System.out.println("Updated TestValue :- " + TestValue);
					}

					// String.format("%.2f", input)
					// -------------------------------
					if (Tc.equals("WBC")) {

						double Tv_db = Double.parseDouble(TestValue.toString());
						TestValue = TestValue.delete(0, TestValue.length());
						Tv_db = Tv_db * 1000;
						// Tv.append(df.format(String.valueOf(Tv_db)));
						Tv.append(String.format("%.2f", Tv_db));
						System.out.println("Updated TestValue :- " + Tv);
						TestValue.append((Tv.toString()).substring(0, Tv.length() - 3));

						System.out.println("Updated TestValue :- " + TestValue);
					}
					if (Tc.equals("PLT")) {
						double Tv_db = Double.parseDouble(TestValue.toString());
						TestValue = TestValue.delete(0, TestValue.length());
						Tv_db = Tv_db / 100;
						// Tv.append(df.format(String.valueOf(Tv_db)));
						Tv.append(String.format("%.2f", Tv_db));
						TestValue.append(Tv.toString());
						;
						System.out.println("Updated TestValue :- " + TestValue);

					}

					System.out.println("r value=" + r);
					System.out.println("TestCode" + Tc);
					abc.insert_SysmexXN350A(Tc, TestValue.toString().replace("^", "").trim(),
							sampleName.toString().replace("^", "").trim());
				}
			}
		}

	}
	
	
	public static void SysmexXN2500get(List<String> list) // SysmexXN350
	{
		
		
	}
	
	//end
	// -------------------------------------------------------
	// simens_atalika
//	public static void parse_simens_atalika(List<String> list) {
//		Map<String, List> mp = new HashMap();
//		List lis = new ArrayList();
//		List lis1 = new ArrayList();
//		String ip = "";
//		StringBuffer sampleName = new StringBuffer();
//		StringBuffer TestCode;
//		StringBuffer TestValue;
//
//		System.out.println("size:-- " + list.size());
//		String stcode;
//		String sTvalue;
//		ABC abc = new ABC();
//
//		int r = 0;
//		for (String line : list) {
//			r++;
//			if (line.length() == 0 || line == null)
//				break;
//			TestCode = new StringBuffer();
//			TestValue = new StringBuffer();
//
//			//System.out.println("line.length() " + line.length());
//			if (line.length() > 2) {
//				if (Character.toUpperCase(line.charAt(0)) == 'O') {
//					sampleName.delete(0, sampleName.length());
//					char[] ch = line.toCharArray();
//					int count = 0;
//					int count_tn = 0;
//					for (int i = 0; i < ch.length; i++) {
//
//						// if(ch[i]==94) // Ascii value for '^'
//						// break;
//
//						if (ch[i] == '|')
//							count++;
//
//						if (count == 2) {
//							if (!(ch[i] == '|'))
//								sampleName.append(ch[i]);
//						}
//
//						if (count == 3) {
//							break;
//						}
//					}
//
//					System.out.println("SampleName===========:- " + sampleName);// Sample ID Read between '|' and '^'
//
//				}
//
//				// if(Test_Name1.equals("MTBRIF")) { parse(list); } else { continue; }
//
//				if (Character.toUpperCase(line.charAt(0)) == 'R') {
//					r++;
//					int ct = 0;
//					char[] ch = line.toCharArray();
//					int count_pipe = 0;
//					int count_carr = 0;
//					for (int i = 0; i < ch.length; i++) {
//
//						if (ch[i] == '|')
//							count_pipe++;
//
//						if (count_pipe == 2 && ch[i] == '^') {
//							count_carr++;
//							if (count_carr == 3)
//								i++;
//
//						}
//
//						if (count_carr == 3 && !(ch[i] == '^')) {
//
//							TestCode.append(ch[i]);
//						}
//
//						if (count_carr == 4)
//							break;
//
//					}
//
//					System.out.println("Testcode==================:- " + TestCode.toString());
//
//					// ----------------------------------------------
//
//					int count1 = 0;
//					for (int i = 0; i < ch.length; i++) {
//
//						if (ch[i] == '|') {
//							count1++;
//							if (count1 == 3)
//								i++;
//						}
//						if (count1 == 4)
//							break;
//
//						if (count1 == 3)
//							TestValue.append(ch[i]);
//
//					}
//					System.out.println("Testvalue==============:- " + TestValue);
//
//				}
//
//				if (!TestCode.toString().trim().equals("") && r > 1) {
//
//					//abc.insert_SysmexXN350((TestCode.toString().replace("^", "")).trim(),
//							TestValue.toString().replaceAll("[^0-9.]", "").trim(),
//							sampleName.toString().replace("^", " ").replace("^", " ").trim());
//
//				}
//			}
//
//		}
//
//		// MachineData msh = new MachineData();
//		// msh.save(mp,sampleName.toString(),ip);
//
//	}

	// -------------------------------------------



	// ---------------------------------------------------------------------

	public static void mindray_MispaClinia(List<String> list) {   //mindray  MispaClinia
		Map<String, List> mp = new HashMap();
		List lis = new ArrayList();
		List lis1 = new ArrayList();
		String ip = "";
		String sampleName = null;//new StringBuffer();
		StringBuffer TestCode;
		StringBuffer TestValue;
		StringBuffer TestCode1;
		StringBuffer TestCode2;
		StringBuffer TestCode3;
		StringBuffer TestCode4;
		StringBuffer TestCode5;

		System.out.println("size:-- " + list.size());
		String stcode;
		String sTvalue;
		ABC abc = new ABC();

		int r = 0;
		for (String line : list) {
			r++;
			if (line.length() == 0 || line == null)
				break;
			TestCode = new StringBuffer();
			TestValue = new StringBuffer();
			TestCode1 = new StringBuffer();
			TestCode2 = new StringBuffer();
			TestCode3 = new StringBuffer();// only MTBRIF
			TestCode4 = new StringBuffer();// only SARScov2
			TestCode5 = new StringBuffer();// only SARScov2

			System.out.println("line.length() " + line.length());
			if (line.length() > 2) {
				if (Character.toUpperCase(line.charAt(2)) == 'O' || Character.toUpperCase(line.charAt(1)) == 'O' ) {
//					char[] ch = line.toCharArray();
//					int count = 0;
//					int count_tn = 0;
//					int count_or_cr= 0;
//					for (int i = 0; i < ch.length; i++) {
//
//						// if(ch[i]==94) // Ascii value for '^'
//						// break;
//
//						if (ch[i] == '|') {
//							count++;
//							if (count == 2)
					
					
//								i++;
//						}
//						if (count == 2 && ch[i] == '^')
//							count_or_cr++;
//
//						if (count == 2 ) {
//							if (!(ch[i] == '^'))
//								sampleName.append(ch[i]);
//						}
//
//						if (count_or_cr == 1) {
//							break;
//						}
//					}

					 String[] parts = line.split("\\|");
				        
				        // Check if the parts array has at least three elements
				        if (parts.length >= 3) {
				            // The value after the second pipe '|' is at index 2
				        	sampleName = parts[2];
				             //TestCode.append(valueAfterSecondPipe);
				            System.out.println("Value after the second pipe: " + sampleName.toString().replaceAll("\\^", ""));
				        }
					
					System.out.println("SampleName===========:- " + sampleName);// Sample ID Read between '|' and '^'

				}

				// if(Test_Name1.equals("MTBRIF")) { parse(list); } else { continue; }

//				if (Character.toUpperCase(line.charAt(0)) == 'R') {
//					r++;
//					int ct = 0;
//					char[] ch = line.toCharArray();
//					int count_pipe = 0;
//					int count_carr = 0;
//					for (int i = 0; i < ch.length; i++) {
//
//						if (ch[i] == '|')
//							count_pipe++;
//
//						if (count_pipe == 2 && ch[i] == '^') {
//							count_carr++;
//							if (count_carr == 1)
//								i++;
//
//						}
//
//						if (count_carr == 1 && !(ch[i] == '^')) {
//
//							TestCode.append(ch[i]);
//						}
//
//						if (count_pipe == 3) {
//							break;
//						}
//
//					}
//
//					System.out.println("Testcode for common component==================:- " + TestCode.toString());
//					// ----------------------------------------------
//
//					int count = 0;
//					int count1 = 0;
//					for (int i = 0; i < ch.length; i++) {
//
//						if (ch[i] == '|') {
//							count1++;
//							if (count1 == 3)
//								i++;
//						}
//						if (ch[i] == '|' && count == 4)
//							break;
//						if (count1 == 3 && !(ch[i] == '^')) {
//
//							TestValue.append(ch[i]);
//							String str2 = TestValue.toString().replace("^", "");
//							// to get a StringBuffer result:
//							TestValue.delete(0, TestValue.length());
//							TestValue.append(str2);
//
//						}
//					}
//					System.out.println("Testvalue==============:- " + TestValue);
//
//					// lis1.add(TestValue.toString());
//				}
				
				
				
				String valueAfterSecondPipe=null;
				String valueAfterThirdPipe=null;
				
				if (Character.toUpperCase(line.charAt(2)) == 'R') {
					
					
					System.out.println("ROHIT ");
					
					
					   String[] parts = line.split("\\|");
				        String sequence=parts[1];
				        String dd=parts[3];
				        String[] typee=dd.split("\\^");
				        
				        if( Integer.parseInt(sequence)==1 && typee[1].equals("RAW")) {
				        // Check if the parts array has at least three elements
				        if (parts.length >= 3) {
				            // The value after the second pipe '|' is at index 2
				             valueAfterSecondPipe = parts[2];
				            // valueAfterThirdPipe = parts[3];
					            String [] str= valueAfterSecondPipe.split("\\^");
					            valueAfterSecondPipe= str[3];
				             //valueAfterSecondPipe= valueAfterSecondPipe.replaceAll("\\^", "");
				             TestCode.append(valueAfterSecondPipe);
				            System.out.println("Value after the second pipe: " + valueAfterSecondPipe.replaceAll("\\^", ""));
				        }
					//System.out.println(parts[2]);
				        
				        if (parts.length >= 3) {
				            // The value after the second pipe '|' is at index 2
				          
				        	
				        	valueAfterThirdPipe = parts[3];
				            String [] str= valueAfterThirdPipe.split("\\^");
				            valueAfterThirdPipe= str[0];
				             //valueAfterThirdPipe=  valueAfterThirdPipe.replaceAll("\\^", "");
//				             double number = Double.parseDouble(valueAfterThirdPipe);
//				             double truncatedNumber = (int) (number * 10) / 10.0;
//				             System.out.println("truncatedNumber"+truncatedNumber);
//				             String result = String.format("%.1f", truncatedNumber);
				            if(valueAfterThirdPipe.equals(" ") || valueAfterThirdPipe.equals("-") || valueAfterThirdPipe.equals(null) ) {
				            
				            	TestValue.append("--");
					            
				            }
				            else {
				            TestValue.append(valueAfterThirdPipe);
				            }System.out.println("Value after the third pipe: " + valueAfterThirdPipe.replaceAll("\\^", ""));
				        }
				        }     
				}
				

				if (!TestCode.toString().trim().equals("") ) {

					abc.insert_SysmexXN350A(TestCode.toString().trim(), TestValue.toString().trim(),
							sampleName.toString().trim());

				}
			}

		}

		// MachineData msh = new MachineData();
		// msh.save(mp,sampleName.toString(),ip);

	}
	 public static void cobas6000_parsing(List<String> list) {
		 
		 System.out.println("ROhit 09 APR");
	      String ip = "";
	      StringBuffer sampleName = new StringBuffer();
	      new ABC();
	      int r = 0;
	      
	      String resCount = null;
	      Iterator var12 = list.iterator();

	      while(var12.hasNext()) {
	         String line = (String)var12.next();
	         ++r;
	         if (line.length() == 0 || line == null) {
	            break;
	         }

	         StringBuffer TestCode = new StringBuffer();
	         StringBuffer TestValue = new StringBuffer();
	         StringBuffer TestUnit = new StringBuffer();
	         if (line.length() > 2) {
	            int count_carr;
	            if (Character.toUpperCase(line.charAt(0)) == 'O') {
	               char[] ch = line.toCharArray();
	               int count = 0;
	               boolean count_tn = false;

	               for(count_carr = 0; count_carr < ch.length; ++count_carr) {
	                  if (ch[count_carr] == '|') {
	                     ++count;
	                  }

	                  if (count == 2 && ch[count_carr] != '|') {
	                     sampleName.append(ch[count_carr]);
	                  }

	                  if (count == 3) {
	                     break;
	                  }
	               }

	               System.out.println("SampleName===========:- " + sampleName);
	            }

	            if (Character.toUpperCase(line.charAt(0)) == 'R') {
	               ++r;
	               
	               String [] resSplit= line.split("\\|");
	               
	                resCount=resSplit[1];
	               boolean ct = false;
	               char[] ch = line.toCharArray();
	               int count_pipe = 0;
	               count_carr = 0;
	               int count_sl = 0;

	               int count1;
	               
	               for(count1 = 0; count1 < ch.length; ++count1) {
	                  if (ch[count1] == '|') {
	                     ++count_pipe;
	                  }

	                  if (count_pipe == 2 && ch[count1] == '^') {
	                     ++count_carr;
	                     if (count_carr == 3) {
	                        ++count1;
	                     }

	                     if (count_carr == 4) {
	                        ++count1;
	                     }
	                  }

	                  if (count_carr == 3 && ch[count1] == '/') {
	                     ++count_sl;
	                  }
	                  

	                  if (count_carr == 3 && ch[count1] != '/' && count_sl == 0) {
	                     TestCode.append(ch[count1]);
	                  }

	                  
	                  if (count_pipe == 3) {
	                     break;
	                  }
	                  
	               }

	              // System.out.println("Testcode :- " + TestCode.toString());

	               count1 = 0;

	               for(int i = 0; i < ch.length; i++) {
	                  if (ch[i] == '|') {
	                     count1++;
	                     if (count1 == 3) {
	                        i++;
	                     }
	                  }

	                  if (ch[i] != '|' && count1 == 3) {
	                     TestValue.append(ch[i]);
	                  }

	                  if (ch[i] != '|' && count1 == 4) {
	                     TestUnit.append(ch[i]);
	                  }
	                
	                  
	               }
	               
	               
// remove stx in 
	               
	              
	               
	               //TestValue=str;
	               
	               System.out.println("Testvalue==============:- " + TestValue);
	               System.out.println("TestUnit not count====:- " + TestUnit);
	              
	             
	            }

//	               String[] str=TestValue.toString().split("");
//	               String[] str1=TestCode.toString().split("");
//	               String tvv=str[0];
//	               String tcc=str1[0];
	              
	           
	             
	            if (!TestCode.toString().trim().equals("") && r>1 && !sampleName.toString().trim().equals("")) {
	               if (TestUnit.toString().trim().equals("count")) {
	                  System.out.println("TestUnit do not have count " + TestUnit);
	               } else {
	                  System.out.println("TestUnit have not count :- " + TestUnit);
	            	   System.out.println("TestCode :"+TestCode);
	            	   System.out.println("Testvalue :"+TestValue);
	            	   
	                  ABC.insert_Cobas801(TestCode.toString().trim(), TestValue.toString().replace("|", "").trim(), sampleName.toString().replace("^", "").trim());
	               }
	            }
	         }
	      }

	   }
	 
	 //
	 
	 
//	 1H|\^&|||||||||TSDWN|||
//	 P|1||||^|||
//	 O|1|^^04100490^0^50006^1^^S1^SC^R2|^|^^^10172^1\^^^10220^1\^^^10195^1\^^^BILI-INDIRECT^1|R||||||A||||1||||||||||O
//	 C|1|I|^^^^|G|
//	 L|1|N|
//	 8D
	 //
	 
	 
	 //AIIMS Bhubaneswar Cobas 801 Bi Directional Solution  working
	 public static List cobas6000_headerParsing_s() {
	      SimpleDateFormat time_formatter2 = new SimpleDateFormat("yyyyMMddHHmmss");
	      String current_time_str2 = time_formatter2.format(System.currentTimeMillis());
	    //  System.out.println("current_time_str2---" + current_time_str2);
	      String rack_type = "";
	      String sample_type = "";
	      SimpleDateFormat time_formatter3 = new SimpleDateFormat("yyyyMMddHHmmss");
	      String current_time_str3 = time_formatter3.format(System.currentTimeMillis());
	     // System.out.println("current_time_str2 after 1 second wait---" + current_time_str3);
	      int CountPipe = 0;
	      int j = 0;
	      char cr = '\r';
	      char lf = '\n';
	      int instance = 0;
	      char stx = 2;
	      String mode1 = "";
	      StringBuffer buf = new StringBuffer();
	      StringBuffer cobas = new StringBuffer();
	      StringBuffer host = new StringBuffer();
	      StringBuffer mode = new StringBuffer();
	      new StringBuffer();
	      SimpleDateFormat time_formatter = new SimpleDateFormat("yyyyMMddHHmmss");
	      String current_time_str = time_formatter.format(System.currentTimeMillis());
	      new StringBuffer("H|@^\\|GXM-88424724426|| LIS|||||CENTRAL RAILWAY HOSPITAL, SECUNDERABAD - 110000888^GeneXpert^6.4||P|1394-97|");
	      StringBuffer header = new StringBuffer("H|\\^&|");
	      header.append("||");
	      header.append("|||||");
	      header.append("|TSDWN|||");
	      new StringBuffer("C|1|L|^^^^|G");
	      StringBuffer line4 = new StringBuffer("L|1|N|");
	      new StringBuffer("|||O");
	      new StringBuffer();
	      char etb = '';
	      char eot = '';
	      char etx = '';
	      counterPID_Packet += 33;
	      StringBuffer p = new StringBuffer("P|");
	      p.append(p_packet_count);
	      p.append("||||^|||");
	      new StringBuffer("L|1|N|");
	      StringBuffer c = new StringBuffer("C|1|I|^^^^|G|");
	      StringBuffer Order = new StringBuffer();
	      new StringBuffer("O|2");
	      new ArrayList();
	      StringBuffer frame = new StringBuffer();
	      new StringBuffer();
	      StringBuffer response = new StringBuffer();
	      StringBuffer time = new StringBuffer();

	      try {
	     //    System.out.println("TIME:- " + time);
	         stx = stx;
	         buf.append(header);
	         buf.append(cr);
	         p.append(cr);
	         buf.append(p);
	      //   System.out.println("testCode   size------- " + testCode.size());
	         Order.append("O|1");
	         Order.append("|");
	         String[] smno=Sample_code.get(0).split("\\^");
	         Order.append(smno[2]+"|");
	         String R2check=smno[9];
	         Sample_code.get(0).toString().replaceAll(R2check,"not");
	         String strr=Sample_code.get(0);
	         strr= strr.replaceAll(R2check, "not");
	         strr=strr.replaceAll("\\^"+smno[2], "");
	        // Order.append(((String)Sample_code.get(0)).toString().replaceAll("\\^"+smno[2],""));
	         Order.append(strr);
	        // Order.append("|");
	       //  Order.append(((String) pa.get(0)).toString());
	         Order.append("|");

	         int frameCount;
	         for(frameCount = 0; frameCount < testCode.size(); ++frameCount) {
	            Order.append("^^^");
	            Order.append(testCode.get(frameCount));
	            if (frameCount != testCode.size() - 1) {
	               Order.append("^1\\");
	            } else {
	               Order.append("^1");
	            }
	         }

	         Order.append("|R||||||A||||1||||||||||O");
	         Order.append(cr);
	         buf.append(Order);
	         Order.delete(0, Order.length());
	         System.out.println("buf= " + buf.toString());
	         c.append(cr);
	         buf.append(c);
	         line4.append(cr);
	         buf.append(line4);
	        // System.out.println("STRING LENGTH BEFORE FRAMING = " + buf.length());
	        // System.out.println("frame BEFORE FRAMING = " + buf.toString());
	    //     int frameCount = 0;
	         int first = 0;
	         int mid = 0;
	         boolean remain = false;
	         frameCount = FrameCounter(buf.length());
	         int length = buf.length();

	         int i;
	         for(i = 1; i <= frameCount; ++i) {
	            if (length < 240) {
	               mid += length;
	            } else if (length == 240) {
	               mid += length;
	            } else if (length > 240) {
	               mid += 240;
	            }

	          //  System.out.println("First " + first + "  " + "last " + mid + " stx  " + stx);
	            frame.append(stx);
	         //   System.out.println("Server.StxCounter " + StxCounter);
	            frame.append(StxCounter);
	        //    System.out.println("STX COUNTER:- " + StxCounter);
	            ++StxCounter;
	            frame.append(buf.substring(first, mid));
	            if (i == frameCount) {
	               frame.append(etx);
	          //     System.out.println("before checksum" + frame.toString());
	               frame.append(CheckSum(frame.toString()));
	               frame.append(cr);
	               frame.append(lf);
	            } else {
	               frame.append(etb);
	          //     System.out.println("before checksum" + frame.toString());
	               frame.append(CheckSum(frame.toString()));
	               frame.append(cr);
	               frame.append(lf);
	            }

	            length -= 240;
	            first = mid;
	            response.append(frame);
	            frameList.add(frame.toString());
	            frame.delete(0, frame.length());
	         }

	         StxCounter = 1;

	         for(i = 0; i < frameList.size(); ++i) {
	            ++count_ack_BA400_o1;
	         }

	        // System.out.println("count_ack_BA400_o1------- " + count_ack_BA400_o1);
	      //   System.out.println("Final String after checksum" + frameList.toString());
	      } catch (Exception var45) {
	         System.out.println(var45.getMessage());
	         var45.printStackTrace();
	      }

	      return frameList;
	   }

	// ---------------------------------------------------------------------------------


	// ---------------------------------------------------------------------

	// ========================================================================================================================
}
