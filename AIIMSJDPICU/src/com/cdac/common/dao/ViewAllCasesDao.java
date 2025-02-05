/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.dao;

import java.util.List;
import java.util.Map;

import com.cdac.common.GeneratedEntities.Patientcase;

// TODO: Auto-generated Javadoc
/**
 * The Interface ViewAllCasesDao.
 */
public interface ViewAllCasesDao {

	/**
	 * Interface method for View all cases.
	 *
	 * @param patientId
	 *            the patient id for the he cases list is required
	 * @param userName
	 *            the logged in user
	 * @param pageNum
	 *            the page num for the results
	 * @param pageSize
	 *            the page size - no. of results to be displayed on a page
	 * @param isClosed
	 *            the is closed - if the case is closed or open
	 * @return the map - with details of patient case in of results found, else
	 *         error response
	 * @throws Exception
	 */
	public Map<String, Object> viewAllCases(Integer patientId, String userName, Integer pageNum, Integer pageSize,
	        Boolean isClosed) throws Exception;
	
	public List<Object[]> viewAllPatientAndCases() throws Exception;
}
