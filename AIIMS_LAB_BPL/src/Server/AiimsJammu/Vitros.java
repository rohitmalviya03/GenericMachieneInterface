package Server.AiimsJammu;

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

public class Vitros {
	
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
	        URL url = new URL(API_URL + "/api/v1/machine/order_details/"+AIIMSLAB.machineId+"?machineId="+AIIMSLAB.machineId);
	       
	        System.out.println(API_URL);
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

	    String insertSQL = "INSERT INTO vitros_sample_dtl (" +
	            "his_order_id, patage, labcode, testcode, deptcode, sampleNo, sampleCode, samcollDate, " +
	            "pataddress, patward, reqdno, patient_id, patient_fname, patient_mname, patient_lname, " +
	            "patient_sex, patdob, phone_number, email_id, patient_type, center_id, test_id, test_name, sampletypename, machineId, testparams) " +
	            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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

	            stmt.setString(1, obj1.getString("his_order_id"));
	            stmt.setString(2, obj1.getString("patage"));
	            stmt.setString(3, obj1.getString("labcode"));
	            stmt.setString(4, obj1.getString("testcode"));
	            stmt.setString(5, obj1.getString("deptcode"));
	            stmt.setString(6, obj1.getString("sampleNo"));
	            stmt.setString(7, obj1.getString("sampleCode"));
	            stmt.setString(8, obj1.getString("samcollDate"));
	            stmt.setString(9, obj1.getString("pataddress"));
	            stmt.setString(10, obj1.getString("patward"));
	            stmt.setString(11, obj1.getString("reqdno"));
	            stmt.setString(12, obj1.getString("patient_id"));
	            stmt.setString(13, obj1.getString("patient_fname"));
	            stmt.setString(14, obj1.getString("patient_mname"));
	            stmt.setString(15, obj1.getString("patient_lname"));
	            stmt.setString(16, obj1.getString("patient_sex"));
	            stmt.setString(17, obj1.getString("patdob"));
	            stmt.setString(18, obj1.getString("phone_number"));
	            stmt.setString(19, obj1.getString("email_id"));
	            stmt.setString(20, obj1.getString("patient_type"));
	            stmt.setString(21, obj1.getString("center_id"));
	            stmt.setString(22, obj1.getString("test_id"));
	            stmt.setString(23, obj1.getString("test_name"));
	            stmt.setString(24, obj1.getString("sampletypename"));
	            stmt.setString(25, obj1.getString("machineId"));
	            stmt.setString(26, obj1.getString("testparams"));

	            // Check if record exists - note SQLite syntax same as others
	            String selectQry = "SELECT * FROM vitros_sample_dtl WHERE reqdno=? AND sampleNo=? AND sample_status=1";

	            try (PreparedStatement pstmt = conn.prepareStatement(selectQry)) {
	                pstmt.setString(1, obj1.getString("reqdno"));
	                pstmt.setString(2, obj1.getString("sampleNo"));
	                ResultSet rdds = pstmt.executeQuery();

	                if (!rdds.next()) {
	                    int res = stmt.executeUpdate();

	                    if (res > 0) {
	                        // Generate HL7 message & send
	                        String orderHl7 = HL7MessageGenerator.generateHL7MessageVitros(
	                                obj1.getString("his_order_id"),
	                                obj1.getString("patient_id"),
	                                obj1.getString("patient_fname"),
	                                obj1.getString("patient_mname"),
	                                obj1.getString("patient_lname"),
	                                obj1.getString("patient_sex"),
	                                obj1.getString("patage"),
	                                obj1.getString("phone_number"),
	                                obj1.getString("email_id"),
	                                "",
	                                obj1.getString("patient_type"),
	                                "",
	                                obj1.getString("center_id"),
	                                "",
	                                obj1.getString("test_id"),
	                                obj1.getString("test_name"),
	                                "",
	                                "",
	                                obj1.getString("sampletypename"),
	                                obj1.getString("pataddress"),
	                                obj1.getString("samcollDate"),
	                                obj1.getString("sampleNo"),
	                                obj1.getString("sampleNo"),
	                                obj1.getString("testparams"));

	                  //      AIIMSLAB.saveToFile("Order HL7: " + orderHl7, FILE_NAME);

	                        int resStatus = ServerConnector.sendToVitrosServer(orderHl7);

	                        if (resStatus > 0) {
	                            String updDtl = "UPDATE vitros_sample_dtl SET sample_status = 1 WHERE reqdno = ? AND sampleNo = ?";
	                            try (PreparedStatement updatestmt = conn.prepareStatement(updDtl)) {
	                                updatestmt.setString(1, obj1.getString("reqdno"));
	                                updatestmt.setString(2, obj1.getString("sampleNo"));
	                                updatestmt.executeUpdate();
	                            }

	                            AIIMSLAB.saveToFile("Update Order Status for sample Number " + obj1.getString("sampleNo"), FILE_NAME);
	                        }
	                    }
	                } else {
	                    System.out.println("Data already present");
	                }
	            }
	        }

	    } catch (Exception e) {
	    	  AIIMSLAB.saveToFile("Stack Trace: " + AIIMSLAB.getStackTraceAsString(e), FILE_NAME);
	        e.printStackTrace();
	    }
	}

}
