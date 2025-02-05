package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class ABC {
	public static String datetime = "";

	public static String pat_name = "";

	public static String pat_age = "";

	public static String pat_gender = "";

	public static String sample_id = "";

	public static String Test_Code = "";
	static List  testList= new ArrayList<>();
	public static String Test_Code_temp = "";

	public static String sample_type = "";

	static final String stx = "\002";

	static final String etb = "\027";

	static final String etx = "\003";

	static final String eot = "\004";
	static Path path1;
	static String currentDirectory;
	static String path = "";
	static {

		// OS_NAME = System.getProperties().getProperty("os.name");
		// ReadPropertyFile.windowsPath = "c:/TcpFiles/property";
		// ReadPropertyFile.linuxPath = "/opt/TcpFiles/property";
		// currentDirectory = System.getProperty("user.dir");
		// System.out.println("The current working directory is " + currentDirectory);
		// path1 = Paths.get(currentDirectory);

		// ====================
		Path currentRelativePath = Paths.get("");
		currentDirectory = currentRelativePath.toAbsolutePath().toString();
		System.out.println("Current absolute path is: " + currentDirectory);
		path = currentDirectory + "\\HIMS_log.txt";

	}

	public static String getSampleDtl(String samplecode) {
		System.out.println("Inside getSampleDtl (type2 query) ");
		List<String> list = new ArrayList<>();
		String return_value = "";
	//	Map res = ReadPropertyFile_old.getPropertyValues();
		Map res = ReadPropertyFile.getPropertyValues();
		final String ip = (String) res.get("ip");

		String eqp = (String) res.get("eqp");
		String hos = (String) res.get("hos");
		String uid = (String) res.get("uid");

		String formatid = (String) res.get("formatid");

		String line = null;
		String line1 = null;
		String line2 = null;

		 String url = "http://" + ip +
		 "/HISInvestigationG5/new_investigation/masters/MachineInterface.jsp?type=3&eqp="
		 + eqp + "&hos=" + hos + "&sam=" + samplecode.toString().trim() + "&uid=" + uid;
	//	String url = "https://" + ip + "/SupportServices/service/app/testdtl?hospcode=" + hos + "&samplecode="
	//			+ samplecode + "&eqpid=" + eqp;
		// String url =
		// "https://hmis.rcil.gov.in/SupportServices/service/app/Get_Hospital_Name_Code";
		// /SupportServices/service/app/testdtl?hospcode=33201&samplecode=0303DW001&eqpid=100001
		 System.out.println("\nBefore Sending URL : " + url); 
/*		try {
			SSLContext ssl_ctx = SSLContext.getInstance("TLS");
			TrustManager[] trust_mgr = get_trust_mgr();
			ssl_ctx.init(null, trust_mgr, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
		
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			con.setHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String host, SSLSession sess) {
					if (host.equals(ip))
						return true;
					return false;
				}
			});
			con.setRequestMethod("GET");
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.flush();
			wr.close();
			System.out.println("\nSending 'GET' request to URL : " + url);
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			StringBuffer response = new StringBuffer();
			String inputLine;
			while ((inputLine = in.readLine()) != null)
				response.append(inputLine);
			System.out.println("response " + response.toString());

			String str = response.toString();
			// System.out.println("String object: "+str);
			// in.close();

			// =====================================

			// String s=request.getParameter("id");
			// System.out.print(" serv "+s);
			// String str = reader.toString();
			// System.out.print(" serv--------- "+str);
			// System.out.print(" serv line1---------------- "+line1);

			line2 = ((((str.replace("{", "")).replace(":", "=")).replace("\"", "")).replace("}", ""));
			System.out.print("  new   line2---------------- " + line2);
			String[] kvPairs = line2.split("#");
			// System.out.print("kvPairs =============------ "+kvPairs);
			int r = 0;

		//	for (String kvPair : kvPairs) {
			//	r++;
		//		if (r == 2)
	//				break;
		//		String[] kv = kvPairs[0].split(";");   //Ramji ("=");
				// String key = kv[0];
				// String value = kv[1];
				// System.out.print(" kv[0] ---------------- "+kv[0]);
				// System.out.print(" kv[1] ---------------- "+kv[1]);
				// System.out.print("Test_Code bbbbbbbbb =============------ "+Test_Code);
				// Now do with key whatever you want with key and value...
			//RAmji	if (kv[0].equals("TestData")) 
				
					Test_Code = kvPairs[0];
					System.out.print("Test_Code =============------ " + Test_Code);
				
		//	}

			// System.out.println(" line2 Test_Code value ---------------- "+Test_Code);
			// System.out.println(" line2 input value ---------------- "+input);
			// System.out.println(" line2 output value ---------------- "+output);
			// System.out.println(" line2 hospitalCode value ----------------
			// "+hospitalCode);
			// System.out.println(" line2 userID value ---------------- "+userID);

			// ==========================================

//			String[] arrOfRes = line2.toString().split("#");
//			String[] arrOfRes1 = (arrOfRes[0]).split("=");
////			String Tes = arrOfRes1[0];
//			Test_Code_temp = arrOfRes1[1];
//			// System.out.print("Tes =============------ "+Tes);
//			System.out.print("Test_Code_temp =============------ " + Test_Code_temp);
			if (Test_Code == null)
				Test_Code = "";
			// Test_Code = Test_Code.replaceAll("\\s+", "^");

			//
			//  pat_name = arrOfRes[1];
			 // 
			 // pat_name = pat_name.replaceAll("\\s+", "^");
			 // 
			 // pat_age = arrOfRes[2]; pat_age = pat_age.trim(); pat_age =
			 // pat_age.replaceAll("\\s+", "^"); pat_gender = arrOfRes[3]; if
			 // (pat_gender.equals("M")) { pat_gender = "Male"; } else if
			 // (pat_gender.equals("F")) { pat_gender = "Female"; } sample_id = arrOfRes[4];
			 // 
			 // list.add(Test_Code); list.add(pat_name); list.add(pat_age);
			 // list.add(pat_gender); list.add(sample_id);
			 //
			return_value = Test_Code;
			// return_value = String.valueOf(Test_Code) + "#" + pat_name + "#" + pat_age +
			// "#" + pat_gender;
		} catch (Exception s) {
			s.printStackTrace();
		}
*/
		//--------------------Http Type=3-----------
			
			try {
				/*
				 * SSLContext ssl_ctx = SSLContext.getInstance("TLS"); TrustManager[] trust_mgr
				 * = get_trust_mgr(); ssl_ctx.init(null, // key manager trust_mgr, // trust
				 * manager new SecureRandom()); // random number generator
				 * HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
				 */
		//		System.out.println("\n RaMji URL1 Check ");
				URL obj = new URL(url);
		//		System.out.println("\n RaMji URL2 Check ");
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		//		System.out.println("\n RaMji URL3 Check ");
				/*
				 * ((HttpsURLConnection) con).setHostnameVerifier(new HostnameVerifier() {
				 * public boolean verify(String host, SSLSession sess) { if (host.equals(ip))
				 * return true; else return false; } });
				 */

				con.setRequestMethod("GET");
		//		System.out.println("\n RaMji URL4 Check ");
				con.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				wr.flush();
				wr.close();
				System.out.println("\n url Check "+wr);	
				//System.out.println("\nSending 'GET' request to URL : " + url);

				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		//		System.out.println("\n RaMji URL6 Check "+in);	

				System.out.println("responce :1+" + con.getResponseMessage());

				//---------------------
				StringBuffer response = new StringBuffer();
				String inputLine;
				while ((inputLine = in.readLine()) != null)
				{	response.append(inputLine);}

				System.out.println("response " + response.toString());
				String str = response.toString();
				// =====================================
				// String s=request.getParameter("id");
				// System.out.print(" serv "+s);
				// String str = reader.toString();
				// System.out.print(" serv--------- "+str);
				// System.out.print(" serv line1---------------- "+line1);
				// line2="10032;10120;10172";
					
				//line2 = ((((str.replace("{", "")).replace(":", "=")).replace("\"", "")).replace("}", ""));
				//System.out.println("  new   line2---------------- " + line2);
			
				
				String[] kvPairs = str.split("#");
				// System.out.println("kvPairs =============------ "+kvPairs[1]);
				int r = 0;

				//RAmji	if (kv[0].equals("TestData")) 
					
						Test_Code = kvPairs[0];
						System.out.println("Test_Code =============------ " + Test_Code);

				if (Test_Code == null)
					Test_Code = "";
				return_value = Test_Code;
				// return_value = String.valueOf(Test_Code) + "#" + pat_name + "#" + pat_age +
				// "#" + pat_gender;

			//	---------------------

			}
		 
/*		 
		 
		 try {
				
				System.out.println("\n RaMji URL1 ");
				URL obj = new URL(url);
				HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
				System.out.println("\n RaMji URL2 ");

				con.setRequestMethod("GET");
				con.setDoOutput(true);
				System.out.println("\n RaMji URL3 ");

				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				System.out.println("\n RaMji URL4 ");

				wr.flush();
				wr.close();
				System.out.println("\nSending 'GET' request to URL : " + url);
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				StringBuffer response = new StringBuffer();
				String inputLine;
				while ((inputLine = in.readLine()) != null)
					response.append(inputLine);
				System.out.println("response " + response.toString());
				String str = response.toString();
				// =====================================
				// String s=request.getParameter("id");
				// System.out.print(" serv "+s);
				// String str = reader.toString();
				// System.out.print(" serv--------- "+str);
				// System.out.print(" serv line1---------------- "+line1);

				line2 = ((((str.replace("{", "")).replace(":", "=")).replace("\"", "")).replace("}", ""));
				System.out.print("  new   line2---------------- " + line2);
				String[] kvPairs = line2.split("#");
				// System.out.print("kvPairs =============------ "+kvPairs);
				int r = 0;

				//RAmji	if (kv[0].equals("TestData")) 
					
						Test_Code = kvPairs[0];
						System.out.print("Test_Code =============------ " + Test_Code);

				if (Test_Code == null)
					Test_Code = "";
				return_value = Test_Code;
				// return_value = String.valueOf(Test_Code) + "#" + pat_name + "#" + pat_age +
				// "#" + pat_gender;
			} 
		 */
		 catch (Exception s) {
				s.printStackTrace();
			}	 
		return return_value;
	}

	
	
	

	public static String getSampleDtlB(String samplecode) {
		System.out.println("Inside getSampleDtl (type2 query) ");
		List<String> list = new ArrayList<>();
		String return_value = "";
	//	Map res = ReadPropertyFile_old.getPropertyValues();
		Map res = ReadPropertyFile.getPropertyValues();
		final String ip = (String) res.get("ip");

		String eqp = (String) res.get("eqp");
		String hos = (String) res.get("hos");
		String uid = (String) res.get("uid");

		String formatid = (String) res.get("formatid");

		String line = null;
		String line1 = null;
		String line2 = null;

		 String url = "http://" + ip +
		 "/HISInvestigationG5/new_investigation/masters/MachineInterface.jsp?type=3&eqp="
		 + eqp + "&hos=" + hos + "&sam=" + samplecode + "&uid=" + uid;
	//	String url = "https://" + ip + "/SupportServices/service/app/testdtl?hospcode=" + hos + "&samplecode="
	//			+ samplecode + "&eqpid=" + eqp;
		// String url =
		// "https://hmis.rcil.gov.in/SupportServices/service/app/Get_Hospital_Name_Code";
		// /SupportServices/service/app/testdtl?hospcode=33201&samplecode=0303DW001&eqpid=100001
		 System.out.println("\nBefore Sending URL : " + url); 
/*		try {
			SSLContext ssl_ctx = SSLContext.getInstance("TLS");
			TrustManager[] trust_mgr = get_trust_mgr();
			ssl_ctx.init(null, trust_mgr, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
		
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			con.setHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String host, SSLSession sess) {
					if (host.equals(ip))
						return true;
					return false;
				}
			});
			con.setRequestMethod("GET");
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.flush();
			wr.close();
			System.out.println("\nSending 'GET' request to URL : " + url);
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			StringBuffer response = new StringBuffer();
			String inputLine;
			while ((inputLine = in.readLine()) != null)
				response.append(inputLine);
			System.out.println("response " + response.toString());

			String str = response.toString();
			// System.out.println("String object: "+str);
			// in.close();

			// =====================================

			// String s=request.getParameter("id");
			// System.out.print(" serv "+s);
			// String str = reader.toString();
			// System.out.print(" serv--------- "+str);
			// System.out.print(" serv line1---------------- "+line1);

			line2 = ((((str.replace("{", "")).replace(":", "=")).replace("\"", "")).replace("}", ""));
			System.out.print("  new   line2---------------- " + line2);
			String[] kvPairs = line2.split("#");
			// System.out.print("kvPairs =============------ "+kvPairs);
			int r = 0;

		//	for (String kvPair : kvPairs) {
			//	r++;
		//		if (r == 2)
	//				break;
		//		String[] kv = kvPairs[0].split(";");   //Ramji ("=");
				// String key = kv[0];
				// String value = kv[1];
				// System.out.print(" kv[0] ---------------- "+kv[0]);
				// System.out.print(" kv[1] ---------------- "+kv[1]);
				// System.out.print("Test_Code bbbbbbbbb =============------ "+Test_Code);
				// Now do with key whatever you want with key and value...
			//RAmji	if (kv[0].equals("TestData")) 
				
					Test_Code = kvPairs[0];
					System.out.print("Test_Code =============------ " + Test_Code);
				
		//	}

			// System.out.println(" line2 Test_Code value ---------------- "+Test_Code);
			// System.out.println(" line2 input value ---------------- "+input);
			// System.out.println(" line2 output value ---------------- "+output);
			// System.out.println(" line2 hospitalCode value ----------------
			// "+hospitalCode);
			// System.out.println(" line2 userID value ---------------- "+userID);

			// ==========================================

//			String[] arrOfRes = line2.toString().split("#");
//			String[] arrOfRes1 = (arrOfRes[0]).split("=");
////			String Tes = arrOfRes1[0];
//			Test_Code_temp = arrOfRes1[1];
//			// System.out.print("Tes =============------ "+Tes);
//			System.out.print("Test_Code_temp =============------ " + Test_Code_temp);
			if (Test_Code == null)
				Test_Code = "";
			// Test_Code = Test_Code.replaceAll("\\s+", "^");

			//
			//  pat_name = arrOfRes[1];
			 // 
			 // pat_name = pat_name.replaceAll("\\s+", "^");
			 // 
			 // pat_age = arrOfRes[2]; pat_age = pat_age.trim(); pat_age =
			 // pat_age.replaceAll("\\s+", "^"); pat_gender = arrOfRes[3]; if
			 // (pat_gender.equals("M")) { pat_gender = "Male"; } else if
			 // (pat_gender.equals("F")) { pat_gender = "Female"; } sample_id = arrOfRes[4];
			 // 
			 // list.add(Test_Code); list.add(pat_name); list.add(pat_age);
			 // list.add(pat_gender); list.add(sample_id);
			 //
			return_value = Test_Code;
			// return_value = String.valueOf(Test_Code) + "#" + pat_name + "#" + pat_age +
			// "#" + pat_gender;
		} catch (Exception s) {
			s.printStackTrace();
		}
*/
		//--------------------Http Type=3-----------
			
			try {
				/*
				 * SSLContext ssl_ctx = SSLContext.getInstance("TLS"); TrustManager[] trust_mgr
				 * = get_trust_mgr(); ssl_ctx.init(null, // key manager trust_mgr, // trust
				 * manager new SecureRandom()); // random number generator
				 * HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
				 */
		//		System.out.println("\n RaMji URL1 Check ");
				URL obj = new URL(url);
		//		System.out.println("\n RaMji URL2 Check ");
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		//		System.out.println("\n RaMji URL3 Check ");
				/*
				 * ((HttpsURLConnection) con).setHostnameVerifier(new HostnameVerifier() {
				 * public boolean verify(String host, SSLSession sess) { if (host.equals(ip))
				 * return true; else return false; } });
				 */

				con.setRequestMethod("GET");
		//		System.out.println("\n RaMji URL4 Check ");
				con.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				wr.flush();
				wr.close();
				//System.out.println("\n RaMji URL5 Check "+wr);	
				//System.out.println("\nSending 'GET' request to URL : " + url);

				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		//		System.out.println("\n RaMji URL6 Check "+in);	

				System.out.println("responce :1+" + con.getResponseMessage());

				//---------------------
				StringBuffer response = new StringBuffer();
				String inputLine;
				while ((inputLine = in.readLine()) != null)
				{	response.append(inputLine);}

				System.out.println("response " + response.toString());
				String str = response.toString();
				// =====================================
				// String s=request.getParameter("id");
				// System.out.print(" serv "+s);
				// String str = reader.toString();
				// System.out.print(" serv--------- "+str);
				// System.out.print(" serv line1---------------- "+line1);

				line2 = ((((str.replace("{", "")).replace(":", "=")).replace("\"", "")).replace("}", ""));
				System.out.print("  new   line2---------------- " + line2);
				String[] kvPairs = line2.split("#");
				// System.out.print("kvPairs =============------ "+kvPairs);
				int r = 0;
				 line2="231004H0061;231004H0062;231004H0063;231004H0064;231004H0066;231004H0068;";
					
				//RAmji	if (kv[0].equals("TestData")) 
					
						Test_Code = kvPairs[0];
						System.out.print("Test_Code =============------ " + Test_Code);

				if (Test_Code == null)
					Test_Code = "";
				return_value = Test_Code;
				// return_value = String.valueOf(Test_Code) + "#" + pat_name + "#" + pat_age +
				// "#" + pat_gender;

			//	---------------------

			}
		 
/*		 
		 
		 try {
				
				System.out.println("\n RaMji URL1 ");
				URL obj = new URL(url);
				HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
				System.out.println("\n RaMji URL2 ");

				con.setRequestMethod("GET");
				con.setDoOutput(true);
				System.out.println("\n RaMji URL3 ");

				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				System.out.println("\n RaMji URL4 ");

				wr.flush();
				wr.close();
				System.out.println("\nSending 'GET' request to URL : " + url);
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				StringBuffer response = new StringBuffer();
				String inputLine;
				while ((inputLine = in.readLine()) != null)
					response.append(inputLine);
				System.out.println("response " + response.toString());
				String str = response.toString();
				// =====================================
				// String s=request.getParameter("id");
				// System.out.print(" serv "+s);
				// String str = reader.toString();
				// System.out.print(" serv--------- "+str);
				// System.out.print(" serv line1---------------- "+line1);

				line2 = ((((str.replace("{", "")).replace(":", "=")).replace("\"", "")).replace("}", ""));
				System.out.print("  new   line2---------------- " + line2);
				String[] kvPairs = line2.split("#");
				// System.out.print("kvPairs =============------ "+kvPairs);
				int r = 0;

				//RAmji	if (kv[0].equals("TestData")) 
					
						Test_Code = kvPairs[0];
						System.out.print("Test_Code =============------ " + Test_Code);

				if (Test_Code == null)
					Test_Code = "";
				return_value = Test_Code;
				// return_value = String.valueOf(Test_Code) + "#" + pat_name + "#" + pat_age +
				// "#" + pat_gender;
			} 
		 */
		 catch (Exception s) {
				s.printStackTrace();
			}	 
		return line2;
	}

	// --------------------------------------------------------
	public static String getSampleDtl_biolis50i(String samplecode) {
		System.out.println("Inside getSampleDtl (type2 query) ");
		List<String> list = new ArrayList<>();
		String return_value = "";
		Map res = ReadPropertyFile.getPropertyValues();
		final String ip = (String) res.get("ip");

		String eqp = (String) res.get("eqp");
		String hos = (String) res.get("hos");
		String uid = (String) res.get("uid");
		

		
		//String httpcheck = (String) res.get("httpcheck");
		String httpcheck="1";
		String pr="";
		if(httpcheck.equals("1")) {
			pr="https";
			
		}
		else {
			
			pr="http";
		}
		String formatid = (String) res.get("formatid");
		String[] kv=new String[2];
		String line = null;
		String line1 = null;
		String line2 = null;
//		testList.remove(0);

		 String url =  ip +
		 "/HISInvestigationG5/new_investigation/masters/MachineInterface.jsp?type=3&eqp="
		 + eqp + "&hos=" + hos + "&sam=" + samplecode + "&uid=" + uid;

	//	String url = "https://" + ip + "/SupportServices/service/app/testdtl?hospcode=" + hos + "&samplecode="
	//			+ samplecode + "&eqpid=" + eqp;
		// String url =
		// "https://hmis.rcil.gov.in/SupportServices/service/app/Get_Hospital_Name_Code";
		// /SupportServices/service/app/testdtl?hospcode=33201&samplecode=0303DW001&eqpid=100001
		try {
//			SSLContext ssl_ctx = SSLContext.getInstance("TLS");
//			TrustManager[] trust_mgr = get_trust_mgr();
//			ssl_ctx.init(null, trust_mgr, new SecureRandom());
//			HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
//			URL obj = new URL(url);
//			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
//			con.setHostnameVerifier(new HostnameVerifier() {
//				public boolean verify(String host, SSLSession sess) {
//					if (host.equals(ip))
//						return true;
//					return false;
//				}
//			});
			
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			if(httpcheck.equals("1")) {
			SSLContext ssl_ctx = SSLContext.getInstance("TLS");
			TrustManager[] trust_mgr = get_trust_mgr();
			ssl_ctx.init(null, trust_mgr, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
//			URL obj = new URL(url);
			//HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			((HttpsURLConnection) con).setHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String host, SSLSession sess) {
					if (host.equals(ip))
						return true;
					return false;
				}
			});
			
			}
			con.setRequestMethod("GET");
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.flush();
			wr.close();
			System.out.println("\nSending 'GET' request to URL : " + url);
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			StringBuffer response = new StringBuffer();
			String inputLine;
			while ((inputLine = in.readLine()) != null)
				response.append(inputLine);
			System.out.println("response " + response.toString());

			String str = response.toString();
			//System.out.println("String object: " + str);
			// in.close();

			// =====================================

			// String s=request.getParameter("id");
			// System.out.print(" serv "+s);
			// String str = reader.toString();
			// System.out.print(" serv--------- "+str);
			// System.out.print(" serv line1---------------- "+line1);

			line2 = (((str.replace("{", "")).replace("\"", "")).replace("}", "")).trim();
			
			//line2="10061;10031;10031#Amarjith Test Cheema(General)#25 Yr     #M#0309240529#379131900028902#";
			//System.out.print("  new   line2---------------- " + line2);
			String[] kvPairs = line2.split("#");
			// System.out.print("kvPairs =============------ "+kvPairs);
			int r = 0;

			for (String kvPair : kvPairs) {
		//		System.out.print("kvPairs =============------ " + kvPair);
				r++;
				if (r == 2)
					break;
				 kv = kvPair.split(":");
				// String key = kv[0];
				// String value = kv[1];
				// System.out.print(" kv[0] ---------------- "+kv[0]);
				// System.out.print(" kv[1] ---------------- "+kv[1]);
				// System.out.print("Test_Code bbbbbbbbb =============------ "+Test_Code);
				// Now do with key whatever you want with key and value...

				if (kv[0].equals("TestData")) {
					String[] kv1 = kv[1].split(";");
					for (String z : kv1) {
						testList.add(z);
			//		System.out.print("Test_Code =============------ " + testList);
					}
				}
		//		kv[1]="aaaaaaa;";
				/*
				 * if(kv[0].equals("input")) { input=kv[1].trim(); } if(kv[0].equals("product"))
				 * { output=kv[1].trim(); } if(kv[0].equals("varHospitalCode")) {
				 * hospitalCode=kv[1].replace("}","" ).trim(); } if(kv[0].equals("varUserID")) {
				 * userID=kv[1].trim(); }
				 */
			}

			// System.out.println(" line2 Test_Code value ---------------- "+Test_Code);
			// System.out.println(" line2 input value ---------------- "+input);
			// System.out.println(" line2 output value ---------------- "+output);
			// System.out.println(" line2 hospitalCode value ----------------
			// "+hospitalCode);
			// System.out.println(" line2 userID value ---------------- "+userID);

			// ==========================================

//			String[] arrOfRes = line2.toString().split("#");
//			String[] arrOfRes1 = (arrOfRes[0]).split("=");
//			String Tes = arrOfRes1[0];
//			Test_Code_temp = arrOfRes1[1];
//			// System.out.print("Tes =============------ "+Tes);
//			System.out.print("Test_Code_temp =============------ " + Test_Code_temp);
//			if (Test_Code == null)
//				Test_Code = "";
			// Test_Code = Test_Code.replaceAll("\\s+", "^");

			/*
			 * pat_name = arrOfRes[1];
			 * 
			 * pat_name = pat_name.replaceAll("\\s+", "^");
			 * 
			 * pat_age = arrOfRes[2]; pat_age = pat_age.trim(); pat_age =
			 * pat_age.replaceAll("\\s+", "^"); pat_gender = arrOfRes[3]; if
			 * (pat_gender.equals("M")) { pat_gender = "Male"; } else if
			 * (pat_gender.equals("F")) { pat_gender = "Female"; } sample_id = arrOfRes[4];
			 * 
			 * list.add(Test_Code); list.add(pat_name); list.add(pat_age);
			 * list.add(pat_gender); list.add(sample_id);
			 */
			return_value = Test_Code;
			// return_value = String.valueOf(Test_Code) + "#" + pat_name + "#" + pat_age +
			// "#" + pat_gender;
		} catch (Exception s) {
			s.printStackTrace();
		}
	//	return kv[1];
		return line2;
	}

	// ---------------------------------------------------

	public static String insertSampleDtl(Map mp, String sam) {
		System.out.println("Inside insertSampleDtl (type1/type5 query) ");
		Map res = ReadPropertyFile.getPropertyValues();
		final String ip = (String) res.get("ip");
		String port = (String) res.get("port");
		String eqp = (String) res.get("eqp");
		String hos = (String) res.get("hos");
		String uid = (String) res.get("uid");
		String url = "";
		List TestCode = (List) mp.get("TestCode");
		List TestValue = (List) mp.get("TestValue");

		for (int i = 0; i < TestCode.size(); i++) {
			// System.out.println("hiiiiii"+(String)TestCode.get(i)+"
			// "+(String)TestValue.get(i) +" "+sam);
			// save((String)TestCode.get(i) , (String)TestList.get(i) , sampleName , conn);

			url = ip + "/HISInvestigationG5/new_investigation/masters/MachineInterface.jsp?type=5&eqp=" + eqp + "&hos="
					+ hos + "&tcode=" + (String) TestCode.get(i) + "&tval=" + (String) TestValue.get(i) + "&sam=" + sam
					+ "&uid=" + uid;
		}
		StringBuffer response = new StringBuffer();
		try {
			SSLContext ssl_ctx = SSLContext.getInstance("TLS");
			TrustManager[] trust_mgr = get_trust_mgr();
			ssl_ctx.init(null, trust_mgr, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			((HttpsURLConnection) con).setHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String host, SSLSession sess) {
					if (host.equals(ip))
						return true;
					return false;
				}
			});
			con.setRequestMethod("GET");
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.flush();
			wr.close();
			System.out.println("\nSending 'GET' request to URL : " + url);
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null)
				response.append(inputLine);
			System.out.println("response " + response.toString());
			in.close();
		} catch (Exception s) {
			s.printStackTrace();
		}
		return response.toString();
	}

	public static String insert_GenExpert(String TestCode, String TestValue, String sam) {
		System.out.println("Inside insertSampleDtl (type1/type5 query) ");
		Map res = ReadPropertyFile.getPropertyValues();
		final String ip = (String) res.get("ip");
		String port = (String) res.get("port");
		String eqp = (String) res.get("eqp");
		String hos = (String) res.get("hos");
		String uid = (String) res.get("uid");
		String url = "";

		TestCode = (((TestCode.toString().replace("%", "%23")).replace("#", "%25")).replace(" ", "%20")).trim();
		TestValue = (((TestValue.toString().replace("%", "%23")).replace("#", "%25")).replace(" ", "%20")).trim();

		/*
		 * TestCode=TestCode.toString().replace("%","");
		 * TestValue=TestValue.toString().replace("%","");
		 */

		// System.out.println("hiiiiii"+(String)TestCode.get(i)+"
		// "+(String)TestValue.get(i) +" "+sam);
		// save((String)TestCode.get(i) , (String)TestList.get(i) , sampleName , conn);

		if (TestValue.equals("POSITIVE")) {
			TestValue = "2";
		}

		if (TestValue.equals("NEGATIVE")) {
			TestValue = "1";
		}

		/*
		 * TestCode=TestCode.toString().replace(" ","%20");
		 * TestValue=TestValue.toString().replace(" ","%20");
		 */
		sam = (sam.toString().replace(" ", "%20")).trim();

		url = "https://" + ip + "/HISInvestigationG5/new_investigation/masters/MachineInterface.jsp?type=1&eqp=" + eqp
				+ "&hos=" + hos + "&tcode=" + TestCode + "&tval=" + TestValue + "&sam=" + sam + "&uid=" + uid;

		StringBuffer response = new StringBuffer();
		try {
			SSLContext ssl_ctx = SSLContext.getInstance("TLS");
			TrustManager[] trust_mgr = get_trust_mgr();
			ssl_ctx.init(null, trust_mgr, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			((HttpsURLConnection) con).setHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String host, SSLSession sess) {
					if (host.equals(ip))
						return true;
					return false;
				}
			});
			con.setRequestMethod("GET");
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.flush();
			wr.close();
			System.out.println("\nSending 'GET' request to URL : " + url);
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null)
				response.append(inputLine);
			System.out.println("response " + response.toString());
			in.close();
		} catch (Exception s) {
			s.printStackTrace();
		}
		return response.toString();
	}

	public static String insert_GenExpert_MTB(String TestCode, String TestValue, String sam) {
		System.out.println("Inside insertSampleDtl (type1/type5 query) ");
		Map res = ReadPropertyFile.getPropertyValues();
		final String ip = (String) res.get("ip");
		String port = (String) res.get("port");
		String eqp = (String) res.get("eqp");
		String hos = (String) res.get("hos");
		String uid = (String) res.get("uid");
		String url = "";

		TestCode = TestCode.toString().replace(" ", "%20");
		// TestValue=TestValue.toString().replace(" ","%20");
		sam = sam.toString().replace(" ", "%20");

		// System.out.println("hiiiiii"+(String)TestCode.get(i)+"
		// "+(String)TestValue.get(i) +" "+sam);
		// save((String)TestCode.get(i) , (String)TestList.get(i) , sampleName , conn);

		if (TestValue.equals("POSITIVE")) {
			TestValue = "2";
		}

		if (TestValue.equals("NEGATIVE")) {
			TestValue = "1";
		}

		System.out.println("hiiiiii" + TestValue);

		url = "https://" + ip + "/HISInvestigationG5/new_investigation/masters/MachineInterface.jsp?type=1&eqp=" + eqp
				+ "&hos=" + hos + "&tcode=" + TestCode + "&tval=" + TestValue + "&sam=" + sam + "&uid=" + uid;

		StringBuffer response = new StringBuffer();
		try {
			SSLContext ssl_ctx = SSLContext.getInstance("TLS");
			TrustManager[] trust_mgr = get_trust_mgr();
			ssl_ctx.init(null, trust_mgr, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			((HttpsURLConnection) con).setHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String host, SSLSession sess) {
					if (host.equals(ip))
						return true;
					return false;
				}
			});
			con.setRequestMethod("GET");
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.flush();
			wr.close();
			System.out.println("\nSending 'GET' request to URL : " + url);
