/**
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for the
 * specific language governing rights and limitations under the License.
 *
 * The Original Code is "ExampleReceiverApplication.java".  Description:
 * "Example Code"
 *
 * The Initial Developer of the Original Code is University Health Network. Copyright (C)
 * 2001.  All Rights Reserved.
 *
 * Contributor(s): James Agnew
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * GNU General Public License (the  �GPL�), in which case the provisions of the GPL are
 * applicable instead of those above.  If you wish to allow use of your version of this
 * file only under the terms of the GPL and not to allow others to use your version
 * of this file under the MPL, indicate your decision by deleting  the provisions above
 * and replace  them with the notice and other provisions required by the GPL License.
 * If you do not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the GPL.
 *
 */
package ca.uhn.hl7v2.examples;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.uhn.hl7v2.examples.ABC;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.model.GenericMessage;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.CanonicalModelClassFactory;
import ca.uhn.hl7v2.parser.DefaultModelClassFactory;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.protocol.ReceivingApplication;
import ca.uhn.hl7v2.util.Terser;

/**
 * Application class for receiving ADT^A01 messages
 */
public class ExampleReceiverApplication implements ReceivingApplication<Message>
{

    /**
     * {@inheritDoc}
     */
    public boolean canProcess(Message theIn) {
        return true;
    }


