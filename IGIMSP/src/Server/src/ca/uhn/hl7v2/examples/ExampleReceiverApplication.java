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
import java.util.ArrayList;
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

        String encodedMessage = new DefaultHapiContext().getPipeParser().encode(theMessage);
       
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
     	if (segment.startsWith("PID")) {
            // Split the line by the pipe character "|"
            String[] parts = segment.split("\\|");

            // Check if there are at least 7 elements in the array
                // Extract and print the value after the 6th pipe
                String value = parts[3];
                String[] pid = value.split("\\^");
                 System.out.println("Patient Id: " + pid[0]);
                 
                 sampleName=pid[0];
                 
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
                // System.out.println("Test Code " + tc[0]);
                 testCode.add(tc[0]);
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

        HapiContext context = new DefaultHapiContext();
        //PipeParser parser = context.getPipeParser();
        boolean useTls = false;
        if(msgtype.equals("ADT")) {
        	
        	
        		String pidmsg=segments[1];
        	System.out.println("Patient Information :"+pidmsg);
        	
        	String[] msgpart=pidmsg.split("\\|");
        	
        	String[] sampleNOblock=msgpart[3].split("\\^");
        	String sampleNO=sampleNOblock[0];
        	
        	System.out.println("sample no :"+sampleNO);
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
        	
        	
        	System.out.println("Order Packed"+orderPacket);
        	
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
        	String msg = orderPacket.toString();
             Parser p = context.getPipeParser();
             Message adt = p.parse(msg);
        	
             
             Connection connection = context.newClient("10.80.0.61", 10001, useTls);

             // The initiator is used to transmit unsolicited messages
             Initiator initiator = connection.getInitiator();
             Message response;
			try {
				response = initiator.sendAndReceive(adt);

	             String responseString = p.encode(response);

	             System.out.println("Send order packet:\n" + responseString);
			} catch (LLPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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
        try {
        	return theMessage.generateACK();
        } catch (IOException e) {
            throw new HL7Exception(e);
        }

    }

}