/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.dao;

import java.util.Date;
import java.util.List;

import com.cdac.common.pojoClasses.InvestigationMapperAndValue;
import com.cdac.common.pojoClasses.InvestigationSetValue;

public interface GetInvestigationValueViewDao 
{
	
	
//	public List<Investigationvaluescasemapper> getInvestigationvaluecasemapperlist(Long caseID,Integer testID,String userName) throws Exception;
	
	
	/**
	 * This method returns list containing test case mapper object and its related test values
	 * 
	 * @param caseID
	 * 
	 * @param testID
	 * 
	 * @param userName
	 * 
	 * @return list containing test case mapper object and its related test values
	 * 
	 */
	public InvestigationMapperAndValue getInvestigationvaluecasemapper(Long caseID,Integer testID,Date timeStamp,String userName) throws Exception;
	

	/**
	 * This method returns list containing test case mapper object and its related test values
	 * 
	 * @param caseID
	 * 
	 * @param testID
	 * 
	 * @param userName
	 * 
	 * @return list containing test case mapper object and its related test values
	 * 
	 */
	public List<InvestigationMapperAndValue> getInvestigationvaluecasemapperlist(Long caseID, Date startDate, Date endDate, String userName) throws Exception;
	
	/**
	 * This method returns list containing test values corresponding to test case mapper ID
	 * 
	 * @param investigationTestCasemapeperID
	 * 
	 * 
	 * @param userName
	 * 
	 * 
	 * @return list containing test values corresponding to test case mapper ID
	 * 
	 */
	
	public List<InvestigationSetValue> getInvestigationValueslist(Integer investigationTestCasemapeperID,String userName) throws Exception;
	/**
	 * This method returns list containing test values 
	 * 
	 * 
	 * 
	 * 
	 * @param userName
	 * 
	 * 
	 * @return list containing test values 
	 * 
	 */
	
	 public List<InvestigationSetValue> getInvestigationSetValueslist(String userName) throws Exception;

}
