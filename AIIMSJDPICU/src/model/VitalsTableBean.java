/*******************************************************************************
 * © 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package model;

import javafx.beans.property.SimpleStringProperty;

public class VitalsTableBean
{

	private final SimpleStringProperty parameter;
	private final SimpleStringProperty timeStamp1;
	private final SimpleStringProperty timeStamp2;
	private final SimpleStringProperty timeStamp3;
	private final SimpleStringProperty timeStamp4;
	private final SimpleStringProperty timeStamp5;
	private final SimpleStringProperty timeStamp6;
	private final SimpleStringProperty timeStamp7;
	private final SimpleStringProperty timeStamp8;
	private final SimpleStringProperty timeStamp9;
	private final SimpleStringProperty timeStamp10;

	public VitalsTableBean(String parameter, String timeStamp1, String timeStamp2, String timeStamp3, String timeStamp4,
	        String timeStamp5, String timeStamp6, String timeStamp7, String timeStamp8, String timeStamp9,
	        String timeStamp10)
	{
		this.parameter = new SimpleStringProperty(parameter);
		this.timeStamp1 = new SimpleStringProperty(timeStamp1);
		this.timeStamp2 = new SimpleStringProperty(timeStamp2);
		this.timeStamp3 = new SimpleStringProperty(timeStamp3);
		this.timeStamp4 = new SimpleStringProperty(timeStamp4);
		this.timeStamp5 = new SimpleStringProperty(timeStamp5);
		this.timeStamp6 = new SimpleStringProperty(timeStamp6);
		this.timeStamp7 = new SimpleStringProperty(timeStamp7);
		this.timeStamp8 = new SimpleStringProperty(timeStamp8);
		this.timeStamp9 = new SimpleStringProperty(timeStamp9);
		this.timeStamp10 = new SimpleStringProperty(timeStamp10);
	}

	public String getParameter()
	{
		return parameter.get();
	}

	public void setParameter(String val)
	{
		parameter.set(val);
	}

	public String getTimeStamp1()
	{
		return timeStamp1.get();
	}

	public void setTimeStamp1(String val)
	{
		timeStamp1.set(val);
	}

	public String getTimeStamp2()
	{
		return timeStamp2.get();
	}

	public void setTimeStamp2(String val)
	{
		timeStamp2.set(val);
	}

	public String getTimeStamp3()
	{
		return timeStamp3.get();
	}

	public void setTimeStamp3(String val)
	{
		timeStamp3.set(val);
	}

	public String getTimeStamp4()
	{
		return timeStamp4.get();
	}

	public void setTimeStamp4(String val)
	{
		timeStamp4.set(val);
	}

	public String getTimeStamp5()
	{
		return timeStamp5.get();
	}

	public void setTimeStamp5(String val)
	{
		timeStamp5.set(val);
	}

	public String getTimeStamp6()
	{
		return timeStamp6.get();
	}

	public void setTimeStamp6(String val)
	{
		timeStamp6.set(val);
	}

	public String getTimeStamp7()
	{
		return timeStamp7.get();
	}

	public void setTimeStamp7(String val)
	{
		timeStamp7.set(val);
	}

	public String getTimeStamp8()
	{
		return timeStamp8.get();
	}

	public void setTimeStamp8(String val)
	{
		timeStamp8.set(val);
	}

	public String getTimeStamp9()
	{
		return timeStamp9.get();
	}

	public void setTimeStamp9(String val)
	{
		timeStamp9.set(val);
	}

	public String getTimeStamp10()
	{
		return timeStamp10.get();
	}

	public void setTimeStamp10(String val)
	{
		timeStamp10.set(val);
	}

}
