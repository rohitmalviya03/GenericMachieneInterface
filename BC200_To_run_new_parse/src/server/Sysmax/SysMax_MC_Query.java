package server.Sysmax;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import server.Server;

public class SysMax_MC_Query {

	
	
	
	
	
	
	
	
	

	private static List<StringBuffer> sysmax2400RBparseBI(String line) {
		System.out.println("Sysmax Bi DIR with ENQ----2500");
		SimpleDateFormat time_formatter2 = new SimpleDateFormat("yyyyMMddHHmmss");
		String current_time_str2 = time_formatter2.format(System.currentTimeMillis());
		System.out.println("current_time---" + current_time_str2);

		Server.StxCounter = 1;

		SimpleDateFormat time_formatter3 = new SimpleDateFormat("yyyyMMddHHmmss");
		String current_time_str3 = time_formatter3.format(System.currentTimeMillis());
		//		System.out.println("current_time_str2 after 1 second wait---" + current_time_str3);
		char[] array = line.toCharArray();
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
		StringBuffer frame5 = new StringBuffer();

		StringBuffer buf_header = new StringBuffer();
		StringBuffer buf_patient = new StringBuffer();
		StringBuffer buf_order = new StringBuffer();
		StringBuffer buf_comment = new StringBuffer();
		StringBuffer buf_order_1 = new StringBuffer();
		StringBuffer buf_termination = new StringBuffer();

		SimpleDateFormat time_formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String current_time_str = time_formatter.format(System.currentTimeMillis());

		StringBuffer header = new StringBuffer("H|\\^&|||||||||||E1394-97|");
		//header.append("|||||||||P");	// for Alinity
		//header.append("||"); 			// for Vitros Ramji Masked
		//header.append("qnxa224"); 	// for Vitros Ramji Masked	
		//header.append("|||||");  		// for Vitros Ramji Masked
		//header.append("|||LIS2-A|");	// for Vitros Ramji Masked
		//header = header.append(current_time_str);	// for Vitros Ramji Masked

		//StringBuffer line4 = new StringBuffer("L|1|N"); // for Vitros Ramji Masked
		StringBuffer line4 = new StringBuffer("L|1|N"); // for Alinity

		//StringBuffer line6 = new StringBuffer("|||O"); // for Vitros Ramji Masked
		StringBuffer line5 = new StringBuffer();
		char etb = 23;
		char eot = '';
		char etx = 3;
		Server.counterPID_Packet = Server.counterPID_Packet + 33;
		 // for Vitros Ramji Masked
		StringBuffer p = new StringBuffer("P|");
		
		String[] patDetails=Server.name_pat.split(" ");
		//P|1|||100|^Heisei^Jiro||20010820|M|||||^Dr.2||||||||||||^^^EAST|||||||||
		p.append(Server.p_packet_count);
		//p.append("|");
		//p.append(Server.Sample_code.get(0).toString().replace("^", ""));
		p.append("||||");
		p.append("^");
		
		if(Server.name_pat.equals(null)) {
		p.append(patDetails[0].trim());
		p.append("^");
		p.append(patDetails[1].trim());
		
		}
		else{
			p.append("");
			p.append("^");
			p.append("");
				
			
		}
		p.append("||");
		p.append(Server.name_pat_age+"|");
		p.append(Server.name_pat_gender);
		//p.append("|||||");
		//Added for Alinity
		//StringBuffer p = new StringBuffer("P|");
		//p.append(p_packet_count);
		//p.append("||||^^|||||||||||||||||||||||||||||");

		StringBuffer Order = new StringBuffer();
		StringBuffer Comment = new StringBuffer();
		StringBuffer Order1 = new StringBuffer("O|2");
		List list = new ArrayList();
		StringBuffer frame = new StringBuffer();
		StringBuffer framevip = new StringBuffer();
		StringBuffer response = new StringBuffer();
		StringBuffer time = new StringBuffer();

		try {

			Server.cobas = cobas;
			Server.host = host;
			System.out.println("TIME:- " + time);

			Server.stx = stx;
			Server.mode = mode;

			while (true) {

				buf_header.append(header);
				buf_header.append(cr);
				buf_patient.append(p);
				buf_patient.append(cr);
				p.delete(0, p.length());

				// ======================================================
				Server.frameList.clear();

				//frameList.add("");
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

				// ===========================Start for Vitros 5600=========================================
				/*	Order.append("O|1");
				// Order.append();
				Order.append("|");
				Order.append(Server.Sample_code.get(0).toString().replace("^", ""));
				Order.append("||^^^1.0000+");
				for (int k = 0; k < Server.testCode.size(); k++)

				{
					Order.append(Server.testCode.get(k));
					Order.append("+1.0");
					if (k != Server.testCode.size() - 1) {
						Order.append("\\");
					}
				}
				Order.append("|R|");
				Order.append("|");
				Order.append("||||N||||5|||||||");
				Order.append(line6);
				Order.append(cr);
				 */	
				//=========================END for Vitros 5600=========================================	
				// ===========================Start for Alinity=======O|1|002111522041500||^^^65|||||||A||||||||||||||O
				frame3.delete(0, frame3.length());
				int OrderCounter=0;
				int fCount  = 3;
				int loopSize = Server.testCode.size();
				if(loopSize==0) //added this if condition for null Response 
				{
					Order.append("O|1|");
					//OrderCounter = k + 1;
					//Order.append(Integer.toString(OrderCounter));
					Order.append("^");
					Order.append("^");
					Order.append(Server.sampleNo.toString().replace("^", "").trim());
					// Order.append("^50087^1^^S1^SC^|");
					Order.append("|");
					Order.append("|");
					//Order.append("|");
					Order.append("^^^");
					Order.append("^^\\");
					Order.append("|R");
					Order.append("|");
					Order.append(current_time_str);
					Order.append("|||||N|||||||||||||||||||");


					Order.append(cr);

					frame3.append(stx);
					Server.StxCounter = Server.StxCounter + 1;
					frame3.append(Server.StxCounter);
					frame3.append(Order);
					frame3.append(etx);
					frame3.append(Server.CheckSum(frame3.toString())); // FRAME WITH STX STX COUNTER AND ETX
					frame3.append(cr);
					frame3.append(lf);
					Server.frameList.add(frame3.toString());
					frame3.delete(0, frame3.length());
					Server.StxCounter=Server.StxCounter+1;	
				}
				else
				{

					Order.append("O|1|");
					//OrderCounter = k + 1;
					//Order.append(Integer.toString(OrderCounter));
					//Order.append("^");
					//Order.append("^");
					Order.append(Server.sampleNo.toString().trim());
					// Order.append("^50087^1^^S1^SC^|");
					Order.append("|");
					Order.append("|");

					for (int k = 0; k < Server.testCode.size(); k++)
					{

						//Order.append("O|1|"); // Need "O|1|" "O|2|" "O|3|"


						//Order.append("||^^^"); //RAMJI
						//Order.append(Server.testCode.get(k));
						//Order.append("||^^^349|||||||N||||||||||||||O"); //Hardcode
						Order.append("^^^");
						Order.append(Server.testCode.get(k));
						if (k != Server.testCode.size() - 1)
							Order.append("^^^\\");
						else
							Order.append("^^^");
					}

					
					
					//^^^040^PT-THS\^^^050^APTT-FS
					//	Order.append(Server.testCode.get(k));
					//	if (k != Server.testCode.size() - 1) {
					//		Order.append("\\");

					//}

					Order.append("|");
					Order.append("R");
					Order.append("|");
					Order.append(current_time_str);
					Order.append("|||||N|||||||||||||||||||");

					Order.append(cr);

					//=========================END for Vitros 5600=========================================	

					buf_order.append(Order);
					Order.delete(0, Order.length());

					buf_comment.append(Comment);
					Comment.delete(0, Comment.length());

					// --------------------------------------------
					System.out.println("Alinity Frame Count Checking :" + Server.StxCounter);
					int frameCount = 0, first = 0, mid = 0, remain = 0;
					frameCount = Server.FrameCounter(buf_order.length());
					int length = buf_order.length();

					//for (int i = 1; i <= frameCount; i++) 
					//{
					if (length < 240)
						mid = mid + length;
					else if (length == 240)
						mid = mid + length;
					else if (length > 240) {
						mid = mid + 240;
					}
					frame3.append(stx);
					Server.StxCounter = Server.StxCounter + 1;
					frame3.append(fCount);

					frame3.append(buf_order.substring(first, mid));
					//---------------------------------
					//		if (fCount != frameCount) 
					{
						frame3.append(etx);
						frame3.append(Server.CheckSum(frame3.toString())); // FRAME WITH STX STX COUNTER AND ETX
						frame3.append(cr);
						frame3.append(lf);
					}
					//		else
					//		{
					//			frame3.append(etb);
					//			frame3.append(CheckSum(frame3.toString())); // FRAME WITH STX STX COUNTER AND ETB
					//			frame3.append(cr);
					//			frame3.append(lf);
					//		}
					length = length - 240;
					first = mid;
					//-----------------------------------------
					Server.frameList.add(frame3.toString());

					frame3.delete(0, frame3.length());


					fCount++;

					if(fCount==8)
					{
						fCount=0;
					}
			
					buf_order.delete(0, buf_order.length());
					//System.out.println("frameList= " +k);
					//				if(k==loopSize-1)
					//				{
					//					Server.StxCounter = fCount;
					//				}
					//System.out.println("frameList= " + frameList.toString());
					//}//For Loop Ramji for Multiple Order Packet frame 3
				}//else		
				// -------------------------------------


				line4.append(cr);

				buf.append(line4);
				buf_termination.append(line4);

				break;
			}

			// ========================================
			frame5.append(stx);
			if(Server.StxCounter==8)
			{	
				Server.StxCounter=0;
			}
			else
			{
				Server.StxCounter = Server.StxCounter ;
			}
			frame5.append(Server.StxCounter);
			Comment.append("C|1|L|Order comment.|G");
			Comment.append(cr);
			Comment.append(etx);
			//Comment.append("78");
			frame5.append(Comment);
			frame5.append(Server.CheckSum(frame5.toString())); // FRAME WITH STX STX COUNTER AND ETB
			frame5.append(cr);
			frame5.append(lf);

			//frameList.add(frame5.toString());
			frame5.delete(0, frame5.length());

			Server.StxCounter = Server.StxCounter + 1;
			if(Server.StxCounter==8)
			{	
				Server.StxCounter=0;
			}
			else
			{
				Server.StxCounter = Server.StxCounter ;
			}


			// frameList4.clear();
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
			System.out.println("Final String after checksum" + Server.frameList.toString());


		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return Server.frameList;
	}
}
