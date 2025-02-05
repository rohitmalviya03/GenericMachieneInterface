

package server;

import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.sql.PreparedStatement;
import java.sql.Connection;

public class HelperMethodsDAO
{
	public static ResourceBundle loadPropertiesFile(String _fileName) throws Exception
	{
		//System.out.println("fileName::::::::::::::::::::::::::::");
		String BUNDLE_NAME = _fileName;
		System.out.println("fileName" + _fileName);
		ResourceBundle rb = ResourceBundle.getBundle(BUNDLE_NAME);
		try
		{

		}
		catch (MissingResourceException e)
		{
			//  return '!' +"database"+ '!';
		}

		/*  System.out.println("_fileName::"+_fileName);
		 String _fileName12="E:\\WorkSpace28-09-07WithRenewal\\AHIMS\\src\\his\\global\\dao\\mastersQuery.properties";		    
		 String _fileName1="E:\\WorkSpace28-09-07WithRenewal\\AHIMS\\src\\his\\global\\dao\\registrationQuery.properties";
		String file=RegistrationConfig.QUERY_FILE_FOR_MASTERSDAO;
		 System.out.println("_fileNames:  "+_fileName12);
		Properties properties = new Properties();	 
		System.out.println("sdsd");
		properties.load(new FileInputStream(_fileName12.trim())); 
		properties.load(new FileInputStream(_fileName1.trim()));
		//properties.load(new FileInputStream(file.trim()));
		System.out.println("_fileName:  "+_fileName12);
		OutputStream os=new FileOutputStream("D:\\file_prop_check");
		properties.list(new PrintStream(os));
		OutputStream os1=new ByteArrayOutputStream();
		properties.list(new PrintStream(os1));
		ByteArrayOutputStream bos=(ByteArrayOutputStream)os1;
		byte[] bb=bos.toByteArray();
		ByteArrayInputStream bis=new ByteArrayInputStream(bb);
		PropertyResourceBundle prb=new PropertyResourceBundle(bis);
		String key=prb.getString("SELECT.HRGT_EPISODE_DTL.EMG_EPISODE.RETRIEVE_BY_CRNO");
		System.out.println("key------  "+key);*/
		return rb;
	}

	public static List getAlOfEntryObjects(ResultSet rs) throws SQLException
	{
		rs.beforeFirst();
		List alRecord = new ArrayList();
		ResultSetMetaData rsmd = rs.getMetaData();
		rs.beforeFirst();
		//System.out.println("hi...." + rsmd.getColumnCount());
		if (rsmd.getColumnCount() > 1)
		{
			while (rs.next())
			{
				Entry objEntry = new Entry();
				objEntry.setLabel(rs.getString(2));
				objEntry.setValue(rs.getString(1));
				//System.out.println("Entry: " + objEntry);
				alRecord.add(objEntry);
			}
		}
		else
		{
			while (rs.next())
			{
				Entry objEntry = new Entry();
				objEntry.setLabel(rs.getString(1));
				objEntry.setValue(rs.getString(1));
				//System.out.println("Entry: " + objEntry);
				alRecord.add(objEntry);
			}
		}
		return alRecord;
	}

	// added by yogender yadav on 11may2018 for 4entryAll
	public static List getAllOfEntryObjects(ResultSet rs) throws SQLException
	{
		rs.beforeFirst();
		List alRecord = new ArrayList();
		ResultSetMetaData rsmd = rs.getMetaData();
		rs.beforeFirst();
		//System.out.println("hi...." + rsmd.getColumnCount());
		if (rsmd.getColumnCount() > 1)
		{
			while (rs.next())
			{
				EntryAll objEntry = new EntryAll();
				objEntry.setValue1(rs.getString(1));
				objEntry.setValue2(rs.getString(2));
				objEntry.setValue3(rs.getString(3));
				objEntry.setValue4(rs.getString(4));
				//System.out.println("Entry: " + objEntry);
				alRecord.add(objEntry);
			}
		}
		else
		{
			while (rs.next())
			{
				EntryAll objEntry = new EntryAll();
				objEntry.setValue1(rs.getString(1));
				objEntry.setValue2(rs.getString(2));
				objEntry.setValue3(rs.getString(3));
				objEntry.setValue4(rs.getString(4));
				//System.out.println("Entry: " + objEntry);
				alRecord.add(objEntry);
			}
		}
		return alRecord;
	}
	
