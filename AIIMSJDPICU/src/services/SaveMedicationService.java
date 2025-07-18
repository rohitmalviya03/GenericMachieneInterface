/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package services;

import com.cdac.common.GeneratedEntities.IntraopMedicationlog;
import com.cdac.common.util.DAOFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.UserSession;
public class SaveMedicationService extends Service<Void>
{

	private static IntraopMedicationlog medicationLog;
	private IntraopMedicationlog returnedMedicationLog;
	
	
	private static SaveMedicationService instance = null;

	public static SaveMedicationService getInstance(IntraopMedicationlog medicationLogVal)
	{
		if(instance == null)
		{
			instance = new SaveMedicationService();
		}
		medicationLog = medicationLogVal;
		return instance;
	}

	private SaveMedicationService() {
		super();
	}
	

	public IntraopMedicationlog getReturnedMedicationLog()
	{
		return returnedMedicationLog;
	}

	@Override
	protected Task<Void> createTask()
	{
		// TODO Auto-generated method stub
		return new Task<Void>()
		{

			@Override
			protected Void call() throws Exception
			{
				// call DB method to save new patient details

				try
				{
					returnedMedicationLog = DAOFactory.medicationLog().saveMediactions(medicationLog,
					        UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
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
