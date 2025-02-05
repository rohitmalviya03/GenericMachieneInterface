/*******************************************************************************
 * © 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package utility;

public class ParamMinMaxRange
{

	// ---S5PtMonitor Parameters
	public static final String HR = "HR";
	public static final String IBP = "IBP";
	public static final String NIBP = "NIBP";
	public static final String IBPSYS = "IBPSYS";
	public static final String IBPDIA = "IBPDIA";
	public static final String SPO2 = "SPO2";
	public static final String ETCO2 = "ETCO2";
	public static final String TEMP = "TEMP";
	public static final String TEMP1 = "TEMP1";
	public static final String TEMP2 = "TEMP2";
	public static final String BIS = "BIS";
	public static final String ETAGENT = "ETAGENT";
	public static final String MAC = "MAC";
	public static final String IBPMEAN = "IBPMEAN";
	public static final String NIBPMEAN = "NIBPMEAN";
	public static final String NIBPDIA = "NIBPDIA";
	public static final String NIBPSYS = "NIBPSYS";

	public static final String HR_LEGEND = "\u25CF";
	public static final String IBPSYS_LEGEND = "v";
	public static final String IBPDIA_LEGEND = "^";
	public static final String SPO2_LEGEND = "+";
	public static final String ETCO2_LEGEND = "\u25CF";
	public static final String TEMP1_LEGEND = "x";
	public static final String TEMP2_LEGEND = "\u2666";
	public static final String BIS_LEGEND = "\u25B2";
	public static final String ETAGENT_LEGEND = "\u2605";
	public static final String MAC_LEGEND = "\u2680";
	public static final String IBPMEAN_LEGEND = "+";
	public static final String NIBPMEAN_LEGEND = "*";
	public static final String NIBPSYS_LEGEND = "#";
	public static final String NIBPDIA_LEGEND = "@";

	public static String HR_COLOR = "#000000";
	public static String IBP_COLOR = "#9b1168";
	public static String SPO2_COLOR = "#5a72bc";
	public static String ETCO2_COLOR = "#27abc4";
	public static String TEMP_COLOR = "#27abc4";
	public static String BIS_COLOR = "#27abc4";
	public static String ETAGENT_COLOR = "#000000";
	public static String MAC_COLOR = "#000000";
	public static String NIBP_COLOR = "#28119b";
	


	public static final double HR_MAX = 400;
	public static final double IBP_MAX = 300;
	public static final double SPO2_MAX = 100;
	public static final double ETCO2_MAX = 100;
	public static final double TEMP_MAX = 400;
	public static final double BIS_MAX = 100;
	public static final double ETAGENT_MAX = 400;
	public static final double MAC_MAX = 10;
	public static final double NIBP_MAX = 300;

	public static final String HR_UNIT = "(bpm)";
	public static final String IBP_UNIT = "(mmHg)";
	public static final String NIBP_UNIT = "(mmHg)";
	public static final String SPO2_UNIT = "(%)";
	public static final String ETCO2_UNIT = "(mmHg)";
	public static final String TEMP_UNIT = "(\u00B0C)";
	public static final String BIS_UNIT = "";
	public static final String ETAGENT_UNIT = "";
	public static final String MAC_UNIT = "";


	// ---Anesthesia Parameters
	public static final String PPEAK = "PPEAK";
	public static final String PMEAN = "PMEAN";
	public static final String PEEPEXT = "PEEPEXT";
	public static final String PEEP = "PEEP";
	public static final String CIRCUITO2 = "CIRCUITO2";
	public static final String MVEXP = "MVEXP";
	public static final String TVEXP = "TVEXP";
	public static final String RR = "RR";

	public static String PPEAK_COLOR = "#000000";
	public static String PMEAN_COLOR = "#000000";
	public static String PEEPEXT_COLOR = "#27abc4";
	public static String CIRCUITO2_COLOR = "#000000";
	public static String MVEXP_COLOR = "#e20d94";
	public static String TVEXP_COLOR = "#5a72bc";
	public static String RR_COLOR = "#9b1168";

	public static final String PPEAK_LEGEND = "+";
	public static final String PMEAN_LEGEND = "\u2666";
	public static final String PEEPEXT_LEGEND = "\u25CF";
	public static final String CIRCUITO2_LEGEND = "x";
	public static final String MVEXP_LEGEND = "^";// up triangle
	public static final String TVEXP_LEGEND = "+";
	public static final String RR_LEGEND = "v";// down triangle

	public static final double PPEAK_MAX = 100;
	public static final double PMEAN_MAX = 50;
	public static final double PEEP_EXT_MAX = 50;
	public static final double CIRCUITO2_MAX = 100;
	public static final double MVEXP_MAX = 50;
	public static final double TVEXP_MAX = 1000;
	public static final double RR_MAX = 100;

	public static final String PPEAK_UNIT = "(cmH20)";
	public static final String PMEAN_UNIT = "(cmH20)";
	public static final String PEEPEXT_UNIT = "(cmH2O)";
	public static final String CIRCUITO2_UNIT = "(%)";
	public static final String MVEXP_UNIT = "(l/min)";
	public static final String TVEXP_UNIT = "(ml)";
	public static final String RR_UNIT = "(rpm)";

	public static void setHR_COLOR(String hR_COLOR)
	{
		HR_COLOR = hR_COLOR;
	}
	public static void setIBP_COLOR(String iBP_COLOR)
	{
		IBP_COLOR = iBP_COLOR;
	}
	public static void setSPO2_COLOR(String sPO2_COLOR)
	{
		SPO2_COLOR = sPO2_COLOR;
	}
	public static void setETCO2_COLOR(String eTCO2_COLOR)
	{
		ETCO2_COLOR = eTCO2_COLOR;
	}
	public static void setTEMP_COLOR(String tEMP_COLOR)
	{
		TEMP_COLOR = tEMP_COLOR;
	}
	public static void setBIS_COLOR(String bIS_COLOR)
	{
		BIS_COLOR = bIS_COLOR;
	}
	public static void setETAGENT_COLOR(String eTAGENT_COLOR)
	{
		ETAGENT_COLOR = eTAGENT_COLOR;
	}
	public static void setMAC_COLOR(String mAC_COLOR)
	{
		MAC_COLOR = mAC_COLOR;
	}

	public static void setPPEAK_COLOR(String pPEAK_COLOR)
	{
		PPEAK_COLOR = pPEAK_COLOR;
	}
	public static void setPMEAN_COLOR(String pMEAN_COLOR)
	{
		PMEAN_COLOR = pMEAN_COLOR;
	}
	public static void setCIRCUITO2_COLOR(String cIRCUITO2_COLOR)
	{
		CIRCUITO2_COLOR = cIRCUITO2_COLOR;
	}
	public static void setMVEXP_COLOR(String mVEXP_COLOR)
	{
		MVEXP_COLOR = mVEXP_COLOR;
	}
	public static void setTVEXP_COLOR(String tVEXP_COLOR)
	{
		TVEXP_COLOR = tVEXP_COLOR;
	}
	public static void setRR_COLOR(String rR_COLOR)
	{
		RR_COLOR = rR_COLOR;
	}
	public static void setNIBP_COLOR(String nIBP_COLOR) {
		NIBP_COLOR = nIBP_COLOR;
	}
	public static void setPEEPEXT_COLOR(String pEEPEXT_COLOR)
	{
		PEEPEXT_COLOR = pEEPEXT_COLOR;
	}

	
}
