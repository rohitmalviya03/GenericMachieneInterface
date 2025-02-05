/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.dao;

import java.util.Map;

import com.cdac.common.GeneratedEntities.Patientcase;

// TODO: Auto-generated Javadoc
/**
 * The Interface CreateCaseDao.
 */
public interface CreateCaseDao {

	/**
	 * The interface method for Create the case.
	 *
	 * @param createCase
	 *            the details of the case to be created
	 * @param patientID
	 *            the patient ID for the case
	 * @param userName
	 *            the logged in user
	 * @param surgeonsID
	 *            the surgeons ID for the case
	 * @param anesthesiologistID
	 *            the anesthesiologist ID for the case
	 * @param specialityID
	 *            the speciality ID for the case
	 * @param operationTheaterNoID
	 *            the operation theater no ID for the case
	 * @return the map - contains case details in case of successfully created a
	 *         case / error response if not created
	 * @throws Exception
	 */
	public Patientcase createCase(Patientcase createCase, Integer patientID, String userName/*,
	        List<Long> surgeonsID,
	        List<Long> anesthesiologistID, Integer specialityID, Integer operationTheaterNoID*/
			) throws Exception;

	/**
	 * Gets the creates the case entity for the given case id.
	 *
	 * @param caseId
	 *            the case id of the required case
	 * @return the patient case entity for given case id
	 * @throws Exception
	 */
	public Patientcase getCreateCaseEntity(Long caseId) throws Exception;
}