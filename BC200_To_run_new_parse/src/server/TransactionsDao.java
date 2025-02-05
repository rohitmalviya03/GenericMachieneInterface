package server;

import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class TransactionsDao {

	public  void save(String Test_code , String Test_val ,String mac_sampleNo , Connection conn)
	{
		System.out.println("In  save Method for inserting");
	String query = "";
	Map populateMAP = new HashMap();
	Sequence sq = new Sequence();
	long Eqp_ID = 100083;
	long Hos_ID = 101; 
	long User_ID= 10031;
	
	 
	//String queryKey ="INSERT.HBBT_BLDREQ_INDICATION_DTL";

	
	try {
		
		
		
		query = "INSERT INTO hmit_result_dtl(HMINUM_MACHINE_RECORDID,HMINUM_MACHINE_ID,GNUM_HOSPITAL_CODE,HMISTR_MACHINE_TEST_CODE,HMISTR_RESULT,"
				+ "HMISTR_MACHINE_SAMPLENO,HMIDT_RESULTENTRY_DATE,HMINUM_RESULTENTRY_BY,HMINUM_STATUS)values"
				+ "(PKG_MACHINE_INTERFACE.gen_resultID('"+ Eqp_ID +"','"+ Hos_ID +"'),'"+ Eqp_ID +"','"+ Hos_ID +"','"+ Test_code +"','"+ Test_val +"',"
				+ "'"+ mac_sampleNo +"',sysdate,'"+ User_ID +"','1') ";
		System.out.println("query");
		//query =  "INSERT INTO hmit_result_dtl(HMINUM_MACHINE_RECORDID,HMINUM_MACHINE_ID,GNUM_HOSPITAL_CODE,HMISTR_MACHINE_TEST_CODE,HMISTR_RESULT,HMISTR_MACHINE_SAMPLENO,HMIDT_RESULTENTRY_DATE,HMINUM_RESULTENTRY_BY,HMINUM_STATUS)values(PKG_MACHINE_INTERFACE.gen_resultID(?,?),?,?,?,?,?,sysdate,?,'1') ";
	} catch (Exception e) {
		/*throw new HisApplicationExecutionException(
				"HelperMethodsDAO.loadPropertiesFile(filename)OR getting query out of property file"
						+ e);*/
	}

	try {
		System.out.println("values");
		/*populateMAP.put(sq.next(), Eqp_ID);
		populateMAP.put(sq.next(), Hos_ID);
		populateMAP.put(sq.next(), Eqp_ID);
		populateMAP.put(sq.next(), Hos_ID);
		populateMAP.put(sq.next(), Test_code);
		populateMAP.put(sq.next(), Test_val);
		populateMAP.put(sq.next(), mac_sampleNo);
		populateMAP.put(sq.next(), User_ID);*/
		
		System.out.println("after values");
		
		
    	
        	
	} catch (Exception e) {
		/*throw new HisApplicationExecutionException(
				"IcdGroupMasterDAO.populateMAP::" + e);*/
	}
	try {
		
		System.out.println("connection"+conn);
		System.out.println("before function");
		HelperMethodsDAO.excecuteUpdate(conn,query, populateMAP);
		System.out.println("after function");
	} catch (Exception e) {
		/*System.out.println(e.getMessage());
		throw new HisDataAccessException("Exception While ADDING");*/
		e.printStackTrace();
		System.out.println("conn"+conn);
		
		try
		{
		conn.rollback();
		}
		catch(SQLException s)
		{
			System.out.println(s.getMessage());
			s.printStackTrace();
		}
	}
	finally
	{
		
	}

}
	
	public  void saveMachineData(Map mp , String sampleName)
	{
		System.out.println("/////////In SaveMachineData/////////"+sampleName);
		Connection conn =null;
		
		/*
		 * added by rahul prasad
		 * Class.forName("com.mysql.jdbc.Driver");  
Connection con=DriverManager.getConnection(  
"jdbc:mysql://localhost:3306/tcpip","root","root");  
		 */
		
		
		
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");  
			//conn  = DriverManager.getConnection("jdbc:oracle:thin:@172.16.13.181:1521:ahis","AHIS","ahis");
			//conn  = DriverManager.getConnection("jdbc:oracle:thin:@10.226.1.238:1521:ahis","pgi","pgi");
			//conn  = DriverManager.getConnection("jdbc:oracle:thin:@172.16.13.93:1521:ahis","AHIS","ahismanager");
			
			
			//added by rahulprasad
			
			conn  = DriverManager.getConnection("jdbc:oracle:thin:@10.10.10.54:5446:ahis","odisha_uat","od15hauat");
			
			//conn  = DriverManager.getConnection("jdbc:oracle:thin:@10.10.10.54:5446:odisha_uat","odisha_uat","od15hauat");
			
			
			
			if(conn != null){
				System.out.println("connection estabished with odisha_uat");
			}
			
			
			 
			
			//conn  = DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","root");
			
		List TestCode = (List)mp.get("TestCode");
		List TestList = (List)mp.get("TestValue");
		
		conn.setAutoCommit(false);
		
		for(int i=0;i<TestCode.size();i++)
		{
			//System.out.println("hiiiiii"+(String)TestCode.get(i)+"    "+(String)TestList.get(i) +"   "+sampleName);
			save((String)TestCode.get(i) ,  (String)TestList.get(i) ,    sampleName , conn);	
			
			
		}
		
		conn.commit();
		
		}
		catch(Exception e)
		{
		
			
			System.out.println("Inside savMachineData Process:----"+e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(conn!=null)
				{
					
					conn.close();
				}
			}
			catch(SQLException e)
			{
				
			}
		}
	}

	// Select Query
	public List selectTestCode(String sampleNo)
	{
		System.out.println("/////////In selectTestCode/////////"+sampleNo);
		Connection conn =null;
		Map populateMAP = new HashMap();
		List list = new ArrayList();
		String query = "SELECT hmtm.hmistr_machine_test_code AS machinetestcode FROM hmit_sample_dtl sam, hmit_machine_testpara_mst hmtm "
				+ "WHERE hmtm.gnum_hospital_code = sam.gnum_hospital_code			AND hmtm.hmistr_hmis_test_code = sam.hmistr_hmis_test_code "
				+ "AND hmtm.hminum_machine_id = sam.hminum_machine_id AND gnum_isvalid = 1			"
				+ "AND TO_DATE (sam.hmidt_collection_date) >= TO_DATE (SYSDATE) AND sam.gnum_hospital_code = 101 AND sam.hmistr_machine_sampleno = "
				+ "'"+sampleNo +"' AND sam.hminum_test_status = '1' and sam.hminum_machine_id='100083'";
		ResultSet rs = null;
		
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");  
			//conn  = DriverManager.getConnection("jdbc:oracle:thin:@172.16.13.181:1521:ahis","AHIS","ahis");
			//conn  = DriverManager.getConnection("jdbc:oracle:thin:@10.226.1.238:1521:ahis","pgi","pgi");
			//conn  = DriverManager.getConnection("jdbc:oracle:thin:@172.16.13.93:1521:ahis1","AHIS","ahismanager");
			
			//added by rahulprasad
			
		
			conn  = DriverManager.getConnection("jdbc:oracle:thin:@10.10.10.54:5446:odisha_uat","odisha_uat","od15hauat");

			//ended
			
			conn.setAutoCommit(false);
			//populateMAP.put(1, sampleNo);
			rs = HelperMethodsDAO.executeQuery(conn,query, populateMAP);
			if(!rs.next())
			{
				list = null;
				System.out.println("rs is null");
			}
			else
			{
				rs.beforeFirst();
				while(rs.next())
				{
					list.add(rs.getString(1));
					System.out.println("sampleCode-----"+rs.getString(1));
				}
			}
			System.out.println("RS Called Successs");
		}
		catch(SQLException e)
		{
			try
			{
				conn.rollback();
			}
			catch(Exception s)
			{
				s.printStackTrace();
			}
			System.out.println("Inside SelectTestCode:----"+e.getMessage());
			e.printStackTrace();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		finally
		{
			try
			{
				if(conn!=null)
				{
					conn.commit();
					conn.close();
				}
			}
			catch(SQLException e)
			{
				
			}
		}
		return list;
	}

}
