package Server;
import ca.uhn.hl7v2.model.v251.message.OML_O33;
import ca.uhn.hl7v2.model.v251.segment.MSH;
import ca.uhn.hl7v2.model.v251.segment.PID;
import ca.uhn.hl7v2.model.v251.segment.PV1;
import ca.uhn.hl7v2.model.v251.segment.SPM;
import ca.uhn.hl7v2.model.v251.segment.OBR;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.HL7Exception;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HL7MessageGenerator {

    public static String generateOrderMessage(String sampleNumber,String specimenType) throws HL7Exception {
    	String MSG="";
    	
    	if(specimenType.equals("URIN")) {
    		specimenType="URIN^URIN";
    		
    	}
    	else if(specimenType.equals("BLOOD")) {
    		specimenType="BC^Blood Culture";
    	}
    	try {
    	 MSG="MSH|^~\\&|LIS|LAB|myla|BMX|DATETIME||OML^O33^OML_O33|MSG-20241007-101200-0377|P|2.5.1|||NE|AL||UNICODE UTF-8 \r\n"
    			+ "PID|||SAMPLEID||CDAC TEST^.^^^^^nill|CDAC TEST2|19681025|M|||nill^^nill^^nill^nill|||||S| \r\n"
    			+ "PV1||E|GW-NEW CUBICAL WARD-J^8-FCGW-J-29^^ AIIMS^^^^^8-FCGW-J-29^||||^||||||||||||25349508    \r\n"
    			+ "SPM|1|SAMPLEID||SPECIMAN^99BMx|||^^|^^|||P^Patient^||||||DATETIME|\r\n"
    			+ "SAC||||24008676 \r\n"
    			+ "ORC|NW||||||||DATETIME\r\n"
    			+ "TQ1|||||||||R^^\r\n"
    			+ "OBR|1|714647||SU^SU^99BMx||||||^^||||||^|123456780";
    	
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

    private static String generateCurrentTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date());
    }
}
