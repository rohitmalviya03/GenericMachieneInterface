package server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class AlinityBhatinda {
	
	
	public static List<StringBuffer> queryParse(String ListData) {
		
		
		System.out.println("ua Query Packet Parsing Start ");
		SimpleDateFormat time_formatter2 = new SimpleDateFormat("yyyyMMddHHmmss");
		String current_time_str2 = time_formatter2.format(System.currentTimeMillis());
		System.out.println("current_time_str2---" + current_time_str2);
		Server.StxCounter = 1;
		String qryFlag=null;
		
		String[] str=ListData.split("\\|");
		qryFlag=	str[12];
		SimpleDateFormat time_formatter3 = new SimpleDateFormat("yyyyMMddHHmmss");
		String current_time_str3 = time_formatter3.format(System.currentTimeMillis());
		// System.out.println("current_time_str2 after 1 second wait---" +
		// current_time_str3);

		int CountPipe = 0, j = 0;
		char cr = 13;
		char lf = 10;
		int instance = 0;
		char stx = '';
		String mode1 = "";
		StringBuffer buf = new StringBuffer();
		StringBuffer cobas = new StringBuffer();
		StringBuffer host = new StringBuffer();
		StringBuffer mode = new StringBuffer();
		StringBuffer lines = new StringBuffer();
		StringBuffer frame1 = new StringBuffer();
		StringBuffer frame2 = new StringBuffer();
		StringBuffer frame3 = new StringBuffer();
		StringBuffer frame3_1 = new StringBuffer();
		StringBuffer frame4 = new StringBuffer();

		StringBuffer buf_header = new StringBuffer();
		StringBuffer buf_patient = new StringBuffer();
		StringBuffer buf_order = new StringBuffer();
		StringBuffer buf_order_1 = new StringBuffer();
		StringBuffer buf_termination = new StringBuffer();

		SimpleDateFormat time_formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		time_formatter.setTimeZone(TimeZone.getTimeZone("GMT+05:30"));
		String current_time_str = time_formatter.format(System.currentTimeMillis());

		//		StringBuffer header1 = new StringBuffer(
		//				"H|@^\\|GXM-88424724426|| LIS|||||CENTRAL RAILWAY HOSPITAL, SECUNDERABAD - 110000888^GeneXpert^6.4||P|1394-97|");

		StringBuffer header = new StringBuffer();

		header.append("H|\\^&|||Alinity ci-series^3.5^SCM22628|||||||P|LIS2-A2|");
		// header = header.append(Query_Message_date_time);
		header = header.append(current_time_str);

		StringBuffer line4 = new StringBuffer("L|1|F");
		StringBuffer line6 = new StringBuffer("|||F");
		StringBuffer line5 = new StringBuffer();
		char etb = 23;
		char eot = '';
		char etx = 3;
		Server.counterPID_Packet = Server.counterPID_Packet + 33;
		StringBuffer p = new StringBuffer("P|1|||********|^********************^||||||||||||||||||||******");
	
		 
		StringBuffer c = new StringBuffer("C|1|L|Order comment.|G");

		StringBuffer Order = new StringBuffer();
		StringBuffer Order1 = new StringBuffer("O|1");
		List list = new ArrayList();
		StringBuffer frame = new StringBuffer();
		StringBuffer framevip = new StringBuffer();
		StringBuffer response = new StringBuffer();
		StringBuffer time = new StringBuffer();

		try {

			System.out.println("TIME:- " + time);

			while (true) {

				buf_header.append(header);
				buf_header.append(cr);
				buf_patient.append(p);
				buf_patient.append(cr);

				System.out.println("Server.testCode.size------- " + Server.testCode.size());

				Server.frameList.clear();
				frame1.append(stx);
				frame1.append(Server.StxCounter);
				frame1.append(buf_header);
				buf_header.delete(0, buf_header.length());
				frame1.append(etx);
				frame1.append(Server.CheckSum(frame1.toString())); // FRAME WITH STX STX COUNTER AND ETB
				frame1.append(cr);
				frame1.append(lf);
				Server.frameList.add(frame1.toString());
				frame1.delete(0, frame1.length());
				//System.out.println("Server.frameList= " + Server.frameList.toString());

				Server.StxCounter = Server.StxCounter + 1;

				frame2.append(stx);
				frame2.append(Server.StxCounter);
				frame2.append(buf_patient);
				buf_patient.delete(0, buf_patient.length());
				frame2.append(etx);
				frame2.append(Server.CheckSum(frame2.toString())); // FRAME WITH STX STX COUNTER AND ETB
				frame2.append(cr);
				frame2.append(lf);
				Server.frameList.add(frame2.toString());
				frame2.delete(0, frame2.length());
				//System.out.println("Server.frameList= " + Server.frameList.toString());


				
				
				
				for(int K=0;K<=Server.testCode.size()-1;K++) {
					
					
					Order.append("O|"+K+"|"+Server.Sample_code.get(0)+"||^^^"+Server.testCode.get(K)+"|||||||N||||||||||||||O");
					Order.append(cr);

					buf_order.append(Order);
					Order.delete(0, Order.length());
					
					int frameCount = 0, first = 0, mid = 0, remain = 0;
					frameCount = Server.FrameCounter(buf_order.length());
					// System.out.println("Char At 0 "+buf.charAt(0));
					int length = buf_order.length();
					// Server.frameList3.clear();
					for (int i = 1; i <= frameCount; i++) {
						if (length < 240)
							// if (length < 306)
							mid = mid + length;
						else if (length == 240)
							mid = mid + length;
						else if (length > 240) {
							mid = mid + 240;
						}
						System.out.println("First " + first + "  " + "last " + mid + " stx  " + stx);
						frame3.append(stx);
						// System.out.println("Server.Server.StxCounter " +Server.StxCounter);
						if(Server.StxCounter==7) {Server.StxCounter=0;}
						else {Server.StxCounter = Server.StxCounter + 1;
						}
						frame3.append(Server.StxCounter);
						// System.out.println("STX COUNTER:- " +Server.StxCounter);

						frame3.append(buf_order.substring(first, mid));

						if (i == frameCount) {
							frame3.append(etx);
							// System.out.println("before Server.CheckSum" + frame3.toString());
							frame3.append(Server.CheckSum(frame3.toString())); // FRAME WITH STX STX COUNTER AND ETX
							// frame.append(Server.CheckSum(buf.substring(first, mid))); //FRAME WITH ONLY
							// CHARACTERS
							frame3.append(cr);
							frame3.append(lf);
						} else

						{
							frame3.append(etb);
							// System.out.println("before Server.CheckSum" + frame3.toString());
							frame3.append(Server.CheckSum(frame3.toString())); // FRAME WITH STX STX COUNTER AND ETB
							// frame.append(Server.CheckSum(buf.substring(first, mid)));
							frame3.append(cr);
							frame3.append(lf);
						}
						// length = length - mid;
						length = length - 240;
						first = mid;

						Server.frameList.add(frame3.toString());
						/*
						 * if(i ==1) frame1.append(frame); else frame2.append(frame);
						 */
						frame3.delete(0, frame3.length());

					}

					buf_order.delete(0, buf_order.length());
				
				}

				// System.out.println("buf_order= " + buf_order.toString());

				// --------------------------------------------
				// System.out.println("STRING LENGTH BEFORE FRAMING buf_order= " +
				// buf_order.length());

				// STRING LENGTH BEFORE FRAMING = 235
			


				line4.append(cr);

				buf.append(line4);
				buf_termination.append(line4);

				break;
			}

			// ========================================
			if(Server.StxCounter==7) {Server.StxCounter=0;}
			else {
			Server.StxCounter = Server.StxCounter + 1;
			}
			frame4.append(stx);
			frame4.append(Server.StxCounter);
			frame4.append(buf_termination);
			buf_termination.delete(0, buf_termination.length());
			frame4.append(etx);
			frame4.append(Server.CheckSum(frame4.toString())); // FRAME WITH STX STX COUNTER AND ETB
			frame4.append(cr);
			frame4.append(lf);

			Server.frameList.add(frame4.toString());
			frame4.delete(0, frame4.length());

			for (int i = 0; i < Server.frameList.size(); i++) {
				Server.count_ack_BA400_o1++;
			}
			//				System.out.println("count_ack_BA400_o1------- " + count_ack_BA400_o1);
			System.out.println("Final String after Server.CheckSum" + Server.frameList.toString());

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		// Server.Server.frameList = Server.frameList;

		return Server.frameList;
		
		
		//return null;
		
		
	
		
		
	}
	
	public static void resultParse(List<String> list) {
		
		
		System.out.println("Result Parse Start ......");
		///System.out.println("Result Packet : "+list);
		
		
		
		
	
		//System.out.println(list);
		String sampleNo=null;
		List testCode = new ArrayList<String>();
		List testValue = new ArrayList<String>();
		Map<String ,List> mp = new HashMap<String ,List>();
		for(String strdata:list) {
			String[] packetData=strdata.split("\\|");
			
			if(packetData[0].contains("P")) {
				
			}
			else if(packetData[0].contains("O")) {
				String[] sampleNodata=packetData[2].split("\\^");
				sampleNo=sampleNodata[0];
			}
			else if(packetData[0].contains("R")) {
				
			//	System.out.println( packetData[2]);
				
				String[] tc=packetData[2].split("\\^");
				if(tc[6].equals("F")) {
				testCode.add(tc[3]);
				testValue.add(packetData[3]);
				
				
				if(!packetData[3].contains("No Result")) {
				ABC.insert_SysmexXN350A(tc[3], packetData[3].trim(), sampleNo.toString().replace("^", "").trim());
				}
				}
				
			}
			
		}
		
		
		
		
		
		
		
	}
	

}
