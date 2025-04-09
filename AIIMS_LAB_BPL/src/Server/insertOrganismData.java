package Server;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class insertOrganismData {

 public static void insertParaValueJson(String sampleNo, String json) {

	  String JsonSql = "INSERT INTO hivt_organism_antibiotic_dtl_json " +
              "(HIVNUM_SAMPLE_NO, GNUM_HOSPITAL_CODE ,hstr_json_data,GDT_ENTRY_DATE) VALUES (?, ?, ?,  sysdate)";

      
      LocalDateTime currentDateTime = LocalDateTime.now();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      String formattedTimestamp = currentDateTime.format(formatter); // Timestamp in required format

      Connection con = null;
      PreparedStatement pstmt = null;

      try {
      	Class.forName("org.postgresql.Driver");
          // Establish connection
          con = DriverManager.getConnection("jdbc:postgresql://10.226.80.35:5444/hmis_aiims_patna", "hmisaiimsp", "hmisaiimsp");

          // Prepare statement
          pstmt = con.prepareStatement(JsonSql);
          
          pstmt.setLong(1, Long.parseLong(sampleNo)); // Set HIVNUM_SAMPLE_NO
        pstmt.setInt(2, 10001); // Set GNUM_HOSPITAL_CODE
        pstmt.setString(3, json); // Set HSTR_ORGANISM_CODE
        pstmt.executeUpdate();

         } catch (ClassNotFoundException e) {
          System.err.println("JDBC Driver not found. Make sure to include the driver in your classpath.");
          AIIMSLAB.saveToFile("Stack Trace: " + AIIMSLAB.getStackTraceAsString(e), AIIMSLAB.FILE_NAME);
			
          e.printStackTrace();
      } catch (SQLException e) {
          System.err.println("SQL Exception occurred:");
          AIIMSLAB.saveToFile("Stack Trace: " + AIIMSLAB.getStackTraceAsString(e), AIIMSLAB.FILE_NAME);
			
          e.printStackTrace();
      } 
      
}
	
    public static void insertParaValue(String sampleNo, String organismName,List<result_parameter> resultData) {
	
	// public static void insertParaValue(String sampleNo, String json) {
        // SQL insert statement
        String sql = "INSERT INTO HIVT_ORGANISM_ANTIBIOTIC_DTL " +
                     "(HIVNUM_SAMPLE_NO, GNUM_HOSPITAL_CODE, HSTR_ORGANISM_CODE, " +
                     "HSTR_ANTIBIOTIC_CODE, HSTR_MIC_VALUE, HSTR_RESULT, SYNC_FLAG, HSTR_SAMPLE_TYPE, GDT_ENTRY_DATE) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, sysdate)";

        
        String JsonSql = "INSERT INTO hivt_organism_antibiotic_dtl_json " +
                "(HIVNUM_SAMPLE_NO, GNUM_HOSPITAL_CODE ,hstr_json_data,GDT_ENTRY_DATE) VALUES (?, ?, ?,  sysdate)";

        
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTimestamp = currentDateTime.format(formatter); // Timestamp in required format

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
        	Class.forName("org.postgresql.Driver");
            // Establish connection
            con = DriverManager.getConnection("jdbc:postgresql://10.226.80.35:5444/hmis_aiims_patna", "hmisaiimsp", "hmisaiimsp");

            // Prepare statement
            pstmt = con.prepareStatement(sql);
           /*   Forjson store
            pstmt.setLong(1, Long.parseLong(sampleNo)); // Set HIVNUM_SAMPLE_NO
          pstmt.setInt(2, 10001); // Set GNUM_HOSPITAL_CODE
          pstmt.setString(3, json); // Set HSTR_ORGANISM_CODE
          pstmt.executeUpdate();
*/
            for (result_parameter param : resultData) {
                // Set values for the query
                pstmt.setLong(1, Long.parseLong(sampleNo)); // Set HIVNUM_SAMPLE_NO
                pstmt.setInt(2, 10001); // Set GNUM_HOSPITAL_CODE
                pstmt.setString(3, organismName); // Set HSTR_ORGANISM_CODE
                pstmt.setString(4, param.getAntiBioticCode()); // Set HSTR_ANTIBIOTIC_CODE
                System.out.println(param.getMiCValue());
                if(param.getMiCValue().equals("") ||param.getMiCValue()=="") {
                	param.setMiCValue(null);
                }
                pstmt.setString(5, param.getMiCValue()); // Set HSTR_MIC_VALUE
                pstmt.setString(6, param.getResult()); // Set HSTR_RESULT
                pstmt.setInt(7, 1); // Set SYNC_FLAG
                pstmt.setString(8, "BLOOD"); // Set HSTR_SAMPLE_TYPE

                // Execute the insert query
                pstmt.executeUpdate();
                System.out.println("Data inserted successfully for Sample NO: " + param.getPatient_id());
                AIIMSLAB.saveToFile("Data inserted successfully for Sample NO: " + param.getPatient_id(), AIIMSLAB.FILE_NAME);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found. Make sure to include the driver in your classpath.");
            AIIMSLAB.saveToFile("Stack Trace: " + AIIMSLAB.getStackTraceAsString(e), AIIMSLAB.FILE_NAME);
			
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("SQL Exception occurred:");
            AIIMSLAB.saveToFile("Stack Trace: " + AIIMSLAB.getStackTraceAsString(e), AIIMSLAB.FILE_NAME);
			
            e.printStackTrace();
        } finally {
            // Close resources in reverse order of opening
            try {
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    public static void sendPostRequest(String jsonData,String sampleno) {
       // String urlString = "http://10.226.28.174:8380/HISInvestigationG5/new_investigation/masters/MachineInterface.jsp?type=6&sampleno="+sampleno; // Replace with your endpoint
     
        try {
            // Create URL object
            //URL url = new URL(urlString);
        	   URL url = new URL(AIIMSLAB.aiimsUrl + "/api/v1/pacs/saveorganismresult");
      	     
               HttpURLConnection connection = null;

            connection = (HttpURLConnection) url.openConnection();

            // Set up the connection
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Write JSON data to the output stream
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonData.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Get the response code
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // Handle the response if needed (e.g., read input stream)

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            
        }
    }

    
}