    /**
     * {@inheritDoc}
     */
	public Message processMessage(Message theMessage, Map<String, Object> theMetadata) throws HL7Exception {
		Message hl7Message = null;
        String encodedMessage = new DefaultHapiContext().getPipeParser().encode(theMessage);
        String msg=null;
        System.out.println("Received message:\n" + encodedMessage + "\n\n");
 
    	Map<String, List> mp = new HashMap();  		//added by Rohit...
		List <String> testCode = new ArrayList<>();
		List <String> testValue = new ArrayList<>();
		List<String> sampleNo=new ArrayList<>();
		
        char cr = 13;

     	int count=0;
        String[] segments = encodedMessage.split("\r");
        String sampleName = null;
     // Print each segment
     
        String msgtype="";
        for (String segment : segments) {
         System.out.println("Segment: " + segment);
         

         
         
      	if (segment.startsWith("MSH")) {
      		String[] parts = segment.split("\\|");

      		
      		String value=parts[8];
      		String[] MSH = value.split("\\^");
      	
      		msgtype=MSH[0];
      		System.out.println("Incoming msg Type :"+msgtype);
      	}
         
         
         
         
         
         if(msgtype.equals("OUL")) {
     	if (segment.startsWith("SPM")) { // to check sample no. in SPM section
            // Split the line by the pipe character "|"
            String[] parts = segment.split("\\|");

            // Check if there are at least 7 elements in the array
                // Extract and print the value after the 6th pipe
                String value = parts[2];
                System.out.println("ROHIT check 1"+value);
                String[] pid = value.split("\\&");
                 System.out.println("Patient Id: " + pid[0]);
                 
                 sampleName=pid[0];
                 System.out.println("ROHIT check 2"+sampleName);
                 //sampleNo.add(sampleName);
                //System.out.println("Patiwnt Id " + pid);
                 
                 
                 
            
                 
                 
    	
    	}
     	
     	
     	
    	if (segment.startsWith("OBX")) {
            // Split the line by the pipe character "|"
    		 String[] parts = segment.split("\\|");
    		 
    		 
count++;
if(parts[2].equals("NM")) {

             // Check if there are at least 5 elements in the array
             if (parts.length > 3) {
                 // Extract and print the value after the 4th pipe
                 String value = parts[3];
                 
                // if(value.equals("WBC")|| value.equals("NEU#") || value.equals("LYM#") || value.equals("MON#") || value.equals("EOS#") || value.equals("BAS#") || value.equals("IMG#") || value.equals("NEU%") || value.equals("LYM%") || value.equals("MON%") || value.equals("EOS%") || value.equals("BAS%") || value.equals("IMG%") || value.equals("RBC") || value.equals("HGB") || value.equals("HCT") || value.equals("MCV") || value.equals("MCH") || value.equals("MCHC") || value.equals("RDW-CV") || value.equals("RDW-SD") || value.equals("PLT") || value.equals("MPV") || value.equals("PDW") || value.equals("PCT") || value.equals("PLCC") || value.equals("NRBC#") || value.equals("NRBC%") || value.equals("PLCR") ) {
                  			
                 String[] tc = value.split("\\^");
                // System.out.println("Test Code " + tc[0]);String t
                 System.out.println("Test Code " + tc[1]); //Elite 580
                 String temp_code="";	//Elite 580
                 temp_code=tc[1];
                 temp_code=   temp_code.replaceAll("\\*", "");  // Remove * from Before
                 testCode.add(temp_code);
             }
             
             
             if (parts.length > 5) {
                 // Extract and print the value after the 4th pipe
                 String value = parts[5];
                 
                // if(value.equals("WBC")|| value.equals("NEU#") || value.equals("LYM#") || value.equals("MON#") || value.equals("EOS#") || value.equals("BAS#") || value.equals("IMG#") || value.equals("NEU%") || value.equals("LYM%") || value.equals("MON%") || value.equals("EOS%") || value.equals("BAS%") || value.equals("IMG%") || value.equals("RBC") || value.equals("HGB") || value.equals("HCT") || value.equals("MCV") || value.equals("MCH") || value.equals("MCHC") || value.equals("RDW-CV") || value.equals("RDW-SD") || value.equals("PLT") || value.equals("MPV") || value.equals("PDW") || value.equals("PCT") || value.equals("PLCC") || value.equals("NRBC#") || value.equals("NRBC%") || value.equals("PLCR") ) {
                  			
                 String[] tv = value.split("\\^");
                 //System.out.println("Test Value " + tv[0]);
                 testValue.add(tv[0]);
                 
continue;
             }
            
    	
    	}}
    	
         }
         
         else {
        	 
        	 
        	 
        	 
         }
     }
     System.out.println("Sample No:"+sampleName);
        System.out.println("Total Test Fetched from MSG"+count);
      System.out.println(testCode);  
      System.out.println("Total Test Entered"+testCode.size());  
      
      System.out.println(testValue);  
         
      mp.put("TestCode", testCode);
     	mp.put("TestValue", testValue);
     	//mp.put("SampleNO",sampleNo);
     	 

     	
     	
     	
        if(msgtype.equals("OUL")) {
     	ABC abc =  new ABC();
      	abc.insert_SysmexXN350(mp,sampleName.toString().trim());
      	
        }

        
        
        
        
        
    
        if(msgtype.equals("QBP")) {
            ///if msh type == QBP
            
        	
        	
        	
        	

            final Map res1 = ReadPropertyFile.getPropertyValues();
        	final  String machineip1 = (String) res1.get("machineip");

        	final  String machineport1 =  (String) res1.get("machineport");
             
        	final  String orderpacketdata =  (String) res1.get("orderpacketdata");
        	
        	
            
        	  int mPort = Integer.parseInt(machineport1);
//            System.out.println("Machine IP :"+machineip1);
//            System.out.println("Machine Port :"+mPort);
            HapiContext context = new DefaultHapiContext();
            //PipeParser parser = context.getPipeParser();
            boolean useTls = false;
        	
        	
        		String pidmsg=segments[1];
        //	System.out.println("Patient Information :"+pidmsg);
        	
        	String[] msgpart=pidmsg.split("\\|");
        	
        	String[] sampleNOblock=msgpart[3].split("\\^");
        	String sampleNO=sampleNOblock[0];
        	String SEC2val=msgpart[2];
        	String rackNo=sampleNOblock[1];
        	System.out.println("sample no :"+sampleNO +"Valueat second place::"+SEC2val);

        	
        	
        	
        	ABC obj =  new ABC();
        	final StringBuffer Test_Code;
    	/*	String it = obj.getSampleDtl(sampleNO);
    		String[] kvPairs = it.split(";");
    		
    		
	String[] kv = it.split(";");
    	
    		String k[] = kv.toString().split(";");
    		
    		
    		System.out.println(it+"faf");
        	
        	
    		for(String data:kv) {
    			
    			testCode.add(data);
    		}
    		System.out.println(testCode);
        	///rrrrrrrrrrrrrrrrrrrr
        	
        	
        	
        	
        	////////rrrrr
        	
        	
        	
        	*/
        	StringBuffer orderPacket = new StringBuffer();
        	
        	String MSH="MSH|^~\\\\&|LIS|LIS|HALIA|HALIA|20230921162512||OML^O33^OML_O33|20230910002512|P|2.5|||||||\r";
        	String PID="PID|||";
        			String PID2= sampleNO;
        		String PID3	= "^^^LIS^PI||989262300504521^ROHITMalviya^^||20230910|F|||^^^^^^||||||||||||||||||||N|AL\r";
        	String SPM="SPM|1|230909H01200||BLOOD||||InterLink|||||||||202309100017||||||\r";
        	String ORC="ORC|NW|33531|1|1|||||20230910001837||||3267||||||||^^^^^^^^^3267\r";
        	String TQ1="TQ1|||||||20230910002512||R\r";
        	String OBR="OBR|1|1|1|CBC^CBC profile^LIS|||||||BLOOD||||||||||||||P\r";
        	
        	orderPacket.append(MSH);
        	orderPacket.append("PV1||N|||||||||||||||||||||||||||||||||||||||||||\r");
        	orderPacket.append(PID);
        	orderPacket.append(PID2);
        	orderPacket.append(PID3);
        	orderPacket.append(SPM);
        	orderPacket.append(ORC);
        	orderPacket.append(TQ1);
        	orderPacket.append(OBR);
        	orderPacket.append("ORC|NW|33531|1|1|||||20230910001837||||3267||||||||^^^^^^^^^3267\n" + 
        			"TQ1|||||||20230910002512||R\n" + 
        			"OBR|1|1|1|DIF^DIF profile^LIS|||||||BLOOD||||||||||||||P");
        	
        	
        	//System.out.println("Order Packed"+orderPacket);
        	
        	
        	
        	//oroginal pkt
        	String hl7Message1="MSH|^~\\&|host||cpure||20231004002512||OML^O33^OML_O33|354|P|2.5|||NE|AL|UNICODE UTF-8|||LAB-28R^ROCHE\r"+
               	"PID|||SAM_NO||^^^^^^U|||U\r" +
                	"SPM|1|SAM_NO&BARCODE||SERPLAS^^99ROC|||||||P^^HL70369||||||||||||||||SC^^99ROC\r" +
                	"SAC|||SAM_NO^BARCODE||||||||||||||||||||||||||^1^1\r"+
                	//"TQ1|||||||||R^^HL70485\r" +
                	"OBR|1|4711||8714^^99ROC||||||||||||||||||||||||||||||||||||||||||\r" +
                	"TCD|8717^^99ROC|^1^:^1\r" +
                	"ORC|NW||||||||20160724080512\r" ;
                	
        	
        	  Parser p = context.getPipeParser();
              
       	   msg="MSH|^~\\&|host||cpure||20231004002512||OML^O33^OML_O33|354|P|2.5|||NE|AL|UNICODE UTF-8|||LAB-28R^ROCHE\r" + 
              		"PID|||SAM_NO^^^LIS^PI||^^^||20230910||||^^^^^^||||||||||||||||||||N|AL\r" + 
              		"PV1||N|||||||||||||||||||||||||||||||||||||||||||\r" + 
              		"SPM|1|SAM_NO||BLOOD||||InterLink|||||||||202309100017||||||\r" + 
              		"ORC|NW||1|1|||||20231004002512||||3267||||||||^^^^^^^^^3267\r" + 
              		"TQ1|||||||20231004002512||R\r" + 
              		"OBR|1|1|1|10018^^99ROC|||||||G||||||||||||||P\r" + 
              		"ORC|NW||1|1|||||20231004002512||||3267||||||||^^^^^^^^^3267\r" + 
              		"OBR|2|1|1|10088^^99ROC\r"+
              		"OBR|3|1|1|10111^^99ROC\r"+
              		"TQ1|||||||20231004002512||R\r" +
              		"TCD|10088^^99ROC|^1^^1\r" +
              		"TCD|10011^^99ROC|^1^^1\r" ;
              		 ;
            //Original Message as per DOC
              			 
              		 
              		 String HL7="MSH|^~\\&|||||YYYYMMDDHHMMSSQZZzz||OML^O33^OML_O33|MSG_CONTROLID||ProcessingID|P|2.5.1|||NE|AL||UNICODE UTF-8|||LAB-28R^ROCHE\n" + 
              		 		"PID|||PAT_ID||^^^^^^U||DOB|GENDER\n" + 
              		 		"SPM|1|SAM_NO&BARCODE||SERPLAS^^99ROC|||||||P^^HL70369||||||||||||||||\n" + 
              		 		"SAC|||SAM_NO&BARCODE||||||||||||||||||||||||||\n" + 
              		 		"ORC|NW||||||||YYYYMMDDhhmmss\n"; 
              		 	//	"TQ1|||||||||R^^HL70485\n" + 
              		 		//"OBR|1|PLACEORDERNO\"\"|testcode^^99ROC||||||||||||||||||||||||||||||||||||||||||\n" + 
              		 		//"TCD|testcode^^99ROC|1^:^2";
       	 
       	 String testCodeforOrder="";
     	testCodeforOrder="TQ1|||||||||R^^HL70485\r" + 
     			"OBR|1|PLACEORDERNO|testcode^^99ROC||||||||||||||||||||||||||||||||||||||||||\r" + 
     			"TCD|testcode^^99ROC|1^^2\r" ;
		int testCount=0;
//       	for(String data:kv) {
//			
//       		testCodeforOrder = testCodeforOrder.replaceAll("testcode", data);
//       		testCodeforOrder = testCodeforOrder.replaceAll("count", "3");
//       		
//       		hl7Message+=testCodeforOrder;
//		
//			
//		}
       //	System.out.println("ROHIT FiNAL"+hl7Message);
       	
      // Define a custom date and time format
       	LocalDateTime now = LocalDateTime.now();
        
    
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
         hl7Message=null;
         
//         hl7Message="MSH|^~\\&|Host||cobas pure||YYYYMMDDHHMMSSQZZzz||OML^O33^OML_O33|10001|P|2.5.1|||NE|AL||UNICODE UTF-8|||LAB-27R^ROCHE\n" + 
//        	       	"PID|||SAM_NO||^^^^^^U|||U\n" + 
//        	       	"SPM|1|SAM_NO&BARCODE||SERPLAS^^99ROC|||||||P^^HL70369||||||20240306102715||||||||||FSBT^^99ROC\n" + 
//        	       	"SAC|||SAM_NO^BARCODE|||||||50009|1||||||||||||||||||^1^1\n" + 
//        	       	"ORC|NW||||||||YYYYMMDDHHMMSSQ\n" + 
//        	       	"TQ1|||||||||R^^HL70485\n" + 
//        	       	"OBR|1|\"\"||20340^^99ROC\n" + 
//        	       	"TCD|20340^^99ROC|1^:^1";
         
         msg= "MSH|^~\\&|Host||cobas pro||20240306190023||OML^O33^OML_O33|10001|P|2.5.1|||NE|AL||UNICODE UTF-8|||LAB-27R^ROCHE\r" + 
         		"PID|||SAM_NO||^^^^^^U||19901024|U\r" + 
         		"SPM|1|SAM_NO&BARCODE||SERPLAS^^99ROC|||||||P^^HL70369||||||20240306190023||||||||||FSBT^^99ROC\r" + 
         		"SAC|||SAM_NO^BARCODE|||||||50058|2||||||||||||||||||^1^1\r" + 
         		"ORC|NW||||||||20240306190023\r" + 
         		"TQ1|||||||||R^^HL70485\r" + 
         		"OBR|1|\"\"||20340^^99ROC\r" + 
         		"TCD|20340^^99ROC|\r" + 
         		"";
         String formattedTimestamp = now.format(formatter);
         
         String str [] =pidmsg.split("\\|");
         
         // str[12]="S";
          int lastIndex = pidmsg.length() - 1;
          pidmsg = pidmsg.substring(0, lastIndex) + "S";
         String data="MSH|^~\\&|Host||cobas pro||20240305212748||RSP^K11^RSP_K11|2053|P|2.5.1|||NE|AL||UNICODE UTF-8|||LAB-27R^ROCHE\r" + 
         		"MSA|AA|2053\r" + 
         		"QAK|12730|OK|INIBAR^^99ROC\r" + 
         		pidmsg;
         
         data=data.replaceAll("SAM_NO", sampleNO);
         data=data.replaceAll("YYYYMMDDHHMMSSQZZzz", formattedTimestamp+"+0530");
         data=data.replaceAll("RRR", SEC2val);
         
         
         // Format the current date and time using the custom format
         
    	Date timestamp = new Date();
    	msg = msg.replaceAll("YYYYMMDDHHMMSSQZZzz", formattedTimestamp+"+0530");
    	msg = msg.replaceAll("YYYYMMDDHHMMSSQ", formattedTimestamp);
    	msg = msg.replaceAll("SAM_NO", sampleNO);
    	msg = msg.replaceAll("YYYYMMDDhhmmss", formattedTimestamp); 
    	msg = msg.replaceAll("SAM_NO", sampleNO);
    	msg = msg.replaceAll("MSG_CONTROLID", sampleNO);
    	msg = msg.replaceAll("PLACEORDERNO", "1000");
    	msg = msg.replaceAll("ProcessingID", "1000");
        
     	 hl7Message = p.parse(msg); 
     	//hl7Message = p.parse(msg); 
    	 
     		
       	String newPatientId = sampleNO+"^^^LIS^PI";
           String newSpecimenId = sampleNO;
        	
        	System.out.println("RS 11 PKT::"+hl7Message);
        	
        	
        	return hl7Message;
//        	String msg="MSH|^~\\&|LIS|LIS|HALIA|HALIA|20230921162512||OML^O33^OML_O33|20230910002512|P|2.5|||||||\r" + 
//               		"PID|||230909H01200^^^LIS^PI||989262300504521^ROHITMalviya^^||20230910|F|||^^^^^^||||||||||||||||||||N|AL\r" + 
//               		"PV1||N|||||||||||||||||||||||||||||||||||||||||||\r" + 
//               		"SPM|1|230909H01200||BLOOD||||InterLink|||||||||202309100017||||||\r" + 
//               		"ORC|NW|33531|1|1|||||20230910001837||||3267||||||||^^^^^^^^^3267\r" + 
//               		"TQ1|||||||20230910002512||R\r" + 
//               		"OBR|1|1|1|CBC^CBC profile^LIS|||||||BLOOD||||||||||||||P\r" + 
//               		"ORC|NW|33531|1|1|||||20230910001837||||3267||||||||^^^^^^^^^3267\r" + 
//               		"TQ1|||||||20230910002512||R\r" + 
//               		"OBR|1|1|1|DIF^DIF profile^LIS|||||||BLOOD||||||||||||||P\r" + 
//               		""; 
//        	
//        	String msg = orderPacket.toString();
//             Parser p = context.getPipeParser();
//             Message adt = p.parse(hl7Message);
        	
//           //  172.16.50.1
//             Connection connection = context.newClient(machineip1, mPort, useTls);
//
//             // The initiator is used to transmit unsolicited messages
//             Initiator initiator = connection.getInitiator();
//            // Message response = initiator.sendAndReceive(adt);
//             Message response;
//			try {
//				response = initiator.sendAndReceive(adt);
//
//	             String responseString = p.encode(response);
//
//	             System.out.println("Send order response:\n" + responseString);
//	             
//			} catch (LLPException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

        }
        
        /*
         * Note that this second technique has one major drawback: Although 
         * message definitions are backwards compatible, some group names
         * change between versions. If you are accessing a group within
         * a complex message structure, this can cause issues.
         * 
         * This is less of an issue for some message types where groups are
         * not used much (e.g. ADT)
         */

        // This works and prints "Test Value"
        //System.out.println(t23.get("/RESPONSE/ORDER_OBSERVATION/OBSERVATION(0)/OBX-5"));

        // This fails...
        // System.out.println(t25.get("/RESPONSE/ORDER_OBSERVATION/OBSERVATION(0)/OBX-5"));
        
        // ...because this would be required to extract the OBX-5 value from a v2.5 message
       // System.out.println(t25.get("/PATIENT_RESULT/ORDER_OBSERVATION/OBSERVATION(0)/OBX-5"));
        // Now generate a simple acknowledgment message and return it
        

        HapiContext context = new DefaultHapiContext();
    	  Parser p = context.getPipeParser();

		 hl7Message = p.parse(msg); 
			System.out.println("OML 32 PKT"+hl7Message);			
		return hl7Message;

	}

}