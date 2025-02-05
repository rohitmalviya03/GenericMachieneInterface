/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.dao;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.cdac.common.GeneratedEntities.PatientMonitorData;

public interface PatientMonitorDataDao {

	/**
	 * Inserts a new record for anesthesia
	 * 
	 * @param anesthesiaRecord
	 * @param userName
	 * @return Anesthesia record entity
	 * @throws Exception
	 */
	void createPatientMonitorData(PatientMonitorData patientMonitorData,String userName)
	        throws Exception;
	
	/**
	 * Get Patient record details
	 * 
	 * @param patientId
	 * @param patientCaseId
	 * @param userName
	 * @return List of anesthesia record
	 * @throws Exception
	 */
	PatientMonitorData getPatientMonitorData(Long patientMonitorDataId, String userName)
	        throws Exception;
	
	PatientMonitorData getPatientMonitorDataTimeStampCaseID(Long caseId, Date timeStamp,String userName)
	        throws Exception;
	
	public List<PatientMonitorData> getTimedPatientMonitorData(Long caseId, Date startTime, Date endTime, String userName)
			 throws Exception;
	
	
	/**
	 * Get Patient record details
	 * 
	 * @param patientId
	 * @param patientCaseId
	 * @param userName
	 * @return List of anesthesia record
	 * @throws Exception
	 */
	List<PatientMonitorData> getAllPatientMonitorData(Long caseId, String userName)
	        throws Exception;

}

