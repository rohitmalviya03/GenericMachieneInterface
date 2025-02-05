package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.io.*;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class abc_old {
	
	public static void main(String args[]){
		String res = getSampleDtl("100002","27916","1012000","10716");
		String res2 = insertSampleDtl("100001","10911","TSH","6.96","1012000","10716");
		System.out.println(res);
		System.out.println(res2);		
	}	
	
	public static String getSampleDtl(String eqp, String hos, String sam, String uid ){
		String testCode= null;
		String patName= null;
		String patAge= null;
		String pat_Gender= null; 
		String sample_id= null;
		
		Map res = ReadPropertyFile.getPropertyValues();
		String ip = (String) res.get("ip");
		String port = (String) res.get("port");
		
		String url = "https://"+ip+"/HISInvestigationG5/new_investigation/masters/MachineInterface.jsp?type=2&eqp="+eqp+"&hos="+hos+"&sam="+sam+"&uid="+uid;
		try{
			
			SSLContext ssl_ctx = SSLContext.getInstance("TLS");
            TrustManager[ ] trust_mgr = get_trust_mgr();
            ssl_ctx.init(null,                
                         trust_mgr,         
                         new SecureRandom()); 
            HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
			
			
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			
			con.setHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String host, SSLSession sess) {
                    if (host.equals("220.156.188.146")) return true;
                    else return false;
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
			StringBuffer response = new StringBuffer();
			
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			System.out.println("response "+response.toString());
			in.close();
			String[] arrOfRes = response.toString().split("#");
			testCode = arrOfRes[0];
			System.out.println(testCode);
			patName = arrOfRes[1];
			System.out.println(patName);
			patAge = arrOfRes[2];
			System.out.println(patAge);
			pat_Gender = arrOfRes[3];
			System.out.println(pat_Gender);
			sample_id = arrOfRes[4];
			System.out.println(sample_id);			
		}
		catch(Exception s)
		{
			s.printStackTrace();
		}		
		return("Return value");
	}

	
	public static String insertSampleDtl(String eqp, String hos, String tcode, String tval, String sam, String uid){
		
		Map res = ReadPropertyFile.getPropertyValues();
		String ip = (String) res.get("ip");
		String port = (String) res.get("port");
		
		String url = "https://"+ip+"/HISInvestigationG5/new_investigation/masters/MachineInterface.jsp?type=1&eqp="+eqp+"&hos="+hos+"&tcode="+tcode+"&tval="+tval+"&sam="+sam+"&uid="+uid;
		StringBuffer response = new StringBuffer();
		try{
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
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
			System.out.println("response "+response.toString());
			in.close();		
		}
		catch(Exception s)
		{
			s.printStackTrace();
		}
		return (response.toString());
		
	}
	
	 private static TrustManager[ ] get_trust_mgr() {
	     TrustManager[ ] certs = new TrustManager[ ] {
	        new X509TrustManager() {
	           public X509Certificate[ ] getAcceptedIssuers() { return null; }
	           public void checkClientTrusted(X509Certificate[ ] certs, String t) { }
	           public void checkServerTrusted(X509Certificate[ ] certs, String t) { }
	         }
	      };
	      return certs;
	  }
	 
	 
	 public static List<String> st_par(String parse_this) {
			
			String temp_arr[]= parse_this.split(" ");
			String line = temp_arr[3];
			
			String temp_arr1[]=line.split("\\|");
			String id = temp_arr1[2];
			
			List<String> list = new ArrayList<>();
			list.add(id);
			
			for (int i=0;i<=23;i++) {
				String temp=temp_arr[i+19];
				String[] temp_arr3=temp.split("\\|");
				String temp2=temp_arr3[2];
				String requiredString = temp2.substring(temp2.indexOf("^") + 1, temp2.indexOf("^^"));
				list.add(requiredString);
				list.add(temp_arr3[3]);
			}
			
			return (list);
		}
}
