/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.dao;


import java.util.List;

import com.cdac.common.GeneratedEntities.EventLogEventStatus;
import com.cdac.common.GeneratedEntities.IntraopEventlog;
import com.cdac.common.GeneratedEntities.SurgeryEventStatus;

// TODO: Auto-generated Javadoc
/**
 * The Interface EventLogDao.
 */
public interface EventLogDao {

	/**
	 * Interface method to Get the all events for the given surgery type.
	 * 
	 * @param surgeryID
	 *            the surgery ID for which the event list is required
	 * @param patientID
	 *            the patientID
	 * @param patientCaseID
	 *            patient case id
	 * @param userName
	 *            the logged in user
	 * @return List<SurgeryEventStatus>
	 * @throws Exception
	 */

	List<SurgeryEventStatus> getAllSurgeryEvents(String surgeryName, int patientID, Long patientCaseID,
	        String userName) throws Exception;

	/**
	 * Interface method To log a new event.
	 *
	 * @param intraopEventLog
	 *            the intraop event log entity with details of event to be
	 *            logged
	 * @param userName
	 *            the logged in user
	 * @return String
	 * @throws Exception
	 *             the exception
	 */
	IntraopEventlog createEventLog(IntraopEventlog intraopEventLog, String userName) throws Exception;
	
	/**
	 * Interface method to Gets the all logged events.
	 * 
	 * @param patientID
	 *            the patient ID for which event needs to be fetched
	 * @param caseID
	 *            the case ID of patient case
	 * @param surgeryID
	 *            the logged in user
	 * @param userName
	 * @return List<EventLogEventStatus>
	 * @throws Exception
	 *             the exception
	 */
	List<EventLogEventStatus> getAllEvents(int patientID, Long caseID, String surgeryName, String userName)
	        throws Exception;
	
	
	/**
	 * Interface method to Delete event log.
	 *
	 * @param eventLogID
	 *            the event log ID which needs to be deleted
	 * @param userName
	 *            the logged in user
	 * @return the string for success/fail
	 * @throws Exception
	 *             the exception
	 */
	String deleteEventLog(int eventLogID, String userName) throws Exception;
	
	/**
	 * Interface method to get event log record.
	 *
	 * @param eventLogId
	 *            the event log ID which needs to be retrieved
	 
	 * @param userName
	 *            the logged in user
	 * @return the IntraopEventLog object
	 * @throws Exception
	 *             the exception
	 */

	IntraopEventlog getEventLogRecord(int eventLogId, String userName) throws Exception;
	/**
	 * Interface method to set snapshot file name corresponding to event log.
	 *
	 * @param eventLogID
	 *            the event log ID which needs to be altered
	 * @param snapshotFileName
	 *            the snapshotFileName which needs to be saved
	 * @param userName
	 *            the logged in user
	 * @return the string for success/fail
	 * @throws Exception
	 *             the exception
	 */
	String setSnapshotFileName(Integer eventLogID,String snapshotFileName,String userName) throws Exception;
	/**
	 * Interface method to get snapshot file name corresponding to event log.
	 *
	 * @param eventLogID
	 *            the event log ID which needs to be altered
	* @param userName
	 *            the logged in user
	 * @return the string for Snapshot FIle Name
	 * @throws Exception
	 *             the exception
	 */
	String getSnapshotFileName(Integer eventLogID,String userName) throws Exception;
	/**
	 * Interface method to get list of intraop event log correspondin to case id , patient id and surgery name.
	 *
	  * @param surgeryName
	 *            the surgery name for which the event list is required
	 * @param patientID
	 *            the patientID
	 * @param patientCaseID
	 *            patient case id
	 * @param userName
	 *            the logged in user
	 * @throws Exception
	 *             the exception
	 */
	List<IntraopEventlog> getIntraopEventLogList(int patientID, Long caseID, String surgeryName, String userName) throws Exception;
}

