/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package services;

import java.util.Date;

import com.cdac.common.GeneratedEntities.IntraopMedicationlog;
import com.cdac.common.util.DAOFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.UserSession;

public class GetIntraopMedicationlogService extends Service<Void> {
	private IntraopMedicationlog intraopMedicationlog;
	private static long caseId;
	private static int patientId;
	private static Date timeStamp;
	private static String medName;
	private static String type;



	public IntraopMedicationlog getIntraopMedicationlog() {
		return intraopMedicationlog;
	}


	private static GetIntraopMedicationlogService instance = null;

	public static GetIntraopMedicationlogService getInstance(String medNameVal,long caseIdVal,int patientIdVal,Date timeStampVal, String medType) {
		if (instance == null) {
			instance = new GetIntraopMedicationlogService();
		}
		medName=medNameVal;
		caseId = caseIdVal;
		patientId = patientIdVal;
		timeStamp = timeStampVal;
		type = medType;
		return instance;
	}

	private GetIntraopMedicationlogService() {
		super();
	}



	@Override
	protected Task<Void> createTask() {
		// TODO Auto-generated method stub
		return new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				// TODO Auto-generated method stub

				try
				{
					intraopMedicationlog=	DAOFactory.medicationLog().getIntraopMedicationlog(medName,caseId,patientId,timeStamp,type, UserSession.getInstance().getUserWithrRolesForUserAuthentication().getUserID());
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
