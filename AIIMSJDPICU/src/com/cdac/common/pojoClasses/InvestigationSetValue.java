/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.pojoClasses;

import java.math.BigDecimal;

import com.cdac.common.GeneratedEntities.Investigationmeasuringunits;

public class InvestigationSetValue
{

	private String Name;
	private BigDecimal value;
	private Investigationmeasuringunits measuringUnit;
	private Integer investigationParameterFieldID;
	private Integer exponentValue;


	public Integer getExponentValue() {
		return exponentValue;
	}


	public void setExponentValue(Integer exponentValue) {
		this.exponentValue = exponentValue;
	}


	public InvestigationSetValue(String name, BigDecimal value) {
		super();
		Name = name;
		this.value = value;
	}


	public InvestigationSetValue(String name, BigDecimal value, Integer investigationParameterFieldID) {
		super();
		Name = name;
		this.value = value;
		this.investigationParameterFieldID = investigationParameterFieldID;
	}


	public Integer getInvestigationParameterFieldID() {
		return investigationParameterFieldID;
	}


	public void setInvestigationParameterFieldID(Integer investigationParameterFieldID) {
		this.investigationParameterFieldID = investigationParameterFieldID;
	}


	public InvestigationSetValue() {
		super();

	}


	public String getName() {
		return Name;
	}


	public void setName(String name) {
		Name = name;
	}


	public BigDecimal getValue() {
		return value;
	}


	public void setValue(BigDecimal value) {
		this.value = value;
	}


	public Investigationmeasuringunits getMeasuringUnit() {
		return measuringUnit;
	}


	public void setMeasuringUnit(Investigationmeasuringunits measuringUnit) {
		this.measuringUnit = measuringUnit;
	}





}
