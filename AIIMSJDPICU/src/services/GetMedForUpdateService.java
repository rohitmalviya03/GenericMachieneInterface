/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package services;

import org.apache.log4j.Logger;

import com.cdac.common.GeneratedEntities.IntraopMedicationlog;
import com.cdac.common.util.DAOFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.UserSession;
public class GetMedForUpdateService extends Service<Void>
{
	private static final Logger LOG = Logger.getLogger(GetMedForUpdateService.class);
	private static long medicationLogId;
	private IntraopMedicationlog medicationLog;
	
	private static GetMedForUpdateService instance = null;

	public static GetMedForUpdateService getInstance(long medicationLogIdVal) {
		if (instance == null) {
			instance = new GetMedForUpdateService();
		}
		medicationLogId = medicationLogIdVal;
		return instance;
	}

	private GetMedForUpdateService() {
		super();
	}

	public IntraopMedicationlog getMedicationLog()
	{
		return medicationLog;
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
					medicationLog = DAOFactory.medicationLog().getMedicationLogForUpdate(medicationLogId,
					        UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
				}
				catch (Exception e)
				{
					// TODO: handle exception
					LOG.error("## Exception occured:", e);
					e.printStackTrace();
				}

				return null;
			}

		};
	}
}
