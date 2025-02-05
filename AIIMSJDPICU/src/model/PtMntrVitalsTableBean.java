/*******************************************************************************
 * © 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package model;

import javafx.beans.property.SimpleStringProperty;

public class PtMntrVitalsTableBean
{
	private final SimpleStringProperty HR;
	private final SimpleStringProperty IBPSYS;
	private final SimpleStringProperty IBPDIA;
	private final SimpleStringProperty SPO2;
	private final SimpleStringProperty ETCO2;

	public PtMntrVitalsTableBean(String hR, String iBPSYS, String iBPDIA, String sPO2, String eTCO2)
	{
		HR = new SimpleStringProperty(hR);
		IBPSYS = new SimpleStringProperty(iBPSYS);
		IBPDIA = new SimpleStringProperty(iBPDIA);
		SPO2 = new SimpleStringProperty(sPO2);
		ETCO2 = new SimpleStringProperty(eTCO2);
	}

	public String getHR()
	{
		return HR.get();
	}

	public void setHR(String val)
	{
		HR.set(val);
	}

	public String getIBPSYS()
	{
		return IBPSYS.get();
	}

	public void setIBPSYS(String val)
	{
		IBPSYS.set(val);
	}

	public String getIBPDIA()
	{
		return IBPDIA.get();
	}

	public void setIBPDIA(String val)
	{
		IBPDIA.set(val);
	}

	public String getSPO2()
	{
		return SPO2.get();
	}

	public void setSPO2(String val)
	{
		SPO2.set(val);
	}

	public String getETCO2()
	{
		return ETCO2.get();
	}

	public void setETCO2(String val)
	{
		ETCO2.set(val);
	}
}