	// added by yogender yadav on 14June2018 for 5entryAll
		public static List getFiveOfEntryObjects(ResultSet rs) throws SQLException
		{
			rs.beforeFirst();
			List alRecord = new ArrayList();
			ResultSetMetaData rsmd = rs.getMetaData();
			rs.beforeFirst();
			//System.out.println("hi...." + rsmd.getColumnCount());
			if (rsmd.getColumnCount() > 1)
			{
				while (rs.next())
				{
					EntryAll objEntry = new EntryAll();
					objEntry.setValue1(rs.getString(1));
					objEntry.setValue2(rs.getString(2));
					objEntry.setValue3(rs.getString(3));
					objEntry.setValue4(rs.getString(4));
					objEntry.setValue5(rs.getString(5));
					//System.out.println("Entry: " + objEntry);
					alRecord.add(objEntry);
				}
			}
			else
			{
				while (rs.next())
				{
					EntryAll objEntry = new EntryAll();
					objEntry.setValue1(rs.getString(1));
					objEntry.setValue2(rs.getString(2));
					objEntry.setValue3(rs.getString(3));
					objEntry.setValue4(rs.getString(4));
					objEntry.setValue5(rs.getString(5));
					//System.out.println("Entry: " + objEntry);
					alRecord.add(objEntry);
				}
			}
			return alRecord;
		}
		
	public static List getAlOfEntryObjectsCallable(ResultSet rs) throws SQLException
	{
		// rs.beforeFirst();
		List alRecord = new ArrayList();
		ResultSetMetaData rsmd = rs.getMetaData();
		//  rs.beforeFirst();
		//System.out.println("hi...." + rsmd.getColumnCount());
		if (rsmd.getColumnCount() > 1)
		{
			while (rs.next())
			{
				Entry objEntry = new Entry();
				objEntry.setLabel(rs.getString(2));
				objEntry.setValue(rs.getString(1));
				//System.out.println("Entry: " + objEntry);
				alRecord.add(objEntry);
			}
		}
		else
		{
			while (rs.next())
			{
				Entry objEntry = new Entry();
				objEntry.setLabel(rs.getString(1));
				objEntry.setValue(rs.getString(1));
				//System.out.println("Entry: " + objEntry);
				alRecord.add(objEntry);
			}
		}
		return alRecord;
	}

	public static List getAlOfResultSet(ResultSet rs) throws SQLException
	{
		rs.beforeFirst();
		List alRecord = new ArrayList();
		ResultSetMetaData rsmd = rs.getMetaData();
		rs.beforeFirst();
		//System.out.println("hi...." + rsmd.getColumnCount());
		while (rs.next())
		{
			for (int i = 1; i <= rsmd.getColumnCount(); i++)
				alRecord.add(rs.getString(i));
			/*Entry objEntry=new Entry();
			objEntry.setLabel(rs.getString(2));
			objEntry.setValue(rs.getString(1));
			System.out.println("Entry: "+objEntry);
			alRecord.add(objEntry);*/
		}
		return alRecord;
	}

	public static List getAllOfResultSetAsListColumnWise(ResultSet rs) throws SQLException
	{
		rs.beforeFirst();
		List outerList = new ArrayList();
		ResultSetMetaData rsmd = rs.getMetaData();
		rs.beforeFirst();
		//System.out.println("hi...." + rsmd.getColumnCount());
		int col = 0;
		while (rs.next())
		{
			List alRecord = new ArrayList();
			for (int i = 1; i <= rsmd.getColumnCount(); i++)
				alRecord.add(rs.getString(i));
			outerList.add(col, alRecord);
			col++;
			/*Entry objEntry=new Entry();
			objEntry.setLabel(rs.getString(2));
			objEntry.setValue(rs.getString(1));
			System.out.println("Entry: "+objEntry);
			alRecord.add(objEntry);*/
		}
		return outerList;
	}

