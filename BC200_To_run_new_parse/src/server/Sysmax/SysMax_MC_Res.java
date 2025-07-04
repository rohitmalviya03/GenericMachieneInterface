package server.Sysmax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import server.ABCbkp;
import server.Server;

public class SysMax_MC_Res {


	public static void sysmaxCS2400(List<String> list) {    // AIIMS Jammu Co=gulation Format ID 100018
		Map<String, List> mp = new HashMap();
		List lis = new ArrayList();
		List lis1 = new ArrayList();
		String ip = "";

		List testCode = new ArrayList<>();
		List testvalue =new ArrayList<>();

		//String Server.sampleName = null;//new StringBuffer();
		StringBuffer TestCode = null;
		StringBuffer TestValue = null;
		StringBuffer TestCode1;
		StringBuffer TestCode2;
		StringBuffer TestCode3;
		StringBuffer TestCode4;
		StringBuffer TestCode5;

		System.out.println("size:-- " + list.size());
		String stcode;
		String sTvalue;
		ABCbkp aBCbkp = new ABCbkp();

		int r = 0;
		for (String line : list) {
			r++;
			if (line.length() == 0 || line == null)
				break;
			TestCode = new StringBuffer();
			TestValue = new StringBuffer();
			TestCode1 = new StringBuffer();
			TestCode2 = new StringBuffer();
			TestCode3 = new StringBuffer();// only MTBRIF
			TestCode4 = new StringBuffer();// only SARScov2
			TestCode5 = new StringBuffer();// only SARScov2

			System.out.println("line.length() " + line.length());
			if (line.length() > 2) {
				if (Character.toUpperCase(line.charAt(2)) == 'O') {

					String[] parts = line.split("\\|");

					// Check if the parts array has at least three elements
					if (parts.length >= 3) {
						// The value after the second pipe '|' is at index 2
						if(Server.formatid.equals("50001")) {
							Server.sampleName = parts[3];
							Server.sampleName=Server.sampleName.replaceAll(" ", "");
							String ab[]=Server.sampleName.split("\\^");
							Server.sampleName=ab[2];
							//TestCode.append(valueAfterSecondPipe);
							System.out.println("Value after the second pipe: " + Server.sampleName.toString().replaceAll("\\^", ""));
						}
						else {

							Server.sampleName = parts[2];
						}

					}

					System.out.println("Server.sampleName===========:- " + Server.sampleName);// Sample ID Read between '|' and '^'

				}

				String valueAfterSecondPipe=null;
				String valueAfterThirdPipe=null;
				String unit="";
				if (Character.toUpperCase(line.charAt(2)) == 'R') {



					String[] parts = line.split("\\|");
					unit =parts[4];
					// Check if the parts array has at least three elements
					if (parts.length >= 3) {
						// The value after the second pipe '|' is at index 2
						valueAfterSecondPipe = parts[2];
						String [] abc= valueAfterSecondPipe.split("\\^");
						valueAfterSecondPipe=abc[3];

						if(!Server.formatid.equals("50001")) {
							if(unit.equalsIgnoreCase("sec")) {
								TestCode.append(valueAfterSecondPipe+"sec");
							}
							if(unit.equalsIgnoreCase("INR")) {
								TestCode.append(valueAfterSecondPipe+"INR");
							}
						}
						else {
							TestCode.append(valueAfterSecondPipe);

						}
						//}
						System.out.println("Value after the second pipe: " + valueAfterSecondPipe.replaceAll("\\^", ""));
					}
					//System.out.println(parts[2]);

					if (parts.length >= 3) {
						// The value after the second pipe '|' is at index 2
						valueAfterThirdPipe = parts[3];
						valueAfterThirdPipe=  valueAfterThirdPipe.replaceAll("\\s", "");

						valueAfterSecondPipe = parts[2];
						TestValue.append(valueAfterThirdPipe);
						System.out.println("Value after the third pipe: " + valueAfterThirdPipe.replaceAll("\\^", ""));
					}



				}


				if (!TestCode.toString().trim().equals("") && !(list.size()==2)) {

					testCode.add(TestCode.toString().trim());

					double val = Double.parseDouble(TestValue.toString().trim());
					String formattedval = String.format("%.2f", val);
					testvalue.add(formattedval);
					//aBCbkpinsert_SysmexXN350();
					ABCbkp.insert_SysmexXN350A(TestCode.toString().trim(), formattedval,
							Server.sampleName.toString().trim());
				}
			}

		}

	}




