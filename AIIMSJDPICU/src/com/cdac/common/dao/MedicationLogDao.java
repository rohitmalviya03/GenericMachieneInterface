/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.dao;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.cdac.common.GeneratedEntities.Fluidvalue;
import com.cdac.common.GeneratedEntities.IntraopMedicationlog;
import com.cdac.common.pojoClasses.MedicationVolumeUpdation;
import com.cdac.common.pojoClasses.Volume;

// TODO: Auto-generated Javadoc
/**
 * The Interface EventLogDao.
 */
public interface MedicationLogDao {

	/**
	 * This method returns IntraopMedicationlog object after saving it into the database
	 * from IntraopMedicationlog entity
	 *
	 * @param medicationLog
	 * - the medication to be saved
	 *
	 * @param userName.
	 *
	 *
	 * @return IntraopMedicationlog object after saving it into the database
	 *
	 */
	public IntraopMedicationlog saveMediactions(IntraopMedicationlog medicationLog, String userName) throws Exception;


	/**
	 * This method returns string containing success/error for deletion of medication
	 * from IntraopMedicationlog entity
	 *
	 * @param medicationLogID
	 * -ID of the the medication to be deleted
	 *
	 * @param userName.
	 *
	 *
	 * @return string containing success/error for deletion of medication
	 *
	 */
	public String deleteMedicationLog(Long medicationLogID, String userName) throws Exception;

	/**
	 * This method returns list of medication corresponding to the patientID and caseID
	 * from IntraopMedicationlog entity
	 *
	 * @param patientId
	 *
	 * @param caseId
	 *
	 * @param userName.
	 *
	 *
	 * @return list of medication corresponding to the patientID and caseID
	 *
	 */
	public List<IntraopMedicationlog> getAllMedicationsForPatientCase(Long caseID, String userName) throws Exception;

	/**
	 * This method returns medication which is to be updated
	 * from IntraopMedicationlog entity
	 *
	 * @param medicationLogId
	 * - ID of the medication to be updated
	 *
	 * @param userName.
	 *
	 *
	 * @return medication which is to be updated
	 *
	 */
	public IntraopMedicationlog getMedicationLogForUpdate(Long medicationLogId, String userName) throws Exception;


	/**
	 * This method returns Volume object containing the total volume of the medication given to the patient
	 * from IntraopMedicationlog entity
	 *
	 * @param medicationName
	 * - Name of the medication whose total volume is to calculated
	 *
	 * @param patientId
	 *
	 *
	 * @param caseId
	 *
	 * @param userName.
	 *
	 *
	 * @return Volume object containing the total volume of the medication given to the patient
	 *
	 *//*
	public Volume getTotalVolume(String medicationName ,int patientID, Long caseID, String userName) throws Exception;*/


	/**
	 * This method returns Volume object containing the total dosage of the medication given to the patient
	 * from IntraopMedicationlog entity
	 *
	 * @param medicationName
	 * - Name of the medication whose total dosage is to calculated
	 *
	 * @param patientId
	 *
	 *
	 * @param caseId
	 *
	 * @param userName.
	 *
	 *
	 * @return Volume object containing the total dosage of the medication given to the patient
	 *
	 */
	public Volume getTotalDosage(String medicationName ,int patientID, Long caseID, String userName) throws Exception;
	/**
	 * This method returns Volume object containing the total dosage of the medication given to the patient
	 * from IntraopMedicationlog entity
	 *
	 * @param medicationName
	 * - Name of the medication whose total dosage is to calculated
	 *
	 * @param patientId
	 *
	 *
	 * @param caseId
	 *
	 * @param userName.
	 *
	 *
	 * @return Volume object containing the total dosage of the medication given to the patient
	 *
	 */
	public HashMap<String,String> getTotalDosageListOfMedication(List<String> medicationName ,int patientID, Long caseID, String userName) throws Exception;

	/**
	 * This method returns success/error message for updation of medication record
	 * from IntraopMedicationlog entity
	 *
	 * @param listOfmedicationForUpdation
	 * - list of medication for updation object containing value to be updated

	 * @param userName.
	 *
	 *
	 * @return success/error message for updation of medication record
	 *
	 */
	public String updateMedication(List<MedicationVolumeUpdation> listOfmedicationForUpdation, String userName) throws Exception;
	/**
	 * This method returns success/error message for updation of medication record
	 * from IntraopMedicationlog entity
	 *
	 * @param listOfmedicationForUpdation
	 * - list of medication for updation object containing value to be updated

	 * @param userName.
	 *
	 *
	 * @return success/error message for updation of medication record
	 *
	 */
	public IntraopMedicationlog getIntraopMedicationlog(String medName,long caseId,int patientId,Date timeStamp, String type, String userName) throws Exception;

	List<IntraopMedicationlog> getTimedMedicationsForPatientCase(Long caseID, Date startTime, Date endTime, String userName)
			throws Exception;

	public List<IntraopMedicationlog> getMedicationsForPatientCase(Long caseID, String userName)
			throws Exception;

	public List<Fluidvalue> getFluidsForPatientCase(Long caseID, String userName)
			throws Exception;


	Boolean getStartFlag(String medName, long caseId, int patientId, Date timeStamp, String type, String userName)
			throws Exception;


	void settingEndTimeinLastDose(String medName, long caseId, int patientId, Date timeStamp, String type,
			String userName) throws Exception;


	List<String> getMedicationList(long caseId, int patientId, String userName) throws Exception;


	Date getStartTime(String medName, long caseId, int patientId,  String type, String userName)
			throws Exception;
}