	public static String getQuery(String _filename, String _queryKey) throws Exception
	{
		ResourceBundle rb = loadPropertiesFile(_filename);
		System.out.println("_queryKey" + _queryKey);
		String query = rb.getString(_queryKey);
		System.out.println("query in getQuery(String _filename,String _queryKey):: " + query);
		return query;
	}

	public static ResultSet executeQuery(Connection _conn, String _query) throws Exception
	{
		ResultSet rs = null;
		Statement st = null;
		st = _conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
	try{
		rs = st.executeQuery(_query);
		//System.out.println("inside execute() of class HelperMethodsDAO");
	}catch (Exception e) {
		e.printStackTrace();
	}
		return rs;
	}

	//Added By Ranjit on 13-April-2017 for getting resultset for query with arraylist params
	public static ResultSet executeQuery(Connection _conn, String _query, List paramList) throws Exception{
		ResultSet rs = null;
		PreparedStatement pst = null;
		System.out.println("_query=="+_query);
		System.out.println("parameter==="+paramList);
		pst = _conn.prepareStatement(_query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

		
		StringBuffer stb = new StringBuffer(_query);
		String[] value = new String[]
		{};
		value = (String[]) paramList.toArray(value);
		for (int i = 0; i < value.length; i++){
			int x = stb.indexOf("?");
			if (value[i] == null) stb.replace(x, x + 1, "null");
			else if (value[i].equals("")) stb.replace(x, x + 1, "\' \'");
			else stb.replace(x, x + 1, value[i]);

		}
		rs = pst.executeQuery();

		return rs;
	}
	
	
	
	
	
	
	public static ResultSet executeQuery(Connection _conn, String _query, Map _populateMap) throws Exception
	{
		ResultSet rs = null;
		//rs.TYPE_SCROLL_SENSITIVE;
		PreparedStatement pst = null;
		System.out.println("_query=="+_query);
		pst = _conn.prepareStatement(_query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		//System.out.println("inside executeQuery() of class HelperMethodsDAO");
		//System.out.println("size of map::" + _populateMap.size());
		StringBuffer stb = new StringBuffer(_query);
		Set set = _populateMap.keySet();
		//System.out.println("sd2");
		Iterator itr = set.iterator();
		//System.out.println("ww2");
		System.out.println("size of map::" + _populateMap.size());
		java.util.ArrayList al = new java.util.ArrayList();
		for (int i = 0; i < set.size(); i++)
		{
			al.add(null);
		}
		//int i=1;
		while (itr.hasNext())
		{
			//System.out.println("SDSDsdd");
			Integer objKey = (Integer) itr.next();
			//objKey.getClass();
			//System.out.println("SDSDsdd");
			String val = (String) _populateMap.get(objKey);
			//System.out.println("SDSDsdd1");
			System.out.println("[" + objKey.intValue() + ", " + val + "]");

			pst.setString(objKey.intValue(), val);
			al.set(objKey.intValue() - 1, val); //<><<<<<<<
			//i++;	
		}

		String[] value = new String[]
		{};
		value = (String[]) al.toArray(value);
		for (int i = 0; i < value.length; i++)
		{
			//System.out.println("value["+i+"]"+value[i]);
			int x = stb.indexOf("?");
			if (value[i] == null) stb.replace(x, x + 1, "null");
			else if (value[i].equals("")) stb.replace(x, x + 1, "\' \'");
			else stb.replace(x, x + 1, value[i]);

		}
		System.out.println("stb....  " + stb);

		System.out.println("al:   +   " + al); //<><<<<<<
		//System.out.println("after setting values for prepare st. in class helpermethodsDAo");
		rs = pst.executeQuery();
		//System.out.println("after executing query");
		return rs;
	}

	public static int excecuteUpdate(Connection _conn, String _query, Map _populateMap) 
	{
		PreparedStatement pst = null;
		//System.out.println("_conn:   +   " + _conn);
		System.out.println("_query:   +   " + _query);
		System.out.println("_populateMap:   +   " + _populateMap);
		int result=0;

		try{
		pst = _conn.prepareStatement(_query);
		//System.out.println("inside excecuteUpdate() of class HelperMethodsDAO");
		System.out.println("size of map::" + _populateMap.size());
		Set set = _populateMap.keySet();
		Iterator itr = set.iterator();
		java.util.ArrayList al = new java.util.ArrayList(); //<><<<<<<<
		for (int i = 0; i < set.size(); i++)
		{
			al.add(null);
		}
		//int i=1;
		// pst.setString(1,"1");
		//pst.setString(2,"2");
		//pst.setString(3,"1");
		///<<<<<<<
		// pst.setString(4,"03-May-2006");
		StringBuffer stb = new StringBuffer(_query);
		while (itr.hasNext())
		{
			Integer objKey = (Integer) itr.next();
			//objKey.getClass();
			//System.out.println();		
			String val = (String) _populateMap.get(objKey);
			System.out.println("[" + objKey.intValue() + ", " + val + "]");
			pst.setString(objKey.intValue(), val);
			al.set(objKey.intValue() - 1, val); //<><<<<<<<
			/*int x=objKey.intValue();
			//int x=stb.indexOf("?");
			stb.replace(x,x+1,val);*/
			//i++;	
		}
		System.out.println("al:   +   " + al); //<><<<<<<
		String[] value = new String[]
		{};
		value = (String[]) al.toArray(value);
		for (int i = 0; i < value.length; i++)
		{
			//System.out.println("value["+i+"]"+value[i]);
			int x = stb.indexOf("?");
			if (value[i] == null) stb.replace(x, x + 1, "null");
			else if (value[i].equals("")) stb.replace(x, x + 1, "\' \'");
			else stb.replace(x, x + 1, value[i]);

		}
		System.out.println("stb....  " + stb);
		System.out.println("al:   +   " + al); //<><<<<<<
		//System.out.println("after setting values for prepare st. in class helpermethodsDAo");

		result= pst.executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	
	//Added By Ranjit on 27062018 for throwing exception
	
	public static int excecuteUpdateThrows(Connection _conn, String _query, Map _populateMap, PreparedStatement pst ) throws Exception
	{
		
		System.out.println("_query:   +   " + _query);
		System.out.println("_populateMap:   +   " + _populateMap);
		int result=0;
		
		System.out.println("size of map::" + _populateMap.size());
		Set set = _populateMap.keySet();
		Iterator itr = set.iterator();
		java.util.ArrayList al = new java.util.ArrayList(); 
		for (int i = 0; i < set.size(); i++){
			al.add(null);
		}
		
		StringBuffer stb = new StringBuffer(_query);
		
		while (itr.hasNext()){
			Integer objKey = (Integer) itr.next();
			
					
			String val = (String) _populateMap.get(objKey);
			System.out.println("[" + objKey.intValue() + ", " + val + "]");
			pst.setString(objKey.intValue(), val);
			al.set(objKey.intValue() - 1, val); //<><<<<<<<
			
		}
		System.out.println("al:   +   " + al); //<><<<<<<
		String[] value = new String[]
		{};
		value = (String[]) al.toArray(value);
		for (int i = 0; i < value.length; i++)
		{
			
			int x = stb.indexOf("?");
			if (value[i] == null) stb.replace(x, x + 1, "null");
			else if (value[i].equals("")) stb.replace(x, x + 1, "\' \'");
			else stb.replace(x, x + 1, value[i]);

		}
		System.out.println("stb....  " + stb);
		System.out.println("al:   +   " + al); //<><<<<<<
		
		result= pst.executeUpdate();
		
		return result;
	}

	public static int excecuteUpdateForMasters(Connection _conn, String _query) throws SQLException
	{
		Statement st = null;
		//System.out.println("_conn:   +   " + _conn);
		System.out.println("_query:   +   " + _query);
		st = _conn.createStatement();
		int num = st.executeUpdate(_query);
		return num;
	}
	
	public static StringBuffer populateQuery(StringBuffer query_p, List<String> lstParams_p)
	{
		for (String param : lstParams_p)
		{
			int x = query_p.indexOf("?");
			if (param == null) query_p.replace(x, x + 1, "null");
			else if (param.equals("")) query_p.replace(x, x + 1, "\' \'");
			else query_p.replace(x, x + 1, param);
		}
		//System.out.println("strBuff....  " + strBuff);
		return query_p;
	}
}
