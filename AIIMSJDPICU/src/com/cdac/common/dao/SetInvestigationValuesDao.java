/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.dao;

import java.util.Date;
import java.util.List;

import com.cdac.common.pojoClasses.InvestigationSetValue;

public interface SetInvestigationValuesDao 
{
	
	/**
	 * This method returns string containing success/error message for saving the test value
	 * from Investigationsvalues entity
	 * 
	 * @param caseId
	 * 
	 * @param testID
	 * -ID of the test whose values is to be saved
	 * 
	 * @param investigationsValues
	 * - list of values of the test
	 * 
	 * @param testDate
	 * 
	 * @param details
	 * @param userName.
	 *
	 * @return map list of devices configured w.r.t. patientID and caseID.
	 * 
	 */
	public String setInvestigationValues(Long caseID,Integer testID,List<InvestigationSetValue> investigationsValues,Date testDate,String details,String userName) throws Exception;
}