	//Sysmax Xn 1000 CBC Machine




	public static void sysmaxXl100BHU(List<String> list) {    // AIIMS Bhubneswar Sysmax 2500 Format ID 100017
		Map<String, List> mp = new HashMap();
		List lis = new ArrayList();
		List lis1 = new ArrayList();
		String ip = "";

		List testCode = new ArrayList<>();
		List testvalue =new ArrayList<>();

		//String sampleName = null;//new StringBuffer();
		StringBuffer TestCode = null;
		StringBuffer TestValue = null;
		StringBuffer TestCode1;
		StringBuffer TestCode2;
		StringBuffer TestCode3;
		StringBuffer TestCode4;
		StringBuffer TestCode5;

		System.out.println("size:-- " + list.size());
		String stcode;
		String sTvalue;
		ABCbkp aBCbkp = new ABCbkp();

		int r = 0;
		for (String line : list) {
			r++;
			if (line.length() == 0 || line == null)
				break;
			TestCode = new StringBuffer();
			TestValue = new StringBuffer();
			TestCode1 = new StringBuffer();
			TestCode2 = new StringBuffer();
			TestCode3 = new StringBuffer();// only MTBRIF
			TestCode4 = new StringBuffer();// only SARScov2
			TestCode5 = new StringBuffer();// only SARScov2

			System.out.println("line.length() " + line.length());
			if (line.length() > 2) {
				if (Character.toUpperCase(line.charAt(2)) == 'O') {
				
					String[] parts = line.split("\\|");
	if (parts.length >= 3) {
				
						Server.sampleName = parts[3];
						Server.sampleName=Server.sampleName.replaceAll(" ", "");
						String ab[]=Server.sampleName.split("\\^");
						Server.sampleName=ab[2];
						//TestCode.append(valueAfterSecondPipe);
						System.out.println("Value after the second pipe: " + Server.sampleName.toString().replaceAll("\\^", ""));
					}

					System.out.println("SampleName===========:- " + Server.sampleName);// Sample ID Read between '|' and '^'

				}

				// if(Test_Name1.equals("MTBRIF")) { parse(list); } else { continue; }



				String valueAfterSecondPipe=null;
				String valueAfterThirdPipe=null;

				if (Character.toUpperCase(line.charAt(2)) == 'R') {



					String[] parts = line.split("\\|");

					// Check if the parts array has at least three elements
					if (parts.length >= 3) {
						// The value after the second pipe '|' is at index 2
						valueAfterSecondPipe = parts[2];
						// valueAfterSecondPipe= valueAfterSecondPipe.replaceAll("\\^1", "");
						String [] abc= valueAfterSecondPipe.split("\\^");
						valueAfterSecondPipe=abc[4];

						//if(valueAfterSecondPipe.equals("BASO%") || valueAfterSecondPipe.equals("EO%") || valueAfterSecondPipe.equals("MONO%") || valueAfterSecondPipe.equals("LYMPH%") || valueAfterSecondPipe.equals("NEUT%") || valueAfterSecondPipe.equals("WBC")|| valueAfterSecondPipe.equals("NEU#") || valueAfterSecondPipe.equals("LYM#") || valueAfterSecondPipe.equals("MON#") || valueAfterSecondPipe.equals("EOS#") || valueAfterSecondPipe.equals("BAS#") || valueAfterSecondPipe.equals("IMG#") || valueAfterSecondPipe.equals("NEU%") || valueAfterSecondPipe.equals("LYM%") || valueAfterSecondPipe.equals("MON%") || valueAfterSecondPipe.equals("EOS%") || valueAfterSecondPipe.equals("BAS%") || valueAfterSecondPipe.equals("IMG%") || valueAfterSecondPipe.equals("RBC") || valueAfterSecondPipe.equals("HGB") || valueAfterSecondPipe.equals("HCT") || valueAfterSecondPipe.equals("MCV") || valueAfterSecondPipe.equals("MCH") || valueAfterSecondPipe.equals("MCHC") || valueAfterSecondPipe.equals("RDW-CV") || valueAfterSecondPipe.equals("RDW-SD") || valueAfterSecondPipe.equals("PLT") || valueAfterSecondPipe.equals("MPV") || valueAfterSecondPipe.equals("PDW") || valueAfterSecondPipe.equals("PCT") || valueAfterSecondPipe.equals("PLCC") || valueAfterSecondPipe.equals("NRBC#") || valueAfterSecondPipe.equals("NRBC%") || valueAfterSecondPipe.equals("PLCR") ) {
						if(valueAfterSecondPipe.equals("PCV") || valueAfterSecondPipe.equals("IG%") || valueAfterSecondPipe.equals("IG#") || valueAfterSecondPipe.equals("BASO#") || valueAfterSecondPipe.equals("BASO#") || valueAfterSecondPipe.equals("EO#") || valueAfterSecondPipe.equals("LYMPH#")  || valueAfterSecondPipe.equals("MONO#")  || valueAfterSecondPipe.equals("NEUT#")  || valueAfterSecondPipe.equals("BASO%")  ||  valueAfterSecondPipe.equals("EO%") || valueAfterSecondPipe.equals("NEUT%") || valueAfterSecondPipe.equals("MONO%") || valueAfterSecondPipe.equals("LYMPH%") || valueAfterSecondPipe.equals("WBC")|| valueAfterSecondPipe.equals("NEU#") || valueAfterSecondPipe.equals("LYM#") || valueAfterSecondPipe.equals("MON#") || valueAfterSecondPipe.equals("EOS#") || valueAfterSecondPipe.equals("BAS#") || valueAfterSecondPipe.equals("IMG#") || valueAfterSecondPipe.equals("NEU%") || valueAfterSecondPipe.equals("LYM%") || valueAfterSecondPipe.equals("MON%") || valueAfterSecondPipe.equals("EOS%") || valueAfterSecondPipe.equals("BAS%") || valueAfterSecondPipe.equals("IMG%") || valueAfterSecondPipe.equals("RBC") || valueAfterSecondPipe.equals("HGB") || valueAfterSecondPipe.equals("HCT") || valueAfterSecondPipe.equals("MCV") || valueAfterSecondPipe.equals("MCH") || valueAfterSecondPipe.equals("MCHC") || valueAfterSecondPipe.equals("RDW-CV") || valueAfterSecondPipe.equals("RDW-SD") || valueAfterSecondPipe.equals("PLT") || valueAfterSecondPipe.equals("MPV") || valueAfterSecondPipe.equals("PDW") || valueAfterSecondPipe.equals("PCT") || valueAfterSecondPipe.equals("PLCC") || valueAfterSecondPipe.equals("NRBC#") || valueAfterSecondPipe.equals("NRBC%") || valueAfterSecondPipe.equals("PLCR") || valueAfterSecondPipe.equals("P-LCR") || valueAfterSecondPipe.equals("MXD%")|| valueAfterSecondPipe.equals("MXD#") ) {	
							TestCode.append(valueAfterSecondPipe);
						}
						System.out.println("Value after the second pipe: " + valueAfterSecondPipe.replaceAll("\\^", ""));
					}
					//System.out.println(parts[2]);

					if (parts.length >= 3) {
						// The value after the second pipe '|' is at index 2
						valueAfterThirdPipe = parts[3];
						valueAfterThirdPipe=  valueAfterThirdPipe.replaceAll("\\^", "");
					
						valueAfterSecondPipe = parts[2];
						// valueAfterSecondPipe= valueAfterSecondPipe.replaceAll("\\^", "");
						String [] abc= valueAfterSecondPipe.split("\\^");
						valueAfterSecondPipe=abc[4];
						//if(valueAfterSecondPipe.equals("BASO%")  ||  valueAfterSecondPipe.equals("EO%") || valueAfterSecondPipe.equals("NEUT%") || valueAfterSecondPipe.equals("MONO%") || valueAfterSecondPipe.equals("LYMPH%") || valueAfterSecondPipe.equals("WBC")|| valueAfterSecondPipe.equals("NEU#") || valueAfterSecondPipe.equals("LYM#") || valueAfterSecondPipe.equals("MON#") || valueAfterSecondPipe.equals("EOS#") || valueAfterSecondPipe.equals("BAS#") || valueAfterSecondPipe.equals("IMG#") || valueAfterSecondPipe.equals("NEU%") || valueAfterSecondPipe.equals("LYM%") || valueAfterSecondPipe.equals("MON%") || valueAfterSecondPipe.equals("EOS%") || valueAfterSecondPipe.equals("BAS%") || valueAfterSecondPipe.equals("IMG%") || valueAfterSecondPipe.equals("RBC") || valueAfterSecondPipe.equals("HGB") || valueAfterSecondPipe.equals("HCT") || valueAfterSecondPipe.equals("MCV") || valueAfterSecondPipe.equals("MCH") || valueAfterSecondPipe.equals("MCHC") || valueAfterSecondPipe.equals("RDW-CV") || valueAfterSecondPipe.equals("RDW-SD") || valueAfterSecondPipe.equals("PLT") || valueAfterSecondPipe.equals("MPV") || valueAfterSecondPipe.equals("PDW") || valueAfterSecondPipe.equals("PCT") || valueAfterSecondPipe.equals("PLCC") || valueAfterSecondPipe.equals("NRBC#") || valueAfterSecondPipe.equals("NRBC%") || valueAfterSecondPipe.equals("PLCR") ) {


						if( valueAfterSecondPipe.equals("PCV") ||valueAfterSecondPipe.equals("IG%") || valueAfterSecondPipe.equals("IG#") || valueAfterSecondPipe.equals("BASO#") || valueAfterSecondPipe.equals("BASO#") || valueAfterSecondPipe.equals("EO#") || valueAfterSecondPipe.equals("LYMPH#")  || valueAfterSecondPipe.equals("MONO#")  || valueAfterSecondPipe.equals("NEUT#")  || valueAfterSecondPipe.equals("BASO%")  ||  valueAfterSecondPipe.equals("EO%") || valueAfterSecondPipe.equals("NEUT%") || valueAfterSecondPipe.equals("MONO%") || valueAfterSecondPipe.equals("LYMPH%") || valueAfterSecondPipe.equals("WBC")|| valueAfterSecondPipe.equals("NEU#") || valueAfterSecondPipe.equals("LYM#") || valueAfterSecondPipe.equals("MON#") || valueAfterSecondPipe.equals("EOS#") || valueAfterSecondPipe.equals("BAS#") || valueAfterSecondPipe.equals("IMG#") || valueAfterSecondPipe.equals("NEU%") || valueAfterSecondPipe.equals("LYM%") || valueAfterSecondPipe.equals("MON%") || valueAfterSecondPipe.equals("EOS%") || valueAfterSecondPipe.equals("BAS%") || valueAfterSecondPipe.equals("IMG%") || valueAfterSecondPipe.equals("RBC") || valueAfterSecondPipe.equals("HGB") || valueAfterSecondPipe.equals("HCT") || valueAfterSecondPipe.equals("MCV") || valueAfterSecondPipe.equals("MCH") || valueAfterSecondPipe.equals("MCHC") || valueAfterSecondPipe.equals("RDW-CV") || valueAfterSecondPipe.equals("RDW-SD") || valueAfterSecondPipe.equals("PLT") || valueAfterSecondPipe.equals("MPV") || valueAfterSecondPipe.equals("PDW") || valueAfterSecondPipe.equals("PCT") || valueAfterSecondPipe.equals("PLCC") || valueAfterSecondPipe.equals("NRBC#") || valueAfterSecondPipe.equals("NRBC%") || valueAfterSecondPipe.equals("PLCR") || valueAfterSecondPipe.equals("P-LCR") || valueAfterSecondPipe.equals("MXD%")|| valueAfterSecondPipe.equals("MXD#")) {	
							TestValue.append(valueAfterThirdPipe);
							//aBCbkp.insert_GenExpert(TestCode.toString().trim(),TestValue.toString().trim(),sampleName.toString().trim());

						}
						System.out.println("Value after the third pipe: " + valueAfterThirdPipe.replaceAll("\\^", ""));
					}

				}


				if (!TestCode.toString().trim().equals("") && !(list.size()==2)) {

					testCode.add(TestCode.toString().trim());
					testvalue.add(TestValue.toString().trim());
					//aBCbkpinsert_SysmexXN350();

				}
			}

		}
		mp.put("TestCode", testCode);
		mp.put("TestValue", testvalue);

		aBCbkp.insert_SysmexXN350(mp,Server.sampleName.toString().trim());


	}