//---------------------------------------

			FileWriter fw = new FileWriter(path, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("sam : " + sam);
			bw.write("\n");
			bw.write("TestCode : " + TestCode);
			bw.write("\n");
			bw.write("TestValue : " + TestValue);
			bw.write("\n");
			bw.write(url);
			bw.write("\n");

			bw.close();

			// -------------------------------------------------

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null)
				response.append(inputLine);
			System.out.println("response " + response.toString());
			// getSampleDtl(sam);
			in.close();
		} catch (Exception s) {
			s.printStackTrace();
		}
		return response.toString();
	}

	public static String insert_Sysmex800i(String TestCode, String TestValue, String sam) {
		System.out.println("Inside insertSampleDtl (type1/type5 query) ");
		Map res = ReadPropertyFile.getPropertyValues();
		final String ip = (String) res.get("ip");
		String port = (String) res.get("port");
		String eqp = (String) res.get("eqp");
		String hos = (String) res.get("hos");
		String uid = (String) res.get("uid");
		String url = "";

		// System.out.println("hiiiiii"+(String)TestCode.get(i)+"
		// "+(String)TestValue.get(i) +" "+sam);
		// save((String)TestCode.get(i) , (String)TestList.get(i) , sampleName , conn);

		url = "https://" + ip + "/HISInvestigationG5/new_investigation/masters/MachineInterface.jsp?type=1&eqp=" + eqp
				+ "&hos=" + hos + "&tcode=" + TestCode + "&tval=" + TestValue + "&sam=" + sam + "&uid=" + uid;

		StringBuffer response = new StringBuffer();
		try {
			SSLContext ssl_ctx = SSLContext.getInstance("TLS");
			TrustManager[] trust_mgr = get_trust_mgr();
			ssl_ctx.init(null, trust_mgr, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			((HttpsURLConnection) con).setHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String host, SSLSession sess) {
					if (host.equals(ip))
						return true;
					return false;
				}
			});
			con.setRequestMethod("GET");
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.flush();
			wr.close();
			System.out.println("\nSending 'GET' request to URL : " + url);
			// ---------------------------------------

			FileWriter fw = new FileWriter(path, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("sam : " + sam);
			bw.write("\n");
			bw.write("TestCode : " + TestCode);
			bw.write("\n");
			bw.write("TestValue : " + TestValue);
			bw.write("\n");
			bw.write(url);
			bw.write("\n");

			bw.close();

			// -------------------------------------------------
			// ------------------------------------------------
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null)
				response.append(inputLine);
			System.out.println("response " + response.toString());
			in.close();
		} catch (Exception s) {
			s.printStackTrace();
		}
		return response.toString();
	}

	private static TrustManager[] get_trust_mgr() {
		TrustManager[] certs = { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String t) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String t) {
			}
		} };
		return certs;
	}

	public static void st_par2(String parse_this) {
		String[] temp_arr0 = parse_this.split("\004");
		for (int i = 0; i < temp_arr0.length; i++) {
			System.out.println("CHECK THIS ALL " + i + temp_arr0[i]);
			st_par(temp_arr0[i]);
		}
	}

	public static String st_par(String parse_this) {
		System.out.println("Inside st_par (To get id, test names and test values) ");
		System.out.println("BUFF " + parse_this);
		String[] temp_arr = parse_this.split("\002");
		String line = temp_arr[3];
		String[] temp_arr1 = line.split("\\|");
		String id = temp_arr1[2];
		List<String> list = new ArrayList<>();
		List<String> list1 = new ArrayList<>();
		String test_names = "";
		String test_values = "";
		for (int i = 0; i <= 28; i++) {
			String temp = temp_arr[i + 16];
			String[] temp_arr3 = temp.split("\\|");
			String temp2 = temp_arr3[2];
			String requiredString = temp2.substring(temp2.indexOf("^") + 1, temp2.indexOf("^^"));
			if (requiredString.charAt(requiredString.length() - 1) == '%') {
				requiredString = String.valueOf(requiredString) + "25";
			} else if (requiredString.charAt(requiredString.length() - 1) == '#') {
				requiredString = requiredString.substring(0, requiredString.length() - 1);
				requiredString = String.valueOf(requiredString) + "%23";
			}
			list.add(requiredString);
			list1.add(temp_arr3[3]);
			test_names = String.valueOf(test_names) + requiredString + "^";
			test_values = String.valueOf(test_values) + temp_arr3[3] + "^";
		}
		test_names = test_names.substring(0, test_names.length() - 1);
		test_values = test_values.substring(0, test_values.length() - 1);
		System.out.println("test_names = " + test_names);
		System.out.println("test_values = " + test_values);
		// insertSampleDtl(test_names, test_values, id);
		return String.valueOf(test_names) + "#" + test_values;
	}
	
	
	
	///xn 3500 old 
	
	
	public static String insert_SysmexXN350A(String TestCode, String TestValue, String sam) {
		System.out.println("Inside insertSampleDtl (type1/type5 query) ");
		Map res = ReadPropertyFile.getPropertyValues();
		final String ip = (String) res.get("ip");
		String port = (String) res.get("port");
		String eqp = (String) res.get("eqp");
		String hos = (String) res.get("hos");
		String uid = (String) res.get("uid");
		String url = "";

		
		String httpcheck = (String) res.get("httpcheck");
	      String pr="";
			if(httpcheck.equals("1")) {
				pr="https";
				
			}
			else {
				
				pr="http";
			}
	      
		TestCode = (((((((TestCode.toString().trim()).replace("%", "%25").replace("/", "").replace("#", "%23"))).replace("(", "%28"))
				.replace(")", "%29")).replace(" ", "%20")).replace("+", "%2B")).trim();
		TestValue = (((((((TestValue.toString().trim())).replace("%", "%25").replace("+", "%2B").replace("-", "%2D").replace("#", "%23")).replace(" ", "%20")).replace("<", "%3C")).replace(">", "%3E")))
				.trim();
		TestValue=TestValue.toString().replaceAll(">", "%3E");
		TestValue=TestValue.toString().replaceAll("<", "%3C");
		/*%2B
		 * TestCode=TestCode.toString().replace("%","");
		 * TestValue=TestValue.toString().replace("%","");
		 */

		// System.out.println("hiiiiii"+(String)TestCode.get(i)+"
		// "+(String)TestValue.get(i) +" "+sam);
		// save((String)TestCode.get(i) , (String)TestList.get(i) , sampleName , conn);

		if (TestValue.equals("POSITIVE")) {
			TestValue = "2";
		}

		if (TestValue.equals("NEGATIVE")) {
			TestValue = "1";
		}

		/*
		 * TestCode=TestCode.toString().replace(" ","%20");
		 * TestValue=TestValue.toString().replace(" ","%20");
		 */
		sam = ((sam.toString().trim()).replace(" ", "%20")).trim();
		System.out.println("TestCode:" + TestCode);
		System.out.println("TestValue:" + TestValue);
		System.out.println("sam:" + sam);
		
		
/*		url = "https://" + ip + "/HISInvestigationG5/new_investigation/masters/MachineInterface.jsp?type=1&eqp=" + eqp
				+ "&hos=" + hos + "&tcode=" + TestCode + "&tval=" + TestValue + "&sam=" + sam + "&uid=" + uid;
		System.out.println("url Cobas 801 https:" + url);
		StringBuffer response = new StringBuffer();
		try {
			SSLContext ssl_ctx = SSLContext.getInstance("TLS");
			TrustManager[] trust_mgr = get_trust_mgr();
			ssl_ctx.init(null, trust_mgr, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			((HttpsURLConnection) con).setHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String host, SSLSession sess) {
					if (host.equals(ip))
						return true;
					return false;
				}
			});
			con.setRequestMethod("GET");
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.flush();
			wr.close();
			System.out.println("\nSending 'GET' request to URL : " + url);
			// ---------------------------------------

			FileWriter fw = new FileWriter(path, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("sam : " + sam);
			bw.write("\n");
			bw.write("TestCode : " + TestCode);
			bw.write("\n");
			bw.write("TestValue : " + TestValue);
			bw.write("\n");
			bw.write(url);
			bw.write("\n");

			bw.close();

			// -------------------------------------------------
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null)
				response.append(inputLine);
			System.out.println("response " + response.toString());
			in.close();
		} catch (Exception s) {
			s.printStackTrace();
		}
*/		//--------------http- AIIMS Bhubneshwat--------
//		url = ip + "/HISInvestigationG5/new_investigation/masters/MachineInterface.jsp?type=1&eqp=" + eqp + "&hos="
//				+ hos + "&tcode=" + TestCode + "&tval=" + TestValue + "&sam=" + sam + "&uid=" + uid;
		url =  ip + "/HISInvestigationG5/new_investigation/masters/MachineInterface.jsp?type=1&eqp=" + eqp
				+ "&hos=" + hos + "&tcode=" + TestCode + "&tval=" + TestValue + "&sam=" + sam + "&uid=" + uid;
		System.out.println("url Cobas 801 :" + url);
		StringBuffer response = new StringBuffer();

		
		try {
			/*
			 * SSLContext ssl_ctx = SSLContext.getInstance("TLS"); TrustManager[] trust_mgr
			 * = get_trust_mgr(); ssl_ctx.init(null, // key manager trust_mgr, // trust
			 * manager new SecureRandom()); // random number generator
			 * HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
			 */

//			URL obj = new URL(url);
//			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//			
//			  ((HttpsURLConnection) con).setHostnameVerifier(new HostnameVerifier() {
//			  public boolean verify(String host, SSLSession sess) { if (host.equals(ip))
//			  return true; else return false; } });
//	
			  
			  
			  URL obj = new URL(url);
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				if(httpcheck.equals("1")) {
				SSLContext ssl_ctx = SSLContext.getInstance("TLS");
				TrustManager[] trust_mgr = get_trust_mgr();
				ssl_ctx.init(null, trust_mgr, new SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
//				URL obj = new URL(url);
				//HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				((HttpsURLConnection) con).setHostnameVerifier(new HostnameVerifier() {
					public boolean verify(String host, SSLSession sess) {
						if (host.equals(ip))
							return true;
						return false;
					}
				});
				
				}
//
//			SSLContext ssl_ctx = SSLContext.getInstance("TLS");
//			TrustManager[] trust_mgr = get_trust_mgr();
//			ssl_ctx.init(null, trust_mgr, new SecureRandom());
//			HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
//			URL obj = new URL(url);
//			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
//			con.setHostnameVerifier(new HostnameVerifier() {
//				public boolean verify(String host, SSLSession sess) {
//					if (host.equals(ip))
//						return true;
//					return false;
//				}
//			});
			
			con.setRequestMethod("GET");
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.flush();
			wr.close();

			System.out.println("\nSending 'GET' request to URL : " + url);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			System.out.println("response--:" + response.toString());
			System.out.println("responce :1+" + con.getResponseMessage());

/*			FileWriter fw11 = new FileWriter(path_HIMS_LOG, true);
			BufferedWriter bw = new BufferedWriter(fw11);
			bw.write("\n");
			bw.write("Sample Number : " + sam);
			bw.write("\n");
			bw.write("Test Code : " + TestCode);
			bw.write("\n");
			bw.write("Test Value : " + TestValue);
			bw.write("\n");
			bw.write(url);
			bw.write("\n");
			bw.write(response.toString());
			bw.write("\n");
			bw.write("----------------------------------------------------------");
			bw.flush();
			bw.close();
*/			in.close();
	
		} catch (Exception s) {
			s.printStackTrace();	   
		//--Http End----------------------------	
		}
		return response.toString();
	}

