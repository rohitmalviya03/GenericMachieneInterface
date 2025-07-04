package Server.Mic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import Server.AIIMSLAB;
import Server.HL7MessageGenerator;
import Server.ReadPropertyFile;
import Server.ServerConnector;

public class MestriaClient {
	
	static Map res = ReadPropertyFile.getPropertyValues();
	static String API_URL = (String) res.get("API_URL");
	static final String FILE_NAME = "./machineLog.txt"; // File to save the data

	
	
	public void getMsg() {
		
		
		System.out.println("Working ");
	}
	
	
	
	
	
	
	
	
	public void fetchOrderDetailsFromAPI() {
	    AIIMSLAB.saveToFile("call service to fetch data :", FILE_NAME);

	    HttpURLConnection conn = null;
	    BufferedReader reader = null;

	    try {
	        URL url = new URL(API_URL + "/api/v1/machine/mb/orderdetails/?machineId="+AIIMSLAB.machineId);
	       
	      //  System.out.println(API_URL);
	        conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setConnectTimeout(5000); // 5 seconds timeout for connection
	        conn.setReadTimeout(5000);    // 5 seconds timeout for reading data

	        int responseCode = conn.getResponseCode();
	        if (responseCode != 200) {
	            System.err.println("Failed : HTTP error code : " + responseCode);
	            return; // Exit early, no retry here but scheduler will call again next time
	        }
	        
	        
	        if (responseCode == HttpURLConnection.HTTP_OK) { // 200
                // Read the response
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                // Print the response
                System.out.println("Response from API: " + response.toString());
                AIIMSLAB.saveToFile("Response from API: " + response.toString(), FILE_NAME);
                try {
               // ObjectMapper mapper = new ObjectMapper();
                // You can deserialize JSON here if needed:
                String json = response.toString();
                AIIMSLAB.saveToFile(json, FILE_NAME);
               // List<OrderDetail> details = mapper.readValue(json, new TypeReference<List<OrderDetail>>() {});
                storeOrderDetailsInDB(json);
                }
                catch (Exception e) {
                	  AIIMSLAB.saveToFile("Stack Trace: " + AIIMSLAB.getStackTraceAsString(e), FILE_NAME);
					e.printStackTrace();
                	// TODO: handle exception
				}
                
                
            }

	        
	        
	    } catch (Exception e) {
	    	  AIIMSLAB.saveToFile("Stack Trace: " + AIIMSLAB.getStackTraceAsString(e), FILE_NAME);
	        e.printStackTrace();
	    } finally {
	        try {
	            if (reader != null) reader.close();
	            if (conn != null) conn.disconnect();
	        } catch (IOException ioe) {
	            ioe.printStackTrace();
	        }
	    }
	}

	
	


