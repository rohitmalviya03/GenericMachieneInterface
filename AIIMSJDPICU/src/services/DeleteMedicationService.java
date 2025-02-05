/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package services;

import org.apache.log4j.Logger;

import com.cdac.common.util.DAOFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.UserSession;
public class DeleteMedicationService extends Service<Void>
{

	private static final Logger LOG = Logger.getLogger(DeleteMedicationService.class);
	private static long medicationLogID;
	private String result;
	
	private static DeleteMedicationService instance = null;

	public static DeleteMedicationService getInstance(long medicationLogIDVal)
	{
		if(instance == null)
		{
			instance = new DeleteMedicationService();
		}
		medicationLogID = medicationLogIDVal;
		return instance;
	}

	private DeleteMedicationService() {
		super();
	}


	public String getResult()
	{
		return result;
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
					result = DAOFactory.medicationLog().deleteMedicationLog(medicationLogID,
					        UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
				}
				catch (Exception e)
				{
					// TODO: handle exception
					e.printStackTrace();
					LOG.error("## Exception occured:", e);
				}

				return null;
			}

		};
	}
}