//Horriba Machine Result Entry....
	// ----------------------------------------------------------------------------------------
	public static String insert_SysmexXN350(Map mp, String sam) {
		System.out.println("Inside insertSampleDtl (type1/type5 query) ");
		Map res = ReadPropertyFile.getPropertyValues();
		final String ip = (String) res.get("ip");
		String port = (String) res.get("port");
		String eqp = (String) res.get("eqp");
		String hos = (String) res.get("hos");
		String uid = (String) res.get("uid");
		String url = "";

		List TestCode = (List)mp.get("TestCode");
		List TestValue = (List)mp.get("TestValue");

		StringBuffer testcode1 =new StringBuffer();;
		StringBuffer testvalue1=new StringBuffer();;
		
		//System.out.println("total testcode"+TestCode.size());
		//System.out.println("total testvalue"+TestValue.size());
		for(int i=0;i<TestCode.size();i++) {
			
			
			String str= (String) TestCode.get(i);
			String str1 =(String) TestValue.get(i);
			//System.out.println("str : " +str);
			//System.out.println("st1r : " +str1);
			//str.replaceAll("", "--");
			//str1.replaceAll("", "--");
			if(str1.equals("")||str1.equals(" ")) str1="--";
			if(i==TestCode.size()-1) {
				testcode1.append(str.replace("#", "*"));
				testvalue1.append(str1);
	
			}
else {
			if(str.toString().trim().length() >=3 && str.toString().trim().length() <=6  )
			{
				
				
			if(str.toString().trim().contains("#")) {
			//TestCode.toString().replaceAll("#", "*");str.ap
				
			testcode1.append(str.replace("#", "*$"));
			if(testvalue1.equals("") ||testvalue1.equals(" ")) {
				testvalue1.append("--");
				
				testvalue1.append("$");
					
			}
			else {
			testvalue1.append(str1);
			testvalue1.append("$");
			}
		}
		else if(str.toString().trim().contains("%"))
		{
			//TestCode.toString().replaceAll("%", "_$");
			testcode1.append(str.replace("%", "_$"));
			testvalue1.append(str1);
			testvalue1.append("$");
		}
		else {
			testcode1.append(str+"$");
			testvalue1.append(str1+"$");
			
		}
		
			
				}
}
		}
		
		String stra=testcode1.toString().replaceAll(" ", "");
		String strb=testvalue1.toString().replaceAll(" ", "");
	//	System.out.println("stra"+stra);
//		System.out.println("strb"+strb);
		
//		String TestCode1 = (((((((TestCode.toString().trim()).replace("%", "_$").replace("#", "*$"))).replace("(", "%28"))
//				.replace(")", "%29")).replace(" ", "%20")).replace("+", "%2B")).trim();
//		String TestValue1 = (((((((TestValue.toString().trim())).replace("%", "_$").replace("#", "*$")).replace(" ", "__")).replace("<", "%3C")).replace(">", "%3E")))
//				.trim();

		/*
		 * TestCode=TestCode.toString().replace("%","");
		 * TestValue=TestValue.toString().replace("%","");
		 */

		// System.out.println("hiiiiii"+(String)TestCode.get(i)+"
		// "+(String)TestValue.get(i) +" "+sam);
		// save((String)TestCode.get(i) , (String)TestList.get(i) , sampleName , conn);

//		if (TestValue.equals("POSITIVE")) {
//			TestValue = "2";
//		}
//
//		if (TestValue.equals("NEGATIVE")) {
//			TestValue = "1";
//		}

		/*
		 * TestCode=TestCode.toString().replace(" ","%20");
		 * TestValue=TestValue.toString().replace(" ","%20");
		 */
		sam = ((sam.toString().trim()).replace(" ", "%20")).trim();
		//System.out.println("TestCode:" + TestCode.toString());
		//System.out.println("TestValue:" + TestValue.toString());
		//System.out.println("sam:" + sam);
/*		url = "https://" + ip + "/HISInvestigationG5/new_investigation/masters/MachineInterface.jsp?type=1&eqp=" + eqp
				+ "&hos=" + hos + "&tcode=" + TestCode + "&tval=" + TestValue + "&sam=" + sam + "&uid=" + uid;
		System.out.println("url Cobas 801 https:" + url);
		StringBuffer response = new StringBuffer();
		try {
			SSLContext ssl_ctx = SSLContext.getInstance("TLS");
			TrustManager[] trust_mgr = get_trust_mgr();
			ssl_ctx.init(null, trust_mgr, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			((HttpsURLConnection) con).setHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String host, SSLSession sess) {
					if (host.equals(ip))
						return true;
					return false;
				}
			});
			con.setRequestMethod("GET");
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.flush();
			wr.close();
			System.out.println("\nSending 'GET' request to URL : " + url);
			// ---------------------------------------

			FileWriter fw = new FileWriter(path, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("sam : " + sam);
			bw.write("\n");
			bw.write("TestCode : " + TestCode);
			bw.write("\n");
			bw.write("TestValue : " + TestValue);
			bw.write("\n");
			bw.write(url);
			bw.write("\n");

			bw.close();

			// -------------------------------------------------
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null)
				response.append(inputLine);
			System.out.println("response " + response.toString());
			in.close();
		} catch (Exception s) {
			s.printStackTrace();
		}
*/		//--------------http- AIIMS Bhubneshwat--------
//		url = ip + "/HISInvestigationG5/new_investigation/masters/MachineInterface.jsp?type=1&eqp=" + eqp + "&hos="
//				+ hos + "&tcode=" + TestCode + "&tval=" + TestValue + "&sam=" + sam + "&uid=" + uid;
		url = "https://" + ip + "/HISInvestigationG5/new_investigation/masters/MachineInterface.jsp?type=5&eqp=" + 100002
				+ "&hos=" + hos + "&tcode=" + stra + "&tval=" + strb + "&sam=" + sam + "&uid=" + uid;
		  String modifiedString = url.replace("tval=$", "tval=");
		  String  modifiedString1 = modifiedString.replace("%", "_");
		   String modifiedString2 = modifiedString1.replace("#", "*");
		     
	       // System.out.println(modifiedString);
		
		System.out.println("url Cobas 801 :" + modifiedString2);
		StringBuffer response = new StringBuffer();

		
		try {
			/*
			 * SSLContext ssl_ctx = SSLContext.getInstance("TLS"); TrustManager[] trust_mgr
			 * = get_trust_mgr(); ssl_ctx.init(null, // key manager trust_mgr, // trust
			 * manager new SecureRandom()); // random number generator
			 * HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
			 */
			SSLContext ssl_ctx = SSLContext.getInstance("TLS");
			TrustManager[] trust_mgr = get_trust_mgr();
			ssl_ctx.init(null, trust_mgr, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			((HttpsURLConnection) con).setHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String host, SSLSession sess) {
					if (host.equals(ip))
						return true;
					return false;
				}
			});
			//URL obj = new URL(modifiedString2);
			//HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			/*
			 * ((HttpsURLConnection) con).setHostnameVerifier(new HostnameVerifier() {
			 * public boolean verify(String host, SSLSession sess) { if (host.equals(ip))
			 * return true; else return false; } });
			 */

			con.setRequestMethod("GET");
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.flush();
			wr.close();

			System.out.println("\nSending 'GET' request to URL : " + modifiedString2);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			System.out.println("response--:" + response.toString());
			System.out.println("responce :1+" + con.getResponseMessage());

