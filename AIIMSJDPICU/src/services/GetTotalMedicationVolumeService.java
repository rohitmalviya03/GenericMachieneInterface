/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package services;

import com.cdac.common.pojoClasses.Volume;
import com.cdac.common.util.DAOFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.UserSession;
public class GetTotalMedicationVolumeService extends Service<Void> {

	private static String medicationName;
	private static int patientID;
	private static long caseID;
	private Volume totalVolume = new Volume();
	
	private static GetTotalMedicationVolumeService instance = null;

	public static GetTotalMedicationVolumeService getInstance(String medicationNameVal, int patientIDVal, long caseIDVal)
	{
		if(instance == null)
		{
			instance = new GetTotalMedicationVolumeService();
		}
		medicationName = medicationNameVal;
		patientID = patientIDVal;
		caseID = caseIDVal;
		return instance;
	}

	private GetTotalMedicationVolumeService() {
		super();
	}

	public Volume getTotalVolume() {
		return totalVolume;
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
					totalVolume = DAOFactory.medicationLog().getTotalDosage(medicationName, patientID, caseID,  UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
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
