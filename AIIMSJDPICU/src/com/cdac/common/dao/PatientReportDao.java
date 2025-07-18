/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.dao;

import java.util.Map;

import org.json.simple.JSONObject;

import com.cdac.common.GeneratedEntities.IntraopReportFile;


public interface PatientReportDao {

	public Map<String, Object> getPatientAndCaseDetailsReport(Long caseID, Integer patientID, String userName) throws Exception;
	public Map<String, Object> getEventDetailsForReport(Long caseID, Integer patientID, String userName) throws Exception;
	public Map<String, Object> getTotalOfDrugsForReport(Long caseID, Integer patientID, String userName) throws Exception;
	public Map<String, Object> getTotalOfFluidsForReport(Long caseID, Integer patientID, String userName) throws Exception;
	public Map<String, Object> getTotalOfOutputsForReport(Long caseID, Integer patientID, String userName) throws Exception;
	public Map<String, Object> getAnesthesiaDetailsForReport(Long caseID, Integer patientID, String userName) throws Exception;
	public Map<String, Object> getPostOpInstructionsForReport(Long caseID, Integer patientID, String userName) throws Exception;
	public JSONObject uploadFile(String userName, IntraopReportFile intraopReportFile);
	public IntraopReportFile getFile(Long caseId);
}
