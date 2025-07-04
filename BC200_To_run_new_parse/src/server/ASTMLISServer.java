package server;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ASTMLISServer {


	private static final List<String> sampleIdsToProcess = new ArrayList<>();
	private static List<String> orderReposne = new ArrayList<>();

	static Map res = ReadPropertyFile.getPropertyValues();


	public static void main(String[] args) throws IOException {
		   int port =  Integer.parseInt((String)res.get("server_port"));
		ServerSocket serverSocket = new ServerSocket(port);
		System.out.println("LIS Server is running... PORT :"+port);

		while (true) {
			Socket clientSocket = serverSocket.accept();
			new Thread(() -> handleClient(clientSocket)).start();
		}
	}

	private static void handleClient(Socket socket) {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				OutputStream out = socket.getOutputStream()) {

			int ch;
			StringBuilder messageBuffer = new StringBuilder();
			boolean receivingMessage = false;

			while ((ch = in.read()) != -1) {
				char currentChar = (char) ch;

				if (currentChar == ASTMConstants.ENQ) {
					System.out.println("ENQ received. Starting new session.");
					System.out.println("RECV : "+currentChar);
					out.write(ASTMConstants.ACK);
					out.flush();
					receivingMessage = true;
					messageBuffer.setLength(0);
				} 
				//                else if (currentChar == ASTMConstants.STX && receivingMessage) {
				//                	//System.out.println("RECV : "+currentChar);
				//                    //messageBuffer.setLength(0); // Reset buffer for new message
				//                   // out.write(ASTMConstants.ACK);
				//                } 
				else if (currentChar == ASTMConstants.EOT ) {
					// Finish reading message; read checksum and CR LF
					//                    char[] tail = new char[4];
					//                    in.read(tail, 0, 4); // checksum + CR + LF

					String message = messageBuffer.toString();
					System.out.println("RECV : "+message);
					System.out.println("RECV : "+currentChar);
					// Check if it's a QUERY (contains Q| segment)


					String[] recvPkt=message.split("\r");

					String qrypkt= recvPkt[1].replace("\n", "");
					if (qrypkt.contains("Q|1")) {
						System.out.println("🔍 QUERY packet received. Processing immediately...");
						processQuery(message,out);


					}



					//  out.write(ASTMConstants.ACK);
					// out.flush();
				} 
				//                else if (currentChar == ASTMConstants.EOT) {
				//                    System.out.println("EOT received. Closing session.");
				//                    //receivingMessage = false;
				//                    System.out.println("RECV : "+currentChar);
				//                    
				//                }
				else if(currentChar == ASTMConstants.ACK && !orderReposne.isEmpty()) {

					System.out.println("ACK Recived : "+currentChar);
					for(String res :orderReposne) {
						out.write(res.getBytes());
						System.out.println("Sent : "+res);
					}
					orderReposne = new ArrayList<String>();

				}
				else if(currentChar == ASTMConstants.ACK && orderReposne.isEmpty()) {

					System.out.println("RECV : "+currentChar);
					out.write(ASTMConstants.ACK);
					out.flush();
					System.out.println("Sent : "+ASTMConstants.ACK);

				}
				else {
					messageBuffer.append(currentChar);
					// System.out.println("RECV : "+messageBuffer);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	private static void processQuery(String queryMessage, OutputStream out) throws IOException {
		String[] lines = queryMessage.split("\r");
	//	System.out.println("Query Request  : "+queryMessage);
		for (String line : lines) {
			line=	line.replace("\n", "");
			if (line.startsWith("Q|")) {
				String[] parts = line.split("\\|");

				if (parts.length > 2) {
					String sampleField = parts[2]; // Format: ^10591363-R

					String[] samples =sampleField.split("`");


					if(samples.length==1) {
						for(String sam:samples) {

							sam=sam.replaceAll("\\^", "");
							String sampleId = sampleField.replace("^", "").split("-")[0]; // Extract 10591363
							System.out.println("➡️  Query for Sample ID: " + sam);

							// Add to list for future tracking (optional)
							sampleIdsToProcess.add(sam);

							// Simulate fetching test code(s)
							String testCodes = getTestCodesForSample(sam);
							System.out.println("🔬 Test Codes: " + testCodes);




							String response=null;

							// Build and send ASTM-formatted response message
							response = buildASTMResponse(sampleId, testCodes);




							//    	                out.write(ASTMConstants.STX);
							//    	                out.write(response.getBytes());
							//    	                out.write(ASTMConstants.ETX);

							// Calculate checksum
							int checksum = 0;
							//   int checksum = 0;
							for (char c : response.toCharArray()) {
								if (c != ASTMConstants.STX) {  // skip STX character
									checksum += c;
								}
							}
							checksum += ASTMConstants.ETX;
							String checksumHex = String.format("%02X", checksum & 0xFF);

							// Append checksum, CR, LF
							response+=ASTMConstants.ETX;
							response+=checksumHex+ASTMConstants.CR+ASTMConstants.LF;
							/*
							 * out.write(ASTMConstants.STX); out.write(response.getBytes()); // out.write();
							 * 
							 * // out.write(checksumHex.getBytes()); out.write(ASTMConstants.CR);
							 * out.write(ASTMConstants.LF); out.flush();
							 */
							out.write(ASTMConstants.ENQ);
							//orderReposne.add(String.valueOf(ASTMConstants.ENQ));
							orderReposne.add(response);
							orderReposne.add(String.valueOf(ASTMConstants.EOT));


							//    System.out.println("✅ Response sent for sample " + sampleId);
							System.out.println("Order Response Packet Prepared :" +response);
						}

					}

					else {


						String response = buildASTMResponseMultiSample(sampleField, "");

						int checksum = 0;
						//   int checksum = 0;
						for (char c : response.toCharArray()) {
							if (c != ASTMConstants.STX) {  // skip STX character
								checksum += c;
							}
						}
						checksum += ASTMConstants.ETX;
						String checksumHex = String.format("%02X", checksum & 0xFF);

						// Append checksum, CR, LF
						response+=ASTMConstants.ETX;
						response+=checksumHex+ASTMConstants.CR+ASTMConstants.LF;
						/*
						 * out.write(ASTMConstants.STX); out.write(response.getBytes()); // out.write();
						 * 
						 * // out.write(checksumHex.getBytes()); out.write(ASTMConstants.CR);
						 * out.write(ASTMConstants.LF); out.flush();
						 */

						//orderReposne.add(String.valueOf(ASTMConstants.ENQ));
						orderReposne.add(response);
						orderReposne.add(String.valueOf(ASTMConstants.EOT));


						//    System.out.println("✅ Response sent for sample " + sampleId);
						System.out.println("Order Response Packet Prepared :" +response);



					}


				}
			}
		}

			out.write(ASTMConstants.ENQ);
			out.flush();
			System.out.println("Sent :" +ASTMConstants.ENQ);

	}


	private static String getTestCodesForSample(String sampleId) {


		return "";
	}

	public static String getCurrentTimestamp() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date());
	}
	private static String buildASTMResponse(String sampleId, String ignoredTestCodes) {
		String sampleIdFull = sampleId; // no "-R" if you want exact match

		ABC abcObj = new ABC();
		String orderresp = abcObj.getSampleDtl(sampleId);

		// Example fallback:
		//orderresp = "ALB;ALPU;BD;BID;BIT;BT;CHOL;CREEN;GOTH;GOTH2;GPTH2;HDLC;PRO;TRIG;UREA;URIC#Bikki Ramesh    ()#42 Yr#M#250609B037#SERUM#339232500462661#";

		System.out.println("Response HIS : "+orderresp);
		
		String[] parts = orderresp.split("#");

		// 1. Extract test codes (use backtick ` to separate)
		String[] testCodes = parts[0].split(";");
		StringBuilder testList = new StringBuilder();
		for (int i = 0; i < testCodes.length; i++) {
			testList.append("^^^").append(testCodes[i]);
			if (i != testCodes.length - 1) {
				testList.append("`");
			}
		}

		// 2. Extract patient and sample info
		String patientName = parts.length > 1 ? parts[1].trim().replace(" ", "^") : "UNKNOWN^PATIENT";
		String age = parts.length > 2 ? parts[2].trim() : "";
		String gender = parts.length > 3 ? parts[3].trim() : "";
		String patientId = parts.length > 4 ? parts[4].trim() : "";
		String sampleType = parts.length > 5 ? parts[5].trim() : "SERUM";
		String barcode = parts.length > 6 ? parts[6].trim() : "";





		String timestamp = getCurrentTimestamp();
		// ASTM-formatted response message
		return
				"1H|`^&||||||||||P|E 1394-97|"+timestamp+"\r" +
				"P|1|" + sampleIdFull+"|"+sampleIdFull+"|^|||U||||||||||||||||||||||||||\r"+ //+ "|" + sampleIdFull + "||" + patientName + "|||||||||||||||||||||||||||\r" +
				"O|1|" + sampleIdFull + "|" + sampleIdFull + "|" + testList +"|||||||N||||SERUM||||||||||O|||||\r" +
				"L|1|N\r";
	}


	private static String buildASTMResponseMultiSample(String sampleId, String ignoredTestCodes) {
		String sampleIdFull = sampleId; // no "-R" if you want exact match

		ABC abcObj = new ABC();
		// String orderresp = abcObj.getSampleDtl(sampleId);

		// Example fallback:
		String orderresp = "ALB;ALPU;BD;BID;BIT;BT;CHOL;CREEN;GOTH;GOTH2;GPTH2;HDLC;PRO;TRIG;UREA;URIC#Bikki Ramesh    ()#42 Yr#M#250609B037#SERUM#339232500462661#";

		String[] parts = orderresp.split("#");

		// 1. Extract test codes (use backtick ` to separate)
		String[] testCodes = parts[0].split(";");
		StringBuilder testList = new StringBuilder();
		for (int i = 0; i < testCodes.length; i++) {
			testList.append("^^^").append(testCodes[i]);
			if (i != testCodes.length - 1) {
				testList.append("`");
			}
		}

		// 2. Extract patient and sample info
		String patientName = parts.length > 1 ? parts[1].trim().replace(" ", "^") : "UNKNOWN^PATIENT";
		String age = parts.length > 2 ? parts[2].trim() : "";
		String gender = parts.length > 3 ? parts[3].trim() : "";
		String patientId = parts.length > 4 ? parts[4].trim() : "";
		String sampleType = parts.length > 5 ? parts[5].trim() : "SERUM";
		String barcode = parts.length > 6 ? parts[6].trim() : "";



		/* 1H|`^&||||||||||P|E 1394-97|20241003151617
    P|1|10010327`10010338`10010320`10010000
    O|1|10010327`10010338`10010320`10010000||^^^0^|R||20241003151617||||N||||SERUM||||||||||Q
    L|1|N
    10*/
		//

		String timestamp = getCurrentTimestamp();
		// ASTM-formatted response message
		return
				"1H|`^&||||||||||P|E 1394-97|"+timestamp+"\r" +
				"P|1|" + sampleIdFull +"\r" +
				"O|1|" + sampleIdFull + "||^^^0^|R||"+timestamp+"||||N||||SERUM||||||||||Q\r" +
				"L|1|N\r";
	}



	class ASTMConstants {
		public static final char STX = 02;
		public static final char ETX = 03;
		public static final char EOT = 04;
		public static final char ENQ = 05;
		public static final char ACK = 06;
		public static final char NAK = 15;
		public static final char CR  = 13;
		public static final char LF  = 10;
	}
}
