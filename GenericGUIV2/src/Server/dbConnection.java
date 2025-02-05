package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

public class dbConnection {

	static Map res = ReadPropertyFile.getPropertyValues();
	static String dbInstanceUrl = (String) res.get("db_url");
	static String dbUser = (String) res.get("db_user");
	static String dbname = (String) res.get("db_name");
	static String dbPassword = (String) res.get("db_password");
	
	public static Connection getDbConnection() 
	{
		Connection con = null;
		try {
		  	String connectionString = "jdbc:sqlserver://"+dbInstanceUrl+";databaseName="+dbname+";integratedSecurity=true";
		            try {
		        		
		        	con = DriverManager.getConnection(connectionString) ;
		            if (con != null) {
		                System.out.println("Connected to the database!");
		                
		            }
		        } catch (SQLException e) {
		            System.out.println("Connection failed: " + e.getMessage());
		        }
			} catch (Exception e) {
			e.printStackTrace();
		}
		return con;

	}
	
	
	
	public static void insertParaValue(List testCode ,List testValue,String sampleno) 
	{
		

        // SQL insert statement
        String sql = "INSERT INTO hmit_result_dtl (hminum_machine_recordid, hminum_machine_id, gnum_hospital_code,hmistr_machine_sampleno, hmistr_machine_test_code, hmistr_result) VALUES (?,?, ?, ?, ?, ?)";
        // Get current timestamp and add 1 hour to it
        // Get current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Add 1 hour to the current date and time
        LocalDateTime modifiedDateTime = currentDateTime.plusHours(1);

        // Format the date and time as DDMMYYHHMMSS
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyHHmm");
        String formattedTimestamp = modifiedDateTime.format(formatter); // Timestamp in DDMMYYHHMMSS format
        //rohitmalviya\\SQLEXPRESS03
        	String connectionString = "jdbc:sqlserver://"+"DRDEEPAKAGARWAL\\SQLEXPRESS"+";databaseName="+"NeuroTrauma"+";integratedSecurity=true";
        try {
    		
        	Connection conn = DriverManager.getConnection(connectionString) ;
        	try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            // Prepare statement
            PreparedStatement pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < testCode.size(); i++) {
                // Set values for the query
            	
            int id =Integer.parseInt(sampleno);
                pstmt.setInt(1, id++); // Set hminum_machine_recordid   //need to store any unique id either timestamp+1 or we can delete that column // not mandatoray
                pstmt.setInt(2, 10001); // Set hminum_machine_id
                pstmt.setInt(3, 20001); // Set gnum_hospital_code
                pstmt.setString(4, sampleno);
                pstmt.setString(5, testCode.get(i).toString()); // Set hmistr_machine_test_code
                pstmt.setString(6, testValue.get(i).toString()); // Set hmistr_result

                // Execute the insert query
                pstmt.executeUpdate();
            }

            System.out.println("Data inserted successfully for Samle NO:   !"+sampleno);
            
            GenericServer.saveToFile("Data inserted successfully for Samle NO:   !"+sampleno,GenericServer.FILE_NAME);
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
	}
	
	
	
	
	
	
	
	
	
	
}
