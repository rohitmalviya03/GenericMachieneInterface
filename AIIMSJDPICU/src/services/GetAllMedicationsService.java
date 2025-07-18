/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package services;

import java.util.List;

import org.apache.log4j.Logger;

import com.cdac.common.GeneratedEntities.IntraopMedicationlog;
import com.cdac.common.util.DAOFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.UserSession;
public class GetAllMedicationsService extends Service<Void>
{
	private static final Logger LOG = Logger.getLogger(GetAllMedicationsService.class);
	private static int patientID;
	private static long caseID;
	private List<IntraopMedicationlog> allMediactionsList;
	
	private static GetAllMedicationsService instance = null;

	public static GetAllMedicationsService getInstance(int patientIDVal, Long caseIDVal)
	{
		if(instance == null)
		{
			instance = new GetAllMedicationsService();
		}
		patientID = patientIDVal;
		caseID = caseIDVal;
		return instance;
	}

	private GetAllMedicationsService() {
		super();
	}

	public List<IntraopMedicationlog> getAllMediactionsList()
	{
		return allMediactionsList;
	}

	@Override
	protected Task<Void> createTask()
	{
		return new Task<Void>()
		{

			@Override
			protected Void call() throws Exception
			{
				// call DB method to save new patient details

				try
				{
					allMediactionsList = DAOFactory.medicationLog().getAllMedicationsForPatientCase(caseID,
					        UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
				}
				catch (Exception e)
				{
					e.printStackTrace();
					LOG.error("## Exception occured:", e);
				}

				return null;
			}

		};
	}
}
