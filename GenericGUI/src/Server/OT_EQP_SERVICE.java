package Server;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;


public class OT_EQP_SERVICE {
	
	
	
	
	
	   // Define parameter IDs for different parameters
    private static final int PARAM_ID_HR = 147842;
    private static final int PARAM_ID_IBP_SYS = 150037;
    private static final int PARAM_ID_IBP_DIA = 150038;
    private static final int PARAM_ID_NIBP_SYS = 150045;
    private static final int PARAM_ID_NIBP_DIA = 150046;
    private static final int PARAM_ID_NIBP_MEAN = 150047;
    private static final int PARAM_ID_IBP_MEAN =150039 ;
    private static final int PARAM_ID_MEAN_TEMP1 = 150344;
    private static final int PARAM_ID_MEAN_TEMP2 = 150356;
    private static final int PARAM_ID_SPO2 = 150456;
    private static final int PARAM_ID_ETCO2 = 151708;
    private static final int PARAM_ID_MAC = 152872;
	
	
	
	
	
	
	
	
	
	
	
	
	
	static Map res = ReadPropertyFile.getPropertyValues();
	static String db_user = (String) res.get("db_user");
	static String db_url = (String) res.get("db_url");
	static String db_pwd = (String) res.get("db_pwd");
   public String saveData(String JsonString) throws JSONException {
	   String response=null;
       // String jsonString = "[{\"patient_id\":\"20202025\",\"param_id\":\"150046\",\"param_name\":\"MDC_PRESS_BLD_ART_PULM_DIA\",\"param_value\":\"9\",\"param_unit\":\"266016^MDC_DIM_MMHG^MDC\",\"param_referenceRange\":\"\",\"timestamp\":\"2024-09-03 12:01:18\",\"packettimestamp\":\"20240809141732.0000+0530\"},{\"patient_id\":\"20202025\",\"param_id\":\"150037\",\"param_name\":\"MDC_PRESS_BLD_ART_ABP_SYS\",\"param_value\":\"120\",\"param_unit\":\"266016^MDC_DIM_MMHG^MDC\",\"param_referenceRange\":\"\",\"timestamp\":\"2024-09-03 12:01:18\",\"packettimestamp\":\"20240809141732.0000+0530\"}]";
	   try {
	   System.out.println("Inside DAO");
        String url = "jdbc:postgresql://"+db_url;//+10.226.80.35:5444/hmis_aiims_patna"; // Replace with your PostgreSQL database URL
        String user = db_user;//"hmisaiimsp"; // Replace with your PostgreSQL username
        String password =db_pwd; //"hmisaiimsp"; // Replace with your PostgreSQL password
      //  String insertSQLforPGSQL = "INSERT INTO ot_equipment_data (HNUM_EQUIPMENT_ID, HRGNUM_PUK, HNUM_PARA_ID, HSTR_PARA_VALUE, DT_CAPTURED_DATE, GDT_ENTRY_DATE, GNUM_ISVALID) VALUES (?, ?, ?, ?, TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS'), DATE_TRUNC('second', CURRENT_TIMESTAMP), ?)";
        String insertSQLforDerbay	 = "INSERT INTO ot_equipment_data (HNUM_EQUIPMENT_ID, HRGNUM_PUK, HNUM_PARA_ID, HSTR_PARA_VALUE, DT_CAPTURED_DATE, GDT_ENTRY_DATE, GNUM_ISVALID) VALUES (?, ?, ?, ?, CAST(? AS TIMESTAMP), CURRENT_TIMESTAMP, ?)";
        		
        		//"INSERT INTO ot_equipment_data (HNUM_EQUIPMENT_ID, HRGNUM_PUK, HNUM_PARA_ID, HSTR_PARA_VALUE, DT_CAPTURED_DATE, GDT_ENTRY_DATE,GNUM_ISVALID) VALUES (?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'), TO_DATE(SYSDATE, 'YYYY-MM-DD HH24:MI:SS'), ?)";
        
        //System.out.println("AA");
        
        String parsedDate=null;
		
       
            // Load PostgreSQL JDBC driver
        	//Class.forName("org.postgresql.Driver");  for PostGresql Driver
        	Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        	  System.out.println("BBB");
            // Establish a connection to the PostgreSQL database
            try (Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/myDB;create=true");
                 PreparedStatement pstmt = conn.prepareStatement(insertSQLforDerbay)) {
            	 // System.out.println("CCC");
                JSONArray jsonArray = new JSONArray(JsonString);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    try {
           			 parsedDate=parseOriginalTimestamp(jsonObject.optString("packettimestamp"));
           			// System.out.println("HH"+parsedDate);
           		} catch (ParseException e1) {
           			// TODO Auto-generated catch block
           			e1.printStackTrace();
           		}
                   // System.out.println("DDD");
                  
                    
                    try {
                    	
                    	  BigInteger bigInt = new BigInteger(jsonObject.optString("patient_id"));
                          
                          // Use setObject for BigInteger
                         // pstmt.setObject(1, bigInt);
                    pstmt.setLong(1, 1001);
                    pstmt.setObject(2,   bigInt);
                    pstmt.setLong(3, Integer.parseInt(jsonObject.optString("param_id")));
                   // pstmt.setString(3, jsonObject.optString("param_name"));
                    pstmt.setString(4, jsonObject.optString("param_value"));
                    pstmt.setString(5, parsedDate);
                    pstmt.setLong(6, 1);
                  //  pstmt.setString(5, jsonObject.optString("param_unit"));
                   // pstmt.setString(6, jsonObject.optString("param_referenceRange"));
                   // System.out.println("EEEE");
                    int rowsAffected = pstmt.executeUpdate();
                    
                    
                    if (rowsAffected > 0) {
                        System.out.println("Data inserted successfully.");
                        response="Data inserted successfully.";
                        GenericServer.saveToFile("Data inserted successfully for CR No. :"+jsonObject.optString("patient_id") +" -- PARA ID :"+Integer.parseInt(jsonObject.optString("param_id")),GenericServer.FILE_NAME);
                    } else {
                    	response="No rows affected.";
                        System.out.println("No rows affected.");
                        GenericServer.saveToFile("No rows affected for CR NO. : "+jsonObject.optString("patient_id") +" -- PARA ID :"+Integer.parseInt(jsonObject.optString("param_id")),GenericServer.FILE_NAME);
                        GenericServer.logClear();
                        
                    
                    }
                   
                    }
                    catch (Exception e) {
						e.printStackTrace();
                    	// TODO: handle exception
					}
                    // Parse the timestamp to java.sql.Timestamp
                  //  java.sql.Timestamp sqlTimestamp = java.sql.Timestamp.valueOf(jsonObject.optString("timestamp"));
                 //   pstmt.setTimestamp(7, sqlTimestamp);
                    
                 //   pstmt.setString(8, jsonObject.optString("packettimestamp"));

                 //   pstmt.execute();//  addBatch(); // Add to batch
                  //  pstmt.addBatch();
                    
                   
                   
                }
// pstmt.executeBatch();
              //  conn.commit(); // Commit transaction
                

                    
                    Random rand = new Random();
                    Calendar calendar = Calendar.getInstance();
/*
               
                   //to insert data manullly for 1hr 
                    try {
//                        conn = DriverManager.getConnection(url, user, password);
//
//                        // Prepare SQL statement
//                        String sql = "INSERT INTO ot_equipment_data (HNUM_EQUIPMENT_ID, HRGNUM_PUK, HNUM_PARA_ID, HSTR_PARA_VALUE, DT_CAPTURED_DATE, GDT_ENTRY_DATE, GNUM_ISVALID) VALUES (?, ?, ?, ?, TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS'), DATE_TRUNC('second', CURRENT_TIMESTAMP), ?)";
//                        pstmt = conn.prepareStatement(sql);

                      

                        // Example fixed values for HNUM_EQUIPMENT_ID and HRGNUM_PUK
                        int equipmentId = 1001;
                        int puk = 12345789;

                        for (int j = 0; j < 3600; j++) { // 3600 seconds in 1 hour
                            calendar.add(Calendar.SECOND, 1);
                            Timestamp capturedDate = new Timestamp(calendar.getTimeInMillis());

                            // Generate and insert data for different parameters
                            insertParameterData(pstmt, equipmentId, puk, PARAM_ID_HR, generateHR(rand), capturedDate, 1);
                            insertParameterData(pstmt, equipmentId, puk, PARAM_ID_IBP_SYS, generateIBPSys(rand), capturedDate, 1);
                            insertParameterData(pstmt, equipmentId, puk, PARAM_ID_IBP_DIA, generateIBPDia(rand), capturedDate, 1);
                            insertParameterData(pstmt, equipmentId, puk, PARAM_ID_NIBP_SYS, generateNIBPSys(rand), capturedDate, 1);
                            insertParameterData(pstmt, equipmentId, puk, PARAM_ID_NIBP_DIA, generateNIBPDia(rand), capturedDate, 1);
                            insertParameterData(pstmt, equipmentId, puk, PARAM_ID_NIBP_MEAN, generateNIBPMean(rand), capturedDate, 1);
                            insertParameterData(pstmt, equipmentId, puk, PARAM_ID_IBP_MEAN, generateIBPO(rand), capturedDate, 1);
                            insertParameterData(pstmt, equipmentId, puk, PARAM_ID_MEAN_TEMP1, generateTEMP1(rand), capturedDate, 1);
                            insertParameterData(pstmt, equipmentId, puk, PARAM_ID_MEAN_TEMP2, generateTEMP2(rand), capturedDate, 1);
                            insertParameterData(pstmt, equipmentId, puk, PARAM_ID_SPO2, generateSPO2(rand), capturedDate, 1);
                            insertParameterData(pstmt, equipmentId, puk, PARAM_ID_ETCO2, generateETCO2(rand), capturedDate, 1);
                            insertParameterData(pstmt, equipmentId, puk, PARAM_ID_MAC, generateMAC(rand), capturedDate, 1);

                            pstmt.executeBatch(); // Execute batch after each record to ensure data is committed
                        }

                        System.out.println("Data generated and inserted successfully.");

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (pstmt != null) pstmt.close();
                            if (conn != null) conn.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } 
                    
                }
                
              */
                    
               // pstmt.executeBatch(); // Execute batch insert

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
		return response;
		
   }
   
   
   private String  parseOriginalTimestamp(String timestamp) throws ParseException {
	      
   	
  	 DateTimeFormatter inputFormatter = new DateTimeFormatterBuilder()
  	            .appendPattern("yyyyMMddHHmmss")
  	            .optionalStart()
  	            .appendLiteral('.')
  	            .appendPattern("SSSS")
  	            .optionalEnd()
  	            .appendOffset("+HHmm", "+0000")
  	            .toFormatter();
  	        
  	        // Parse the input date-time string to OffsetDateTime
  	        OffsetDateTime offsetDateTime = OffsetDateTime.parse(timestamp, inputFormatter);
  	        
  	        // Convert to LocalDateTime by removing the offset
  	        LocalDateTime localDateTime = offsetDateTime.toLocalDateTime();
  	        
  	        // Define the output date-time formatter
  	        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  	        
  	        // Format the LocalDateTime to the desired output format
  	        String outputDateStr = localDateTime.format(outputFormatter);
  	        
  	return outputDateStr;
  	
  }
   
   
   
   
   
   
   
   
   
   private static void insertParameterData(PreparedStatement pstmt, int equipmentId, int puk, int paraId, String paraValue, Timestamp capturedDate, int isValid) throws SQLException {
       pstmt.setInt(1, equipmentId); // HNUM_EQUIPMENT_ID
       pstmt.setInt(2, puk); // HRGNUM_PUK
       pstmt.setInt(3, paraId); // HNUM_PARA_ID
       pstmt.setString(4, paraValue); // HSTR_PARA_VALUE
       pstmt.setTimestamp(5, capturedDate); // DT_CAPTURED_DATE
       pstmt.setInt(6, isValid); // GNUM_ISVALID

       pstmt.addBatch();
   }

   // Methods to generate random data
   private static String generateHR(Random rand) {
       return String.valueOf( 60 + rand.nextInt(41)); // HR between 60 and 100
   }

   private static String generateIBPSys(Random rand) {
       return String.valueOf( 100 + rand.nextInt(41)); // IBP SYS between 100 and 140
   }

   private static String generateIBPDia(Random rand) {
       return String.valueOf( 70 + rand.nextInt(31)); // IBP DIA between 70 and 100
   }

   private static String generateNIBPSys(Random rand) {
       return String.valueOf( 90 + rand.nextInt(41)); // NIBP SYS between 90 and 130
   }

   private static String generateNIBPDia(Random rand) {
       return String.valueOf( 60 + rand.nextInt(31)); // NIBP DIA between 60 and 90
   }

   private static String generateNIBPMean(Random rand) {
       return String.valueOf( 70 + rand.nextInt(31)); // NIBP MEAN between 70 and 100
   }

   private static String generateIBPO(Random rand) {
       return String.format("%.1f", 4.0 + rand.nextFloat() * 2.0); // IBPO between 4.0 and 6.0
   }

   private static String generateTEMP1(Random rand) {
       return String.format("%.1f", 96.8 + rand.nextFloat() * 3.6); // TEMP1 between 96.8 and 100.4 (approx. 36.0°C to 38.0°C)
   }

   private static String generateTEMP2(Random rand) {
       return String.format("%.1f", 96.8 + rand.nextFloat() * 3.6); // TEMP2 between 96.8 and 100.4 (approx. 36.0°C to 38.0°C)
   }

   private static String generateSPO2(Random rand) {
       return String.valueOf( 95 + rand.nextInt(6)); // SPO2 between 95 and 100
   }

   private static String generateETCO2(Random rand) {
       return String.valueOf( 30 + rand.nextInt(11)); // ETCO2 between 30 and 40
   }

   private static String generateMAC(Random rand) {
       return String.format("%.1f", 1.0 + rand.nextFloat() * 0.5); // MAC between 1.0 and 1.5
   }
   
   
   
   
   
   
   
   
   
   
   
    }

