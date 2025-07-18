/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package services;


import java.util.List;

import com.cdac.common.GeneratedEntities.EventLogEventStatus;
import com.cdac.common.util.DAOFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class EventLogService  extends Service<Void> {


	private static String surgeryID;

	private static int patientID;

	private static Long patientCaseID;


	private List<EventLogEventStatus> eventLogDetails;
	
	private static EventLogService instance = null;

	public static EventLogService getInstance(String surgeryIDVal, int patientIDVal, Long patientCaseIDVal)
	{
		if(instance == null)
		{
			instance = new EventLogService();
		}
		surgeryID = surgeryIDVal;
		patientID = patientIDVal;
		patientCaseID = patientCaseIDVal;
		return instance;
	}

	private EventLogService() {
		super();
	}


	public List<EventLogEventStatus> getEventLogDetails() {
		return eventLogDetails;
	}


	@Override
	protected Task<Void> createTask() {
		// TODO Auto-generated method stub
		return new Task<Void>()
		{

			@Override
			protected Void call() throws Exception
			{
				// call DB method to save new patient details


				try
				{
					eventLogDetails = DAOFactory.getEventLog().getAllEvents(patientID,patientCaseID,surgeryID,
					        System.getProperty("user.name"));

				}
				catch (Exception e) 
				{
					throw e;
				}


				return null;
			}

		};
	}


}
