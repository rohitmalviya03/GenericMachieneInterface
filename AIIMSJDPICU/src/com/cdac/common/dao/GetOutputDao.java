/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.cdac.common.GeneratedEntities.IntraopOutput;

public interface GetOutputDao 
{
	
	/**
	 * This method returns ID of the output record which is saved or updated
	 * from IntraopOutput entity
	 * 
	 * 
	 * @param output
	 * - output object containing the output details which is to be saved
	 * 
	 * 
	 * 
	 * @param userName.
	 * 
	 * @return ID of the output record which is saved or updated
	 * 
	 */
	public Long saveOrUpdate(IntraopOutput output, String userName) throws Exception;
	
	/**
	 * This method returns List of output records corresponding to the patientID and caseID
	 * from IntraopOutput entity
	 * 
	 * 
	 * @param patientId
	 * 
	 * 
	 * @param caseId
	 * 
	 * 
	 * 
	 * @param userName.
	 * 
	 * @return List of output records corresponding to the patientID and caseID
	 * 
	 */
	public List<IntraopOutput> fetchOutputList(Long caseID, String userName) throws Exception;
	
	
	/**
	 * This method returns String containing success/error message for deleting the output record
	 * from IntraopOutput entity
	 * 
	 * 
	 * @param outputRecordId
	 * - ID of output record to be deleted
	 * 
	 * 
	 * 
	 * @param userName.
	 * 
	 * @return String containing success/error message for deleting the output record
	 * 
	 */
	public String deleteOutput(Long outputRecordId, String userName) throws Exception;
	
	/**
	 * This method returns String containing success/error message for deleting the output record
	 * from IntraopOutput entity
	 * 
	 * 
	 * @param outputRecordId
	 * - ID of output record to be deleted
	 * 
	 * 
	 * 
	 * @param userName.
	 * 
	 * @return String containing success/error message for deleting the output record
	 * 
	 */
	public IntraopOutput searchIntraOpOutput(Long caseId,String outputName,Date timeStamp,String userName) throws Exception;
	/**
	 * This method returns String containing success/error message for deleting the output record
	 * from IntraopOutput entity
	 * 
	 * 
	 * @param outputRecordId
	 * - ID of output record to be deleted
	 * 
	 * 
	 * 
	 * @param userName.
	 * 
	 * @return String containing success/error message for deleting the output record
	 * 
	 */
	public HashMap<String,String> getTotalOutputTypeVolume(Long caseId,List<String> outputNameList,String userName) throws Exception;
	
	public List<IntraopOutput> fetchTimedOutputList(Long caseID, Date startTime, Date endTime, String userName) throws Exception;
}
