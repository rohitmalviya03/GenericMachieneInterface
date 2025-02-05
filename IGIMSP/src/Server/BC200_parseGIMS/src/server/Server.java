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
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
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

	static Map res = ReadPropertyFile.getPropertyValues();
	static String formatid = (String) res.get("formatid");
	static String server_port = (String) res.get("server_port");
	static Path path1;
	static String currentDirectory;
	static String path_HIMS_LOG = "";
	static String path_MachineData = "";

	static int server_port1 = Integer.parseInt(server_port);
	static String[] argument;

	static List frameList1 = new ArrayList<>();
	static List frameList2 = new ArrayList<>();
	static List frameList3 = new ArrayList<>();
	static List frameList3_1 = new ArrayList<>();
	static List frameList4 = new ArrayList<>();
	static List main_frameList = new ArrayList<>();

	public static void main(String[] args) throws IOException {
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
		if (formatid.equals("20038")) { // BA400_client
			BA400_client.main(argument);
		}
		
		Server serverobj = new Server(server_port1);
		serverobj.startServer();
	}

	Server(int port) {
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

		Server server = null;
		Socket client = null;
		BufferedReader cin;
		PrintStream cout;
		Scanner sc = new Scanner(System.in);
		int id;
		char s;
		String si;

		ServerThread(Socket client, int count, Server server) throws IOException {

			this.client = client;
			this.server = server;
			this.id = count;
			System.out.println("CONNECTION " + id + " ESTABLISHED WITH CLIENT " + client);

			System.out.println("test-----------");
		

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
				String fileName = "C:\\TcpFiles\\property\\data_Out1.txt";
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
						String fileName = "C:\\TcpFiles\\property\\data_Out1.txt";

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
									splitFunction(redDataText); // SEND FOR SAVE
									order_packet_buffer.delete(0, order_packet_buffer.length());// DELETE STRINGBUFFER
									/*
									 * cout.print(ack); System.out.println("Server1: " + ack);
									 */

									order_packet_buffer_counter = 0;
								}

								// ------------------
								else if (formatid.equals("20005")) {
									if (character_array[0] == ack || character_array[0] == eot) {
										if (query_Packet_List.size() != 0) // In Case of ACK receive check if query is
																			// not null then parse Query
										{
											StringBuilder out1 = new StringBuilder();
											for (Object o : query_Packet_List) {
												out1.append(o.toString());
												out1.append("\t");
											}

											System.out.println(" after eot ===packet================" + out1);

											splitFunction(out1.toString()); // Sends Query 1 bY 1

											query_Packet_List.clear(); // Clears query list cause we received ACK i.e
																		// reply with list
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
								if (formatid.equals("20011") || formatid.equals("20043") || formatid.equals("20045")) { // single packet vitrose
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
											if (count_ack_BA400_o1 != 0) {
												cout.print(frameList.get(p)); // after every query's frames are sended
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
//													frameList.remove(0);
													Server.testCode.clear();
													Server.testCode_mapList.clear();
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
									if (Character.toUpperCase(redDataText.charAt(2)) == 'H') {
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

				if ((str.length())>2 && Character.toUpperCase(str.charAt(0)) == 'O' || Character.toUpperCase(str.charAt(2)) == 'O'
						|| Character.toUpperCase(str.charAt(1)) == 'O' || Character.toUpperCase(str.charAt(1)) == 'R'
						|| Character.toUpperCase(str.charAt(2)) == 'R' || Character.toUpperCase(str.charAt(2)) == 'P'
						|| Character.toUpperCase(str.charAt(2)) == 'P') {
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
			if (list.size() == 1) {
				buf.append("NO Packet Available");
				return 0;
			}
			String Q = list.get(0);
			String Q1 = list.get(0);
			if (Q.length() > 2) {
				if (Character.toUpperCase(Q.charAt(0)) == 'Q' || Q.contains("Q|1|")) {
					sendAck = 1;
				}
			}
			if (Q1.length() > 2) {
				if (Character.toUpperCase(Q1.charAt(2)) == 'Q' || Q1.contains("Q|1|")) {
					sendAck = 1;
				}
			} else
				sendAck = 0;
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
			if (list.size() == 1) {
				buf.append("NO Packet Available");
				return frameList;
			}
			
			System.out.println("<####>"+list.size()+"list------------------"+list);
			String Q = list.get(2);
			String Q1 = list.get(1);
			System.out.println("<####>");
			System.out.println("parsing start");
			System.out.println(" The Value LIST : " + list);
			System.out.println(" The Value of text : " + text);
			System.out.println(" The_Value_of Q" + Q);
			if (Character.toUpperCase(Q.charAt(0)) == 'Q' || Q.contains("Q|1|") || Q1.contains("Q|1|") ||Character.toUpperCase(Q1.charAt(0)) == 'Q') {

				if (formatid.equals("20003"))// genexpert
				{
					frameList = parseSelect(list);
				}

			
				if (formatid.equals("20045"))// mispa clinia
				{
					frameList = common_parseSelect(list);
				}
				
				if (formatid.equals("20001111"))// genexpert same
				{
					frameList = parseSelect(list);
				}
				if (formatid.equals("211"))// genexpert
				{
					frameList = parseSelect(list);
				}
			

			} else {
				// parse(list); genexpert format ID 20001
				// parse_sysmex800i(list); format ID 20002

				if (formatid.equals("200031")) {
					parse(list);
				}

			
				if (formatid.equals("20016")) { // SysmexXN350
					SysmexXN350(list);
				}
					
				
				if (formatid.equals("20045")) { // Mindray mispa clinia
					mindray_MispaClinia(list);
				}

			
				if (formatid.equals("20035")) { // Vitros 560
					Vitros_client.main(argument);
				}
			
				if (formatid.equals("20044")) { // simens_atalika
					parse_simens_atalika(list);
				}
				
				if (formatid.equals("20050")) { // parse_AIIMSRB 18/10/2022
					parse_AIIMSRB(list);
				}
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
		String line = list.get(1);

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
		// get test code
		String it = ABC.getSampleDtl(bf.toString().replace("^", ""));
		Server.Test_Code = bf1.append(it);
//	  System.out.println("lt========= "+it);
		// String line2=(((it.replace("{",
		// "")).replace(":","=")).replace("\"","")).trim();
		// System.out.print(" new line2---------------- "+line2);
		String[] kv = it.split("#");
		// String key = kv[0];
		// String[] kv1 = key.split(";");
		// String key1 = kv1[0];
		// String value = kv[1];
		// System.out.print(" kv[0] ---------------- "+kv[0]);
		// System.out.print(" kv1[0] ---------------- "+kv1[0]);

		String k[] = kv[0].split(";");
		// System.out.print(" k[0] ---------------- "+k[0]);
		// Server.Test_Code = bf1.append(k[0]);
		System.out.print(" Test_Code ---------------- " + Test_Code);
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
		Server.testCode = testCode;

		Server.packet = packet;
		// System.out.println("Packet "+Server.packet);
		// Server.packet.append("741");
		Server.sampleNo = bf;
		Server.Query_Message_Id = bf_header;
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

				// frameList = headerParsing(list.get(0));
			}

			break;
		}
		// System.out.println("PAcket:-----"+packet);
		return frameList;
	}

	// Header parsing and Reply Packet Generation Method
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
			Server.cobas = cobas;
			Server.host = host;
			System.out.println("TIME:- " + time);
			// mode.replace(2, 5, "DWN"); //Replace TSREQ TO TSDWN
			/*
			 * if(=="TSREQ") { System.out.println("in tsreq");
			 * 
			 * }
			 */
			Server.stx = stx;
			Server.mode = mode;
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

				Order.append(Server.sampleNo.toString().replace("^", ""));
				// Order.append("^50087^1^^S1^SC^|");

				https: // hmis.rcil.gov.in/HISInvestigationG5/new_investigation/masters/MachineInterface.jsp?type=2&eqp=100007&hos=33201&sam=2102OP0220&uid=4323

				Order.append("||^^^SARSCOV2|R|");

				Order.append(current_time_str);
				Order.append("");
				if (Server.testCode != null) {
					System.out.println("TEST-CODE LIST IS NOT NULL");
					for (int k = 0; k < Server.testCode.size(); k++)

					{
						Order.append("^^^");
						Order.append(Server.testCode.get(k));
						if (k != Server.testCode.size() - 1)
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
				frame.append(Server.StxCounter);
				System.out.println("STX COUNTER:- " + Server.StxCounter);
				Server.StxCounter++;
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
			Server.StxCounter = 1;
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
			Server.cobas = cobas;
			Server.host = host;
			System.out.println("TIME:- " + time);
			// mode.replace(2, 5, "DWN"); //Replace TSREQ TO TSDWN
			/*
			 * if(=="TSREQ") { System.out.println("in tsreq");
			 * 
			 * }
			 */
			Server.stx = stx;
			Server.mode = mode;
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

				Order.append(Server.sampleNo.toString().replace("^", ""));
				// Order.append("^50087^1^^S1^SC^|");
				// Order.append("||^^^SARSCOV2|R|");

				Order.append("||^^^");
				Order.append(Test_Code);
				Order.append("|R|");
				Order.append(current_time_str);
				Order.append("");
				// ---------SECOND ORDER
				Order1.append("|");
				Order1.append(Server.sampleNo.toString().replace("^", ""));
				// Order.append("^50087^1^^S1^SC^|");
				Order1.append("||^^^MTBRIF|R|");
				Order1.append(current_time_str);
				Order1.append("");
				// -----------------------------
				if (Server.testCode != null) {
					System.out.println("TEST-CODE LIST IS NOT NULL");
					for (int k = 0; k < Server.testCode.size(); k++)

					{
						Order.append("^^^");
						Order.append(Server.testCode.get(k));
						Order1.append("^^^");
						Order1.append(Server.testCode.get(k));
						if (k != Server.testCode.size() - 1) {
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
				frame.append(Server.StxCounter);
				System.out.println("STX COUNTER:- " + Server.StxCounter);
				Server.StxCounter++;
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
			Server.StxCounter = 1;
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
		StringBuffer packet = new StringBuffer();
		List<StringBuffer> frameList = new ArrayList<>();
		List testCode = new ArrayList();
		String line_header = list.get(0);
		String line = list.get(2);
		String line1 = list.get(1);
		Map<String, String> testCodeMap = new LinkedHashMap<String, String>();
		int count = 0;
		StringBuffer bf = new StringBuffer();
		StringBuffer bf1 = new StringBuffer();

		int count_header = 0;
		StringBuffer bf_header = new StringBuffer();
		StringBuffer bf_header_sender = new StringBuffer();
		StringBuffer bf_header_receiver = new StringBuffer();
		StringBuffer bf_header_datetime = new StringBuffer();

		Sample_code.clear();
		testCodeMap.put("Amyl", "306");
		testCodeMap.put("Urea", "315");
		testCodeMap.put("Alb", "303");
		testCodeMap.put("Glu", "300");
		testCodeMap.put("Glu", "300");
		testCodeMap.put("Crea", "314");
		testCodeMap.put("Tp", "301");
		testCodeMap.put("Glob", "910");
		testCodeMap.put("Chol", "305");
		testCodeMap.put("Trig", "304");

		testCodeMap.put("Dhdl", "356");
		testCodeMap.put("Vldl", "912");
		testCodeMap.put("Uric", "302");
		testCodeMap.put("Tbil", "319");
		testCodeMap.put("Bc", "327");
		testCodeMap.put("Ast", "320");
		testCodeMap.put("Altv", "357");
		testCodeMap.put("Alkp", "321");
		testCodeMap.put("K+", "308");
		testCodeMap.put("Lipa", "325");

		testCodeMap.put("Phos", "311");
		testCodeMap.put("Che", "337");
		testCodeMap.put("Ldh", "323");
		testCodeMap.put("Ckmb", "329");
		testCodeMap.put("Crp", "344");
		testCodeMap.put("Urea", "315");
		testCodeMap.put("Ldl", "916");
		testCodeMap.put("Glu", "300");
		testCodeMap.put("Ca", "318");
		testCodeMap.put("Na+", "309");

		testCodeMap.put("Bu", "317");
		testCodeMap.put("Glu", "300");
		testCodeMap.put("Ck", "324");
		testCodeMap.put("Alb", "303");

//		testCodeMap.put("AMYL", "306");
//		testCodeMap.put("Urea", "315");
//		testCodeMap.put("ALB", "303");
//		testCodeMap.put("GLU", "300");
//		testCodeMap.put("GLU", "300");
//		testCodeMap.put("CREA", "314");
//		testCodeMap.put("Tp", "301");
//		testCodeMap.put("GLOB", "910");
//		testCodeMap.put("CHOL", "305");
//		testCodeMap.put("Trig", "304");
//
//		testCodeMap.put("dHDL", "356");
//		testCodeMap.put("Vldl", "912");
//		testCodeMap.put("Uric", "302");
//		testCodeMap.put("Tbil", "319");
//		testCodeMap.put("Bc", "327");
//		testCodeMap.put("AST", "320");
//		testCodeMap.put("ALTV", "357");
//		testCodeMap.put("ALKP", "321");
//		testCodeMap.put("K+", "308");
//		testCodeMap.put("Lipa", "325");
//
//		testCodeMap.put("Phos", "311");
//		testCodeMap.put("CHE", "337");
//		testCodeMap.put("Ldh", "323");
//		testCodeMap.put("CKMB", "329");
//		testCodeMap.put("CRP", "344");
//		testCodeMap.put("Urea", "315");
//		testCodeMap.put("Ldl", "916");
//		testCodeMap.put("GLU", "300");
//		testCodeMap.put("Ca", "318");
//		testCodeMap.put("Na+", "309");
//
//		testCodeMap.put("Bu", "317");
//		testCodeMap.put("GLU", "300");
//		testCodeMap.put("CK", "324");
//		testCodeMap.put("ALB", "303");
//		

		// get message id
		if (Character.toUpperCase(line_header.charAt(2)) == 'H') {
			char[] ch = line_header.toCharArray();

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

		long num = Long.parseLong(bf_header_datetime.toString());
		long last_digit = num % 10; // extract the last digit
		num /= 10; // modify the original number
		long second_to_last_digit = num % 10; // extract second last digit
		String str = String.valueOf(second_to_last_digit) + String.valueOf(last_digit); // convert and add strings

		System.out.println("str==========vvvvvvvvvvvvvvvvv=========" + str); // print desired string
		long num1 = Long.parseLong(str);
		long num2 = num1 + 2;
		long num3;
		String bc = String.valueOf(num2);
		System.out.println("bc======" + bc);
		System.out.println("num2======" + num2);
		if ((String.valueOf(num2)).length() == 1) {
			String ab = "0";
			bc = String.valueOf(0).concat(String.valueOf(num2));
			num3 = Long.parseLong(bc);
			num2 = num3;
			System.out.println("updated  bc======" + bc);

		}
		System.out.println("updatednum2======" + num2);

		String ab = bf_header_datetime.toString().substring(0, bf_header_datetime.toString().length() - 2);
		bf_header_datetime.delete(0, bf_header_datetime.length());
		bf_header_datetime.append(ab.concat(String.valueOf(bc)));
		System.out.println("ab==========vvvvvvvvv111111111111111111=========" + ab);
		System.out.println("bf_header_datetime==========vvvvvvvvv111111111111111111=========" + bf_header_datetime);

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

			System.out.println("SampleName:- " + bf);

		}
		System.out.println("SampleNo " + bf);
		
		if (!line.contains("Q|1|") && line1.contains("Q|1|")) {
			bf.delete(0, bf.length());
			char[] ch = line1.toCharArray();

			for (int i = 0; i < ch.length; i++) {

				if (ch[i] == '|')
					count++;

				if (count == 2) {
					if (!(ch[i] == '|'))
						bf.append(ch[i]);
				}

			}

			System.out.println("SampleName line1:- " + bf);

		}
		System.out.println("SampleName line1:- " + bf);
		// -------------------------------------------------
		int c = 1;
		count = 0;
		if (Character.toUpperCase(line.charAt(0)) == 'Q' || line.contains("Q|")) {
			char[] ch = line.toCharArray();
			for (int i = 0; i < ch.length; i++) {

				if (ch[i] == '|')
					count++;

				if (count == 2 && ch[i] == '\\') {
					c++;
					Sample_code.add(bf1.toString().replace("\\", "").trim());
					bf1.delete(0, bf1.length());
				}
				if (count == 3) {
					Sample_code.add(bf1.toString().replace("\\", "").trim());
					bf1.delete(0, bf1.length());
					c = 0;
					System.out.println("Sample_code " + Sample_code.toString());
					break;
				}

				if (count == 2 && c != 0) {
					if (!(ch[i] == '|')) {
						bf1.append(ch[i]);

					}
				}

			}

			System.out.println("Sample_code:- " + Sample_code);// Sample ID Read between '|' and '^'

		}
		
		
		if (!line.contains("Q|") && line1.contains("Q|")) {
			char[] ch = line1.toCharArray();
			for (int i = 0; i < ch.length; i++) {

				if (ch[i] == '|')
					count++;

				if (count == 2 && ch[i] == '\\') {
					c++;
					Sample_code.add(bf1.toString().replace("\\", "").trim());
					bf1.delete(0, bf1.length());
				}
				if (count == 3) {
					Sample_code.add(bf1.toString().replace("\\", "").trim());
					bf1.delete(0, bf1.length());
					c = 0;
					System.out.println("Sample_code " + Sample_code.toString());
					break;
				}

				if (count == 2 && c != 0) {
					if (!(ch[i] == '|')) {
						bf1.append(ch[i]);

					}
				}

			}

			System.out.println("Sample_code line1:- " + Sample_code);// Sample ID Read between '|' and '^'

		}

		for (int i = 0; i < Sample_code.size(); i++) {
			count_ack_BA400_s++;
		}
//		System.out.println("count_ack_BA400_s------- " + count_ack_BA400_s);
		// ---------------------------------------------------

		String line2 = ABC.getSampleDtl_biolis50i(Server.Sample_code.get(0).toString().replace("^", ""));
		System.out.println("line2------- " + line2);
		int r = 0;
		String tc = "";

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
		
		// -----------
		String[] kv1 = tc.split(":");
		String[] kv2 = kv1[1].split(";");
		for (String z : kv2) {
			Server.testCode.add(z);
			// System.out.print("Test_Code =============------ " + Server.testCode);
		}
		System.out.println("Test_Code =============------ " + Server.testCode);

		String testCode1 = "";
		String testValue1 = "";
	//	-------------------------------------------
//		name_pat="Employee General Test";
		System.out.println("name_pat11111 =============------ " + name_pat);

		String[] nameP = name_pat.split(" ");
		if(nameP.length>=1) {
			System.out.println("nameP.length =============------ " + nameP.length);
		if(nameP.length==1) {
			name_pat_first=nameP[0];
		
			System.out.println("name_pat_first =============------ " + name_pat_first+"========"+name_pat_last);

		}
		if(nameP.length==2) {
			name_pat_first=nameP[0];
			name_pat_last=nameP[1];		
			System.out.println("name_pat_first =============------ " + name_pat_first+"========"+name_pat_last);

		}
		if(nameP.length==3) {
			name_pat_first=nameP[0];
			name_pat_last=nameP[2];		
			System.out.println("name_pat_first =============------ " + name_pat_first+"========"+name_pat_last);

		}
	}
    //-----------------------------------------------------
		for (Map.Entry<String, String> entry : testCodeMap.entrySet()) {
			testCode1 = entry.getKey();
			testValue1 = entry.getValue();
			for (int i = 0; i < Server.testCode.size(); i++) {
				if (testCode1.equalsIgnoreCase(Server.testCode.get(i).toString()))
					testCode_mapList.add(testValue1);
			}
		}
		System.out.print("testCode_mapList =============------ " + testCode_mapList);
		// ------------------------------------
//		testCode_mapList.add(300);
//		testCode_mapList.add(301);
//		testCode_mapList.add(3001);
//		testCode_mapList.add(30032);
//		testCode_mapList.add(3005);
//		
//		testCode_mapList.add(3006);
//		testCode_mapList.add(3007);
//		testCode_mapList.add(3008);
//		
//		testCode_mapList.add(3009);
//		
//		testCode_mapList.add(3003);
//		testCode_mapList.add(3002);
//		testCode_mapList.add(3003);
//		testCode_mapList.add(3009);
//		testCode_mapList.add(3000);
//		testCode_mapList.add(3000);
//		testCode_mapList.add(3000);
//		testCode_mapList.add(300);
//		testCode_mapList.add(3004);
//		testCode_mapList.add(3000);
//		testCode_mapList.add(30097);
//		testCode_mapList.add(3006);
//		testCode_mapList.add(3007);
//		Server.testCode.add("IG");
//		Server.testCode.add("WBC");
//		Server.testCode.add("RBC");
//		Server.testCode.add("NEUT");
//		Server.testCode.add("LYMPH");
//		Server.testCode.add("EO");
//		Server.testCode.add("BASO");
//		Server.testCode.add("HGB");
//		Server.testCode.add("HCT");
		// =============================
		// get test code
		// BA400_client.testCode =
		// Arrays.asList(ABC.getSampleDtl_biolis50i(bf.toString().replace("^",
		// "").replace(";", "")));
		// -------------------------------------------------
		/*
		 * String tc = ABC.getSampleDtl_biolis50i(bf.toString().replace("^",""));
		 * String[] kv1 = tc.split(";"); for (String z : kv1) {
		 * BA400_client.testCode.add(z);
		 * System.out.print("Test_Code =============------ " + BA400_client.testCode); }
		 * System.out.print("Test_Code =============------ " + BA400_client.testCode);
		 */
		// ------------------------------------
		// Server.testCode.add("950");
//		Server.testCode.add("951");
//		Server.testCode.add("952+1.0");
//		BA400_client.testCode.add("GLUCOSE");
//		BA400_client.testCode.add("abc");
//	BA400_client.testCode.add("bcd");
		// --------------------------------------------------
//		System.out.print(" test_code_array=========1111111===========----------- " + BA400_client.testCode);

		// Server.Test_Code = bf1.append(it);
//	  System.out.println("lt========= "+it);
		// String line2=(((it.replace("{",
		// "")).replace(":","=")).replace("\"","")).trim();
		// System.out.print(" new line2---------------- "+line2);
		/*
		 * String[] kv = test_code_array.split("#");
		 * System.out.print(" kv[0]============1111111===========----------- "+kv[0]);
		 * // String key = kv[0]; // String[] kv1 = key.split(";"); // String key1 =
		 * kv1[0]; // String value = kv[1]; //
		 * System.out.print(" kv[0] ---------------- "+kv[0]); //
		 * System.out.print(" kv1[0] ---------------- "+kv1[0]);
		 * 
		 * 
		 * k= kv[0].split(";");
		 * System.out.print(" k==========1111111===========----------- "+k); //
		 * System.out.print(" k[0] ---------------- "+k[0]); Server.testCode =
		 * testCode.add(test_code_array);
		 * System.out.print(" Test_Code ----11111111------------ "+Test_Code);
		 */
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
		// Server.testCode = testCode;

		Server.packet = packet;
		// System.out.println("Packet "+Server.packet);
		// Server.packet.append("741");
		Server.sampleNo = bf;
		Server.Query_Message_Id = bf_header;
		Server.Query_Message_sender = bf_header_sender;
		Server.Query_Message_receiver = bf_header_receiver;
		Server.Query_Message_date_time = bf_header_datetime;
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
				// frameList = headerParsing(list.get(0));
				if (formatid.equals("20011"))// headerParsing_vitrose5600
				{
			//		frameList = headerParsing_vitrose56001(list.get(0));// vitrose5600
					frameList = headerParsing_vitrose5600_railtel(list.get(0));// vitrose5600
				}
				if (formatid.equals("20043"))// tecom tc6060
				{
		//			frameList = headerParsing_tecom_tc6060_multiPacket(list.get(0));// tecom
					frameList = headerParsing_tecom_TC6060_singlePacket(list.get(0));// tecom
				}
				if (formatid.equals("20045"))// mispa clinia
				{
					frameList = headerParsing_Mispa_Clinia_s(list.get(0));// mispa clinia
				}
			}

			break;
		}
		// System.out.println("PAcket:-----"+packet);
		return frameList;
	}
	// ------------------------------------------------------------

	// Header parsing and Reply Packet Generation Method//========== vitrose5600
	// PACKET Format id=20038
	public static List headerParsing_vitrose5600(String line) {
		SimpleDateFormat time_formatter2 = new SimpleDateFormat("yyyyMMddHHmmss");
		String current_time_str2 = time_formatter2.format(System.currentTimeMillis());
		System.out.println("current_time_str2---" + current_time_str2);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
		SimpleDateFormat time_formatter3 = new SimpleDateFormat("yyyyMMddHHmmss");
		String current_time_str3 = time_formatter3.format(System.currentTimeMillis());
		System.out.println("current_time_str2 after 1 second wait---" + current_time_str3);
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

		StringBuffer header = new StringBuffer("H|\\^&|");
//		header.append(Query_Message_Id);
		header.append("||");
		header.append("qnxa224");
		// header.append(Query_Message_receiver);
		header.append("|||||");
//		header.append(Query_Message_sender);
		header.append("|||LIS2-A|");
		header = header.append(Query_Message_date_time);
		// header = header.append(current_time_str);
		StringBuffer line3 = new StringBuffer("C|1|L|^^^^|G");
		StringBuffer line4 = new StringBuffer("L|1|N");
		// StringBuffer line6 = new StringBuffer("||||P||||ORH||||||||||Q");
		// StringBuffer line6 = new StringBuffer("||||A||||ORH||||||||||Q");
		StringBuffer line6 = new StringBuffer("|||F");
		StringBuffer line5 = new StringBuffer();
		char etb = 23;
		char eot = '';
		char etx = 3;
		counterPID_Packet = counterPID_Packet + 33;
		// P|1||IND-000166864033|||||
		// StringBuffer p = new StringBuffer("P|1||IND-"+counterPID_Packet+"||^|||");
		// StringBuffer p = new StringBuffer("P|1||||^|||");
		// StringBuffer p = new StringBuffer("P|1");
		// StringBuffer p = new StringBuffer("P|1|U000856|||^");
		StringBuffer p = new StringBuffer("P|");
		p.append(p_packet_count);
		p.append("|");
		p.append(Server.sampleNo.toString().replace("^", ""));
		p.append("|||");
		// p.append("RR^ABIGAIL^G||19780407|F||843 TALL OAKSDR^HAILVILLE, MD
		// 45831|||RASHAMDRA^SANJAY^V|S|||||||||||U7");
		p.append("^");
		// p.append(name_pat.trim());
		p.append("name");
		// p.append("^");
//		p.append("name");
//		p.append("^ab");
		// p.append("^|||M||");
//	    p.append("^|||M||");
		// p.append("|||||");
		// StringBuffer p = new StringBuffer("P|1||||^|||U||||||"); // P|1|||||||M
		StringBuffer c = new StringBuffer("C|1|I|Patient is complaining of shortness of breath and chest pain.|G");

		// StringBuffer Order = new StringBuffer("O|1||");
		StringBuffer Order = new StringBuffer();
		StringBuffer Order1 = new StringBuffer("O|2");
		List list = new ArrayList();
		StringBuffer frame = new StringBuffer();
		StringBuffer framevip = new StringBuffer();
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
			Server.cobas = cobas;
			Server.host = host;
			System.out.println("TIME:- " + time);
			// mode.replace(2, 5, "DWN"); //Replace TSREQ TO TSDWN
			/*
			 * if(=="TSREQ") { System.out.println("in tsreq");
			 * 
			 * }
			 */
			Server.stx = stx;
			Server.mode = mode;
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
				buf.append(cr);
				// Ascii of CR
				// Ascii of LF
				p.append(cr);
				buf.append(p);
				c.append(cr);
				// buf.append(c);
				// Order.append(Server.packet);
				// ==============================

				System.out.println("BA400_client.testCode.size()------- " + Server.testCode.size());
				// System.out.println("BA400_client.testCode.size(------- " +
				// BA400_client.testCode.toString().replace(null, " ").trim());

				// ---------------------------------
				/*
				 * if (BA400_client.testCode!=null &&BA400_client.testCode.size() ==1 ) {
				 * System.out.println("TEST-CODE LIST IS NOT NULL"); for (int k = 0; k <
				 * BA400_client.testCode.size(); k++) { Order.append("O|"); Order.append(k + 1);
				 * Order.append("|"); Order.append(BA400_client.sampleNo.toString().replace("^",
				 * "")); Order.append("||^^^");
				 * Order.append(BA400_client.testCode.get(k).toString().replace(";", ""));
				 * Order.append("|R|"); Order.append("|"); Order.append("||||A||||SER|||||||");
				 * Order.append(line6); Order.append(cr); buf.append(Order); Order.delete(0,
				 * Order.length());
				 * 
				 * 
				 * System.out.println("buf= " + buf.toString()); } } else if
				 * (BA400_client.testCode.size() >1 && BA400_client.testCode!=null) {
				 * System.out.println("TEST-CODE LIST IS NOT NULL"); for (int k = 0; k <
				 * BA400_client.testCode.size(); k++) {
				 * 
				 * Order.append("O|"); Order.append(k + 1); Order.append("|");
				 * Order.append(BA400_client.sampleNo.toString().replace("^", ""));
				 * Order.append("||^^^");
				 * Order.append(BA400_client.testCode.get(k).toString().replace(";", ""));
				 * Order.append("|R|"); Order.append("|"); Order.append("||||A||||SER|||||||");
				 * Order.append(line6); Order.append(cr); buf.append(Order); Order.delete(0,
				 * Order.length()); //
				 * 
				 * // System.out.println("frameList3= " + frameList3.toString()); //
				 * BA400_client.StxCounter = BA400_client.StxCounter + 1; }
				 * 
				 * }
				 */

				Order.append("O|1");
				// Order.append();
				Order.append("|");
				Order.append(Server.sampleNo.toString().replace("^", ""));
				Order.append("||^^^1.0000+");
				for (int k = 0; k < Server.testCode.size(); k++)

				{
					Order.append(Server.testCode.get(k));
					Order.append("+1.0");
					if (k != Server.testCode.size() - 1) {

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
				Order.append("||||N||||5|||||||");
				Order.append(line6);
				Order.append(cr);
				buf.append(Order);
				Order.delete(0, Order.length());

				System.out.println("buf= " + buf.toString());

				// ==============================================================

				// =================================================

				// ---------SECOND ORDER
				Order1.append("|");
				Order1.append(Server.sampleNo.toString().replace("^", ""));
				// Order.append("^50087^1^^S1^SC^|");
				Order1.append("||^^^MTBRIF|R|");
				Order1.append(current_time_str);
				Order1.append("");
				// -----------------------------
				/*
				 * if(Server.testCode!=null) { System.out.println("TEST-CODE LIST IS NOT NULL");
				 * for(int k=0;k<Server.testCode.size();k++)
				 * 
				 * { Order.append("^^^"); Order.append(Server.testCode.get(k));
				 * Order1.append("^^^"); Order1.append(Server.testCode.get(k));
				 * if(k!=Server.testCode.size()-1) { Order.append("^\\"); Order1.append("^\\");
				 * } else { Order.append("^"); Order1.append("^"); } } }
				 */
				/*
				 * else System.out.println("NO TEST CODE FOUND !!! TEST-CODE LIST IS NULL");
				 */
				// Order.append(cr);
				// buf.append(Order);

				// buf.append("");
				// buf.append("20181227190037"+"|"+"20181227190037");
				// buf.append("|");
				// buf.append(line6);

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
				// buf.append(cr);
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
			System.out.println("frame BEFORE FRAMING = " + buf.toString());
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
				System.out.println("First " + first + "  " + "last " + mid + " stx  " + stx);
				frame.append(stx);
				System.out.println("Server.StxCounter " + Server.StxCounter);
				frame.append(Server.StxCounter);
				System.out.println("STX COUNTER:- " + Server.StxCounter);
				Server.StxCounter++;
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
			Server.StxCounter = 1;
			newS.delete(0, newS.length());
			System.out.println("Final String after checksum" + frameList.size());
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
	// ------------------------------------------------------------

	// PACKET Format id=20043
	public static List headerParsing_tecom_TC6060_singlePacket(String line) {
		SimpleDateFormat time_formatter2 = new SimpleDateFormat("yyyyMMddHHmmss");
		String current_time_str2 = time_formatter2.format(System.currentTimeMillis());
		System.out.println("current_time_str2---" + current_time_str2);
		Server.StxCounter = 1;
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
		p.append(Server.Sample_code.get(0).toString().replace("^", ""));
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
		
			Server.cobas = cobas;
			Server.host = host;
			System.out.println("TIME:- " + time);
		
			Server.stx = stx;
			Server.mode = mode;
	

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

				System.out.println("BA400_client.testCode.size()------- " + Server.testCode.size());
				
				Order.append("O|1");
				// Order.append();
				Order.append("|^^|");
				Order.append(Server.Sample_code.get(0).toString().replace("^", ""));
				Order.append("|");
				for (int k = 0; k < Server.testCode.size(); k++)
				{
					Order.append(k + 1);
					Order.append("^");
					Order.append(Server.testCode.get(k));
					Order.append("^^");
					if (k != Server.testCode.size() - 1) {

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

				System.out.println("buf= " + buf.toString());

				// ==============================================================
			

				line4.append(cr);

				buf.append(line4);

				break;
			}


			System.out.println("STRING LENGTH BEFORE FRAMING = " + buf.length());
			System.out.println("frame BEFORE FRAMING = " + buf.toString());
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
				System.out.println("First " + first + "  " + "last " + mid + " stx  " + stx);
				frame.append(stx);
				System.out.println("Server.StxCounter " + Server.StxCounter);
				frame.append(Server.StxCounter);
				System.out.println("STX COUNTER:- " + Server.StxCounter);
				Server.StxCounter++;
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
			Server.StxCounter = 1;
			newS.delete(0, newS.length());
			System.out.println("Final String after checksum" + frameList.size());
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
	
	// ------------------------------------------------------------

	// PACKET Format id=20045
	public static List headerParsing_Mispa_Clinia_s(String line) {
		SimpleDateFormat time_formatter2 = new SimpleDateFormat("yyyyMMddHHmmss");
		String current_time_str2 = time_formatter2.format(System.currentTimeMillis());
		System.out.println("current_time_str2---" + current_time_str2);
		Server.StxCounter = 1;

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
//		header.append("^^");
	    header.append(Query_Message_receiver);
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
		p.append(Server.Sample_code.get(0).toString().replace("^", ""));
		p.append("||^");
		p.append(name_pat.trim());
		// p.append("^name^M");
		p.append("^|||||||||||||||");
		p.append("||||||||||||||");
		
		StringBuffer c = new StringBuffer("C|1|I|Patient is complaining of shortness of breath and chest pain.|G");


		StringBuffer Order = new StringBuffer();
		StringBuffer Order1 = new StringBuffer("O|2");
		List list = new ArrayList();
		StringBuffer frame = new StringBuffer();
		StringBuffer framevip = new StringBuffer();
		StringBuffer response = new StringBuffer();
		StringBuffer time = new StringBuffer();

		try {
		
			Server.cobas = cobas;
			 Server.host = host;
			System.out.println("TIME:- " + time);
		
			Server.stx = stx;
			Server.mode = mode;
	

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

				System.out.println("BA400_client.testCode.size()------- " + Server.testCode.size());
				
				Order.append("O|1");
				// Order.append();
				Order.append("|1^1^1|");
				Order.append(Server.Sample_code.get(0).toString().replace("^", ""));
				Order.append("|");
				for (int k = 0; k < Server.testCode.size(); k++)
				{
					Order.append(k + 1);
					Order.append("^");
					Order.append(Server.testCode.get(k));
					Order.append("^2^1");				
					if (k != Server.testCode.size() - 1) {

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

				System.out.println("buf= " + buf.toString());

				// ==============================================================
			

				line4.append(cr);

				buf.append(line4);

				break;
			}


			System.out.println("STRING LENGTH BEFORE FRAMING = " + buf.length());
			System.out.println("frame BEFORE FRAMING = " + buf.toString());
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
				System.out.println("First " + first + "  " + "last " + mid + " stx  " + stx);
				frame.append(stx);
				System.out.println("Server.StxCounter " + Server.StxCounter);
				frame.append(Server.StxCounter);
				System.out.println("STX COUNTER:- " + Server.StxCounter);
				Server.StxCounter++;
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
			Server.StxCounter = 1;
			newS.delete(0, newS.length());
			System.out.println("Final String after checksum" + frameList.size());
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
	
	// ------------------------------------------------------------

	// Header parsing and Reply Packet Generation Method//========== vitrose5600
	// PACKET Format id=20038
	public static List headerParsing_vitrose56001(String line) {
		SimpleDateFormat time_formatter2 = new SimpleDateFormat("yyyyMMddHHmmss");
		String current_time_str2 = time_formatter2.format(System.currentTimeMillis());
//		System.out.println("current_time_str2---" + current_time_str2);

		Server.StxCounter = 1;
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException ex) {
//			Thread.currentThread().interrupt();
//		}
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

		StringBuffer buf_header = new StringBuffer();
		StringBuffer buf_patient = new StringBuffer();
		StringBuffer buf_order = new StringBuffer();
		StringBuffer buf_order_1 = new StringBuffer();
		StringBuffer buf_termination = new StringBuffer();

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

		StringBuffer header = new StringBuffer("H|\\^&|");
//		header.append(Query_Message_Id);
		header.append("||");
		header.append("qnxa224");
		// header.append(Query_Message_receiver);
		header.append("|||||");
//		header.append(Query_Message_sender);
		header.append("|||LIS2-A|");
//		header = header.append(Query_Message_date_time);
	    header = header.append(current_time_str);
//		StringBuffer line3 = new StringBuffer("C|1|L|^^^^|G");
		StringBuffer line4 = new StringBuffer("L|1|N");
		// StringBuffer line6 = new StringBuffer("||||P||||ORH||||||||||Q");
		// StringBuffer line6 = new StringBuffer("||||A||||ORH||||||||||Q");
		StringBuffer line6 = new StringBuffer("|||O");
		StringBuffer line5 = new StringBuffer();
		char etb = 23;
		char eot = '';
		char etx = 3;
		counterPID_Packet = counterPID_Packet + 33;
		// P|1||IND-000166864033|||||
		// StringBuffer p = new StringBuffer("P|1||IND-"+counterPID_Packet+"||^|||");
		// StringBuffer p = new StringBuffer("P|1||||^|||");
		// StringBuffer p = new StringBuffer("P|1");
		// StringBuffer p = new StringBuffer("P|1|U000856|||^");
		StringBuffer p = new StringBuffer("P|");
		p.append(p_packet_count);
		p.append("|");
		// p.append(Server.sampleNo.toString().replace("^", ""));
		p.append(Server.Sample_code.get(0).toString().replace("^", ""));
		p.append("|||");
		// p.append("RR^ABIGAIL^G||19780407|F||843 TALL OAKSDR^HAILVILLE, MD
		// 45831|||RASHAMDRA^SANJAY^V|S|||||||||||U7");

		p.append(name_pat.trim());
		// p.append("^name^M");
//		p.append("^ab");
		// p.append("^|||M||");
//	    p.append("^|||M||");
		// p.append("|||||");
		// StringBuffer p = new StringBuffer("P|1||||^|||U||||||"); // P|1|||||||M
		StringBuffer c = new StringBuffer("C|1|I|Patient is complaining of shortness of breath and chest pain.|G");

		// StringBuffer Order = new StringBuffer("O|1||");
		StringBuffer Order = new StringBuffer();
		StringBuffer Order1 = new StringBuffer("O|2");
		List list = new ArrayList();
		StringBuffer frame = new StringBuffer();
		StringBuffer framevip = new StringBuffer();
		StringBuffer response = new StringBuffer();
		StringBuffer time = new StringBuffer();

		try {

			Server.cobas = cobas;
			Server.host = host;
			System.out.println("TIME:- " + time);
			// mode.replace(2, 5, "DWN"); //Replace TSREQ TO TSDWN
			/*
			 * if(=="TSREQ") { System.out.println("in tsreq");
			 * 
			 * }
			 */
			Server.stx = stx;
			Server.mode = mode;
			// MAKING STRING FOR REPLYING TO CLIENT MACHINE
			// SimpleDateFormat time_formatter1 = new SimpleDateFormat("yyyyMMddHHmmss");
			// String current_time_str1 =
			// time_formatter1.format(System.currentTimeMillis());

			while (true) {

				buf_header.append(header);
				buf_header.append(cr);
				buf_patient.append(p);
				buf_patient.append(cr);
				p.delete(0, p.length());
				// System.out.println("BA400_client.testCode.size()------- " +
				// Server.testCode.size());
				// System.out.println("BA400_client.testCode.size(------- " +
				// BA400_client.testCode.toString().replace(null, " ").trim());

				// ---------------------------------
				/*
				 * if (BA400_client.testCode!=null &&BA400_client.testCode.size() ==1 ) {
				 * System.out.println("TEST-CODE LIST IS NOT NULL"); for (int k = 0; k <
				 * BA400_client.testCode.size(); k++) { Order.append("O|"); Order.append(k + 1);
				 * Order.append("|"); Order.append(BA400_client.sampleNo.toString().replace("^",
				 * "")); Order.append("||^^^");
				 * Order.append(BA400_client.testCode.get(k).toString().replace(";", ""));
				 * Order.append("|R|"); Order.append("|"); Order.append("||||A||||SER|||||||");
				 * Order.append(line6); Order.append(cr); buf.append(Order); Order.delete(0,
				 * Order.length());
				 * 
				 * 
				 * System.out.println("buf= " + buf.toString()); } } else if
				 * (BA400_client.testCode.size() >1 && BA400_client.testCode!=null) {
				 * System.out.println("TEST-CODE LIST IS NOT NULL"); for (int k = 0; k <
				 * BA400_client.testCode.size(); k++) {
				 * 
				 * Order.append("O|"); Order.append(k + 1); Order.append("|");
				 * Order.append(BA400_client.sampleNo.toString().replace("^", ""));
				 * Order.append("||^^^");
				 * Order.append(BA400_client.testCode.get(k).toString().replace(";", ""));
				 * Order.append("|R|"); Order.append("|"); Order.append("||||A||||SER|||||||");
				 * Order.append(line6); Order.append(cr); buf.append(Order); Order.delete(0,
				 * Order.length()); //
				 * 
				 * // System.out.println("frameList3= " + frameList3.toString()); //
				 * BA400_client.StxCounter = BA400_client.StxCounter + 1; }
				 * 
				 * }
				 */
				// ======================================================
				frameList.clear();
				frame1.append(stx);
				frame1.append(Server.StxCounter);
				frame1.append(buf_header);
				buf_header.delete(0, buf_header.length());
				frame1.append(etx);
				frame1.append(CheckSum(frame1.toString())); // FRAME WITH STX STX COUNTER AND ETB
				frame1.append(cr);
				frame1.append(lf);
				frameList.add(frame1.toString());
				frame1.delete(0, frame1.length());
//				System.out.println("frameList= " + frameList.toString());

				Server.StxCounter = Server.StxCounter + 1;
				// frameList2.clear();
				frame2.append(stx);
				frame2.append(Server.StxCounter);
				frame2.append(buf_patient);
				buf_patient.delete(0, buf_patient.length());
				frame2.append(etx);
				frame2.append(CheckSum(frame2.toString())); // FRAME WITH STX STX COUNTER AND ETB
				frame2.append(cr);
				frame2.append(lf);
				frameList.add(frame2.toString());
				frame2.delete(0, frame2.length());
//				System.out.println("frameList= " + frameList.toString());

				// ====================================================================
				Order.append("O|1");
				// Order.append();
				Order.append("|");
				Order.append(Server.Sample_code.get(0).toString().replace("^", ""));
				Order.append("||^^^1.0000+");
				for (int k = 0; k < Server.testCode_mapList.size(); k++)

				{
					Order.append(Server.testCode_mapList.get(k));
					Order.append("+1.0");
					if (k != Server.testCode_mapList.size() - 1) {

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
				Order.append("||||N||||5|||||||");
				Order.append(line6);
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
					if (Server.StxCounter == 8)
						Server.StxCounter = 0;
					if (length < 240)
						// if (length < 306)
						mid = mid + length;
					else if (length == 240)
						mid = mid + length;
					else if (length > 240) {
						mid = mid + 240;
					}
					// System.out.println("First " + first + " " + "last " + mid + " stx " + stx);
					frame3.append(stx);
					Server.StxCounter = Server.StxCounter + 1;
					// System.out.println("Server.StxCounter " + Server.StxCounter);
					frame3.append(Server.StxCounter);
					// System.out.println("STX COUNTER:- " + Server.StxCounter);

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
				// -------------------------------------
				// ==============================================================

				while (count_ack_BA400_s != 0) {
					if (Server.StxCounter == 8)
						Server.StxCounter = 0;
					p_packet_count = p_packet_count + 1;
					// System.out.println("Sample_code.get(0).toString()---"+Sample_code.get(0).toString());
					count_ack_BA400_s = count_ack_BA400_s - 1;
					if (count_ack_BA400_s == 0) {
						Sample_code.clear();
						p_packet_count = 1;
						testCode.clear();

						break;

					}
					Sample_code.remove(sam_count);
					sam_count = 0;
					parseSelect2();
					p.append("P|");
					p.append(p_packet_count);
					p.append("||");
					p.append(Sample_code.get(0).toString().replace("^", ""));
					p.append("||^");
					p.append(name_pat.trim());
					// p.append("|||");
					p.append(cr);
					buf_patient.append(p);
					p.delete(0, p.length());

					Server.StxCounter = Server.StxCounter + 1;

					frame2.append(stx);
					frame2.append(Server.StxCounter);
					frame2.append(buf_patient);
					buf_patient.delete(0, buf_patient.length());
					frame2.append(etx);
					frame2.append(CheckSum(frame2.toString())); // FRAME WITH STX STX COUNTER AND ETB
					frame2.append(cr);
					frame2.append(lf);
					frameList.add(frame2.toString());
					frame2.delete(0, frame2.length());
					// System.out.println("frameList= " + frameList.toString());

					// ---------------------------------

					// if (testCode_mapList.size() > 1 && testCode_mapList != null)
					// {
					Order.append("O|1");
					// Order.append();
					Order.append("|");
					Order.append(Server.Sample_code.get(0).toString().replace("^", ""));
					Order.append("||^^^1.0000+");
					for (int k = 0; k < Server.testCode_mapList.size(); k++) {
						Order.append(Server.testCode_mapList.get(k));
						Order.append("+1.0");
						if (k != Server.testCode_mapList.size() - 1) {

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
					Order.append("||||N||||5|||||||");
					Order.append(line6);
					Order.append(cr);

					buf_order.append(Order);
					Order.delete(0, Order.length());

					// System.out.println("buf_order= " + buf_order.toString());

					// --------------------------------------------
					// System.out.println("STRING LENGTH BEFORE FRAMING buf_order= " +
					// buf_order.length());

					// STRING LENGTH BEFORE FRAMING = 235
					int frameCount1 = 0, first1 = 0, mid1 = 0, remain1 = 0;
					frameCount1 = FrameCounter(buf_order.length());
					// System.out.println("Char At 0 "+buf.charAt(0));
					int length1 = buf_order.length();

					for (int i = 1; i <= frameCount1; i++) {
						if (length1 < 240)
							// if (length < 306)
							mid1 = mid1 + length1;
						else if (length1 == 240)
							mid1 = mid1 + length1;
						else if (length1 > 240) {
							mid1 = mid1 + 240;
						}
						// System.out.println("First1 " + first1 + " " + "last1 " + mid1 + " stx1 " +
						// stx);
						frame3.append(stx);
						Server.StxCounter = Server.StxCounter + 1;
						// System.out.println("Server.StxCounter " + Server.StxCounter);
						frame3.append(Server.StxCounter);
						// System.out.println("STX COUNTER:- " + Server.StxCounter);

						frame3.append(buf_order.substring(first1, mid1));

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
						length1 = length1 - 240;
						first1 = mid1;

						frameList.add(frame3.toString());
						/*
						 * if(i ==1) frame1.append(frame); else frame2.append(frame);
						 */
						frame3.delete(0, frame3.length());

					}

					// -------------------------------------

					// ===============================

					// }

				}
				// =================================================

				// -------------------------------------------------------------
//
//				BA400_client.StxCounter = BA400_client.StxCounter + 1;
//				frameList3.clear();
//				frame3.append(stx);
//				frame3.append(BA400_client.StxCounter);
//				frame3.append(buf_order);
//				buf_order.delete(0, buf_order.length());
//				frame3.append(etb);
//				frame3.append(CheckSum(frame3.toString())); // FRAME WITH STX STX COUNTER AND ETB
//				frame3.append(cr);
//				frame3.append(lf);
//				frameList3.add(frame3.toString());
//				frame3.delete(0, frame3.length());

				// System.out.println("frameList3= " + frameList.toString());

				// ==============================================================

				// =================================================

				// ---------SECOND ORDER
				Order1.append("|");
				Order1.append(Server.sampleNo.toString().replace("^", ""));
				// Order.append("^50087^1^^S1^SC^|");
				Order1.append("||^^^MTBRIF|R|");
				Order1.append(current_time_str);
				Order1.append("");
				// -----------------------------
				/*
				 * if(Server.testCode!=null) { System.out.println("TEST-CODE LIST IS NOT NULL");
				 * for(int k=0;k<Server.testCode.size();k++)
				 * 
				 * { Order.append("^^^"); Order.append(Server.testCode.get(k));
				 * Order1.append("^^^"); Order1.append(Server.testCode.get(k));
				 * if(k!=Server.testCode.size()-1) { Order.append("^\\"); Order1.append("^\\");
				 * } else { Order.append("^"); Order1.append("^"); } } }
				 */
				/*
				 * else System.out.println("NO TEST CODE FOUND !!! TEST-CODE LIST IS NULL");
				 */
				// Order.append(cr);
				// buf.append(Order);

				// buf.append("");
				// buf.append("20181227190037"+"|"+"20181227190037");
				// buf.append("|");
				// buf.append(line6);

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
				// buf.append(cr);
				// buf.append(line3);

				// buf.append(cr);

				line4.append(cr);

				buf.append(line4);
				buf_termination.append(line4);

				break;
			}

			// ========================================

			Server.StxCounter = Server.StxCounter + 1;
			// frameList4.clear();
			frame4.append(stx);
			frame4.append(Server.StxCounter);
			frame4.append(buf_termination);
			buf_termination.delete(0, buf_termination.length());
			frame4.append(etx);
			frame4.append(CheckSum(frame4.toString())); // FRAME WITH STX STX COUNTER AND ETB
			frame4.append(cr);
			frame4.append(lf);

			frameList.add(frame4.toString());
			frame4.delete(0, frame4.length());

			// System.out.println("frameList= " + frameList.toString());
			// =======================================

			// CODE TO DELETE LF IF EXISTS IN STRING *** STARTS ***
			/*
			 * for(int i=0;i<buf.length();i++) { if(buf.charAt(i)!=10)
			 * faltu.append(buf.charAt(i)); else System.out.println("yesssss"); }
			 * buf.delete(0, buf.length()); buf = faltu;
			 */
			// CODE TO DELETE LF IF EXISTS IN STRING *** ENDS ***

//			System.out.println("STRING LENGTH BEFORE FRAMING = " + buf.length());
//			System.out.println("frame BEFORE FRAMING = " + buf.toString());
//			// STRING LENGTH BEFORE FRAMING = 235
//			int frameCount = 0, first = 0, mid = 0, remain = 0;
//			frameCount = FrameCounter(buf.length());
//			// System.out.println("Char At 0 "+buf.charAt(0));
//			int length = buf.length();
//
//			for (int i = 1; i <= frameCount; i++) {
//				if (length < 240)
//					// if (length < 306)
//					mid = mid + length;
//				else if (length == 240)
//					mid = mid + length;
//				else if (length > 240) {
//					mid = mid + 240;
//				}
//				System.out.println("First " + first + "  " + "last " + mid + " stx  " + stx);
//				frame.append(stx);
//				System.out.println("Server.StxCounter " + Server.StxCounter);
//				frame.append(Server.StxCounter);
//				System.out.println("STX COUNTER:- " + Server.StxCounter);
//				Server.StxCounter++;
//				frame.append(buf.substring(first, mid));
//
//				if (i == frameCount) {
//					frame.append(etx);
//					System.out.println("before checksum" + frame.toString());
//					frame.append(CheckSum(frame.toString())); // FRAME WITH STX STX COUNTER AND ETX
//					// frame.append(CheckSum(buf.substring(first, mid))); //FRAME WITH ONLY
//					// CHARACTERS
//					frame.append(cr);
//					frame.append(lf);
//				} else
//
//				{
//					frame.append(etb);
//					System.out.println("before checksum" + frame.toString());
//					frame.append(CheckSum(frame.toString())); // FRAME WITH STX STX COUNTER AND ETB
//					// frame.append(CheckSum(buf.substring(first, mid)));
//					frame.append(cr);
//					frame.append(lf);
//				}
//				// length = length - mid;
//				length = length - 240;
//				first = mid;
//
//				response.append(frame);
//				frameList.add(frame.toString());
//				/*
//				 * if(i ==1) frame1.append(frame); else frame2.append(frame);
//				 */
//				frame.delete(0, frame.length());
//
//			}
//			// response.append(eot);
//			// frameList.add(eot);
//			Server.StxCounter = 1;
//			newS.delete(0, newS.length());
			// System.out.println("Final String after checksum" + frameList.size());
			for (int i = 0; i < frameList.size(); i++) {
				count_ack_BA400_o1++;
			}
			// System.out.println("count_ack_BA400_o1------- " + count_ack_BA400_o1);
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
	// ------------------------------------------------------------

	// Header parsing and Reply Packet Generation Method//========== vitrose5600
	// PACKET Format id=20038
	public static List headerParsing_vitrose5600_railtel(String line) {
		SimpleDateFormat time_formatter2 = new SimpleDateFormat("yyyyMMddHHmmss");
		String current_time_str2 = time_formatter2.format(System.currentTimeMillis());
//		System.out.println("current_time_str2---" + current_time_str2);

		Server.StxCounter = 1;
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException ex) {
//			Thread.currentThread().interrupt();
//		}
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

		StringBuffer buf_header = new StringBuffer();
		StringBuffer buf_patient = new StringBuffer();
		StringBuffer buf_order = new StringBuffer();
		StringBuffer buf_order_1 = new StringBuffer();
		StringBuffer buf_termination = new StringBuffer();

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

		StringBuffer header = new StringBuffer("H|\\^&|");
//		header.append(Query_Message_Id);
		header.append("||");
		header.append("qnxa224");
		// header.append(Query_Message_receiver);
		header.append("|||||");
//		header.append(Query_Message_sender);
		header.append("|||LIS2-A|");
//		header = header.append(Query_Message_date_time);
	    header = header.append(current_time_str);
//		StringBuffer line3 = new StringBuffer("C|1|L|^^^^|G");
		StringBuffer line4 = new StringBuffer("L|1|N");
		// StringBuffer line6 = new StringBuffer("||||P||||ORH||||||||||Q");
		// StringBuffer line6 = new StringBuffer("||||A||||ORH||||||||||Q");
		StringBuffer line6 = new StringBuffer("|||O");
		StringBuffer line5 = new StringBuffer();
		char etb = 23;
		char eot = '';
		char etx = 3;
		counterPID_Packet = counterPID_Packet + 33;
		// P|1||IND-000166864033|||||
		// StringBuffer p = new StringBuffer("P|1||IND-"+counterPID_Packet+"||^|||");
		// StringBuffer p = new StringBuffer("P|1||||^|||");
		// StringBuffer p = new StringBuffer("P|1");
		// StringBuffer p = new StringBuffer("P|1|U000856|||^");
		StringBuffer p = new StringBuffer("P|");
		p.append(p_packet_count);
		p.append("|");
		// p.append(Server.sampleNo.toString().replace("^", ""));
		p.append(Server.Sample_code.get(0).toString().replace("^", ""));
		p.append("|||");
		// p.append("RR^ABIGAIL^G||19780407|F||843 TALL OAKSDR^HAILVILLE, MD
		// 45831|||RASHAMDRA^SANJAY^V|S|||||||||||U7");

	//	p.append(name_pat.trim());
		p.append(name_pat_last.trim());
		p.append("^");
		p.append(name_pat_first.trim());
		// p.append("^name^M");
//		p.append("^ab");
		// p.append("^|||M||");
//	    p.append("^|||M||");
		// p.append("|||||");
		// StringBuffer p = new StringBuffer("P|1||||^|||U||||||"); // P|1|||||||M
		StringBuffer c = new StringBuffer("C|1|I|Patient is complaining of shortness of breath and chest pain.|G");

		// StringBuffer Order = new StringBuffer("O|1||");
		StringBuffer Order = new StringBuffer();
		StringBuffer Order1 = new StringBuffer("O|2");
		List list = new ArrayList();
		StringBuffer frame = new StringBuffer();
		StringBuffer framevip = new StringBuffer();
		StringBuffer response = new StringBuffer();
		StringBuffer time = new StringBuffer();

		try {

			Server.cobas = cobas;
			Server.host = host;
			System.out.println("TIME:- " + time);
			// mode.replace(2, 5, "DWN"); //Replace TSREQ TO TSDWN
			/*
			 * if(=="TSREQ") { System.out.println("in tsreq");
			 * 
			 * }
			 */
			Server.stx = stx;
			Server.mode = mode;
			// MAKING STRING FOR REPLYING TO CLIENT MACHINE
			// SimpleDateFormat time_formatter1 = new SimpleDateFormat("yyyyMMddHHmmss");
			// String current_time_str1 =
			// time_formatter1.format(System.currentTimeMillis());

			while (true) {

				buf_header.append(header);
				buf_header.append(cr);
				buf_patient.append(p);
				buf_patient.append(cr);
				p.delete(0, p.length());
				// System.out.println("BA400_client.testCode.size()------- " +
				// Server.testCode.size());
				// System.out.println("BA400_client.testCode.size(------- " +
				// BA400_client.testCode.toString().replace(null, " ").trim());

				// ---------------------------------
				/*
				 * if (BA400_client.testCode!=null &&BA400_client.testCode.size() ==1 ) {
				 * System.out.println("TEST-CODE LIST IS NOT NULL"); for (int k = 0; k <
				 * BA400_client.testCode.size(); k++) { Order.append("O|"); Order.append(k + 1);
				 * Order.append("|"); Order.append(BA400_client.sampleNo.toString().replace("^",
				 * "")); Order.append("||^^^");
				 * Order.append(BA400_client.testCode.get(k).toString().replace(";", ""));
				 * Order.append("|R|"); Order.append("|"); Order.append("||||A||||SER|||||||");
				 * Order.append(line6); Order.append(cr); buf.append(Order); Order.delete(0,
				 * Order.length());
				 * 
				 * 
				 * System.out.println("buf= " + buf.toString()); } } else if
				 * (BA400_client.testCode.size() >1 && BA400_client.testCode!=null) {
				 * System.out.println("TEST-CODE LIST IS NOT NULL"); for (int k = 0; k <
				 * BA400_client.testCode.size(); k++) {
				 * 
				 * Order.append("O|"); Order.append(k + 1); Order.append("|");
				 * Order.append(BA400_client.sampleNo.toString().replace("^", ""));
				 * Order.append("||^^^");
				 * Order.append(BA400_client.testCode.get(k).toString().replace(";", ""));
				 * Order.append("|R|"); Order.append("|"); Order.append("||||A||||SER|||||||");
				 * Order.append(line6); Order.append(cr); buf.append(Order); Order.delete(0,
				 * Order.length()); //
				 * 
				 * // System.out.println("frameList3= " + frameList3.toString()); //
				 * BA400_client.StxCounter = BA400_client.StxCounter + 1; }
				 * 
				 * }
				 */
				// ======================================================
				frameList.clear();
				frame1.append(stx);
				frame1.append(Server.StxCounter);
				frame1.append(buf_header);
				buf_header.delete(0, buf_header.length());
				frame1.append(etx);
				frame1.append(CheckSum(frame1.toString())); // FRAME WITH STX STX COUNTER AND ETB
				frame1.append(cr);
				frame1.append(lf);
				frameList.add(frame1.toString());
				frame1.delete(0, frame1.length());
//				System.out.println("frameList= " + frameList.toString());

				Server.StxCounter = Server.StxCounter + 1;
				// frameList2.clear();
				frame2.append(stx);
				frame2.append(Server.StxCounter);
				frame2.append(buf_patient);
				buf_patient.delete(0, buf_patient.length());
				frame2.append(etx);
				frame2.append(CheckSum(frame2.toString())); // FRAME WITH STX STX COUNTER AND ETB
				frame2.append(cr);
				frame2.append(lf);
				frameList.add(frame2.toString());
				frame2.delete(0, frame2.length());
//				System.out.println("frameList= " + frameList.toString());

				// ====================================================================
				Order.append("O|1");
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
				Order.append("||||N||||5|||||||");
				Order.append(line6);
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
					if (Server.StxCounter == 8)
						Server.StxCounter = 0;
					if (length < 240)
						// if (length < 306)
						mid = mid + length;
					else if (length == 240)
						mid = mid + length;
					else if (length > 240) {
						mid = mid + 240;
					}
					// System.out.println("First " + first + " " + "last " + mid + " stx " + stx);
					frame3.append(stx);
					Server.StxCounter = Server.StxCounter + 1;
					// System.out.println("Server.StxCounter " + Server.StxCounter);
					frame3.append(Server.StxCounter);
					// System.out.println("STX COUNTER:- " + Server.StxCounter);

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
				// -------------------------------------
				// ==============================================================

				while (count_ack_BA400_s != 0) {
					if (Server.StxCounter == 8)
						Server.StxCounter = 0;
					p_packet_count = p_packet_count + 1;
					// System.out.println("Sample_code.get(0).toString()---"+Sample_code.get(0).toString());
					count_ack_BA400_s = count_ack_BA400_s - 1;
					if (count_ack_BA400_s == 0) {
						Sample_code.clear();
						p_packet_count = 1;
						testCode.clear();

						break;

					}
					Sample_code.remove(sam_count);
					sam_count = 0;
					parseSelect2();
					p.append("P|");
					p.append(p_packet_count);
					p.append("||");
					p.append(Sample_code.get(0).toString().replace("^", ""));
					p.append("||^");
					p.append(name_pat.trim());
					// p.append("|||");
					p.append(cr);
					buf_patient.append(p);
					p.delete(0, p.length());

					Server.StxCounter = Server.StxCounter + 1;

					frame2.append(stx);
					frame2.append(Server.StxCounter);
					frame2.append(buf_patient);
					buf_patient.delete(0, buf_patient.length());
					frame2.append(etx);
					frame2.append(CheckSum(frame2.toString())); // FRAME WITH STX STX COUNTER AND ETB
					frame2.append(cr);
					frame2.append(lf);
					frameList.add(frame2.toString());
					frame2.delete(0, frame2.length());
					// System.out.println("frameList= " + frameList.toString());

					// ---------------------------------

					// if (testCode_mapList.size() > 1 && testCode_mapList != null)
					// {
					Order.append("O|1");
					// Order.append();
					Order.append("|");
					Order.append(Server.Sample_code.get(0).toString().replace("^", ""));
					Order.append("||^^^1.0000+");
					for (int k = 0; k < Server.testCode.size(); k++) {
						Order.append(Server.testCode.get(k));
						Order.append("+1.0");
						if (k != Server.testCode.size() - 1) {

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
					Order.append("||||N||||5|||||||");
					Order.append(line6);
					Order.append(cr);

					buf_order.append(Order);
					Order.delete(0, Order.length());

					// System.out.println("buf_order= " + buf_order.toString());

					// --------------------------------------------
					// System.out.println("STRING LENGTH BEFORE FRAMING buf_order= " +
					// buf_order.length());

					// STRING LENGTH BEFORE FRAMING = 235
					int frameCount1 = 0, first1 = 0, mid1 = 0, remain1 = 0;
					frameCount1 = FrameCounter(buf_order.length());
					// System.out.println("Char At 0 "+buf.charAt(0));
					int length1 = buf_order.length();

					for (int i = 1; i <= frameCount1; i++) {
						if (length1 < 240)
							// if (length < 306)
							mid1 = mid1 + length1;
						else if (length1 == 240)
							mid1 = mid1 + length1;
						else if (length1 > 240) {
							mid1 = mid1 + 240;
						}
						// System.out.println("First1 " + first1 + " " + "last1 " + mid1 + " stx1 " +
						// stx);
						frame3.append(stx);
						Server.StxCounter = Server.StxCounter + 1;
						// System.out.println("Server.StxCounter " + Server.StxCounter);
						frame3.append(Server.StxCounter);
						// System.out.println("STX COUNTER:- " + Server.StxCounter);

						frame3.append(buf_order.substring(first1, mid1));

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
						length1 = length1 - 240;
						first1 = mid1;

						frameList.add(frame3.toString());
						/*
						 * if(i ==1) frame1.append(frame); else frame2.append(frame);
						 */
						frame3.delete(0, frame3.length());

					}

					// -------------------------------------

					// ===============================

					// }

				}
				// =================================================

				// -------------------------------------------------------------
//
//				BA400_client.StxCounter = BA400_client.StxCounter + 1;
//				frameList3.clear();
//				frame3.append(stx);
//				frame3.append(BA400_client.StxCounter);
//				frame3.append(buf_order);
//				buf_order.delete(0, buf_order.length());
//				frame3.append(etb);
//				frame3.append(CheckSum(frame3.toString())); // FRAME WITH STX STX COUNTER AND ETB
//				frame3.append(cr);
//				frame3.append(lf);
//				frameList3.add(frame3.toString());
//				frame3.delete(0, frame3.length());

				// System.out.println("frameList3= " + frameList.toString());

				// ==============================================================

				// =================================================

				// ---------SECOND ORDER
				Order1.append("|");
				Order1.append(Server.sampleNo.toString().replace("^", ""));
				// Order.append("^50087^1^^S1^SC^|");
				Order1.append("||^^^MTBRIF|R|");
				Order1.append(current_time_str);
				Order1.append("");
				// -----------------------------
				/*
				 * if(Server.testCode!=null) { System.out.println("TEST-CODE LIST IS NOT NULL");
				 * for(int k=0;k<Server.testCode.size();k++)
				 * 
				 * { Order.append("^^^"); Order.append(Server.testCode.get(k));
				 * Order1.append("^^^"); Order1.append(Server.testCode.get(k));
				 * if(k!=Server.testCode.size()-1) { Order.append("^\\"); Order1.append("^\\");
				 * } else { Order.append("^"); Order1.append("^"); } } }
				 */
				/*
				 * else System.out.println("NO TEST CODE FOUND !!! TEST-CODE LIST IS NULL");
				 */
				// Order.append(cr);
				// buf.append(Order);

				// buf.append("");
				// buf.append("20181227190037"+"|"+"20181227190037");
				// buf.append("|");
				// buf.append(line6);

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
				// buf.append(cr);
				// buf.append(line3);

				// buf.append(cr);

				line4.append(cr);

				buf.append(line4);
				buf_termination.append(line4);

				break;
			}

			// ========================================

			Server.StxCounter = Server.StxCounter + 1;
			// frameList4.clear();
			frame4.append(stx);
			frame4.append(Server.StxCounter);
			frame4.append(buf_termination);
			buf_termination.delete(0, buf_termination.length());
			frame4.append(etx);
			frame4.append(CheckSum(frame4.toString())); // FRAME WITH STX STX COUNTER AND ETB
			frame4.append(cr);
			frame4.append(lf);

			frameList.add(frame4.toString());
			frame4.delete(0, frame4.length());

			// System.out.println("frameList= " + frameList.toString());
			// =======================================

			// CODE TO DELETE LF IF EXISTS IN STRING *** STARTS ***
			/*
			 * for(int i=0;i<buf.length();i++) { if(buf.charAt(i)!=10)
			 * faltu.append(buf.charAt(i)); else System.out.println("yesssss"); }
			 * buf.delete(0, buf.length()); buf = faltu;
			 */
			// CODE TO DELETE LF IF EXISTS IN STRING *** ENDS ***

//			System.out.println("STRING LENGTH BEFORE FRAMING = " + buf.length());
//			System.out.println("frame BEFORE FRAMING = " + buf.toString());
//			// STRING LENGTH BEFORE FRAMING = 235
//			int frameCount = 0, first = 0, mid = 0, remain = 0;
//			frameCount = FrameCounter(buf.length());
//			// System.out.println("Char At 0 "+buf.charAt(0));
//			int length = buf.length();
//
//			for (int i = 1; i <= frameCount; i++) {
//				if (length < 240)
//					// if (length < 306)
//					mid = mid + length;
//				else if (length == 240)
//					mid = mid + length;
//				else if (length > 240) {
//					mid = mid + 240;
//				}
//				System.out.println("First " + first + "  " + "last " + mid + " stx  " + stx);
//				frame.append(stx);
//				System.out.println("Server.StxCounter " + Server.StxCounter);
//				frame.append(Server.StxCounter);
//				System.out.println("STX COUNTER:- " + Server.StxCounter);
//				Server.StxCounter++;
//				frame.append(buf.substring(first, mid));
//
//				if (i == frameCount) {
//					frame.append(etx);
//					System.out.println("before checksum" + frame.toString());
//					frame.append(CheckSum(frame.toString())); // FRAME WITH STX STX COUNTER AND ETX
//					// frame.append(CheckSum(buf.substring(first, mid))); //FRAME WITH ONLY
//					// CHARACTERS
//					frame.append(cr);
//					frame.append(lf);
//				} else
//
//				{
//					frame.append(etb);
//					System.out.println("before checksum" + frame.toString());
//					frame.append(CheckSum(frame.toString())); // FRAME WITH STX STX COUNTER AND ETB
//					// frame.append(CheckSum(buf.substring(first, mid)));
//					frame.append(cr);
//					frame.append(lf);
//				}
//				// length = length - mid;
//				length = length - 240;
//				first = mid;
//
//				response.append(frame);
//				frameList.add(frame.toString());
//				/*
//				 * if(i ==1) frame1.append(frame); else frame2.append(frame);
//				 */
//				frame.delete(0, frame.length());
//
//			}
//			// response.append(eot);
//			// frameList.add(eot);
//			Server.StxCounter = 1;
//			newS.delete(0, newS.length());
			// System.out.println("Final String after checksum" + frameList.size());
			for (int i = 0; i < frameList.size(); i++) {
				count_ack_BA400_o1++;
			}
			// System.out.println("count_ack_BA400_o1------- " + count_ack_BA400_o1);
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
	// ------------------------------------------------------------

	// Header parsing and Reply Packet Generation Method//========== vitrose5600

	public static List headerParsing_tecom_tc6060_multiPacket(String line) {
		SimpleDateFormat time_formatter2 = new SimpleDateFormat("yyyyMMddHHmmss");
		String current_time_str2 = time_formatter2.format(System.currentTimeMillis());
		System.out.println("current_time_str2---" + current_time_str2);

		Server.StxCounter = 1;
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException ex) {
//			Thread.currentThread().interrupt();
//		}
		SimpleDateFormat time_formatter3 = new SimpleDateFormat("yyyyMMddHHmmss");
		String current_time_str3 = time_formatter3.format(System.currentTimeMillis());
		System.out.println("current_time_str2 after 1 second wait---" + current_time_str3);
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

		StringBuffer buf_header = new StringBuffer();
		StringBuffer buf_patient = new StringBuffer();
		StringBuffer buf_order = new StringBuffer();
		StringBuffer buf_order_1 = new StringBuffer();
		StringBuffer buf_termination = new StringBuffer();

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

		StringBuffer header = new StringBuffer("H|\\^&|");
//		header.append(Query_Message_Id);
		header.append("||");
//		header.append("TC6030^01.03.07.03^123456");
		header.append("^^");
		// header.append(Query_Message_receiver);
		header.append("|||||");
//		header.append(Query_Message_sender);
		header.append("||SA|1394-97|");
//		header = header.append(Query_Message_date_time);
//		header = header.append(current_time_str);
//		StringBuffer line3 = new StringBuffer("C|1|L|^^^^|G");
		StringBuffer line4 = new StringBuffer("L|1|N");
		// StringBuffer line6 = new StringBuffer("||||P||||ORH||||||||||Q");
		// StringBuffer line6 = new StringBuffer("||||A||||ORH||||||||||Q");
		StringBuffer line6 = new StringBuffer("|||O");
		StringBuffer line5 = new StringBuffer();
		char etb = 23;
		char eot = '';
		char etx = 3;
		counterPID_Packet = counterPID_Packet + 33;
		// P|1||IND-000166864033|||||
		// StringBuffer p = new StringBuffer("P|1||IND-"+counterPID_Packet+"||^|||");
		// StringBuffer p = new StringBuffer("P|1||||^|||");
		// StringBuffer p = new StringBuffer("P|1");
		// StringBuffer p = new StringBuffer("P|1|U000856|||^");
		StringBuffer p = new StringBuffer("P|");
		p.append(p_packet_count);
		p.append("||");
		// p.append(Server.sampleNo.toString().replace("^", ""));
		p.append(Server.Sample_code.get(0).toString().replace("^", ""));
		p.append("||");
		// p.append("RR^ABIGAIL^G||19780407|F||843 TALL OAKSDR^HAILVILLE, MD
		// 45831|||RASHAMDRA^SANJAY^V|S|||||||||||U7");

		p.append(name_pat.trim());
		// p.append("^name^M");
	//	p.append("|||M|||A|||||||||||||||||||||||");
		// p.append("^|||M||");
//	    p.append("^|||M||");
		// p.append("|||||");
		// StringBuffer p = new StringBuffer("P|1||||^|||U||||||"); // P|1|||||||M
		StringBuffer c = new StringBuffer("C|1|I|Patient is complaining of shortness of breath and chest pain.|G");

		// StringBuffer Order = new StringBuffer("O|1||");
		StringBuffer Order = new StringBuffer();
		StringBuffer Order1 = new StringBuffer("O|2");
		List list = new ArrayList();
		StringBuffer frame = new StringBuffer();
		StringBuffer framevip = new StringBuffer();
		StringBuffer response = new StringBuffer();
		StringBuffer time = new StringBuffer();

		try {

			Server.cobas = cobas;
			Server.host = host;
			System.out.println("TIME:- " + time);
			// mode.replace(2, 5, "DWN"); //Replace TSREQ TO TSDWN
			/*
			 * if(=="TSREQ") { System.out.println("in tsreq");
			 * 
			 * }
			 */
			Server.stx = stx;
			Server.mode = mode;
			// MAKING STRING FOR REPLYING TO CLIENT MACHINE
			// SimpleDateFormat time_formatter1 = new SimpleDateFormat("yyyyMMddHHmmss");
			// String current_time_str1 =
			// time_formatter1.format(System.currentTimeMillis());

			while (true) {

				buf_header.append(header);
				buf_header.append(cr);
				buf_patient.append(p);
				buf_patient.append(cr);
				p.delete(0, p.length());
				// System.out.println("BA400_client.testCode.size()------- " +
				// Server.testCode.size());
				// System.out.println("BA400_client.testCode.size(------- " +
				// BA400_client.testCode.toString().replace(null, " ").trim());

				// ---------------------------------
				/*
				 * if (BA400_client.testCode!=null &&BA400_client.testCode.size() ==1 ) {
				 * System.out.println("TEST-CODE LIST IS NOT NULL"); for (int k = 0; k <
				 * BA400_client.testCode.size(); k++) { Order.append("O|"); Order.append(k + 1);
				 * Order.append("|"); Order.append(BA400_client.sampleNo.toString().replace("^",
				 * "")); Order.append("||^^^");
				 * Order.append(BA400_client.testCode.get(k).toString().replace(";", ""));
				 * Order.append("|R|"); Order.append("|"); Order.append("||||A||||SER|||||||");
				 * Order.append(line6); Order.append(cr); buf.append(Order); Order.delete(0,
				 * Order.length());
				 * 
				 * 
				 * System.out.println("buf= " + buf.toString()); } } else if
				 * (BA400_client.testCode.size() >1 && BA400_client.testCode!=null) {
				 * System.out.println("TEST-CODE LIST IS NOT NULL"); for (int k = 0; k <
				 * BA400_client.testCode.size(); k++) {
				 * 
				 * Order.append("O|"); Order.append(k + 1); Order.append("|");
				 * Order.append(BA400_client.sampleNo.toString().replace("^", ""));
				 * Order.append("||^^^");
				 * Order.append(BA400_client.testCode.get(k).toString().replace(";", ""));
				 * Order.append("|R|"); Order.append("|"); Order.append("||||A||||SER|||||||");
				 * Order.append(line6); Order.append(cr); buf.append(Order); Order.delete(0,
				 * Order.length()); //
				 * 
				 * // System.out.println("frameList3= " + frameList3.toString()); //
				 * BA400_client.StxCounter = BA400_client.StxCounter + 1; }
				 * 
				 * }
				 */
				// ======================================================
				frameList.clear();
				frame1.append(stx);
				frame1.append(Server.StxCounter);
				frame1.append(buf_header);
				buf_header.delete(0, buf_header.length());
				frame1.append(etx);
				frame1.append(CheckSum(frame1.toString())); // FRAME WITH STX STX COUNTER AND ETB
				frame1.append(cr);
				frame1.append(lf);
				frameList.add(frame1.toString());
				frame1.delete(0, frame1.length());
				System.out.println("frameList= " + frameList.toString());

				Server.StxCounter = Server.StxCounter + 1;
				// frameList2.clear();
				frame2.append(stx);
				frame2.append(Server.StxCounter);
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
			//	Order.append("|1^1^1|");
				Order.append("||");
				Order.append(Server.Sample_code.get(0).toString().replace("^", ""));
				Order.append("|");
				for (int k = 0; k < Server.testCode.size(); k++)

				{
					Order.append(k + 1);
					Order.append("^");
					Order.append(Server.testCode.get(k));
				//	Order.append("^2^1");
					Order.append("^^");
					if (k != Server.testCode.size() - 1) {

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
				Order.append("||||||||Serum|||1|||||||O");    //|||||
				// Order.append(line6);
				Order.append(cr);

				buf_order.append(Order);
				Order.delete(0, Order.length());

				 System.out.println("buf_order= " + buf_order.toString());

				// --------------------------------------------
				 System.out.println("STRING LENGTH BEFORE FRAMING buf_order= " +
				 buf_order.length());

				// STRING LENGTH BEFORE FRAMING = 235
				int frameCount = 0, first = 0, mid = 0, remain = 0;
				frameCount = FrameCounter(buf_order.length());
				// System.out.println("Char At 0 "+buf.charAt(0));
				int length = buf_order.length();
				// frameList3.clear();
				for (int i = 1; i <= frameCount; i++) {
					if (Server.StxCounter == 8)
						Server.StxCounter = 0;
					if (length < 240)
						// if (length < 306)
						mid = mid + length;
					else if (length == 240)
						mid = mid + length;
					else if (length > 240) {
						mid = mid + 240;
					}
					 System.out.println("First " + first + " " + "last " + mid + " stx " + stx);
					frame3.append(stx);
					Server.StxCounter = Server.StxCounter + 1;
					 System.out.println("Server.StxCounter " + Server.StxCounter);
					frame3.append(Server.StxCounter);
					 System.out.println("STX COUNTER:- " + Server.StxCounter);

					frame3.append(buf_order.substring(first, mid));

					if (i == frameCount) {
						frame3.append(etx);
						 System.out.println("before checksum" + frame3.toString());
						frame3.append(CheckSum(frame3.toString())); // FRAME WITH STX STX COUNTER AND ETX
						// frame.append(CheckSum(buf.substring(first, mid))); //FRAME WITH ONLY
						// CHARACTERS
						frame3.append(cr);
						frame3.append(lf);
					} else

					{
						frame3.append(etb);
						 System.out.println("before checksum" + frame3.toString());
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
				// -------------------------------------
				// ==============================================================

				while (count_ack_BA400_s != 0) {
					if (Server.StxCounter == 8)
						Server.StxCounter = 0;
					p_packet_count = p_packet_count + 1;
					 System.out.println("Sample_code.get(0).toString()---"+Sample_code.get(0).toString());
					count_ack_BA400_s = count_ack_BA400_s - 1;
					if (count_ack_BA400_s == 0) {
						Sample_code.clear();
						p_packet_count = 1;
						testCode.clear();

						break;

					}
					Sample_code.remove(sam_count);
					sam_count = 0;
					parseSelect2();
					p.append("P|");
					p.append(p_packet_count);
					p.append("||");
					p.append(Sample_code.get(0).toString().replace("^", ""));
					p.append("||^");
					p.append(name_pat.trim());
					// p.append("|||");
					p.append(cr);
					buf_patient.append(p);
					p.delete(0, p.length());

					Server.StxCounter = Server.StxCounter + 1;

					frame2.append(stx);
					frame2.append(Server.StxCounter);
					frame2.append(buf_patient);
					buf_patient.delete(0, buf_patient.length());
					frame2.append(etx);
					frame2.append(CheckSum(frame2.toString())); // FRAME WITH STX STX COUNTER AND ETB
					frame2.append(cr);
					frame2.append(lf);
					frameList.add(frame2.toString());
					frame2.delete(0, frame2.length());
					 System.out.println("frameList= " + frameList.toString());

					// ---------------------------------

					// if (testCode_mapList.size() > 1 && testCode_mapList != null)
					// {
					Order.append("O|1");
					// Order.append();
					Order.append("|");
					Order.append(Server.Sample_code.get(0).toString().replace("^", ""));
					Order.append("||^^^1.0000+");
					for (int k = 0; k < Server.testCode.size(); k++) {
						Order.append(Server.testCode_mapList.get(k));
						Order.append("+1.0");
						if (k != Server.testCode.size() - 1) {

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
					Order.append("||||N||||5|||||||");
					Order.append(line6);
					Order.append(cr);

					buf_order.append(Order);
					Order.delete(0, Order.length());

					 System.out.println("buf_order= " + buf_order.toString());

					// --------------------------------------------
					// System.out.println("STRING LENGTH BEFORE FRAMING buf_order= " +
					// buf_order.length());

					// STRING LENGTH BEFORE FRAMING = 235
					int frameCount1 = 0, first1 = 0, mid1 = 0, remain1 = 0;
					frameCount1 = FrameCounter(buf_order.length());
					// System.out.println("Char At 0 "+buf.charAt(0));
					int length1 = buf_order.length();

					for (int i = 1; i <= frameCount1; i++) {
						if (length1 < 240)
							// if (length < 306)
							mid1 = mid1 + length1;
						else if (length1 == 240)
							mid1 = mid1 + length1;
						else if (length1 > 240) {
							mid1 = mid1 + 240;
						}
						// System.out.println("First1 " + first1 + " " + "last1 " + mid1 + " stx1 " +
						// stx);
						frame3.append(stx);
						Server.StxCounter = Server.StxCounter + 1;
						// System.out.println("Server.StxCounter " + Server.StxCounter);
						frame3.append(Server.StxCounter);
						// System.out.println("STX COUNTER:- " + Server.StxCounter);

						frame3.append(buf_order.substring(first1, mid1));

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
						length1 = length1 - 240;
						first1 = mid1;

						frameList.add(frame3.toString());
						/*
						 * if(i ==1) frame1.append(frame); else frame2.append(frame);
						 */
						frame3.delete(0, frame3.length());

					}

					// -------------------------------------

					// ===============================

					// }

				}
				// =================================================

				// -------------------------------------------------------------
//
//				BA400_client.StxCounter = BA400_client.StxCounter + 1;
//				frameList3.clear();
//				frame3.append(stx);
//				frame3.append(BA400_client.StxCounter);
//				frame3.append(buf_order);
//				buf_order.delete(0, buf_order.length());
//				frame3.append(etb);
//				frame3.append(CheckSum(frame3.toString())); // FRAME WITH STX STX COUNTER AND ETB
//				frame3.append(cr);
//				frame3.append(lf);
//				frameList3.add(frame3.toString());
//				frame3.delete(0, frame3.length());

				 System.out.println("frameList3= " + frameList.toString());

				// ==============================================================

				// =================================================

				// ---------SECOND ORDER
				Order1.append("|");
				Order1.append(Server.sampleNo.toString().replace("^", ""));
				// Order.append("^50087^1^^S1^SC^|");
				Order1.append("||^^^MTBRIF|R|");
				Order1.append(current_time_str);
				Order1.append("");
				// -----------------------------
				/*
				 * if(Server.testCode!=null) { System.out.println("TEST-CODE LIST IS NOT NULL");
				 * for(int k=0;k<Server.testCode.size();k++)
				 * 
				 * { Order.append("^^^"); Order.append(Server.testCode.get(k));
				 * Order1.append("^^^"); Order1.append(Server.testCode.get(k));
				 * if(k!=Server.testCode.size()-1) { Order.append("^\\"); Order1.append("^\\");
				 * } else { Order.append("^"); Order1.append("^"); } } }
				 */
				/*
				 * else System.out.println("NO TEST CODE FOUND !!! TEST-CODE LIST IS NULL");
				 */
				// Order.append(cr);
				// buf.append(Order);

				// buf.append("");
				// buf.append("20181227190037"+"|"+"20181227190037");
				// buf.append("|");
				// buf.append(line6);

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
				// buf.append(cr);
				// buf.append(line3);

				// buf.append(cr);

				line4.append(cr);

				buf.append(line4);
				buf_termination.append(line4);

				break;
			}

			// ========================================

			Server.StxCounter = Server.StxCounter + 1;
			// frameList4.clear();
			frame4.append(stx);
			frame4.append(Server.StxCounter);
			frame4.append(buf_termination);
			buf_termination.delete(0, buf_termination.length());
			frame4.append(etx);
			frame4.append(CheckSum(frame4.toString())); // FRAME WITH STX STX COUNTER AND ETB
			frame4.append(cr);
			frame4.append(lf);

			frameList.add(frame4.toString());
			frame4.delete(0, frame4.length());

			 System.out.println("frameList= " + frameList.toString());
			// =======================================

			// CODE TO DELETE LF IF EXISTS IN STRING *** STARTS ***
			/*
			 * for(int i=0;i<buf.length();i++) { if(buf.charAt(i)!=10)
			 * faltu.append(buf.charAt(i)); else System.out.println("yesssss"); }
			 * buf.delete(0, buf.length()); buf = faltu;
			 */
			// CODE TO DELETE LF IF EXISTS IN STRING *** ENDS ***

//			System.out.println("STRING LENGTH BEFORE FRAMING = " + buf.length());
//			System.out.println("frame BEFORE FRAMING = " + buf.toString());
//			// STRING LENGTH BEFORE FRAMING = 235
//			int frameCount = 0, first = 0, mid = 0, remain = 0;
//			frameCount = FrameCounter(buf.length());
//			// System.out.println("Char At 0 "+buf.charAt(0));
//			int length = buf.length();
//
//			for (int i = 1; i <= frameCount; i++) {
//				if (length < 240)
//					// if (length < 306)
//					mid = mid + length;
//				else if (length == 240)
//					mid = mid + length;
//				else if (length > 240) {
//					mid = mid + 240;
//				}
//				System.out.println("First " + first + "  " + "last " + mid + " stx  " + stx);
//				frame.append(stx);
//				System.out.println("Server.StxCounter " + Server.StxCounter);
//				frame.append(Server.StxCounter);
//				System.out.println("STX COUNTER:- " + Server.StxCounter);
//				Server.StxCounter++;
//				frame.append(buf.substring(first, mid));
//
//				if (i == frameCount) {
//					frame.append(etx);
//					System.out.println("before checksum" + frame.toString());
//					frame.append(CheckSum(frame.toString())); // FRAME WITH STX STX COUNTER AND ETX
//					// frame.append(CheckSum(buf.substring(first, mid))); //FRAME WITH ONLY
//					// CHARACTERS
//					frame.append(cr);
//					frame.append(lf);
//				} else
//
//				{
//					frame.append(etb);
//					System.out.println("before checksum" + frame.toString());
//					frame.append(CheckSum(frame.toString())); // FRAME WITH STX STX COUNTER AND ETB
//					// frame.append(CheckSum(buf.substring(first, mid)));
//					frame.append(cr);
//					frame.append(lf);
//				}
//				// length = length - mid;
//				length = length - 240;
//				first = mid;
//
//				response.append(frame);
//				frameList.add(frame.toString());
//				/*
//				 * if(i ==1) frame1.append(frame); else frame2.append(frame);
//				 */
//				frame.delete(0, frame.length());
//
//			}
//			// response.append(eot);
//			// frameList.add(eot);
//			Server.StxCounter = 1;
//			newS.delete(0, newS.length());
			 System.out.println("Final String after checksum" + frameList.size());
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
	// ------------------------------------------------------------

	// Header parsing and Reply Packet Generation Method//========== vitrose5600
	// PACKET Format id=20038
	public static List headerParsing_vitrose56001_backup(String line) {
		SimpleDateFormat time_formatter2 = new SimpleDateFormat("yyyyMMddHHmmss");
		String current_time_str2 = time_formatter2.format(System.currentTimeMillis());
		System.out.println("current_time_str2---" + current_time_str2);

		Server.StxCounter = 1;
		try {
			Thread.sleep(2000);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
		SimpleDateFormat time_formatter3 = new SimpleDateFormat("yyyyMMddHHmmss");
		String current_time_str3 = time_formatter3.format(System.currentTimeMillis());
		System.out.println("current_time_str2 after 1 second wait---" + current_time_str3);
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

		StringBuffer buf_header = new StringBuffer();
		StringBuffer buf_patient = new StringBuffer();
		StringBuffer buf_order = new StringBuffer();
		StringBuffer buf_order_1 = new StringBuffer();
		StringBuffer buf_termination = new StringBuffer();

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

		StringBuffer header = new StringBuffer("H|\\^&|");
//		header.append(Query_Message_Id);
		header.append("||");
		header.append("qnxa224");
		// header.append(Query_Message_receiver);
		header.append("|||||");
//		header.append(Query_Message_sender);
		header.append("|||LIS2-A|");
		header = header.append(Query_Message_date_time);
//	    header = header.append(current_time_str);
//		StringBuffer line3 = new StringBuffer("C|1|L|^^^^|G");
		StringBuffer line4 = new StringBuffer("L|1|N");
		// StringBuffer line6 = new StringBuffer("||||P||||ORH||||||||||Q");
		// StringBuffer line6 = new StringBuffer("||||A||||ORH||||||||||Q");
		StringBuffer line6 = new StringBuffer("|||O");
		StringBuffer line5 = new StringBuffer();
		char etb = 23;
		char eot = '';
		char etx = 3;
		counterPID_Packet = counterPID_Packet + 33;
		// P|1||IND-000166864033|||||
		// StringBuffer p = new StringBuffer("P|1||IND-"+counterPID_Packet+"||^|||");
		// StringBuffer p = new StringBuffer("P|1||||^|||");
		// StringBuffer p = new StringBuffer("P|1");
		// StringBuffer p = new StringBuffer("P|1|U000856|||^");
		StringBuffer p = new StringBuffer("P|");
		p.append(p_packet_count);
		p.append("|");
		// p.append(Server.sampleNo.toString().replace("^", ""));
		p.append(Server.Sample_code.get(0).toString().replace("^", ""));
		p.append("|||");
		// p.append("RR^ABIGAIL^G||19780407|F||843 TALL OAKSDR^HAILVILLE, MD
		// 45831|||RASHAMDRA^SANJAY^V|S|||||||||||U7");

		p.append(name_pat.trim());
		// p.append("^name^M");
//		p.append("^ab");
		// p.append("^|||M||");
//	    p.append("^|||M||");
		// p.append("|||||");
		// StringBuffer p = new StringBuffer("P|1||||^|||U||||||"); // P|1|||||||M
		StringBuffer c = new StringBuffer("C|1|I|Patient is complaining of shortness of breath and chest pain.|G");

		// StringBuffer Order = new StringBuffer("O|1||");
		StringBuffer Order = new StringBuffer();
		StringBuffer Order1 = new StringBuffer("O|2");
		List list = new ArrayList();
		StringBuffer frame = new StringBuffer();
		StringBuffer framevip = new StringBuffer();
		StringBuffer response = new StringBuffer();
		StringBuffer time = new StringBuffer();

		try {

			Server.cobas = cobas;
			Server.host = host;
			System.out.println("TIME:- " + time);
			// mode.replace(2, 5, "DWN"); //Replace TSREQ TO TSDWN
			/*
			 * if(=="TSREQ") { System.out.println("in tsreq");
			 * 
			 * }
			 */
			Server.stx = stx;
			Server.mode = mode;
			// MAKING STRING FOR REPLYING TO CLIENT MACHINE
			// SimpleDateFormat time_formatter1 = new SimpleDateFormat("yyyyMMddHHmmss");
			// String current_time_str1 =
			// time_formatter1.format(System.currentTimeMillis());

			while (true) {

				buf_header.append(header);
				buf_header.append(cr);
				buf_patient.append(p);
				buf_patient.append(cr);
				p.delete(0, p.length());
				// System.out.println("BA400_client.testCode.size()------- " +
				// Server.testCode.size());
				// System.out.println("BA400_client.testCode.size(------- " +
				// BA400_client.testCode.toString().replace(null, " ").trim());

				// ---------------------------------
				/*
				 * if (BA400_client.testCode!=null &&BA400_client.testCode.size() ==1 ) {
				 * System.out.println("TEST-CODE LIST IS NOT NULL"); for (int k = 0; k <
				 * BA400_client.testCode.size(); k++) { Order.append("O|"); Order.append(k + 1);
				 * Order.append("|"); Order.append(BA400_client.sampleNo.toString().replace("^",
				 * "")); Order.append("||^^^");
				 * Order.append(BA400_client.testCode.get(k).toString().replace(";", ""));
				 * Order.append("|R|"); Order.append("|"); Order.append("||||A||||SER|||||||");
				 * Order.append(line6); Order.append(cr); buf.append(Order); Order.delete(0,
				 * Order.length());
				 * 
				 * 
				 * System.out.println("buf= " + buf.toString()); } } else if
				 * (BA400_client.testCode.size() >1 && BA400_client.testCode!=null) {
				 * System.out.println("TEST-CODE LIST IS NOT NULL"); for (int k = 0; k <
				 * BA400_client.testCode.size(); k++) {
				 * 
				 * Order.append("O|"); Order.append(k + 1); Order.append("|");
				 * Order.append(BA400_client.sampleNo.toString().replace("^", ""));
				 * Order.append("||^^^");
				 * Order.append(BA400_client.testCode.get(k).toString().replace(";", ""));
				 * Order.append("|R|"); Order.append("|"); Order.append("||||A||||SER|||||||");
				 * Order.append(line6); Order.append(cr); buf.append(Order); Order.delete(0,
				 * Order.length()); //
				 * 
				 * // System.out.println("frameList3= " + frameList3.toString()); //
				 * BA400_client.StxCounter = BA400_client.StxCounter + 1; }
				 * 
				 * }
				 */
				// ======================================================
				frameList.clear();
				frame1.append(stx);
				frame1.append(Server.StxCounter);
				frame1.append(buf_header);
				buf_header.delete(0, buf_header.length());
				frame1.append(etx);
				frame1.append(CheckSum(frame1.toString())); // FRAME WITH STX STX COUNTER AND ETB
				frame1.append(cr);
				frame1.append(lf);
				frameList.add(frame1.toString());
				frame1.delete(0, frame1.length());
//				System.out.println("frameList= " + frameList.toString());

				Server.StxCounter = Server.StxCounter + 1;
				// frameList2.clear();
				frame2.append(stx);
				frame2.append(Server.StxCounter);
				frame2.append(buf_patient);
				buf_patient.delete(0, buf_patient.length());
				frame2.append(etx);
				frame2.append(CheckSum(frame2.toString())); // FRAME WITH STX STX COUNTER AND ETB
				frame2.append(cr);
				frame2.append(lf);
				frameList.add(frame2.toString());
				frame2.delete(0, frame2.length());
//				System.out.println("frameList= " + frameList.toString());

				// ====================================================================
				Order.append("O|1");
				// Order.append();
				Order.append("|");
				Order.append(Server.Sample_code.get(0).toString().replace("^", ""));
				Order.append("||^^^1.0000+");
				for (int k = 0; k < Server.testCode_mapList.size(); k++)

				{
					Order.append(Server.testCode_mapList.get(k));
					Order.append("+1.0");
					if (k != Server.testCode_mapList.size() - 1) {

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
				Order.append("||||N||||5|||||||");
				Order.append(line6);
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
					if (Server.StxCounter == 8)
						Server.StxCounter = 0;
					if (length < 240)
						// if (length < 306)
						mid = mid + length;
					else if (length == 240)
						mid = mid + length;
					else if (length > 240) {
						mid = mid + 240;
					}
					// System.out.println("First " + first + " " + "last " + mid + " stx " + stx);
					frame3.append(stx);
					Server.StxCounter = Server.StxCounter + 1;
					// System.out.println("Server.StxCounter " + Server.StxCounter);
					frame3.append(Server.StxCounter);
					// System.out.println("STX COUNTER:- " + Server.StxCounter);

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
				// -------------------------------------
				// ==============================================================

				while (count_ack_BA400_s != 0) {
					if (Server.StxCounter == 8)
						Server.StxCounter = 0;
					p_packet_count = p_packet_count + 1;
					// System.out.println("Sample_code.get(0).toString()---"+Sample_code.get(0).toString());
					count_ack_BA400_s = count_ack_BA400_s - 1;
					if (count_ack_BA400_s == 0) {
						Sample_code.clear();
						p_packet_count = 1;
						testCode.clear();

						break;

					}
					Sample_code.remove(sam_count);
					sam_count = 0;
					parseSelect2();
					p.append("P|");
					p.append(p_packet_count);
					p.append("||");
					p.append(Sample_code.get(0).toString().replace("^", ""));
					p.append("||^");
					p.append(name_pat.trim());
					// p.append("|||");
					p.append(cr);
					buf_patient.append(p);
					p.delete(0, p.length());

					Server.StxCounter = Server.StxCounter + 1;

					frame2.append(stx);
					frame2.append(Server.StxCounter);
					frame2.append(buf_patient);
					buf_patient.delete(0, buf_patient.length());
					frame2.append(etx);
					frame2.append(CheckSum(frame2.toString())); // FRAME WITH STX STX COUNTER AND ETB
					frame2.append(cr);
					frame2.append(lf);
					frameList.add(frame2.toString());
					frame2.delete(0, frame2.length());
					// System.out.println("frameList= " + frameList.toString());

					// ---------------------------------

					// if (testCode_mapList.size() > 1 && testCode_mapList != null)
					// {
					Order.append("O|1");
					// Order.append();
					Order.append("|");
					Order.append(Server.Sample_code.get(0).toString().replace("^", ""));
					Order.append("||^^^1.0000+");
					for (int k = 0; k < Server.testCode_mapList.size(); k++) {
						Order.append(Server.testCode_mapList.get(k));
						Order.append("+1.0");
						if (k != Server.testCode_mapList.size() - 1) {

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
					Order.append("||||N||||5|||||||");
					Order.append(line6);
					Order.append(cr);

					buf_order.append(Order);
					Order.delete(0, Order.length());

					// System.out.println("buf_order= " + buf_order.toString());

					// --------------------------------------------
					// System.out.println("STRING LENGTH BEFORE FRAMING buf_order= " +
					// buf_order.length());

					// STRING LENGTH BEFORE FRAMING = 235
					int frameCount1 = 0, first1 = 0, mid1 = 0, remain1 = 0;
					frameCount1 = FrameCounter(buf_order.length());
					// System.out.println("Char At 0 "+buf.charAt(0));
					int length1 = buf_order.length();

					for (int i = 1; i <= frameCount1; i++) {
						if (length1 < 240)
							// if (length < 306)
							mid1 = mid1 + length1;
						else if (length1 == 240)
							mid1 = mid1 + length1;
						else if (length1 > 240) {
							mid1 = mid1 + 240;
						}
						// System.out.println("First1 " + first1 + " " + "last1 " + mid1 + " stx1 " +
						// stx);
						frame3.append(stx);
						Server.StxCounter = Server.StxCounter + 1;
						// System.out.println("Server.StxCounter " + Server.StxCounter);
						frame3.append(Server.StxCounter);
						// System.out.println("STX COUNTER:- " + Server.StxCounter);

						frame3.append(buf_order.substring(first1, mid1));

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
						length1 = length1 - 240;
						first1 = mid1;

						frameList.add(frame3.toString());
						/*
						 * if(i ==1) frame1.append(frame); else frame2.append(frame);
						 */
						frame3.delete(0, frame3.length());

					}

					// -------------------------------------

					// ===============================

					// }

				}
				// =================================================

				// -------------------------------------------------------------
//
//				BA400_client.StxCounter = BA400_client.StxCounter + 1;
//				frameList3.clear();
//				frame3.append(stx);
//				frame3.append(BA400_client.StxCounter);
//				frame3.append(buf_order);
//				buf_order.delete(0, buf_order.length());
//				frame3.append(etb);
//				frame3.append(CheckSum(frame3.toString())); // FRAME WITH STX STX COUNTER AND ETB
//				frame3.append(cr);
//				frame3.append(lf);
//				frameList3.add(frame3.toString());
//				frame3.delete(0, frame3.length());

				// System.out.println("frameList3= " + frameList.toString());

				// ==============================================================

				// =================================================

				// ---------SECOND ORDER
				Order1.append("|");
				Order1.append(Server.sampleNo.toString().replace("^", ""));
				// Order.append("^50087^1^^S1^SC^|");
				Order1.append("||^^^MTBRIF|R|");
				Order1.append(current_time_str);
				Order1.append("");
				// -----------------------------
				/*
				 * if(Server.testCode!=null) { System.out.println("TEST-CODE LIST IS NOT NULL");
				 * for(int k=0;k<Server.testCode.size();k++)
				 * 
				 * { Order.append("^^^"); Order.append(Server.testCode.get(k));
				 * Order1.append("^^^"); Order1.append(Server.testCode.get(k));
				 * if(k!=Server.testCode.size()-1) { Order.append("^\\"); Order1.append("^\\");
				 * } else { Order.append("^"); Order1.append("^"); } } }
				 */
				/*
				 * else System.out.println("NO TEST CODE FOUND !!! TEST-CODE LIST IS NULL");
				 */
				// Order.append(cr);
				// buf.append(Order);

				// buf.append("");
				// buf.append("20181227190037"+"|"+"20181227190037");
				// buf.append("|");
				// buf.append(line6);

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
				// buf.append(cr);
				// buf.append(line3);

				// buf.append(cr);

				line4.append(cr);

				buf.append(line4);
				buf_termination.append(line4);

				break;
			}

			// ========================================

			Server.StxCounter = Server.StxCounter + 1;
			// frameList4.clear();
			frame4.append(stx);
			frame4.append(Server.StxCounter);
			frame4.append(buf_termination);
			buf_termination.delete(0, buf_termination.length());
			frame4.append(etx);
			frame4.append(CheckSum(frame4.toString())); // FRAME WITH STX STX COUNTER AND ETB
			frame4.append(cr);
			frame4.append(lf);

			frameList.add(frame4.toString());
			frame4.delete(0, frame4.length());

			// System.out.println("frameList= " + frameList.toString());
			// =======================================

			// CODE TO DELETE LF IF EXISTS IN STRING *** STARTS ***
			/*
			 * for(int i=0;i<buf.length();i++) { if(buf.charAt(i)!=10)
			 * faltu.append(buf.charAt(i)); else System.out.println("yesssss"); }
			 * buf.delete(0, buf.length()); buf = faltu;
			 */
			// CODE TO DELETE LF IF EXISTS IN STRING *** ENDS ***

//			System.out.println("STRING LENGTH BEFORE FRAMING = " + buf.length());
//			System.out.println("frame BEFORE FRAMING = " + buf.toString());
//			// STRING LENGTH BEFORE FRAMING = 235
//			int frameCount = 0, first = 0, mid = 0, remain = 0;
//			frameCount = FrameCounter(buf.length());
//			// System.out.println("Char At 0 "+buf.charAt(0));
//			int length = buf.length();
//
//			for (int i = 1; i <= frameCount; i++) {
//				if (length < 240)
//					// if (length < 306)
//					mid = mid + length;
//				else if (length == 240)
//					mid = mid + length;
//				else if (length > 240) {
//					mid = mid + 240;
//				}
//				System.out.println("First " + first + "  " + "last " + mid + " stx  " + stx);
//				frame.append(stx);
//				System.out.println("Server.StxCounter " + Server.StxCounter);
//				frame.append(Server.StxCounter);
//				System.out.println("STX COUNTER:- " + Server.StxCounter);
//				Server.StxCounter++;
//				frame.append(buf.substring(first, mid));
//
//				if (i == frameCount) {
//					frame.append(etx);
//					System.out.println("before checksum" + frame.toString());
//					frame.append(CheckSum(frame.toString())); // FRAME WITH STX STX COUNTER AND ETX
//					// frame.append(CheckSum(buf.substring(first, mid))); //FRAME WITH ONLY
//					// CHARACTERS
//					frame.append(cr);
//					frame.append(lf);
//				} else
//
//				{
//					frame.append(etb);
//					System.out.println("before checksum" + frame.toString());
//					frame.append(CheckSum(frame.toString())); // FRAME WITH STX STX COUNTER AND ETB
//					// frame.append(CheckSum(buf.substring(first, mid)));
//					frame.append(cr);
//					frame.append(lf);
//				}
//				// length = length - mid;
//				length = length - 240;
//				first = mid;
//
//				response.append(frame);
//				frameList.add(frame.toString());
//				/*
//				 * if(i ==1) frame1.append(frame); else frame2.append(frame);
//				 */
//				frame.delete(0, frame.length());
//
//			}
//			// response.append(eot);
//			// frameList.add(eot);
//			Server.StxCounter = 1;
//			newS.delete(0, newS.length());
			// System.out.println("Final String after checksum" + frameList.size());
			for (int i = 0; i < frameList.size(); i++) {
				count_ack_BA400_o1++;
			}
			// System.out.println("count_ack_BA400_o1------- " + count_ack_BA400_o1);
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



	// -----------------------------------------------

	// BA400_parseSelect2
	public static void parseSelect2() {
		StringBuffer packet = new StringBuffer();
		List<StringBuffer> frameList = new ArrayList<>();
		List testCode = new ArrayList();

		int count = 0;
		StringBuffer bf = new StringBuffer();
		StringBuffer bf1 = new StringBuffer();

		int count_header = 0;
		StringBuffer bf_header = new StringBuffer();
		StringBuffer bf_header_sender = new StringBuffer();
		StringBuffer bf_header_receiver = new StringBuffer();
		Server.testCode.clear();
		testCode_mapList.clear();

		Map<String, String> testCodeMap = new LinkedHashMap<String, String>();

		testCodeMap.put("Amyl", "306");
		testCodeMap.put("Urea", "315");
		testCodeMap.put("Alb", "303");
		testCodeMap.put("Glu", "300");
		testCodeMap.put("Glu", "300");
		testCodeMap.put("Crea", "314");
		testCodeMap.put("Tp", "301");
		testCodeMap.put("Glob", "910");
		testCodeMap.put("Chol", "305");
		testCodeMap.put("Trig", "304");

		testCodeMap.put("Dhdl", "356");
		testCodeMap.put("Vldl", "912");
		testCodeMap.put("Uric", "302");
		testCodeMap.put("Tbil", "319");
		testCodeMap.put("Bc", "327");
		testCodeMap.put("Ast", "320");
		testCodeMap.put("Altv", "357");
		testCodeMap.put("Alkp", "321");
		testCodeMap.put("K+", "308");
		testCodeMap.put("Lipa", "325");

		testCodeMap.put("Phos", "311");
		testCodeMap.put("Che", "337");
		testCodeMap.put("Ldh", "323");
		testCodeMap.put("Ckmb", "329");
		testCodeMap.put("Crp", "344");
		testCodeMap.put("Urea", "315");
		testCodeMap.put("Ldl", "916");
		testCodeMap.put("Glu", "300");
		testCodeMap.put("Ca", "318");
		testCodeMap.put("Na+", "309");

		testCodeMap.put("Bu", "317");
		testCodeMap.put("Glu", "300");
		testCodeMap.put("Ck", "324");
		testCodeMap.put("Alb", "303");

		// -------------------------------------------------

		String line2 = ABC.getSampleDtl_biolis50i(Sample_code.get(0).toString());

		int r = 0;
		String tc = "";

		String[] kvPairs = line2.split("#");
		for (String kvPair : kvPairs) {
			System.out.print("kvPairs =============------ " + kvPair);
			if (r == 0)
				tc = kvPair;
			if (r == 1)
				name_pat = kvPair;
			r++;
			if (r == 2)
				break;

		}

		// -----------
		String[] kv1 = tc.split(":");
		String[] kv2 = kv1[1].split(";");
		for (String z : kv2) {
			Server.testCode.add(z);
			System.out.print("Test_Code =============------ " + Server.testCode);
		}
		System.out.print("Test_Code =============------ " + Server.testCode);

		for (Map.Entry<String, String> entry : testCodeMap.entrySet()) {

			for (int i = 0; i < Server.testCode.size(); i++) {
				if (entry.getKey().equalsIgnoreCase(Server.testCode.get(i).toString()))
					testCode_mapList.add(entry.getValue().toString());
			}
		}
		System.out.print("testCode_mapList =============------ " + testCode_mapList);

		// ------------------------------------
//		testCode_mapList.add("GLUCOSE");
//		testCode_mapList.add("abc");
//		testCode_mapList.add("bcd");
//			BA400_client.testCode.add("GLUCOSE1");
//			BA400_client.testCode.add("abc1");
//		BA400_client.testCode.add("bcd1");
//		 BA400_client.testCode.add("GLUCOSE");
//		 BA400_client.testCode.add("abc");
//		 BA400_client.testCode.add("bcd");
//			BA400_client.testCode.add("GLUCOSE1");
//			BA400_client.testCode.add("abc1");
//		BA400_client.testCode.add("bcd1");
//		 BA400_client.testCode.add("GLUCOSE");
//		 BA400_client.testCode.add("abc");
//		 BA400_client.testCode.add("bcd");
//			BA400_client.testCode.add("GLUCOSE1");
//			BA400_client.testCode.add("abc1");
//		BA400_client.testCode.add("bcd1");
//		 BA400_client.testCode.add("GLUCOSE");
//		 BA400_client.testCode.add("abc");
//		 BA400_client.testCode.add("bcd");
//			BA400_client.testCode.add("GLUCOSE1");
//			BA400_client.testCode.add("abc1");
//		BA400_client.testCode.add("bcd1");
//		 BA400_client.testCode.add("GLUCOSE");
//		 BA400_client.testCode.add("abc");
//		 BA400_client.testCode.add("bcd");
//			BA400_client.testCode.add("GLUCOSE1");
//			BA400_client.testCode.add("abc1");
//		BA400_client.testCode.add("bcd1");

//		BA400_client.packet = packet;
//		// System.out.println("Packet "+Server.packet);
//		// Server.packet.append("741");
//		BA400_client.sampleNo = bf;
//		BA400_client.Query_Message_Id = bf_header;
//		BA400_client.Query_Message_sender = bf_header_sender;
//		BA400_client.Query_Message_receiver = bf_header_receiver;
		// Make Packet for Reply

		// System.out.println("PAcket:-----"+packet);
		// return frameList;
	}



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
			bf.append(Server.stx);
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
		System.out.println("Sum " + str + "    str" + string);
		if (str.length() == 1)
			return string.toString();
		else
			return str;
	}

	// ------------------------------------------------------------------

	// -------------------------------------------------------------------------
	public static void Vitrose_5600_odisha(List<String> list) // Vitrose_5600
	{
		Map<String, List> mp = new HashMap();
		List lis = new ArrayList();
		List lis1 = new ArrayList();
		String ip = "";
		StringBuffer sampleName = new StringBuffer();
		StringBuffer TestCode = null;
		StringBuffer testCode1 = null;
		StringBuffer TestValue;
		System.out.println("size:-- " + list.size());
		String stcode;
		String sTvalue;
		ABC abc = new ABC();
		Map<String, String> testCodeMap = new LinkedHashMap<String, String>();

//		testCodeMap.put("306", "Amyl");
//		testCodeMap.put("315", "Urea");
//		testCodeMap.put("303", "Alb");
//		testCodeMap.put("300", "Glu");
//		testCodeMap.put("300", "Glu");
//		testCodeMap.put("314", "Crea");
//		testCodeMap.put("301", "Tp");
//		testCodeMap.put("910", "Glob");
//		testCodeMap.put("305", "Chol");
//		testCodeMap.put("304", "Trig");
//
//		testCodeMap.put("356", "Dhdl");
//		testCodeMap.put("912", "Vldl");
//		testCodeMap.put("302", "Uric");
//		testCodeMap.put("319", "Tbil");
//		testCodeMap.put("327", "Bc");
//		testCodeMap.put("320", "Ast");
//		testCodeMap.put("357", "Altv");
//		testCodeMap.put("321", "Alkp");
//		testCodeMap.put("308", "K+");
//		testCodeMap.put("325", "Lipa");
//
//		testCodeMap.put("311", "Phos");
//		testCodeMap.put("337", "Che");
//		testCodeMap.put("323", "Ldh");
//		testCodeMap.put("329", "Ckmb");
//		testCodeMap.put("344", "Crp");
//		testCodeMap.put("315", "Urea");
//		testCodeMap.put("916", "Ldl");
//		testCodeMap.put("300", "Glu");
//		testCodeMap.put("318", "Ca");
//		testCodeMap.put("309", "Na+");
//
//		testCodeMap.put("317", "Bu");
//		testCodeMap.put("300", "Glu");
//		testCodeMap.put("324", "Ck");
//		testCodeMap.put("303", "Alb");
		testCodeMap.put("306", "AMYL");
		testCodeMap.put("315", "Urea");
		testCodeMap.put("303", "ALB");
		testCodeMap.put("300", "GLU");
		testCodeMap.put("300", "GLU");
		testCodeMap.put("314", "CREA");
		testCodeMap.put("301", "TP");
		testCodeMap.put("910", "GLOB");
		testCodeMap.put("305", "CHOL");
		testCodeMap.put("304", "TRIG");

		testCodeMap.put("356", "dHDL");
		testCodeMap.put("912", "VLDL");
		testCodeMap.put("302", "URIC");
		testCodeMap.put("319", "TBIL");
		testCodeMap.put("327", "Bc");
		testCodeMap.put("320", "AST");
		testCodeMap.put("357", "ALTV");
		testCodeMap.put("321", "ALKP");
		testCodeMap.put("308", "K+");
		testCodeMap.put("325", "LIPA");

		testCodeMap.put("311", "PHOS");
		testCodeMap.put("337", "CHE");
		testCodeMap.put("323", "LDH");
		testCodeMap.put("329", "CKMB");
		testCodeMap.put("344", "CRP");
		testCodeMap.put("315", "UREA");
		testCodeMap.put("916", "LDL");
		testCodeMap.put("300", "GLU");
		testCodeMap.put("318", "Ca");
		testCodeMap.put("309", "Na+");

		testCodeMap.put("317", "Bu");
		testCodeMap.put("300", "GLU");
		testCodeMap.put("324", "CK");
		testCodeMap.put("303", "ALB");
		int r = 0;
		for (String line : list) {
			r++;
			if (line.length() == 0 || line == null)
				break;
			TestCode = new StringBuffer();
			testCode1 = new StringBuffer();
			TestValue = new StringBuffer();

			System.out.println("line.length() " + line.length());
			if (line.length() > 2) {
				if (Character.toUpperCase(line.charAt(2)) == 'O') {
					sampleName.delete(0, sampleName.length());
					System.out.println(
							" Character.toUpperCase(line.charAt(2))========" + Character.toUpperCase(line.charAt(2)));

					char[] ch = line.toCharArray();
					int count = 0;
					int count_cr = 0;
					for (int i = 0; i < ch.length; i++) {

						if (ch[i] == '|')
							count++;

						if (count == 2 && (ch[i] == '^')) {
							count_cr++;

						}

						if (count == 2) {
							if (!(ch[i] == '^'))
								sampleName.append(ch[i]);
						}
						if (count_cr == 1)
							break;

					}

					System.out.println("SampleID:- " + sampleName.toString().replace("^", ""));

				}

				int countEX = 0;
				if (Character.toUpperCase(line.charAt(2)) == 'R') {
					System.out.println(
							" Character.toUpperCase(line.charAt(2))========" + Character.toUpperCase(line.charAt(2)));

					char[] ch = line.toCharArray();
					int count = 0;
					int count_plus = 0;
					for (int i = 0; i < ch.length; i++) {

						if (ch[i] == '|')
							count++;
						if (count == 2 && (ch[i] == '^')) {
							countEX++;
						}

						if (countEX == 3 && (ch[i] == '+')) {
							count_plus++;
							if (count_plus == 1)
								i++;
						}

						if (countEX == 3 && count_plus == 1) {
							if (!(ch[i] == '+'))

								TestCode.append(ch[i]);
						}

						if (count == 3) {

							if (!(ch[i] == '|'))
								TestValue.append(ch[i]);

						}

						if (count == 4) {
							break;
						}

					}

				}
				System.out.print("testCode =============------ " + TestCode);
				// -- ---------------------------------------
				if (TestCode.length() > 1) {
					for (Map.Entry<String, String> entry : testCodeMap.entrySet()) {

						if (entry.getKey().equals(TestCode.toString()))
							testCode1.append(entry.getValue());

					}
					System.out.print("testCode1 =============------ " + testCode1);
				}

				// --------------------------------------------

				String Tc = (testCode1.toString().replace("!", "")).trim();
				System.out.println("TestCode :- " + Tc);
				// System.out.println("TestCode :- "+TestCode);
				System.out.println("TestValue :- " + TestValue);
				if (!Tc.trim().equals("") && r > 1) {
					System.out.println("r value=" + r);
					System.out.println("TestCode" + Tc);
					abc.insert_SysmexXN350(Tc, (TestValue.toString().replace("|", "")).trim(),    
							sampleName.toString().replace("|", "").trim());
					TestCode.delete(0, TestCode.length());
					TestCode.delete(0, testCode1.length());
					TestCode.delete(0, TestValue.length());       //replaceAll("[^0-9.]", "")
				}
			}
		}

	}



//---------------------------------------------------------------------



	public static void parse_AIIMSRB(List<String> list) // //18/10/2022
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

			System.out.println("line.length() " + line.length());
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

						if (count == 2) {
							if (!(ch[i] == '|'))
								sampleName.append(ch[i]);
						}
						if (count == 3)
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
					int count_cr = 0;
					for (int i = 0; i < ch.length; i++) {

						if (ch[i] == '|')
							count++;

					

						if (count == 2 && (ch[i] == '^')) {
							countAs++;
						}
						if (count == 3 && (ch[i] == '^')) {
							count_cr++;
						}
						if (countAs == 3) {

							if (!(ch[i] == '^'))
								TestCode.append(ch[i]);
						}

						if (count == 3) {
							if (!(ch[i] == '^'))
								TestValue.append(ch[i]);
						}

						
						if (count_cr == 1) 
							break;

					}
					System.out.println("TestCode :- " + TestCode);
					System.out.println("TestValue :- " + TestValue);

				}
				
			
			
				if (!TestCode.toString().trim().equals("") && r > 1 && !sampleName.toString().trim().equals("")) {

				

					System.out.println("r value=" + r);
			
					abc.insert_SysmexXN350(TestCode.toString().trim(), TestValue.toString().replace("|", "").trim(),
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

			System.out.println("line.length() " + line.length());
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

						if (countAs == 4) {

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
					abc.insert_SysmexXN350(Tc, TestValue.toString().replace("^", "").trim(),
							sampleName.toString().replace("^", "").trim());
				}
			}
		}

	}


	// -------------------------------------------------------
	// simens_atalika
	public static void parse_simens_atalika(List<String> list) {
		Map<String, List> mp = new HashMap();
		List lis = new ArrayList();
		List lis1 = new ArrayList();
		String ip = "";
		StringBuffer sampleName = new StringBuffer();
		StringBuffer TestCode;
		StringBuffer TestValue;

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

			System.out.println("line.length() " + line.length());
			if (line.length() > 2) {
				if (Character.toUpperCase(line.charAt(0)) == 'O') {
					sampleName.delete(0, sampleName.length());
					char[] ch = line.toCharArray();
					int count = 0;
					int count_tn = 0;
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

				// if(Test_Name1.equals("MTBRIF")) { parse(list); } else { continue; }

				if (Character.toUpperCase(line.charAt(0)) == 'R') {
					r++;
					int ct = 0;
					char[] ch = line.toCharArray();
					int count_pipe = 0;
					int count_carr = 0;
					for (int i = 0; i < ch.length; i++) {

						if (ch[i] == '|')
							count_pipe++;

						if (count_pipe == 2 && ch[i] == '^') {
							count_carr++;
							if (count_carr == 3)
								i++;

						}

						if (count_carr == 3 && !(ch[i] == '^')) {

							TestCode.append(ch[i]);
						}

						if (count_carr == 4)
							break;

					}

					System.out.println("Testcode==================:- " + TestCode.toString());

					// ----------------------------------------------

					int count1 = 0;
					for (int i = 0; i < ch.length; i++) {

						if (ch[i] == '|') {
							count1++;
							if (count1 == 3)
								i++;
						}
						if (count1 == 4)
							break;

						if (count1 == 3)
							TestValue.append(ch[i]);

					}
					System.out.println("Testvalue==============:- " + TestValue);

				}

				if (!TestCode.toString().trim().equals("") && r > 1) {

					abc.insert_SysmexXN350((TestCode.toString().replace("^", "")).trim(),
							TestValue.toString().replaceAll("[^0-9.]", "").trim(),
							sampleName.toString().replace("^", " ").replace("^", " ").trim());

				}
			}

		}

		// MachineData msh = new MachineData();
		// msh.save(mp,sampleName.toString(),ip);

	}

	// -------------------------------------------



	// ---------------------------------------------------------------------

	public static void mindray_MispaClinia(List<String> list) {   //mindray  MispaClinia
		Map<String, List> mp = new HashMap();
		List lis = new ArrayList();
		List lis1 = new ArrayList();
		String ip = "";
		StringBuffer sampleName = new StringBuffer();
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
				if (Character.toUpperCase(line.charAt(0)) == 'O') {
					char[] ch = line.toCharArray();
					int count = 0;
					int count_tn = 0;
					int count_or_cr= 0;
					for (int i = 0; i < ch.length; i++) {

						// if(ch[i]==94) // Ascii value for '^'
						// break;

						if (ch[i] == '|') {
							count++;
							if (count == 2)
								i++;
						}
						if (count == 2 && ch[i] == '^')
							count_or_cr++;

						if (count == 2 ) {
							if (!(ch[i] == '^'))
								sampleName.append(ch[i]);
						}

						if (count_or_cr == 1) {
							break;
						}
					}

					System.out.println("SampleName===========:- " + sampleName);// Sample ID Read between '|' and '^'

				}

				// if(Test_Name1.equals("MTBRIF")) { parse(list); } else { continue; }

				if (Character.toUpperCase(line.charAt(0)) == 'R') {
					r++;
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

						if (count_pipe == 3) {
							break;
						}

					}

					System.out.println("Testcode for common component==================:- " + TestCode.toString());
					// ----------------------------------------------

					int count = 0;
					int count1 = 0;
					for (int i = 0; i < ch.length; i++) {

						if (ch[i] == '|') {
							count1++;
							if (count1 == 3)
								i++;
						}
						if (ch[i] == '|' && count == 4)
							break;
						if (count1 == 3 && !(ch[i] == '^')) {

							TestValue.append(ch[i]);
							String str2 = TestValue.toString().replace("^", "");
							// to get a StringBuffer result:
							TestValue.delete(0, TestValue.length());
							TestValue.append(str2);

						}
					}
					System.out.println("Testvalue==============:- " + TestValue);

					// lis1.add(TestValue.toString());
				}

				if (!TestCode.toString().trim().equals("") && r > 1) {

					abc.insert_SysmexXN350(TestCode.toString().trim(), TestValue.toString().trim(),
							sampleName.toString().trim());

				}
			}

		}

		// MachineData msh = new MachineData();
		// msh.save(mp,sampleName.toString(),ip);

	}

	// ---------------------------------------------------------------------------------


	// ---------------------------------------------------------------------

	// ========================================================================================================================
}
