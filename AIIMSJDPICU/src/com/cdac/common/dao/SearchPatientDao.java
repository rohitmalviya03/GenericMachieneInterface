/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.dao;

import java.util.Date;
import java.util.Map;

import com.cdac.common.GeneratedEntities.SearchPatientEntity;



/**
 * The Interface SearchPatientDao.
 */
public interface SearchPatientDao
{


	/**
	 * Interface method for Search patient.
	 *
	 * @param searchPatient
	 *            - the entity which will be populated with search criteria
	 * @param userName
	 *            - the logged in user
	 * @param pageNum
	 *            - page number for the results
	 * @param pageSize
	 *            - number of results to be displayed on the page
	 * @return the map - containing total results and list of patients for
	 *         matches found / contains error message for no match found
	 * @throws Exception
	 *             the exception
	 */
	Map<String, Object> searchPatient(SearchPatientEntity searchPatient, String userName, int pageNum, int pageSize)
	        throws Exception;

	public Map<String, Object> searchCases(String OTName, Date startDate, Date endDate, int pageNum,
	        int pageSize) throws Exception;

}