	public static void storeOrderDetailsInDB(String orderDetails) throws Exception {
	    AIIMSLAB.saveToFile("Call Fun to Save Data in Local Db", FILE_NAME);

	    String insertSQL = "INSERT INTO mb_orders_dtl (hrgnum_puk, hivt_req_do,"
	    		+ " gnum_lab_code,"
	    		+ " gnum_test_code, hgnum_dept_code_reqd, "
	    		+ "his_order_id, hrgstr_fname, hrgstr_mname, hrgstr_lname, hivstr_age, "
	    		+ "patient_gender, hivnum_sample_no, hivnum_sample_type, hivt_pat_type, pat_sample_collection_date,"
	    		+ " patient_birth_date, org_test_status, order_created_at ,"
	    		+ "samplecode) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);\r\n"
	    		+ "";

	    // SQLite connection URL (adjust path as needed)
	    String sqliteDbUrl = "jdbc:sqlite:vitrossample.sqlite";

	    try (Connection conn = DriverManager.getConnection(sqliteDbUrl);
	         PreparedStatement stmt = conn.prepareStatement(insertSQL)) {

	        JSONArray jsonArray1 = new JSONArray(orderDetails);

	        for (int i = 0; i < jsonArray1.length(); i++) {
	            JSONObject obj1 = jsonArray1.getJSONObject(i);

	            // Printing values (optional)
	            System.out.println("his_order_id: " + obj1.getString("his_order_id"));
	            // ... print other fields similarly

	            stmt.setString(1, obj1.getString("patient_id")); // hrgnum_puk - numeric, you may need setLong or setNull if missing
	            stmt.setString(2, obj1.getString("reqdno")); // hivt_req_do - numeric
	            stmt.setString(3, obj1.getString("labcode"));           // gnum_lab_code
	            stmt.setString(4, obj1.getString("testcode"));          // gnum_test_code
	            stmt.setString(5, obj1.getString("deptcode"));          // hgnum_dept_code_reqd
	            stmt.setString(6, obj1.getString("his_order_id"));      // his_order_id
	            stmt.setString(7, obj1.getString("patient_fname"));     // hrgstr_fname
	            stmt.setString(8, obj1.getString("patient_mname"));     // hrgstr_mname
	            stmt.setString(9, obj1.getString("patient_lname"));     // hrgstr_lname
	            stmt.setString(10, obj1.getString("patage"));           // hivstr_age
	            stmt.setString(11, obj1.getString("patient_sex"));      // patient_gender
	            stmt.setString(12, obj1.getString("sampleNo"));         // hivnum_sample_no
	            stmt.setString(13, obj1.getString("sampletypename"));   // hivnum_sample_type
	            stmt.setString(14, obj1.getString("patient_type"));     // hivt_pat_type
	            stmt.setString(15, obj1.getString("samcollDate"));      // pat_sample_collection_date
	            stmt.setString(16, obj1.getString("patdob"));           // patient_birth_date
	            stmt.setString(17, "0"); // org_test_status - numeric, maybe 0 default or setLong
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
	            String formattedDate = sdf.format(new Date());
	            stmt.setString(18, formattedDate);
	          
	            stmt.setString(19, obj1.getString("sampleCode"));       // samplecode

	            
	            //
//	            stmt.setString(1, obj1.getString("his_order_id"));
//	            stmt.setString(2, obj1.getString("patage"));
//	            stmt.setString(3, obj1.getString("labcode"));
//	            stmt.setString(4, obj1.getString("testcode"));
//	            stmt.setString(5, obj1.getString("deptcode"));
//	            stmt.setString(6, obj1.getString("sampleNo"));
//	            stmt.setString(7, obj1.getString("sampleCode"));
//	            stmt.setString(8, obj1.getString("samcollDate"));
//	            stmt.setString(9, obj1.getString("pataddress"));
//	            stmt.setString(10, obj1.getString("patward"));
//	            stmt.setString(11, obj1.getString("reqdno"));
//	            stmt.setString(12, obj1.getString("patient_id"));
//	            stmt.setString(13, obj1.getString("patient_fname"));
//	            stmt.setString(14, obj1.getString("patient_mname"));
//	            stmt.setString(15, obj1.getString("patient_lname"));
//	            stmt.setString(16, obj1.getString("patient_sex"));
//	            stmt.setString(17, obj1.getString("patdob"));
//	            stmt.setString(18, obj1.getString("phone_number"));
//	            stmt.setString(19, obj1.getString("email_id"));
//	            stmt.setString(20, obj1.getString("patient_type"));
//	            stmt.setString(21, obj1.getString("center_id"));
//	            stmt.setString(22, obj1.getString("test_id"));
//	            stmt.setString(23, obj1.getString("test_name"));
//	            stmt.setString(24, obj1.getString("sampletypename"));
//	            stmt.setString(25, obj1.getString("machineId"));
//	            stmt.setString(26, obj1.getString("testparams"));

	            // Check if record exists - note SQLite syntax same as others
	            String selectQry = "SELECT * FROM mb_orders_dtl WHERE hivt_req_do=? AND hivnum_sample_no=? and org_test_status =1";

	            try (PreparedStatement pstmt = conn.prepareStatement(selectQry)) {
	                pstmt.setString(1, obj1.getString("reqdno"));
	                pstmt.setString(2, obj1.getString("sampleNo"));
	                ResultSet rdds = pstmt.executeQuery();

	                if (!rdds.next()) {
	                    int res = stmt.executeUpdate();

	                    if (res > 0) {
	                        // Generate HL7 message & send
	                    	
//	                    	   public static String generateOrderMessageDynamic(
//	                    	    		String crno,
//	                    	    		String fname,
//	                    	    		String mname,
//	                    	    		String lname,
//	                    	    		
//	                    	    		String gender,
//	                    	    		String sampleNumber,
//	                    	    		String specimenType,
//	                    	    		String Age) 
	                        String orderHl7 = HL7MessageGenerator.generateOrderMessageDynamic(
	                        		obj1.getString("patient_id"),
	                                obj1.getString("patient_fname"),
	                                obj1.getString("patient_mname"),
	                                obj1.getString("patient_lname"),
	                                obj1.getString("patient_sex"),
	                                obj1.getString("sampleNo"),
	                                obj1.getString("sampletypename"),
	                                obj1.getString("patage"));

	                  //      AIIMSLAB.saveToFile("Order HL7: " + orderHl7, FILE_NAME);
	                        System.out.println(orderHl7);
	                        int resStatus = ServerConnector.sendToVitrosServer(orderHl7);

	                        if (resStatus > 0) {
	                            String updDtl = "UPDATE mb_orders_dtl SET org_test_status = 1 WHERE hivt_req_do = ? AND hivnum_sample_no = ?";
	                            try (PreparedStatement updatestmt = conn.prepareStatement(updDtl)) {
	                                updatestmt.setString(1, obj1.getString("reqdno"));
	                                updatestmt.setString(2, obj1.getString("sampleNo"));
	                                updatestmt.executeUpdate();
	                            }

	                            AIIMSLAB.saveToFile("Update Order Status for sample Number " + obj1.getString("sampleNo"), FILE_NAME);
	                        }
	                    }
	                } else {
	                    System.out.println("Order Alerady proceed for Sample Id : "+ obj1.getString("sampleNo"));
	                    AIIMSLAB.saveToFile("Order Alerady proceed for Sample Id : "+ obj1.getString("sampleNo"), FILE_NAME);
	                }
	            }
	        }

	    } catch (Exception e) {
	    	  AIIMSLAB.saveToFile("Stack Trace: " + AIIMSLAB.getStackTraceAsString(e), FILE_NAME);
	        e.printStackTrace();
	    }
	}

}
