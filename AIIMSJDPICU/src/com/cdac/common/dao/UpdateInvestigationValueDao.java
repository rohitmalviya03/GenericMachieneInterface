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

public interface UpdateInvestigationValueDao 
{
	
	/**
	 * This method returns string with success/error message for succesfull updation of test values record
	 * from Deviceconfiguration entity
	 * 
	 * @param investigationValueCaseMapperID
	 * ID of the test whose values is to updated
	 * 
	 * @param investigationsValues
	 * list of updated values
	 * 
	 * 
	 * @param userName.
	 * 
	 * @return string with success/error message for succesfull updation of test values record
	 * 
	 */
	public String updateInvestigationValue(Integer investigationValueCaseMapperID,List<InvestigationSetValue> investigationsValues,Date testDate,String details,String userName) throws Exception;
	
}
