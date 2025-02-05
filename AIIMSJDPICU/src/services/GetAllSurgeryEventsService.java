/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package services;

import java.util.List;

import com.cdac.common.GeneratedEntities.SurgeryEventStatus;
import com.cdac.common.util.DAOFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.UserSession;
public class GetAllSurgeryEventsService extends Service<Void>
{

	private List<SurgeryEventStatus> surgeryEventsList;
	private static String speciality;
	private static int patientID;
	private static long patientCaseID;

	private static GetAllSurgeryEventsService instance = null;

	public static GetAllSurgeryEventsService getInstance(String specialityVal, int patientIDVal, long patientCaseIDVal)
	{
		if(instance == null)
		{
			instance = new GetAllSurgeryEventsService();
		}
		speciality = specialityVal;
		patientID = patientIDVal;
		patientCaseID = patientCaseIDVal;
		return instance;
	}

	private GetAllSurgeryEventsService() {
		super();
	}

	public List<SurgeryEventStatus> getSurgeryEventsList()
	{
		return surgeryEventsList;
	}

	@Override
	protected Task<Void> createTask()
	{
		return new Task<Void>()
		{

			@Override
			protected Void call() throws Exception
			{
				try
				{
					surgeryEventsList = DAOFactory.getEventLog().getAllSurgeryEvents(speciality, patientID,
					        patientCaseID, UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
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
