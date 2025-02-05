package ca.uhn.hl7v2.examples;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HL7Order {

    // Define the list for sample numbers
    private static List<String> sampleNo = new ArrayList<>();
	
	static Map res = ReadPropertyFile.getPropertyValues();
	final static String orderip = (String) res.get("orderip");

    
    private static String instrumentHost = orderip;
    private static int instrumentPort = 54000; // Replace with the actual port number
    private static String lastSampleNo =null;
    private static  StringBuffer orderPacket = new StringBuffer();
    public static void main(String[] args) {
        // ... Rest of your code ...

        // Create and start a dedicated thread for updating the sampleNo list and sending data
        Thread sampleNoUpdaterThread = new Thread(() -> {
            while (true) {
                updateSampleNoList();
                sendDataToInstrument();
                try {
                    // Sleep for 30 minutes
                    Thread.sleep(1 * 60 * 1000); // 30 minutes in milliseconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        sampleNoUpdaterThread.start();

        // ... Rest of your code ...
    }

    // Method to update the sampleNo list
    private static void updateSampleNoList() {
        // Clear the existing list and add new sample numbers
        
    	ABC abc = new ABC();
    	String res=abc.getSampleDtl("12345");
    	System.out.println(res);
    	
    	String [] sampleNOlist =res.split(";");
    	//sampleNo.clear();
    	
    	for(String str:sampleNOlist) {
    		sampleNo.add(str);
    	}
//        sampleNo.add("NewSampleNo1");
//        sampleNo.add("NewSampleNo2");
//        sampleNo.add("NewSampleNo3");
//        // Add more sample numbers as needed
    }

    // Method to send data to the instrument
    private static void sendDataToInstrument() {
        try {
            Socket socket = new Socket("192.168.112.135", 10001);
            OutputStream outputStream = socket.getOutputStream();
            
            for (String sample : sampleNo) {
                // Create and send the request packet for each sample
//            	if(!lastSampleNo.equals("")&&lastSampleNo.equals(null)) {
//                	
//                	System.out.println("Last Sample uploaded is :" +lastSampleNo);
//                if(sample.equals(lastSampleNo)) {
//                
//                	
//                	
//                	
//                }}	
                String requestPacket = createRequestPacket(sample);
                System.out.println("Order Packet :"+requestPacket);
                outputStream.write(requestPacket.getBytes());
                outputStream.flush();
                orderPacket.delete(0, orderPacket.length());
                //sampleNo.remove(0);
                // Sleep for a short duration (adjust as needed) before sending the next request
                lastSampleNo=sample;
                Thread.sleep(1000); // Sleep for 1 second
            }
System.out.println("Last Sample "+lastSampleNo);
            socket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Method to create an HL7 request packet for a sample
    private static String createRequestPacket(String sampleNo) {
        // Modify this method to create the HL7 request packet for the given sampleNo
        // You can use your existing code to build the request packet
        // Example: return "MSH..."; // Construct the HL7 message
    	
    	String MSH="MSH|^~\\\\&|LIS|LIS|HALIA|HALIA|20230921162512||OML^O33^OML_O33|20230910002512|P|2.5|||||||\r";
        String PID="PID|||";
        
        		String PID1=sampleNo;
        		
        String PID2="^^^LIS^PI||989262300504521^";
        		String pname= "";
        	String PID4= "^^||20230910|F|||^^^^^^||||||||||||||||||||N|AL\r";
        String PV1="PV1||N|||||||||||||||||||||||||||||||||||||||||||\r";
        String SPM="SPM|1|";
        String SPM1=sampleNo;
        String SPM2="||BLOOD||||InterLink|||||||||202309100017||||||\r";
        String ORC="ORC|NW|33531|1|1|||||20230910001837||||3267||||||||^^^^^^^^^3267\r";
        String TQ1="TQ1|||||||20230910002512||R\r";
        String OBR="OBR|1|1|1|DIF^DIF profile^LIS|||||||BLOOD||||||||||||||P\r";
        String end="ORC|NW|33531|1|1|||||20230910001837||||3267||||||||^^^^^^^^^3267\r" + 
          		"TQ1|||||||20230910002512||R\r" + 
          		"OBR|1|1|1|DIF^DIF profile^LIS|||||||BLOOD||||||||||||||P\r" + 
          		"";
        
        orderPacket.append("");
        orderPacket.append(MSH);
        orderPacket.append(PID);
        orderPacket.append(PID1);
        orderPacket.append(PID2);
        orderPacket.append(pname);
        orderPacket.append(PID4);
        
        orderPacket.append(PV1);
        orderPacket.append(SPM);
        orderPacket.append(SPM1);
        orderPacket.append(SPM2);
        
        orderPacket.append(ORC);
        orderPacket.append(TQ1);
        orderPacket.append(OBR);
        orderPacket.append(end);
        return orderPacket.toString(); // Placeholder, replace with actual HL7 message construction
    }
}
