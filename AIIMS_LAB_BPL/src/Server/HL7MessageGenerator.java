package Server;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class HL7MessageGenerator {
	private static final char START_BLOCK = 0x0B; // <VT> (0B in HEX)
    private static final char END_BLOCK_1 = 0x1C; // <FS> (1C in HEX)
    private static final char END_BLOCK_2 = 0x0D; // <CR> (0D in HEX)
    public static String generateOrderMessageMB(String sampleNumber,String specimenType)   {
    	String MSG="";
    	
    	if(specimenType.equals("URIN")) {
    		specimenType="URIN^URIN";
    		
    	}
    	else if(specimenType.equals("BLOOD")) {
    		specimenType="BC^Blood Culture";
    	}
    	try {
    		
    	 MSG=START_BLOCK+"MSH|^~\\&|LIS|LAB|myla|BMX|DATETIME||OML^O33^OML_O33|MSG-20241007-101200-0377|P|2.5.1|||NE|AL||UNICODE UTF-8 \r"
    			+ "PID|||SAMPLEID||CDAC TEST^.^^^^^nill|CDAC TEST2|19681025||||nill^^nill^^nill^nill|||||S|\r"
    			+ "PV1||E|GW-NEW CUBICAL WARD-J^8-FCGW-J-29^^ AIIMS^^^^^8-FCGW-J-29^||||^||||||||||||SAMPLEID\r"
    			+ "SPM|1|SAMPLEID||SPECIMAN^99BMx|||^^|^^|||P^Patient^||||||DATETIME|\r"
    			+ "SAC||||24008676 \r"
    			+ "ORC|NW||||||||DATETIME\r"
    			+ "TQ1|||||||||R^^\r"
    			+ "OBR|1|714647||SU^SU^99BMx||||||^^||||||^|123456780"+END_BLOCK_1+END_BLOCK_2;
    	
    	   String currentTimestamp = generateCurrentTimestamp();
    	MSG=MSG.replaceAll("SAMPLEID", sampleNumber);
    	MSG=MSG.replaceAll("DATETIME", currentTimestamp);
    	MSG=MSG.replaceAll("SPECIMAN", specimenType);
    	
    	StringBuffer orderPacket = new StringBuffer();
    	}
    	
    	catch (Exception e) {
			e.printStackTrace();
    		// TODO: handle exception
		}
    	return MSG;
    	
    	
    }
    
    public static String generateOrderMessageDynamic(
    		String crno,
    		String fname,
    		String mname,
    		String lname,
    		
    		String gender,
    		String sampleNumber,
    		String specimenType,
    		String Age)   {
    	String MSG="";
    	
//    	if(specimenType.equals("1021")) {
//    		specimenType="URIN^URIN";
//    		
//    	}
//    	else if(specimenType.equals("1002")) {
//    		specimenType="BC^Blood Culture";
//    	}
    	try {
    		
    		String [] dd=null;
    		if(Age.contains("Yr")) {
    			
    			dd=Age.split(" ");
    		}
    		else {
    			
    			dd[0]=Age;
    		}
    		
    		 LocalDate today = LocalDate.now();
    	        LocalDate dob = today.minusYears(Integer.parseInt(dd[0]));

    	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    	        String dobStr = dob.format(formatter);

    	        System.out.println("DOB (estimated): " + dobStr);
    	 MSG=START_BLOCK+"MSH|^~\\&|LIS|LAB|myla|BMX|DATETIME||OML^O33^OML_O33|MSGCONTROLID|P|2.5.1|||NE|AL||UNICODE UTF-8 \r"
    			+ "PID|||CRNO||Fname^.^^^^^nill|CDAC TEST2|AGE|GENDER|||nill^^nill^^nill^nill|||||S|\r"
    			+ "PV1||E|GW-NEW CUBICAL WARD-J^8-FCGW-J-29^^ AIIMS^^^^^8-FCGW-J-29^||||^||||||||||||SAMPLEID\r"
    			+ "SPM|1|SAMPLEID||SPECIMAN^99BMx|||^^|^^|||P^Patient^||||||DATETIME|\r"
    			+ "SAC||||24008676 \r"
    			+ "ORC|NW||||||||DATETIME\r"
    			+ "TQ1|||||||||R^^\r"
    			+ "OBR|1|714647||SU^SU^99BMx||||||^^||||||^|123456780"+END_BLOCK_1+END_BLOCK_2;
    	
    	   String currentTimestamp = generateCurrentTimestamp();
    	MSG=MSG.replaceAll("SAMPLEID", sampleNumber);
    	MSG=MSG.replaceAll("CRNO", crno);
    	MSG=MSG.replaceAll("Fname", fname);
    	MSG=MSG.replaceAll("GENDER", gender);
    	MSG=MSG.replaceAll("DATETIME", currentTimestamp);
    	MSG=MSG.replaceAll("SPECIMAN", specimenType);
    	MSG=MSG.replaceAll("AGE", dobStr);
    	MSG=MSG.replaceAll("MSGCONTROLID", currentTimestamp);
    	StringBuffer orderPacket = new StringBuffer();
    	}
    	
    	catch (Exception e) {
			e.printStackTrace();
    		// TODO: handle exception
		}
    	return MSG;
    	
    	
    }
    
    
    //PACS HL 7 Message Generator
    
    
    
	// Generate HL7 Message based on sample ID
	static String generateHL7MessagePACS(String his_order_id, String pat_id, String pat_fname, String pat_mname,
			String pat_lname, String pat_gender, String patient_birth_date, String phone_number, String email_id,
			String patient_weight, String patient_type, String patient_history, String center_id, String modality,
			String test_id, String test_name, String referring_physician_id, String referring_physician_name,
			String radiologist_id, String technician_id,String visitNo,String msgControlId) {
		// Customize HL7 message generation based on your requirements

		/*
		 * 
		 * .[
		 * MSH|^~\&|HIS_APP|HIS_FACILITY|PACS_APP|PACS_FACILITY|DateTime||ORM^O01|
		 * MsgCtrlId_ORM|P|2.3
		 * PID|1||P0001||Smith^John^Doe^^||john.smith@example.com|M|||||C001^1990-05-14^
		 * CT Head Scan PV1|1|||||||R001^Dr. Alice Walker||||||||||OP||VisitNumber
		 * ORC|NW|||||||||||RAD001 OBR|1|1|555-0100|Outpatient^No significant medical
		 * history.||||||||||||||||||||75.50||||||||||
		 * 
		 * 
		 * 
		 */

		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");

		// Define the output format (yyyyMMdd)
		SimpleDateFormat outputFormat = new SimpleDateFormat("yyyyMMdd");

		Date currentDate = new Date();

		// Define the desired format
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		String formattedDate = sdf.format(currentDate);

		try {
			// Parse the input string into a Date object
			Date dobDate = inputFormat.parse(patient_birth_date);

			// Convert the Date object into the desired format
			patient_birth_date = outputFormat.format(dobDate);

			// Output the formatted date
			System.out.println("Formatted DOB (yyyyMMdd): " + patient_birth_date);

		} catch (Exception e) {
			e.printStackTrace();
		}

		String OrderMSg = START_BLOCK
				+ "MSH|^~\\&|HIS_APP|HIS_FACILITY|PACS_APP|PACS_FACILITY|DateTime||ORM^O01|MsgCtrlId_ORM|P|2.3\r"
				+ "PID|1||PatientID||PatientLast^PatientFirst^PatientMiddle^^Title||DateOfBirth|Gender|||||Phone^Email^History\r"
				+ "PV1|1|||||||RefPhyID^RefPhyName||||||||||OP||VisitNumber\r" + "ORC|NW|||||||||||RadiologistID\r"
				+ "OBR|1|OID|CenterID|TestID^TestName||||||||||||||||||||MODALITY||||||||||TechnicianID\r" + END_BLOCK_1
				+ END_BLOCK_2;

		// OrderMSg=OrderMSg.replaceAll("sampleId", sampleId);
		/*
		 * OrderMSg = OrderMSg.replaceAll("DateTime", formattedDate); OrderMSg =
		 * OrderMSg.replaceAll("OID", his_order_id); OrderMSg =
		 * OrderMSg.replaceAll("CenterID", center_id); OrderMSg =
		 * OrderMSg.replaceAll("PatientID", pat_id); OrderMSg =
		 * OrderMSg.replaceAll("PatientLast", pat_lname); OrderMSg =
		 * OrderMSg.replaceAll("PatientFirst", pat_fname); OrderMSg =
		 * OrderMSg.replaceAll("PatientMiddle", pat_mname); OrderMSg =
		 * OrderMSg.replaceAll("Title", ""); OrderMSg = OrderMSg.replaceAll("Gender",
		 * pat_gender); OrderMSg = OrderMSg.replaceAll("Phone", phone_number); OrderMSg
		 * = OrderMSg.replaceAll("History", patient_history); OrderMSg =
		 * OrderMSg.replaceAll("DateOfBirth", patient_birth_date); OrderMSg =
		 * OrderMSg.replaceAll("Email", email_id); OrderMSg =
		 * OrderMSg.replaceAll("PatientType", patient_type); OrderMSg =
		 * OrderMSg.replaceAll("MODALITY", modality); OrderMSg =
		 * OrderMSg.replaceAll("TestID", test_id); OrderMSg =
		 * OrderMSg.replaceAll("TestName", test_name); OrderMSg =
		 * OrderMSg.replaceAll("RefPhyID", referring_physician_id); OrderMSg =
		 * OrderMSg.replaceAll("RefPhyName", referring_physician_name); OrderMSg =
		 * OrderMSg.replaceAll("RadiologistID", radiologist_id); OrderMSg =
		 * OrderMSg.replaceAll("TechnicianID", technician_id);
		 */
		OrderMSg = OrderMSg.replaceAll("MsgCtrlId_ORM", formatValue(msgControlId));
		OrderMSg = OrderMSg.replaceAll("DateTime", formatValue(formattedDate));
		OrderMSg = OrderMSg.replaceAll("OID", formatValue(his_order_id));
		OrderMSg = OrderMSg.replaceAll("CenterID", formatValue(center_id));
		OrderMSg = OrderMSg.replaceAll("PatientID", formatValue(pat_id));
		OrderMSg = OrderMSg.replaceAll("PatientLast", formatValue(pat_lname));
		OrderMSg = OrderMSg.replaceAll("PatientFirst", formatValue(pat_fname));
		OrderMSg = OrderMSg.replaceAll("PatientMiddle", formatValue(pat_mname));
		OrderMSg = OrderMSg.replaceAll("Title", "-");
		OrderMSg = OrderMSg.replaceAll("Gender", formatValue(pat_gender));
		OrderMSg = OrderMSg.replaceAll("Phone", formatValue(phone_number));
		OrderMSg = OrderMSg.replaceAll("History", formatValue(patient_history));
		OrderMSg = OrderMSg.replaceAll("DateOfBirth", formatValue(patient_birth_date));
		OrderMSg = OrderMSg.replaceAll("Email", "");//formatValue(email_id));
		OrderMSg = OrderMSg.replaceAll("PatientType", formatValue(patient_type));
		OrderMSg = OrderMSg.replaceAll("MODALITY", formatValue(modality));
		OrderMSg = OrderMSg.replaceAll("TestID", formatValue(test_id));
		OrderMSg = OrderMSg.replaceAll("TestName", formatValue(test_name));
		OrderMSg = OrderMSg.replaceAll("RefPhyID", formatValue(referring_physician_id));
		OrderMSg = OrderMSg.replaceAll("RefPhyName", formatValue(referring_physician_name));
		OrderMSg = OrderMSg.replaceAll("RadiologistID", formatValue(radiologist_id));
		OrderMSg = OrderMSg.replaceAll("TechnicianID", formatValue(technician_id));
		OrderMSg = OrderMSg.replaceAll("VisitNumber", formatValue(visitNo));

		String oldOrderMSgTemplate = START_BLOCK   //This is not in use;
				+ "MSH|^~\\&|HIS_APP|HIS_HOSPITAL|PACS_APP|PACS_HOSPITAL|202410151230||ORM^O01|MSG001|P|2.3\r\n"
				+ "PID|1||123456||Doe^John^^Mr.||19800101|M|||||555-1234^john.doe@example.com^No relevant history\r\n"
				+ "PV1|1|||||||123^Dr. Smith||||||||||OP||50\r\n" + "ORC|NW|||||||||||789\r\n"
				+ "OBR|1|HOID123|Center456|TST001^CT Scan Head||||||||||||||||||||CR||||||||||TECH456\r\n" + "";

		
		OrderMSg = OrderMSg.replaceAll("null", "");

		return OrderMSg;

	}
    
    
	public static String generateAckResponseVitros(String receivedMessage) {
		// TODO Auto-generated method stub
			
			
		
		/*
		 * MSH|^~\&|IM|AIIMS Hospital Jammu|||20250612112011||ACK^O01|0|P|2.4|||NE|NE
		 * MSA|CA|20250612112011
		 */
			String[] msgPart = receivedMessage.split("\r");
			String[] mshPart = msgPart[0].split("\\|");
//MSH|^~\&|||IM||20180728010048||ACK|20180728010048232589|P|2.3|1||||91|||
			String ackTemplate =START_BLOCK+"MSH|^~\\&|IM|AIIMS Hospital Jammu|||YYYYMMDDHHMMSS||ACK^O01|MsgCtrlId_ORM|P|2.4\r"
					+ "MSA|AA|MsgCtrlId_ORM|Result Recived\r"+END_BLOCK_1+END_BLOCK_2;
			Date currentDate = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssZ");
			String formattedDate = dateFormat.format(currentDate);

			//if(mshPart[0].equals("MSH")) {
			String controlId = mshPart[9];  // Extract CONTROLID from received message
			ackTemplate = ackTemplate.replace("MsgCtrlId_ORM", controlId);
			ackTemplate = ackTemplate.replace("YYYYMMDDHHMMSS", formattedDate);
			//ackTemplate = ackTemplate.replace("YYYYMMDDHHMMSS", formattedDate);
			//ackTemplate = ackTemplate.replace("YYYYMMDDHHMMSS", formattedDate);
			
			
			
		return ackTemplate;
	}

    
	private static String formatValue(String value) {
	    return (value == null || value.trim().isEmpty()) ? "-" : value.trim();
	}

    
    
	static String generateHL7MessageHoriba(String sampleNo)
	{
    
	 String hl7Message=START_BLOCK+"MSH|^~\\&|LIS|LIS|HALIA|HALIA|20231004002512||OML^O33^OML_O33|20231004002512|P|2.5|||||||\r" + 
        		"PID|||230909H0696^^^LIS^PI||^^^||20230910||||^^^^^^||||||||||||||||||||N|AL\r" + 
        		"PV1||N|||||||||||||||||||||||||||||||||||||||||||\r" + 
        		"SPM|1|samid||BLOOD||||InterLink|||||||||202309100017||||||\r" + 
        		"ORC|NW||1|1|||||20231004002512||||3267||||||||^^^^^^^^^3267\r" + 
        		"TQ1|||||||20231004002512||R\r" + 
        		"OBR|1|1|1|CBC^CBC profile^LIS|||||||BLOOD||||||||||||||P\r" + 
        		"ORC|NW||1|1|||||20231004002512||||3267||||||||^^^^^^^^^3267\r" + 
        		"TQ1|||||||20231004002512||R\r" + 
        		"OBR|1|1|1|DIF^DIF profile^LIS|||||||BLOOD||||||||||||||P\r" +END_BLOCK_1+END_BLOCK_2 ;
        	
	 LocalDateTime now = LocalDateTime.now();
     
     // Define a custom date and time format
     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
     
     // Format the current date and time using the custom format
     String formattedTimestamp = now.format(formatter);
	Date timestamp = new Date();
	System.out.println("Current Time Stamp : "+formattedTimestamp);
 	 hl7Message = hl7Message.replaceAll("20231004002512", formattedTimestamp);
 	 hl7Message = hl7Message.replaceAll("230909H0696", sampleNo);
 	 hl7Message = hl7Message.replaceAll("samid", sampleNo);
   	
 	String newPatientId = sampleNo+"^^^LIS^PI";
     String newSpecimenId = sampleNo;
 
     
     return hl7Message;
}

    private static String generateCurrentTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date());
    }
    
    
    
    
    
    
    //Vitros IM
    
    public static String generateHL7MessageVitros(String his_order_id, String pat_id, String pat_fname, String pat_mname,
			String pat_lname, String pat_gender, String patient_birth_date, String phone_number, String email_id,
			String patient_weight, String patient_type, String patient_history, String center_id, String modality,
			String test_id, String test_name, String referring_physician_id, String referring_physician_name,
			String sampleType, String patAdd,String sampleCollDate,String msgControlId,String sampleNo,String testParams) {
		// Customize HL7 message generation based on your requirements

		Set<String> testCode = new HashSet<>();
		
		
  if(!testParams.equals(""))
  {
	  String[] str= testParams.split("#");
	  
	  for(String dd:str) {
		  testCode.add(dd);
	  }
	  
	  
  }
  
  System.out.println("Test Code ::  "+testCode);
  SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");

		// Define the output format (yyyyMMdd)
		SimpleDateFormat outputFormat = new SimpleDateFormat("yyyyMMdd");

		Date currentDate = new Date();

		// Define the desired format
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		String formattedDate = sdf.format(currentDate);

		try {
			// Parse the input string into a Date object
			Date dobDate = inputFormat.parse(patient_birth_date);

			// Convert the Date object into the desired format
			patient_birth_date = outputFormat.format(dobDate);

			// Output the formatted date
			System.out.println("Formatted DOB (yyyyMMdd): " + patient_birth_date);

		} catch (Exception e) {
			e.printStackTrace();
		}
		 SimpleDateFormat inputFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		 Date date = null;
		try {
			date = inputFormat.parse(sampleCollDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

			try {
				date = inputFormat.parse(sampleCollDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
		        // Output formatter for HL7 format
		        SimpleDateFormat hl7Format = new SimpleDateFormat("yyyyMMddHHmmss");
		        String smCollDate = hl7Format.format(date);

		
		

		String OrderMSg2 = START_BLOCK+
				"MSH|^~\\&|Arcus|1|EXTERNAL||DateTime||ORM^O01^ORM_O01|DateTime|P|2.4|100\r"
				 +"PID|||PatientID||PatientFirst^PatientLast^^||DateOfBirth|Gender|||&73/10 , THAATHA MUTHIAPPAN STREET, BROADWAY^^Chennai^Tamil Nadu^600001^^Default Address||^^^^^^7299992655|^^^^^^9840884948||^|||||||19580606|||||IND^INDIAN\r"
				+ "PV1|1|IP|LABO|INPAT|||||||||||||||OP1807246414^^^^IP180707644|ECHS POLYCLINIC- DEFENCE COLONY,ECHS POLYCLINIC- DEFENCE COLONY,SELFPAY||||||||||||||||||||||||20180724200110\r"
				 +"ORC|NW|18223292-1||18223292^^18223292|||||20180727224024|11357||11873^^CHITRAMBALAM S^^^Dr.|918^^^^^^^^NEURO ICU||||||11873^^CHITRAMBALAM S^^^Dr.\r"
				 +"OBR|1|18223292-1|18223292-1|UREA|Routine||20180727224110|||||||20180727230425|SER|11873^^CHITRAMBALAM S^^^Dr.||||||||||||||||||||20180727224024\r"
				 +"OBR|2|18223292-1|18223292-1|CREA|Routine||20180727224110|||||||20180727230425|SER|11873^^CHITRAMBALAM S^^^Dr.||||||||||||||||||||20180727224024\r"
				 +"OBR|3|18223292-1|18223292-1|NA|Routine||20180727224110|||||||20180727230425|SER|11873^^CHITRAMBALAM S^^^Dr.||||||||||||||||||||20180727224024\r"
				+ "OBR|4|18223292-1|18223292-1|K|Routine||20180727224110|||||||20180727230425|SER|11873^^CHITRAMBALAM S^^^Dr.||||||||||||||||||||20180727224024\r"
				 +"OBR|5|18223292-1|18223292-1|CL|Routine||20180727224110|||||||20180727230425|SER|11873^^CHITRAMBALAM S^^^Dr.||||||||||||||||||||20180727224024\r"
				 +"OBR|6|18223292-1|18223292-1|ECO2|Routine||20180727224110|||||||20180727230425|SER|11873^^CHITRAMBALAM S^^^Dr.||||||||||||||||||||20180727224024\r"
				+ END_BLOCK_2;

		if(sampleType.toUpperCase().contains("SERUM")) {
			sampleType="SER";
			
		}
		else	if(sampleType.toUpperCase().contains("CSF")) {
			sampleType="CSF";
			
		}
		else	if(sampleType.toUpperCase().contains("FLUID")) {
			sampleType="BDYF";
			
		}
		
		else if(sampleType.toUpperCase().contains("PLASMA")) {
			sampleType="PLAS";
			
		}
		else if(sampleType.toUpperCase().contains("BLOOD")) {
			sampleType="BLD";
			
		}
		else if(sampleType.toUpperCase().contains("URINE")) {
			sampleType="URI";
			
		}
		
		
		
		StringBuffer obrSeg =new StringBuffer();
		for (String data : testCode) {
		    String obrLine = "OBR|1|"
		        + "sampleno|"
		         +"sampleno|"
		        + data + "|Routine||"
		       +smCollDate+"|||||||"+smCollDate+"|"
		         +sampleType+ "|"
		        + "^^^^^||||||||||||||||||||20180727224024";

		    obrSeg.append(obrLine).append("\r"); // HL7 segments typically end with \r
		}
		String OrderMSg = START_BLOCK+
				"MSH|^~\\&|Arcus|1|EXTERNAL||DateTime||ORM^O01^ORM_O01|DateTime|P|2.4|100\r"
				 +"PID|||PatientID||PatientFirst^PatientLast^^||DateOfBirth|Gender|||^^^^||^^^^^^|^^^^^^||^|||||||DateOfBirth|||||^\r"
				+ "PV1|1|IP|LABO|INPAT|||||||||||||||OP1807246414^^^^IP180707644|||||||||||||||||||||||||20250209120627\r"
				 +"ORC|NW|sampleno||^^||||||||^^^^^|^^^^^^^^||||||^^^^^\r"
				 +obrSeg
				 //+"OBR|1|sampleno|sampleno|testName|Routine||CollDate|||||||recvTime|Sampletype|11873^^CHITRAMBALAM S^^^Dr.||||||||||||||||||||20180727224024"
				+ END_BLOCK_1+END_BLOCK_2;
		
		
		OrderMSg = OrderMSg.replaceAll("MsgCtrlId_ORM", formatValue(msgControlId));
		OrderMSg = OrderMSg.replaceAll("DateTime", formatValue(formattedDate));
		OrderMSg = OrderMSg.replaceAll("OID", formatValue(his_order_id));
		OrderMSg = OrderMSg.replaceAll("CenterID", formatValue(center_id));
		OrderMSg = OrderMSg.replaceAll("PatientID", formatValue(pat_id));
		OrderMSg = OrderMSg.replaceAll("PatientLast", formatValue(pat_lname));
		OrderMSg = OrderMSg.replaceAll("PatientFirst", formatValue(pat_fname));
		OrderMSg = OrderMSg.replaceAll("PatientMiddle", formatValue(pat_mname));
		OrderMSg = OrderMSg.replaceAll("Title", "-");
		OrderMSg = OrderMSg.replaceAll("Gender", formatValue(pat_gender));
		OrderMSg = OrderMSg.replaceAll("Phone", formatValue(phone_number));
		OrderMSg = OrderMSg.replaceAll("History", formatValue(patient_history));
		OrderMSg = OrderMSg.replaceAll("DateOfBirth", formatValue(patient_birth_date));
		OrderMSg = OrderMSg.replaceAll("Email", "");//formatValue(email_id));
		OrderMSg = OrderMSg.replaceAll("PatientType", formatValue(patient_type));
		OrderMSg = OrderMSg.replaceAll("MODALITY", formatValue(modality));
		OrderMSg = OrderMSg.replaceAll("TestID", formatValue(test_id));
		OrderMSg = OrderMSg.replaceAll("TestName", formatValue(test_name));
		OrderMSg = OrderMSg.replaceAll("RefPhyID", formatValue(referring_physician_id));
		OrderMSg = OrderMSg.replaceAll("RefPhyName", formatValue(referring_physician_name));
		OrderMSg = OrderMSg.replaceAll("sampleCollDate", formatValue(sampleCollDate));
		OrderMSg = OrderMSg.replaceAll("testName", formatValue(test_name));
		OrderMSg = OrderMSg.replaceAll("sampleno", formatValue(sampleNo));
		OrderMSg = OrderMSg.replaceAll("Sampletype", formatValue(sampleType));
		OrderMSg = OrderMSg.replaceAll("samCollDate", formatValue(sampleCollDate));
		
		
	//	OrderMSg+=END_BLOCK_2;
		String oldOrderMSgTemplate = START_BLOCK   //This is not in use;
				+ "MSH|^~\\&|HIS_APP|HIS_HOSPITAL|PACS_APP|PACS_HOSPITAL|202410151230||ORM^O01|MSG001|P|2.3\r\n"
				+ "PID|1||123456||Doe^John^^Mr.||19800101|M|||||555-1234^john.doe@example.com^No relevant history\r\n"
				+ "PV1|1|||||||123^Dr. Smith||||||||||OP||50\r\n" + "ORC|NW|||||||||||789\r\n"
				+ "OBR|1|HOID123|Center456|TST001^CT Scan Head||||||||||||||||||||CR||||||||||TECH456\r\n" + "";

		
		OrderMSg = OrderMSg.replaceAll("null", "");

		return OrderMSg;

	}
    
    
    
    
    
    
    
    
    
    
    
}