/*			FileWriter fw11 = new FileWriter(path_HIMS_LOG, true);
			BufferedWriter bw = new BufferedWriter(fw11);
			bw.write("\n");
			bw.write("Sample Number : " + sam);
			bw.write("\n");
			bw.write("Test Code : " + TestCode);
			bw.write("\n");
			bw.write("Test Value : " + TestValue);
			bw.write("\n");
			bw.write(url);
			bw.write("\n");
			bw.write(response.toString());
			bw.write("\n");
			bw.write("----------------------------------------------------------");
			bw.flush();
			bw.close();
*/			in.close();
	
		} catch (Exception s) {
			s.printStackTrace();	   
		//--Http End----------------------------	
		}
		return response.toString();
	}
	
	
	public static String insert_SysmexXN350Bubneswar(Map mp, String sam) {
		System.out.println("Inside insertSampleDtl (type1/type5 query) ");
		//GenericServer.saveToFile("Inside insertSampleDtl (type1/type5 query) ",GenericServer.FILE_NAME);
		Map res = ReadPropertyFile.getPropertyValues();
		final String ip = (String) res.get("ip");
		String port = (String) res.get("port");
		String eqp = (String) res.get("eqp");
		String hos = (String) res.get("hos");
		String uid = (String) res.get("uid");
		String url = "";

		
		String httpcheck = (String) res.get("httpcheck");
	      String pr="";
			if(httpcheck.equals("1")) {
				pr="https";
				
			}
			else {
				
				pr="http";
			}
		
		
		List TestCode = (List)mp.get("TestCode");
		List TestValue = (List)mp.get("TestValue");
		
		
		
		
		StringBuffer testcode1 =new StringBuffer();;
		StringBuffer testvalue1=new StringBuffer();;
		
		//System.out.println("total testcode"+TestCode.size());
		//System.out.println("total testvalue"+TestValue.size());
		for(int i=0;i<TestCode.size();i++) {
			
			
			String str= (String) TestCode.get(i);
			String str1 =(String) TestValue.get(i);
			//System.out.println("str : " +str);
			//System.out.println("st1r : " +str1);
			//str.replaceAll("", "--");
			//str1.replaceAll("", "--");
			if(str1.equals("")||str1.equals(" ")) str1="--";
			if(i==TestCode.size()-1) {
				testcode1.append(str.replace("#", "*"));
				testvalue1.append(str1);
	
			}
else {
			if(str.toString().trim().length() >=2 && str.toString().trim().length() <=6  )
			{
				
				
			if(str.toString().trim().contains("#")) {
			//TestCode.toString().replaceAll("#", "*");str.ap
				
			testcode1.append(str.replace("#", "*$"));
			if(testvalue1.equals("") ||testvalue1.equals(" ")) {
				testvalue1.append("--");
				
				testvalue1.append("$");
					
			}
			else {
			testvalue1.append(str1);
			testvalue1.append("$");
			}
		}
		else if(str.toString().trim().contains("%"))
		{
			//TestCode.toString().replaceAll("%", "_$");
			testcode1.append(str.replace("%", "_$"));
			testvalue1.append(str1);
			testvalue1.append("$");
		}
		else {
			testcode1.append(str+"$");
			testvalue1.append(str1+"$");
			
		}
		
			
				}
}
		}
		
		String stra=testcode1.toString().replaceAll(" ", "");
		String strb=testvalue1.toString().replaceAll(" ", "");
		
		 
	//	System.out.println("stra"+stra);
//		System.out.println("strb"+strb);
		
//		String TestCode1 = (((((((TestCode.toString().trim()).replace("%", "_$").replace("#", "*$"))).replace("(", "%28"))
//				.replace(")", "%29")).replace(" ", "%20")).replace("+", "%2B")).trim();
//		String TestValue1 = (((((((TestValue.toString().trim())).replace("%", "_$").replace("#", "*$")).replace(" ", "__")).replace("<", "%3C")).replace(">", "%3E")))
//				.trim();

		/*
		 * TestCode=TestCode.toString().replace("%","");
		 * TestValue=TestValue.toString().replace("%","");
		 */

		// System.out.println("hiiiiii"+(String)TestCode.get(i)+"
		// "+(String)TestValue.get(i) +" "+sam);
		// save((String)TestCode.get(i) , (String)TestList.get(i) , sampleName , conn);

//		if (TestValue.equals("POSITIVE")) {
//			TestValue = "2";
//		}
//
//		if (TestValue.equals("NEGATIVE")) {
//			TestValue = "1";
//		}

		/*
		 * TestCode=TestCode.toString().replace(" ","%20");
		 * TestValue=TestValue.toString().replace(" ","%20");
		 */
		sam = ((sam.toString().trim()).replace(" ", "%20")).trim();
		//System.out.println("TestCode:" + TestCode.toString());
		//System.out.println("TestValue:" + TestValue.toString());
		//System.out.println("sam:" + sam);
/*		url = "https://" + ip + "/HISInvestigationG5/new_investigation/masters/MachineInterface.jsp?type=1&eqp=" + eqp
				+ "&hos=" + hos + "&tcode=" + TestCode + "&tval=" + TestValue + "&sam=" + sam + "&uid=" + uid;
		System.out.println("url Cobas 801 https:" + url);
		StringBuffer response = new StringBuffer();
		try {
			SSLContext ssl_ctx = SSLContext.getInstance("TLS");
			TrustManager[] trust_mgr = get_trust_mgr();
			ssl_ctx.init(null, trust_mgr, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			((HttpsURLConnection) con).setHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String host, SSLSession sess) {
					if (host.equals(ip))
						return true;
					return false;
				}
			});
			con.setRequestMethod("GET");
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.flush();
			wr.close();
			System.out.println("\nSending 'GET' request to URL : " + url);
			// ---------------------------------------

			FileWriter fw = new FileWriter(path, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("sam : " + sam);
			bw.write("\n");
			bw.write("TestCode : " + TestCode);
			bw.write("\n");
			bw.write("TestValue : " + TestValue);
			bw.write("\n");
			bw.write(url);
			bw.write("\n");

			bw.close();

			// -------------------------------------------------
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null)
				response.append(inputLine);
			System.out.println("response " + response.toString());
			in.close();
		} catch (Exception s) {
			s.printStackTrace();
		}
*/		//--------------http- AIIMS Bhubneshwat--------
//		url = ip + "/HISInvestigationG5/new_investigation/masters/MachineInterface.jsp?type=1&eqp=" + eqp + "&hos="
//				+ hos + "&tcode=" + TestCode + "&tval=" + TestValue + "&sam=" + sam + "&uid=" + uid;
		url =  ip + "/HISInvestigationG5/new_investigation/masters/MachineInterface.jsp?type=5&eqp=" + eqp
				+ "&hos=" + hos + "&tcode=" + stra + "&tval=" + strb + "&sam=" + sam + "&uid=" + uid;
		  String modifiedString = url.replace("tval=$", "tval=");
		  String  modifiedString1 = modifiedString.replace("%", "_");
		   String modifiedString2 = modifiedString1.replace("#", "*");
		   modifiedString2 = modifiedString2.replace("\\>", "%3E");
		   modifiedString2 = modifiedString2.replaceAll("\\<", "%3C");
		   modifiedString2 = modifiedString2.replaceAll(" ", "%20");
		   modifiedString2 = modifiedString2.replaceAll("\\+", "%2B");
		   modifiedString2 = modifiedString2.replaceAll("\\-", "%2D");
		     
	        System.out.println(modifiedString2);
	     	//GenericServer.logMessage(modifiedString2, Color.BLUE);
	//	System.out.println("url Cobas 801 :" + modifiedString2);
		StringBuffer response = new StringBuffer();

		
		try {
			/*
			 * SSLContext ssl_ctx = SSLContext.getInstance("TLS"); TrustManager[] trust_mgr
			 * = get_trust_mgr(); ssl_ctx.init(null, // key manager trust_mgr, // trust
			 * manager new SecureRandom()); // random number generator
			 * HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
			 */
			
			URL obj = new URL(modifiedString2);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			
			if(httpcheck.equals("1")) {
				SSLContext ssl_ctx = SSLContext.getInstance("TLS");
				TrustManager[] trust_mgr = get_trust_mgr();
				ssl_ctx.init(null, trust_mgr, new SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
//				URL obj = new URL(url);
				//HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				((HttpsURLConnection) con).setHostnameVerifier(new HostnameVerifier() {
					public boolean verify(String host, SSLSession sess) {
						if (host.equals(ip))
							return true;
						return false;
					}
				});
				
				}
			
			/*
			 * ((HttpsURLConnection) con).setHostnameVerifier(new HostnameVerifier() {
			 * public boolean verify(String host, SSLSession sess) { if (host.equals(ip))
			 * return true; else return false; } });
			 */

			con.setRequestMethod("GET");
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.flush();
			wr.close();

			System.out.println("\nSending 'GET' request to URL : " + modifiedString2);
			//GenericServer.logMessage("\nSending 'GET' request to URL : " + modifiedString2 , Color.RED);
		//	GenericServer.saveToFile("\nSending 'GET' request to URL : " + modifiedString2,GenericServer.FILE_NAME);
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			System.out.println("response--:" + response.toString());
			System.out.println("responce :1+" + con.getResponseMessage());
		//	GenericServer.saveToFile("response--:" + response.toString(),GenericServer.FILE_NAME);
			
			//GenericServer.logMessage("response--:" + response.toString(), Color.RED);
			//GenericServer.logMessage("responce :1+" + con.getResponseMessage() , Color.RED);

/*			FileWriter fw11 = new FileWriter(path_HIMS_LOG, true);
			BufferedWriter bw = new BufferedWriter(fw11);
			bw.write("\n");
			bw.write("Sample Number : " + sam);
			bw.write("\n");
			bw.write("Test Code : " + TestCode);
			bw.write("\n");
			bw.write("Test Value : " + TestValue);
			bw.write("\n");
			bw.write(url);
			bw.write("\n");
			bw.write(response.toString());
			bw.write("\n");
			bw.write("----------------------------------------------------------");
			bw.flush();
			bw.close();
*/			in.close();
	
		} catch (Exception s) {
			s.printStackTrace();	   
		//--Http End----------------------------	
		}
		return response.toString();
	}
	
	

	// --------------------------------------------------------------------
	public static List getSampleDtl_imola(String samplecode) {
		System.out.println("Inside getSampleDtl (type2 query) ");
		List<String> list = new ArrayList<>();
		String return_value = "";
		Map res = ReadPropertyFile.getPropertyValues();
		final String ip = (String) res.get("ip");
		String eqp = (String) res.get("eqp");
		String hos = (String) res.get("hos");
		String uid = (String) res.get("uid");
		String formatid = (String) res.get("formatid");
		String line = null;
		String line1 = null;
		String line2 = null;
		String line3 = null;

		String url = "https://" + ip + "/HISInvestigationG5/new_investigation/masters/MachineInterface.jsp?type=2&eqp="
				+ eqp + "&hos=" + hos + "&sam=" + samplecode + "&uid=" + uid;
		// String url = "https://" + ip +
		// "/SupportServices/service/app/testdtl?hospcode=" + hos + "&samplecode=" +
		// samplecode + "&eqpid=" + eqp;
		// /SupportServices/service/app/testdtl?hospcode=33201&samplecode=0303DW001&eqpid=100001
		try {
			SSLContext ssl_ctx = SSLContext.getInstance("TLS");
			TrustManager[] trust_mgr = get_trust_mgr();
			ssl_ctx.init(null, trust_mgr, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			con.setHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String host, SSLSession sess) {
					if (host.equals(ip))
						return true;
					return false;
				}
			});
			con.setRequestMethod("GET");
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.flush();
			wr.close();
			System.out.println("\nSending 'GET' request to URL : " + url);
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			StringBuffer response = new StringBuffer();
			String inputLine;
			while ((inputLine = in.readLine()) != null)
				response.append(inputLine);
			System.out.println("response " + response.toString());

			String str = response.toString();
			// System.out.println("String object: "+str);
			// in.close();

			// =====================================

			// String s=request.getParameter("id");
			// System.out.print(" serv "+s);
			// String str = reader.toString();
			// System.out.print(" serv--------- "+str);
			// System.out.print(" serv line1---------------- "+line1);

			/*
			 * line2=((((str.replace("{",
			 * "")).replace(":","=")).replace("\"","")).replace("}","")).trim();
			 * System.out.print("  new   line2---------------- "+line2); String[] kvPairs =
			 * line2.split("#"); //
			 * System.out.print("kvPairs =============------ "+kvPairs); int r=0;
			 * 
			 * for(String kvPair: kvPairs) { r++; if(r==2) break; String[] kv =
			 * kvPair.split("="); // String key = kv[0]; // String value = kv[1]; //
			 * System.out.print(" kv[0] ---------------- "+kv[0]); //
			 * System.out.print(" kv[1] ---------------- "+kv[1]); //
			 * System.out.print("Test_Code  bbbbbbbbb =============------ "+Test_Code); //
			 * Now do with key whatever you want with key and value...
			 * if(kv[0].equals("TestData")) { Test_Code=kv[1];
			 * System.out.print("Test_Code =============------ "+Test_Code); }
			 * if(kv[0].equals("input")) { input=kv[1].trim(); } if(kv[0].equals("product"))
			 * { output=kv[1].trim(); } if(kv[0].equals("varHospitalCode")) {
			 * hospitalCode=kv[1].replace("}","" ).trim(); } if(kv[0].equals("varUserID")) {
			 * userID=kv[1].trim(); }
			 */
			// }

			// System.out.println(" line2 Test_Code value ---------------- "+Test_Code);
			// System.out.println(" line2 input value ---------------- "+input);
			// System.out.println(" line2 output value ---------------- "+output);
			// System.out.println(" line2 hospitalCode value ----------------
			// "+hospitalCode);
			// System.out.println(" line2 userID value ---------------- "+userID);

			// ==========================================

			line3 = response.toString();
			String[] arrOfRes_HI_API = line3.toString().split("#");
			String[] arrOfRes11 = (arrOfRes_HI_API[0]).split(";");

			// ===============================

			/*
			 * String[] arrOfRes = line2.toString().split("#"); String[]
			 * arrOfRes1=(arrOfRes[0]).split("="); String Tes = arrOfRes1[0]; Test_Code_temp
			 * = arrOfRes1[1]; // System.out.print("Tes =============------ "+Tes);
			 * System.out.print("Test_Code_temp =============------ "+Test_Code_temp);
			 */
			// Test_Code = Test_Code.replaceAll("\\s+", "^");

			/*
			 * pat_name = arrOfRes[1];
			 * 
			 * pat_name = pat_name.replaceAll("\\s+", "^");
			 * 
			 * pat_age = arrOfRes[2]; pat_age = pat_age.trim(); pat_age =
			 * pat_age.replaceAll("\\s+", "^"); pat_gender = arrOfRes[3]; if
			 * (pat_gender.equals("M")) { pat_gender = "Male"; } else if
			 * (pat_gender.equals("F")) { pat_gender = "Female"; } sample_id = arrOfRes[4];
			 */
			for (int i = 0; i < arrOfRes11.length; i++) {
				list.add(arrOfRes11[i]);
			}
			/*
			 * list.add(Test_Code); list.add(pat_name); list.add(pat_age);
			 * list.add(pat_gender); list.add(sample_id);
			 */
			// return_value = Test_Code;
			return_value = line3;
			// return_value = String.valueOf(Test_Code) + "#" + pat_name + "#" + pat_age +
			// "#" + pat_gender;
		} catch (

		Exception s) {
			s.printStackTrace();
		}
		return list;
	}

	// ===========================================
	// ---------------------------------------------------------------------------------------------------
	public static String get_id_dtl(String parse_this) {
		System.out.println("Inside get_id_dtl (To get id and sample type) ");
		String[] temp_arr = parse_this.split("\002|\027|\003");
		String temp = temp_arr[3];
		String[] temp_arr1 = temp.split("\\|");
		List<String> list = new ArrayList<>();
		list.add(temp_arr1[2]);
		list.add(temp_arr1[10]);
		sample_id = temp_arr1[2];
		sample_type = temp_arr1[10];
		String return_value = String.valueOf(temp_arr1[2]) + "#" + temp_arr1[10];
		return return_value;
	}

	public static String getResponsePacket(String name, String age, String gender, String s_id) {
		System.out.println("Inside getResponsePacket (To get response packet) ");
		char CR = '\r';
		char LF = '\n';
		String parse_this = "\0021H|\\^&|1||Mindray^BC-6800^||||||Worksheetresponse^00011|P|LIS2-A2|20191014173209\027\0022P|1||||Michael^Jordan||20191014173209^6^Y|Male||||||||||||||||Internal medicine|A -501^1002\027\0023O|1|1012000|||||20191014173209|||Jack|||Virus infections|20191014173209|Venousblood^||||||||||Q\027\0024R|1|^Test Mode^^08003|CBC+DIFF||^|^^^^^^\027\0025R|2|^Ref Group^^01002|Child||^|^^^^^^\027\0026R|3|^Remark^^01001|Emergency patient||^|^^^^^^\027\0027R|4|^Charge type^^01015|Public||^|^^^^^^\027\0020R|5|^Patient type^^01016|Outpatient||^|^^^^^^\027\0021R|6|^SerialNumber^^08005|3||^|^^^^^^\027\0022R|7|^Custom patient info 1^^01009|Nothing||^|^^^^^^\027\0023R|8|^Custom patient info 2^^01010|Nothing||^|^^^^^^\027\0024R|9|^Custom patient info 3^^01011|Nothing||^|^^^^^^\027\0025L|1|N\00305";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		String datetime = formatter.format(date).toString();
		parse_this = parse_this.replaceAll("20140909165555", datetime);
		parse_this = parse_this.replaceAll("Michael\\^Jordan", name);
		parse_this = parse_this.replaceAll("6\\^Y", age);
		parse_this = parse_this.replaceAll("SampleID4001", s_id);
		parse_this = parse_this.replaceAll("Male", gender);
		List<String> list = new ArrayList<>();
		String[] temp_arr = parse_this.split("\027");
		for (int i = 1; i < temp_arr.length; i++) {
			String temp = Server.CheckSum(temp_arr[i]);
			list.add(temp);
		}
		for (int i = 1; i < temp_arr.length; i++)
			temp_arr[i] = "\r\027" + (String) list.get(i - 1) + '\r' + '\n' + temp_arr[i];
		String res = "";
		for (int i = 0; i < temp_arr.length; i++)
			res = String.valueOf(res) + temp_arr[i];
		System.out.println(res);
		return res;
	}

	public static String insert_Cobas801(String TestCode, String TestValue, String sam) {
	      System.out.println("Inside insertSampleDtl (type1/type5 query) ");
	      Map res = ReadPropertyFile.getPropertyValues();
	      final String ip = (String)res.get("ip");
	      String port = (String)res.get("port");
	      String eqp = (String)res.get("eqp");
	      String hos = (String)res.get("hos");
	      String uid = (String)res.get("uid");


			String httpcheck = (String) res.get("httpcheck");
	      String pr="";
			if(httpcheck.equals("1")) {
				pr="https";
				
			}
			else {
				
				pr="http";
			}
	      
	      
	      
	      String url = "";
	      TestCode = TestCode.toString().trim().replace("%", "%25").replace("#", "%23").replace("(", "%28").replace(")", "%29").replace(" ", "%20").replace("+", "%2B").trim();
	      TestValue = TestValue.toString().trim().replace("%", "%25").replace("#", "%23").replace(" ", "%20").replace("<", "%3C").replace(">", "%3E").trim();
	      if (TestValue.equals("POSITIVE")) {
	         TestValue = "2";
	      }
	   
	      if (TestValue.equals("NEGATIVE")) {
	         TestValue = "1";
	      }
	      

	      if (TestValue.equals("") || TestValue.equals(null)) {
	         TestValue = "--";
	      }

	      sam = sam.toString().trim().replace(" ", "%20").trim();
	      System.out.println("TestCode:" + TestCode);
	      System.out.println("TestValue:" + TestValue);
	      System.out.println("sam:" + sam);
	      url = pr+"://" + ip + "/HISInvestigationG5/new_investigation/masters/MachineInterface.jsp?type=1&eqp=" + eqp + "&hos=" + hos + "&tcode=" + TestCode + "&tval=" + TestValue + "&sam=" + sam + "&uid=" + uid;
	      System.out.println("url Cobas 801 " + url);
	      StringBuffer response = new StringBuffer();

	      try {
	        
//	         //HTTPS
//	     	SSLContext ssl_ctx = SSLContext.getInstance("TLS");
//			TrustManager[] trust_mgr = get_trust_mgr();
//			ssl_ctx.init(null, trust_mgr, new SecureRandom());
//			HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
//			 URL obj = new URL(url);
//	         HttpsURLConnection con = (HttpsURLConnection)obj.openConnection();
//	         
//			con.setHostnameVerifier(new HostnameVerifier() {
//				public boolean verify(String host, SSLSession sess) {
//					if (host.equals(ip))
//						return true;
//					return false;
//				}
//			});
	    	  
//	         //END
	    	  
	    	  URL obj = new URL(url);
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				if(httpcheck.equals("1")) {
				SSLContext ssl_ctx = SSLContext.getInstance("TLS");
				TrustManager[] trust_mgr = get_trust_mgr();
				ssl_ctx.init(null, trust_mgr, new SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
//				URL obj = new URL(url);
				//HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				((HttpsURLConnection) con).setHostnameVerifier(new HostnameVerifier() {
					public boolean verify(String host, SSLSession sess) {
						if (host.equals(ip))
							return true;
						return false;
					}
				});
				
				}
	         con.setRequestMethod("GET");
	         con.setDoOutput(true);
	         DataOutputStream wr = new DataOutputStream(con.getOutputStream());
	         wr.flush();
	         wr.close();
	         System.out.println("\nSending 'GET' request to URL : " + url);
	         BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

	         String inputLine;
	         while((inputLine = in.readLine()) != null) {
	            response.append(inputLine);
	         }

	         System.out.println("response--:" + response.toString());
	         System.out.println("responce :1+" + con.getResponseMessage());
	         in.close();
	      } catch (Exception var16) {
	         var16.printStackTrace();
	      }

	      return response.toString();
	   }
	
}