	//endd
	public static void SysmexXN2500(List<String> list) // SysmexXN350
	{  System.out.println("inside 20017");
	Map<String, List> mp = new HashMap();
	List lis = new ArrayList();
	List lis1 = new ArrayList();
	String ip = "";
	StringBuffer sampleName = new StringBuffer();
	StringBuffer TestCode = null;
	StringBuffer TestCode1 = null;
	StringBuffer TestValue;
	StringBuffer TestValue1;
	System.out.println("size:-- " + list.size());
	String stcode;
	String sTvalue;
	ABCbkp aBCbkp = new ABCbkp();

	int r = 0;
	for (String line : list) {
		r++;
		if (line.length() == 0 || line == null)
			break;
		TestCode = new StringBuffer();
		TestCode1 = new StringBuffer();
		TestValue = new StringBuffer();
		TestValue1 = new StringBuffer();

		//System.out.println("line.length() " + line.length());
		if (line.length() > 2) {
			if (Character.toUpperCase(line.charAt(2)) == 'O') {
				System.out.println(
						" Character.toUpperCase(line.charAt(2))========" + Character.toUpperCase(line.charAt(2)));

				char[] ch = line.toCharArray();
				int count = 0;
				int countCR = 0;
				for (int i = 0; i < ch.length; i++) {

					if (ch[i] == '|')
						count++;
					if (count == 3 && (ch[i] == '^')) {
						countCR++;

						if (countCR == 2)
							i++;

					}

					if (countCR == 2) {
						if (!(ch[i] == '^'))
							sampleName.append(ch[i]);
					}
					if (countCR == 3)
						break;
				}

				System.out.println("SampleID:- " + sampleName.toString().replace("^", ""));

			}

			if (Character.toUpperCase(line.charAt(2)) == 'R') {
				System.out.println(
						" Character.toUpperCase(line.charAt(2))========" + Character.toUpperCase(line.charAt(2)));

				char[] ch = line.toCharArray();
				int count = 0;
				int countAs = 0;
				for (int i = 0; i < ch.length; i++) {

					if (ch[i] == '|')
						count++;

					if (count == 2) {
						if (!(ch[i] == '|'))
							TestCode.append(ch[i]);
					}

					if (count == 2 && (ch[i] == '^')) {
						countAs++;
					}

					if (countAs == 3) {

						if (!(ch[i] == '^'))
							TestCode1.append(ch[i]);
					}

					if (count == 3) {
						if (!(ch[i] == '|'))
							TestValue.append(ch[i]);
					}

					if (count == 4) {
						if (!(ch[i] == '|'))
							TestValue1.append(ch[i]);
					}

				}

			}

			StringBuffer Tv = new StringBuffer();
			String Tc = (TestCode1.toString().replace("^", "")).trim();
			System.out.println("TestCode :- " + Tc);
			System.out.println("TestCode1 :- " + TestCode1);
			System.out.println("TestValue :- " + TestValue);
			System.out.println("TestValue1 :- " + TestValue1);
			if (!Tc.trim().equals("") && r > 1) {

				if (TestValue.toString().contains("-")) {

					String Tv_db = (TestValue.toString().replace("-", " "));
					TestValue = TestValue.delete(0, TestValue.length());

					TestValue.append(Tv_db);
					System.out.println("Updated TestValue :- " + TestValue);
				}

				// String.format("%.2f", input)
				// -------------------------------
				if (Tc.equals("WBC")) {

					double Tv_db = Double.parseDouble(TestValue.toString());
					TestValue = TestValue.delete(0, TestValue.length());
					Tv_db = Tv_db * 1000;
					// Tv.append(df.format(String.valueOf(Tv_db)));
					Tv.append(String.format("%.2f", Tv_db));
					System.out.println("Updated TestValue :- " + Tv);
					TestValue.append((Tv.toString()).substring(0, Tv.length() - 3));

					System.out.println("Updated TestValue :- " + TestValue);
				}
				if (Tc.equals("PLT")) {
					double Tv_db = Double.parseDouble(TestValue.toString());
					TestValue = TestValue.delete(0, TestValue.length());
					Tv_db = Tv_db / 100;
					// Tv.append(df.format(String.valueOf(Tv_db)));
					Tv.append(String.format("%.2f", Tv_db));
					TestValue.append(Tv.toString());
					;
					System.out.println("Updated TestValue :- " + TestValue);

				}

				System.out.println("r value=" + r);
				System.out.println("TestCode" + Tc);
				aBCbkp.insert_SysmexXN350A(Tc, TestValue.toString().replace("^", "").trim(),
						sampleName.toString().replace("^", "").trim());
			}
		}
	}

	}





}
