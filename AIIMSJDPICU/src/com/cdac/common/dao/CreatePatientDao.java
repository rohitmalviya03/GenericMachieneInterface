/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.dao;

import com.cdac.common.GeneratedEntities.Patient;

// TODO: Auto-generated Javadoc
/**
 * The Interface CreatePatientDao.
 */
public interface CreatePatientDao {

	/**
	 * Interface method for Create the patient.
	 *
	 * @param createPatientEntity
	 *            the patient entity 
	 * @param userName
	 *            the logged in user
	 * @return the patient entity created
	 */
	Patient createPatient(Patient createPatientEntity,String userName) throws Exception;

	/**
	 * This method takes Long CRNumber as input to check if this exists already
	 * and returns boolean false if found this CR Number.
	 *
	 * @param crNumber
	 *            the CR number to be checked for if already present
	 * @return booelean false in case CR number already exists else
	 *         empty
	 */
	boolean checkCrNumber(Long crNumber) throws Exception;

}