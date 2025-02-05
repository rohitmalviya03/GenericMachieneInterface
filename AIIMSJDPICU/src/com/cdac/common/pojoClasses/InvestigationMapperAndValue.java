/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.pojoClasses;

import java.util.List;

import com.cdac.common.GeneratedEntities.Investigationvaluescasemapper;

public class InvestigationMapperAndValue 
{
	Investigationvaluescasemapper investigationvaluescasemapper;
	List<InvestigationSetValue> investigationSetValueList;
	
	
	public InvestigationMapperAndValue() 
	{
		super();
		// TODO Auto-generated constructor stub
	}


	public InvestigationMapperAndValue(Investigationvaluescasemapper investigationvaluescasemapper,
			List<InvestigationSetValue> investigationSetValueList) {
		super();
		this.investigationvaluescasemapper = investigationvaluescasemapper;
		this.investigationSetValueList = investigationSetValueList;
	}


	public Investigationvaluescasemapper getInvestigationvaluescasemapper() {
		return investigationvaluescasemapper;
	}


	public void setInvestigationvaluescasemapper(Investigationvaluescasemapper investigationvaluescasemapper) {
		this.investigationvaluescasemapper = investigationvaluescasemapper;
	}


	public List<InvestigationSetValue> getInvestigationSetValueList() {
		return investigationSetValueList;
	}


	public void setInvestigationSetValueList(List<InvestigationSetValue> investigationSetValueList) {
		this.investigationSetValueList = investigationSetValueList;
	}
	
	

}
